package market.interfaces;

import java.util.Map;

public interface MarketTruck {

	boolean isAvailable();

	void msgHereIsAnOrder(MarketCustomer customer, Map<String, Integer> items);

}
