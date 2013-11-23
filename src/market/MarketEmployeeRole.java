package market;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import restaurant.CashierRole;
import restaurant.CookRole;

public class MarketEmployeeRole {
	// data
	Map <String, Item> items = new HashMap<String, Item>();
	MarketCashierRole cashier;
		
	class Item {
		int ID;
		Dimension location;
		String name;
		
		public Item (int id, Dimension loc, String n) {
			ID = id;
			location = loc;
			name = n;
		}
	}
	
	List<MarketTruckAgent> trucks;
	List<Order> orders;

	class Order {
		Map<String, Integer> items = new HashMap<String, Integer>();
		MarketCustomerRole customer = null;
		CookRole cook = null;
		CashierRole restaurantCashier = null;
		//boolean customerAtMarket;
		//boolean customerIsRestaurantCashier;
	}

	
	private Boolean isActive;

	// constructor
	public MarketEmployeeRole(){}
	
	
	// messages
	public void msgHereIsAnOrder(Map<String, Integer> chosenItems) {
		/*newOrder = new Order(order);
		newOrder.state = orderState.New;
		for(item in chosenItems) {
			newOrder.items.add(item);
		}
		newOrder.customerAtRestaurant = isAtRestaurant;
		orders.add(newOrder);*/
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
		//for (int i : itemList) {
			//DoGetItem(items.get(i).location);
		//}
	}

	
	// if customer is at the Market, give it the items; otherwise send a truck to its place
	private void giveOrderToCustomer(Order order) {
		getOrder(order.items);
		//DoGoToCustomer(); //gui
		//if(order.customer.getPersonAgent().getPosition().equals("market")) {
		//	order.customer.msgHereIsYourOrder(order.items);
		//} 
		//else {
		//	for truck in trucks where truck is available {
		//		truck.msgHereIsAnOrder(order.items,order.customer.location);
		//	}
		//}
		//cashier.msgHereIsACheck(order.customer, order.items);
		//orders.remove(order);
	}



	//utilities
	public Boolean isActive() {
		return isActive;
	}

	public void setCashier(MarketCashierRole c) {
		cashier = c;
	}
	
}
