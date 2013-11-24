package restaurant.interfaces;

import java.util.Map;

import market.interfaces.MarketCashier;

public interface Cashier {
	// Cook receives an order from the waiter and stores it into a list
	public abstract void msgHereIsBill (Customer c, String food, Waiter w);
	public abstract void msgHereIsWhatIsDue (MarketCashier marketCashier, double price, Map<String, Integer> items);
	
	public abstract void msgPayMyCheck (Customer c, Double amount);
	public abstract String getName();
	public abstract void msgHereIsChange(double change);
	public abstract void msgMarketOrderReceived(Map<String, Integer> marketOrder);
}
