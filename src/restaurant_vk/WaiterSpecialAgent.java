package restaurant_vk;

import restaurant_vk.gui.RestaurantPanel.RevolvingStand;
import restaurant_vk.interfaces.Host;
import restaurant_vk.interfaces.Waiter;

public class WaiterSpecialAgent extends WaiterBaseAgent implements Waiter {
	// Data
	
	public RevolvingStand stand;
	
	public WaiterSpecialAgent(Host host, String name, RevolvingStand s) {
		super(host, name);
		this.stand = s;
	}
		
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
	
	// Messages
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
	
	// Actions
		
	/*
	 * This is an action to send a message to the cook telling him the choice
	 * and the table number.
	 */
	protected void passOrderToCook(MyCustomer mc) {
		DoGoToRevolvingStand();
		try {
			movingAround.acquire();
		} catch (InterruptedException e) {}
		
		stand.addOrder(this, mc.choice, mc.table);
		print("Order has been put in revolving stand.");
		mc.os = OrderStatus.GivenToCook;
	}

	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
		
	// Scheduler
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
	
	// Utilities
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
	
	// Helper Data Structures
}
