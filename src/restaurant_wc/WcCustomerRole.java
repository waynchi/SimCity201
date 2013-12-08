package restaurant_wc;

import restaurant_wc.gui.CustomerGui;
import restaurant_wc.gui.RestaurantGui;
import restaurant_wc.interfaces.Cashier;
import restaurant_wc.interfaces.Customer;
import restaurant_wc.interfaces.Waiter;
import agent.Agent;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import people.Role;

/**
 * Restaurant customer agent.
 */
public class WcCustomerRole extends Role implements Customer{
	private String name;
	private int hungerLevel = 5;        // determines length of meal
	private double WalletMin = 5.00;
	private double WalletMax = 20.00;
	Timer timer = new Timer();
	private CustomerGui customerGui;
	//private DecimalFormat myFormatter = new DecimalFormat("#.##");
	private NumberFormat nf = NumberFormat.getInstance();
	
	private Semaphore following = new Semaphore(0,true);
	
	private String MyChoice;

	// agent correspondents
	private WcHostAgent host;
	private Waiter waiter;
	private Menu MyMenu;
	private Cashier cashier;
	private double Check;
	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, Ordering, Ordered, BeingSeated, Seated, Eating, DoneEating, Leaving, Paying, Paid};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, followHost, receivedFood, ordering, seated, doneEating, doneLeaving, newMenu, donePaying, paying, noOrder, tooLong};
	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for WcCustomerRole class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public WcCustomerRole(){
		super();
		
		/*Random RandomNum = new Random();
		Wallet = WalletMin + (WalletMax - WalletMin) * RandomNum.nextDouble();
		print("Amount of Money: " + Wallet);*/
	}

	public Semaphore getFollowing() {
		return following;
	}
	/**
	 * hack to establish connection to Host agent.
	 */
	

	public void setCashier(Cashier cashier2) {
		this.cashier = cashier2;
		
	}
	
	public void setHost(WcHostAgent host) {
		this.host = host;
	}
	
	public void setWaiter(Waiter waiter) {
		this.waiter = waiter;
	}
	
	public Waiter getWaiter(){
		return waiter;
	}

	public String getCustomerName() {
		return name;
	}
	// Messages

	public void msgIsActive()
	{
		print("I'm hungry");
		event = AgentEvent.gotHungry;
		this.isActive = true;
		stateChanged();
	}

	public void msgSitAtTable(Waiter w) {
		print("Received msgSitAtTable");
		waiter = w;
		event = AgentEvent.followHost;
		//print("Received Menu");
		stateChanged();
	}
	
	public void msgWhatWouldYouLike(Menu menu) {
		print("Received msgWhatWouldYouLike");
		event = AgentEvent.ordering;
		MyMenu = menu;
		stateChanged();
	}
	

	public void msgWhatElseWouldYouLike(String outOfChoice) {
		MyMenu.Choices.remove(outOfChoice);
		print("Received msgWhatElseWouldYouLike");
		if(MyMenu.Choices.size() > 0){
			event = AgentEvent.newMenu;
		//state = AgentState.Ordering;
		}
		else{
			event = AgentEvent.noOrder;
		}
		stateChanged();
		
	}
	public void msgHereIsYourFood() {
		print("Recieved my Food!");
		event = AgentEvent.receivedFood;
		stateChanged();
	}

	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		stateChanged();
	}
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}
	
	public void msgHereIsYourTotal(double bill) {
		// TODO Auto-generated method stub
		//Confirm choice?
		Check = bill;
		event = AgentEvent.paying;
		stateChanged();
	}
	

	public void msgHereIsYourChange(double d) {
		// TODO Auto-generated method stub
		print("Recieving Change");
		event = AgentEvent.donePaying;
		stateChanged();
	}
	
	public void msgYouNeedToWait(int customerNum) {
		// TODO Auto-generated method stub
		Random RandomNum = new Random();
		int rand = RandomNum.nextInt(5);
		if(rand < 2)
		{
			print("This is not worth the wait. I'm leaving.");
			event = AgentEvent.tooLong;
			stateChanged();
		}
		else
		{
			print("This is worth the wait!");
			customerGui.msgHeadToWaitingArea(customerNum);
		}
		
	}


	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		//	WcCustomerRole is a finite state machine

		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.tooLong)
		{
			state = AgentState.DoingNothing;
			leaveRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followHost ){
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.Ordering;
			timer.schedule(new TimerTask() {
				public void run() {
				}
			},
			hungerLevel*600);
			CallWaiter();
			//EatFood();
			return true;
		}
		if (state == AgentState.Ordering && event == AgentEvent.ordering) {
			state = AgentState.Ordered;
			TellWaiter();
			return true;
		}
		if (state == AgentState.Ordered && event == AgentEvent.newMenu) {
			state = AgentState.Ordering;
			ReOrder();
			return true;
		}
		if (state == AgentState.Ordered && event == AgentEvent.noOrder) {
			state = AgentState.Leaving;
			leaveTable();
			return true;
		}
		if (state == AgentState.Ordered && event == AgentEvent.receivedFood){
			state = AgentState.Eating;
			EatFood();
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.Paying;
			GetCheck();
			return true;
		}
		if (state == AgentState.Paying && event == AgentEvent.paying){
			state = AgentState.Paid;
			PayCheck();
			return true;
		}
		if (state == AgentState.Paid && event == AgentEvent.donePaying){
			state = AgentState.Leaving;
			leaveTable();
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

	private void leaveRestaurant() {
		// TODO Auto-generated method stub
		host.msgImLeaving(this);
		customerGui.DoExitRestaurant();
	}

	private void ReOrder() {
		event = AgentEvent.ordering;
		stateChanged();
		
	}

	private void PayCheck() {
		Do("Paying Cashier");
		if( getPersonAgent().getMoney() >= Check)
		{
			 getPersonAgent().setMoney(getPersonAgent().getMoney() - Check);
			 cashier.msgHereIsMyPayment(Check, Check, this);
		}
		else
		{
			cashier.msgHereIsMyPayment(Check, getPersonAgent().getMoney(), this);
			getPersonAgent().setMoney(0);
		}
		
		
		
		
	}

	private void GetCheck() {
		Do("Getting Check");
		waiter.msgIWantMyCheck(MyChoice, this);
	}

	private void TellWaiter() {
		Random RandomOrder = new Random();
		//So that the customer will only order items he can afford.
		Menu tempMenu = new Menu(MyMenu.Choices, MyMenu.FoodCosts);
		String choice = null;
		synchronized(tempMenu.Choices)
		{
		for(int j = 0; j < tempMenu.FoodCosts.size(); j++)
		{
			if(MyMenu.FoodCosts.get(MyMenu.Choices.get(j)) > getPersonAgent().getMoney())
			{
				print("Unable to purchase " + MyMenu.Choices.get(j) + " so I am not going to order it");
				//tempMenu.FoodCosts.remove(MyMenu.Choices.get(j));
				tempMenu.Choices.remove(MyMenu.Choices.get(j));
			}
		}
		}
		if(tempMenu.Choices.isEmpty())
		{
			print("Unable to afford anything!");
			Random randomNum = new Random();
			int random = randomNum.nextInt(5);
			if(random < 1)
			{
				print ("Ordering anyway");
				int OrderChoice = RandomOrder.nextInt(MyMenu.Choices.size());
				synchronized(MyMenu.Choices){
				for(int i = 0; i < MyMenu.Choices.size(); i++) {
					if(i == OrderChoice)
					{
						choice = MyMenu.Choices.get(i);
					}
			}
				}
			}
			else
			{
				event = AgentEvent.noOrder;
				return;
			}
		}
		else
		{
			int OrderChoice = RandomOrder.nextInt(tempMenu.Choices.size());
			//String choice = null;
			synchronized(tempMenu.Choices)
			{
			for(int i = 0; i < tempMenu.Choices.size(); i++) {
				if(i == OrderChoice)
				{
				choice = tempMenu.Choices.get(i);
				}
			}
			}
		}
		/*switch (OrderChoice){
			case 0: choice = MyMenu.Choices.get(0);
			break;
			case 1: choice = "Chicken";
			break;
			case 2: choice = "Salad";
			break;
			case 3: choice = "Pizza";
			break;
			default: choice = "Steak";
			break;
		}*/
		//choice = name;
		//TODO
		//change it back
		MyChoice = choice;
		if(name.contains("Steak") || name.contains("steak"))
		{
			MyChoice = "Steak";
			MyMenu.Choices.remove("Chicken");
			MyMenu.Choices.remove("Pizza");
			MyMenu.Choices.remove("Salad");
		}
		else if(name.contains("Chicken") || name.contains("chicken"))
		{
			MyChoice = "Chicken";
			MyMenu.Choices.remove("Steak");
			MyMenu.Choices.remove("Pizza");
			MyMenu.Choices.remove("Salad");
		}
		else if(name.contains("Salad") || name.contains("salad"))
		{
			MyChoice = "Salad";
			MyMenu.Choices.remove("Chicken");
			MyMenu.Choices.remove("Pizza");
			MyMenu.Choices.remove("Steak");
		}
		else if(name.contains("Pizza") || name.contains("pizza"))
		{
			MyChoice = "Pizza";
			MyMenu.Choices.remove("Chicken");
			MyMenu.Choices.remove("Steak");
			MyMenu.Choices.remove("Salad");
		}
		Do("Here's my Order: " + MyChoice);
		waiter.msgHereIsMyChoice(MyChoice, this);
		customerGui.OrderWaiting();
		
	}

	private void goToRestaurant() {
		Do("Going to restaurant_wc");
		host.msgIWantFood(this);
		final WcCustomerRole myself = this;
		//This is how the customer decides to stay or leave if she's been waiting for a while.
		/*timer.schedule(new TimerTask() {
			public void run() {
				if(state == AgentState.WaitingInRestaurant)
				{
					Random RandomNum = new Random();
					int random = RandomNum.nextInt(5);
					if(random <= 2)
					{
					Do("This is taking too long. I'm leaving");
					host.msgImLeaving(myself);
					event = AgentEvent.tooLong;
					stateChanged();
					}
					else
					{
						print("I'll wait it out because this restaurant_wc is fantastic.");
					}
				}
			}
		},
		hungerLevel*2000);*///send our instance, so he can respond to us
	}

	private void SitDown() {
		Do("Being seated. Going to table");
		try {
			following.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		customerGui.DoGoToSeat(0);//hack; only one table
		
	}
	
	private void CallWaiter() {
		Do("Calling Waiter over.");
		waiter.msgReadyToOrder(this);
	}

	private void EatFood() {
		customerGui.OrderDelivered(MyChoice);
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
			public void run() {
				print("Done eating " + MyChoice);
				event = AgentEvent.doneEating;
				//isHungry = false;
				stateChanged();
			}
		},
		hungerLevel*1000);//getHungerLevel() * 1000);//how long to wait before running task
	}

	private void leaveTable() {
		Do("Leaving.");
		waiter.msgLeavingTable(this);
		customerGui.DoExitRestaurant();
	}

	// Accessors, etc.

	public String getName() {
		return name;
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




}

