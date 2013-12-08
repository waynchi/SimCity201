package test;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Timer;

import market.test.MockMarketCashier;
import market.test.MockMarketEmployee;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import restaurant.CashierRole;
import restaurant.gui.RestaurantGui;
import restaurant.test.mock.MockCook;
import restaurant.test.mock.MockHost;
import restaurant.test.mock.MockPeople;
import restaurant.test.mock.MockTeller;
import restaurant.test.mock.MockWaiter;

public class NewCashierTest extends TestCase{
	CashierRole cashier;
	MockTeller teller;
	MockCook cook;
	MockWaiter waiter;
	MockPeople people;
	MockMarketEmployee mme;
	MockMarketCashier mmc;
	RestaurantGui gui;
	MockHost host;
	Map<String,Integer> items1 = new HashMap<String,Integer>();
	Map<String,Integer> items2 = new HashMap<String,Integer>();


	public static void main(String args[]) {
		junit.textui.TestRunner.run(suite());
	}

	public static TestSuite suite ( ) {
		return new TestSuite(NewCashierTest.class);
	}

	public void setUp() throws Exception{
		super.setUp();          
		Timer timer = new Timer(20, null);
		gui = new RestaurantGui(timer);
		cashier = new CashierRole(gui);
		cashier.inTest = true;
		host = new MockHost("host");
		cashier.setHost(host);
		teller = new MockTeller("teller");
		cook = new MockCook("cook");
		waiter = new MockWaiter("waiter");
		people = new MockPeople("people");
		people.addRole(cashier, "restaurantCashierRole");
		cashier.setPerson(people);
		cashier.setTeller(teller);
		mme = new MockMarketEmployee("employee");
		mme.setCashier(mmc);
		mmc = new MockMarketCashier("customer");
		items1.put("Steak", 2);
		items1.put("Chicken", 3);
		items2.put("Steak", 2);
		items2.put("Chicken", 2);
	}      
	
	public void testOneMarketBill_getBillFirst() {
		cashier.msgIsActive();
		cashier.pickAndExecuteAnAction();
		assertTrue("cashier should be in action clockIn but it's not. Instead, cashier log reads: " + cashier.log.toString(),
				cashier.log.containsString("in clock in"));
		assertTrue("the market bill list should be empty but it's not", 
				cashier.getMarketBills().size() == 0);
		
		cashier.msgHereIsWhatIsDue(100.0, items1, 1); 
		assertTrue("cashier should have received msgHereIsWhatIsDue but it doesn't. instead cashier log reads: " + cashier.log.toString(),
				cashier.log.containsString("Received msgHereIsWhatIsDue with price 100.0 and order number is 1"));
		assertTrue("the market bill list should have size 1 but it doesn't. instead the size is " + cashier.getMarketBills().size(),
				cashier.getMarketBills().size() == 1);
		assertTrue("checkReceived should be true for the market bill just received, but it's not",
				cashier.getMarketBills().get(0).checkReceived);
		assertFalse("itemsReceived should be false for the market bill just received, but it's not",
				cashier.getMarketBills().get(0).itemsReceived);
		cashier.pickAndExecuteAnAction();
		
		// cashier shouldn't be doing anything
		assertTrue("scheduler should not pick anything so the log should be the same but it's not. instead"
				+ " cashier log reads: " + cashier.log.toString(),
				cashier.log.containsString("Received msgHereIsWhatIsDue with price 100.0 and order number is 1"));

		
		cashier.msgGotMarketOrder(items2, 1); // different items shouldn't matter, cashier checks the order number only
		assertTrue("market size should still be 1 but it's not. the size is " + cashier.getMarketBills().size(),
				cashier.getMarketBills().size() == 1);
		assertTrue("checkReceived should be true for the market bill just received, but it's not",
				cashier.getMarketBills().get(0).checkReceived);
		assertTrue("itemsReceived should be true for the market bill just received, but it's not",
				cashier.getMarketBills().get(0).itemsReceived);
		cashier.pickAndExecuteAnAction();
		
		assertTrue("cashier has verified the market bill and should be paying it, but it's not. "
				+ "instead the cashier log reads: " + cashier.log.toString(),
				cashier.log.containsString("In action payMarket, amount due is 100"));
		assertEquals("cashier gives every penny to the market cashier so its money should be 0, but it's not. "
				+ "instead the money is " + cashier.getMyMoney(),
				cashier.getMyMoney(),0.0);
		
		mmc.msgHereIsPayment(100000.0, items1, cashier);
		assertTrue("mock market cashier log reads: " + mmc.log.toString(),
				mmc.log.containsString("received msgHereIsPayment from cashier"));
		
		cashier.msgHereIsChange(99900.0);
		cashier.pickAndExecuteAnAction();
		assertEquals(cashier.getMyMoney(),99900.0);

	}
	
}
