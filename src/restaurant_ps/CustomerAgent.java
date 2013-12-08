package restaurant_ps;

import restaurant_ps.gui.CustomerGui;
import restaurant.interfaces.Cashier;
import restaurant_ps.interfaces.Customer;
import restaurant_ps.interfaces.Waiter;
import agent.Agent;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import people.Role;

/**
 * Restaurant customer agent.
 */
public class CustomerAgent extends Role implements Customer {
	Random generator = new Random();
	private int tableNumber;
	private String name;
	private int hungerLevel = 5;        // determines length of meal
	private int moneyDollars;
	Timer timer = new Timer();
	private CustomerGui customerGui;

	// agent correspondents
	private HostAgent host = null;
	private Waiter waiter = null;
	private Cashier cashier = null;
	
	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, WaitingForAvailableWaiter, BeingSeated, Seated, PickingFood, ordering, Eating, DoneEating, Leaving, ReadyForCheck, AskingForCheck, payingCheck, goingToCashier, decidingToStayOrLeave};
	
	public AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, askHostToBeSeated, followHost, seated, waiterAsksForOrder, recievedFood, doneEating, doneLeaving, waiterAsksToReOrder, waiterArrived, recievedCheck, arrivedAtCashier, cashierApproved, tablesAreFull, arrivedAtWaiting};
	AgentEvent event = AgentEvent.none;
	
	Menu myMenu;
	Choice myChoice;
	Check myCheck;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public CustomerAgent(String name, int money){
		super();
		this.name = name;
		moneyDollars = money;
		myMenu = null;
		myCheck = null;
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#setHost(restaurant.HostAgent)
	 */
	@Override
	public void setHost(HostAgent host) {
		this.host = host;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#setWaiter(restaurant.WaiterAgent)
	 */
	@Override
	public void setWaiter(Waiter waiter) {
		this.waiter = waiter;
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#getCustomerName()
	 */
	@Override
	public String getCustomerName() {
		return name;
	}
	
	
	// Messages

	/* (non-Javadoc)
	 * @see restaurant.Customer#gotHungry()
	 */
	@Override
	public void gotHungry() {//from animation
		//print("I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgAllTablesAreOccupied()
	 */
	@Override
	public void msgAllTablesAreOccupied() {
		// TODO Auto-generated method stub
		Do("Found out all tables are occupied");
		event = AgentEvent.tablesAreFull;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgAnimationFinishedGoToHost()
	 */
	@Override
	public void msgAnimationFinishedGoToHost() {
		// TODO Auto-generated method stub
		//print("Asking host for food");
		event = AgentEvent.askHostToBeSeated;
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#msgFollowMe(restaurant.Menu, int)
	 */
	@Override
	public void msgFollowMe(Menu m,int table) {
		//print("Received msgSitAtTable" + table);
		tableNumber = table;
		myMenu = m;
		event = AgentEvent.followHost;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgWhatWouldYouLikeToOrder()
	 */
	@Override
	public void msgWhatWouldYouLikeToOrder(){
		customerGui.gui.updateInfoPanel(this);
		//print("Waiter came to give order");
		event = AgentEvent.waiterAsksForOrder;
		stateChanged();
	}
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgOutOfFoodPleaseReOrder(restaurant.Menu)
	 */
	@Override
	public void msgOutOfFoodPleaseReOrder(Menu menu) {
		// TODO Auto-generated method stub
		myMenu = menu;
		event = AgentEvent.waiterAsksToReOrder;
		stateChanged();
		
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgHereIsYourFood(restaurant.Choice)
	 */
	@Override
	public void msgHereIsYourFood(Choice o) {
		// TODO Auto-generated method stub
		//print("Received msgHereIsYourFood");
		event = AgentEvent.recievedFood;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgWhatDoYouNeed()
	 */
	@Override
	public void msgWhatDoYouNeed() {
		// TODO Auto-generated method stub
		event = AgentEvent.waiterArrived;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgHereIsYourCheck(restaurant.Check)
	 */
	@Override
	public void msgHereIsYourCheck(Check bill) {
		// TODO Auto-generated method stub
		myCheck = bill;
		event = AgentEvent.recievedCheck;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgPayNextTime()
	 */
	@Override
	public void msgPayNextTime() {
		// TODO Auto-generated method stub
		event = AgentEvent.cashierApproved;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgAcceptedPayment()
	 */
	@Override
	public void msgAcceptedPayment() {
		// TODO Auto-generated method stub
		event = AgentEvent.cashierApproved;
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#msgAnimationFinishedGoToSeat()
	 */
	@Override
	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgAnimationFinishedGoToCashier()
	 */
	@Override
	public void msgAnimationFinishedGoToCashier() {
		// TODO Auto-generated method stub
		event = AgentEvent.arrivedAtCashier;
		stateChanged();
	}
	@Override
	public void msgAnimationFinishedGoToWaiting() {
		//event = AgentEvent.arrivedAtWaiting;
		host.msgArrivedToWaitingArea(this);
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#msgAnimationFinishedLeaveRestaurant()
	 */
	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine

		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		
		if(state == AgentState.WaitingInRestaurant && event == AgentEvent.askHostToBeSeated) {
			state = AgentState.WaitingForAvailableWaiter;
			waitForWaiter();
			return true;
		}
		
		if (state == AgentState.WaitingForAvailableWaiter && event == AgentEvent.tablesAreFull){
			state = AgentState.decidingToStayOrLeave;
			DecideToStayOrLeave();
			return true;
		}
		
		if (state == AgentState.WaitingForAvailableWaiter && event == AgentEvent.followHost ){
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.PickingFood;
			pickFood();
			//state = AgentState.Eating;
			//EatFood();
			return true;
		}
		if(state == AgentState.PickingFood && event == AgentEvent.waiterAsksForOrder){
			state = AgentState.ordering;
			orderFood();
			return true;
		}
		
		if(state == AgentState.ordering && event == AgentEvent.waiterAsksToReOrder) {
			state = AgentState.PickingFood;
			pickFood();
			return true;
		}
		
		if(state == AgentState.ordering && event == AgentEvent.recievedFood){
			state = AgentState.Eating;
			EatFood();
			return true;
		}
	

		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			//state = AgentState.Leaving;
			state = AgentState.ReadyForCheck;
			//leaveTable();
			CallWaiter();
			return true;
		}
		if(state == AgentState.ReadyForCheck && event == AgentEvent.waiterArrived){
			state = AgentState.AskingForCheck;
			AskWaiterForCheck();
			return true;
		}
		
		if(state == AgentState.AskingForCheck && event == AgentEvent.recievedCheck){
			state = AgentState.goingToCashier;
			GoToCashier();
			return true;
		}
		
		if(state == AgentState.goingToCashier && event == AgentEvent.arrivedAtCashier){
			state = AgentState.payingCheck;
			PayCheck();
			return true;
		}
		
		if(state == AgentState.payingCheck && event == AgentEvent.cashierApproved){
			state = AgentState.Leaving;
			leaveRestaurant();
			return true;
		}
	
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			//no action
			return true;
		}
		return false;
	}
	
	

	private void DecideToStayOrLeave() {
		// TODO Auto-generated method stub
		int k = generator.nextInt(2);
		
		if((k == 0 || name.contains("leave"))){ //50& chance to leave
			Do("Decides to leave because tables are full");
			state = AgentState.Leaving;
			leaveRestaurant();
			return;
		}
		Do("Decides to stay and wait for tables to open");
		state = AgentState.WaitingForAvailableWaiter;
		event = AgentEvent.askHostToBeSeated;
		customerGui.DoGoToWaiting();
	}

	private void PayCheck() {
		double payment = myCheck.getMoneyOwed();
		if((moneyDollars - payment) >= 0)
		{
			Do("Paying check, deducting " + payment + " from " + moneyDollars);
			moneyDollars -= payment;
			((CashierAgent) cashier).msgHereIsMyPayment(payment,this);
		}
		else
		{
			//handle paying next time
			((CashierAgent) cashier).msgCanIPayNextTime(this);
		}
	}

	private void GoToCashier() {
		// TODO Auto-generated method stub
		Do("Going to cashier");
		customerGui.DoGoToCashier();
	}

	private void AskWaiterForCheck() {
		// TODO Auto-generated method stub
		Do("Asking waiter for check");
		waiter.msgCheckPlease(this);
	}

	private void CallWaiter() {
		// TODO Auto-generated method stub
		Do("Calling waiter over");
		waiter.msgPleaseComeHere(this);
		
	}

	private void waitForWaiter() {
		// TODO Auto-generated method stub
		Do("Waiting For Waiter");
		
		host.msgIWantFood(this);
		customerGui.DoGoToWaiting();
		
		
		if(name.equals("leavenow"))
		{
			//System.out.println("LEAVING");
			state = AgentState.Leaving;
			this.leaveRestaurant();
		}
		
	}

	// Actions

	
	
	private void orderFood() {
		// TODO Auto-generated method stub
		Do("Gave waiter order " + myChoice.food.foodname + " at table " + this.tableNumber);
		
		waiter.msgHereIsMyChoice(this,myChoice);
		
	}

	
	
	private void pickFood() {
		Do("Picking food choice");
		if(!canAffordAnythingOnMenu())
		{
			if((generator.nextInt(2) == 0 && !name.contains("flake") || name.contains("cheapleave"))) // 50% chance of leaving restaurant or ordering anyways
			{	
				Do("Can't afford anything with only " + this.moneyDollars + " dollars, preparing to leave");
				state = AgentState.Leaving;
				leaveRestaurant();
				return;
			}
			Do("Can't afford anything, ordering anyways");
		}
		
		for(Choice c : myMenu.menuItems ) 
		{
			if(c.food.foodname.equals(this.name))
			{
				myChoice = c;
				waiter.msgReadyToOrder(this);
				return;
			}
		}
		
		if(canOnlyAffordOneItem())
		{
			//System.out.println("CAN ONLY AFFORD ONE ITEM");
			myChoice = cheapestItem();
			waiter.msgReadyToOrder(this);
			return;
		}
		int foodChoice = generator.nextInt(myMenu.menuItems.size()) + 1;
		//System.out.println("my food choice = " + foodChoice);
		
		myChoice = myMenu.chooseItem(foodChoice);
		waiter.msgReadyToOrder(this);
		
	}

	private Choice cheapestItem() {
		// TODO Auto-generated method stub
		Choice cheapest = myMenu.menuItems.get(0);
		for(Choice c : myMenu.menuItems ) 
		{
			if(c.food.price < cheapest.food.price)
				cheapest = c;
		}
		
		return cheapest;
	}

	private boolean canOnlyAffordOneItem() {
		// TODO Auto-generated method stub
		int numberOfItemsICanAfford = 0;
		for(Choice c : myMenu.menuItems ) 
		{
			if(c.food.price <= moneyDollars)
				numberOfItemsICanAfford++;
		}
		if(numberOfItemsICanAfford > 1)
			return false;
		else
			return true;
	}

	private boolean canAffordAnythingOnMenu() {
		// TODO Auto-generated method stub
		boolean canAffordAnything = false;
		for(Choice c: myMenu.menuItems)
		{
			if(c.food.price <= moneyDollars)
				canAffordAnything = true;
		}
		return canAffordAnything;
	}

	private void goToRestaurant() {
		Do("Going to restaurant");
		this.customerGui.DoGoToHost(host.hostGui.getXPos(),host.hostGui.getYPos());
		//host.msgIWantFood(this);//send our instance, so he can respond to us
	}

	private void SitDown() {
		//Do("Being seated. Going to table");
		customerGui.DoGoToSeat(tableNumber);//hack; only one table
	}

	private void EatFood() {
		//Do("Eating Food");
		customerGui.DoEatFood(myChoice);
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			public void run() {
				//print("Done eating, cookie=" + cookie);
				event = AgentEvent.doneEating;
				customerGui.DoNoFoodOnTable();
				//isHungry = false;
				stateChanged();
			}
		},
		5000);//getHungerLevel() * 1000);//how long to wait before running task
	}

	private void leaveRestaurant() {
		Do("Leaving.");
		if(waiter != null)
			waiter.msgLeavingTable(this);
		else
			host.msgImLeaving(this);
		customerGui.DoExitRestaurant();
		myMenu = null;
		myChoice = null;
		
	}

	// Accessors, etc.

	/* (non-Javadoc)
	 * @see restaurant.Customer#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#getHungerLevel()
	 */
	@Override
	public int getHungerLevel() {
		return hungerLevel;
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#setHungerLevel(int)
	 */
	@Override
	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
		//could be a state change. Maybe you don't
		//need to eat until hunger lever is > 5?
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#toString()
	 */
	@Override
	public String toString() {
		return "customer " + getName();
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#setGui(restaurant.gui.CustomerGui)
	 */
	@Override
	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#getGui()
	 */
	@Override
	public CustomerGui getGui() {
		return customerGui;
	}

	
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#setCashier(restaurant.CashierAgent)
	 */
	@Override
	public void setCashier(Cashier c){
		cashier = c;
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#getWaiter()
	 */
	@Override
	public Waiter getWaiter(){
		return waiter;
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#getMoney()
	 */
	@Override
	public int getMoney() {
		// TODO Auto-generated method stub
		return moneyDollars;
	}

	@Override
	public void restartAgent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pauseAgent() {
		// TODO Auto-generated method stub
		
	}

	

	

	

	

	
	

	

	
}

