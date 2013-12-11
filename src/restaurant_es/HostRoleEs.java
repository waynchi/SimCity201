package restaurant_es;

import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import restaurant_es.interfaces.Host;
import restaurant_es.interfaces.Waiter;

import java.util.*;

import people.People;
import people.Role;

/**
 * Restaurant Host Agent
 */
//A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class HostRoleEs extends Role implements Host{
	static final int NTABLES = 3;
	private List<Waiter> allWaiters = Collections.synchronizedList(new ArrayList<Waiter>());
	private List<People> workers = Collections.synchronizedList(new ArrayList<People>());
	private boolean leaveWork = false;
	private boolean setClose = false;
	private Cashier cashier;
	private Cook cook;
	public Collection<Table> tables;
	public List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	public enum waiterStatus{ON_BREAK, AT_WORK, ASKING_FOR_BREAK};	
	private int waiterCount = -1;
	private int customerCount = 0;
	private List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	private enum customerState{PENDING, ASKED_WHETHER_TO_WAIT, WAITING, SEATED, LEAVING};
	
	public HostRoleEs() {
		super();
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));
		}
	}

	// Messages
	public void msgIsActive() {
		print("Received msgIsActive from person agent");
		isActive = true;
		if(!workers.contains(this.getPersonAgent())) workers.add(this.getPersonAgent());
		getPersonAgent().getRestaurant(4).isClosed = false;
		getPersonAgent().CallstateChanged();
	}

	public void msgIsInActive() {
		print("Received msgIsInactive from person agent");
		leaveWork = true;
		getPersonAgent().CallstateChanged();
	}

	public void msgSetClose() {
		print("Need to close restaurant");
		setClose = true;
		getPersonAgent().CallstateChanged();
	}
	
	
	public void addWaiter(Waiter w){
		allWaiters.add(w);
		waiters.add(new MyWaiter(w));
		if(!workers.contains(((BaseWaiterRoleEs)w).getPersonAgent())) workers.add(((BaseWaiterRoleEs)w).getPersonAgent());
		getPersonAgent().CallstateChanged();
	}

	public void IWantToEat(RestaurantCustomerRoleEs cust) {
		print("New customer has arrived. Added him to list");
		customers.add(new MyCustomer(cust));
		getPersonAgent().CallstateChanged();

	}

	public void leaveRestaurant(RestaurantCustomerRoleEs cust){
		print("Customer has left the restaurant.");
		synchronized(customers){
			for (MyCustomer mc : customers){
				if (mc.customer == cust) {
					mc.state = customerState.LEAVING;
					getPersonAgent().CallstateChanged();

				}
			}
		}
	}

	public void waitInRestaurant(RestaurantCustomerRoleEs cust){
		print("Customer is waiting for a spot in the restaraunt");
		synchronized(customers){
			for (MyCustomer mc : customers){
				if (mc.customer == cust) {
					mc.state = customerState.WAITING;
					getPersonAgent().CallstateChanged();

				}
			}
		}
	}

	public void msgTableIsFree(int tableNum) {
		print("Waiter sent a message saying a table is now free.");
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
					getPersonAgent().CallstateChanged();

				}
			}
		}
	}



	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {

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
									if (waiterCount == waiters.size()-1) {
										waiterCount = 0;
									}

									else {
										waiterCount++;
									}
									synchronized(waiters){

										while (waiters.get(waiterCount).s == waiterStatus.ON_BREAK){
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
									return true;
								}
							}
						}
					}
				}

			}
		}
		
		if (leaveWork) {
			done();
			return true;
		}
		
		if(setClose) {
			closeRestaurant();
			return true;
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void TellWaiterToSeatCustomer(MyCustomer mc, Waiter waiter, Table table) {
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
		waiter.s = waiterStatus.AT_WORK;
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
		getPersonAgent().getRestaurant(4).isClosed = true;
		setClose = false;
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
		if(!workers.contains(((CashierRoleEs)cashier).getPersonAgent())) workers.add(((CashierRoleEs)cashier).getPersonAgent());
	}

	public Cashier getCashier() {
		return cashier;
	}

	public void setCook(Cook c) {
		cook = c;
		if(!workers.contains(((CookRoleEs)cook).getPersonAgent())) workers.add(((CookRoleEs)cook).getPersonAgent());
	}

	public Cook getCook() {
		return cook;
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
			if (mw.s == waiterStatus.AT_WORK) {
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
	
	public class MyWaiter {
		Waiter w;
		waiterStatus s;

		public MyWaiter (Waiter waiter) {
			w = waiter;
			s = waiterStatus.AT_WORK;
		}
		public void msgBreakApproved() {
			// TODO Auto-generated method stub

		}	
	}
	
	public class Table {
		RestaurantCustomerRoleEs occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(RestaurantCustomerRoleEs cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		RestaurantCustomerRoleEs getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}
	
	public class MyCustomer {
		RestaurantCustomerRoleEs customer;
		customerState state;

		public MyCustomer (RestaurantCustomerRoleEs cust) {
			customer = cust;
			state = customerState.WAITING;
		}
	}
}
