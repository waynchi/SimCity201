package people.test.mock;

import market.interfaces.MarketCashier;
import people.Role;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

public class MockCashier extends Role implements Cashier {

	
	public MockCashier(String name) {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHereIsBill (Customer c, String food, Waiter w){
		//log.add(new LoggedEvent("Received msgHereIsBill from Waiter " + w.getName()));
	}
	
	public void msgPayMyCheck (Customer c, Double amount){
		//log.add(new LoggedEvent("Received msgPayMyCheck from Customer " + c.getName()));
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgHereIsWhatIsDue(MarketCashier marketCashier, double price) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsChange(double change) {
		// TODO Auto-generated method stub
		
	}
}
