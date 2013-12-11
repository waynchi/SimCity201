package restaurant_wc;

import restaurant_wc.BaseWaiterRole.FoodOnMenu;
import restaurant_wc.gui.CustomerGui;
import restaurant_wc.gui.RestaurantGuiWc;
import restaurant.interfaces.Cashier;
import restaurant_wc.interfaces.Customer;
import restaurant_wc.interfaces.Waiter;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import people.Role;

/**
 * Restaurant customer agent.
 */
// Customers are created by user, and could be set hungry when created.
// Customers behave differently upon situations, which depends on the name they have

public class RestaurantCustomerRoleWc extends Role implements Customer{
	private int hungerLevel = 5;        // determines length of meal
	Timer timer = new Timer();
	private CustomerGui customerGui;
	private RestaurantGuiWc restGui;

	// agent correspondents
	private HostRoleWc host;
	private Waiter waiter;
	private Cashier cashier;
	private int tableNum;
	private List<FoodOnMenu> menu;
	private String choice;
	private Random randomGenerator = new Random();
	private Semaphore atCashier = new Semaphore(0,true);
	private Semaphore atExit = new Semaphore(0,true);
	//private Double due;
	
	//customer behaviors
	private Boolean leaveIfRestIsFull = false;
	private Boolean orderFoodThatICanAfford = false;
	private Boolean leaveIfCheapestFoodOutOfStock = false;
	private Boolean reorderAcceptable = false;
			
	// State of a customer
	public enum AgentState
	{DoingNothing, GoingToRestaurant, WaitingInRestaurant, BeingSeated, Ordering, Ordered, 
		WaitingForFood, Eating, DoneEating, EatingAndCheckArrived, Paying, Leaving};
	private AgentState state = AgentState.DoingNothing;//The start state

	// Events for a customer
	public enum AgentEvent
	{NONE, gotHungry, restaurantFull, followWaiter, seated, ordering, ordered, reordered,  
		eating, doneEating, gotCheck, donePayng, doneLeaving};
	private AgentEvent event = AgentEvent.NONE; // The start event

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 */
	public RestaurantCustomerRoleWc(RestaurantGuiWc gui){
		super();
		this.restGui = gui;
		customerGui = new CustomerGui(this);
		restGui.getAnimationPanel().addGui(customerGui);
		customerGui.setPresent(false);
		
	}

	/**
	 * hack to establish connection to Host agent and Waiter agent.
	 */
	public void setHost(HostRoleWc host) {
		this.host = host;
	}
	
	public void setWaiter(Waiter waiter) {
		this.waiter = waiter;
	}

	
	// Messages

	// from animation. Eventually messages the Host about wanting food...

	public void msgIsActive() {
		customerGui.setPresent(true);
		host = (HostRoleWc) myPerson.getHost(3);
		print("I'm hungry");
		state = AgentState.DoingNothing;
		event = AgentEvent.gotHungry;
		isActive = true;
		stateChanged();

	}
	
	public void msgAtExit() {
		event = AgentEvent.doneLeaving;
		atExit.release();
		stateChanged();
	}

	public void msgYouNeedToWait() { // from host, notifying customer that restaurant is full
		event = AgentEvent.restaurantFull;
		stateChanged();
	}
	
	// handles waiter follow me message and eventually sits down at the correct table
	public void msgSitAtTable(Waiter waiter, int tableNumber, List<FoodOnMenu> m) {
		print("got message from waiter " + waiter.getName());
		this.waiter = waiter;
		tableNum = tableNumber;
		menu = m;
		event = AgentEvent.followWaiter;
		stateChanged();
	}

	// from animation, when customer has arrived at the table
	public void msgAtTable() {
//		print("atTable released");
		event = AgentEvent.seated;
		stateChanged();
	}
	
	public void msgAtCashier() {
		atCashier.release();
//		print("atCashier released");
		stateChanged();
	}
	
	//from animation, when customer has made the choice on pop up list
	public void msgAnimationChoiceMade() {
		print("made decision");
		event = AgentEvent.ordering;
		stateChanged();
	}
	
	//from waiter agent
	public void msgWhatWouldYouLike() {
		print("asked to order");
		event = AgentEvent.ordered;
		stateChanged();
	}
	
	//from waiter agent
	public void msgWhatElseWouldYouLike(List<FoodOnMenu> newMenu) {
		print("asked to reorder");
		event = AgentEvent.reordered;
		menu = newMenu;
		stateChanged();
	}
	
	//from waiter agent
	public void msgHereIsYourFood() {
		print("got my food");
		event = AgentEvent.eating;
		stateChanged();
	}
	
	public void msgHereIsYourTotal (Double d, Cashier c) {
		print("got my check");
		event = AgentEvent.gotCheck;
		cashier = c;
		stateChanged();
	}
	
	public void msgHereIsYourChange (Double change) {
		print("got my change");
		getPersonAgent().setMoney(change);
		event = AgentEvent.donePayng;
		stateChanged();
	}

	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine

		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.WaitingInRestaurant;
			//state = AgentState.GoingToRestaurant;
			goToRestaurant();
			return true;
		}
		
	/*	if (state == AgentState.GoingToRestaurant && event == AgentEvent.restaurantFull){
			//Customer comes to restaurant and restaurant is full, customer is told and leaves.
			if (leaveIfRestIsFull){
				state = AgentState.Leaving;
				leaveRestaurantBecauseItsFull();
				return true;
			}
			//Customer comes to restaurant and restaurant is full, customer is told and waits
			else {
				state = AgentState.WaitingInRestaurant;
				stayInRestaurant();
				return true;
			}
		}*/
			
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followWaiter ){
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		
		if (state == AgentState.GoingToRestaurant && event == AgentEvent.followWaiter ){
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.Ordering;
			TellWaiter();
			return true;
		}
		

		if (state == AgentState.Ordering && event == AgentEvent.ordering) {
			state = AgentState.Ordered;
			CallWaiter();
			return true;
		}
		
		if (state == AgentState.Ordered && event == AgentEvent.ordered) {
			state = AgentState.WaitingForFood;
			OrderFood();
			return true;
		}
		
		if (state == AgentState.WaitingForFood && event == AgentEvent.eating) {
			state = AgentState.Eating;
			EatFood();
			return true;
		}
		
		if (state == AgentState.WaitingForFood && event == AgentEvent.reordered) {
			state = AgentState.Ordering;
			if (reorderAcceptable){
				TellWaiter();
				return true;
			}
			else {
				NoMoreChoices();
				return true;
			}
		}
		
		if (state == AgentState.Eating && event == AgentEvent.doneEating) {
			state = AgentState.DoneEating;
			print("Done eating, waiting for check");
			//waiting for check to pay
 			return true;
		}
		
		if (state == AgentState.Eating && event == AgentEvent.gotCheck) {
			state = AgentState.EatingAndCheckArrived;
			print("Got check, will pay after done eating");
			//waiting to finish up food and pay
			return true;
		}
		
		if (state == AgentState.DoneEating && event == AgentEvent.gotCheck) {
			state = AgentState.Paying;
			PayCheck();
			return true;
		}
		
		if (state == AgentState.EatingAndCheckArrived && event == AgentEvent.doneEating) {
			state = AgentState.Paying;
			PayCheck();
			return true;
		}
		
		if (state == AgentState.Paying && event == AgentEvent.donePayng){
			state = AgentState.Leaving;
			LeaveRestaurant();
			return true;
		}
		
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			//no action
			return true;
		}
		return false;
	}

	// Actions

	private void goToRestaurant() {
		Do("Going to restaurant");
		host.IWantToEat(this);//send our instance, so he can respond to us
	}

//	private void leaveRestaurantBecauseItsFull(){
//		print ("Rather leave than wait");
//		host.leaveRestaurant(this);
//		customerGui.DoExitRestaurant();
//
//	}
	
	private void NoMoreChoices() {
		//Animation support for Customer leaving after restaurant runs out of your choice
		print ("Don't want to reorder, leaving");
		waiter.msgDoneEatingAndLeaving(this);
		customerGui.DoExitRestaurant();
		state = AgentState.Leaving;
	}
	
//	private void stayInRestaurant() {
//		print("It's fine I'll wait");
//		host.waitInRestaurant(this);
//	}
	
	private void SitDown() {
		Do("Being seated. Going to table");
		customerGui.DoGoToSeat(tableNum);
	}

	private void TellWaiter() {	
		print("making decision");
		Double minimumPrice = 100.00;
		for (FoodOnMenu temp : menu) {
			if (temp.price < minimumPrice) minimumPrice = temp.price;
		}
		//Case 1 - Can't afford anything on the menu. Customer leaves
		if (event == AgentEvent.seated){
			if (getPersonAgent().getMoney() < minimumPrice && orderFoodThatICanAfford){
				print ("Can't afford anything, leaving");
				waiter.msgDoneEatingAndLeaving(this);
				state = AgentState.Leaving;
				customerGui.DoExitRestaurant();
				return;
				
			}
		}
		
		//Case 2 - Restaurant runs out of Customer order, can't afford anything else
		if (event == AgentEvent.reordered){

			if (getPersonAgent().getMoney() < minimumPrice && (orderFoodThatICanAfford||leaveIfCheapestFoodOutOfStock)){
				print ("Can't afford anything else, leaving");
				waiter.msgDoneEatingAndLeaving(this);
				state = AgentState.Leaving;
				customerGui.DoExitRestaurant();
				return;
			}
		}
		
		timer.schedule(new TimerTask() {
			public void run() {
				event = AgentEvent.ordering;
				stateChanged();
			}
		},
		3000);
	}
	
	private void CallWaiter(){
		Do("I'm ready to order");
		waiter.msgIAmReadyToOrder(this);
	}
	
	private void OrderFood() {
		// can only order one item at a time. Prices are on the menu
		print("ordering");

		Double maximumPrice = 0.00;
		
		// Customer has only enough money to order the cheapest item if moneyOnMe is greater than 5.99 and less
		// than 8.99.
		if (orderFoodThatICanAfford) {
			for (FoodOnMenu temp : menu) {
				if (temp.price > maximumPrice && temp.price <= getPersonAgent().getMoney()) {
					maximumPrice = temp.price;
					choice = temp.type;
					//print ("in makeOrder, should be here, choice is " + temp.type + "price is " + temp.price);

				}
			}
			Do("Can I have " + choice+ " ?");
			waiter.msgHereIsMyOrder (this, choice);
			customerGui.madeDecision(choice);
			return;
		}
		
		
		
		for (FoodOnMenu f : menu){
			if (f.type.equals(choice)){
				Do("Can I have " + choice+ " ?");
				waiter.msgHereIsMyOrder (this, choice);
				customerGui.madeDecision(choice);
				return;
			}
		}
		
		int rand = randomGenerator.nextInt(menu.size());
		choice = menu.get(rand).type;
		Do("Can I have " + choice + " ?");
		waiter.msgHereIsMyOrder (this, choice);
		customerGui.madeDecision(choice);
		
	}
	
	private void EatFood() {
		customerGui.eatFood();
		Do("Eating Food");
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
				print("Done eating, the " + choice + " is so good: )");
				event = AgentEvent.doneEating;
				stateChanged();
			}
		},
		5000);//getHungerLevel() * 1000);//how long to wait before running task
	}

	// customer must pay for his meal
	private void PayCheck() {
		print("going to pay check");

		customerGui.DoGoToCashier();
		try {
			atCashier.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		((CashierRoleWc) cashier).msgPayMyCheck(this, getPersonAgent().getMoney());
		getPersonAgent().setMoney(0.0);
		print ("Paying my bill");
	}
	
	private void LeaveRestaurant() {
		print("leaving table");

		waiter.msgDoneEatingAndLeaving(this);
		customerGui.DoExitRestaurant();
		try {
			atExit.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// gui needs to walk to the exit
		isActive = false;
		customerGui.setPresent(false);
		getPersonAgent().msgDone("RestaurantCustomerRoleWc");
	}

	
	// Accessors, etc.
	
	public String getName() {
		return getPersonAgent().getName();
	}

	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
		//could be a state change. Maybe you don't
		//need to eat until hunger lever is > 5?
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	public CustomerGui getGui() {
		return customerGui;
	}
	
	public void setChoice(String c) {
		choice = c;
	}
	
	public String getChoice() {
		return choice;
	}
	
	public int getTableNumber() {
		return tableNum;
	}

	
	public Boolean isActive() {
		return isActive;
	}

	@Override
	public void gotHungry() {
		// TODO Auto-generated method stub
		
	}
	
	public String getState() {
		return state.toString();
	}

}

