package market.mock;

import java.util.Map;

import people.Role;
import restaurant.interfaces.Cashier;
import market.interfaces.MarketCashier;
import market.interfaces.MarketCustomer;

public class MockMarketCashier extends Role implements MarketCashier{

	public MockMarketCashier(String name) {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHereIsACheck(MarketCustomer customer,
			Map<String, Integer> items) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsACheck(Cashier restaurantCashier,
			Map<String, Integer> items) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsPayment(Double amount, Cashier cashier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsPayment(MarketCustomer customer, double totalPaid) {
		// TODO Auto-generated method stub
		
	}

}
