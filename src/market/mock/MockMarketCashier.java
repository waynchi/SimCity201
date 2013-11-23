package market.mock;

import java.util.Map;

import market.interfaces.MarketCashier;
import market.interfaces.MarketCustomer;

public class MockMarketCashier extends Mock implements MarketCashier{

	public MockMarketCashier(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHereIsACheck(MarketCustomer customer,
			Map<String, Integer> items) {
		// TODO Auto-generated method stub
		
	}

}
