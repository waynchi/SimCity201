package market.test;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Timer;

import market.MarketCashierRole;
import market.gui.MarketGui;
import market.test.MockPeople.MyMarket;
import restaurant.test.mock.MockCashier;
import restaurant.test.mock.MockCook;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class MarketCashierTest extends TestCase{
	MockPeople people;
	MyMarket market;
	MarketCashierRole cashier;
	MockTeller teller;
	MockMarketEmployee mme;
	MockMarketCustomer mmc;
	MockCook mcook;
	MockCashier restaurantCashier ;
	Map<String, Integer> items = new HashMap<String, Integer>();
	Map<String, Integer> items2 = new HashMap<String, Integer>();
	Timer timer;
	MarketGui gui;
	
	
	public static void main(String args[]) {
		junit.textui.TestRunner.run(suite());
	}

	public static TestSuite suite ( ) {
		return new TestSuite(MarketCashierTest.class);
	}

	public void setUp() throws Exception{
		super.setUp();  
		timer = new Timer(20,null);
		gui = new MarketGui(timer);
		people = new MockPeople("people");
		market = people.new MyMarket();
		people.markets.add(market);
		cashier = new MarketCashierRole(gui);
		cashier.setPerson(people);
		teller = new MockTeller("teller");
		mme = new MockMarketEmployee("mme");
		cashier.setMarketEmployee(mme);
		cashier.setTeller(teller);
		mme.setCashier(cashier);

		mmc = new MockMarketCustomer("mmc");
		restaurantCashier = new MockCashier("mcashier");
		items.put("Car", 1);
		items2.put("Steak", 3);
		items2.put("Salad", 5);
		cashier.inTest = true;
	}
	
	// market is still open when market cashier leaves work
	public void testOpeningAndLeavingWork (){
		//check precondition
		assertFalse(cashier.isActive());
		cashier.msgIsActive();
		cashier.pickAndExecuteAnAction();
		assertTrue(cashier.log.containsString("clock in"));
		assertFalse(cashier.turnActive);
		
		cashier.msgIsInActive();
		cashier.pickAndExecuteAnAction();
		assertTrue(cashier.log.containsString("in action leave work"));
		assertFalse(cashier.isActive());
		assertFalse(cashier.leaveWork);
	}
	
	// market is closed but bank is open when market cashier leaves work
	public void testCloseMarketWithBank () {
		//check precondition
				assertFalse(cashier.isActive());
				cashier.msgIsActive();
				cashier.pickAndExecuteAnAction();
				assertTrue(cashier.log.containsString("clock in"));
				assertFalse(cashier.turnActive);
				assertTrue("market cashier should be added to the workers list of employee, and the size of list shoud be 1 now but "
						+ "instead it's " + mme.getWorkers().size(),
						mme.getWorkers().size()==1);
				
				cashier.msgIsInActive();
				people.getMyMarket(0).isClosed = true;
				cashier.pickAndExecuteAnAction();
				assertTrue("market cashier shoule be in action prepare to close but it's not. the log reads " + cashier.log.toString(),
						cashier.log.containsString("In action prepareToClose"));
				assertEquals("total salary for workers should be 200.0, but instead it's " + cashier.getTotalSalary(),
						cashier.getTotalSalary(),200.0);
				assertTrue("cashier has enough money to pay everyone and should be paying, but it's not",
						cashier.log.containsString("paying everybody"));
				assertEquals("working capital should be 10000-200 = 9800 but it's not. Instead it's " + cashier.working_capital,
						cashier.working_capital, 9800.0);
				assertEquals("the mock people of cahsier should now get paid and have 1200 in total but it doesn't",
						people.getMoney(), 1200.0);
				
				//check if mock teller has received the correct message
				assertTrue("teller should have received message need help from market cashier, but it doesn't. The log reads " + teller.log.toString(),
						teller.log.containsString("received message need help from cashier people"));
				
	}
	
	public void testBankInteraction() {
		
	}
	
	public void testBankClosed (){
		
	}
	
	public void testOneCustomerCheck (){
		cashier.msgIsActive();
		cashier.pickAndExecuteAnAction();
		
		assertEquals(cashier.checks.size(),0);
		cashier.msgHereIsACheck(mmc, items);
		assertEquals(cashier.checks.size(),1);
		
		cashier.pickAndExecuteAnAction();
		assertTrue("cahsier should be sending a check to customer but it's not. log reads " + cashier.log.toString(),
				cashier.log.containsString("sending check to customer and total due is 20000.0"));
		assertEquals(cashier.checks.get(0).totalDue, 20000.0);
		
		//check if message is sent to mock customer
		assertTrue(mmc.log.containsString("received msgHereIsWhatIsDue from market cashier"));
		
		//make customer send message
		assertEquals(cashier.working_capital,10000.0);
		cashier.msgHereIsPayment(mmc, 100000.0);
		cashier.pickAndExecuteAnAction();
		assertTrue("cashier log reads: " + cashier.log.toString(),
				cashier.log.containsString("giving change to customer and the amount is 80000"));
		assertEquals(cashier.working_capital,30000.0);
	}
		
	public void testOneRestaurantCheck() {
		cashier.msgIsActive();
		cashier.pickAndExecuteAnAction();
		
		assertEquals(cashier.checks.size(),0);
		cashier.msgHereIsACheck(restaurantCashier, items2, 1);
		assertEquals(cashier.checks.size(),1);
		cashier.pickAndExecuteAnAction();
		
		assertTrue("cashier should be computing and sending bill to restaurant cashier, but it's not. log reads " + cashier.log.toString(),
				cashier.log.containsString("sending check to restaurant cashier mcashier and total due is 38.92"));
		
		//verify the message sent
		assertTrue("restaurant cashier should have received a bill from market cashier, but it's not. log reads " + restaurantCashier.log.toString(),
				restaurantCashier.log.containsString("got a market bill with price 38.92 and order number 1"));
		
		cashier.msgHereIsPayment(100.0, 1, restaurantCashier);
		assertTrue("check state should now be paid but it's not.",
				cashier.checks.get(0).getState().equals("PAID"));
		
		cashier.pickAndExecuteAnAction();
		assertTrue("cashier should be giving change to restaurant cashier but it's not",
				cashier.log.containsString("giving change to restaurant cashier and the amount is 61.08"));
		
		//check if restaurant cashier has got the correct message
		assertTrue("restaurant cashier should have got the change but it hasn't. log reads " + restaurantCashier.log.toString(),
				restaurantCashier.log.containsString("Received msgHereIsChange, change is 61.08"));
	}
	
}
