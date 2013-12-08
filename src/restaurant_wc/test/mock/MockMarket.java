package restaurant_wc.test.mock;

import restaurant_wc.WcCookRole;
import restaurant_wc.interfaces.Cashier;
import restaurant_wc.interfaces.Customer;
import restaurant_wc.interfaces.Market;

public class MockMarket  extends Mock implements Market{
	
	public EventLog log = new EventLog();

	public MockMarket(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setCook(WcCookRole c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCashier(Cashier c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBuyFood(String choice, int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBillPayment(double payment) {
		log.add(new LoggedEvent("Recieved message Bill Payment. Total = " + payment));		
	}

}
