package market.interfaces;

import java.util.Map;

import people.People;
import bank.interfaces.Teller;
import restaurant.interfaces.Cashier;

public interface MarketCashier {

	public abstract void msgIsActive();

	public abstract void msgIsInActive();

	public abstract void msgHereIsACheck(MarketCustomer customer, Map<String, Integer> items);
	
	public abstract void msgGetOut();

	public abstract void msgHereIsACheck(Cashier restaurantCashier, Map<String, Integer> items, int orderNumber, int marketNumber);

	public abstract void msgHereIsPayment(MarketCustomer customer, double totalPaid);
	
	public abstract void msgHereIsPayment(Double amount, int orderNumber, Cashier cashier);

	public abstract String getName();
	
	public abstract void msgReadyToHelp(Teller teller);
	
	public abstract void msgGiveLoan(double balance, double amount);
	
	public abstract void msgWithdrawSuccessful(double funds, double amount);
	
	public abstract void msgDepositSuccessful(double funds);

	public abstract People getPersonAgent();
}
