package market.mock;

import java.util.Map;

import market.interfaces.MarketCustomer;
import market.interfaces.MarketEmployee;

public class MockMarketEmployee extends Mock implements MarketEmployee{

	public MockMarketEmployee(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHereIsAnOrder(MarketCustomer marketCustomerRole,
			Map<String, Integer> itemsNeeded) {
		// TODO Auto-generated method stub
		
	}

}
