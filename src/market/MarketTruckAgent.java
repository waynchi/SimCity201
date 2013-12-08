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
import market.interfaces.MarketEmployee;
import market.interfaces.MarketTruck;

public class MarketTruckAgent extends Agent implements MarketTruck{
	//data
	private EventLog log = new EventLog();
	public boolean inTest = false;
	
	MarketTruckGui gui = null;
	MarketEmployee employee;
	String name;
	Semaphore atRestaurant = new Semaphore(0,true);
	Semaphore atMarket = new Semaphore(0,true);

	
	public MarketTruckAgent(String n, MarketEmployee me) {
		employee = me;
		name = n;
	}
	
	List<Order> orders = new ArrayList<Order>();
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
	
	public void msgAnimationFinishedArrivedAtDestination(String destination) {
		if (destination.equals("Market")) {
			atMarket.release();
		}
		else {
			atRestaurant.release();
		}
		stateChanged();
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
/*				gui.doDeliver(this, order.cook, order.orderNumber);
				try {
					atRestaurant.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
				if (!((MarketEmployeeRole) employee).getPersonAgent().getRestaurant(order.cook.getRestaurantIndex()).isClosed) {
					log.add(new LoggedEvent("order delivered to restaurant"));
					order.cook.msgHereIsYourOrder(order.items, order.orderNumber);	
					employee.msgOrderDelivered(order.orderNumber);
				}
				else {
					log.add(new LoggedEvent("restaurant is closed and delivery failed"));
					employee.msgOrderNotDelivered(order.orderNumber);
				}
				orders.remove(order);
				
				/*gui.doGoBackToMarket();
				try {
					atMarket.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
	}


	//utilities
	public void setGui(MarketTruckGui mtg){
		gui = mtg;
	}
	
	public void setEmployee (MarketEmployee me) {
		employee = me;
	}
	

}
