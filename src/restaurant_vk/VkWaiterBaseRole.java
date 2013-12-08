package restaurant_vk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.Semaphore;
import people.PeopleAgent;
import people.Role;
import restaurant.interfaces.Cashier;
import restaurant_vk.gui.VkWaiterGui;
import restaurant_vk.VkCashierRole;
import restaurant_vk.VkCookRole;
import restaurant_vk.interfaces.Customer;
import restaurant_vk.interfaces.Host;
import restaurant_vk.interfaces.Waiter;

public class VkWaiterBaseRole extends Role implements Waiter {

	// Data
	
	protected List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	protected Semaphore waitingForOrder = new Semaphore(0, true);
	protected Semaphore movingAround = new Semaphore(0, true);
	public VkWaiterGui gui = null;
	protected Host host;
	protected VkCookRole cook;
	protected MyState state = MyState.Working;
	protected Cashier cashier = null;
	private boolean leave = false;
	private boolean enter = false;
	private ClosingState closingState = ClosingState.Closed;
		
	public VkWaiterBaseRole(Host host) {
		super();
		this.host = host;
		setCashier(((VkHostRole)host).getCashier());
	}
		
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
		
	// Messages
		
	/*
	 * A message given by the host when she decides that the customer
	 * is ready to be seated.
	 */
	public void sitAtTable(Customer c, int table) {
		try {
			customers.add(new MyCustomer(c, table));
			stateChanged();
		}
		catch (ConcurrentModificationException e) {
			print("Exception caught.");
		}
	}
		
	/*
	 * This is a message that a customer calls when he/ she is ready to order.
	 * A message to call the waiter at that moment.
	 */
	public void readyToOrder(Customer c) {
		try {
			print(c + " has called me to order.");
			MyCustomer mc = find(c);
			mc.s = CustomerState.ReadyToOrder;
			stateChanged();
		}
		catch (ConcurrentModificationException e) {
			print("Exception caught.");
		}
	}
		
	/*
	 * When the waiter is at the table, the customer used this message to
	 * convey the waiter his/ her choice.
	 */
	public void hereIsMyChoice(String item, Customer c) {
		try {
			MyCustomer mc = find(c);
			mc.s = CustomerState.Ordered;
			mc.os = OrderStatus.TakenFromCustomer;
			mc.choice = item;
			waitingForOrder.release();
			stateChanged();
		}
		catch (ConcurrentModificationException e) {
			print("Exception caught.");
		}
	}
		
	/*
	 * A message called by the cook, once the order for a specific
	 * table is ready to be served.
	 */
	public void orderIsReady(String choice, int table) {
		try {
			for (MyCustomer mc : customers) {
				if (mc.table == table && mc.s == CustomerState.Ordered) {
					mc.os = OrderStatus.ReadyToServe;
					break;
				}
			}
			stateChanged();
			print("Received orderIsReady() for table " + table + " of" + choice);
		}
		catch (ConcurrentModificationException e) {
			print("Exception caught.");
		}
	}
		
	/*
	 * A message called by the customer once he/ she has eaten and is
	 * leaving the restaurant.
	 */
	public void doneEatingAndLeaving(Customer c) {
		try {
			MyCustomer mc = find(c);
			mc.s = CustomerState.Leaving;
			mc.os = OrderStatus.None;
			mc.checkGiven = false;
			stateChanged();
		}
		catch (ConcurrentModificationException e) {
			print("Exception caught.");
		}
	}
		
	/*
	 * A message called by the animation when the Waiter has reached his
	 * destination.
	 */
	public void atDestination() {
		movingAround.release();		
	}
	
	/*
	 * Message from Gui that makes the Waiter demand a break.
	 */
	public void wantBreak() {
		try {
			state = MyState.WantABreak;
		}
		catch (ConcurrentModificationException e) {
			print("Exception caught.");
		}
		stateChanged();
	}
	
	/*
	 * Message from Gui that makes the waiter return to work.
	 */
	public void getBackToWork() {
		try {
			state = MyState.BackFromBreak;
		}
		catch (ConcurrentModificationException e) {
			print("Exception caught.");
		}
		stateChanged();
	}
	
	/*
	 * Message from host that disallows any breaks.
	 */
	public void noBreak() {
		try {
			state = MyState.Working;
		}
		catch (ConcurrentModificationException e) {
			print("Exception caught.");
		}
		gui.setOffBreak();
		stateChanged();
	}
	
	/*
	 * Message from host that allows a break.
	 */
	public void takeABreak() {
		try {
			state = MyState.OnBreak;
		}
		catch (ConcurrentModificationException e) {
			print("Exception caught.");
		}
		gui.setOnBreak();
		stateChanged();
	}
	
	/*
	 * A message sent by the cook to inform that a given order can't be made.
	 */
	public void outOf(String choice, int table) {
		try {
			for (MyCustomer mc : customers) {
				if (mc.table == table && mc.s == CustomerState.Ordered) {
					mc.os = OrderStatus.OutOfMaterials;
					break;
				}
			}
		}
		catch (ConcurrentModificationException e) {
			print("Exception caught.");
		}
		stateChanged();
	}
	
	/*
	 * A message called by the cashier once the check is ready for a customer.
	 */
	public void hereIsCheck(CustomerRestaurantCheck ch, Customer c) {
		try {
			MyCustomer mc = find(c);
			mc.check = ch;
		}
		catch (ConcurrentModificationException e) {
			print("Exception caught.");
		}
		stateChanged();
	}
		
	/*
	 * A message called by the customer when he wants to leave without eating.
	 */
	public void leavingWithoutEating(Customer c) {
		try {
			MyCustomer mc = find(c);
			mc.s = CustomerState.LeavingWithoutEating;
			mc.os = OrderStatus.None;
		}
		catch (ConcurrentModificationException e) {
			print("Exception caught.");
		}
		stateChanged();
	}
	
	public void msgIsActive() {
		if (cashier == null) {
			this.cashier = ((VkHostRole)host).cashier;
		}
		if (cook == null) {
			this.cook = ((VkHostRole)host).cook;
		}
		isActive = true;
		enter = true;
		stateChanged();
	}
	
	public void msgIsInActive() {
		leave = true;
		stateChanged();
	}
	
	public void closeRestaurant() {
		closingState = ClosingState.ToBeClosed;
		stateChanged();
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
	
	// Actions
		
	/*
	 * An action to seat the customer. He leads the customer to the table
	 * and goes off-screen.
	 */
	private void seatCustomer(MyCustomer c) {
		gui.setCustomerGui(c.c.getGui());
		DoPickUpCustomer();
		try {
			movingAround.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		c.c.followMeToTable(new Menu());
		c.s = CustomerState.Seated;
			
		DoSeatCustomer(c.c, c.table);
		try {
			movingAround.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
		
	/*
	 * An action to take the order from the customer. This basically takes
	 * the waiter to the table, and makes him ask the customer what the
	 * choice is. Then he waits till the choice has been conveyed. Then he
	 * goes back off-screen.
	 */
	private void takeOrder(MyCustomer mc) {
		print("Going to table to take order.");
		
		DoGoToTable(mc.table);
		try {
			movingAround.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		mc.c.whatWouldYouLike();
		print("What would you like?");
		mc.s = CustomerState.AskedToOrder;
		
		try {
			waitingForOrder.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		DoGoBack();
		try {
			movingAround.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * This is an action to send a message to the cook telling him the choice
	 * and the table number.
	 */
	protected void passOrderToCook(MyCustomer mc) {
	}
	
	/*
	 * This action makes the waiter serve the order to the customer once it
	 * is ready. He goes to the table of the customer, and serves it and comes
	 * back.
	 */
	private void serve(MyCustomer mc) {
		print("Serving " + mc.c);
		((VkCashierRole)cashier).computeBill(mc.c, mc.choice, this);
		gui.setFoodServed(mc.choice);
		
		DoGoToTable(mc.table);
		try {
			movingAround.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		mc.c.hereIsYourFood();
		mc.os = OrderStatus.Served;
		mc.s = CustomerState.Eating;
		print(mc.c + " has been served.");
		
		DoGoBack();
		try {
			movingAround.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
		
	/*
	 * It informs the host that a customer has left and his/ her table if free.
	 */
	private void customerIsLeaving(MyCustomer mc) {
		print("Customer " + mc.c + " has left.");
		mc.s = CustomerState.Left;
		host.tableIsFree(mc.table);
	}
	
	/*
	 * Action to request break from host.
	 */
	private void requestBreak() {
		state = MyState.RequestedBreak;
		host.wantABreak(this);
		print("I have requested for a break.");
	}
	
	/*
	 * Action to inform host that waiter is back from break.
	 */
	private void ImBack() {
		state = MyState.Working;
		host.ImBackToWork(this);
		print("Told the host that I'm back!");
	}
	
	/*
	 * Action to go to the customer and inform him/ her that the order
	 * can't be made because of the lack of materials. The customer
	 * then chooses something else.
	 */
	private void informCustomerNoMoreFood(MyCustomer mc) {
		DoGoToTable(mc.table);
		try {
			movingAround.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		print(mc.c + ", we have no more of " + mc.choice + ".");
		mc.c.outOfChoice(mc.m.removeAndReturn(mc.choice).deepCopy(), mc.choice);
		mc.s = CustomerState.InformedNoFood;
		mc.os = OrderStatus.ToBeOrderedAgain;
		
		DoGoBack();
		try {
			movingAround.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Action to give check to a customer.
	 */
	private void giveCheck(MyCustomer mc) {
		gui.giveCheckCaption();
		DoGoToTable(mc.table);
		try {
			movingAround.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		print("Giving check to " + mc.c + "!");
		mc.c.hereIsCheck(mc.check);
		mc.checkGiven = true;
		
		DoGoBack();
		try {
			movingAround.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Used when the customer leaves without eating anything.
	 */
	private void customerIsLeavingWithoutEating(MyCustomer mc) {
		host.customerIsLeavingWithoutEating(mc.c, mc.table);
		customers.remove(mc);
	}
	
	private void enterRestaurant() {
		if (closingState == ClosingState.Closed) {
			closingState = ClosingState.None;
		}
		gui.DoEnterRestaurant();
		try {
			movingAround.acquire();
		} catch (InterruptedException e) {}
		enter = false;
	}
	
	private void leaveRestaurant() {
		if (closingState == ClosingState.None)
			((VkCashierRole) cashier).recordShift((PeopleAgent)myPerson, "Waiter");
		gui.DoLeaveRestaurant();
		try {
			movingAround.acquire();
		} catch (InterruptedException e) {}
		isActive = false;
		leave = false;
		myPerson.msgDone("Waiter");
	}
	
	private void prepareToClose() {
		((VkCashierRole) cashier).recordShift((PeopleAgent)myPerson, "Waiter");
		closingState = ClosingState.Preparing;
	}
	
	private void shutDown() {
		closingState = ClosingState.Closed;
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
	
	// Scheduler
	
	@Override
	protected boolean pickAndExecuteAnAction() {
		if (enter == true) {
			enterRestaurant();
			return true;
		}
		
		if (closingState == ClosingState.ToBeClosed) {
			prepareToClose();
			return true;
		}
		
		if (closingState == ClosingState.Preparing && !((VkHostRole)host).anyCustomer() && leave == true) {
			shutDown();
			leaveRestaurant();
			return true;
		}
		
		if (leave == true && closingState == ClosingState.None) {
			leaveRestaurant();
			return true;
		}
		
		MyCustomer mc;
		
		try {
			// If the waiter is back from a break, he has to inform the host.
			if (state == MyState.BackFromBreak) {
				ImBack();
				return true;
			}
		
			// If the waiter needs a break he requests for a break.
			if (state == MyState.WantABreak) {
				requestBreak();
				return true;
			}
		
			// Finds the first customer in the list whose order has been taken, but has
			// not been given to the cook yet. The order is then passed to the cook.
			mc = findCustomerByOrderStatus(OrderStatus.TakenFromCustomer);
			if (mc != null) {
				passOrderToCook(mc);
				return true;
			}
		
			// Finds the first customer in the list whose order is ready to be served, 
			// and serves him/ her.
			mc = findCustomerByOrderStatus(OrderStatus.ReadyToServe);
			if (mc != null) {
				serve(mc);
				return true;
			}
		
			// Finds the first customer who is leaving without eating and sends a message
			// to the host and removes from his own list.
			mc = findCustomerByCustomerState(CustomerState.LeavingWithoutEating);
			if (mc != null) {
				customerIsLeavingWithoutEating(mc);
				return true;
			}
		
			// Finds the first customer in the list who is leaving, and takes appropriate
			// action for it.
			mc = findCustomerByCustomerState(CustomerState.Leaving);
			if (mc != null) {
				customerIsLeaving(mc);
				return true;
			}
		
			// Searches if there is any customer who can't be served and informs him/ her.
			mc = findCustomerByOrderStatus(OrderStatus.OutOfMaterials);
			if (mc != null) {
				informCustomerNoMoreFood(mc);
				return true;
			}
		
			// Finds a customer who is yet to be given his/ her bill and gives it.
			mc = findCustomerToBeGivenCheck();
			if (mc != null) {
				giveCheck(mc);
				return true;
			}
		
			// Finds the first customer in the list who is waiting to be seated. Then
			// he/ she is seated.
			mc = findCustomerByCustomerState(CustomerState.Waiting);
			if (mc != null) {
				seatCustomer(mc);
				return true;
			}
		
			// Finds the first customer in the list who is ready to order, and takes
			// the order.
			mc = findCustomerByCustomerState(CustomerState.ReadyToOrder);
			if (mc != null) {
				takeOrder(mc);
				return true;
			}
		}
		catch (ConcurrentModificationException e) {
			print("Exception caught.");
			return false;
		}
		
		return false;
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
	
	// Animation methods. DoXYZ() routines.
	
	private void DoGoToTable(int table) {
		gui.DoGoToTable(table);
	}
	
	private void DoSeatCustomer(Customer c, int table) {
		print("Seating " + c + " at " + table);
		gui.DoBringToTable((VkCustomerRole)c, table);
	}
	
	private void DoGoBack() {
		gui.DoLeaveCustomer();
	}
	
	private void DoPickUpCustomer() {
		gui.DoPickUpCustomer();
	}
	
	protected void DoGoToRevolvingStand() {
		gui.DoGoToRevolvingStand();
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
	
	// Utilities
	
	/*
	 * Private helper method to retrieve MyCustomer object that has a
	 * specified Customer object in it.
	 */
	private MyCustomer find(Customer ca) {
		MyCustomer mc = null;
		for (MyCustomer o : customers) {
			if (o.c == ca) {
				mc = o;
				break;
			}
		}
		return mc;
	}
	
	public void setCook(VkCookRole cook) {
		this.cook = cook;
	}
	
	public void setGui(VkWaiterGui gui) {
		this.gui = gui;
	}
	
	public VkWaiterGui getGui() {
		return gui;
	}
	
	public void setCashier(Cashier c) {
		cashier = c;
	}
	
	/*
	 * Helps to find the first customer in the list that has the specified
	 * status.
	 */
	private MyCustomer findCustomerByCustomerState(CustomerState state) {
		for (MyCustomer mc : customers) {
			if (mc.s == state)
				return mc;
		}
		return null;
	}
	
	/*
	 * Helps to find the first customer in the list whose order has the specified
	 * status.
	 */
	private MyCustomer findCustomerByOrderStatus(OrderStatus state) {
		for (MyCustomer mc : customers) {
			if (mc.os == state)
				return mc;
		}
		return null;
	}
	
	/*
	 * 
	 */
	private MyCustomer findCustomerToBeGivenCheck() {
		for (MyCustomer mc : customers) {
			if (mc.check != null && mc.checkGiven == false && mc.s == CustomerState.Eating) {
				return mc;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "Waiter " + ((PeopleAgent)myPerson).name;
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
	
	// Helper Data Structures
	
	/*
	 * List of customer's states once he/ she becomes the  waiter's responsibility.
	 */
	public enum CustomerState {Waiting, Seated, ReadyToOrder, AskedToOrder, Ordered, Eating, Leaving, Left, InformedNoFood, LeavingWithoutEating};
	
	/*
	 * List of customer's order's states once he/ she becomes the  waiter's responsibility.
	 */
	public enum OrderStatus {None, TakenFromCustomer, GivenToCook, ReadyToServe, Served, OutOfMaterials, ToBeOrderedAgain};
	
	public enum MyState {Working, WantABreak, OnBreak, RequestedBreak, BackFromBreak};
	
	public enum ClosingState {None, ToBeClosed, Preparing, Closed};
	
	/*
	 * A private class that encapsulates a customers information.
	 */
	public class MyCustomer {
		Customer c;
		int table;
		String choice;
		CustomerState s;
		OrderStatus os = OrderStatus.None;
		CustomerRestaurantCheck check = null;
		boolean checkGiven = false;
		Menu m = new Menu();
		
		public MyCustomer(Customer c, int table) {
			this.c = c;
			this.table = table;
			this.choice = "";
			s = CustomerState.Waiting;
		}
	}
}