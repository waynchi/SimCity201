package restaurant;

import restaurant.interfaces.Waiter;

public class NormalWaiterRole extends BaseWaiterRole implements Waiter{
	public NormalWaiterRole(String name) {
		super();
		this.name = name;
		currentCustomerNum = 0;
		menu.add(new FoodOnMenu("Steak", 15.99));
		menu.add(new FoodOnMenu("Chicken", 10.99));
		menu.add(new FoodOnMenu("Salad", 5.99));
		menu.add(new FoodOnMenu("Pizza", 8.99));
		state = agentState.WORKING;
	}
	
	public void goPlaceOrder(MyCustomer customer) {
		print ("Here's an order for table " + customer.tableNumber);
		cook.msgHereIsAnOrder (customer.choice, this, customer.tableNumber);
		customer.state = customerState.waitingForFood;
	}
}