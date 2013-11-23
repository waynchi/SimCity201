package restaurant;

import agent.Agent;
import restaurant.gui.HostGui;

import java.util.*;

import people.Role;

/**
 * Restaurant Host Agent
 */
//A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class HostRole extends Role {
	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	private List<BaseWaiterRole> availableWaiters = Collections.synchronizedList(new ArrayList<BaseWaiterRole>());
	private List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	private enum customerState{PENDING, ASKED_WHETHER_TO_WAIT, WAITING, SEATED, LEAVING};


	public List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	public enum waiterStatus{ON_BREAK, AT_WORK, ASKING_FOR_BREAK};	
	private int waiterCount = 0;

	//total number of customers waiting or eating in restaurant
	private int customerCount = 0;

	public Collection<Table> tables;

	private String name;
	public HostGui hostGui = null;
	private CashierRole cashier;
	private CookRole cook;

	public HostRole(String name) {
		super();
		this.name = name;
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));
		}
	}

	// Messages


	public void addWaiter(BaseWaiterRole w){
		availableWaiters.add(w);
		waiters.add(new MyWaiter(w));
		stateChanged();
	}

	// from restaurant panel, "Go On Break" button is pressed
	public void IWantABreak(BaseWaiterRole waiter) {
		synchronized (waiters){
			for (MyWaiter w : waiters) {
				if (waiter.getName().equals(w.w.getName())) {
					w.s = waiterStatus.ASKING_FOR_BREAK;
					stateChanged();
				}
			}
		}
	}

	public void IAmOffBreak(BaseWaiterRole waiter) {
		print ("Hey "+waiter.getName()+", welcome back to work!");
		availableWaiters.add(waiter);
		synchronized (waiters){
			for (MyWaiter w : waiters) {
				if (waiter.getName().equals(w.w.getName())) {
					w.s = waiterStatus.AT_WORK;
					stateChanged();
				}
			}
		}
	}

	// from hungry CustomerAgent, add to the list
	public void IWantToEat(RestaurantCustomerRole cust) {

		customers.add(new MyCustomer(cust));
		stateChanged();
	}

	public void leaveRestaurant(RestaurantCustomerRole cust){
		synchronized(customers){
			for (MyCustomer mc : customers){
				if (mc.customer == cust) {
					mc.state = customerState.LEAVING;
					stateChanged();
				}
			}
		}
	}

	public void waitInRestaurant(RestaurantCustomerRole cust){
		synchronized(customers){

			for (MyCustomer mc : customers){
				if (mc.customer == cust) {
					mc.state = customerState.WAITING;
				}
			}
		}
	}

	// from waiter, informing that customer has left and table is available
	public void msgTableIsFree(int tableNum) {
		synchronized(customers){

			for (Table table : tables) {
				if (table.tableNumber == tableNum) {
					print(table.getOccupant() + " leaving " + table);
					for (MyCustomer mc : customers){
						if (mc.customer == table.getOccupant()) {
							mc.state = customerState.LEAVING;
						}
					}
					customerCount--;
					table.setUnoccupied();
					stateChanged();
				}
			}
		}
	}



	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer and waiter,
            so that table is unoccupied and customer is waiting and waiter is available
            If so tell the waiter to seat him at the table.
		 */

		if (!customers.isEmpty()){
			synchronized(customers){

				for (MyCustomer mc : customers) {
					if (mc.state == customerState.LEAVING) {
						customers.remove(mc);
						return true;
					}
				}
			}
		}

		synchronized(waiters){

			for (MyWaiter waiter : waiters) {
				if (waiter.s == waiterStatus.ASKING_FOR_BREAK) {
					replyToBreakRequest(waiter);
					return true;

				}		
			}
		}

		if (!customers.isEmpty()){
			synchronized(customers){

				for (MyCustomer mc : customers) {
					if(mc.state == customerState.WAITING){
						if (!waiters.isEmpty()) {
							for (Table table : tables) {					
								if (!table.isOccupied()) {
									//print ("waiterCount: " + waiterCount);
									if (waiterCount == waiters.size()-1) {
										waiterCount = 0;
									}

									else {
										waiterCount++;
									}
									synchronized(waiters){

										while (waiters.get(waiterCount).s == waiterStatus.ON_BREAK){
											// find the first waiter that is at work
											//print("shouldn't be in this while loop for the first waiter");
											if (waiterCount != waiters.size()-1) {
												waiterCount ++;
											}
											else {
												waiterCount = 0;
											}
										}
									}
									TellWaiterToSeatCustomer( mc, waiters.get(waiterCount).w, table);
									customerCount++;
									//print ("in host scheduler"+waiterCount + waiters.size());


									return true;//return true to the abstract agent to reinvoke the scheduler.
								}
							}
						}
					}
				}

			}
		}

		synchronized(customers){

			for (MyCustomer mc : customers) {
				if (mc.state == customerState.PENDING) {
					if (customerCount >= NTABLES){
						tellCustomerRestIsFull(mc);
						return true;
					}
				}
			}
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void tellCustomerRestIsFull (MyCustomer mc) {
		print ("Hi "+ mc.customer.getName() + ", restaurant is full now, do you want to wait?");
		mc.customer.msgRestaurantIsFull();
		mc.state = customerState.ASKED_WHETHER_TO_WAIT;
	}

	private void TellWaiterToSeatCustomer(MyCustomer mc, BaseWaiterRole waiter, Table table) {
		print("Please take "+mc.customer.getName()+ " to table#" + table.tableNumber);
		waiter.SitAtTable(mc.customer, table.tableNumber);
		mc.state = customerState.SEATED;
		table.setOccupant (mc.customer);
	}

	public void replyToBreakRequest(MyWaiter waiter) {
		synchronized(waiters){

			for (MyWaiter temp : waiters) {
				if (temp.s == temp.s.AT_WORK && temp!=waiter) {
					print ("Okay "+waiter.w.getName()+", you can have a break!");
					waiter.s = waiterStatus.ON_BREAK;
					waiter.w.msgBreakApproved();
					//availableWaiters.remove(waiter.w);
					return;
				}
			}
		}
		print ("Sorry "+waiter.w.getName()+" , you are the only available waiter now and we're"
				+ " counting on you. Fight on!");
		waiter.s = waiterStatus.AT_WORK;
		waiter.w.msgBreakDenied();
	}

	//utilities

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List<BaseWaiterRole> getAvailableWaiters() {
		return availableWaiters;
	}

	public Collection getTables() {
		return tables;
	}

	public void setCashier(CashierRole c) {
		cashier = c;
	}

	public CashierRole getCashier() {
		return cashier;
	}
	
	public void setCook(CookRole c) {
		cook = c;
	}
	
	public CookRole getCook() {
		return cook;
	}
	
	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}

	public class Table {
		RestaurantCustomerRole occupiedBy;
		int tableNumber;


		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(RestaurantCustomerRole cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		RestaurantCustomerRole getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}

	public class MyWaiter {
		BaseWaiterRole w;
		waiterStatus s;

		public MyWaiter (BaseWaiterRole waiter) {
			w = waiter;
			s = waiterStatus.AT_WORK;
		}

		public void msgBreakApproved() {
			// TODO Auto-generated method stub

		}
	}

	public class MyCustomer {
		RestaurantCustomerRole customer;
		customerState state;

		public MyCustomer (RestaurantCustomerRole cust) {
			customer = cust;
			if (customerCount >= NTABLES){
				state = customerState.PENDING;
			}
			else {
				state = customerState.WAITING;
			}
		}
	}
}
