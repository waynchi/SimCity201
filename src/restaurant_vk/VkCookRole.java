package restaurant_vk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import people.PeopleAgent;
import people.Role;
import restaurant_vk.gui.VkCookGui;
import restaurant_vk.gui.RestaurantVkAnimationPanel;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import market.interfaces.*;
import restaurant_vk.interfaces.Waiter;

/**
 * @author Vikrant Singhal
 * 
 * A class representing the Cook. It takes orders from waiters, cooks food
 * and plates the orders for the respective waiters to collect. When running
 * low on some food item, he orders it from the three markets it has.
 */
public class VkCookRole extends Role implements Cook {

	// Data
	
	private Map<String, Food> inventory = new HashMap<String, Food>();
	private List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	private List<MarketOrder> marketOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
	private Timer timer;
	private VkCookGui gui = null;
	private Semaphore movingAround = new Semaphore(0, true);
	private RevolvingStand stand;
	private Timer standTimer;
	private final int period = 1000;
	private List<MarketEmployee> markets = new ArrayList<MarketEmployee>();
	private Cashier cashier;
	private boolean leave = false;
	private boolean enter = false;
	private ClosingState closingState = ClosingState.Closed;
	private VkHostRole host;

	public VkCookRole(RevolvingStand s, RestaurantVkAnimationPanel p) {
		timer = new Timer();
		standTimer = null;
		this.stand = s;
		gui = new VkCookGui(this);
		gui.setAnimationPanel(p);
		initializeInventory();
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/

	// Actions
	
	/*
	 * An action to cook the specified choice of food.
	 */
	private void cookIt(final Order o) {
		print("Cooking " + o.choice + "!");
		
		Food f = inventory.get(o.choice);
		synchronized (f) {
			if (f.amount == 0) {
				print("I'm out of " + o.choice + "!");
				o.w.outOf(o.choice, o.table);
				synchronized (orders) {
					orders.remove(o);
				}
				if (f.amount < f.low && f.s != FoodState.Requested) {
					f.s = FoodState.Low;
				}
				return;
			}
		}
		DoCookIt(o.choice);
		try {
			movingAround.acquire();
		} catch (InterruptedException e) {}
		o.s = State.Cooking;
		synchronized (f) {
			print("Amount of " + o.choice + " before cooking: " + f.amount);
			f.amount = f.amount - 1;
			print("Amount of " + o.choice + " after cooking: " + f.amount);
			if (f.amount < f.low && f.s != FoodState.Requested) {
				f.s = FoodState.Low;
			}
		}
		timer.schedule(new TimerTask() {
			public void run() {
				foodCooked(o);
			}
		}, inventory.get(o.choice).getCookingTime());
	}

	/*
	 * An action to plate the order once it has been cooked.
	 */
	private void plateIt(Order o) {
		print("Plating " + o.choice);
		DoPlateIt(o.choice);
		try {
			movingAround.acquire();
		} catch (InterruptedException e) {}
		o.w.orderIsReady(o.choice, o.table);
		o.s = State.Finished;
		print("Done plating " + o.choice);
	}
	
	public void orderFromMarket() {
		print("Ordering from market.");
		Map<String, Integer> order = new HashMap<String, Integer>();
		for (Map.Entry<String, Food> e : inventory.entrySet()) {
			if (e.getValue().s == FoodState.Low) {
				Food f = e.getValue();
				f.s = FoodState.Requested;
				int qty = f.capacity - f.amount;
				order.put(f.name, qty);
			}
		}
		int marketNumber = selectMarket();
		MarketEmployee market = markets.get(marketNumber);
		MarketOrder o = new MarketOrder(order, marketNumber);
		marketOrders.add(o);
		market.msgHereIsAnOrder(order, this, cashier);
	}
	
	public void informCashier(MarketOrder mo) {
		print("Informing cashier about order.");
		mo.s = MarketOrderState.InformedCashier;
		cashier.msgGotMarketOrder(mo.itemsSupplied, mo.orderNumber, mo.marketNumber);
	}
	
	private void enterRestaurant() {
		if (closingState == ClosingState.Closed) {
			standTimer = new Timer();
			startStandTimer();
			closingState = ClosingState.None;
		}
		gui.DoEnterRestaurant();
		try {
			movingAround.acquire();
		} catch (InterruptedException e) {}
		enter = false;
	}
	
	private void leaveRestaurant() {
		if (closingState == ClosingState.None)
			((VkCashierRole) cashier).recordShift((PeopleAgent)myPerson, "Cook");
		gui.DoLeaveRestaurant();
		try {
			movingAround.acquire();
		} catch (InterruptedException e) {}
		isActive = false;
		leave = false;
		myPerson.msgDone("RestaurantCookRole");
	}
	
	private void prepareToClose() {
		((VkCashierRole) cashier).recordShift((PeopleAgent)myPerson, "Cook");
		closingState = ClosingState.Preparing;
	}
	
	private void shutDown() {
		standTimer = null;
		closingState = ClosingState.Closed;
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/

	// Messages
	
	/*
	 * A message used by the waiter to tell the cook what to cook for a
	 * specified table.
	 */
	public void hereIsOrder(Waiter w, String choice, int table) {
		synchronized (orders) {
			orders.add(new Order(w, choice, table));
		}
		stateChanged();
	}

	/*
	 * A message that the timer calls once the food has been cooked.
	 */
	public void foodCooked(Order o) {
		o.s = State.Cooked;
		print("Done cooking " + o.choice + ".");
		stateChanged();
	}
	
	/*
	 * A message called by the standTimer asking the cook to check
	 * the stand.
	 */
	public void checkStand() {
		if (stand.size() > 0) {
			synchronized(orders) {
				orders.add(stand.removeOrder());
			}
			stateChanged();
		}
	}
	
	/*
	 * A message called by the gui when the destination is reached.
	 */
	public void atDestination() {
		movingAround.release();
	}
	
	/*
	 * A message called by the market employee to assign order number to an order.
	 */
	@Override
	public void msgHereIsYourOrderNumber(Map<String, Integer> items, int orderNumber, int marketNumber) {
		for (MarketOrder mo : marketOrders) {
			if (mo.itemsRequested == items && marketNumber == mo.marketNumber) {
				mo.orderNumber = orderNumber;
				return;
			}
		}
		stateChanged();
	}
	
	/*
	 * A message called by the market truck to deliver items that had
	 * been requested.
	 */
	@Override
	public void msgHereIsYourOrder(Map<String, Integer> items, int orderNumber, int marketNumber) {
		MarketOrder mo = findMarketOrder(orderNumber, marketNumber);
		mo.itemsSupplied = items;
		for (Map.Entry<String, Integer> e : items.entrySet()) {
			Food f = inventory.get(e.getKey());
			f.s = FoodState.Enough;
			f.amount += e.getValue();
		}
		for (Map.Entry<String, Food> e : inventory.entrySet()) {
			Food f = e.getValue();
			if (f.amount < f.low) {
				f.s = FoodState.Low;
			}
		}
		mo.s = MarketOrderState.Supplied;
		stateChanged();
	}
	
	public void closeRestaurant() {
		closingState = ClosingState.ToBeClosed;
		stateChanged();
	}
	
	public void msgIsActive() {
		if (cashier == null) {
			this.cashier = ((VkHostRole)host).cashier;
		}
		if (markets.size() == 0) {
			this.markets.add((MarketEmployee) myPerson.getMarketEmployee(0));
//			this.markets.add((MarketEmployee) myPerson.getMarketEmployee(1));
		}
		isActive = true;
		enter = true;
		stateChanged();
	}
	
	public void msgIsInActive() {
		leave = true;
		stateChanged();
	}
	
	public void setLow() {
		Food f1 = inventory.get("Steak");
		f1.low = 2;
		f1.amount = 1;
		f1.s = FoodState.Low;
		
		Food f2 = inventory.get("Chicken");
		f2.low = 2;
		f2.amount = 1;
		f2.s = FoodState.Low;
		
		Food f3 = inventory.get("Salad");
		f3.low = 2;
		f3.amount = 1;
		f3.s = FoodState.Low;
		
		Food f4 = inventory.get("Pizza");
		f4.low = 2;
		f4.amount = 1;
		f4.s = FoodState.Low;
		
		stateChanged();
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/

	// Scheduler
	
	@Override
	protected boolean pickAndExecuteAnAction() {
		if (enter == true) {
			enterRestaurant();
			return true;
		}
		
		if (closingState == ClosingState.ToBeClosed) {
			prepareToClose();
			return true;
		}
		
		if (closingState == ClosingState.Preparing && !((VkHostRole)host).anyCustomer() && leave == true) {
			shutDown();
			leaveRestaurant();
			return true;
		}
		
		if (leave == true && closingState == ClosingState.None) {
			leaveRestaurant();
			return true;
		}
		
		Order o;
		
		MarketOrder mo = findMarketOrderByState(MarketOrderState.Supplied);
		if (mo != null) {
			informCashier(mo);
			return true;
		}
		
		if (isAnythingLow()) {
			orderFromMarket();
			return true;
		}
		
		// Searches for an order that has been cooked and plates it.
		o = findOrderByState(State.Cooked);
		if (o != null) {
			plateIt(o);
			return true;
		}
		
		// Searches for a pending order and cooks it.
		o = findOrderByState(State.Pending);
		if (o != null) {
			cookIt(o);
			return true;
		}
		
		return false;
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
	
	// Animation methods. DoXYZ() routines.
	
	private void DoCookIt(String choice) {
		gui.DoCookIt(choice);
	}
	
	private void DoPlateIt(String choice) {
		gui.DoPlateIt(choice);
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
	
	// Utilities
	
	public boolean isAnythingLow() {
		for (Map.Entry<String, Food> e : inventory.entrySet()) {
			if (e.getValue().s == FoodState.Low)
				return true;
		}
		return false;
	}
	
	/*
	 * Finds the first order in the list of orders which has the specified
	 * state.
	 */
	private Order findOrderByState(State state) {
		synchronized (orders) {
			for (Order o : orders) {
				if (o.s == state)
					return o;
			}
		}
		return null;
	}
	
	private MarketOrder findMarketOrder(int number, int marketNumber) {
		for (MarketOrder o : marketOrders) {
			if (o.orderNumber == number && o.marketNumber == marketNumber)
				return o;
		}
		return null;
	}
	
	private MarketOrder findMarketOrderByState(MarketOrderState state) {
		synchronized (marketOrders) {
			for (MarketOrder o : marketOrders) {
				if (o.s == state)
					return o;
			}
		}
		return null;
	}
	
	/*
	 * This is to initialize the inventory of food materials of the cook.
	 */
	private void initializeInventory() {
		Food f1 = new Food("Steak", 500, 10, 2, 10);
		Food f2 = new Food("Chicken", 500, 10, 2, 10);
		Food f3 = new Food("Salad", 500, 10, 2, 10);
		Food f4 = new Food("Pizza", 500, 10, 2, 10);
		inventory.put("Steak", f1);
		inventory.put("Chicken", f2);
		inventory.put("Salad", f3);
		inventory.put("Pizza", f4);
	}
	
	@Override
	public String toString() {
		return "Cook";
	}
	
	public void setGui(VkCookGui g) {
		gui = g;
	}
	
	public void setStand(RevolvingStand s) {
		this.stand = s;
	}
	
	public void startStandTimer() {
		standTimer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				checkStand();
			}
		}, period, period);
	}
	
	public void setHost(VkHostRole h) {
		this.host = h;
		setCashier(h.getCashier());
	}
	
	public void addMarket(MarketEmployee m) {
		markets.add(m);
	}
	
	@Override
	public int getRestaurantIndex() {
		return 1;
	}
	
	public void setCashier(Cashier c) {
		this.cashier = c;
	}
	
	public String getName() {
		return myPerson.getName();
	}
	
	private int selectMarket() {
		Random generator = new Random();
		int num = generator.nextInt(markets.size());
		if (markets.size() != 0)
			return num;
		return -1;
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/

	// Helper Data Structures
	
	/*
	 * List of states of an order.
	 */
	enum State {
		Pending, Cooking, Cooked, Finished
	};
	
	/*
	 * States for different types of food items according to their
	 * availability.
	 */
	enum FoodState {Enough, Low, Requested};
	
	/*
	 * States for a market order.
	 */
	enum MarketOrderState {Requested, Supplied, InformedCashier};
	
	/*
	 * Class for encapsulating the food type and its availability
	 * parameters.
	 */
	private class Food {
		private String name;
		private int cookingTime;
		private int amount;
		private int low;
		private int capacity;
		private FoodState s;
		
		public Food(String name, int cookingTime, int amount, int low, int capacity) {
			this.name = name;
			this.cookingTime = cookingTime;
			this.amount = amount;
			this.low = low;
			this.capacity = capacity;
			this.s = FoodState.Enough;
		}
		
		public int getCookingTime() {
			return cookingTime;
		}
	}

	/*
	 * A private class that encapsulates the details of an order.
	 */
	public class Order {
		Waiter w;
		String choice;
		int table;
		State s;

		public Order(Waiter w, String choice, int table) {
			this.w = w;
			this.choice = choice;
			this.table = table;
			s = State.Pending;
		}
	}
	
	/*
	 * A class to encapsulate a market order.
	 */
	public class MarketOrder {
		public Map<String, Integer> itemsRequested = new HashMap<String, Integer>();
		public Map<String, Integer> itemsSupplied = new HashMap<String, Integer>();
		public int orderNumber;
		public int marketNumber;
		public MarketOrderState s;
		
		public MarketOrder(Map<String, Integer> items, int marketNumber) {
			itemsRequested = items;
			s = MarketOrderState.Requested;
			this.marketNumber = marketNumber;
		}
	}
	
	enum ClosingState {None, ToBeClosed, Preparing, Closed}
}