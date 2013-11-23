package market.mock;

import java.util.Map;

import market.interfaces.MarketCashier;
import market.interfaces.MarketCustomer;

public class MockMarketCustomer extends Mock implements MarketCustomer {

	public MockMarketCustomer(String name) {
		super(name);
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

}
