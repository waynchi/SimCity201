package bank;


import agent.Agent;
import bank.gui.BankCustomerGui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Bank customer agent.
 */
public class BankCustomerAgent extends Agent {
	private String name;

	Timer timer = new Timer();
	private BankCustomerGui bankCustomerGui;

	// agent correspondents
	private TellerAgent teller;
	
	private double wallet;
	private int accountID;
	
	public enum CustomerState
	{none, waiting, ready, needAccount, done};
	
	public enum CustomerAction 
	{deposit, withdraw}
	
	private CustomerState state;
	private CustomerAction action;
	
	private double withdraw;
	private double deposit;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public BankCustomerAgent(String name){
		super();
		this.name = name;
	}

	public String getCustomerName() {
		return name;
	}
	// Messages

	public void needMoney(double money) {//from animation
		print("I need money");
		action = CustomerAction.withdraw;
		withdraw = money;
		stateChanged();
	}
	
	public void depositMoney(double money) {//from animation
		print("I need to deposit money");
		action = CustomerAction.deposit;
		deposit = money;
		stateChanged();
	}
	
	public void msgReadyToHelp(TellerAgent t) {
		this.teller = t;
		state = CustomerState.ready;
		stateChanged();
	}
	
	public void msgAccountBalance(int accountID, double balance) {
		this.accountID = accountID;
		print("Account created. Account has a balance of: " + balance);
		state = CustomerState.done;
	}
	
	public void msgGiveLoan(double balance, double money) {
		print("Account has a balance of: " + balance + ". Must pay teller next time for loan");
		wallet += money;
		state = CustomerState.done;
	}
	
	public void msgWithdrawSuccessful(double balance, double money) {
		wallet += money;
		print("Withdraw successful. Account has a balance of: " + balance);
		state = CustomerState.done;
	}
	
	public void msgDepositSuccessful(double balance) {
		print("Deposit successful. Account has a balance of: " + balance);
		state = CustomerState.done;
	}
	
	public void msgRestFull() {
		event = AgentEvent.fullHouse;
		stateChanged();
	}

	public void msgSitAtTable(WaiterAgent waiter, Menu m) {
		this.waiter = waiter;
		this.menu = m;
		print("Received msgSitAtTable");
		event = AgentEvent.followHost;
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
	
	public void msgWhatDoYouWant(){
		print("Received msgWhatDoYouWant");
		event = AgentEvent.ordering;
		stateChanged();
	}
	
	public void msgHereIsYourFood(String choice) {
		print("Received msgHereIsYourFood");
		event = AgentEvent.eating;
		stateChanged();
	}
	
	public void msgChoiceNotAvailable(Menu menu) {
		this.menu = menu;
		print("Received reorder request");
		event = AgentEvent.reordering;
		stateChanged();
	}
	
	public void msgHereIsCheck(Check c) {
		print("Received check for: " + c.balance);
		this.check = c;
		stateChanged();
	}
	
	public void msgCanLeave() {
		print("Allowed to leave");
		event = AgentEvent.donePaying;
		stateChanged();
	}
	
	public void msgPayNextTime() {
		print("Allowed to leave but have to pay remaining bill next time");
		event = AgentEvent.donePaying;
		mustPay = true;
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
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.fullHouse && !wait){
			leaveRest();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.fullHouse && wait){
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followHost ){
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.WaitingAtTable;
			HailWaiter();
			return true;
		}
		
		if (state == AgentState.WaitingAtTable && event == AgentEvent.ordering) {
			state = AgentState.Ordered;
			chooseItem();
			return true;
		}
		if (state == AgentState.Ordered && event == AgentEvent.reordering) {
			state = AgentState.ReOrdered;
		    chooseOtherItem();
		    return true;
		}
		
		if (state == AgentState.Ordered && event == AgentEvent.eating) {
			state = AgentState.Eating;
			EatFood();
			return true;
		}
		if (state == AgentState.ReOrdered && event == AgentEvent.eating) {
			state = AgentState.Eating;
			EatFood();
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.Paying;
			payCheck();
			return true;
		}
		if (state == AgentState.Paying && event == AgentEvent.donePaying){
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

	private void goToRestaurant() {
		Do("Going to restaurant");
		host.msgIWantFood(this);//send our instance, so he can respond to us
	}

	private void SitDown() {
		Do("Being seated. Going to table");
		customerGui.DoGoToSeat();//hack; only one table
	}

	private void EatFood() {
		print(" " + choice);
		customerGui.setChoice(choice);
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
				print("Done eating, " + choice);
				event = AgentEvent.doneEating;
				//isHungry = false;
				stateChanged();
			}
		},
		hungerLevel*1000);
	}
	
	private void HailWaiter() {
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
				CallWaiter();
			}
		},
		waitLevel*1000);
	}
	
	private void CallWaiter() {
		Do("Calling " + waiter.getName());
		waiter.msgReadyToOrder(this);
	}
	
	private void chooseItem(){
		Do("Choosing item");
		if (money > menu.getCost("Salad") && money < menu.getCost("Chicken") ) {
			customerGui.setChoice("Salad" + "?");
			choice = "Salad";
			waiter.msgHereIsChoice(this, choice);
		}
		else {
			if (money == 0 && dash) {
				customerGui.setChoice(name + "?"); //hack for testing. the agent name will be his choice until that food runs out of inventory
				choice = name;
				waiter.msgHereIsChoice(this, name);
			}
			else if (money == 0 && !dash) {
				print("No money, leaving restaurant");
				leaveTable();
			}
			else {
				customerGui.setChoice(name + "?"); //hack for testing. the agent name will be his choice until that food runs out of inventory
				choice = name;
				waiter.msgHereIsChoice(this, name);
			}
		}
	}
	
	private void chooseOtherItem(){
		Do("Choosing new item");
		if (menu.outOfStock("Salad")) {
			print("Can't afford anything else");
			leaveTable();
		}
		else {
			if (money == 0 && dash) {
				Random rand = new Random();
				int randomChoice = rand.nextInt(menu.choices.size());
				choice = menu.choices.get(randomChoice);
				customerGui.setChoice(choice + "?");
				waiter.msgHereIsChoice(this, menu.choices.get(randomChoice));
			}
			else {
				Random rand = new Random();
				int randomChoice = rand.nextInt(menu.choices.size());
				choice = menu.choices.get(randomChoice);
				customerGui.setChoice(choice + "?");
				waiter.msgHereIsChoice(this, menu.choices.get(randomChoice));
			}
		}
	}

	private void leaveTable() {
		customerGui.setChoice(null);
		Do("Leaving.");
		waiter.msgDoneAndLeaving(this);
		customerGui.DoExitRestaurant();
		state = AgentState.Leaving;
	}
	
	private void leaveRest() {
		Do("Leaving restaurant.");
		customerGui.DoExitRestaurant();
		host.msgNoWait(this);
		state = AgentState.Leaving;
	}
	
	private void payCheck() {
		if (mustPay) {
			cashier.msgHereIsMoney(this, check.balance);
			money = 0;
		}
		else {
			if (money < check.balance) {
				cashier.msgHereIsMoney(this, money);
				money = 0;
			}
			else {
				double amount = check.balance;
				money -= check.balance;
				cashier.msgHereIsMoney(this, amount);
			}
		}
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

