package market;

import java.awt.Dimension;
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
import restaurant.CashierRole;
import restaurant.CookRole;

public class MarketEmployeeRole extends Role implements MarketEmployee{
	// data
	Map <String, Item> items = new HashMap<String, Item>();
	MarketCashier cashier;
	MarketEmployeeGui gui;
		
	class Item {
		//Dimension location;
		String name;
		int inventory;
		
		public Item (Dimension loc, String n, int i) {
			//location = loc;
			name = n;
			inventory = i;
		}
	}
	
	List<MarketTruck> trucks = new ArrayList<MarketTruck>();
	int marketTruckCount = 0;
	
	List<Order> orders = new ArrayList<Order>();

	class Order {
		Map<String, Integer> items = new HashMap<String, Integer>();
		MarketCustomer customer = null;
		CookRole cook = null;
		CashierRole restaurantCashier = null;
		//boolean customerAtMarket;
		//boolean customerIsRestaurantCashier;
		
		public Order (MarketCustomer cust, Map<String,Integer> itemsNeeded) {
			customer = cust;
			items = itemsNeeded;
		}
	}

	
	private Boolean isActive;

	// constructor
	public MarketEmployeeRole(){}
	
	
	// messages
	public void msgHereIsAnOrder(MarketCustomer customer, Map<String, Integer> chosenItems) {
		 orders.add(new Order(customer, chosenItems));
		getPersonAgent().CallstateChanged();

	}

	// scheduler
	public boolean pickAndExecuteAnAction() {
		if (!orders.isEmpty()) {
			giveOrderToCustomer(orders.get(0));
			return true;
		}
		return false;
	}


	// action
	private void getOrder(Map<String, Integer> itemList) { //gui
		for (Map.Entry<String,Integer> entry : itemList.entrySet()) {
			gui.doGetItem(entry.getKey(), entry.getValue());
			items.get(entry.getKey()).inventory -= entry.getValue();
		}
	}

	
	// if customer is at the Market, give it the items; otherwise send a truck to its place
	private void giveOrderToCustomer(Order order) {
		getOrder(order.items);
		//if customer is in market, give it the order
		if (getPersonAgent().getGui().getLocation().equals("market")) { // how to check if customer is in the market...
			gui.doWalkToCustomer(order.customer);
			order.customer.msgHereIsYourOrder(order.items);
		}
		
		//if customer is at somewhere else, send a truck to the place
		else {
			getNextMarketTruck().msgHereIsAnOrder(order.customer,order.items); 
			
		}
		
		cashier.msgHereIsACheck(order.customer, order.items);
		orders.remove(order);
	}



	private MarketTruck getNextMarketTruck() {
		MarketTruck temp;
		if (marketTruckCount == trucks.size()-1) {
			marketTruckCount = 0;
			return trucks.get(0);
		}
		else {
			marketTruckCount ++;
			return trucks.get(marketTruckCount);
		}
	}


	//utilities
	public Boolean isActive() {
		return isActive;
	}

	public void setCashier(MarketCashierRole c) {
		cashier = c;
	}
	
}
