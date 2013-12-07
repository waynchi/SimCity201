package restaurant_wc;

import agent.Agent;
import restaurant_wc.WaiterAgent.AgentState;
import restaurant_wc.gui.WaiterGui;
import restaurant_wc.interfaces.Customer;
import restaurant_wc.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the WcHostAgent. A Host is the manager of a restaurant_wc who sees that all
//is proceeded as he wishes.
public class WcHostAgent extends Agent {
	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<MyCustomer> waitingCustomers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	public Collection<Table> tables;
	//public List<WaiterAgent> MyWaiters = new ArrayList<WaiterAgent>();
	public List<MyWaiter> MyWaiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	public List<WaiterAgent> WaitersAskingForBreak = Collections.synchronizedList(new ArrayList<WaiterAgent>());
	private int customerNum = 0;
	//public WaiterAgent MyWaiter;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented

	private String name;
	public WcHostAgent(String name) {
		super();

		this.name = name;
		// make some tables
		tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
		synchronized(tables){
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
		}
	}

	public void addWaiter(Waiter w) {
		this.MyWaiters.add(new MyWaiter(w));
		stateChanged();
	}
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	/*public List getWaitingCustomers() {
		return waitingCustomers;
	}

	public Collection getTables() {
		return tables;
	}*/
	// Messages

	public void msgIWantFood(Customer cust) {
		waitingCustomers.add(new MyCustomer(cust));
		stateChanged();
	}
	
	public void msgImLeaving(Customer cust) {
		// TODO Auto-generated method stub
		print("removing customer " + cust);
		synchronized(waitingCustomers){
		for(int i = 0; i < waitingCustomers.size(); i++)
		{
			if(waitingCustomers.get(i).Mycustomer == cust)
			{
				waitingCustomers.remove(waitingCustomers.get(i));
				customerNum--;
			}
		}
		}
		
		
		
	}

	public void msgTableIsFree(Customer cust, Waiter w) {
		synchronized(tables){
		for (Table table : tables) {
			if (table.getOccupant() == cust) {
				print(cust + " leaving " + table);
				table.setUnoccupied();
				stateChanged();
			}
		}
		}
		synchronized(MyWaiters){
		for (MyWaiter wa: MyWaiters)
		{
			if(wa.Mywaiter == w)
			{
				wa.amt--;
			}
		}
		}
	}
	
	public void msgRequestBreak(WaiterAgent w) {
		WaitersAskingForBreak.add(w);
		stateChanged();
		
	}
	
	public void msgTakeOffBreak(Waiter waiterAgent) {
		print("Taking " + waiterAgent + " off break.");
		boolean temp = true;
		synchronized(MyWaiters){
		for(MyWaiter w: MyWaiters)
		{
			if(w.Mywaiter == waiterAgent)
			{
				temp = false;
			}
		}
		}
		if(temp){
		MyWaiters.add(new MyWaiter(waiterAgent));
		}
		temp = true;
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
		synchronized(WaitersAskingForBreak){
		for(Waiter w : WaitersAskingForBreak) {
			if(MyWaiters.size() > 1)
			{
				w.msgPermissionToGoOnBreak(true);
				synchronized(MyWaiters){
				for(int i = 0; i < MyWaiters.size(); i++)
				{
					if(MyWaiters.get(i).Mywaiter == w)
					{
						MyWaiters.remove(MyWaiters.get(i));
					}
				}
				}
				print("Break Granted");
				WaitersAskingForBreak.remove(w);
				return true;
			}
			else{
			w.msgPermissionToGoOnBreak(false);
			WaitersAskingForBreak.remove(w);
			print("You can't go on Break!");
			return true;
			}
		}
		}
		
		synchronized(tables){
		for (Table table : tables) {
			if (!table.isOccupied()) {
				if (!waitingCustomers.isEmpty()) {
					//this is how i load balance my waiters. (Waiter with least customers take the new one)
					if(!MyWaiters.isEmpty()){
					MyWaiter tempx = MyWaiters.get(0);
					synchronized(MyWaiters){
					for(MyWaiter tempw : MyWaiters)
					{
						if(tempw.amt < tempx.amt ){
							tempx = tempw;
						}
					}
					}
					tempx.Mywaiter.msgSitAtTable(waitingCustomers.get(0).Mycustomer, table);//the action
					tempx.amt++;
					waitingCustomers.remove(waitingCustomers.get(0));
					}
					//return true;//return true to the abstract agent to reinvoke the scheduler.			
				}
			}
		}
		}
		synchronized(waitingCustomers){
		for(MyCustomer c: waitingCustomers)
			{
				if(!c.informed)
				{
					InformWaiting(waitingCustomers.get(waitingCustomers.size()-1).Mycustomer);
					c.informed = true;
				}
			}
			
		}
		
		


		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}
	private void InformWaiting(Customer customerAgent) {
		// TODO Auto-generated method stub
		if(customerNum >= 20 && waitingCustomers.size() <= customerNum)
		{
			customerNum = 0;
		}
		customerAgent.msgYouNeedToWait(customerNum);
		customerNum ++;

		
	}
	private class MyWaiter {
		Waiter Mywaiter;
		int amt;
		
		public MyWaiter(Waiter MyW){
			Mywaiter = MyW;
			amt = 0;
		}
		}
	
	private class MyCustomer {
		Customer Mycustomer;
		boolean informed;
		
		public MyCustomer(Customer MyC){
			Mycustomer = MyC;
			informed = false;
		}
	}


}

