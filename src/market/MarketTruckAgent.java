package market;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import restaurant.CookRole;
import restaurant.interfaces.Cook;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;
import transportation.CarGui;
import agent.Agent;
import market.gui.MarketTruckGui;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketEmployee;
import market.interfaces.MarketTruck;

public class MarketTruckAgent extends Agent implements MarketTruck{
	//data
	private EventLog log = new EventLog();
	public boolean inTest = false;
	
	//MarketTruckGui gui = new MarketTruckGui(this);
	MarketEmployee employee;
	List<Order> orders = new ArrayList<Order>();
	String name;
	Semaphore atRestaurant = new Semaphore(0,true);

	
	public MarketTruckAgent(String n, MarketEmployee me) {
		employee = me;
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
	
	public void msgAtRestaurant() {
		atRestaurant.release();
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
				//gui.doDelivery(order.cook, order.orderNumber);
				//atRestaurant.acquire
				if (!((CookRole) order.cook).getPersonAgent().getRestaurant(order.cook.getRestaurantIndex()).isClosed) {
					order.cook.msgHereIsYourOrder(order.items, order.orderNumber);	
					employee.msgOrderDelivered(order.orderNumber);
				}
				else {
					employee.msgOrderNotDelivered(order.orderNumber);
				}
				log.add(new LoggedEvent("order delivered to restaurant"));
				orders.remove(order);
	}


	//utilities
	public void setGui(MarketTruckGui mtg){
		gui = mtg;
	}
	
	public void setEmployee (MarketEmployee me) {
		employee = me;
	}
	

}
