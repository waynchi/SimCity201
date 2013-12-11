package market.test;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Timer;

import market.MarketCustomerRole;
import market.gui.MarketGui;
import people.PeopleAgent;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class MarketCustomerTest extends TestCase{
	PeopleAgent people;
	MarketCustomerRole customer;
	MockMarketCashier mmc;
	MockMarketEmployee mme;
	Map<String, Integer> items = new HashMap<String, Integer>();
	MarketGui gui;
	Timer timer;
	
	
	
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
		timer = new Timer(20, null);
		gui = new MarketGui(timer);
		customer = new MarketCustomerRole(gui);
		mmc = new MockMarketCashier("mmc");
		people.addRole(customer, "MarketCustomerRole");
		customer.setPerson(people);
		mme = new MockMarketEmployee("mme");
		customer.setEmployee(mme);
		customer.setPerson(people);
	}
	
	public void test (){
		customer.inTest = true;
		assertFalse(customer.isActive());
		customer.msgIsActive();
		assertEquals(customer.getState(),"IN_MARKET");
		
		customer.pickAndExecuteAnAction();
		assertTrue("customer should be in action order item, but it's not. log reads " + customer.log.toString(),
				customer.log.containsString("ordering my items"));
		
		assertTrue(mme.log.containsString("received msgHereIsAnOrder from market customer"));
		
		customer.msgHereIsYourOrder(items);
		customer.pickAndExecuteAnAction();
		assertEquals(customer.getState(),"WAITING_FOR_CHECK");
		
		customer.msgHereIsWhatIsDue(10000.0, mmc);
		assertEquals("customer state is "+customer.getState(), customer.getState(),"WAITING_FOR_CHECK");
		assertEquals(customer.getEvent(),"RECEIVED_CHECK");
		customer.pickAndExecuteAnAction();
		assertTrue("log reads "+ customer.log.toString(),customer.log.containsString("making payment to market cashier"));
		assertEquals(people.getMoney(),0.0);
		
		assertTrue(mmc.log.containsString("received msgHereIsACheck from customer"));
		
		customer.msgHereIsChange(90000.0);
		assertEquals(people.getMoney(),90000.0);
		assertEquals(customer.getEvent(),"RECEIVED_CHANGE");

		customer.pickAndExecuteAnAction();
		
		assertTrue(customer.log.containsString("done and leaving"));

	}

}
