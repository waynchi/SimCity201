package market.interfaces;

import java.util.Map;

import restaurant.interfaces.Cook;
import restaurant.test.mock.EventLog;

public interface MarketTruck {


	//void msgHereIsAnOrder(MarketCustomer customer, Map<String, Integer> items);

	public EventLog log = new EventLog();

	public abstract void msgHereIsAnOrder(Cook cook, Map<String, Integer> items);

}
