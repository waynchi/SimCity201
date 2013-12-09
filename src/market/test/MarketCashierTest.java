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

		mmc = new MockMarketCustomer("mmc");
		restaurantCashier = new MockCashier("mcashier");
		items.put("Car", 1);
		items2.put("Steak", 3);
		items2.put("Salad", 5);
		cashier.inTest = true;
	}
	
	public void testOpeningAndClosing (){
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
	
	public void testBankInteraction() {
		
	}
	
	public void testBankClosing (){
		
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
		
		cashier.msgHereIsPayment(100.0, items2, restaurantCashier);
		assertTrue("check state should now be paid but it's not.",
				cashier.checks.get(0).getState())
		
	}
	
}
