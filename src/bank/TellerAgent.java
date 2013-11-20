package bank;

import agent.Agent;

import java.util.*;
import java.util.concurrent.Semaphore;


/**
 * Bank Host Agent
 */

public class TellerAgent extends Agent {

	public List<BankCustomerAgent> waitingCustomers
	= Collections.synchronizedList(new ArrayList<BankCustomerAgent>()); //For this prototype there is one teller who will store every waiting customer

	private String name;
	
	public enum WaiterState
	{none, atFront, away, wantBreak, onBreak};

	public TellerAgent(String name) {
		super();

		this.name = name;
		// make some tables
		tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
	}
	
	//Hack to establish connection to waiter
	public void addWaiter(WaiterAgent waiter) {
		waiters.add(new myWaiter(waiter));
		stateChanged();
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List getWaitingCustomers() {
		return waitingCustomers;
	}

	public Collection getTables() {
		return tables;
	}
	// Messages

	public void msgIWantFood(CustomerAgent cust) {
		waitingCustomers.add(cust);
		stateChanged();
	}
	
	public void msgNoWait(CustomerAgent cust) {
		waitingCustomers.remove(cust);
		stateChanged();
	}
	
	public void msgReadyToSeat(WaiterAgent waiter) {
		synchronized(waiters) {
			for (myWaiter mywaiter : waiters) {
				if (mywaiter.waiter.equals(waiter)) {
					mywaiter.state = WaiterState.atFront;
					stateChanged();
				}
			}
		}
	}

	public void msgTableFree(int tableNumber) {
		synchronized(tables) {
			for (Table table : tables) {
				if (table.tableNumber == tableNumber) {
					table.setUnoccupied();
					stateChanged();
				}
			}
		}
	}
	
	public void msgCanIGoOnBreak(WaiterAgent waiter) {
		print("Waiter" + waiter + "asked for break");
		synchronized(waiters) {
			for (myWaiter mywaiter : waiters) {
				if (mywaiter.waiter.equals(waiter)) {
					mywaiter.state = WaiterState.wantBreak;
					stateChanged();
				}
			}
		}
	}
	
	public void msgOffBreak(WaiterAgent waiter) {
		synchronized(waiters) {
			for (myWaiter mywaiter : waiters) {
				if (mywaiter.waiter.equals(waiter)) {
					mywaiter.state = WaiterState.atFront;
					stateChanged();
				}
			}
		}
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		
		if (!waiters.isEmpty()) {
			freeWaiter();
			int open = 0;
			synchronized(tables) {
				for (Table table : tables) {
					if (!table.isOccupied()) {
						if (!waitingCustomers.isEmpty()) {
							seatCustomer(waitingCustomers.get(0), table, currentWaiter);//the action
							return true;//return true to the abstract agent to reinvoke the scheduler.
						}
					}
					if (table.isOccupied()) {
						open++;
					}
				}
			}
			synchronized(waiters) {
				for (myWaiter mywaiter : waiters) {
					if (mywaiter.state == WaiterState.wantBreak) {
						if (waiters.size() > 1) {
							allowBreak(mywaiter);
							return true;
						}
						else {
							denyBreak(mywaiter);
							return true;
						}
					}
				}
			}
			if (open == 3 && !waitingCustomers.isEmpty()) {
				restFull(waitingCustomers.get(0));
				return true;
			}
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void seatCustomer(CustomerAgent customer, Table table, myWaiter mywaiter) {
		print("Sent seat");
		mywaiter.customers++;
		print("Messaging waiter");
		mywaiter.state = WaiterState.away;
		mywaiter.waiter.msgSeatCustomer(customer, table.tableNumber);
		table.setOccupant(customer);
		waitingCustomers.remove(customer);
	}
	
	private void allowBreak(myWaiter waiter) {
		waiter.state = WaiterState.onBreak;
		waiter.waiter.msgGoOnBreak();
	}
	
	private void denyBreak(myWaiter waiter) {
		print("Waiter cannot go on break");
		waiter.state = WaiterState.away;
		waiter.waiter.msgNoBreak();
	}
	
	private void restFull(CustomerAgent c) {
		c.msgRestFull();
	}
	
	//Utilities
	
	private void freeWaiter() {
		if (waiters.size() == 1) {
			currentWaiter = waiters.get(0);
			return;
		}
		else {
			synchronized(waiters) {
				for (myWaiter waiter : waiters) {
					if (waiter.state != WaiterState.onBreak) {
						currentWaiter = waiter;
						break;
					}
				}
			}
			synchronized(waiters) {
				for (myWaiter waiter1 : waiters) {
					if (waiter1.customers < currentWaiter.customers && waiter1.state != WaiterState.onBreak) {
						currentWaiter = waiter1;
					}
				}
			}
		}
	}


	private class Table {
		CustomerAgent occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(CustomerAgent cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		CustomerAgent getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}
	
	private class myWaiter {
		WaiterAgent waiter;
		private WaiterState state = WaiterState.none;
		int customers;
		
		myWaiter(WaiterAgent waiter) {
			this.waiter = waiter;
			this.state = WaiterState.atFront;
			customers = 0;
		}
	}
}

