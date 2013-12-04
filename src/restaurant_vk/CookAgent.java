package restaurant_vk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import restaurant_vk.gui.CookGui;
import restaurant.interfaces.Cook;
import market.interfaces.*;
import restaurant_vk.interfaces.Waiter;
import agent.Agent;

/**
 * @author Vikrant Singhal
 * 
 * A class representing the Cook. It takes orders from waiters, cooks food
 * and plates the orders for the respective waiters to collect. When running
 * low on some food item, he orders it from the three markets it has.
 */
public class CookAgent extends Agent implements Cook {

	// Data
	
	private Map<String, Food> inventory = new HashMap<String, Food>();
	private List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
//	private List<Market> markets = new ArrayList<Market>();
	private Timer timer;
	private CookGui gui = null;
	private Semaphore movingAround = new Semaphore(0, true);
	private RevolvingStand stand;
	private Timer standTimer;
	private final int period = 5000;

	public CookAgent() {
		timer = new Timer();
		standTimer = new Timer();
		initializeInventory();
		startStandTimer();
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
//					f.qtyRequested = f.capacity - f.amount;
				}
				return;
			}
		}
		DoCookIt(o.choice);
		try {
			movingAround.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		o.s = State.Cooking;
		synchronized (f) {
			print("Amount of " + o.choice + " before cooking: " + f.amount);
			f.amount = f.amount - 1;
			print("Amount of " + o.choice + " after cooking: " + f.amount);
			if (f.amount < f.low && f.s != FoodState.Requested) {
				f.s = FoodState.Low;
//				f.qtyRequested = f.capacity - f.amount;
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
		DoPlateIt(o.choice);
		try {
			movingAround.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		o.w.orderIsReady(o.choice, o.table);
		o.s = State.Finished;
	}
	
	/*
	 * Action to request for food.
	 */
	private void requestFood(Food f) {
//		if (f.m0Req == false) {
//			f.s = FoodState.Requested;
//			print("Requesting Market #0 for " + f.name + ".");
//			markets.get(0).runningLow(f.name, f.qtyRequested);
//			f.m0Req = true;
//		}
//		else if (f.m1Req == false) {
//			print("Requesting Market #1 for " + f.name + ".");
//			f.s = FoodState.Requested;
//			markets.get(1).runningLow(f.name, f.qtyRequested);
//			f.m1Req = true;
//		}
//		else if (f.m2Req == false) {
//			print("Requesting Market #2 for " + f.name + ".");
//			f.s = FoodState.Requested;
//			markets.get(2).runningLow(f.name, f.qtyRequested);
//			f.m2Req = true;
//		}
//		else {
//			f.s = FoodState.Requested;
//		}
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
	 * A message called by the market if it doesn't have the requested item.
	 */
	public void dontHaveIt(String food, int mNum) {
		Food f = inventory.get(food);
		synchronized (f) {
			f.s = FoodState.Low;
		}
		stateChanged();
	}
	
//	/*
//	 * A message called by the market when it gives the requested materials to
//	 * the cook.
//	 */
//	public void hereAreMaterials(String food, int qty, int marketNum) {
//		Food f = inventory.get(food);
//		synchronized (f) {
//			if (qty < f.qtyRequested) {
//				f.qtyRequested = f.qtyRequested - qty;
//				if (marketNum == 0)
//					f.m0Req = true;
//				else if (marketNum == 1)
//					f.m1Req = true;
//				else if (marketNum == 2)
//					f.m2Req = true;
//				f.s = FoodState.Low;
//			}
//			else {
//				f.s = FoodState.Enough;
//				f.m0Req = false;
//				f.m1Req = false;
//				f.m2Req = false;
//			}
//			f.amount += qty;
//		}
//		print(qty + " " + food + " supplied by Market #" + marketNum + ".");
//		stateChanged();
//	}
	
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
	 * A message called by the market truck to deliver items that had
	 * been requested.
	 */
	@Override
	public void msgHereIsYourOrder(Map<String, Integer> items) {
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/

	// Scheduler
	
	@Override
	protected boolean pickAndExecuteAnAction() {
		Order o;
		
		// If running low on any food item, request a market for materials.
		Food f = fFind(FoodState.Low);
		if (f != null) {
			requestFood(f);
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
	
//	/*
//	 * Adds an instance of market to the list of markets.
//	 */
//	public void addMarket(Market m) {
//		markets.add(m);
//	}
	
	/*
	 * This is to initialize the inventory of food materials of the cook.
	 */
	private void initializeInventory() {
		Food f1 = new Food("Steak", 1000, 1, 1, 5);
		Food f2 = new Food("Chicken", 1000, 1, 2, 5);
		Food f3 = new Food("Salad", 1000, 1, 2, 5);
		Food f4 = new Food("Pizza", 1000, 1, 2, 5);
		inventory.put("Steak", f1);
		inventory.put("Chicken", f2);
		inventory.put("Salad", f3);
		inventory.put("Pizza", f4);
	}
	
	@Override
	public String toString() {
		return "Cook";
	}
	
	public void setGui(CookGui g) {
		gui = g;
	}
	
	private Food fFind(FoodState fs) {
		if (inventory.get("Steak").s == fs) {
			return inventory.get("Steak");
		}
		if (inventory.get("Chicken").s == fs) {
			return inventory.get("Chicken");
		}
		if (inventory.get("Salad").s == fs) {
			return inventory.get("Salad");
		}
		if (inventory.get("Pizza").s == fs) {
			return inventory.get("Pizza");
		}
		return null;
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
		private boolean ordered = false;
		private int qtyNeeded = 0;
		
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
		public String item;
		public int qtyRequested;
		public int qtySupplied;
		public MarketOrderState s;
		
		public MarketOrder(String item, int qty) {
			this.item = item;
			this.qtyRequested = qty;
			s = MarketOrderState.Requested;
		}
	}
}