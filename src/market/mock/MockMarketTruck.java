package market.mock;

import java.util.Map;

import restaurant.interfaces.Cook;
import market.interfaces.MarketTruck;

public class MockMarketTruck extends Mock implements MarketTruck{

	public MockMarketTruck(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	
	/*public void msgHereIsAnOrder(MarketCustomer customer,
			Map<String, Integer> items) {
		// TODO Auto-generated method stub
		
	}*/

	@Override
	public void msgHereIsAnOrder(Cook cook, Map<String, Integer> items) {
		// TODO Auto-generated method stub
		
	}

}
