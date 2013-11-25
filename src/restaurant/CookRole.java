package restaurant;

import agent.Agent;
import restaurant.BaseWaiterRole;
import restaurant.gui.CookGui;
import restaurant.gui.HostGui;
import restaurant.gui.RestaurantPanel.CookWaiterMonitor;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Host;
import restaurant.interfaces.Waiter;

import java.awt.Dimension;
import java.util.*;
import java.util.concurrent.Semaphore;

import market.MarketEmployeeRole;
import market.interfaces.MarketEmployee;
import people.People;
import people.PeopleAgent;
import people.Role;

/**
 * Restaurant Cook Agent
 */
// There is only one CookAgent in this version of Restaurant. Waiter(s) will take customers' order 
// and then go to the cook and tell him to prepare the food. The CookAgent will then cook the food (
// cooking time depends on food) and plate it, then notify the correct waiter that food is done.
// Cook may run out of food and order that from markets (three for now)

//Cook can run out of food, and when that happens, he orders food from the market. He only orders food 
//has not been ordered for now.


public class CookRole extends Role implements Cook{

	private List<MyOrder> orders = Collections.synchronizedList(new ArrayList<MyOrder>());
	//private List<MarketEmployeeRole> marketEmployees = Collections.synchronizedList(new ArrayList<MarketEmployeeRole>());
	public enum OrderState {PENDING, COOKING, DONE, PLATED};
	private CookWaiterMonitor theMonitor = null;

	private Map<String, Food> foods = Collections.synchronizedMap(new HashMap<String, Food>());			
	private Timer schedulerTimer = new Timer();
	protected Semaphore atRevolvingStand = new Semaphore (0,true);
	private String name;
	private CookGui cookGui = null;

	private Boolean isActive = false;
	private Boolean turnActive = false;
	private Boolean leaveWork = false;

	private Host host;
	private Cashier cashier;
	private MarketEmployee marketEmployee;

	//private MarketEmployeeRole marketEmployee = null;


	private List<MarketOrder> marketOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
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


	/**
	 * Constructor for CookAgent class
	 *
	 * @param name name of the cook
	 */
	public CookRole(CookWaiterMonitor monitor, String name) {
		super();
		this.name = name;
		foods.put("Steak", new Food("Steak"));
		foods.put("Chicken", new Food("Chicken"));
		foods.put("Salad", new Food("Salad"));
		foods.put("Pizza", new Food("Pizza"));
		theMonitor = monitor;
	}


	// Messages

	// Cook receives an order from the waiter and stores it into a list
	public void msgIsActive() {
		isActive = true;
		turnActive = true;
		getPersonAgent().CallstateChanged();
	}

	public void msgIsInActive() {
		leaveWork = true;
		getPersonAgent().CallstateChanged();
	}
	
	public void msgAtRevolvingStand() {
		atRevolvingStand.release();
		myPerson.CallstateChanged();
		
	}

	public void msgHereIsAnOrder (String food, Waiter w,int tableNum) {
		orders.add( new MyOrder(food, w, tableNum));
		print("Receiving order, " + food + " for table #"+tableNum);
		getPersonAgent().CallstateChanged();
	}

	// Food order is cooked, managed by timer
	public void timerDone(MyOrder order)
	{
		order.state = OrderState.DONE;
		getPersonAgent().CallstateChanged();
	}	


	public void msgHereIsYourOrder(Map<String, Integer> items) {
		for (Map.Entry<String, Integer> entry : items.entrySet()) {
			foods.get(entry.getKey()).amount += entry.getValue();
		}
		for (MarketOrder mo : marketOrders) {
			if (mo.marketOrder == items) {
				mo.isResponded = true;
			}
		}
		getPersonAgent().CallstateChanged();

	}

	/*public void msgSupply (Map<String,Integer> orderList, Map<String,Integer> supplyList) {
		synchronized (marketOrders) {

			for (MarketOrder mo : marketOrders) {
				if (mo.marketOrder == orderList) {
					for (Map.Entry<String, Integer> entry : mo.marketOrder.entrySet()) {
						mo.marketOrder.put(entry.getKey(), (entry.getValue()-supplyList.get(entry.getKey())));
						foods.get(entry.getKey()).amount += supplyList.get(entry.getKey());
					}
					mo.isResponded = true;
					getPersonAgent().CallstateChanged();
				}
			}
		}
	}*/

	/*public void addMarket (MarketEmployeeRole m) {
		markets.add(m);
		getPersonAgent().CallstateChanged();
	}*/
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		//if (onOpen) {
		//	orderFoodThatIsLow();
		//	return true;
		//}
		
		if (turnActive) {
			clockIn();
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

		/*synchronized(marketOrders){
			for (MarketOrder mo : marketOrders) {
				if (mo.isResponded) {
					orderFromAnotherMarket (mo);
					return true;
				}
			}
		}*/
		
		synchronized(marketOrders) {
			for (MarketOrder mo:marketOrders) {
				if (mo.isResponded) {
					askCashierToPayForOrder(mo);
					return true;
				}
			}
		}


		schedulerTimer.scheduleAtFixedRate(
				new TimerTask(){
					public void run(){
						while (theMonitor.getOrderSize() != 0){
							getOrderFromRevolvingStand();
							orders.add (new MyOrder(theMonitor.removeOrder()));
						}									
					} 
				},0,5000);

		if (leaveWork) {
			done();
			return true;
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	public void askCashierToPayForOrder(MarketOrder order) {
		cashier.msgMarketOrderReceived(order.marketOrder);
		marketOrders.remove(order);
	}
	
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
		Map<String, Integer> marketOrder = Collections.synchronizedMap(new HashMap<String, Integer>());
		synchronized (foods) {
			for (Food f: foods.values()){
				if (f.amount <= f.low /*&& (!f.isOrdered)*/){
					marketOrder.put(f.type, f.capacity-f.amount);
					//f.isOrdered = true;
				}
			}
		}
		//System.out.println(foods.get("Chicken").isOrdered);
		marketOrders.add(new MarketOrder(marketOrder));
		marketEmployee.msgOrder(marketOrder,this, cashier);	
	}
	
	
	public void getOrderFromRevolvingStand() {
		cookGui.DoGoToRevolvingStand();
		try {
			atRevolvingStand.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/*private void orderFromAnotherMarket (MarketOrder mo) {

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


	}*/

	private void clockIn() {
		host = myPerson.Restaurants.get(0).h;
		host.setCook(this);
		marketEmployee = (MarketEmployee) getPersonAgent().getMarketEmployee();
		cashier = host.getCashier();
		turnActive = false;
	}

	public void done() {
		getPersonAgent().msgDone("RestaurantCookRole");
	}
	//utilities

	public String getMaitreDName() {
		return getPersonAgent().getName();
	}

	public String getName() {
		return getPersonAgent().getName();
	}

	public void setCashier(Cashier ca) {
		cashier = ca;
	}

	public class MyOrder {
		Waiter waiter;
		int tableNumber;
		String food;
		OrderState state;

		public MyOrder (String f, Waiter w, int t) {
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

	public void setHost(HostRole h) {
		host = h;
	}

	public boolean isActive() {
		return isActive;
	}


	@Override
	public People getPerson() {
		return getPersonAgent();
	}


}

