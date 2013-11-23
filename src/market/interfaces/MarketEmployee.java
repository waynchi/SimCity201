package market.interfaces;

import java.util.Map;

import market.MarketCustomerRole;

public interface MarketEmployee {

	public void msgHereIsAnOrder(MarketCustomer marketCustomerRole,
			Map<String, Integer> itemsNeeded);

}
