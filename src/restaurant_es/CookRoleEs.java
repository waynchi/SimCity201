package restaurant_es;

import restaurant_es.gui.CookGui;
import restaurant_es.gui.RestaurantGuiEs;
import restaurant_es.gui.RestaurantPanelEs.CookWaiterMonitorEs;
import restaurant_es.gui.RestaurantPanelEs.Order;
import restaurant.CookRole.MarketOrder;
import restaurant.CookRole.MyOrder;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import restaurant_es.interfaces.Host;
import restaurant_es.interfaces.Waiter;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;

import java.util.*;
import java.util.concurrent.Semaphore;

import market.interfaces.MarketEmployee;
import people.People;
import people.PeopleAgent;
import people.Role;

/**
 * Restaurant Cook Agent
 */

public class CookRoleEs extends Role implements Cook{
	public EventLog log = new EventLog();
	private List<MyOrder> orders = Collections.synchronizedList(new ArrayList<MyOrder>());
	public enum OrderState {PENDING, COOKING, DONE, PLATED};
	private CookWaiterMonitorEs theMonitor = null;
	private Map<String, Food> foods = Collections.synchronizedMap(new HashMap<String, Food>());			
	private Timer schedulerTimer;
	protected Semaphore atRevolvingStand = new Semaphore (0,true);
	protected Semaphore atGrill= new Semaphore (0,true);
	protected Semaphore atExit= new Semaphore (0,true);
	private CookGui cookGui = null;
	private RestaurantGuiEs restGui = null;
	public int restaurantIndex = 4;
	private Boolean turnActive = false;
	private Boolean leaveWork = false;
	private Host host;
	private Cashier cashier;
	private MarketEmployee marketEmployee;
	private final int period = 700;
	private List<MarketOrder> marketOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
	
	/**
	 * Constructor for CookAgent class
	 *
	 * @param name name of the cook
	 */
	public CookRoleEs(CookWaiterMonitorEs monitor, RestaurantGuiEs gui) {
		this.restGui = gui;
		cookGui = new CookGui(this);
		restGui.getAnimationPanel().addGui(cookGui);
		cookGui.setPresent(false);
		foods.put("Steak", new Food("Steak"));
		foods.put("Chicken", new Food("Chicken"));
		foods.put("Salad", new Food("Salad"));
		foods.put("Pizza", new Food("Pizza"));
		theMonitor = monitor;
	}


	// Messages

	// Cook receives an order from the waiter and stores it into a list
	public void msgIsActive() {
		print("People agent sent msgIsActive");
		isActive = true;
		turnActive = true;
		getPersonAgent().CallstateChanged();
	}

	public void msgIsInActive() {
		print("msgInActive");
		leaveWork = true;
		getPersonAgent().CallstateChanged();
	}
	
	public void msgAtRevolvingStand() {
		atRevolvingStand.release();
		myPerson.CallstateChanged();
	}
	
	public void msgAtExit() {
		atExit.release();;
		getPersonAgent().CallstateChanged();
	}
	

	public void msgAtGrill() {
		atGrill.release();
		myPerson.CallstateChanged();
	}

	public void msgHereIsAnOrder (String food, Waiter w,int tableNum) {
		print("Received new order");
		orders.add( new MyOrder(food, w, tableNum));
		getPersonAgent().CallstateChanged();
	}

	// Food order is cooked, managed by timer
	public void timerDone(MyOrder order)
	{
		order.state = OrderState.DONE;
		getPersonAgent().CallstateChanged();
	}	
	
	public void startStandTimer() {
		schedulerTimer = new Timer();
		schedulerTimer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				checkStand();
			}
		}, period, period);
	}
	
	
	public void checkStand() {
		if (theMonitor.getOrderSize() != 0) {
			synchronized(orders) {
				getOrderFromRevolvingStand();			}
			getPersonAgent().CallstateChanged();
		}
	}

	public void msgHereIsYourOrder(Map<String, Integer> items, int orderNumber, int marketNumber) {
		print("Received items from market");
		for (Map.Entry<String, Integer> entry : items.entrySet()) {
			foods.get(entry.getKey()).amount += entry.getValue();
			foods.get(entry.getKey()).isOrdered = false;
		}
		for (MarketOrder mo : marketOrders) {
			if (mo.orderNumber == orderNumber && mo.marketNumber == marketNumber) {
				mo.delivered = true;
			}
		}
		getPersonAgent().CallstateChanged();
	}


	public void msgHereIsYourOrderNumber(Map<String, Integer> items, int orderNumber, int market) {
		print("Market has recieved our order");
		for (MarketOrder mo : marketOrders) {
			if (mo.marketOrder == items && mo.marketNumber == market) {
				mo.orderNumber = orderNumber;
			}
		}
		getPersonAgent().CallstateChanged();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		if (turnActive) {
			clockIn();
			return true;
		}

		if (!orders.isEmpty()){
			synchronized(orders){
				for (MyOrder order : orders) {
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
		
		synchronized(marketOrders) {
			for (MarketOrder mo:marketOrders) {
				if (mo.delivered) {
					askCashierToPayForOrder(mo);
					return true;
				}
			}
		}

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
		cashier = host.getCashier();
		cashier.msgGotMarketOrder(order.marketOrder, order.orderNumber, order.marketNumber);
		marketOrders.remove(order);
	}
	
	public void plateFood(MyOrder order) {
		order.waiter.msgOrderIsReady(order.food, order.tableNumber);
		order.state = OrderState.PLATED;
		cookGui.plateFood(order.food,order.tableNumber);
		orders.remove(order);
	}

	public void tryToCookFood (final MyOrder order) {
		Food f = foods.get(order.food);
		if (f.amount == 0) {
			order.waiter.msgOutOfFood (order.food, order.tableNumber);
			orders.remove(order);
			return;
		}
		f.amount--;

		order.state = OrderState.COOKING;
		cookGui.DoGoToCookingPlace();
		try {
			atGrill.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
				if (f.amount <= f.low && (!f.isOrdered)){
					marketOrder.put(f.type, f.capacity-f.amount);
					f.isOrdered = true;
				}
			}
		}
		if (marketOrder.size()!=0) {
			int marketSize = ((PeopleAgent)getPersonAgent()).Markets.size();
			int marketNumber = (int)(Math.random() * marketSize);
			marketOrders.add(new MarketOrder(marketOrder,marketNumber));
			((MarketEmployee)getPersonAgent().getMarketEmployee(marketNumber)).msgHereIsAnOrder(marketOrder,this, cashier);	
			}
		}
	
	
	public void getOrderFromRevolvingStand() {
		orders.add (new MyOrder(theMonitor.removeOrder()));
		cookGui.DoGoToRevolvingStand();
		try {
			atRevolvingStand.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		cookGui.DoGoToCookingPlace();
		try {
			atGrill.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getPersonAgent().CallstateChanged();
	}

	private void clockIn() {
		cookGui.setPresent(true);
		cookGui.DoGoToCookingPlace();
		try {
			atGrill.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		host = (Host) getPersonAgent().getHost(4);
		host.setCook(this);
		startStandTimer();
		cashier = host.getCashier(); 
		turnActive = false;
		orderFoodThatIsLow();
	}

	public void done() {
		cookGui.DoLeaveWork();
		try {
			atExit.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		schedulerTimer = null;
		isActive = false;
		leaveWork = false;
		cookGui.setPresent(false);
		cookGui.setDefaultDestination();
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
		
		public MyOrder(Order order) {
			waiter = order.waiter;
			tableNumber = order.table;
			food= order.food;
			state = OrderState.PENDING;
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
			low = 3;
			capacity = 5;
			isOrdered = false;
			if (type.equals("Steak")) {
				cookingTime = 1000;
				amount = 100;
			}
			if (type.equals("Chicken")) {
				cookingTime = 1000;
				amount = 100;
			}
			if (type.equals("Salad")) {
				cookingTime = 1000;
				amount = 100;
			}
			if (type.equals("Pizza")) {
				cookingTime = 1000;
				amount = 100;
			}
		}
	}

	public void setGui(CookGui _cookGui) {
		cookGui = _cookGui;
	}

	public CookGui getGui() {
		return cookGui;
	}

	public void setHost(HostRoleEs h) {
		host = h;
	}

	public boolean isActive() {
		return isActive;
	}

	public People getPerson() {
		return getPersonAgent();
	}


	@Override
	public int getRestaurantIndex() {
		// TODO Auto-generated method stub
		return restaurantIndex;
	}


	@Override
	public void setLow() {
		for (Map.Entry<String, Food> entry : foods.entrySet()) {
			foods.get(entry.getKey()).amount = 2;
		}
		orderFoodThatIsLow();
		getPersonAgent().CallstateChanged();
	}
	
	public class MarketOrder {
		private Map<String, Integer> marketOrder = Collections.synchronizedMap(new HashMap<String, Integer>());
		Boolean delivered;
		int orderNumber = -1;
		int marketNumber = -1;

		private MarketOrder (Map<String,Integer> mo, int market){
			marketOrder = mo;
			delivered = false;
			marketNumber = market;
		};
	}

	
}

