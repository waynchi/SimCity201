package restaurant_wc;

import agent.Agent;
//import restaurant_wc.WcCustomerRole.AgentEvent;
//import restaurant_wc.WcCustomerRole.AgentState;
import restaurant_wc.gui.WaiterGui;
import restaurant_wc.interfaces.Cashier;
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
public class WaiterAgent extends Agent implements Waiter {
	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<MyCustomer> MyCustomerList = Collections.synchronizedList(new ArrayList<MyCustomer>());
	public List<Order> MyReadyOrders = Collections.synchronizedList(new ArrayList<Order>());
	private WcHostAgent host;
	private WcCookRole cook;
	private Cashier cashier;
	public boolean OnBreak = false;
	private boolean DoIWantToGoOnBreak = false;
	private boolean IWantToGoWork = false;
	
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	
	public enum AgentState
	{DoingNothing, ReadyToOrder, Ordered, WaitingForOrder, BeingSeated, Seated, GaveOrder, OrderIsDelivered, EatingMyOrder, NewMenu, requestingCheck, WaitingForCheck, RecievingCheck, CheckRecieved}
	
	public enum OrderWaiter
	{pending,cooking, cooked, delivered, none};
 
	private String name;
	private String MissingFood = "none";
	private Semaphore TableSet = new Semaphore(0,true);
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore atDoor = new Semaphore(1,true);
	private Semaphore ReadyAtTable = new Semaphore(0,true);
	private Semaphore atCook = new Semaphore(0,true);
	//private Semaphore OrderReady = new Semaphore(1,true);
	public boolean fetching = false;
	public boolean negotiating = false;
	public WaiterGui waiterGui = null;
	private Menu defaultMenu;

	@SuppressWarnings("serial")
	public WaiterAgent(String name) {
		super();

		this.name = name;
		defaultMenu = new Menu(new ArrayList<String>(){{ add("Steak"); add("Chicken"); add("Salad"); add("Pizza");}}, new ArrayList<Double>(){{ add(15.99); add(10.99); add(5.99); add(8.99);}});
		// make some tables
		/*tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}*/
	}

	/* (non-Javadoc)
	 * @see restaurant_wc.Waiter#getTableSet()
	 */
	@Override
	public Semaphore getTableSet() {
		return TableSet;
	}
	/* (non-Javadoc)
	 * @see restaurant_wc.Waiter#getMaitreDName()
	 */
	@Override
	public String getMaitreDName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see restaurant_wc.Waiter#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see restaurant_wc.Waiter#setHost(restaurant_wc.WcHostAgent)
	 */
	@Override
	public void setHost(WcHostAgent host) {
		this.host = host; 
	}
	
	/* (non-Javadoc)
	 * @see restaurant_wc.Waiter#setCook(restaurant_wc.WcCookRole)
	 */
	@Override
	public void setCook(WcCookRole cook) {
		this.cook = cook;
	}
	
	/* (non-Javadoc)
	 * @see restaurant_wc.Waiter#setCashier(restaurant_wc.interfaces.Cashier)
	 */
	@Override
	public void setCashier(Cashier cashier) {
		this.cashier = cashier;
		
	}
	// Messages

	/* (non-Javadoc)
	 * @see restaurant_wc.Waiter#msgSitAtTable(restaurant_wc.interfaces.Customer, restaurant_wc.Table)
	 */
	@Override
	public void msgSitAtTable(Customer cust, Table t)
	{
		t.setOccupant(cust);
		MyCustomer temp = new MyCustomer();
		temp.setCustomer(cust);
		temp.setTable(t);
		temp.state = AgentState.BeingSeated;
		MyCustomerList.add(temp);
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant_wc.Waiter#msgDoIWantToGoOnBreak()
	 */
	@Override
	public void msgDoIWantToGoOnBreak()
	{
		negotiating = true;
		DoIWantToGoOnBreak = true;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant_wc.Waiter#msgIWantToGoWork()
	 */
	@Override
	public void msgIWantToGoWork()
	{
		//negotiating = true;
		IWantToGoWork = true;
		stateChanged();
		OnBreak = false;
	}

	/* (non-Javadoc)
	 * @see restaurant_wc.Waiter#msgLeavingTable(restaurant_wc.interfaces.Customer)
	 */
	@Override
	public void msgLeavingTable(Customer cust) {
		host.msgTableIsFree(cust, this);
		print("Removing all customers named " + cust.getName());
		synchronized(MyCustomerList){
		for(int i = 0; i < MyCustomerList.size(); i++)
		{
			if(MyCustomerList.get(i).MyCust == cust)
			{
				MyCustomerList.remove(MyCustomerList.get(i));
			}
		}
		}
		/*for (Table table : tables) {
			if (table.getOccupant() == cust) {
				print(cust + " leaving " + table);
				table.setUnoccupied();
				stateChanged();
			}
		}*/
	}
	
	/* (non-Javadoc)
	 * @see restaurant_wc.Waiter#msgOrderIsReady(restaurant_wc.Order)
	 */
	@Override
	public void msgOrderIsReady(Order o)
	{
		print("Getting Order " + o.choice);
		synchronized(MyReadyOrders){
		for(final Order order: MyReadyOrders)
		{
			if(order == o)
			{
				o.waiterState = OrderWaiter.cooked;
				stateChanged();
			}
		}
		}
	}

	/* (non-Javadoc)
	 * @see restaurant_wc.Waiter#msgAtTable()
	 */
	@Override
	public void msgAtTable() {//from animation
		//print("msgAtTable() called");
		atTable.release();// = true;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant_wc.Waiter#msgReadyAtTable()
	 */
	@Override
	public void msgReadyAtTable() {
		//print("test");
		ReadyAtTable.release();
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant_wc.Waiter#msgAtDoor()
	 */
	@Override
	public void msgAtDoor() {
		atDoor.release();
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant_wc.Waiter#msgAtCook()
	 */
	@Override
	public void msgAtCook() {
		atCook.release();
		stateChanged();
		
	}
	/* (non-Javadoc)
	 * @see restaurant_wc.Waiter#msgReadyToOrder(restaurant_wc.interfaces.Customer)
	 */
	@Override
	public void msgReadyToOrder(Customer cust) {
		synchronized(MyCustomerList){
		for(MyCustomer c : MyCustomerList)
		{
			if(c.getCustomer() == cust)
			{
				print ("changing state for customer");
				c.state = AgentState.ReadyToOrder;
				stateChanged();
				return;
			}
		}		
		}
	}
	
	/* (non-Javadoc)
	 * @see restaurant_wc.Waiter#msgHereIsMyChoice(java.lang.String, restaurant_wc.interfaces.Customer)
	 */
	@Override
	public void msgHereIsMyChoice(String choice, Customer cust) {
		/*for(MyCustomer c : MyCustomerList)
		{
			if(c.getCustomer() == cust)
			{
				c.setChoice(choice);
				c.state = AgentState.GaveOrder;
				stateChanged();
			}
		}*/
		synchronized(MyCustomerList){
		for(MyCustomer c : MyCustomerList)
		{
			if(c.getCustomer() == cust)
			{
				MyReadyOrders.add(new Order(this, choice, c.MyTable));
				stateChanged();
				return;
			}
		}
		}
		
	}
	/* (non-Javadoc)
	 * @see restaurant_wc.Waiter#msgPermissionToGoOnBreak(boolean)
	 */
	@Override
	public void msgPermissionToGoOnBreak(boolean t) {
		negotiating = false;
		OnBreak = t;
		waiterGui.getGui().setWaiterEnabled(this);
	}
	
	/* (non-Javadoc)
	 * @see restaurant_wc.Waiter#msgIWantMyCheck(java.lang.String, restaurant_wc.interfaces.Customer)
	 */
	@Override
	public void msgIWantMyCheck(String Choice, Customer cust) {
		print("Recieved message requesting a check");
		synchronized(MyCustomerList){
		for(MyCustomer c : MyCustomerList)
		{
			if(c.getCustomer() == cust)
			{
				c.Choice = Choice;
				c.state = AgentState.requestingCheck;
				stateChanged();
			}
		}
		}
	}
	
	/* (non-Javadoc)
	 * @see restaurant_wc.Waiter#msgHereisACheck(double, restaurant_wc.interfaces.Customer)
	 */
	@Override
	public void msgHereisACheck(double amt, Customer customer) {
		print("Recieved Check from Cashier");
		synchronized(MyCustomerList){
		for(MyCustomer c : MyCustomerList)
		{
			if(c.getCustomer() == customer)
			{
				c.Bill = amt;
				c.state = AgentState.RecievingCheck;
				stateChanged();
			}
		}
		}
		
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		try{
		if(DoIWantToGoOnBreak)
		{
			RequestBreak();
		}
		if(IWantToGoWork)
		{
			TakeOffBreak();			
		}
		for(int i = 0; i < MyReadyOrders.size(); i++)
		{
			if(MyReadyOrders.get(i).waiterState == OrderWaiter.pending)
			{
				GiveCookOrder(MyReadyOrders.get(i)); 
			}
			if(MyReadyOrders.get(i).waiterState == OrderWaiter.cooked){
			/*try {
				OrderReady.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
				MyReadyOrders.get(i).waiterState = OrderWaiter.none;
				GetandDeliverOrder(MyReadyOrders.get(i));
			//waiterGui.goGetOrder();
			}			
		}
		for(int i = 0; i < MyCustomerList.size(); i++)
		{
			if(MyCustomerList.get(i).state == AgentState.BeingSeated)
			{
				MyCustomerList.get(i).state = AgentState.Seated;
				seatCustomer(MyCustomerList.get(i).MyCust, MyCustomerList.get(i).MyTable, MyCustomerList.get(i));
				return true;
			}
			if(MyCustomerList.get(i).state == AgentState.ReadyToOrder)
			{
				MyCustomerList.get(i).state = AgentState.Ordered;
				RecieveOrder(MyCustomerList.get(i).MyCust, MyCustomerList.get(i).MyTable);
				return true;
			}
			if(MyCustomerList.get(i).state == AgentState.NewMenu)
			{
				MyCustomerList.get(i).state = AgentState.Ordered;
				RecieveNewOrder(MyCustomerList.get(i).MyCust, MyCustomerList.get(i).MyTable, MissingFood);
				return true;
				
			}
					
			/*if(MyCustomerList.get(i).state == AgentState.GaveOrder)
			{
				MyCustomerList.get(i).state = AgentState.WaitingForOrder;
				//Temporary Hack
				//MyCustomerList.get(i).state = AgentState.OrderIsDelivered;	
			}*/
			if(MyCustomerList.get(i).state == AgentState.OrderIsDelivered)
			{
				MyCustomerList.get(i).state = AgentState.EatingMyOrder;
				SendOrder(MyCustomerList.get(i).MyTable);
				return true;
				//OrderReady.release();
			}
			if(MyCustomerList.get(i).state == AgentState.requestingCheck)
			{
				MyCustomerList.get(i).state = AgentState.WaitingForCheck;
				GetBill(MyCustomerList.get(i).MyCust, MyCustomerList.get(i).Choice);
				return true;
			}
			if(MyCustomerList.get(i).state == AgentState.RecievingCheck)
			{
				MyCustomerList.get(i).state = AgentState.CheckRecieved;
				DeliverCheck(MyCustomerList.get(i));
				return true;
			}
		}
		//for(Order o: MyReadyOrders)
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 
		for (Table table : tables) {
			if (!table.isOccupied()) {
				if (!waitingCustomers.isEmpty()) {
					seatCustomer(waitingCustomers.get(0), table);//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.
				}
			}
		}
*/
		return false;
		}
		catch (ConcurrentModificationException e)
		{
			return true;
		}
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}



	// Actions
	

	private void TakeOffBreak() {
		// TODO Auto-generated method stub
		IWantToGoWork = false;
		host.msgTakeOffBreak(this);
	}

	private void RequestBreak() {
		// TODO Auto-generated method stub
		host.msgRequestBreak(this);
		DoIWantToGoOnBreak = false;
	}

	private void DeliverCheck(MyCustomer cust) {
		Do("Delivering check to " + cust.getCustomer());
		cust.MyCust.msgHereIsYourTotal(cust.Bill);
		
		
	}

	private void GetBill(Customer cust, String c) {
		Do("Asking Cashier For a Check");
		cashier.msgHereIsACheck(c, cust, this);
		
	}

	private void GetandDeliverOrder(Order o) {
		//head off screen
		waiterGui.giveCookOrder();
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		waiterGui.OrderAppearing(true, o.choice);
		waiterGui.DoBringFoodToTable(o.table.occupiedBy, o.table.tableNumber);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		waiterGui.OrderAppearing(false, o.choice);
		synchronized(MyCustomerList){
		for(MyCustomer c : MyCustomerList)
		{
			if(c.getCustomer() == o.table.occupiedBy)
			{
				c.state = AgentState.OrderIsDelivered;
				stateChanged();
				break;
			}
		}
		}
		waiterGui.DoLeaveCustomer();
		//only temporarily here. Need to move to GUI
		//OrderReady.release();
		while(MyReadyOrders.contains(o))
		{
			MyReadyOrders.remove(o);
		}
		
		
	}

	private void GiveCookOrder(Order o) {
		waiterGui.giveCookOrder();
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		stateChanged();
		cook.msgHereIsAnOrder(o);	
		o.waiterState = OrderWaiter.cooking;
		waiterGui.DoLeaveCustomer();
	}
	
	private void SendOrder(Table myTable) {
		myTable.occupiedBy.msgHereIsYourFood();
		/*if(name.equalsIgnoreCase("onbreak"))
		{
			host.msgRequestBreak(this);
			print("Requesting Break");
		}*/
	}

	private void RecieveOrder(Customer customer, Table table) {
		MissingFood = "none";
		waiterGui.GoToTable(customer, table);
		try {
			ReadyAtTable.acquire();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		stateChanged();
		customer.msgWhatWouldYouLike(new Menu((ArrayList<String>) defaultMenu.Choices, defaultMenu.FoodCosts));
		//customer.msgWhatWouldYouLike(new Menu(new ArrayList<String>(){{ add("Steak"); add("Chicken"); add("Salad"); add("Pizza");}}));
		waiterGui.DoLeaveCustomer();
		
	}
	
	private void RecieveNewOrder(Customer customer, Table table, String outOfChoice) {
		MissingFood = "none";
		waiterGui.GoToTable(customer, table);
		try {
			ReadyAtTable.acquire();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		stateChanged();
		customer.msgWhatElseWouldYouLike(outOfChoice);
		waiterGui.DoLeaveCustomer();
		
	}

	/* (non-Javadoc)
	 * @see restaurant_wc.Waiter#seatCustomer(restaurant_wc.interfaces.Customer, restaurant_wc.Table, restaurant_wc.WaiterAgent.MyCustomer)
	 */
	@Override
	public void seatCustomer(Customer customer, Table table, MyCustomer myCust) {
		DoSeatCustomer(customer, table);
			try {
				TableSet.acquire();
			} catch (InterruptedException e1) {
				
				e1.printStackTrace();
			}
		customer.msgSitAtTable(this);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		//myCust.state = AgentState.DoingNothing;
		stateChanged();
		table.setOccupant(customer);
		
		waiterGui.DoLeaveCustomer();
	}

	// The animation DoXYZ() routines
	private void DoSeatCustomer(Customer customer, Table table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		//while(waiterGui.AmIAtDoor());
		fetching = true;
		try {
			atDoor.acquire();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		print("Seating " + customer + " at " + table);
		waiterGui.DoBringToTable(customer, table.tableNumber);
		
		//customer.getGui().setTableNumber(table.tableNumber);
		

	}
	

	/* (non-Javadoc)
	 * @see restaurant_wc.Waiter#msgOutOfChoice(java.lang.String, restaurant_wc.interfaces.Customer)
	 */
	@Override
	public void msgOutOfChoice(String choice, Customer cust) {
		MissingFood = choice;
		synchronized(MyCustomerList){
		for(MyCustomer c : MyCustomerList)
		{
			if(c.getCustomer() == cust)
			{
				c.state = AgentState.NewMenu;
				stateChanged();
				break;
			}
		}
		}
		
	}

	//utilities

	/* (non-Javadoc)
	 * @see restaurant_wc.Waiter#setGui(restaurant_wc.gui.WaiterGui)
	 */
	@Override
	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	/* (non-Javadoc)
	 * @see restaurant_wc.Waiter#getGui()
	 */
	@Override
	public WaiterGui getGui() {
		return waiterGui;
	}

	public class MyCustomer {
		Customer MyCust;
		Table MyTable;
		String Choice;
		double Bill;
		
		
		public AgentState state = AgentState.DoingNothing;
		
		void setChoice(String choice){
			Choice = choice;
		}

		String getChoice(){
			return Choice;
		}
		void setCustomer(Customer cust){
			MyCust = cust;
		}
		
		Customer getCustomer(){
			return MyCust;
		}
		
		void setTable(Table table)
		{
			MyTable = table;
		}
		
		//Table getTable(){
			//return MyTable;
		//}
		}


	}

/*	private class Table {
		WcCustomerRole occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(WcCustomerRole cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		WcCustomerRole getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}*/

