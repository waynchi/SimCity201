package bank.test.mock;

import java.util.Map;

import market.interfaces.MarketCashier;
import market.interfaces.MarketCustomer;
import bank.interfaces.Teller;
import bank.test.LoggedEvent;
import bank.test.Mock;
import people.Role;
import restaurant.interfaces.Cashier;


public class MockMarketCashier extends Mock implements MarketCashier{
	
	public int accountID;

	public MockMarketCashier(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHereIsACheck(MarketCustomer customer,
			Map<String, Integer> items) {	
		
	}

	@Override
	public void msgHereIsACheck(Cashier restaurantCashier,
			Map<String, Integer> items) {		
		
	}

	@Override
	public void msgHereIsPayment(MarketCustomer customer, double totalPaid) {	
		
	}
	

	@Override
	public void msgHereIsPayment(Double amount,
			Map<String, Integer> itemsOrdered, Cashier cashier) {	
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public void msgIsActive() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIsInActive() {
		// TODO Auto-generated method stub
		
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


}
