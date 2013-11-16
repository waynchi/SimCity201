package restaurant.interfaces;

public interface Cashier {
	// Cook receives an order from the waiter and stores it into a list
	public abstract void msgHereIsBill (Customer c, String food, Waiter w);
	public abstract void msgHereIsMarketBill (Market m, double price);
	
	public abstract void msgPayMyCheck (Customer c, Double amount);
	public abstract String getName();
}
