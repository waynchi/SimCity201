package market;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import restaurant.CookRole;
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
	private EventLog log = new EventLog();

	
	public MarketTruckAgent(String n) {
		name = n;
	}
	
	class Order {
		Cook cook = null;
		Map<String,Integer> items;
		int orderNumber;
		
		Order (Cook c, Map<String, Integer> i, int number) {
			cook = c;
			items = i;
			orderNumber = number;
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
	
	public void msgHereIsAnOrder(Cook cook, Map<String, Integer> items, int number) {
		log.add(new LoggedEvent("received an order from employee, deliver to cook"));
		orders.add(new Order (cook, items, number));
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
	private void deliverOrder(final Order order) {
		//if(!inTest){
		//	gui.deliver(order.cook.getPerson());
		//}
		//new java.util.Timer().schedule(
		//		new java.util.TimerTask(){
		//			public void run(){
						print("order delivered to restaurant");
						((CookRole) order.cook).msgHereIsYourOrder(order.items, order.orderNumber);		
						orders.remove(order);
		//		},
		//		2000);
		
		//}
		
	}


	//utilities
	

}
