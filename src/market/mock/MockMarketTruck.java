package market.mock;

import java.util.Map;

import market.interfaces.MarketCustomer;
import market.interfaces.MarketTruck;

public class MockMarketTruck extends Mock implements MarketTruck{

	public MockMarketTruck(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void msgHereIsAnOrder(MarketCustomer customer,
			Map<String, Integer> items) {
		// TODO Auto-generated method stub
		
	}

}
