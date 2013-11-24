package people.test.mock;

import java.util.Map;

import people.Role;
import restaurant.CookRole;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Market;

public class MockMarket extends Role implements Market{

	public MockMarket(String name) {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgOrder(Map<String, Integer> orderList, CookRole c, Cashier ca) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPayMarketBill(double amount, Cashier ca) {
		// TODO Auto-generated method stub
		//log.add(new LoggedEvent("Received msgPayMarketBill from Cashier " + ca.getName() + " and the amount"
		//		+ " is " + amount));

		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
