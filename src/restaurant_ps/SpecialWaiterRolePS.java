package restaurant_ps;

import restaurant_ps.gui.RestaurantGuiPS;
import restaurant_ps.gui.RestaurantPanelPS.CookWaiterMonitor;
import restaurant_ps.gui.WaiterGuiPS;
import restaurant_ps.interfaces.Waiter;

public class SpecialWaiterRolePS extends BaseWaiterRole implements Waiter{
	public SpecialWaiterRolePS(CookWaiterMonitor monitor, RestaurantGuiPS gui) {
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
		// gui has to walk to exit
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
