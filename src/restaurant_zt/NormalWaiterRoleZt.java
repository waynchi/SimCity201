package restaurant_zt;

import restaurant_zt.gui.RestaurantGuiZt;
import restaurant_zt.gui.WaiterGuiZt;
import restaurant_zt.interfaces.Waiter;

public class NormalWaiterRoleZt extends BaseWaiterRole implements Waiter{
	public NormalWaiterRoleZt(RestaurantGuiZt gui) {
		super();
		restGui = gui;
		waiterGui = new WaiterGuiZt(this);
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
		((CookRoleZt) cook).msgHereIsAnOrder (customer.choice, this, customer.tableNumber);
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
