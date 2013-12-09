package restaurant_ps;

import restaurant_ps.gui.RestaurantGuiPS;
import restaurant_ps.gui.WaiterGuiPS;
import restaurant_ps.interfaces.Waiter;

public class NormalWaiterRolePS extends BaseWaiterRole implements Waiter{
	public NormalWaiterRolePS(RestaurantGuiPS gui) {
		super();
		restGui = gui;
		waiterGui = new WaiterGuiPS(this);
		restGui.getAnimationPanel().addGui(waiterGui);
		waiterGui.setPresent(false);
		
		currentCustomerNum = 0;
		menu.add(new FoodOnMenu("Steak", 15.99));
		menu.add(new FoodOnMenu("Chicken", 10.99));
		menu.add(new FoodOnMenu("Salad", 5.99));
		menu.add(new FoodOnMenu("Pizza", 8.99));
		state = agentState.WORKING;
	}
	
	public void goPlaceOrder(MyCustomer customer) {
		cook = host.getCook();
		print ("Here's an order for table " + customer.tableNumber);
		((CookRolePS) cook).msgHereIsAnOrder (customer.choice, this, customer.tableNumber);
		customer.state = customerState.waitingForFood;
	}
	
	public void done () {
		// gui needs to walk to exit
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
		leaveWork = false;
		getPersonAgent().msgDone("RestaurantNormalWaiterRole");
	}
}
