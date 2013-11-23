package market.interfaces;

import java.util.Map;

public interface MarketCustomer {

	void msgHereIsYourOrder(Map<String, Integer> items);

	void msgHereIsWhatIsDue(double totalDue, MarketCashier marketCashier);

	void msgHereIsChange(double change);

}
