package market.test;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Timer;

import market.MarketEmployeeRole;
import market.gui.MarketGui;
import market.test.MockPeople.MyRestaurant;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import restaurant.test.mock.MockCashier;
import restaurant.test.mock.MockCook;
import restaurant.test.mock.MockHost;

public class MarketEmployeeTest extends TestCase{
	MarketEmployeeRole employee;
	MockMarketCashier cashier;
	MockPeople people;
	MockMarketCustomer customer;
	MockMarketCustomer customer2;
	MockCook cook;
	MockHost host;
	MockMarketTruck truck1;
	MockMarketTruck truck2;
	MockCashier restaurantCashier;
	Map<String, Integer> items = new HashMap<String, Integer>();
	Map<String, Integer> restOrder = new HashMap<String, Integer>();
	Map<String, Integer> restOrder2 = new HashMap<String, Integer>();

	MarketGui gui;
	Timer timer;
	MyRestaurant restaurant;


	public static void main(String args[]) {
		junit.textui.TestRunner.run(suite());
	}

	public static TestSuite suite ( ) {
		return new TestSuite(MarketEmployeeTest.class);
	}

	public void setUp() throws Exception{
		super.setUp();  
		timer = new Timer(20,null);
		gui = new MarketGui(timer);
		people = new MockPeople("people");
		cashier = new MockMarketCashier("marketCashier");
		employee = new MarketEmployeeRole(gui);
		employee.inTest = true;
		employee.getTrucks().clear();
		employee.setPerson(people);
		restaurant = people.new MyRestaurant();
		people.addResetaurant(restaurant);
		
		truck1 = new MockMarketTruck("truck1");
		truck2 = new MockMarketTruck("truck2");
		employee.addTruck(truck1);
		employee.addTruck(truck2);


		employee.setCashier(cashier);
		host = new MockHost("host");
		host.setCashier(restaurantCashier);
		host.setCook(cook);
		restaurantCashier = new MockCashier("mcashier");

		customer = new MockMarketCustomer("customer");
		customer2 = new MockMarketCustomer("customer2");

		cook = new MockCook("cook");
		cook.setRestaurantIndex(0);
		cook.setHost(host);
		restaurantCashier.setHost(host);

		items.put("Car", 1);
		restOrder.put("Steak", 10);
		restOrder.put("Salad", 5);
		restOrder2.put("Pizza", 20);
		restOrder2.put("Chicken", 30);

	}

	// normative scenario of one restaurant order and order delivered
	public void testOneRestaurantOrder (){
		employee.msgIsActive();
		employee.inTest = true;

		employee.pickAndExecuteAnAction();

		assertTrue("the order list for market employee should be empty, but it has the size of " + employee.orders.size(), 
				employee.orders.isEmpty());

		//cook sends an order to employee
		employee.msgHereIsAnOrder(restOrder, cook, restaurantCashier);
		assertTrue("right now the order list should have size 1, but instead it has the size of " + employee.orders.size(),
				employee.orders.size() == 1);
		assertEquals("the only order in the list should have the state of pending, but instead it has the state " + employee.orders.get(0).getState(),
				employee.orders.get(0).getState(),"PENDING");

		employee.pickAndExecuteAnAction();

		assertEquals("inventory of steak should now be decreased to 9990, but instead it's "+employee.items.get("Steak").inventory,
				employee.items.get("Steak").inventory,9990);
		assertEquals("inventory of salad should now be decreased to 9995, but instead it's "+employee.items.get("Salad").inventory,
				employee.items.get("Salad").inventory,9995);		
		assertTrue("employee should be in action give items to customers, instead the log reads " + employee.log.toString(),
				employee.log.containsString("sending confirmation to cook, check to market cashier, and order to truck"));
		assertEquals("the only order in the list should have the state of in delivery, but instead it has the state " + employee.orders.get(0).getState(),
				employee.orders.get(0).getState(),"IN_DELIVERY");

		// verify message reception for mocks
		assertTrue("cook should have received the original items list along with an order number, it doesn't. the log reads " + cook.log.toString(),
				cook.log.containsString("order confirmed, get order number 1"));
		assertTrue("market cashier should have received a check for cook, it doesn't",
				cashier.log.containsString("received msgHereIsACheck for restaurant cashier with order number 1"));
		assertTrue("market truck 1 should get a message to deliver the items to cook, it doesn't. the log reads " + truck1.log.toString(),
				truck1.log.containsString("get an order from employee for cook, with order number 1"));		

		//truck noticed employee that order has been delivered
		employee.msgOrderDelivered(1);
		assertTrue("the only order should be removed from the order list and list should be empty, but it has the size of " + employee.orders.size(), 
				employee.orders.isEmpty());
	}




	public void testOneCustOrder (){
		employee.msgIsActive();
		employee.inTest = true;

		employee.pickAndExecuteAnAction();

		assertTrue("the order list for market employee should be empty, but it has the size of " + employee.orders.size(), 
				employee.orders.isEmpty());

		//customer sends an order to employee
		employee.msgHereIsAnOrder(customer, items);
		assertTrue("right now the order list should have size 1, but instead it has the size of " + employee.orders.size(),
				employee.orders.size() == 1);
		assertEquals("the only order in the list should have the state of pending, but instead it has the state " + employee.orders.get(0).getState(),
				employee.orders.get(0).getState(),"PENDING");

		employee.pickAndExecuteAnAction();

		assertEquals("inventory of car should now be decreased to 9999, but instead it's "+employee.items.get("Steak").inventory,
				employee.items.get("Car").inventory,9999);
		assertTrue("employee should be in action give items to customers, instead the log reads " + employee.log.toString(),
				employee.log.containsString("giving items to customer"));
		assertTrue("the only order should be removed from the order list and list should be empty, but it has the size of " + employee.orders.size(), 
				employee.orders.isEmpty());

		// verify message reception for mocks
		assertTrue("customer should have got its items, but it doesn't. the log reads " + customer.log.toString(),
				customer.log.containsString("received my items"));
		assertTrue("market cashier should have received a check for customer, it doesn't",
				cashier.log.containsString("received msgHereIsACheck for customer"));
	}



	// two orders from a same restaurant, normative scenario
	public void testTwoRestaurantOrder (){
		employee.msgIsActive();
		employee.inTest = true;

		employee.pickAndExecuteAnAction();

		assertTrue("the order list for market employee should be empty, but it has the size of " + employee.orders.size(), 
				employee.orders.isEmpty());

		//received an order from restaurant cook
		employee.msgHereIsAnOrder(restOrder, cook, restaurantCashier);
		assertTrue("right now the order list should have size 1, but instead it has the size of " + employee.orders.size(),
				employee.orders.size() == 1);
		assertEquals("the only order in the list should have the state of pending, but instead it has the state " + employee.orders.get(0).getState(),
				employee.orders.get(0).getState(),"PENDING");
		assertEquals("the only order in the list should have orderNumber 1, but it doesn't",
				employee.orders.get(0).getOrderNumber(),1);

		//received another order from the same cook
		employee.msgHereIsAnOrder(restOrder2, cook, restaurantCashier);
		assertTrue("right now the order list should have size 2, but instead it has the size of " + employee.orders.size(),
				employee.orders.size() == 2);
		assertEquals("the second order should have the state of pending, but instead it has the state " + employee.orders.get(1).getState(),
				employee.orders.get(1).getState(),"PENDING");
		assertEquals("the second order in the list should have orderNumber 2, but it doesn't",
				employee.orders.get(1).getOrderNumber(),2);


		employee.pickAndExecuteAnAction();

		// employee is dealing with the first order
		assertEquals("inventory of steak should now be decreased to 9990, but instead it's "+employee.items.get("Steak").inventory,
				employee.items.get("Steak").inventory,9990);
		assertEquals("inventory of salad should now be decreased to 9995, but instead it's "+employee.items.get("Salad").inventory,
				employee.items.get("Salad").inventory,9995);		
		assertTrue("employee should be in action give items to customers, instead the log reads " + employee.log.toString(),
				employee.log.containsString("sending confirmation to cook, check to market cashier, and order to truck"));
		assertEquals("the only order in the list should have the state of in delivery, but instead it has the state " + employee.orders.get(0).getState(),
				employee.orders.get(0).getState(),"IN_DELIVERY");

		// verify message reception for mocks
		assertTrue("cook should have received the original items list along with an order number, it doesn't. the log reads " + cook.log.toString(),
				cook.log.containsString("order confirmed, get order number 1"));
		assertTrue("market cashier should have received a check for cook, it doesn't",
				cashier.log.containsString("received msgHereIsACheck for restaurant cashier with order number 1"));
		assertTrue("market truck 1 should get a message to deliver the items to cook, it doesn't. the log reads " + truck1.log.toString(),
				truck1.log.containsString("get an order from employee for cook, with order number 1"));		

		employee.pickAndExecuteAnAction();

		// employee is dealing with the second order
		assertEquals("inventory of pizza should now be decreased to 9980, but instead it's "+employee.items.get("Pizza").inventory,
				employee.items.get("Pizza").inventory,9980);
		assertEquals("inventory of chicken should now be decreased to 9970, but instead it's "+employee.items.get("Chicken").inventory,
				employee.items.get("Chicken").inventory,9970);		
		assertTrue("employee should be in action give items to customers, instead the log reads " + employee.log.toString(),
				employee.log.containsString("sending confirmation to cook, check to market cashier, and order to truck"));
		assertEquals("the only order in the list should have the state of in delivery, but instead it has the state " + employee.orders.get(0).getState(),
				employee.orders.get(0).getState(),"IN_DELIVERY");

		// verify message reception for mocks
		assertTrue("cook should have received the original items list along with an order number, it doesn't. the log reads " + cook.log.toString(),
				cook.log.containsString("order confirmed, get order number 2"));
		assertTrue("market cashier should have received a check for cook, it doesn't",
				cashier.log.containsString("received msgHereIsACheck for restaurant cashier with order number 2"));
		assertTrue("market truck 2 should get a message to deliver the items to cook, it doesn't. the log reads " + truck1.log.toString(),
				truck2.log.containsString("get an order from employee for cook, with order number 2"));		


		//just make sure the order number is correct
		assertEquals("first order in list should have order number 1", 
				employee.orders.get(0).getOrderNumber(),1);
		assertEquals("second order in list should have order number 2", 
				employee.orders.get(1).getOrderNumber(),2);

		//truck noticed employee that order 2 has been delivered
		employee.msgOrderDelivered(2);
		assertEquals("the order list should have one order left in it, but it has the size of " + employee.orders.size(), 
				employee.orders.size(),1);
		assertEquals("the order left in the list should have orderNumber 1, but it doesn't",
				employee.orders.get(0).getOrderNumber(), 1);

		//truck noticed employee that order 1 has been delivered
		employee.msgOrderDelivered(1);
		assertTrue("the only order should be removed from the order list and list should be empty, but it has the size of " + employee.orders.size(), 
				employee.orders.isEmpty());
	}




	public void testTwoCustOrder () {
		employee.msgIsActive();
		employee.inTest = true;

		employee.pickAndExecuteAnAction();

		assertTrue("the order list for market employee should be empty, but it has the size of " + employee.orders.size(), 
				employee.orders.isEmpty());

		//customer sends an order to employee
		employee.msgHereIsAnOrder(customer, items);
		assertTrue("right now the order list should have size 1, but instead it has the size of " + employee.orders.size(),
				employee.orders.size() == 1);
		assertEquals("the only order in the list should have the state of pending, but instead it has the state " + employee.orders.get(0).getState(),
				employee.orders.get(0).getState(),"PENDING");

		//another customer sends an order to employee
		employee.msgHereIsAnOrder(customer2, items);
		assertTrue("right now the order list should have size 2, but instead it has the size of " + employee.orders.size(),
				employee.orders.size() == 2);
		assertEquals("the second order in the list should have the state of pending, but instead it has the state " + employee.orders.get(0).getState(),
				employee.orders.get(1).getState(),"PENDING");

		employee.pickAndExecuteAnAction();

		// employee should be dealing with the first order
		assertEquals("inventory of car should now be decreased to 9999, but instead it's "+employee.items.get("Steak").inventory,
				employee.items.get("Car").inventory,9999);
		assertTrue("employee should be in action give items to customers, instead the log reads " + employee.log.toString(),
				employee.log.containsString("giving items to customer"));
		assertTrue("one order should be removed from the order list and list should be of size 1, but it has the size of " + employee.orders.size(), 
				employee.orders.size() == 1);
		assertTrue("the only order now should still have the state pending, but it doesn't",
				employee.orders.get(0).getState().equals("PENDING"));

		// verify message reception for mocks
		assertTrue("customer should have got its items, but it doesn't. the log reads " + customer.log.toString(),
				customer.log.containsString("received my items"));
		assertTrue("market cashier should have received a check for customer, it doesn't",
				cashier.log.containsString("received msgHereIsACheck for customer"));
		
		employee.pickAndExecuteAnAction();

		// employee should be dealing with the second order
		assertEquals("inventory of car should now be decreased to 9998, but instead it's "+employee.items.get("Steak").inventory,
				employee.items.get("Car").inventory,9998);
		assertTrue("employee should be in action give items to customers, instead the log reads " + employee.log.toString(),
				employee.log.containsString("giving items to customer"));
		assertTrue("one order should be removed from the order list and list should be empty, but it has the size of " + employee.orders.size(), 
				employee.orders.isEmpty());
	
		// verify message reception for mocks
		assertTrue("customer2 should have got its items, but it doesn't. the log reads " + customer2.log.toString(),
				customer2.log.containsString("received my items"));
		assertTrue("market cashier should have received a check for customer2, it doesn't",
				cashier.log.containsString("received msgHereIsACheck for customer"));
	}

	
	
	
	public void testBoth() {
		employee.msgIsActive();
		employee.inTest = true;

		employee.pickAndExecuteAnAction();

		assertTrue("the order list for market employee should be empty, but it has the size of " + employee.orders.size(), 
				employee.orders.isEmpty());

		//received an order from restaurant cook
		employee.msgHereIsAnOrder(restOrder, cook, restaurantCashier);
		assertTrue("right now the order list should have size 1, but instead it has the size of " + employee.orders.size(),
				employee.orders.size() == 1);
		assertEquals("the only order in the list should have the state of pending, but instead it has the state " + employee.orders.get(0).getState(),
				employee.orders.get(0).getState(),"PENDING");
		assertEquals("the only order in the list should have orderNumber 1, but it doesn't",
				employee.orders.get(0).getOrderNumber(),1);

		//received another order from the same cook
		employee.msgHereIsAnOrder(restOrder2, cook, restaurantCashier);
		assertTrue("right now the order list should have size 2, but instead it has the size of " + employee.orders.size(),
				employee.orders.size() == 2);
		assertEquals("the second order should have the state of pending, but instead it has the state " + employee.orders.get(1).getState(),
				employee.orders.get(1).getState(),"PENDING");
		assertEquals("the second order in the list should have orderNumber 2, but it doesn't",
				employee.orders.get(1).getOrderNumber(),2);


		employee.pickAndExecuteAnAction();

		// employee is dealing with the first order
		assertEquals("inventory of steak should now be decreased to 9990, but instead it's "+employee.items.get("Steak").inventory,
				employee.items.get("Steak").inventory,9990);
		assertEquals("inventory of salad should now be decreased to 9995, but instead it's "+employee.items.get("Salad").inventory,
				employee.items.get("Salad").inventory,9995);		
		assertTrue("employee should be in action give items to customers, instead the log reads " + employee.log.toString(),
				employee.log.containsString("sending confirmation to cook, check to market cashier, and order to truck"));
		assertEquals("the only order in the list should have the state of in delivery, but instead it has the state " + employee.orders.get(0).getState(),
				employee.orders.get(0).getState(),"IN_DELIVERY");

		// verify message reception for mocks
		assertTrue("cook should have received the original items list along with an order number, it doesn't. the log reads " + cook.log.toString(),
				cook.log.containsString("order confirmed, get order number 1"));
		assertTrue("market cashier should have received a check for cook, it doesn't",
				cashier.log.containsString("received msgHereIsACheck for restaurant cashier with order number 1"));
		assertTrue("market truck 1 should get a message to deliver the items to cook, it doesn't. the log reads " + truck1.log.toString(),
				truck1.log.containsString("get an order from employee for cook, with order number 1"));		

		//customer sends an order to employee
				employee.msgHereIsAnOrder(customer, items);
				assertTrue("right now the order list should have size 3, but instead it has the size of " + employee.orders.size(),
						employee.orders.size() == 3);
				assertEquals("the new order in the list should have the state of pending, but instead it has the state " + employee.orders.get(0).getState(),
						employee.orders.get(2).getState(),"PENDING");

		
		employee.pickAndExecuteAnAction();

		// employee is dealing with the second order
		assertEquals("inventory of pizza should now be decreased to 9980, but instead it's "+employee.items.get("Pizza").inventory,
				employee.items.get("Pizza").inventory,9980);
		assertEquals("inventory of chicken should now be decreased to 9970, but instead it's "+employee.items.get("Chicken").inventory,
				employee.items.get("Chicken").inventory,9970);		
		assertTrue("employee should be in action give items to customers, instead the log reads " + employee.log.toString(),
				employee.log.containsString("sending confirmation to cook, check to market cashier, and order to truck"));
		assertEquals("the only order in the list should have the state of in delivery, but instead it has the state " + employee.orders.get(0).getState(),
				employee.orders.get(0).getState(),"IN_DELIVERY");

		// verify message reception for mocks
		assertTrue("cook should have received the original items list along with an order number, it doesn't. the log reads " + cook.log.toString(),
				cook.log.containsString("order confirmed, get order number 2"));
		assertTrue("market cashier should have received a check for cook, it doesn't",
				cashier.log.containsString("received msgHereIsACheck for restaurant cashier with order number 2"));
		assertTrue("market truck 2 should get a message to deliver the items to cook, it doesn't. the log reads " + truck2.log.toString(),
				truck2.log.containsString("get an order from employee for cook, with order number 2"));		


		//just make sure the order number is correct
		assertEquals("first order in list should have order number 1", 
				employee.orders.get(0).getOrderNumber(),1);
		assertEquals("second order in list should have order number 2", 
				employee.orders.get(1).getOrderNumber(),2);

		//truck noticed employee that order 2 has been delivered
		employee.msgOrderDelivered(2);
		assertEquals("the order list should have 2 order left in it, but it has the size of " + employee.orders.size(), 
				employee.orders.size(),2);
		assertEquals("the order left in the list should have orderNumber 1, but it doesn't",
				employee.orders.get(0).getOrderNumber(), 1);
		
		employee.pickAndExecuteAnAction();

		//employee should be dealing with the only pending order, i.e. from market customer
		assertEquals("inventory of car should now be decreased to 9999, but instead it's "+employee.items.get("Steak").inventory,
				employee.items.get("Car").inventory,9999);
		assertTrue("employee should be in action give items to customers, instead the log reads " + employee.log.toString(),
				employee.log.containsString("giving items to customer"));
		assertTrue("the only order should be removed from the order list and list should have size 1, but it has the size of " + employee.orders.size(), 
				employee.orders.size() ==1);

		// verify message reception for mocks
		assertTrue("customer should have got its items, but it doesn't. the log reads " + customer.log.toString(),
				customer.log.containsString("received my items"));
		assertTrue("market cashier should have received a check for customer, it doesn't",
				cashier.log.containsString("received msgHereIsACheck for customer"));
		
		//truck noticed employee that order 1 has been delivered
		employee.msgOrderDelivered(1);
		assertTrue("the only order should be removed from the order list and list should be empty, but it has the size of " + employee.orders.size(), 
				employee.orders.isEmpty());
	}
	
	
	
	public void testRedelivery() {
		employee.msgIsActive();
		employee.inTest = true;
		people.getMyRestaurant(0).isClosed = true;

		employee.pickAndExecuteAnAction();

		assertTrue("the order list for market employee should be empty, but it has the size of " + employee.orders.size(), 
				employee.orders.isEmpty());

		//cook sends an order to employee
		employee.msgHereIsAnOrder(restOrder, cook, restaurantCashier);
		assertTrue("right now the order list should have size 1, but instead it has the size of " + employee.orders.size(),
				employee.orders.size() == 1);
		assertEquals("the only order in the list should have the state of pending, but instead it has the state " + employee.orders.get(0).getState(),
				employee.orders.get(0).getState(),"PENDING");

		employee.pickAndExecuteAnAction();

		assertEquals("inventory of steak should now be decreased to 9990, but instead it's "+employee.items.get("Steak").inventory,
				employee.items.get("Steak").inventory,9990);
		assertEquals("inventory of salad should now be decreased to 9995, but instead it's "+employee.items.get("Salad").inventory,
				employee.items.get("Salad").inventory,9995);		
		assertTrue("employee should be in action give items to customers, instead the log reads " + employee.log.toString(),
				employee.log.containsString("sending confirmation to cook, check to market cashier, and order to truck"));
		assertEquals("the only order in the list should have the state of to be redelivered, but instead it has the state " + employee.orders.get(0).getState(),
				employee.orders.get(0).getState(),"TO_BE_REDELIVERED");

		// verify message reception for mocks
		assertTrue("cook should have received the original items list along with an order number, it doesn't. the log reads " + cook.log.toString(),
				cook.log.containsString("order confirmed, get order number 1"));
		assertTrue("market cashier should have received a check for cook, it doesn't",
				cashier.log.containsString("received msgHereIsACheck for restaurant cashier with order number 1"));
		
		employee.msgIsInActive();
		employee.msgIsActive();
		people.getMyRestaurant(0).isClosed = false;
		employee.inTest = true;
		employee.pickAndExecuteAnAction();
		
		//employee should be trying to redeliver the restaurant order
		assertTrue("employee shoule be in action redeliver restaurant order, but it's not",
				employee.log.containsString("check order list to see if any redelivery is needed"));
		assertTrue("market truck should get a message to deliver the items to cook, it doesn't. the log reads " + truck1.log.toString(),
				truck1.log.containsString("get an order from employee for cook, with order number 1"));		

		
		//truck noticed employee that order has been delivered
		employee.msgOrderDelivered(1);
		assertTrue("the only order should be removed from the order list and list should be empty, but it has the size of " + employee.orders.size(), 
				employee.orders.isEmpty());
	}

}
