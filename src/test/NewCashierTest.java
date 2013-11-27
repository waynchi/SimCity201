package test;

import java.util.Vector;

import market.test.MockMarketCashier;
import market.test.MockMarketEmployee;
import people.PeopleAgent;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import restaurant.BaseWaiterRole;
import restaurant.CashierRole;
import restaurant.NormalWaiterRole;
import restaurant.gui.RestaurantGui;
import restaurant.gui.RestaurantPanel;
import restaurant.interfaces.Waiter;
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
		mmc = new MockMarketCashier("customer");
	}      
	
	//scenario: make PeopleAgent a CashierRole, then Cashier will do the opening
	public void testActive() {
		cashier.msgIsActive();
		cashier.pickAndExecuteAnAction();
		assertTrue(cashier.log.containsString("in clock in"));

	}
	
}
