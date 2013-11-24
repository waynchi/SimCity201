package restaurant;

import restaurant.gui.RestaurantPanel.CookWaiterMonitor;
import restaurant.interfaces.Waiter;

public class SpecialWaiterRole extends BaseWaiterRole implements Waiter{
	public SpecialWaiterRole(String name, CookWaiterMonitor monitor) {
		super();
		this.name = name;
		currentCustomerNum = 0;
		menu.add(new FoodOnMenu("Steak", 15.99));
		menu.add(new FoodOnMenu("Chicken", 10.99));
		menu.add(new FoodOnMenu("Salad", 5.99));
		menu.add(new FoodOnMenu("Pizza", 8.99));
		state = agentState.WORKING;
		theMonitor = monitor;
	}
	
	public void goPlaceOrder(MyCustomer customer) {
		print ("Here's an order for table " + customer.tableNumber + ". Adding to the revolving stand!");
        theMonitor.addOrder(customer.tableNumber, customer.choice, this);
        customer.state = customerState.waitingForFood;
	}
	
	public void done () {
		isActive = false;
		leaveWork = false;
		getPersonAgent().msgDone("RestaurantSpecialWaiter");
	}
}
