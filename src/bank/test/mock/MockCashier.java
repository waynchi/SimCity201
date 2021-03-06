package bank.test.mock;

import java.util.Map;

import people.Role;
import market.interfaces.MarketCashier;
import market.interfaces.MarketEmployee;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Host;
import restaurant.interfaces.Waiter;
import bank.interfaces.Teller;
import bank.test.LoggedEvent;
import bank.test.Mock;


public class MockCashier extends Mock implements Cashier {

	public int accountID;
	
	public MockCashier(String name) {
		super(name);
		// TODO Auto-generated constructor stub
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
	public void msgReadyToHelp(Teller teller) {
		log.add(new LoggedEvent("received msgReadyToHelp from teller"));
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
		log.add(new LoggedEvent("received account balance " + funds));
		
	}

	public void setHost(Host host) {
		// TODO Auto-generated method stub
		
	}







	@Override
	public void msgGotMarketOrder(Map<String, Integer> marketOrder,
			int orderNumber, int marketNumber) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void msgHereIsWhatIsDue(double price, Map<String, Integer> items,
			int orderNumber, int marketNumber) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void msgGetOut() {
		// TODO Auto-generated method stub
		
	}
}


