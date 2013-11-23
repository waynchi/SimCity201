package market.interfaces;

import java.util.Map;

public interface MarketCashier {

	void msgHereIsACheck(MarketCustomer customer, Map<String, Integer> items);

}
