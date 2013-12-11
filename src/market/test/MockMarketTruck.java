package market.test;

import java.util.Map;

import restaurant.interfaces.Cook;
import market.interfaces.MarketTruck;

public class MockMarketTruck extends Mock implements MarketTruck{

	public MockMarketTruck(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}


	@Override
	public void msgHereIsAnOrder(Cook cook, Map<String, Integer> items,
			int number, int marketNumber) {
		log.add(new LoggedEvent("get an order from employee for cook, with order number "+ number));
		
	}

}
