package market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import people.People;
import people.Role;
import market.gui.MarketEmployeeGui;
import market.gui.MarketGui;
import market.interfaces.MarketCashier;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketEmployee;
import market.interfaces.MarketTruck;
import market.test.MockPeople;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;

public class MarketEmployeeRole extends Role implements MarketEmployee{
	// data
	public EventLog log = new EventLog();
	public boolean inTest = false;
	private boolean turnActive = false;
	private boolean setClose = false;
	private List<People> workers = Collections.synchronizedList(new ArrayList<People>());
	private int marketNumber = 0;

	public int restaurantOrderNumber;

	MarketCashier cashier;
	MarketEmployeeGui employeeGui;
	MarketGui marketGui;

	List<MarketTruck> trucks = new ArrayList<MarketTruck>();
	int marketTruckCount = -1;

	public Map <String, Item> items = new HashMap<String, Item>();
	public class Item {
		String name;
		public int inventory;

		public Item (String n, int i) {
			name = n;
			inventory = i;
		}
	}


	enum orderState {NONE, PENDING, TO_BE_REDELIVERED, IN_DELIVERY};

	public List<Order> orders = new ArrayList<Order>();

	public class Order {
		Map<String, Integer> itemsOrdered = new HashMap<String, Integer>();
		Map<String, Integer> supply = new HashMap<String, Integer>();
		MarketCustomer customer = null;
		Cook cook = null;
		Cashier restaurantCashier = null;
		int orderNumber;
		orderState state = orderState.PENDING;

		public Order (MarketCustomer cust, Map<String,Integer> itemsNeeded) {
			customer = cust;
			itemsOrdered = itemsNeeded;
			orderNumber = restaurantOrderNumber;
			restaurantOrderNumber++;
		}

		public Order(Cook _cook, Cashier _cashier, Map<String, Integer> order) {
			itemsOrdered = order;
			cook = _cook;
			restaurantCashier = _cashier;
			orderNumber = restaurantOrderNumber;
			restaurantOrderNumber++;
		}

		public String getState() {
			return state.toString();
		}

		public int getOrderNumber() {
			return orderNumber;
		}
	}

	private boolean leaveWork = false;

	Semaphore atCabinet = new Semaphore(0,true);
	Semaphore atCounter = new Semaphore(0,true);
	Semaphore atExit = new Semaphore(0,true);

	// constructor
	public MarketEmployeeRole(MarketGui gui){
		restaurantOrderNumber = 1;
		marketGui = gui;
		employeeGui = new MarketEmployeeGui(this);
		marketGui.getAnimationPanel().addGui(employeeGui);
		employeeGui.setPresent(false);

		if (!inTest) {
			for (int i=0; i<10; i++) { // create 10 market trucks
				trucks.add(new MarketTruckAgent("MarketTruck "+i, this));
				((MarketTruckAgent) trucks.get(i)).startThread();
			}
		}
		items.put("Steak", new Item("Steak", 10000));
		items.put("Salad", new Item("Salad", 10000));
		items.put("Pizza", new Item("Pizza", 10000));
		items.put("Chicken", new Item("Chicken", 10000));
		items.put("Car", new Item("Car", 10000));

	}


	// messages

	public void msgIsActive() {
		log.add(new LoggedEvent("received msgActive"));
		if(!workers.contains(this.getPersonAgent())) workers.add(this.getPersonAgent());
		if (!inTest) {
			getPersonAgent().getMarket(0).isClosed = false;
		}
		isActive = true;
		turnActive = true;
		employeeGui.setPresent(true);
		getPersonAgent().CallstateChanged();
	}

	public void msgIsInActive() {
		log.add(new LoggedEvent("received msgIsInActive"));
		leaveWork = true;
		getPersonAgent().CallstateChanged();
	}

	public void msgSetClose() {
		setClose = true;
		getPersonAgent().CallstateChanged();
	}

	public void msgAtCabinet() {
		atCabinet.release();
		getPersonAgent().CallstateChanged();
	}

	public void msgAtCounter() {
		atCounter.release();
		getPersonAgent().CallstateChanged();
	}

	public void msgAtExit() {
		atExit.release();
		getPersonAgent().CallstateChanged();
	}

	// order from regular market customer
	public void msgHereIsAnOrder(MarketCustomer customer, Map<String, Integer> chosenItems) {
		print("received order from market customer");
		if (!inTest) {
			log.add(new LoggedEvent("received an order from market customer " + customer.getPerson().getName()));
		}
		orders.add(new Order(customer, chosenItems));
		getPersonAgent().CallstateChanged();
	}

	// order from restaurant cook
	public void msgHereIsAnOrder(Map<String, Integer> chosenItems, Cook cook, Cashier cashier) {
		print("received order from restaurant cook " + cook.getName());
		if(!inTest){
			log.add(new LoggedEvent("received an order from restaurant cook " + cook.getName()));
		}
		orders.add(new Order(cook, cashier, chosenItems));
		getPersonAgent().CallstateChanged();

	}

	// from truck
	public void msgOrderDelivered(int orderNum) {
		for (Order o : orders) {
			if (o.orderNumber == orderNum) {
				orders.remove(o);
				break;
			}
		}
		getPersonAgent().CallstateChanged();
	}

	public void msgOrderNotDelivered(int orderNumber) {
		for (Order o : orders) {
			if (o.orderNumber == orderNumber) {
				o.state = orderState.TO_BE_REDELIVERED;
				break;
			}
			getPersonAgent().CallstateChanged();
		}
	}


	// scheduler
	public boolean pickAndExecuteAnAction() {
		if (turnActive) {
			redeliverRestaurantOrder();
			return true;
		}

		if (setClose) {
			closeMarket();
			return true;
		}

		if (!orders.isEmpty()) {
			for (Order o : orders) {
				if (o.state ==orderState.PENDING) {
					giveOrderToCustomer(o);
					return true;
				}
			}


		}

		if (leaveWork) {
			done();
			return true;
		}

		return false;
	}


	// action
	private void getOrder(Map<String, Integer> itemList) { //gui
		print ("getting items");
		print ("there are " + itemList.size() + " items to get");
		for (Map.Entry<String,Integer> entry : itemList.entrySet()) {
			employeeGui.doGetItem(entry.getKey());
			try {
				atCabinet.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			items.get(entry.getKey()).inventory -= entry.getValue();
		}
		employeeGui.doGoToCounter();
		try {
			atCounter.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	// if customer is at the Market, give it the items; otherwise send a truck to its place
	private void giveOrderToCustomer(Order order) {
		
		Map<String,Integer> supply = new HashMap<String, Integer>(); 
		for (Map.Entry<String, Integer> entry: order.itemsOrdered.entrySet()) {
			if (items.get(entry.getKey()).inventory >= entry.getValue()) {
				supply.put(entry.getKey(), entry.getValue());
				items.get(entry.getKey()).inventory -= entry.getValue();
			}
			else {
				supply.put(entry.getKey(), items.get(entry.getKey()).inventory);
				items.get(entry.getKey()).inventory = 0;
			}
		}
		//gui show text
		if (order.cook!=null) {
		employeeGui.showGotOrderFromRestaurant(order.cook);
		}
		
		order.supply = supply;
		if(!inTest)		getOrder(supply);

		employeeGui.noCommand();
		
		// if order is from restaurant
		if (order.cook != null) {
			print ("sending confirmation to cook " + order.cook.getName());
			log.add(new LoggedEvent("sending confirmation to cook, check to market cashier, and order to truck"));
			order.cook.msgHereIsYourOrderNumber(order.itemsOrdered, order.orderNumber, marketNumber);
			cashier.msgHereIsACheck(order.restaurantCashier, supply, order.orderNumber, marketNumber);
			if (!inTest) {
				if (!getPersonAgent().getRestaurant(order.cook.getRestaurantIndex()).isClosed) {
					//do some gui stuff
					getNextMarketTruck().msgHereIsAnOrder(order.cook, supply, order.orderNumber, marketNumber);
					order.state = orderState.IN_DELIVERY;
				}
				else {//if restaurant is already closed
					order.state = orderState.TO_BE_REDELIVERED;
				}
			}
			else {
				if (!((MockPeople)getPersonAgent()).getMyRestaurant(order.cook.getRestaurantIndex()).isClosed) {
					//do some gui stuff
					getNextMarketTruck().msgHereIsAnOrder(order.cook, supply, order.orderNumber, marketNumber);
					order.state = orderState.IN_DELIVERY;
				}
				else {//if restaurant is already closed
					order.state = orderState.TO_BE_REDELIVERED;
				}
			}
		}

		//if order is from customer
		else {
			log.add(new LoggedEvent("giving items to customer"));
			print ("giving items to customer");
			order.customer.msgHereIsYourOrder(order.supply);
			cashier.msgHereIsACheck(order.customer, order.itemsOrdered);
			orders.remove(order);
		}
	}



	private MarketTruck getNextMarketTruck() {
		if (marketTruckCount == trucks.size()-1) {
			marketTruckCount = 0;
			return trucks.get(0);
		}
		else {
			marketTruckCount ++;
			return trucks.get(marketTruckCount);
		}
	}

	private void redeliverRestaurantOrder() {
		log.add(new LoggedEvent("check order list to see if any redelivery is needed"));
		if (!orders.isEmpty()) {
			for (Order o : orders) {
				if (!inTest){
					if (o.state == orderState.TO_BE_REDELIVERED && !getPersonAgent().getRestaurant(o.cook.getRestaurantIndex()).isClosed) {
						getNextMarketTruck().msgHereIsAnOrder(o.cook, o.supply, o.orderNumber, marketNumber);
						o.state = orderState.IN_DELIVERY;
					}
				}
				else{
					if (o.state == orderState.TO_BE_REDELIVERED && !((MockPeople)getPersonAgent()).getMyRestaurant(o.cook.getRestaurantIndex()).isClosed) {
						getNextMarketTruck().msgHereIsAnOrder(o.cook, o.supply, o.orderNumber, marketNumber);
						o.state = orderState.IN_DELIVERY;
					}
				}
			}
		}
		turnActive = false;
	}

	private void done() {
		employeeGui.doExit();
		try {
			atExit.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		workers = Collections.synchronizedList(new ArrayList<People>());
		isActive = false;
		leaveWork = false;
		employeeGui.setPresent(false);
		employeeGui.setDefaultDestination();
		getPersonAgent().msgDone("MarketEmployeeRole");
	}

	private void closeMarket() {
		setClose = false;
		getPersonAgent().getMarket(0).isClosed = true;
	}
	//utilities
	public Boolean isActive() {
		return isActive;
	}

	public void setCashier(MarketCashier c) {
		cashier = c;
		if (!inTest){
			getPersonAgent().CallstateChanged();
			if(!workers.contains(((MarketCashierRole)c).getPersonAgent())) {
				workers.add(((MarketCashierRole)c).getPersonAgent());
			}
		}
	}

	public MarketCashier getCashier() {
		return cashier;
	}

	public String getName() {
		return getPersonAgent().getName();
	}

	public List<MarketTruck> getTrucks() {
		return trucks;
	}

	public void addTruck (MarketTruck truck) {
		trucks.add(truck);
	}
	
	public List<People> getWorkers () {
		return workers;
	}

}
