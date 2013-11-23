package market;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import agent.Agent;
import market.gui.MarketTruckGui;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketTruck;

public class MarketTruckAgent extends Agent implements MarketTruck{
	//data
	MarketTruckGui gui = new MarketTruckGui(this);
	List<Order> orders = new ArrayList<Order>();
	
	class Order {
		MarketCustomer customer;
		Map<String,Integer> items;
		
		Order (MarketCustomer c, Map<String, Integer> i) {
			customer = c;
			items = i;
		}
	}
	
	//messages
	public void msgHereIsAnOrder(MarketCustomer customer, Map<String,Integer> items) {
		orders.add(new Order (customer, items));
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
		gui.deliver(order.customer.getPersonAgent().getPosition(); // need a getGui for personAgent
		order.customer.msgHereIsYourOrder(order.items);
		orders.remove(order);
	}
	
	//utilities
	@Override
	public boolean isAvailable() {
		// TODO Auto-generated method stub
		return false;
	}
}
