package restaurant_ps;

import agent.Agent;
import restaurant_ps.gui.WaiterGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import restaurant_ps.interfaces.Customer;
import restaurant_ps.interfaces.Host;
import restaurant_ps.interfaces.Waiter;

import java.util.*;

import people.PeopleAgent;
import people.Role;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class WaiterAgent extends Role implements Waiter {
	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	//public List<CustomerAgent> waitingCustomers = new ArrayList<CustomerAgent>();
	//public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	
	
	
	public enum State { doingNothing, seatingCustomer, withOrderingCustomer, givingOrderBackToCustomer, gettingOrderFromCook, returningFoodToCustomer, servingCustomer, waiting, 
		readyToBeSeated, beingSeated, seated, ready, readyToGiveOrder, waitingForWaiterToGetOrder, 
		givingOrder, ordered, waitingAfterOrder, readyToBeServed, waitingForWaiterToGetFood, served, busy, waitingForWaiterToReturn, 
		aboutToTellCustomerToReorder, beingToldToReOrder, goingToTellCustomerToReorder, goingtoBeToldToReOrder, aboutToGiveCashierWaiterRequest, givingCheckRequestToCashier, readyToBeServedAndCheckIsReady, goingToCustomerTable, waitingForCustomersRequest, askedForCheck, goingToGetCheck, askingCashierForCheck, arrivedToCashier, recievedCheck, goingToGiveCheckToCustomer, readyToGiveCheck, aboutToRecieveCheck, aboutToGoToHomePosition, goingToHomePosition, aboutToGetFoodFromWaiter };
		enum ClosingState {None, ToBeClosed, Preparing, Closed};
		
		public ClosingState closingState;
	
	
	public class MyCust{
		Customer c;
		Table t;
		State st;
		Choice o;
		Check bill;
		
		MyCust(Customer cu, Table ta)
		{
			c = cu;
			t = ta;
			st = State.doingNothing;
			bill = null;
		}
		
	};
	
	
	List<MyCust> myCustomers = new ArrayList<MyCust>();

	private String name;
	private boolean isOnBreak;
	private boolean wantsToGoOnBreak;
	public WaiterGui waiterGui = null;
	public State waiterState = State.doingNothing;
	protected Cook cook;
	private Host host;
	private Cashier cashier;
	List<Food> inventory;
	public RevolvingStand revolvingStand;
	private boolean leave = false;
	private boolean enter = false;
	public WaiterAgent(String name, Cook cook, Host host, List<Food> inventory) {
		super();
		
		
		this.name = name;
		this.host = host;
		this.cook = cook;
		this.inventory = inventory;
		wantsToGoOnBreak = false;
		isOnBreak = false;
		// make some tables
//		tables = new ArrayList<Table>(NTABLES);
//		for (int ix = 1; ix <= NTABLES; ix++) {
//			tables.add(new Table(ix));//how you add to a collections
//		}
	}

	/* (non-Javadoc)
	 * @see restaurant.Waiter#getMaitreDName()
	 */
	public void msgIsActive() {
		isActive = true;
		enter = true;
		stateChanged();
	}
	
	public void msgIsInActive() {
		leave = true;
		stateChanged();
	}
	
	@Override
	public String getMaitreDName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see restaurant.Waiter#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
	

//	public List getWaitingCustomers() {
//		return waitingCustomers;
//	}
//
//	public Collection getTables() {
//		return tables;
//	}
//	// Messages

	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgAnimationFinishedGoToCustomer(restaurant.interfaces.Customer)
	 */
	@Override
	public void msgAnimationFinishedGoToCustomer(Customer cust) {
		// TODO Auto-generated method stub
		waiterState = State.seatingCustomer;
		MyCust c = findCust(cust);
		c.st = State.readyToBeSeated;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgAnimationFinishedGoToSeat(int)
	 */
	public void closeRestaurant() {
		closingState = ClosingState.ToBeClosed;
		stateChanged();
	}
	
	@Override
	public void msgAnimationFinishedGoToSeat(int table) {
		
		MyCust customer = null;
		for(MyCust cust : myCustomers)
		{
			if(cust.t.tableNumber == table)
			{
				customer = cust;
				//cust.st = State.seated;
			}
		}
		Do("Arrived at table " + table + " occupied by " + customer.c.getCustomerName());
		if(customer.st == State.beingSeated)
		{
			customer.st = State.seated;
			waiterState = State.doingNothing;
		}
		else if(customer.st == State.readyToGiveOrder){
			customer.st = State.waitingForWaiterToGetOrder;
			waiterState = State.withOrderingCustomer;
		}
		else if(customer.st == State.goingtoBeToldToReOrder) {
			//waiterState = State.aboutToTellCustomerToReorder;
			waiterState = State.aboutToTellCustomerToReorder;
		}
		else if(waiterState == State.returningFoodToCustomer)
		{
			waiterState = State.servingCustomer;
		}
		else if(customer.st == State.waitingForWaiterToReturn)
		{
			waiterState = State.waitingForCustomersRequest;
		}
		else if(customer.st == State.aboutToRecieveCheck)
		{
			waiterState = State.readyToGiveCheck;
		}
		
		
		//waiterState = State.doingNothing;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgAnimationFinishedGoToCook()
	 */
	@Override
	public void msgAnimationFinishedGoToCook(Customer c) {
		// TODO Auto-generated method stub
		Do("Getting order from the cook");
		MyCust mc = this.findCust(c);
		waiterState = State.gettingOrderFromCook;
		
		mc.st = State.aboutToGetFoodFromWaiter;
		stateChanged();
	}
	
	

	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgAnimationFinishedGoToCashier(restaurant.interfaces.Customer)
	 */
	@Override
	public void msgAnimationFinishedGoToCashier(Customer cust) {
		// TODO Auto-generated method stub
		MyCust myCust = findCust(cust);
		if(myCust == null)
		{
			return;
		}
		if(myCust.st == State.readyToBeServed)
		{
			waiterState = State.givingCheckRequestToCashier;
		}
		else if(myCust.st == State.askedForCheck)
		{
			waiterState = State.arrivedToCashier;
		}
		stateChanged();
	}

	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgGoOnBreakAfterFinishingCustomers()
	 */
	@Override
	public void msgGoOnBreakAfterFinishingCustomers() {
		// TODO Auto-generated method stub
		Do("Allowed to go on break after finished with current customers, should disable button" + waiterState.toString());
		this.wantsToGoOnBreak = true;
		waiterGui.gui.updateInfoPanel(this);
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgDoNotGoOnBreak()
	 */
	@Override
	public void msgDoNotGoOnBreak() {
		// TODO Auto-generated method stub
		waiterGui.gui.updateInfoPanel(this);
		stateChanged();
	}
	
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgSeatCustomer(restaurant.CustomerAgent, restaurant.Table)
	 */
	@Override
	public void msgSeatCustomer(Customer cust, Table t)
	{
		myCustomers.add(new MyCust(cust,t));
		t.setOccupant(cust);
		//System.out.println("Adding " + cust.getCustomerName() + " to list of customers, Sean now has " + myCustomers.size() + " customers");
		stateChanged();
	}
	
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgReadyToOrder(restaurant.interfaces.Customer)
	 */
	@Override
	public void msgReadyToOrder(Customer cust)
	{
		for(MyCust customer : myCustomers)
		{
			if(customer.c == cust)
			{
				//Do("Recieved "  + customer.c.getCustomerName() + " message that he/she is ready to order");
				customer.st = State.ready;
				break;
			}
		}
		
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgHereIsMyChoice(restaurant.interfaces.Customer, restaurant.Choice)
	 */
	@Override
	public void msgHereIsMyChoice(Customer cust,Choice c)
	{
		for(MyCust customer : myCustomers)
		{
			if(customer.c == cust)
			{
				//Do("Recieved " + customer.c.getCustomerName() + "'s choice: " + c.food);
				customer.o = c;
				customer.st = State.ordered;
				break;
			}
			
		}
		waiterState = State.doingNothing;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgOutOfFood(restaurant.Order)
	 */
	@Override
	public void msgOutOfFood(Order o) {
		// TODO Auto-generated method stub
		MyCust cust = findCust(o.table.occupiedBy);
		waiterState = State.goingToTellCustomerToReorder;
		cust.st = State.waitingForWaiterToReturn;
		stateChanged();
	}
	
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgOrderReady(restaurant.Order)
	 */
	@Override
	public void msgOrderReady(Order o)
	{
		Do("Recieved cook's message that order is ready " + o.table.tableNumber);
		//table.occupiedBy.myChoice.setIsCooked(true);
		for(MyCust cust : myCustomers){
			if(cust.t == o.table && cust.o == o.o)
			{
				cust.o.setIsCooked(true);
				cust.st = State.readyToBeServed;
				break;
				//Do("Recieved cook's message that "+cust.c.getCustomerName() + "'s order of " + c.food + "is ready");
				
			}
		}
		stateChanged();
		
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgPleaseComeHere(restaurant.interfaces.Customer)
	 */
	@Override
	public void msgPleaseComeHere(Customer customerAgent) {
		// TODO Auto-generated method stub
		Do("Recieved message that " + customerAgent.getCustomerName() + " wants me to come");
		findCust(customerAgent).st = State.waitingForWaiterToReturn;
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgCheckPlease(restaurant.interfaces.Customer)
	 */
	@Override
	public void msgCheckPlease(Customer customerAgent) {
		// TODO Auto-generated method stub
		Do("Recieved message that " + customerAgent.getCustomerName() + " wants the check");
		findCust(customerAgent).st = State.askedForCheck;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgHereIsCheck(restaurant.Check)
	 */
	@Override
	public void msgHereIsCheck(Check c) {
		// TODO Auto-generated method stub
		Do("Recieved check");
		MyCust cust = findCust(c.getCustomer());
		cust.bill = c;
		waiterState = State.recievedCheck;
		cust.st = State.aboutToRecieveCheck;
		stateChanged();
	}

	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgLeavingTable(restaurant.interfaces.Customer)
	 */
	@Override
	public void msgLeavingTable(Customer customer) {
		// TODO Auto-generated method stub
		for(MyCust cust : myCustomers){
			if(cust.c == customer)
			{
				myCustomers.remove(cust);
				host.msgTableIsFree(cust.t);
				break;
			}
		}
		
		waiterState = State.aboutToGoToHomePosition;
				
		stateChanged();
	}
	@Override
	public void msgAnimationFinishedGoToHomePosition() 
	{
		waiterState = State.doingNothing;
		stateChanged();
	}


	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() throws ConcurrentModificationException{
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
//		for (Table table : tables) {
//			if (!table.isOccupied()) {
//				if (!waitingCustomers.isEmpty()) {
//					seatCustomer(waitingCustomers.get(0), table);//the action
//					return true;//return true to the abstract agent to reinvoke the scheduler.
//				}
//			}
//		}
		if (enter == true) {
			enterRestaurant();
			return true;
		}
		
		if (closingState == ClosingState.ToBeClosed) {
			prepareToClose();
			return true;
		}
		
		if (closingState == ClosingState.Preparing && !((HostAgent)host).anyCustomer() && leave == true) {
			shutDown();
			leaveRestaurant();
			return true;
		}
		
		if (leave == true && closingState == ClosingState.None) {
			leaveRestaurant();
			return true;
		}
		try{
			
		
		if(this.wantsToGoOnBreak && myCustomers.size() == 0)
		{
			GoOnBreak();
			return true;
		}
		
		
		for (MyCust cust : myCustomers){
			if(cust.st == State.readyToBeSeated && waiterState == State.seatingCustomer) {
				cust.st = State.beingSeated;
				SeatCustomer(cust);
				return true;
			}
		}
		
		for (MyCust cust : myCustomers){
			if(cust.st == State.ready && waiterState == State.doingNothing) {
				cust.st = State.readyToGiveOrder;
				GoToCustomersTable(cust);
				return true;
			}
		}
		
		for (MyCust cust: myCustomers){
			if(cust.st == State.waitingForWaiterToGetOrder && waiterState == State.withOrderingCustomer) {
				cust.st = State.givingOrder;
				GetCustomersOrder(cust);
				return true;
			}
		}
		
		for (MyCust cust : myCustomers){
			if(cust.st == State.ordered && waiterState == State.doingNothing) {
				cust.st = State.waitingAfterOrder;
				HandleOrder(cust.o,cust.t);
				return true;
			}
		}
		
		for(MyCust cust: myCustomers){
			if(cust.st == State.waitingForWaiterToReturn && waiterState == State.goingToTellCustomerToReorder) {
				cust.st = State.goingtoBeToldToReOrder;
				GoToCustomersTable(cust);
				return true;
			}
		}
		
		
		
		
		for(MyCust cust: myCustomers){
			if(cust.st == State.goingtoBeToldToReOrder && waiterState == State.aboutToTellCustomerToReorder) {
				cust.st = State.beingToldToReOrder;
				TellCustomerToReOrder(cust);
				return true;
			}
		}
		
		for(MyCust cust: myCustomers){
			if(cust.st == State.readyToBeServed && waiterState == State.doingNothing)  {
				waiterState = State.aboutToGiveCashierWaiterRequest;
				//ServeFood(cust.c,cust.o);
				//GetFoodFromCook(cust);
				GoToCashier(cust);
				return true;
			}
		}
		
		for(MyCust cust: myCustomers){
			if(cust.st == State.readyToBeServed && waiterState == State.givingCheckRequestToCashier)  {
				cust.st = State.readyToBeServedAndCheckIsReady;
				//ServeFood(cust.c,cust.o);
				//GetFoodFromCook(cust);
				GiveWaiterCheckRequest(cust);
				return true;
			}
		}
		
		for(MyCust cust: myCustomers){
			if(cust.st == State.readyToBeServedAndCheckIsReady && waiterState == State.doingNothing)  {
				cust.st = State.waitingForWaiterToGetFood;
				//ServeFood(cust.c,cust.o);
				GetFoodFromCook(cust);
				return true;
			}
		}
		
		
		
		for(MyCust cust: myCustomers){
			if(cust.st == State.aboutToGetFoodFromWaiter && waiterState == State.gettingOrderFromCook) {
				//ServeFood(cust.c,cust.o);
				waiterState = State.returningFoodToCustomer;
				GiveCustomerOrder(cust);
				return true;
			}
			
		
		}
		for(MyCust cust: myCustomers){
			if(cust.st == State.aboutToGetFoodFromWaiter && waiterState == State.servingCustomer){
				cust.st = State.served;
				ServeFood(cust.c, cust.o);
				return true;
			}
		}
		for(MyCust cust: myCustomers){
			if(cust.st == State.waitingForWaiterToReturn && waiterState == State.doingNothing) {
				GoToCustomersTable(cust);
				return true;
			}
		}
		for(MyCust cust: myCustomers){
			if(cust.st == State.waitingForWaiterToReturn && waiterState == State.waitingForCustomersRequest) {
				cust.st = State.ready;
				waiterState = State.busy;
				AskWhatCustomerNeeds(cust);
				return true;
			}
			
		}
		for(MyCust cust: myCustomers){
			if(cust.st == State.askedForCheck && waiterState == State.busy) {
				waiterState = State.goingToGetCheck;
				GoToCashier(cust);
				return true;
			}
			
		}
		for(MyCust cust: myCustomers){
			if(cust.st == State.askedForCheck && waiterState == State.arrivedToCashier) {
				waiterState = State.askingCashierForCheck;
				AskCashierForCheck(cust);
				return true;
			}
			
		}
		
		for(MyCust cust: myCustomers){
			if(cust.st == State.aboutToRecieveCheck && waiterState == State.recievedCheck) {
				waiterState = State.goingToGiveCheckToCustomer;
				GoToCustomersTable(cust);
				return true;
			}
			
		}
		
		for(MyCust cust: myCustomers){
			if(cust.st == State.aboutToRecieveCheck && waiterState == State.readyToGiveCheck) {
				cust.st = State.recievedCheck;
				GiveCheckToCustomer(cust);
				return true;
			}
			
		}
		
		for (MyCust cust : myCustomers){
			if(cust.st == State.doingNothing && waiterState == State.doingNothing)
			{
				//System.out.println("Customer " + cust.c.getName() + " is set to waiting");
				cust.st = State.waiting;
				//SeatCustomer(cust);
				GoToCustomer(cust);
				return true;
			}
			
		}
		
		if(waiterState == State.aboutToGoToHomePosition)
		{
			waiterState = State.goingToHomePosition;
			DoGoToHomePosition();
			return true;
		}
		}catch (ConcurrentModificationException e){
			return false;
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}
	
	private void enterRestaurant() {
		if (closingState == ClosingState.Closed) {
			closingState = ClosingState.None;
		}
		waiterGui.DoEnterRestaurant();
		
		enter = false;
	}
	
	private void leaveRestaurant() {
		if (closingState == ClosingState.None)
			((CashierAgent) cashier).recordShift((PeopleAgent)myPerson, "Waiter");
		waiterGui.DoLeaveRestaurant();
		
		isActive = false;
		leave = false;
		myPerson.msgDone("Waiter");
	}
	
	private void prepareToClose() {
		((CashierAgent) cashier).recordShift((PeopleAgent)myPerson, "Waiter");
		closingState = ClosingState.Preparing;
	}
	
	private void shutDown() {
		closingState = ClosingState.Closed;
	}
	
	private void DoGoToHomePosition() {
		// TODO Auto-generated method stub
		waiterGui.DoGoToHomePosition();
	}

	private void GiveCheckToCustomer(MyCust cust)
	{
		cust.c.msgHereIsYourCheck(cust.bill);
		waiterState = State.doingNothing;
	}

	private void AskCashierForCheck(MyCust cust) {
		// TODO Auto-generated method stub
		Do("Asking cashier for check");
		((CashierAgent) cashier).msgCheckPlease(cust.c);
	}

	

	private void AskWhatCustomerNeeds(MyCust cust) {
		// TODO Auto-generated method stub
		Do("Asking Customer " + cust.c.getCustomerName() + " what they want");
		cust.c.msgWhatDoYouNeed();
	}

	private void GiveWaiterCheckRequest(MyCust cust) {
		// TODO Auto-generated method stub
		Do("Giving check request to cashier for " + cust.c.getCustomerName() + "'s " + cust.o.food.foodname);
		((CashierAgent) cashier).msgPleaseMakeCheck(this,cust.c,cust.o);
		waiterState = State.doingNothing;
		stateChanged();
	}

	private void GoToCashier(MyCust cust) {
		// TODO Auto-generated method stub
		if(cust.st == State.askedForCheck)
		{
			Do("Going to cashier to get check for " + cust.c.getCustomerName());
		}
		else
			Do("Going to cashier to give " + cust.c.getCustomerName() + "'s check request");
		
		waiterGui.DoGoToCashier(cust.c);
	}

	private void GoOnBreak() {
		// TODO Auto-generated method stub
		Do("GOING ON BREAK");
		isOnBreak = true;
		wantsToGoOnBreak = false;
		waiterGui.gui.updateInfoPanel(this);
		waiterGui.DoGoToBreakArea();
	}

	private void TellCustomerToReOrder(MyCust cust) {
		// TODO Auto-generated method stub
		Do("Telling Customer to reorder because out of food");
		cust.c.msgOutOfFoodPleaseReOrder(new Menu(inventory));
		waiterState = State.doingNothing;
	}

	private void GiveCustomerOrder(MyCust cust) {
		// TODO Auto-generated method stub
		Do("Going back to serve customer " + cust.c.getCustomerName());
		((CookAgent) cook).msgPickedUpFood(cust.o);
		waiterGui.DoGoToSeatWithOrder(cust.t.tableNumber,cust.o);
		
		
	}

	

	private void GetFoodFromCook(MyCust cust) {
		// TODO Auto-generated method stub
		waiterState = State.busy;
		waiterGui.DoGoToCook(cust.c);
		
	}

	private void GetCustomersOrder(MyCust cust) {
		// TODO Auto-generated method stub
		waiterState = State.busy;
		cust.c.msgWhatWouldYouLikeToOrder();
		
	}

	// Actions



	// The animation DoXYZ() routines


	//utilities

	private void GoToCustomer(MyCust cust) {
		// TODO Auto-generated method stub
		waiterState = State.busy;
		//Do("Going to customer: " + cust.c.getCustomerName());
		
		waiterGui.DoGoToCustomer(cust.c);
		
	}

	private void ServeFood(Customer cust, Choice o) {
		// TODO Auto-generated method stub
		//Take food to customer
		Do("Serving food("+o.food.foodname+") to customer " + cust.getCustomerName());
		
		cust.msgHereIsYourFood(o);
		
		waiterState = State.doingNothing;
		stateChanged();
	}

	protected void HandleOrder(Choice o,Table table) {
//		// TODO Auto-generated method stub
//		Do("Giving order to the cook"+table.tableNumber);
//		
//		((CookAgent) cook).msgHereIsOrder(new Order(this,o,table));
}

	private void GoToCustomersTable(MyCust cust) {
		// TODO Auto-generated method stub
		Do("Going to " + cust.c.getCustomerName() + "'s table");
		//tell gui to walk to customer
		waiterState = State.busy;
		//System.out.println("Going to customer to retrieve order");
		if(cust.st == State.aboutToRecieveCheck)
			waiterGui.DoGoToSeatWithCheck(cust.t.tableNumber);
		else
			waiterGui.DoGoToSeat(cust.t.tableNumber);
		//cust.c.msgWhatWouldYouLikeToOrder();
		
		
		
	}

	private void SeatCustomer(MyCust cust) {
		Do("Seating customer: " + cust.c.getCustomerName());
		waiterState = State.busy;
		waiterGui.DoGoToSeat(cust.t.tableNumber);
		cust.c.msgFollowMe(new Menu(inventory), cust.t.tableNumber);
		
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#WantsToGoOnBreak()
	 */
	@Override
	public void WantsToGoOnBreak(){
		Do("Asking host to go on break");
		host.msgWantsToGoOnBreak(this);
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#goOffBreak()
	 */
	@Override
	public void goOffBreak() {
		// TODO Auto-generated method stub
		Do("Going OFF BREAK");
		this.setBreak(false);
		waiterGui.DoGoToHomePosition();
		
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#isWantingToGoOnBreak()
	 */
	@Override
	public boolean isWantingToGoOnBreak() {
		return this.wantsToGoOnBreak;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#isOnBreak()
	 */
	@Override
	public boolean isOnBreak() {
		return isOnBreak;
	}

	/* (non-Javadoc)
	 * @see restaurant.Waiter#setGui(restaurant.gui.WaiterGui)
	 */
	@Override
	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	/* (non-Javadoc)
	 * @see restaurant.Waiter#getGui()
	 */
	@Override
	public WaiterGui getGui() {
		return waiterGui;
	}

	/* (non-Javadoc)
	 * @see restaurant.Waiter#isAvailable()
	 */
	@Override
	public boolean isAvailable() {
		// TODO Auto-generated method stub
		if(waiterState == State.doingNothing)
			return true;
		else
		return false;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#findCust(restaurant.interfaces.Customer)
	 */
	@Override
	public MyCust findCust(Customer cust)
	{
		MyCust found = null;
		for(MyCust c : myCustomers)
		{
		if(c.c == cust)
			found = c;
		}
		
		return found;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#setCashier(restaurant.interfaces.Cashier)
	 */
	@Override
	public void setCashier(Cashier c)
	{
		cashier = c;
	}

	/* (non-Javadoc)
	 * @see restaurant.Waiter#numberOfCustomersBeingServed()
	 */
	@Override
	public int numberOfCustomersBeingServed()
	{
		return myCustomers.size();
	}

	/* (non-Javadoc)
	 * @see restaurant.Waiter#pauseCustomers()
	 */
	@Override
	public void pauseCustomers() {
		// TODO Auto-generated method stub
		for(MyCust c : myCustomers)
		{
			c.c.pauseAgent();
		}
		
	}

	/* (non-Javadoc)
	 * @see restaurant.Waiter#restartCustomers()
	 */
	@Override
	public void restartCustomers() {
		// TODO Auto-generated method stub
		for(MyCust c : myCustomers)
		{
			c.c.restartAgent();
		}
		
	}

	/* (non-Javadoc)
	 * @see restaurant.Waiter#setBreak(boolean)
	 */
	@Override
	public void setBreak(boolean b) {
		// TODO Auto-generated method stub
		isOnBreak = b;
	}
	
	public void setRevolvingStand(RevolvingStand stand)
	{
		this.revolvingStand = stand;
	}

	
	
//	public String getInfo(CustomerAgent c) {
//		MyCust temp = findCust(c);
//		String temp1 = temp.st.toString();
//		return temp1;
//		
//	}

	
	
	
	

	

	
	
	

	
}
