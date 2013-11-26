package market;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import restaurant.interfaces.Cook;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;
import agent.Agent;
import market.gui.MarketTruckGui;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketTruck;

public class MarketTruckAgent extends Agent implements MarketTruck{
	//data
	public boolean inTest = false;
	
	MarketTruckGui gui = new MarketTruckGui(this);
	List<Order> orders = new ArrayList<Order>();
	String name;
	Semaphore orderDelivered = new Semaphore(0,true);	
	
	
	public MarketTruckAgent(String n) {
		name = n;
	}
	
	class Order {
		MarketCustomer customer = null;
		Cook cook = null;
		Map<String,Integer> items;
		
		Order (MarketCustomer c, Map<String, Integer> i) {
			customer = c;
			items = i;
		}
		
		Order (Cook c, Map<String, Integer> i) {
			cook = c;
			items = i;
		}
	}
	
	//messages
	//public void msgHereIsAnOrder(MarketCustomer customer, Map<String,Integer> items) {
	//	orders.add(new Order (customer, items));
	//	stateChanged();
	//}
	
	public void msgOrderDelivered() {
		orderDelivered.release();
		stateChanged();;
	}
	
	public void msgHereIsAnOrder(Cook cook, Map<String, Integer> items) {
		log.add(new LoggedEvent("received message here is an order"));
		orders.add(new Order (cook, items));
		stateChanged();
	}


	
	//scheduler
	public boolean pickAndExecuteAnAction() {
		if (!orders.isEmpty()) {
			deliverOrder(orders.get(0));
			return true;
		}
		return false;
	}


	//actions
	private void deliverOrder(Order order) {
		// if order is from restaurant, deliver to restaurant
		//if (order.cook != null) {
		if(!inTest){
			gui.deliver(order.cook.getPerson());
		}
		try {
			orderDelivered.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		order.cook.msgHereIsYourOrder(order.items);
		//}
		//else {
		//	gui.deliver(order.customer.getPerson().getPosition();
		//	order.customer.msgHereIsYourOrder(order.items);
		//}
		
		 // need a getGui for personAgent
		orders.remove(order);
	}
	
	//utilities
	

}
