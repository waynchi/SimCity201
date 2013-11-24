package market.interfaces;

import java.util.Map;

import restaurant.interfaces.Cashier;

public interface MarketCashier {

	public abstract void msgIsActive();

	public abstract void msgIsInActive();

	public abstract void msgHereIsACheck(MarketCustomer customer, Map<String, Integer> items);

	public abstract void msgHereIsACheck(Cashier restaurantCashier, Map<String, Integer> items);

	public abstract void msgHereIsPayment(MarketCustomer customer, double totalPaid);
	
	public abstract void msgHereIsPayment(Double amount, Cashier cashier);

	public abstract String getName();
}
