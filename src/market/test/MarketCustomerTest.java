package market.test;

import java.util.HashMap;
import java.util.Map;

import market.MarketCashierRole;
import market.MarketCustomerRole;
import market.gui.MarketGui;
import people.PeopleAgent;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import restaurant.test.mock.LoggedEvent;
import restaurant.test.mock.MockCashier;
import restaurant.test.mock.MockCook;
import test.NewCashierTest;

public class MarketCustomerTest extends TestCase{
	PeopleAgent people;
	MarketCustomerRole customer;
	MockMarketCashier mmc;
	MockMarketEmployee mme;
	Map<String, Integer> items = new HashMap<String, Integer>();
	MarketGui gui;
	
	
	
	public static void main(String args[]) {
		junit.textui.TestRunner.run(suite());
	}

	public static TestSuite suite ( ) {
		return new TestSuite(MarketCustomerTest.class);
	}

	public void setUp() throws Exception{
		super.setUp();  
		people = new PeopleAgent("people", 0.0, false);
		people.setMoney(100000.0);
		gui = new MarketGui();
		customer = new MarketCustomerRole(gui);
		mmc = new MockMarketCashier("mmc");
		people.addRole(customer, "MarketCustomerRole");
		customer.setPerson(people);
		mme = new MockMarketEmployee("mme");
		customer.setEmployee(mme);
	}
	
	public void test (){
		customer.inTest = true;
		assertFalse(customer.isActive());
		customer.msgIsActive();
		customer.pickAndExecuteAnAction();
		assertTrue(customer.log.containsString("in action order item"));
		
		assertTrue(mme.log.containsString("received msgHereIsAnOrder from market customer"));
		
		customer.msgHereIsYourOrder(items);
		customer.pickAndExecuteAnAction();
		assertEquals(customer.getState(),"WAITING_FOR_CHECK");
		
		customer.msgHereIsWhatIsDue(10000.0, mmc);
		assertEquals("customer state is "+customer.getState(), customer.getState(),"WAITING_FOR_CHECK");
		assertEquals(customer.getEvent(),"RECEIVED_CHECK");
		customer.pickAndExecuteAnAction();
		assertTrue("log reads "+ customer.log.toString(),customer.log.containsString("in action pay bill"));
		assertEquals(people.getMoney(),0.0);
		
		assertTrue(mmc.log.containsString("received msgHereIsACheck from customer"));
		
		customer.msgHereIsChange(90000.0);
		assertEquals(people.getMoney(),90000.0);
		customer.pickAndExecuteAnAction();
		
		assertTrue(customer.log.containsString("in action pay bill"));

	}

}
