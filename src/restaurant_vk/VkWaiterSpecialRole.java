package restaurant_vk;

import people.PeopleAgent;
import restaurant_vk.VkWaiterBaseRole.ClosingState;
import restaurant_vk.interfaces.Host;
import restaurant_vk.interfaces.Waiter;

public class VkWaiterSpecialRole extends VkWaiterBaseRole implements Waiter {
	// Data
	
	public RevolvingStand stand;
	
	public VkWaiterSpecialRole(Host host, RevolvingStand s) {
		super(host);
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
	
	protected void leaveRestaurant() {
		if (closingState == ClosingState.None)
			((VkCashierRole) cashier).recordShift((PeopleAgent)myPerson, "Waiter");
		gui.DoLeaveRestaurant();
		try {
			movingAround.acquire();
		} catch (InterruptedException e) {}
		isActive = false;
		leave = false;
		myPerson.msgDone("RestaurantSpecialWaiterRole");
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
