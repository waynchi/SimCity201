package market;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import agent.Agent;
import people.Role;
import market.gui.MarketEmployeeGui;
import market.gui.MarketGui;
import market.interfaces.MarketCashier;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketEmployee;
import market.interfaces.MarketTruck;
import restaurant.CookRole;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;

public class MarketEmployeeRole extends Role implements MarketEmployee{
	// data
	public EventLog log = new EventLog();
	public boolean inTest = true;
	private boolean turnActive = false;
	private boolean setClose = false;
	
	public int restaurantOrderNumber = 1;
	
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

	class Order {
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
	}

	private boolean leaveWork = false;
	
	Semaphore atCabinet = new Semaphore(0,true);
	Semaphore atCounter = new Semaphore(0,true);
	Semaphore atExit = new Semaphore(0,true);

	// constructor
	public MarketEmployeeRole(MarketGui gui){
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
		items.put("Steak", new Item("Steak", 1000000));
		items.put("Salad", new Item("Salad", 1000000));
		items.put("Pizza", new Item("Pizza", 1000000));
		items.put("Chicken", new Item("Chicken", 1000000));
		items.put("Car", new Item("Car", 1000000));

	}


	// messages

	public void msgIsActive() {
		log.add(new LoggedEvent("received msgActive"));
		getPersonAgent().getMarket(0).isClosed = false;
		isActive = true;
		turnActive = true;
		inTest = false;
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
		log.add(new LoggedEvent("received an order from market customer " + customer.getPerson().getName()));
		orders.add(new Order(customer, chosenItems));
		getPersonAgent().CallstateChanged();
	}

	// order from restaurant cook
	public void msgOrder(Map<String, Integer> chosenItems, Cook cook, Cashier cashier) {
		if(!inTest){
			log.add(new LoggedEvent("received an order from restaurant cook " + ((CookRole) cook).getPerson().getName()));
		}
		orders.add(new Order (cook, cashier, chosenItems));
		getPersonAgent().CallstateChanged();

	}
	
	// from truck
	public void msgOrderDelivered(int orderNumber) {
		for (Order o : orders) {
			if (o.orderNumber == orderNumber)
				orders.remove(o);
				break;
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
		order.supply = supply;
			
		if(!inTest)		getOrder(supply);
		
		// if order is from restaurant
		if (order.cook != null) {
			log.add(new LoggedEvent("sending confirmation to cook, check to market cashier, and order to truck"));
			order.cook.msgHereIsYourOrderNumber(order.itemsOrdered, order.orderNumber);
			cashier.msgHereIsACheck(order.restaurantCashier, supply, order.orderNumber);
			if (!getPersonAgent().getRestaurant(order.cook.getRestaurantIndex()).isClosed) {
				//getNextMarketTruck().msgHereIsAnOrder(order.cook, supply, order.orderNumber);
				order.cook.msgHereIsYourOrder(order.supply, order.orderNumber);	
				order.state = orderState.IN_DELIVERY;
			}
			//order.cook.msgHereIsYourOrder(supply, order.orderNumber);	
		}


		//if order is from customer
		else {
			log.add(new LoggedEvent("giving items to customer " + order.customer.getPerson().getName()));
				order.customer.msgHereIsYourOrder(order.itemsOrdered);
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
		if (!orders.isEmpty()) {
			for (Order o : orders) {
				if (o.state == orderState.TO_BE_REDELIVERED && !getPersonAgent().getRestaurant(o.cook.getRestaurantIndex()).isClosed) {
					getNextMarketTruck().msgHereIsAnOrder(o.cook, o.supply, o.orderNumber);
					o.state = orderState.IN_DELIVERY;
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
		isActive = false;
		leaveWork = false;
		employeeGui.setPresent(false);
		employeeGui.setDefaultDestination();
		getPersonAgent().msgDone("MarketEmployeeRole");
	}

	private void closeMarket() {
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
	
}
