package market.interfaces;

import java.util.Map;

import restaurant.interfaces.Cook;
import restaurant.test.mock.EventLog;

public interface MarketTruck {

	public abstract void msgHereIsAnOrder(Cook cook, Map<String, Integer> items, int number, int marketNumber);

}
