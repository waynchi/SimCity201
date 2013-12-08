package restaurant_ps;

import agent.Agent;
import restaurant_ps.gui.HostGui;
import restaurant_ps.interfaces.Customer;
import restaurant_ps.interfaces.Host;
import restaurant_ps.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

import people.Role;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class HostAgent extends Role implements Host {
	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public enum State {nothing, atWaitingArea, beingToldThatCustomerTablesAreFull, toldThatCustomerTablesAreFull};
	class WaitingCustomer{
		Customer c;
		State st;
		WaitingCustomer(Customer cust)
		{
			c = cust;
			st = State.nothing;
		}
	};
	public List<WaitingCustomer> myWaitingCustomers = Collections.synchronizedList(new ArrayList<WaitingCustomer>());
	//public List<CustomerAgent> waitingCustomers = new ArrayList<CustomerAgent>();
	public Collection<Table> tables;
	
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	
	public List<WaiterAgent>waiters = Collections.synchronizedList(new ArrayList<WaiterAgent>());
	

	private String name;
	private Semaphore atTable = new Semaphore(0,true);

	public HostGui hostGui = null;

	Waiter waiterRejectedToSetOnBreak;;
	Waiter waiterAllowedToSetOnBreak;
	
	
	public HostAgent(String name) {
		super();
		this.name = name;
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
	}

	/* (non-Javadoc)
	 * @see restaurant.Host#getMaitreDName()
	 */
	@Override
	public String getMaitreDName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see restaurant.Host#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Host#addWaiter(restaurant.WaiterAgent)
	 */
	@Override
	public void addWaiter(WaiterAgent w) {
		waiters.add(w);
	}

//	public List<CustomerAgent> getWaitingCustomers() {
//		return waitingCustomers;
//	}

	/* (non-Javadoc)
	 * @see restaurant.Host#getTables()
	 */
	@Override
	public Collection<Table> getTables() {
		return tables;
	}
	// Messages

	/* (non-Javadoc)
	 * @see restaurant.Host#msgIWantFood(restaurant.CustomerAgent)
	 */
	public void msgArrivedToWaitingArea(Customer cust) {
		// TODO Auto-generated method stub
		Do(cust.getCustomerName() + "arrived to waiting");
		WaitingCustomer myCust = findCustomer(cust);
		myCust.st = State.atWaitingArea;
		stateChanged();
		System.out.println(myCust.c.getCustomerName() + "'s state is " + myCust.st.toString());
	}
	
	@Override
	public void msgIWantFood(Customer cust) {
		WaitingCustomer c = new WaitingCustomer(cust);
		if(allTablesAreOccupied() || cust.getCustomerName().equals("lieitsfull"))
		{
			c.st = State.beingToldThatCustomerTablesAreFull;
		}
		myWaitingCustomers.add(c);
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Host#msgImLeaving(restaurant.interfaces.Customer)
	 */
	@Override
	public void msgImLeaving(Customer cust) {
		// TODO Auto-generated method stub
		WaitingCustomer myCust = findCustomer(cust);
		myWaitingCustomers.remove(myCust);
		stateChanged();
	}
	

	/* (non-Javadoc)
	 * @see restaurant.Host#msgLeavingTable(restaurant.interfaces.Customer)
	 */
	@Override
	public void msgLeavingTable(Customer cust) {
		for (Table table : tables) {
			if (table.getOccupant() == cust) {
				//print(cust + " leaving " + table);
				table.setUnoccupied();
				stateChanged();
			}
		}
	}

	/* (non-Javadoc)
	 * @see restaurant.Host#msgAtTable()
	 */
	@Override
	public void msgAtTable() {//from animation
		//print("msgAtTable() called");
		atTable.release();// = true;
		stateChanged();
	
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Host#msgTableIsFree(restaurant.Table)
	 */
	@Override
	public void msgTableIsFree(Table t) {
		// TODO Auto-generated method stub
		t.setUnoccupied();
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Host#msgWantsToGoOnBreak(restaurant.WaiterAgent)
	 */
	@Override
	public void msgWantsToGoOnBreak(Waiter waiterAgent) {
		// TODO Auto-generated method stub
		int availableWaiters = 0;
		for(Waiter w : waiters)
		{
			if(!w.isOnBreak() && !w.isWantingToGoOnBreak())
			{
				availableWaiters++;
			}
		}
		if(waiters.size() > 1 && availableWaiters > 1)
		{
			Do("Allows waiter " + waiterAgent.getName() + " to go on break after he/she finishes customers");
			this.waiterAllowedToSetOnBreak = waiterAgent;
			stateChanged();
			return;
		}
		Do("Doesnt not allow waiter " + waiterAgent.getName() + " to go on break beause there is only one waiter");
		this.waiterRejectedToSetOnBreak = waiterAgent;
		stateChanged();
			
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
		if(waiterAllowedToSetOnBreak != null)
		{
			AllowWaiterToGoOnBreak(waiterAllowedToSetOnBreak);
			return true;
		}
		
		if(waiterRejectedToSetOnBreak != null)
		{
			RejectWaiterToGoOnBreak(waiterRejectedToSetOnBreak);
			return true;
		}
		
		synchronized(myWaitingCustomers)
		{
		for(WaitingCustomer wc : myWaitingCustomers)
		{
			if(wc.st == State.beingToldThatCustomerTablesAreFull)
			{
				wc.st = State.toldThatCustomerTablesAreFull;
				//wc.st = State.atWaitingArea;
				TellCustomerTablesAreFull(wc);
			}
		}
		}
		synchronized(tables)
		{
		for (Table table : tables) {
			if (!table.isOccupied()) {
				if (!myWaitingCustomers.isEmpty() && !waiters.isEmpty() && myWaitingCustomers.get(0).st == State.atWaitingArea) {
					AssignWaiterAndTableToCustomer(myWaitingCustomers.get(0),table);
					return true;//return true to the abstract agent to reinvoke the scheduler.
				}
			}
		}
		}
		

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	private void TellCustomerTablesAreFull(WaitingCustomer wc) {
		// TODO Auto-generated method stub
		wc.c.msgAllTablesAreOccupied();
		
	}

	private boolean allTablesAreOccupied() {
		// TODO Auto-generated method stub
		for(Table t : tables)
		{
			if(!t.isOccupied())
				return false;
		}
		return true;
	}

	// Actions



	private void AssignWaiterAndTableToCustomer(WaitingCustomer cust, Table table) {
		// TODO Auto-generated method stub
		Waiter bestWaiterToAssign = waiterWithLeastAmountOfCustomers();
		Do("Waiter being called");
		bestWaiterToAssign.msgSeatCustomer(cust.c, table);
		cust.c.setWaiter(bestWaiterToAssign);
		myWaitingCustomers.remove(cust);
	}

	private void RejectWaiterToGoOnBreak(Waiter waiter) {
		// TODO Auto-generated method stub
		waiter.msgDoNotGoOnBreak();
		this.waiterRejectedToSetOnBreak = null;
		
	}
	
	private WaitingCustomer findCustomer(Customer cust) {
		for(WaitingCustomer myCust : myWaitingCustomers)
		{
			if(myCust.c == cust)
				return myCust;
		}
		return null;
	}

	private void AllowWaiterToGoOnBreak(Waiter waiter) {
		// TODO Auto-generated method stub
		waiter.msgGoOnBreakAfterFinishingCustomers();
		waiterAllowedToSetOnBreak = null;
	}

	private Waiter waiterWithLeastAmountOfCustomers() {
		// TODO Auto-generated method stub
		Waiter temp = waiters.get(0);
		for(Waiter w: waiters)
		{
			if(!w.isOnBreak())
			{
				temp = w;
				break;
			}
		}
		for(Waiter w : waiters)
		{
			//print(w.getName() + " is serving " + w.numberOfCustomersBeingServed() +" customers.");
			if(w.numberOfCustomersBeingServed() < temp.numberOfCustomersBeingServed())
			{
				if(w.isWantingToGoOnBreak() || w.isOnBreak())
					continue;
				
				temp = w;
			}
		}
		return temp;
	}

	/* (non-Javadoc)
	 * @see restaurant.Host#setGui(restaurant.gui.HostGui)
	 */
	@Override
	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	/* (non-Javadoc)
	 * @see restaurant.Host#getGui()
	 */
	@Override
	public HostGui getGui() {
		return hostGui;
	}

	/* (non-Javadoc)
	 * @see restaurant.Host#pauseWaitersAndTheirCustomers()
	 */
	@Override
	public void pauseWaitersAndTheirCustomers() {
		// TODO Auto-generated method stub
		for(WaiterAgent w : waiters){
			//w.pauseCustomers();
			//w.pauseAgent();
		}
	}
	/* (non-Javadoc)
	 * @see restaurant.Host#restartWaiters()
	 */
	@Override
	public void restartWaiters() {
		for(WaiterAgent w : waiters){
			//w.restartAgent();
			//w.restartCustomers();
		}
	}

	/* (non-Javadoc)
	 * @see restaurant.Host#newWaiter()
	 */
	@Override
	public void newWaiter() {
		// TODO Auto-generated method stub
		stateChanged();
	}
	
	public boolean anyCustomer(){
		if(!myWaitingCustomers.isEmpty())
			return true;
		for(Table t: tables) {
			if(t.isOccupied())
				return true;
		}
		return false;
	}

	

	

	

	
	
	
}



