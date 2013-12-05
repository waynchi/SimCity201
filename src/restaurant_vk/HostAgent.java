package restaurant_vk;

import restaurant_vk.gui.HostGui;
import restaurant_vk.interfaces.Customer;
import restaurant_vk.interfaces.Host;
import restaurant_vk.interfaces.Waiter;
import java.util.*;
import java.util.concurrent.Semaphore;

import people.Role;

/**
 * @author Vikrant Singhal
 *
 * Restaurant Host Agent. A Host is the manager of a restaurant who sees that all
 * is proceeded as he wishes.
 */
public class HostAgent extends Role implements Host{
	static final int NTABLES = 4;//a global for the number of tables.
	public List<Customer> customers = Collections.synchronizedList(new ArrayList<Customer>());
	public List<Waiter> waiters = new ArrayList<Waiter>();
	private List<MyWaiter> myWaiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	public Collection<Table> tables;

	private String name;
	
	private int waiterIndex = 0; 
	
	// To be removed.
	private Semaphore atTable = new Semaphore(0,true);
	
	public HostGui hostGui = null;

	public HostAgent(String name) {
		super();

		this.name = name;
		
		// Make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));
		}
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
	
	// Messages

	/*
	 * A message called by the customer once he/ she is hungry and arrives at
	 * the restaurant.
	 */
	public void IWantToEat(Customer cust) {
		synchronized (customers) {
			customers.add(cust);
		}
		stateChanged();
	}
	
	/*
	 * This message is used by the waiter to inform the host that a customer has
	 * left and his/ her table is unoccupied.
	 */
	public void tableIsFree(int table) {
		Table t = ((ArrayList<Table>) tables).get(table - 1);
		t.setUnoccupied();
		System.out.println("Table " + table + " is free.");
		stateChanged();
	}
	
	/*
	 * A message called by the waiter when he wants to take a break.
	 */
	public void wantABreak(Waiter w) {
		MyWaiter mw = find(w);
		mw.s = WaiterState.BreakRequested;
		stateChanged();
	}
	
	/*
	 * A message by the waiter once he has returned from break.
	 */
	public void ImBackToWork(Waiter w) {
		MyWaiter mw = find(w);
		mw.s = WaiterState.Working;
		stateChanged();
	}

	// To be removed.
	public void msgAtTable() {//from animation
		print("msgAtTable() called");
		atTable.release();// = true;
		stateChanged();
	}
	
	/*
	 * This is called by the gui to add a newly created waiter to the
	 * list of waiters.
	 */
	public void addWaiter(Waiter w) {
		synchronized (waiters) {
			waiters.add(w);
		}
		MyWaiter mw = new MyWaiter(w);
		synchronized (myWaiters) {
			myWaiters.add(mw);
		}
		stateChanged();
	}
	
	/*
	 * A message given by the customer when waiting in line and wants to leave.
	 */
	public void ICantWait(Customer c) {
		print("Okay, I'm sorry!");
		synchronized (customers) {
			customers.remove(c);
		}
		stateChanged();
	}
	
	/*
	 * A message called by the waiter when customer leaves without eating.
	 */
	public void customerIsLeavingWithoutEating(Customer c, int table) {
		synchronized (customers) {
			customers.remove(c);
		}
		Table t = ((ArrayList<Table>) tables).get(table - 1);
		t.setUnoccupied();
		System.out.println("Table " + table + " is free.");
		stateChanged();
	}

	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
	
	// Scheduler
	
	/*
	 * It carries out the job of seating customers, informing them if all tables
	 * are occupied, and decides if a waiter could take a break.
	 */
	public boolean pickAndExecuteAnAction() {
		// If there is any waiter who has requested a break, then the host decides
		// whether he should be given a break.
		MyWaiter mw = findWaiterByState(WaiterState.BreakRequested);
		if (mw != null) {
			decideBreakForWaiter(mw);
			return true;
		}
		
		// If there are unoccupied tables, waiters available and customers ready
		// to be seated, then the host assigns the customers to waiters.
		synchronized (tables) {
			for (Table table : tables) {
				if (!table.isOccupied() && !waiters.isEmpty()) {
					for (Customer c : customers) {
						if (c.isReadyToSit()) {
							assignCustomerToWaiter(table.tableNumber, c);
							return true;
						}
					}
				}
			}
		}
		
		// Checks if any customer who is waiting to be seated and informs him/ her
		// that all tables are full.
		synchronized (customers) {
			for (Customer c : customers) {
				if (c.canLeaveWhileWaiting()) {
					tablesAreFull(c);
					return true;
				}
			}
		}

		return false;
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/

	// Actions
	
	/*
	 * Assigns a customer to a waiter and sets the table
	 * to be occupied.
	 */
	private void assignCustomerToWaiter(int table, Customer c) {
		print(c + " is hungry. He would be seated at table " + table);
		Waiter w = selectWaiter();
		c.setWaiter(w);
		w.sitAtTable(c, table);
		Table t = ((ArrayList<Table>) tables).get(table - 1);
		t.setOccupant(c);	
	}
	
	/*
	 * Decides whether the specified waiter will take a break or not.
	 */
	private void decideBreakForWaiter(MyWaiter mw) {
		if (myWaiters.size() == 1) {
			mw.s = WaiterState.Working;
			mw.w.noBreak();
			print("No break for " + mw.w + "!");
			return;
		}
		int count = 0;
		synchronized (myWaiters) {
			for (MyWaiter ref : myWaiters) {
				if (ref.s != WaiterState.OnBreak) {
					count++;
				}
				if (count > 1) {
					mw.s = WaiterState.OnBreak;
					mw.w.takeABreak();
					print("Break for " + mw.w + "!");
					return;
				}
			}
		}
		if (count == 1) {
			mw.s = WaiterState.Working;
			mw.w.noBreak();
			print("No break for " + mw.w + "!");
		}
	}
	
	private void tablesAreFull(Customer c) {
		c.tablesAreFull();
		print("Tables are full!");
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/

	// Utilities

	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}
	
	public List<Waiter> getWaiters() {
		return waiters;
	}
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List<Customer> getWaitingCustomers() {
		return customers;
	}
	
	/*
	 * A method to select a waiter from the list of waiters via a
	 * round-robin scheme.
	 */
	private Waiter selectWaiter() {
		Waiter w = null;
		synchronized (myWaiters) {
			while (true) {
				waiterIndex = waiterIndex % waiters.size();
				MyWaiter mw = myWaiters.get(waiterIndex);
				if (mw.s != WaiterState.OnBreak) {
					w = mw.w;
					break;
				}
				waiterIndex++;
			}
		}
		waiterIndex++;
		return w;
	}
	
	private MyWaiter find(Waiter w) {
		synchronized (myWaiters) {
			for (MyWaiter mw : myWaiters) {
				if (mw.w == w)
					return mw;
			}
		}
		return null;
	}
	
	private MyWaiter findWaiterByState(WaiterState state) {
		synchronized (myWaiters) {
			for (MyWaiter mw : myWaiters) {
				if (mw.s == state) {
					return mw;
				}
			}
		}
		return null;
	}
	
	public List<Customer> getCustomers() {
		return customers;
	}
	
	public boolean anyCustomer() {
		for (Customer c : customers) {
			if (c.isReadyToSit())
				return true;
		}
		for (Table t : tables) {
			if (t.isOccupied())
				return true;
		}
		return false;
	}
	
	public boolean isATableOccupied() {
		synchronized (tables) {
			for (Table t : tables) {
				if (t.isOccupied()) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/

	// Helper Data Structures
	
	private class Table {
		Customer occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(Customer cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}
	
	private class MyWaiter {
		private Waiter w;
		private WaiterState s;
		
		public MyWaiter(Waiter waiter) {
			w = waiter;
			s = WaiterState.Working;
		}
	}
	
	enum WaiterState {BreakRequested, OnBreak, Working};
}