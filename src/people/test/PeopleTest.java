package people.test;



import java.util.ArrayList;
import java.util.List;

import city.gui.CityGui;
import people.PeopleAgent;
import people.Role;
import restaurant.CashierRole;
import restaurant.CookRole;
import restaurant.HostRole;
import restaurant.NormalWaiterRole;
import restaurant.RestaurantCustomerRole;
import restaurant.gui.RestaurantPanel;
import restaurant.gui.RestaurantPanel.CookWaiterMonitor;
import restaurant.test.mock.MockCustomer;
import junit.framework.*;
/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * @author Monroe Ekilah
 */
public class PeopleTest extends TestCase
{
	List<PeopleAgent> people;
	PeopleAgent cook;
	PeopleAgent waiter;
	PeopleAgent host;
	PeopleAgent cashier;
	PeopleAgent customer;
	CookWaiterMonitor theMonitor;
	RestaurantPanel restPanel;
	//these are instantiated for each test separately via the setUp() method.
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		people = new ArrayList<PeopleAgent>();
		cook = new PeopleAgent("Gordon", 100, false);
		waiter = new PeopleAgent("Waiter", 50, false);
	    customer = new PeopleAgent("Customer", 50, false);
		host = new PeopleAgent("Host", 200, false);
		cashier = new PeopleAgent("Cashier", 300, false);
		people.add(cook);
		people.add(waiter);
		people.add(customer);
		people.add(host);
		people.add(cashier);
		restPanel = new RestaurantPanel();
		theMonitor = restPanel.theMonitor;
		
		//person.addRole(new )
	}	
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */
	public void testRestaurantScenario()
	{
		//Adding Roles into all of the People
		
		cook.addRole((Role)new RestaurantCustomerRole(cook.name), "RestaurantCustomer");
		assertTrue("Testing Role addition", cook.log.getLastLoggedEvent().toString().contains("Role added: RestaurantCustomer"));
		cook.addRole((Role)new CookRole(cook.name, theMonitor), "RestaurantCook");
		assertTrue("Testing Role addition", cook.log.getLastLoggedEvent().toString().contains("Role added: RestaurantCook"));
		
		waiter.addRole((Role)new RestaurantCustomerRole(waiter.name), "RestaurantCustomer");
		assertTrue("Testing Role addition", waiter.log.getLastLoggedEvent().toString().contains("Role added: RestaurantCustomer"));
		waiter.addRole((Role)new NormalWaiterRole(waiter.name), "RestaurantNormalWaiter");
		assertTrue("Testing Role addition", waiter.log.getLastLoggedEvent().toString().contains("Role added: RestaurantNormalWaiter"));
		
		customer.addRole((Role)new RestaurantCustomerRole(cook.name), "RestaurantCustomer");
		assertTrue("Testing Role addition", customer.log.getLastLoggedEvent().toString().contains("Role added: RestaurantCustomer"));
		customer.addRole((Role)new CookRole(cook.name, theMonitor), "RestaurantCook");
		assertTrue("Testing Role addition", customer.log.getLastLoggedEvent().toString().contains("Role added: RestaurantCook"));
		
		host.addRole((Role)new RestaurantCustomerRole(cook.name), "RestaurantCustomer");
		assertTrue("Testing Role addition", host.log.getLastLoggedEvent().toString().contains("Role added: RestaurantCustomer"));
		host.addRole((Role)new HostRole(host.name), "RestaurantHost");
		assertTrue("Testing Role addition", host.log.getLastLoggedEvent().toString().contains("Role added: RestaurantHost"));
		
		cashier.addRole((Role)new RestaurantCustomerRole(cook.name), "RestaurantCustomer");
		assertTrue("Testing Role addition", cashier.log.getLastLoggedEvent().toString().contains("Role added: RestaurantCustomer"));
		cashier.addRole((Role)new CashierRole(cashier.name), "RestaurantCashier");
		assertTrue("Testing Role addition", cashier.log.getLastLoggedEvent().toString().contains("Role added: RestaurantCashier"));
		
		//Adding Jobs
		cook.addJob("RestaurantCook", 1200, 1800);
		assertTrue("Testing Job addition", cook.log.getLastLoggedEvent().toString().contains("Job added: RestaurantCook"));
		
		waiter.addJob("RestaurantNormalWaiter", 1200, 1800);
		assertTrue("Testing Job addition", waiter.log.getLastLoggedEvent().toString().contains("Job added: RestaurantNormalWaiter"));
		
		customer.addJob("RestaurantCook", 5000, 50000);
		assertTrue("Testing Job addition", customer.log.getLastLoggedEvent().toString().contains("Job added: RestaurantCook"));
		
		host.addJob("RestaurantHost" , 1100, 1900);
		assertTrue("Testing Job addition", host.log.getLastLoggedEvent().toString().contains("Job added: RestaurantHost"));
		
		cashier.addJob("RestaurantCashier", 1200, 1800);
		assertTrue("Testing Job addition", cashier.log.getLastLoggedEvent().toString().contains("Job added: RestaurantCashier"));
		
		//Sending message to people!
		//Wake up call!
		
		for(PeopleAgent p: people)
		{
			assertTrue("Make sure Initial state is sleeping!", p.getAgentState().equals("Sleeping"));
			p.msgTimeIs(800);
			assertTrue("Testing TimeIs", p.log.getLastLoggedEvent().toString().contains("Waking Up In Message"));
			assertTrue("Testing Scheduler", p.pickAndExecuteAnAction());
			assertTrue("Testing Scheduler Log", p.log.getLastLoggedEvent().toString().contains("Waking Up In Scheduler. New State is Idle"));
		}
		
		

		
		for(PeopleAgent p: people)
		{
			if(p == host)
			{
				p.msgTimeIs(1100);
				assertTrue("Testing TimeIs", p.log.getLastLoggedEvent().toString().contains("Going To Work"));
				assertTrue("Testing Scheduler", p.pickAndExecuteAnAction());
				assertTrue("Testing to see if scheduler changed state", p.log.getLastLoggedEvent().toString().contains("Going To Work. New State is Working"));
			}
			else if(p != customer)
			{
				p.msgTimeIs(1200);
				assertTrue("Testing TimeIs", p.log.getLastLoggedEvent().toString().contains("Going To Work"));
				assertTrue("Testing Scheduler", p.pickAndExecuteAnAction());
				assertTrue("Testing to see if scheduler changed state", p.log.getLastLoggedEvent().toString().contains("Going To Work. New State is Working"));
			}
			System.out.println("test");
			
		}
		
		//setUp() runs first before this test!
		/*
		customer.cashier = cashier;//You can do almost anything in a unit test.			
		
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.pendingChecks.size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		//Testing Milestone v2.2A 
		//One Market with one bill
		System.out.println("Testing Market Interactions");
		assertEquals("Cashier should have 0 market bills in it", cashier.MarketBills.size(), 0);
		cashier.msgHereIsMarketBill(new MBill("Steak", 5, 8), market);
		assertFalse("CashierAgent should have something in MarketBills now.",cashier.MarketBills.isEmpty());
		System.out.println(cashier.log.getLastLoggedEvent().toString());
		assertTrue("Testing if msg is sent", cashier.log.getLastLoggedEvent().toString().equals("Received HereIsMarketBill from market. Total = 40.0"));
		assertTrue("Testing Scheduler", cashier.pickAndExecuteAnAction());
		assertTrue("Testing if payment is sent", cashier.log.getLastLoggedEvent().toString().equals("Paid Market back. Total = 40.0"));
		assertTrue("Testing if market recieved message", market.log.getLastLoggedEvent().toString().equals("Recieved message Bill Payment. Total = 40.0"));
		assertTrue("Testing if cashier MBill is now empty", cashier.MarketBills.isEmpty());
		assertFalse("Scheduler testing again", cashier.pickAndExecuteAnAction());*/
		
	
	}
	
	
}
