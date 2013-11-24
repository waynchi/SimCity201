package people.test.mock;

import people.Role;
import restaurant.BaseWaiterRole;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Market;
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
	
	public void msgHereIsMarketBill (Market m, double price) {
		//log.add(new LoggedEvent("Recieved msgHereIsMarketBill from Market" + m.getName()));
	}
	
	public void msgPayMyCheck (Customer c, Double amount){
		//log.add(new LoggedEvent("Received msgPayMyCheck from Customer " + c.getName()));
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
}
