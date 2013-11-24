package market;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import people.Role;
import market.gui.MarketEmployeeGui;
import market.interfaces.MarketCashier;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketEmployee;
import market.interfaces.MarketTruck;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;

public class MarketEmployeeRole extends Role implements MarketEmployee{
	// data
	MarketCashier cashier;
	MarketEmployeeGui gui;
	List<MarketTruck> trucks = new ArrayList<MarketTruck>();
	int marketTruckCount = 0;

	Map <String, Item> items = new HashMap<String, Item>();
	class Item {
		String name;
		int inventory;

		public Item (String n, int i) {
			name = n;
			inventory = i;
		}
	}

	List<Order> orders = new ArrayList<Order>();

	class Order {
		Map<String, Integer> items = new HashMap<String, Integer>();
		MarketCustomer customer = null;
		Cook cook = null;
		Cashier restaurantCashier = null;
		//boolean customerAtMarket;
		//boolean customerIsRestaurantCashier;

		public Order (MarketCustomer cust, Map<String,Integer> itemsNeeded) {
			customer = cust;
			items = itemsNeeded;
		}

		public Order(Cook _cook, Cashier _cashier, Map<String, Integer> order) {
			items = order;
			cook = _cook;
			restaurantCashier = _cashier;
		}
	}


	private boolean isActive = false;
	private boolean leaveWork = false;

	// constructor
	public MarketEmployeeRole(){
		for (int i=0; i<10; i++) { // create 10 market trucks
			trucks.add(new MarketTruckAgent("MarketTruck "+i));
		}
		items.put("Steak", new Item("Steak", 100));
		items.put("Salad", new Item("Salad", 100));
		items.put("Pizza", new Item("Pizza", 100));
		items.put("Chicken", new Item("Chicken", 100));
		items.put("Car", new Item("Car", 100));

	}


	// messages

	public void msgIsActive() {
		isActive = true;
		getPersonAgent().CallstateChanged();
	}

	public void msgIsInActive() {
		leaveWork = true;
		getPersonAgent().CallstateChanged();
	}

	// order from regular market customer
	public void msgHereIsAnOrder(MarketCustomer customer, Map<String, Integer> chosenItems) {
		orders.add(new Order(customer, chosenItems));
		getPersonAgent().CallstateChanged();

	}

	// order from restaurant cook
	public void msgOrder(Map<String, Integer> order, Cook cook, Cashier cashier) {
		orders.add(new Order (cook, cashier, order));
		getPersonAgent().CallstateChanged();

	}

	// scheduler
	public boolean pickAndExecuteAnAction() {
		if (!orders.isEmpty()) {
			giveOrderToCustomer(orders.get(0));
			return true;
		}
		
		if (leaveWork) {
			done();
		}
		
		return false;
	}


	// action
	private void getOrder(Map<String, Integer> itemList) { //gui
		for (Map.Entry<String,Integer> entry : itemList.entrySet()) {
			gui.doGetItem(entry.getKey());
			items.get(entry.getKey()).inventory -= entry.getValue();
		}
	}


	// if customer is at the Market, give it the items; otherwise send a truck to its place
	private void giveOrderToCustomer(Order order) {
		getOrder(order.items);
		// if order is from restaurant
		if (order.cook != null) {
			getNextMarketTruck().msgHereIsAnOrder(order.cook, order.items);
			cashier.msgHereIsACheck(order.restaurantCashier, order.items);
		}


		//if customer is in market, give it the order
		else {
			//if (order.customer.getPerson().getGui().getLocation().equals("market")) { // how to check if customer is in the market...
				gui.doWalkToCustomer(order.customer);
				order.customer.msgHereIsYourOrder(order.items);
			//}

			//if customer is at somewhere else, send a truck to the place
			//else {
			//	getNextMarketTruck().msgHereIsAnOrder(order.customer,order.items); 

			//}
			cashier.msgHereIsACheck(order.customer, order.items);
		}

		orders.remove(order);
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

	private void done() {
		isActive = false;
		leaveWork = false;
		getPersonAgent().msgDone("MarketEmployee");
	}

	//utilities
	public Boolean isActive() {
		return isActive;
	}

	public void setCashier(MarketCashier c) {
		cashier = c;
		getPersonAgent().CallstateChanged();
	}
	
	public MarketCashier getCashier(MarketCashier c) {
		return cashier;
	}
	
	public String getName() {
		return getPersonAgent().getName();
	}



}
