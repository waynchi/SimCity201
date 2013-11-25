package restaurant.test.mock;

import java.util.Map;

import bank.interfaces.Teller;
import market.interfaces.MarketCashier;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

public class MockCashier extends Mock implements Cashier {

	
	public MockCashier(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHereIsBill (Customer c, String food, Waiter w){
		log.add(new LoggedEvent("Received msgHereIsBill from Waiter " + w.getName()));
	}
	
	public void msgPayMyCheck (Customer c, Double amount){
		log.add(new LoggedEvent("Received msgPayMyCheck from Customer " + c.getName()));
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void msgHereIsChange(double change) {
		log.add(new LoggedEvent("Received msgHereIsChange, change is " + change));
	}

	@Override
	public void msgHereIsWhatIsDue(MarketCashier marketCashier, double price,
			Map<String, Integer> items) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgMarketOrderReceived(Map<String, Integer> marketOrder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyToHelp(Teller teller) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGiveLoan(double balance, double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWithdrawSuccessful(double funds, double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDepositSuccessful(double funds) {
		// TODO Auto-generated method stub
		
	}
}
