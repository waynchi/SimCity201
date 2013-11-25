package people.test;



import java.util.ArrayList;
import java.util.List;

import city.gui.CityGui;
import city.gui.PersonGui;
import people.PeopleAgent;
import people.PeopleAgent.AgentLocation;
import people.PeopleAgent.HungerState;
import people.Role;
import restaurant.CashierRole;
import restaurant.CookRole;
import restaurant.HostRole;
import restaurant.NormalWaiterRole;
import restaurant.RestaurantCustomerRole;
import restaurant.gui.RestaurantPanel;
import restaurant.gui.RestaurantPanel.CookWaiterMonitor;
import restaurant.test.mock.LoggedEvent;
import people.test.mock.MockBankCustomer;
import people.test.mock.MockCashier;
import people.test.mock.MockCook;
import people.test.mock.MockCustomer;
import people.test.mock.MockHost;
import people.test.mock.MockTeller;
import people.test.mock.MockWaiter;
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
	List<PeopleAgent> RestaurantPeople;
	PeopleAgent cook;
	PeopleAgent waiter;
	PeopleAgent host;
	PeopleAgent restaurantCashier;
	PeopleAgent restaurantCustomer;
	CookWaiterMonitor theMonitor;
	RestaurantPanel restPanel;
	
	
	List<PeopleAgent> BankPeople;
	PeopleAgent teller;
	PeopleAgent bankCustomer;
	
	List<PeopleAgent> MarketPeople;
	PeopleAgent marketCashier;
	PeopleAgent marketEmployee;
	PeopleAgent marketCustomer;
	//these are instantiated for each test separately via the setUp() method.
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		RestaurantPeople = new ArrayList<PeopleAgent>();
		cook = new PeopleAgent("Gordon", 100, false);
		waiter = new PeopleAgent("Waiter", 50, false);
	    restaurantCustomer = new PeopleAgent("Customer", 50, true);
		host = new PeopleAgent("Host", 200, false);
		restaurantCashier = new PeopleAgent("Cashier", 300, false);
		RestaurantPeople.add(cook);
		RestaurantPeople.add(waiter);
		RestaurantPeople.add(restaurantCustomer);
		RestaurantPeople.add(host);
		RestaurantPeople.add(restaurantCashier);
		for(PeopleAgent p: RestaurantPeople)
		{
			p.setTest();
		}

		restPanel = new RestaurantPanel();
		theMonitor = restPanel.theMonitor;
		
		BankPeople = new ArrayList<PeopleAgent>();
		teller = new PeopleAgent("teller", 100, false);
		bankCustomer = new PeopleAgent("restaurantCustomer", 100, false);
		BankPeople.add(teller);
		BankPeople.add(bankCustomer);
		for(PeopleAgent p: BankPeople)
		{
			p.setTest();
		}
		
		MarketPeople = new ArrayList<PeopleAgent>();
		marketCashier = new PeopleAgent("marketCashier", 100, false);
		marketEmployee = new PeopleAgent("marketEmployee", 100, false);
		marketCustomer = new PeopleAgent("marketCustomer", 1000000, false);
		MarketPeople.add(marketCashier);
		MarketPeople.add(marketEmployee);
		MarketPeople.add(marketCustomer);
		for(PeopleAgent p: MarketPeople)
		{
			p.setTest();
		}
		
		//person.addRole(new )
	}	
	
//	public void testMarketScenario()
//	{
//		//AddingRoles
//		marketCashier.addRole((Role)new MockCustomer(marketCashier.name), "RestaurantCustomer");
//		assertTrue("Testing Role addition", marketCashier.log.getLastLoggedEvent().toString().contains("Role added: RestaurantCustomer"));
//		marketCashier.addRole((Role)new MockBankCustomer(marketCashier.name), "BankCustomer");
//		assertTrue("Testing Role addition", marketCashier.log.getLastLoggedEvent().toString().contains("Role added: BankCustomer"));
//		marketCashier.addRole((Role)new MockBankCustomer(marketCashier.name), "MarketCustomer");
//		assertTrue("Testing Role addition", marketCashier.log.getLastLoggedEvent().toString().contains("Role added: MarketCustomer"));
//		marketCashier.addRole((Role)new MockTeller(marketCashier.name), "MarketCashier");
//		assertTrue("Testing Role addition", marketCashier.log.getLastLoggedEvent().toString().contains("Role added: MarketCashier"));
//		
//		marketEmployee.addRole((Role)new MockCustomer(marketEmployee.name), "RestaurantCustomer");
//		assertTrue("Testing Role addition", marketEmployee.log.getLastLoggedEvent().toString().contains("Role added: RestaurantCustomer"));
//		marketEmployee.addRole((Role)new MockBankCustomer(marketEmployee.name), "BankCustomer");
//		assertTrue("Testing Role addition", marketEmployee.log.getLastLoggedEvent().toString().contains("Role added: BankCustomer"));
//		marketEmployee.addRole((Role)new MockBankCustomer(marketEmployee.name), "MarketCustomer");
//		assertTrue("Testing Role addition", marketEmployee.log.getLastLoggedEvent().toString().contains("Role added: MarketCustomer"));
//		marketEmployee.addRole((Role)new MockTeller(marketEmployee.name), "MarketEmployee");
//		assertTrue("Testing Role addition", marketEmployee.log.getLastLoggedEvent().toString().contains("Role added: MarketEmployee"));
//		
//		marketCustomer.addRole((Role)new MockCustomer(marketCustomer.name), "RestaurantCustomer");
//		assertTrue("Testing Role addition", marketCustomer.log.getLastLoggedEvent().toString().contains("Role added: RestaurantCustomer"));
//		marketCustomer.addRole((Role)new MockBankCustomer(marketCustomer.name), "BankCustomer");
//		assertTrue("Testing Role addition", marketCustomer.log.getLastLoggedEvent().toString().contains("Role added: BankCustomer"));
//		marketCustomer.addRole((Role)new MockBankCustomer(marketEmployee.name), "MarketCustomer");
//		assertTrue("Testing Role addition", marketCustomer.log.getLastLoggedEvent().toString().contains("Role added: MarketCustomer"));
//		marketCustomer.addRole((Role)new MockTeller(marketEmployee.name), "MarketEmployee");
//		assertTrue("Testing Role addition", marketCustomer.log.getLastLoggedEvent().toString().contains("Role added: MarketEmployee"));
//		
//		//Adding Jobs
//		marketCashier.addJob("MarketCashier", 1200, 1800);
//		assertTrue("Testing Job addition", marketCashier.log.getLastLoggedEvent().toString().contains("Job added: MarketCashier"));
//					
//		marketEmployee.addJob("MarketEmployee", 1200, 1800);
//		assertTrue("Testing Job addition", marketEmployee.log.getLastLoggedEvent().toString().contains("Job added: MarketEmployee"));
//		
//		marketCustomer.addJob("MarketEmployee", 5000, 500000);
//		assertTrue("Testing Job addition", marketCustomer.log.getLastLoggedEvent().toString().contains("Job added: MarketEmployee"));
//		
//		//Wake Up Call
//		for(PeopleAgent p: MarketPeople)
//		{
//			assertTrue("Make sure Initial state is sleeping!", p.getAgentState().equals("Sleeping"));
//			p.msgTimeIs(800);
//			assertTrue("Testing TimeIs", p.log.getLastLoggedEvent().toString().contains("Waking Up In Message"));
//			assertTrue("Testing Scheduler", p.pickAndExecuteAnAction());
//			assertTrue("Testing Scheduler Log", p.log.getLastLoggedEvent().toString().contains("Waking Up In Scheduler. New State is Idle"));
//		}
//		
//		//Going to Work
//		for(PeopleAgent p: MarketPeople)
//		{
//			p.msgTimeIs(1200);
//			if(p != marketCustomer)
//			{
//				assertTrue("Testing TimeIs", p.log.getLastLoggedEvent().toString().contains("Going To Work"));
//				assertTrue("Testing Scheduler", p.pickAndExecuteAnAction());
//				assertTrue("Testing to see if scheduler changed state", p.log.getLastLoggedEvent().toString().contains("Going To Work. New State is Working"));
//			}
//			else
//			{
//				assertTrue("TestingTimeIs", p.log.getLastLoggedEvent().toString().contains("Going To Buy Car. Event is now: GoingToBuyCar" ));
//				assertTrue("Testing Scheduler", p.pickAndExecuteAnAction());
//				p.Arrived();
//				assertTrue("Testing to see if scheduler changed state", p.log.getLastLoggedEvent().toString().contains("Going To Buy Car. New State is BuyingCar"));
//			}
//				assertFalse("Testing Scheduler", p.pickAndExecuteAnAction());
//		}
//		
//		marketCustomer.msgDone("MarketCustomerRole");
//		assertTrue("Testing to see if scheduler changed state", marketCustomer.log.getLastLoggedEvent().toString().contains("Recieved msgDone"));
//		marketCustomer.Money = 0.0;
//		//Taking them off work
//		for(PeopleAgent p : MarketPeople)
//		{
//			p.msgTimeIs(1800);
//			if (p != marketCustomer)
//			{
//				assertTrue("Testing to see if Leaving work", p.log.getLastLoggedEvent().toString().contains("Leaving Work"));
//				assertTrue("Testing Scheduler", p.pickAndExecuteAnAction());
//				assertTrue("Testing to see if scheduler changed state", p.log.getLastLoggedEvent().toString().contains("Leaving Work. New State is Idle"));
//			}
//			else
//			{
//				System.out.println(p + " " + p.log.getLastLoggedEvent().toString());
//				assertTrue("Testing Scheduler", p.pickAndExecuteAnAction());
//			}
//		}
//		
//		//Sleeping Time!
//		for(PeopleAgent p : MarketPeople)
//		{
//			System.out.println(p + p.state.toString());
//			assertTrue("Make sure Initial state is Idle!", p.getAgentState().equals("Idle"));
//			p.msgTimeIs(2330);
//			System.out.println(p + p.state.toString());
//			System.out.println(p + p.event.toString());
//			assertTrue("Testing TimeIs", p.log.getLastLoggedEvent().toString().contains("Sleeping In Message"));
//			assertTrue("Testing Scheduler", p.pickAndExecuteAnAction());
//			assertTrue("Testing Scheduler Log", p.log.getLastLoggedEvent().toString().contains("Sleeping In Scheduler. New State is Sleeping"));
//			assertFalse("Testing Scheduler", p.pickAndExecuteAnAction());
//		}	
//		
//	}
//	
//	public void testBankScenario()
//	{
//		//AddingRoles
//		teller.addRole((Role)new MockCustomer(teller.name), "RestaurantCustomer");
//		assertTrue("Testing Role addition", teller.log.getLastLoggedEvent().toString().contains("Role added: RestaurantCustomer"));
//		teller.addRole((Role)new MockBankCustomer(teller.name), "BankCustomer");
//		assertTrue("Testing Role addition", teller.log.getLastLoggedEvent().toString().contains("Role added: BankCustomer"));
//		teller.addRole((Role)new MockTeller(teller.name), "Teller");
//		assertTrue("Testing Role addition", teller.log.getLastLoggedEvent().toString().contains("Role added: Teller"));
//		
//		bankCustomer.addRole((Role)new MockCustomer(teller.name), "RestaurantCustomer");
//		assertTrue("Testing Role addition", bankCustomer.log.getLastLoggedEvent().toString().contains("Role added: RestaurantCustomer"));
//		bankCustomer.addRole((Role)new MockBankCustomer(teller.name), "BankCustomer");
//		assertTrue("Testing Role addition", bankCustomer.log.getLastLoggedEvent().toString().contains("Role added: BankCustomer"));
//		bankCustomer.addRole((Role)new MockTeller(teller.name), "Teller");
//		assertTrue("Testing Role addition", bankCustomer.log.getLastLoggedEvent().toString().contains("Role added: Teller"));
//		
//		//Adding Jobs
//		teller.addJob("Teller", 1200, 1800);
//		assertTrue("Testing Job addition", teller.log.getLastLoggedEvent().toString().contains("Job added: Teller"));
//				
//		bankCustomer.addJob("Teller", 5000, 50000);
//		assertTrue("Testing Job addition", bankCustomer.log.getLastLoggedEvent().toString().contains("Job added: Teller"));
//		
//		//Wake Up Call
//		for(PeopleAgent p: BankPeople)
//		{
//			assertTrue("Make sure Initial state is sleeping!", p.getAgentState().equals("Sleeping"));
//			p.msgTimeIs(800);
//			assertTrue("Testing TimeIs", p.log.getLastLoggedEvent().toString().contains("Waking Up In Message"));
//			assertTrue("Testing Scheduler", p.pickAndExecuteAnAction());
//			assertTrue("Testing Scheduler Log", p.log.getLastLoggedEvent().toString().contains("Waking Up In Scheduler. New State is Idle"));
//		}
//		
//		//Going to Work
//		for(PeopleAgent p: BankPeople)
//		{
//			p.msgTimeIs(1200);
//			if(p == teller)
//			{
//				assertTrue("Testing TimeIs", p.log.getLastLoggedEvent().toString().contains("Going To Work"));
//				assertTrue("Testing Scheduler", p.pickAndExecuteAnAction());
//				assertTrue("Testing to see if scheduler changed state", p.log.getLastLoggedEvent().toString().contains("Going To Work. New State is Working"));
//			}
//			else
//			{
//				assertTrue("TestingTimeIs", p.log.getLastLoggedEvent().toString().contains("Retrieving Money. Event is now: GoingToRetrieveMoney" ));
//				assertTrue("Testing Scheduler", p.pickAndExecuteAnAction());
//				assertTrue("Testing to see if scheduler changed state", p.log.getLastLoggedEvent().toString().contains("Going To Bank. New State is GoingToBank"));
//			}
//				assertFalse("Testing Scheduler", p.pickAndExecuteAnAction());
//		}
//		
//		//Taking them off work
//		for(PeopleAgent p : BankPeople)
//		{
//			p.msgTimeIs(1800);
//			if (p == teller)
//			{
//				assertTrue("Testing to see if Leaving work", p.log.getLastLoggedEvent().toString().contains("Leaving Work"));
//				assertTrue("Testing Scheduler", p.pickAndExecuteAnAction());
//				assertTrue("Testing to see if scheduler changed state", p.log.getLastLoggedEvent().toString().contains("Leaving Work. New State is Idle"));
//			}
//			else
//			{
//				assertTrue("Testing to see if scheduler changed state", p.log.getLastLoggedEvent().toString().contains("Going To Bank. New State is GoingToBank"));
//			}
//		}
//		
//		bankCustomer.msgDone("BankCustomerRole");
//		assertTrue("Testing to see if msg is recieved", bankCustomer.log.getLastLoggedEvent().toString().contains("Recieved msgDone"));
//		
//		
//		//Sleeping Time!
//		for(PeopleAgent p : BankPeople)
//		{
//			System.out.println(p + p.state.toString());
//			assertTrue("Make sure Initial state is Idle!", p.getAgentState().equals("Idle"));
//			p.msgTimeIs(2330);
//			System.out.println(p + p.state.toString());
//			System.out.println(p + p.event.toString());
//			assertTrue("Testing TimeIs", p.log.getLastLoggedEvent().toString().contains("Sleeping In Message"));
//			assertTrue("Testing Scheduler", p.pickAndExecuteAnAction());
//			assertTrue("Testing Scheduler Log", p.log.getLastLoggedEvent().toString().contains("Sleeping In Scheduler. New State is Sleeping"));
//			assertFalse("Testing Scheduler", p.pickAndExecuteAnAction());
//		}	
//	}
	
	/**
	 * This tests the restaurantCashier under very simple terms: one restaurantCustomer is ready to pay the exact bill.
	 */
	public void testRestaurantScenario()
	{
		//Adding Roles into all of the People
		
		
		cook.addRole((Role)new MockCustomer(cook.name), "RestaurantCustomer");
		assertTrue("Testing Role addition", cook.log.getLastLoggedEvent().toString().contains("Role added: RestaurantCustomer"));
		cook.addRole((Role)new MockCook(cook.name), "RestaurantCook");
		assertTrue("Testing Role addition", cook.log.getLastLoggedEvent().toString().contains("Role added: RestaurantCook"));
		
		waiter.addRole((Role)new MockCustomer(waiter.name), "RestaurantCustomer");
		assertTrue("Testing Role addition", waiter.log.getLastLoggedEvent().toString().contains("Role added: RestaurantCustomer"));
		waiter.addRole((Role)new MockWaiter(waiter.name), "RestaurantNormalWaiter");
		assertTrue("Testing Role addition", waiter.log.getLastLoggedEvent().toString().contains("Role added: RestaurantNormalWaiter"));
		
		restaurantCustomer.addRole((Role)new MockCustomer(cook.name), "RestaurantCustomer");
		assertTrue("Testing Role addition", restaurantCustomer.log.getLastLoggedEvent().toString().contains("Role added: RestaurantCustomer"));
		restaurantCustomer.addRole((Role)new MockCook(cook.name), "RestaurantCook");
		assertTrue("Testing Role addition", restaurantCustomer.log.getLastLoggedEvent().toString().contains("Role added: RestaurantCook"));
		
		host.addRole((Role)new MockCustomer(cook.name), "RestaurantCustomer");
		assertTrue("Testing Role addition", host.log.getLastLoggedEvent().toString().contains("Role added: RestaurantCustomer"));
		host.addRole((Role)new MockHost(host.name), "RestaurantHost");
		assertTrue("Testing Role addition", host.log.getLastLoggedEvent().toString().contains("Role added: RestaurantHost"));
		
		restaurantCashier.addRole((Role)new MockCustomer(cook.name), "RestaurantCustomer");
		assertTrue("Testing Role addition", restaurantCashier.log.getLastLoggedEvent().toString().contains("Role added: RestaurantCustomer"));
		restaurantCashier.addRole((Role)new MockCashier(restaurantCashier.name), "RestaurantCashier");
		assertTrue("Testing Role addition", restaurantCashier.log.getLastLoggedEvent().toString().contains("Role added: RestaurantCashier"));
		
		//Adding Jobs
		cook.addJob("RestaurantCook", 1200, 1800);
		assertTrue("Testing Job addition", cook.log.getLastLoggedEvent().toString().contains("Job added: RestaurantCook"));
		
		waiter.addJob("RestaurantNormalWaiter", 1200, 1800);
		assertTrue("Testing Job addition", waiter.log.getLastLoggedEvent().toString().contains("Job added: RestaurantNormalWaiter"));
		
		restaurantCustomer.addJob("RestaurantCook", 5000, 50000);
		assertTrue("Testing Job addition", restaurantCustomer.log.getLastLoggedEvent().toString().contains("Job added: RestaurantCook"));
		
		host.addJob("RestaurantHost" , 1100, 1900);
		assertTrue("Testing Job addition", host.log.getLastLoggedEvent().toString().contains("Job added: RestaurantHost"));
		
		restaurantCashier.addJob("RestaurantCashier", 1200, 1800);
		assertTrue("Testing Job addition", restaurantCashier.log.getLastLoggedEvent().toString().contains("Job added: RestaurantCashier"));
		
		//Sending message to RestaurantPeople!
		//Wake up call!
		
		for(PeopleAgent p: RestaurantPeople)
		{
			assertTrue("Make sure Initial state is sleeping!", p.getAgentState().equals("Sleeping"));
			p.msgTimeIs(800);
			assertTrue("Testing TimeIs", p.log.getLastLoggedEvent().toString().contains("Waking Up In Message"));
			assertTrue("Testing Scheduler", p.pickAndExecuteAnAction());
			assertTrue("Testing Scheduler Log", p.log.getLastLoggedEvent().toString().contains("Waking Up In Scheduler. New State is Idle"));
		}
		
		restaurantCustomer.hunger = HungerState.Hungry;

		
		for(PeopleAgent p: RestaurantPeople)
		{
			if(p == host)
			{
				p.msgTimeIs(1100);
				assertTrue("Testing TimeIs", p.log.getLastLoggedEvent().toString().contains("Going To Work"));
				assertTrue("Testing Scheduler", p.pickAndExecuteAnAction());
				assertTrue("Testing to see if scheduler changed state", p.log.getLastLoggedEvent().toString().contains("Going To Work. New State is Working"));
			}
			else if(p != restaurantCustomer)
			{
				p.msgTimeIs(1200);
				assertTrue("Testing TimeIs", p.log.getLastLoggedEvent().toString().contains("Going To Work"));
				assertTrue("Testing Scheduler", p.pickAndExecuteAnAction());
				assertTrue("Testing to see if scheduler changed state", p.log.getLastLoggedEvent().toString().contains("Going To Work. New State is Working"));
			}
			else
			{
				p.msgTimeIs(1200);
				assertTrue("Testing Scheduler", p.pickAndExecuteAnAction());
			}
				assertFalse("Testing Scheduler", p.pickAndExecuteAnAction());
		}
		for(PeopleAgent p : RestaurantPeople)
		{
			if(p == host)
			{
				p.msgTimeIs(1900);
				assertTrue("Testing TimeIs", p.log.getLastLoggedEvent().toString().contains("Leaving Work"));
				assertTrue("Testing Scheduler", p.pickAndExecuteAnAction());
				assertTrue("Testing to see if scheduler changed state", p.log.getLastLoggedEvent().toString().contains("Leaving Work. New State is Idle"));
			}
			else if(p!= restaurantCustomer)
			{
				p.msgTimeIs(1800);
				assertTrue("Testing TimeIs", p.log.getLastLoggedEvent().toString().contains("Leaving Work"));
				assertTrue("Testing Scheduler", p.pickAndExecuteAnAction());
				assertTrue("Testing to see if scheduler changed state", p.log.getLastLoggedEvent().toString().contains("Leaving Work. New State is Idle"));
			}
			else
			{
				p.msgTimeIs(1800);
				if(p.state.toString().equals("EatingAtRestaurant"))
				{
					p.msgDone("RestaurantCustomerRole");
				}
				else
				{
					p.msgDone("HomeEating");
				}
				assertFalse("Testing Scheduler", p.pickAndExecuteAnAction());
			}
			assertFalse("Testing Scheduler", p.pickAndExecuteAnAction());
		}
		
		//Sleeping Time!
		for(PeopleAgent p : RestaurantPeople)
		{
			if(p == restaurantCustomer)
			{
				p.msgDone("RestaurantCustomerRole");
			}
			assertTrue("Make sure Initial state is Idle!", p.getAgentState().equals("Idle"));
			if(!p.location.toString().equals("Home"))
			{
			p.msgTimeIs(2200);
			System.out.println(p.event.toString());
			System.out.println(p.state.toString());
			assertTrue("TestingScheduler", p.pickAndExecuteAnAction());
			assertTrue("Testing to see if state is correct", p.state.toString().equals("Idle"));
			}
			p.msgTimeIs(2330);
			assertTrue("Testing TimeIs", p.log.getLastLoggedEvent().toString().contains("Sleeping In Message"));
			assertTrue("Testing Scheduler", p.pickAndExecuteAnAction());
			assertTrue("Testing Scheduler Log", p.log.getLastLoggedEvent().toString().contains("Sleeping In Scheduler. New State is Sleeping"));
			
			assertFalse("Testing Scheduler", p.pickAndExecuteAnAction());
		}	
	}

}
