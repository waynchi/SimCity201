package restaurant_vk;

import people.PeopleAgent;
import restaurant_vk.VkWaiterBaseRole.ClosingState;
import restaurant_vk.interfaces.Host;
import restaurant_vk.interfaces.Waiter;

/**
 * @author Vikrant Singhal
 *
 * Waiter of the restaurant. There will be multiple such waiters that will be
 * added via the gui. It has all the responsibilities that a waiter has.
 */
public class VkWaiterNormalRole extends VkWaiterBaseRole implements Waiter {
	
	// Data
	
	public VkWaiterNormalRole(Host host) {
		super(host);
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
		mc.os = OrderStatus.GivenToCook;
		print("Order has been given to cook.");
		cook.hereIsOrder(this, mc.choice, mc.table);
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
		myPerson.msgDone("RestaurantNormalWaiterRole");
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