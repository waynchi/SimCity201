package restaurant_wc;

import restaurant.gui.HostGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import restaurant_wc.interfaces.Host;
import restaurant_wc.interfaces.Waiter;

import java.util.*;

import people.People;
import people.Role;

/**
 * Restaurant Host Agent
 */
//A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class HostRoleWc extends Role implements Host{
	static final int NTABLES = 3;//a global for the number of tables.
	private List<Waiter> allWaiters = Collections.synchronizedList(new ArrayList<Waiter>());
	private List<People> workers = Collections.synchronizedList(new ArrayList<People>());
	
	private boolean leaveWork = false;
	private boolean closeRestaurant = false;

	public HostGui hostGui = null;
	private Cashier cashier;
	private Cook cook;
	
	public Collection<Table> tables;
	public class Table {
		RestaurantCustomerRoleWc occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(RestaurantCustomerRoleWc cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		RestaurantCustomerRoleWc getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}

	
	
	public List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	public enum waiterStatus{onBreak, Working, askingForBreak};	
	private int waiterCount = 0;
	public class MyWaiter {
		Waiter w;
		waiterStatus s;

		public MyWaiter (Waiter waiter) {
			w = waiter;
			s = waiterStatus.Working;
		}
		public void msgBreakApproved() {
			// TODO Auto-generated method stub

		}	
	}


	//total number of customers waiting or eating in restaurant
	private int customerCount = 0;
	private List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	private enum customerState{pending, informed, waiting, seated, leaving};
	public class MyCustomer {
		RestaurantCustomerRoleWc customer;
		customerState state;

		public MyCustomer (RestaurantCustomerRoleWc cust) {
			customer = cust;
			if (customerCount >= NTABLES){
				state = customerState.pending;
			}
			else {
				state = customerState.waiting;
			}
		}
	}
	
	
	
	// constructor
	public HostRoleWc() {
		super();
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));
		}
	}

	
	
	// Messages
	public void msgIsActive() {
		isActive = true;
		if(!workers.contains(this.getPersonAgent())) workers.add(this.getPersonAgent());
		getPersonAgent().getRestaurant(3).isClosed = false;
		getPersonAgent().CallstateChanged();
	}

	public void msgIsInActive() {
		leaveWork = true;
		getPersonAgent().CallstateChanged();

	}

	public void msgSetClose() {
		closeRestaurant = true;
		getPersonAgent().CallstateChanged();
	}
	
	
	public void addWaiter(Waiter w){
		allWaiters.add(w);
		waiters.add(new MyWaiter(w));
		if(!workers.contains(((BaseWaiterRole)w).getPersonAgent())) workers.add(((BaseWaiterRole)w).getPersonAgent());
		getPersonAgent().CallstateChanged();
	}

	// from restaurant panel, "Go On Break" button is pressed
	public void IWantABreak(Waiter waiter) {
		synchronized (waiters){
			for (MyWaiter w : waiters) {
				if (waiter.getName().equals(w.w.getName())) {
					w.s = waiterStatus.askingForBreak;
					getPersonAgent().CallstateChanged();

				}
			}
		}
	}

	public void IAmOffBreak(Waiter waiter) {
		print ("Hey "+waiter.getName()+", welcome back to work!");
		synchronized (waiters){
			for (MyWaiter w : waiters) {
				if (waiter.getName().equals(w.w.getName())) {
					w.s = waiterStatus.Working;
					getPersonAgent().CallstateChanged();

				}
			}
		}
	}

	// from hungry CustomerAgent, add to the list
	public void IWantToEat(RestaurantCustomerRoleWc cust) {

		customers.add(new MyCustomer(cust));
		System.out.println("got message i want to eat from customer");
		getPersonAgent().CallstateChanged();

	}

	public void leaveRestaurant(RestaurantCustomerRoleWc cust){
		synchronized(customers){
			for (MyCustomer mc : customers){
				if (mc.customer == cust) {
					mc.state = customerState.leaving;
					getPersonAgent().CallstateChanged();

				}
			}
		}
	}

	public void waitInRestaurant(RestaurantCustomerRoleWc cust){
		synchronized(customers){

			for (MyCustomer mc : customers){
				if (mc.customer == cust) {
					mc.state = customerState.waiting;
					getPersonAgent().CallstateChanged();

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
							mc.state = customerState.leaving;
						}
					}
					customerCount--;
					table.setUnoccupied();
					getPersonAgent().CallstateChanged();

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
					if (mc.state == customerState.leaving) {
						customers.remove(mc);
						return true;
					}
				}
			}
		}

		synchronized(waiters){

			for (MyWaiter waiter : waiters) {
				if (waiter.s == waiterStatus.askingForBreak) {
					replyToBreakRequest(waiter);
					return true;

				}		
			}
		}

		if (!customers.isEmpty()){
			synchronized(customers){

				for (MyCustomer mc : customers) {
					if(mc.state == customerState.waiting){
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

										while (waiters.get(waiterCount).s == waiterStatus.onBreak){
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
				if (mc.state == customerState.pending) {
					if (customerCount >= NTABLES){
						tellCustomerRestIsFull(mc);
						return true;
					}
				}
			}
		}
		
		if (leaveWork) {
			done();
			return true;
		}
		
		if(closeRestaurant) {
			closeRestaurant();
			return true;
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void tellCustomerRestIsFull (MyCustomer mc) {
		print ("Hi "+ mc.customer.getName() + ", restaurant is full now, do you want to wait?");
		mc.customer.msgYouNeedToWait();
		mc.state = customerState.informed;
	}

	private void TellWaiterToSeatCustomer(MyCustomer mc, Waiter waiter, Table table) {
		print("Please take "+mc.customer.getName()+ " to table#" + table.tableNumber);
		waiter.SitAtTable(mc.customer, table.tableNumber);
		mc.state = customerState.seated;
		table.setOccupant (mc.customer);
	}

	public void replyToBreakRequest(MyWaiter waiter) {
		synchronized(waiters){

			for (MyWaiter temp : waiters) {
				if (temp.s == temp.s.Working && temp!=waiter) {
					print ("Okay "+waiter.w.getName()+", you can have a break!");
					waiter.s = waiterStatus.onBreak;
					waiter.w.msgBreakApproved();
					//availableWaiters.remove(waiter.w);
					return;
				}
			}
		}
		print ("Sorry "+waiter.w.getName()+" , you are the only available waiter now and we're"
				+ " counting on you. Fight on!");
		waiter.s = waiterStatus.Working;
		waiter.w.msgBreakDenied();
	}
	
	
	private void done() {
		isActive = false;
		leaveWork = false;
		// reset the two lists of waiters
		waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
		allWaiters = Collections.synchronizedList(new ArrayList<Waiter>());
		workers = Collections.synchronizedList(new ArrayList<People>());
		getPersonAgent().msgDone("RestaurantHostRole");
	}
	
	private void closeRestaurant() {
		getPersonAgent().getRestaurant(3).isClosed = true;
		closeRestaurant = false;
	}

	//utilities

	public String getMaitreDName() {
		return getPersonAgent().getName();
	}

	public String getName() {
		return getPersonAgent().getName();
	}


	public List<Waiter> getWaiters() {
		return allWaiters;
	}

	public Collection<Table> getTables() {
		return tables;
	}

	public void setCashier(Cashier c) {
		cashier = c;
		if(!workers.contains(((CashierRoleWc)cashier).getPersonAgent())) workers.add(((CashierRoleWc)cashier).getPersonAgent());
	}

	public Cashier getCashier() {
		return cashier;
	}

	public void setCook(Cook c) {
		cook = c;
		if(!workers.contains(((CookRoleWc)cook).getPersonAgent())) workers.add(((CookRoleWc)cook).getPersonAgent());
	}

	public Cook getCook() {
		return cook;
	}

	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}

	public boolean isActive() {
		return isActive;
	}

	@Override
	public People getPerson() {
		// TODO Auto-generated method stub
		return getPersonAgent();
	}

	@Override
	public List<Waiter> getAvailableWaiters() {
		List<Waiter> temp = new ArrayList<Waiter>();
		for (MyWaiter mw: waiters){
			if (mw.s == waiterStatus.Working) {
				temp.add(mw.w);
			}
		}
		return temp;
	}
	
	public int getCustomerSize() {
		return customers.size();
	}
	
	public List<People> getWorkers () {
		return workers;
	}	
}
