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
	MockMarketTruck truck1;
	MockMarketTruck truck2;
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
		employee.inTest = true;
		truck1 = new MockMarketTruck("truck1");
		truck2 = new MockMarketTruck("truck2");
		employee.addTruck(truck1);
		employee.addTruck(truck2);
		
		employee.setCashier(cashier);
		host = new MockHost("host");
		host.setCashier(mcashier);
		host.setCook(cook);
		mcashier = new MockCashier("mcashier");
		
		people.addRole(employee, "MarketEmployeeRole");
		employee.setPerson(people);

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
		employee.inTest = true;
		employee.pickAndExecuteAnAction();
		
		assertTrue("the order list for market employee should be empty, but it has the size of " + employee.orders.size(), 
				employee.orders.isEmpty());
		employee.msgOrder(restOrder, cook, mcashier);
		assertTrue("right now the order list should have size 1, but instead it has the size of " + employee.orders.size(),
				employee.orders.size() == 1);
		employee.pickAndExecuteAnAction();
		
		assertEquals("inventory of steak should now be decreased to 90, but instead it's "+employee.items.get("Steak").inventory,
				employee.items.get("Steak").inventory,90);
		assertEquals("inventory of salad should now be decreased to 95, but instead it's "+employee.items.get("Salad").inventory,
				employee.items.get("Salad").inventory,95);		
		assertTrue("employee should be in action give items to customers, instead the log reads " + employee.log.toString(),
				employee.log.containsString("sending confirmation to cook"));
		assertTrue("order list is not empty, but instead it has the size of " + employee.orders.size(), 
				employee.orders.isEmpty());
		
		assertTrue("cook should have received the original items list along with an order number, it doesn't. the log reads " + cook.log.toString(),
				cook.log.containsString("order confirmed, get order number 1"));
		assertTrue("market cashier should have received a check for cook, it doesn't",
				cashier.log.containsString("received msgHereIsACheck for restaurant cashier with order number 1"));
		assertTrue("market truck 1 should get a message to deliver the items to cook, it doesn't. the log reads " + truck1.log.toString(),
				truck1.log.containsString("received order number 1, about to deliver it to cook"));		
	}
	
	public void testOneCustOrder (){
		
	}
	
	public void testTwoRestOrder (){
		
	}
	
	public void testTwoCustOrder () {
		
	}
	
	public void testBoth() {
		
	}

}
