package restaurant;

import agent.Agent;
import restaurant.BaseWaiterRole;
import restaurant.gui.CookGui;
import restaurant.gui.HostGui;
import restaurant.gui.RestaurantPanel.CookWaiterMonitor;
import restaurant.interfaces.Cashier;
import java.awt.Dimension;
import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Cook Agent
 */
// There is only one CookAgent in this version of Restaurant. Waiter(s) will take customers' order 
// and then go to the cook and tell him to prepare the food. The CookAgent will then cook the food (
// cooking time depends on food) and plate it, then notify the correct waiter that food is done.
// Cook may run out of food and order that from markets (three for now)

//Cook can run out of food, and when that happens, he orders food from the market. He only orders food 
//has not been ordered for now.


public class CookRole extends Agent{

	private List<MyOrder> orders = Collections.synchronizedList(new ArrayList<MyOrder>());
	private List<MarketRole> markets = Collections.synchronizedList(new ArrayList<MarketRole>());
	private String name;
	public enum OrderState {PENDING, COOKING, DONE, PLATED};
	private CookWaiterMonitor theMonitor = null;

	private Map<String, Food> foods = Collections.synchronizedMap(new HashMap<String, Food>());			
	private Boolean onOpen;
	private Timer schedulerTimer = new Timer();
	private Cashier cashier;

	private CookGui cookGui = null;
	
	private class MarketOrder {
		private Map<String, Integer> marketOrder = Collections.synchronizedMap(new HashMap<String, Integer>());
		Boolean isResponded;
		int marketCount;

		private MarketOrder (Map<String,Integer> mo){
			marketOrder = mo;
			isResponded = false;
			marketCount = 1;
		};
	}

	private List<MarketOrder> marketOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());

	/**
	 * Constructor for CookAgent class
	 *
	 * @param name name of the cook
	 */
	public CookRole(String name, CookWaiterMonitor monitor) {
		super();
		onOpen = true;
		this.name = name;
		foods.put("Steak", new Food("Steak"));
		foods.put("Chicken", new Food("Chicken"));
		foods.put("Salad", new Food("Salad"));
		foods.put("Pizza", new Food("Pizza"));
		theMonitor = monitor;
	}


	// Messages

	// Cook receives an order from the waiter and stores it into a list
	public void msgHereIsAnOrder (String food, BaseWaiterRole w,int tableNum) {
		orders.add( new MyOrder(food, w, tableNum));
		print("Receiving order, " + food + " for table #"+tableNum);
		stateChanged();
	}

	// Food order is cooked, managed by timer
	public void timerDone(MyOrder order)
	{
		order.state = OrderState.DONE;
		stateChanged();
	}	


	public void msgSupply (Map<String,Integer> orderList, Map<String,Integer> supplyList) {
		synchronized (marketOrders) {

			for (MarketOrder mo : marketOrders) {
				if (mo.marketOrder == orderList) {
					for (Map.Entry<String, Integer> entry : mo.marketOrder.entrySet()) {
						mo.marketOrder.put(entry.getKey(), (entry.getValue()-supplyList.get(entry.getKey())));
						foods.get(entry.getKey()).amount += supplyList.get(entry.getKey());
					}
					mo.isResponded = true;
					stateChanged();
				}
			}
		}
	}

	public void addMarket (MarketRole m) {
		markets.add(m);
		stateChanged();
	}
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		if (onOpen) {
			orderFoodThatIsLow();
			return true;
		}

		if (!orders.isEmpty()){
			synchronized(orders){
				for (MyOrder order : orders) {
					//print ("in cook scheduler. order done: " + order.food);
					if (order.state == OrderState.DONE){
						plateFood (order);
						return true;
					}
				}
			}
		}

		synchronized(orders){
			for (MyOrder order : orders) {
				if (order.state == OrderState.PENDING){
					tryToCookFood (order);
					return true;
				}
			}
		}

		synchronized(marketOrders){
			for (MarketOrder mo : marketOrders) {
				if (mo.isResponded) {
					orderFromAnotherMarket (mo);
					return true;
				}
			}
		}
      
		schedulerTimer.scheduleAtFixedRate(
				new TimerTask(){
					public void run(){
						while (theMonitor.getOrderSize() != 0){
							orders.add (new MyOrder(theMonitor.removeOrder()));
						}									
					} 
				},0,5000);
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	// The correct waiter is notified of the cooked order
	public void plateFood(MyOrder order) {
		print("food for table " + order.tableNumber + " is ready!");
		order.waiter.msgOrderIsReady(order.food, order.tableNumber);
		order.state = OrderState.PLATED;
		cookGui.plateFood(order.food,order.tableNumber);
		orders.remove(order);
	}

	public void tryToCookFood (final MyOrder order) {
		// problem here, the following code is not logically correct
		Food f = foods.get(order.food);
		if (f.amount == 0) {
			order.waiter.msgOutOfFood (order.food, order.tableNumber);
			print("we are running our of " + order.food);
			orders.remove(order);
			return;
		}
		f.amount--;

		order.state = OrderState.COOKING;
		cookGui.cookFood(order.food);
		new java.util.Timer().schedule(
				new java.util.TimerTask(){
					public void run(){
						timerDone(order);
						cookGui.finishCooking();
					}
				},
				f.cookingTime);
		if (f.amount <= f.low) {
			orderFoodThatIsLow ();
		}
	}

	public void orderFoodThatIsLow(){
		onOpen = false;
		Map<String, Integer> marketOrder = Collections.synchronizedMap(new HashMap<String, Integer>());
		synchronized (foods) {
			for (Food f: foods.values()){
				if (f.amount <= f.low && (!f.isOrdered)){
					marketOrder.put(f.type, f.capacity-f.amount);
					f.isOrdered = true;
				}
			}
		}
		//System.out.println(foods.get("Chicken").isOrdered);
		marketOrders.add(new MarketOrder(marketOrder));
		markets.get(0).msgOrder(marketOrder,this, cashier);	
	}


	private void orderFromAnotherMarket (MarketOrder mo) {

		List<String> foodFulfilled = Collections.synchronizedList (new ArrayList<String>());
		synchronized (mo.marketOrder){
			for (Map.Entry<String, Integer> entry : mo.marketOrder.entrySet()) {
				if (entry.getValue() == 0) {
					foods.get(entry.getKey()).isOrdered = false;
					foodFulfilled.add(entry.getKey());
				}
			}	
		}
		for (String temp : foodFulfilled) {
			mo.marketOrder.remove(temp);
		}

		if (!mo.marketOrder.isEmpty()){
			if (mo.marketCount < markets.size()) {
				(markets.get(mo.marketCount)).msgOrder(mo.marketOrder, this, cashier);
				mo.marketCount ++;
				mo.isResponded = false;
				return;
			}
			// we've gone through the market list 
			else {
				//print("Already gone through all markets, request not fulfilled");
				marketOrders.remove(mo);
				return;
			}		
		}



	}
	//utilities

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public void setCashier(Cashier ca) {
		cashier = ca;
	}

	public class MyOrder {
		BaseWaiterRole waiter;
		int tableNumber;
		String food;
		OrderState state;

		public MyOrder (String f, BaseWaiterRole w, int t) {
			food = f;
			waiter = w;
			tableNumber = t;
			state = OrderState.PENDING;
		}
		public MyOrder (MyOrder order) {
			if (order != null) {
				waiter = order.waiter;
				tableNumber = order.tableNumber;
				food = order.food;
				state = OrderState.PENDING;
			}
		}
	}

	private class Food {
		String type;
		int cookingTime;
		int amount;
		int low;
		int capacity; //how much food could be stored at cook's place
		Boolean isOrdered;

		public Food (String t) {
			type = t;
			low = 4;
			capacity = 5;
			isOrdered = false;
			if (type.equals("Steak")) {
				cookingTime = 5000;
				amount = 3;
			}
			if (type.equals("Chicken")) {
				cookingTime = 3000;
				amount = 3;
			}
			if (type.equals("Salad")) {
				cookingTime = 3000;
				amount = 5;
			}
			if (type.equals("Pizza")) {
				cookingTime = 4000;
				amount = 5;
			}
		}
	}

	public void setGui(CookGui _cookGui) {
		cookGui = _cookGui;
	}
	
	public CookGui getGui() {
		return cookGui;
	}

}

