package restaurant.test.mock;

import java.util.Map;

import restaurant.CookRole;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Market;

public class MockMarket extends Mock implements Market{

	public MockMarket(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgOrder(Map<String, Integer> orderList, CookRole c, Cashier ca) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPayMarketBill(double amount, Cashier ca) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgPayMarketBill from Cashier " + ca.getName() + " and the amount"
				+ " is " + amount));

		
	}
	

}
