package market.interfaces;

import java.util.Map;

import bank.interfaces.Teller;
import restaurant.interfaces.Cashier;

public interface MarketCashier {

	public abstract void msgIsActive();

	public abstract void msgIsInActive();

	public abstract void msgHereIsACheck(MarketCustomer customer, Map<String, Integer> items);

	public abstract void msgHereIsACheck(Cashier restaurantCashier, Map<String, Integer> items);

	public abstract void msgHereIsPayment(MarketCustomer customer, double totalPaid);
	
	public abstract void msgHereIsPayment(Double amount, Map<String, Integer> itemsOrdered, Cashier cashier);

	public abstract String getName();
	
	public abstract void msgReadyToHelp(Teller teller);
	
	public abstract void msgGiveLoan(double balance, double amount);
	
	public abstract void msgWithdrawalSuccessful(double funds, double amount);
	
	public abstract void msgDepositSuccessful(double funds);
}
