package restaurant;

import restaurant.gui.RestaurantGui;
import restaurant.gui.RestaurantPanel.CookWaiterMonitor;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.Waiter;

public class SpecialWaiterRole extends BaseWaiterRole implements Waiter{
	public SpecialWaiterRole(CookWaiterMonitor monitor, RestaurantGui gui) {
		super();
		restGui = gui;
		waiterGui = new WaiterGui(this);
		restGui.getAnimationPanel().addGui(waiterGui);
		waiterGui.setPresent(false);
		
		currentCustomerNum = 0;
		menu.add(new FoodOnMenu("Steak", 15.99));
		menu.add(new FoodOnMenu("Chicken", 10.99));
		menu.add(new FoodOnMenu("Salad", 5.99));
		menu.add(new FoodOnMenu("Pizza", 8.99));
		state = agentState.WORKING;
		theMonitor = monitor;
	}
	
	public void goPlaceOrder(MyCustomer customer) throws InterruptedException {
		cook = host.getCook();
		print ("Here's an order for table " + customer.tableNumber + ". Adding to the revolving stand!");
		waiterGui.DoGoToRevolvingStand();
		atRevolvingStand.acquire();
        theMonitor.addOrder(customer.tableNumber, customer.choice, this);
        customer.state = customerState.waitingForFood;
	}
	
	public void done () {
		print("leaving work");
		leaveWork = false;
		waiterGui.DoExit();
		try {
			atExit.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waiterGui.setPresent(false);
		waiterGui.setDefaultDestination();
		isActive = false;
		getPersonAgent().msgDone("RestaurantSpecialWaiterRole");
	}
}
