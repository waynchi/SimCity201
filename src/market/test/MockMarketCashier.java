package market.test;

import java.util.Map;

import bank.interfaces.Teller;
import people.Role;
import restaurant.interfaces.Cashier;
import restaurant.test.mock.LoggedEvent;
import market.interfaces.MarketCashier;
import market.interfaces.MarketCustomer;

public class MockMarketCashier extends Mock implements MarketCashier{

	public MockMarketCashier(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHereIsACheck(MarketCustomer customer,Map<String, Integer> items) {
		log.add(new LoggedEvent("received msgHereIsACheck from employee for customer"));		
		
	}

	@Override
	public void msgHereIsACheck(Cashier restaurantCashier,	Map<String, Integer> items, int orderNumber) {
		log.add(new LoggedEvent("received msgHereIsACheck for restaurant cashier with order number " + orderNumber));		
		
	}
	
	@Override
	public void msgHereIsPayment(MarketCustomer customer, double totalPaid) {
		log.add(new LoggedEvent("received msgHereIsACheck from customer"));		
		
	}
	

	@Override
	public void msgHereIsPayment(Double amount, Map<String, Integer> itemsOrdered, Cashier cashier) {
		log.add(new LoggedEvent("received msgHereIsPayment from cashier"));		
		
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

	


}
