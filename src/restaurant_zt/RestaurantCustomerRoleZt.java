package restaurant_zt;

import restaurant_zt.BaseWaiterRole.FoodOnMenu;
import restaurant_zt.gui.CustomerGuiZt;
import restaurant_zt.gui.RestaurantGuiZt;
import restaurant.interfaces.Cashier;
import restaurant_zt.interfaces.Customer;
import restaurant_zt.interfaces.Waiter;

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

public class RestaurantCustomerRoleZt extends Role implements Customer{
	private int hungerLevel = 5;        // determines length of meal
	Timer timer = new Timer();
	private CustomerGuiZt customerGui;
	private RestaurantGuiZt restGui;

	// agent correspondents
	private HostRoleZt host;
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
	public enum CustomerState
	{DOING_NOTHING, COMING_TO_RESTAURANT, WAITING_IN_RESTAURANT, BEING_SEATED, MAKING_DECISION, READY_TO_ORDER, 
		WAITING_FOR_FOOD, EATING, DONE_EAETING_AND_WAITING_FOR_CHECK, EATING_AND_CHECK_AVAILABLE, PAYING, LEAVING};
	private CustomerState state = CustomerState.DOING_NOTHING;//The start state

	// Events for a customer
	public enum CustomerEvent
	{NONE, GOT_HUNGRY, REST_IS_FULL, FOLLOW_WAITER, SEATED, MADE_DECISION, ASKED_TO_ORDER, ASKED_TO_REORDER,  
		FOOD_SERVED, DONE_EATING, GOT_CHECK, DONE_PAYING, DONE_LEAVING};
	private CustomerEvent event = CustomerEvent.NONE; // The start event

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 */
	public RestaurantCustomerRoleZt(RestaurantGuiZt gui){
		super();
		this.restGui = gui;
		customerGui = new CustomerGuiZt(this);
		restGui.getAnimationPanel().addGui(customerGui);
		customerGui.setPresent(false);
		
	}

	/**
	 * hack to establish connection to Host agent and Waiter agent.
	 */
	public void setHost(HostRoleZt host) {
		this.host = host;
	}
	
	public void setWaiter(Waiter waiter) {
		this.waiter = waiter;
	}

	
	// Messages

	// from animation. Eventually messages the Host about wanting food...

	public void msgIsActive() {
		customerGui.setPresent(true);
		host = (HostRoleZt) myPerson.getHost(2);
		print("I'm hungry");
		state = CustomerState.DOING_NOTHING;
		event = CustomerEvent.GOT_HUNGRY;
		isActive = true;
		getPersonAgent().CallstateChanged();

	}
	
	public void msgAtExit() {
		event = CustomerEvent.DONE_LEAVING;
		atExit.release();
		getPersonAgent().CallstateChanged();
	}

	public void msgRestaurantIsFull() { // from host, notifying customer that restaurant is full
		event = CustomerEvent.REST_IS_FULL;
		getPersonAgent().CallstateChanged();
	}
	
	// handles waiter follow me message and eventually sits down at the correct table
	public void msgFollowMeToTable(Waiter waiter, int tableNumber, List<FoodOnMenu> m) {
		print("got message from waiter " + waiter.getName());
		this.setWaiter(waiter);
		tableNum = tableNumber;
		menu = m;
		event = CustomerEvent.FOLLOW_WAITER;
		getPersonAgent().CallstateChanged();
	}

	// from animation, when customer has arrived at the table
	public void msgAtTable() {
		print("atTable released");

		event = CustomerEvent.SEATED;
		getPersonAgent().CallstateChanged();
	}
	
	public void msgAtCashier() {
		atCashier.release();
		print("atCashier released");

		getPersonAgent().CallstateChanged();
	}
	
	//from animation, when customer has made the choice on pop up list
	public void msgAnimationChoiceMade() {
		print("made decision");
		event = CustomerEvent.MADE_DECISION;
		getPersonAgent().CallstateChanged();
	}
	
	//from waiter agent
	public void msgWhatWouldYouLike() {
		print("asked to order");
		event = CustomerEvent.ASKED_TO_ORDER;
		getPersonAgent().CallstateChanged();
	}
	
	//from waiter agent
	public void msgReorder(List<FoodOnMenu> newMenu) {
		print("asked to reorder"
				+ "");
		event = CustomerEvent.ASKED_TO_REORDER;
		menu = newMenu;
		getPersonAgent().CallstateChanged();
	}
	
	//from waiter agent
	public void msgHereIsYourFood() {
		print("got my food");

		event = CustomerEvent.FOOD_SERVED;
		getPersonAgent().CallstateChanged();
	}
	
	public void msgHereIsCheck (Double d, Cashier c) {
		print("got my check");

		event = CustomerEvent.GOT_CHECK;
		cashier = c;
		//due = d;
		getPersonAgent().CallstateChanged();
	}
	
	public void msgHereIsYourChange (Double change) {
		print("got my change");

		getPersonAgent().setMoney(change);
		
		event = CustomerEvent.DONE_PAYING;
		getPersonAgent().CallstateChanged();
	}

	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine

		if (state == CustomerState.DOING_NOTHING && event == CustomerEvent.GOT_HUNGRY ){
			state = CustomerState.WAITING_IN_RESTAURANT;
			//state = CustomerState.COMING_TO_RESTAURANT;
			goToRestaurant();
			return true;
		}
		
	/*	if (state == CustomerState.COMING_TO_RESTAURANT && event == CustomerEvent.REST_IS_FULL){
			//Customer comes to restaurant and restaurant is full, customer is told and leaves.
			if (leaveIfRestIsFull){
				state = CustomerState.LEAVING;
				leaveRestaurantBecauseItsFull();
				return true;
			}
			//Customer comes to restaurant and restaurant is full, customer is told and waits
			else {
				state = CustomerState.WAITING_IN_RESTAURANT;
				stayInRestaurant();
				return true;
			}
		}*/
			
		if (state == CustomerState.WAITING_IN_RESTAURANT && event == CustomerEvent.FOLLOW_WAITER ){
			state = CustomerState.BEING_SEATED;
			SitDown();
			return true;
		}
		
		if (state == CustomerState.COMING_TO_RESTAURANT && event == CustomerEvent.FOLLOW_WAITER ){
			state = CustomerState.BEING_SEATED;
			SitDown();
			return true;
		}
		
		if (state == CustomerState.BEING_SEATED && event == CustomerEvent.SEATED){
			state = CustomerState.MAKING_DECISION;
			makeDecision();
			return true;
		}
		

		if (state == CustomerState.MAKING_DECISION && event == CustomerEvent.MADE_DECISION) {
			state = CustomerState.READY_TO_ORDER;
			signalToWaiter();
			return true;
		}
		
		if (state == CustomerState.READY_TO_ORDER && event == CustomerEvent.ASKED_TO_ORDER) {
			state = CustomerState.WAITING_FOR_FOOD;
			makeOrder();
			return true;
		}
		
		if (state == CustomerState.WAITING_FOR_FOOD && event == CustomerEvent.FOOD_SERVED) {
			state = CustomerState.EATING;
			eatFood();
			return true;
		}
		
		if (state == CustomerState.WAITING_FOR_FOOD && event == CustomerEvent.ASKED_TO_REORDER) {
			state = CustomerState.MAKING_DECISION;
			if (reorderAcceptable){
				makeDecision();
				return true;
			}
			else {
				doNotWantToReorder();
				return true;
			}
		}
		
		if (state == CustomerState.EATING && event == CustomerEvent.DONE_EATING) {
			state = CustomerState.DONE_EAETING_AND_WAITING_FOR_CHECK;
			print("Done eating, waiting for check");
			//waiting for check to pay
 			return true;
		}
		
		if (state == CustomerState.EATING && event == CustomerEvent.GOT_CHECK) {
			state = CustomerState.EATING_AND_CHECK_AVAILABLE;
			print("Got check, will pay after done eating");
			//waiting to finish up food and pay
			return true;
		}
		
		if (state == CustomerState.DONE_EAETING_AND_WAITING_FOR_CHECK && event == CustomerEvent.GOT_CHECK) {
			state = CustomerState.PAYING;
			payCheck();
			return true;
		}
		
		if (state == CustomerState.EATING_AND_CHECK_AVAILABLE && event == CustomerEvent.DONE_EATING) {
			state = CustomerState.PAYING;
			payCheck();
			return true;
		}
		
		if (state == CustomerState.PAYING && event == CustomerEvent.DONE_PAYING){
			state = CustomerState.LEAVING;
			leaveTable();
			return true;
		}
		
		if (state == CustomerState.LEAVING && event == CustomerEvent.DONE_LEAVING){
			state = CustomerState.DOING_NOTHING;
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

	private void leaveRestaurantBecauseItsFull(){
		print ("Rather leave than wait");
		host.leaveRestaurant(this);
		customerGui.DoExitRestaurant();

	}
	
	private void doNotWantToReorder() {
		//Animation support for Customer leaving after restaurant runs out of your choice
		print ("Don't want to reorder, leaving");
		waiter.msgDoneEating(this);
		customerGui.DoExitRestaurant();
		state = CustomerState.LEAVING;
	}
	
	private void stayInRestaurant() {
		print("It's fine I'll wait");
		host.waitInRestaurant(this);
	}
	
	private void SitDown() {
		Do("Being seated. Going to table");
		customerGui.DoGoToSeat(tableNum);
	}

	private void makeDecision() {	
		print("making decision");

		Double minimumPrice = 100.00;
		for (FoodOnMenu temp : menu) {
			if (temp.price < minimumPrice) minimumPrice = temp.price;
		}
		//Case 1 - Can't afford anything on the menu. Customer leaves
		if (event == CustomerEvent.SEATED){
			if (getPersonAgent().getMoney() < minimumPrice && orderFoodThatICanAfford){
				print ("Can't afford anything, leaving");
				waiter.msgDoneEating(this);
				state = CustomerState.LEAVING;
				customerGui.DoExitRestaurant();
				return;
				
			}
		}
		
		//Case 2 - Restaurant runs out of Customer order, can't afford anything else
		if (event == CustomerEvent.ASKED_TO_REORDER){

			if (getPersonAgent().getMoney() < minimumPrice && (orderFoodThatICanAfford||leaveIfCheapestFoodOutOfStock)){
				print ("Can't afford anything else, leaving");
				waiter.msgDoneEating(this);
				state = CustomerState.LEAVING;
				customerGui.DoExitRestaurant();
				return;
			}
		}
		
		timer.schedule(new TimerTask() {
			public void run() {
				event = CustomerEvent.MADE_DECISION;
				getPersonAgent().CallstateChanged();
			}
		},
		3000);
	}
	
	private void signalToWaiter(){
		Do("I'm ready to order");
		waiter.msgIAmReadyToOrder(this);
	}
	
	private void makeOrder() {
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
			waiter.msgHereIsOrder (this, choice);
			customerGui.madeDecision(choice);
			return;
		}
		
		
		
		for (FoodOnMenu f : menu){
			if (f.type.equals(choice)){
				Do("Can I have " + choice+ " ?");
				waiter.msgHereIsOrder (this, choice);
				customerGui.madeDecision(choice);
				return;
			}
		}
		
		int rand = randomGenerator.nextInt(menu.size());
		choice = menu.get(rand).type;
		Do("Can I have " + choice + " ?");
		waiter.msgHereIsOrder (this, choice);
		customerGui.madeDecision(choice);
		
	}
	
	private void eatFood() {
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
				event = CustomerEvent.DONE_EATING;
				getPersonAgent().CallstateChanged();
			}
		},
		5000);//getHungerLevel() * 1000);//how long to wait before running task
	}

	// customer must pay for his meal
	private void payCheck() {
		print("going to pay check");

		customerGui.DoGoToCashier();
		try {
			atCashier.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		((CashierRoleZt) cashier).msgPayMyCheck(this, getPersonAgent().getMoney());
		getPersonAgent().setMoney(0.0);
		print ("Paying my bill");
	}
	
	private void leaveTable() {
		print("leaving table");

		waiter.msgDoneEating(this);
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
		getPersonAgent().msgDone("RestaurantCustomerRole");
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

	public void setGui(CustomerGuiZt g) {
		customerGui = g;
	}

	public CustomerGuiZt getGui() {
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

