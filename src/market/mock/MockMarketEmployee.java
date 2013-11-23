package market.mock;

import java.util.Map;

import people.Role;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketEmployee;

public class MockMarketEmployee extends Role implements MarketEmployee{

	public MockMarketEmployee(String name) {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHereIsAnOrder(MarketCustomer marketCustomerRole,
			Map<String, Integer> itemsNeeded) {
		// TODO Auto-generated method stub
		
	}

}
