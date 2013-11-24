package market.test;

import java.util.Map;

import people.People;
import people.Role;
import market.interfaces.MarketCashier;
import market.interfaces.MarketCustomer;

public class MockMarketCustomer extends Role implements MarketCustomer {

	public MockMarketCustomer(String name) {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHereIsYourOrder(Map<String, Integer> items) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsWhatIsDue(double totalDue, MarketCashier marketCashier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsChange(double change) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public People getPerson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgBuy(Map<String, Integer> items) {
		// TODO Auto-generated method stub
		
	}

}
