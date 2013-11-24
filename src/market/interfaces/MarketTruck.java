package market.interfaces;

import java.util.Map;

import restaurant.interfaces.Cook;

public interface MarketTruck {


	//void msgHereIsAnOrder(MarketCustomer customer, Map<String, Integer> items);

	public abstract void msgHereIsAnOrder(Cook cook, Map<String, Integer> items);

}
