package test;

import java.util.HashMap;
import java.util.Map;
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
	Map<String,Integer> items = new HashMap<String,Integer>();


	public static void main(String args[]) {
		junit.textui.TestRunner.run(suite());
	}

	public static TestSuite suite ( ) {
		return new TestSuite(NewCashierTest.class);
	}

	public void setUp() throws Exception{
		super.setUp();          
		gui = new RestaurantGui();
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
		items.put("Steak", 2);
		items.put("Chicken", 3);
	}      
	
	public void testOneMarketBill() {
		cashier.msgIsActive();
		cashier.pickAndExecuteAnAction();
		assertTrue("cashier log reads: " + cashier.log.toString(),
				cashier.log.containsString("in clock in"));
		assertTrue(cashier.getMarketBills().size() == 0);
		
		cashier.msgHereIsWhatIsDue(100.0, items); 
		assertTrue("cashier log reads: " + cashier.log.toString(),
				cashier.log.containsString("Received msgHereIsWhatIsDue with price 100.0"));
		assertTrue(cashier.getMarketBills().size() == 1);
		assertTrue(cashier.getMarketBills().get(0).checkReceived);
		assertFalse(cashier.getMarketBills().get(0).itemsReceived);
		cashier.pickAndExecuteAnAction();
		
		// cashier shouldn't be doing anything
		assertTrue("cashier log reads: " + cashier.log.toString(),
				cashier.log.containsString("Received msgHereIsWhatIsDue with price 100.0"));

		
		cashier.msgGotMarketOrder(items);
		assertTrue(cashier.getMarketBills().size() == 1);
		assertTrue(cashier.getMarketBills().get(0).checkReceived);
		assertTrue(cashier.getMarketBills().get(0).itemsReceived);
		cashier.pickAndExecuteAnAction();
		
		assertTrue("cashier log reads: " + cashier.log.toString(),
				cashier.log.containsString("In action payMarket, amount due is 100"));
		assertEquals(cashier.getMyMoney(),0.0);
		mmc.msgHereIsPayment(100000.0, items, cashier);
		assertTrue("mock market cashier log reads: " + mmc.log.toString(),
				mmc.log.containsString("received msgHereIsPayment from cashier"));
		
		cashier.msgHereIsChange(99900.0);
		cashier.pickAndExecuteAnAction();
		assertEquals(cashier.getMyMoney(),99900.0);

	}
	
}
