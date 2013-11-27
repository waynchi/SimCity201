package market.test;

import java.util.HashMap;
import java.util.Map;

import market.MarketCashierRole;
import market.MarketEmployeeRole;
import market.gui.MarketGui;
import people.PeopleAgent;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import restaurant.interfaces.Host;
import restaurant.test.mock.MockCashier;
import restaurant.test.mock.MockCook;
import restaurant.test.mock.MockHost;
import test.NewCashierTest;

public class MarketEmployeeTest extends TestCase{
	PeopleAgent people;
	MarketEmployeeRole employee;
	MockMarketCashier cashier;
	MockMarketCustomer customer;
	MockCook cook;
	MockHost host;
	//MockMarketTruck truck;
	MockCashier mcashier;
	Map<String, Integer> items = new HashMap<String, Integer>();
	Map<String, Integer> restOrder = new HashMap<String, Integer>();
	MarketGui gui;
	
	
	public static void main(String args[]) {
		junit.textui.TestRunner.run(suite());
	}

	public static TestSuite suite ( ) {
		return new TestSuite(MarketEmployeeTest.class);
	}

	public void setUp() throws Exception{
		super.setUp();  
		gui = new MarketGui();
		people = new PeopleAgent("people", 0.0, false);
		cashier = new MockMarketCashier("marketCashier");
		employee = new MarketEmployeeRole(gui);
		employee.setCashier(cashier);
		host = new MockHost("host");
		host.setCashier(mcashier);
		host.setCook(cook);
		mcashier = new MockCashier("mcashier");
		employee.inTest = true;
		//truck = new MockMarketTruck("truck");
		
		people.addRole(employee, "MarketEmployeeRole");
		employee.setPerson(people);
		//cashier.setMarketEmployee(employee);

		customer = new MockMarketCustomer("customer");
		cook = new MockCook("cook");
		cook.setHost(host);
		mcashier.setHost(host);
		
		items.put("Car", 1);
		restOrder.put("Steak", 10);
		restOrder.put("Salad", 5);
	}
	
	public void testOneRestOrder (){
		employee.msgIsActive();
		employee.pickAndExecuteAnAction();
		
		assertTrue(employee.orders.isEmpty());
		employee.msgOrder(restOrder, cook, mcashier);
		assertTrue(employee.orders.size() == 1);
		
		employee.pickAndExecuteAnAction();
		assertTrue(employee.log.containsString("in action give order to customer"));
		assertTrue(employee.getTrucks().get(0).log.containsString("received message here is an order"));
		cook.msgHereIsYourOrder(restOrder);
		assertTrue("cook log reads: " + cook.log.toString() ,cook.log.containsString("received msgHereIsYourOrder from market"));
		assertTrue(cashier.log.containsString("received msgHereIsACheck from employee for restaurant cashier"));
		
	}

}
