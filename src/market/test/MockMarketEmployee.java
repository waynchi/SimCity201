package market.test;

import java.util.Map;

import people.Role;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import market.interfaces.MarketCashier;
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

	@Override
	public void msgOrder(Map<String, Integer> order, Cook cook, Cashier cashier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCashier(MarketCashier marketCashier) {
		// TODO Auto-generated method stub
		
	}

}
