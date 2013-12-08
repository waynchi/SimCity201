package restaurant_vk;

import restaurant_vk.gui.VkCustomerGui;
import restaurant_vk.gui.RestaurantVkAnimationPanel;
import restaurant_vk.VkCashierRole;
import restaurant_vk.interfaces.Customer;
import restaurant_vk.interfaces.Host;
import restaurant_vk.interfaces.Waiter;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import people.PeopleAgent;
import people.Role;

/**
 * @author Vikrant Singhal
 *
 * Restaurant customer agent.
 */
public class VkCustomerRole extends Role implements Customer{
	private int hungerLevel = 5; // determines length of meal
	Timer timer = new Timer();
	private VkCustomerGui customerGui;
	private String choice = new String("");
	private Menu menu;
	private CustomerRestaurantCheck currentCheck = null;
	private List<CustomerRestaurantCheck> checks = new ArrayList<CustomerRestaurantCheck>();
	public Host host = null;
	private Waiter waiter;
	private VkCashierRole cashier = null;
	private Semaphore movingAround = new Semaphore(0, true);
	private boolean leaveOption = false;
	private AgentState state = AgentState.DoingNothing;
	private AgentEvent event = AgentEvent.none;

	public VkCustomerRole(RestaurantVkAnimationPanel p) {
		super();
		customerGui = new VkCustomerGui(this);
		customerGui.setAnimationPanel(p);
	}

	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
	
	// Messages

	/*
	 * A message that is called from the gui, which sets the customer to be
	 * hungry.
	 */
	public void gotHungry() {
		print("I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}

	/*
	 * A message called by the waiter asking the customer to follow him.
	 */
	public void followMeToTable(Menu m) {
		print("Received followMeToTable(Menu m).");
		event = AgentEvent.followHost;
		leaveOption = false;
		customerGui.setLeaveOption(leaveOption);
		this.menu = m;
		stateChanged();
	}

	/*
	 * A message called by the waiter asking the customer what he/ she
	 * would like to eat.
	 */
	public void whatWouldYouLike() {
		event = AgentEvent.askedToOrder;
		stateChanged();
	}

	/*
	 * A message called by the waiter that basically serves the customer
	 * his/ her food.
	 */
	public void hereIsYourFood() {
		event = AgentEvent.foodArrives;
		stateChanged();
	}

	/*
	 * A message called from the animation telling the agent that the
	 * seating process has been completed.
	 */
	public void msgAnimationFinishedGoToSeat() {
		event = AgentEvent.seated;
		List<String> menuItems = menu.getAllFoodNames();
		for (String s : menuItems) {
			if (menu.getPrice(s) <= ((PeopleAgent)myPerson).Money) {
				leaveOption = false;
				break;
			}
			else {
				leaveOption = true;
			}
		}
		customerGui.setLeaveOption(leaveOption);
		stateChanged();
	}

	/*
	 * A message called from the animation telling the agent that the
	 * process of leaving the restaurant has been completed..
	 */
	public void msgAnimationFinishedLeaveRestaurant() {
		// from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}
	
	/*
	 * A message called from the animation telling the agent that the
	 * process of going to the cashier is completed.
	 */
	public void msgAnimationFinishedGoToPay() {
		movingAround.release();
		stateChanged();
	}
	
	/*
	 * A message sent by the animation to inform that the customer
	 * has reached the restaurant.
	 */
	public void msgAnimationFinishedGoToRestaurant() {
		movingAround.release();
		stateChanged();
	}

	/*
	 * A message that will be called by the GUI. Once the user selects the
	 * choice from the menu, this message will be called.
	 */
	public void msgDecideChoice(String order) {
		event = AgentEvent.orderDecided;
		choice = order;
		print("My choice is " + choice);
		leaveOption = false;
		customerGui.setLeaveOption(leaveOption);
		stateChanged();
	}
	
	/*
	 * Message by gui which makes the customer leave;
	 */
	public void msgWantToLeave() {
		print("Screw it. I'm leaving!");
		event = AgentEvent.abruptlyLeaving;
		leaveOption = false;
		customerGui.setLeaveOption(leaveOption);
		stateChanged();
	}
	
	/*
	 * A message used by the waiter to tell that the order can't be made.
	 */
	public void outOfChoice(Menu m, String choice) {
		print("I have to decide again! Fuck it!");
		customerGui.setCaption("");
		this.menu = m;
		state = AgentState.BeingSeated;
		event = AgentEvent.seated;
		leaveOption = true;
		customerGui.setLeaveOption(leaveOption);
		stateChanged();
	}
	
	/*
	 * Message sent by the waiter to give the check to the customer;
	 */
	public void hereIsCheck(CustomerRestaurantCheck c) {
		this.currentCheck = c;
		checks.add(c);
		event = AgentEvent.checkArrives;
		stateChanged();
	}
	
	/*
	 * A message sent by the cashier to give back the change.
	 */
	public void hereIsChangeAndApprovedPayments(double change, List<CustomerRestaurantCheck> approvedPayments) {
		event = AgentEvent.changeCollected;
		((PeopleAgent)myPerson).Money += change;
		for (CustomerRestaurantCheck c : approvedPayments) {
			checks.remove(c);
		}
		if (! checks.isEmpty()) {
			print("I still have to pay fo " + checks.size() + " checks and I have $" + ((PeopleAgent)myPerson).Money + " with me now.");
		}
		else {
			print("I have $" + ((PeopleAgent)myPerson).Money + " with me now.");
		}
		stateChanged();
	}
	
	/*
	 * A message called by the host when the customer is waiting in line.
	 */
	public void tablesAreFull() {
		leaveOption = true;
		customerGui.setLeaveOption(leaveOption);
		stateChanged();
	}
	
	public void msgIsActive() {
		isActive = true;
		if (host == null) {
			host = (VkHostRole)myPerson.getRestaurant(1).h;
			if (cashier == null) {
				this.cashier = (VkCashierRole) ((VkHostRole)host).cashier;
			}
		}
		if (host == null) {
			print("FUCK");
		}
		gotHungry();
	}
	
	public void msgIsInActive() {
		stateChanged();
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/

	// Scheduler
	
	/*
	 * CustomerAgent is a finite state machine. Here the combination of states and
	 * events is sufficient to understand what is going on.
	 */
	protected boolean pickAndExecuteAnAction() {

		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry) {
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followHost) {
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		
		if (state == AgentState.BeingSeated && event == AgentEvent.seated) {
			state = AgentState.DecidingOrder;	
			seated();
			return true;
		}

		if (state == AgentState.DecidingOrder && event == AgentEvent.orderDecided) {
			state = AgentState.OrderDecided;
			callWaiter();
			return true;
		}

		if (state == AgentState.OrderDecided && event == AgentEvent.askedToOrder) {
			state = AgentState.Ordering;
			orderFood();
			return true;
		}

		if (state == AgentState.Ordering && event == AgentEvent.foodArrives) {
			state = AgentState.Eating;
			EatFood();
			return true;
		}

		if (state == AgentState.Eating && event == AgentEvent.doneEating) {
			state = AgentState.WaitingForCheck;
			if (currentCheck != null) {
				event = AgentEvent.checkArrives;
			}
			return true;
		}
		
		if (state == AgentState.WaitingForCheck && event == AgentEvent.checkArrives) {
			state = AgentState.GoingToPay;
			goToPay();
			return true;
		}
		
		if (state == AgentState.GoingToPay && event == AgentEvent.changeCollected) {
			state = AgentState.Leaving;
			leaveRestaurant();
			return true;
		}
		
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving) {
			state = AgentState.DoingNothing;
			// no action
			return true;
		}
		
		if (state == AgentState.DecidingOrder && event == AgentEvent.abruptlyLeaving) {
			state = AgentState.Leaving;
			leaveWhileOrdering();
			return true;
		}
		
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.abruptlyLeaving) {
			state = AgentState.Leaving;
			leaveWhileWaiting();
			return true;
		}
		return false;
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/

	// Actions

	/*
	 * An action that is executed when the customer is hungry and reaches the
	 * restaurant. It informs the host that the customer has to be seated.
	 */
	private void goToRestaurant() {
		print("Going to host.");
		customerGui.DoGoToRestaurant();
		try {
			movingAround.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		host.IWantToEat(this);
	}

	/*
	 * An action that carries forward the process of sitting down.
	 */
	private void SitDown() {
		print("Being seated. Going to table");
		customerGui.DoGoToSeat();
	}
	
	/*
	 * It doesn't really take any action. It just prints that the customer is
	 * sitting.
	 */
	private void seated() {
		customerGui.setMenuCopy();
		List<String> menuItems = menu.getAllFoodNames();
		List<String> affordableItems = new ArrayList<String>();
		for (String s : menuItems) {
			double price = menu.getPrice(s);
			double money = ((PeopleAgent)myPerson).Money;
			if (money >= price) {
				affordableItems.add(s);
			}
		}
		if (affordableItems.isEmpty()) {
			print("Screw it. I'm leaving!");
			event = AgentEvent.abruptlyLeaving;
			leaveOption = false;
			customerGui.setLeaveOption(leaveOption);
		}
		else {
			Random generator = new Random();
			int num = generator.nextInt(affordableItems.size());
			choice = affordableItems.get(num);
			event = AgentEvent.orderDecided;
			print("My choice is " + choice);
			leaveOption = false;
			customerGui.setLeaveOption(leaveOption);
		}
	}
	
	/*
	 * An action that calls the waiter by sending him a message.
	 */
	private void callWaiter() {
		print("Waiter called.");
		customerGui.setCaption(choice + "?");
		waiter.readyToOrder(this);
	}
	
	/*
	 * An action that conveys the waiter the choice of he customer. The customer
	 * takes some time to order.
	 */
	private void orderFood() {
		print("Ordering food.");
		waiter.hereIsMyChoice(choice, this);
	}

	/*
	 * An action that makes the customer eat the food. The actual eating is
	 * not shown, but there is timer that causes a delay according to the
	 * hunger level.
	 */
	private void EatFood() {
		print("Eating Food");
		customerGui.setCaption(choice);
		timer.schedule(new TimerTask() {
			Object cookie = 1;

			public void run() {
				print("Done eating, cookie=" + cookie);
				event = AgentEvent.doneEating;
				stateChanged();
			}
		}, 5000);
	}

	/*
	 * An action of leaving the table. It also sends a message to the gui to
	 * carry out the animation.
	 */
	private void leaveRestaurant() {
		print("Leaving.");
		waiter = null;
		leaveOption = false;
		customerGui.setLeaveOption(leaveOption);
		customerGui.DoExitRestaurant();
		currentCheck = null;
		isActive = false;
		myPerson.msgDone("Customer");
	}
	
	/*
	 * Action of just going to the cashier.
	 */
	private void goToPay() {
		waiter.doneEatingAndLeaving(this);
		print("Going to pay.");
		customerGui.DoGoToPay(new Dimension(620, 170));
		try {
			movingAround.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cashier.hereIsPayment(currentCheck, ((PeopleAgent)myPerson).Money, checks);
		((PeopleAgent)myPerson).Money = 0.0;
		print("Take the money.");
	}
	
	/*
	 * Leaves restaurant when ordering.
	 */
	public void leaveWhileOrdering() {
		waiter.leavingWithoutEating(this);
		waiter = null;
		choice = "";
		leaveOption = false;
		customerGui.setLeaveOption(leaveOption);
		customerGui.DoGoAway();
		isActive = false;
		myPerson.msgDone("Customer");
	}
	
	/*
	 * Leaves restaurant when ordering.
	 */
	public void leaveWhileWaiting() {
		host.ICantWait(this);
		waiter = null;
		leaveOption = false;
		customerGui.setLeaveOption(leaveOption);
		customerGui.DoGoAway();
		isActive = false;
		myPerson.msgDone("Customer");
	}

	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/

	// Utilities

	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
	}

	public String toString() {
		return "customer " + ((PeopleAgent)myPerson).name;
	}

	public void setGui(VkCustomerGui g) {
		customerGui = g;
	}

	public VkCustomerGui getGui() {
		return customerGui;
	}

	public AgentState getState() {
		return state;
	}
	
	public void setWaiter(Waiter waiter) {
		this.waiter = waiter;
	}
	
	/*
	 * Just checks if the customer has been assigned to the waiter.
	 */
	public boolean isAssignedToWaiter() {
		if (waiter != null)
			return true;
		return false;
	}
	
	public boolean canLeave() {
		return leaveOption;
	}
	
	public boolean canLeaveWhileWaiting() {
		if (leaveOption == false && waiter == null && state == AgentState.WaitingInRestaurant)
			return true;
		return false;
	}
	
	/*
	 * Checks if the customer is ready to be seated. This is purely an
	 * implementation hack.
	 */
	public boolean isReadyToSit() {
		if (event == AgentEvent.gotHungry && state == AgentState.WaitingInRestaurant && !isAssignedToWaiter())
			return true;
		return false;
	}
	
	public boolean isWaitingInLine() {
		if (state == AgentState.WaitingInRestaurant)
			return true;
		return false;
	}
	
	public Menu getMenu() {
		return menu;
	}
	
	public void setCashier(VkCashierRole c) {
		cashier = c;
	}
	
	public void setLeaveOption(boolean b) {
		leaveOption = b;
	}
	
	public double getCash() {
		return ((PeopleAgent)myPerson).Money;
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
	
	// Helper Data Structures
	
	/*
	 * List of states of the customer.
	 */
	public enum AgentState
	{
		DoingNothing, WaitingInRestaurant, BeingSeated, Seated, Eating, DoneEating, Leaving, Ordering, DecidingOrder, OrderDecided,
		WaitingForCheck, Paying, GoingToPay
	};
	
	/*
	 * List of events that can happen.
	 */
	public enum AgentEvent {
		none, gotHungry, followHost, seated, doneEating, doneLeaving, askedToOrder, foodArrives, orderDecided, checkArrives,
		changeCollected, abruptlyLeaving
	};
}