package market.test;

import java.util.Map;

import people.People;
import bank.interfaces.Teller;
import restaurant.interfaces.Cashier;
import market.interfaces.MarketCashier;
import market.interfaces.MarketCustomer;

public class MockMarketCashier extends Mock implements MarketCashier{

	public MockMarketCashier(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHereIsACheck(MarketCustomer customer,Map<String, Integer> items) {
		log.add(new LoggedEvent("received msgHereIsACheck for customer"));		
		
	}

	
	@Override
	public void msgHereIsPayment(MarketCustomer customer, double totalPaid) {
		log.add(new LoggedEvent("received msgHereIsACheck from customer"));		
		
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

	@Override
	public void msgGetOut() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public People getPersonAgent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgHereIsACheck(Cashier restaurantCashier,
			Map<String, Integer> items, int orderNumber, int marketNumber) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("received msgHereIsACheck for restaurant cashier with order number " + orderNumber));
	}

	@Override
	public void msgHereIsPayment(Double amount, int orderNumber, Cashier cashier) {
		// TODO Auto-generated method stub
		
	}

	


}
