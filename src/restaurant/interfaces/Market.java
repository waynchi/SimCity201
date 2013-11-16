package restaurant.interfaces;

import java.util.Map;

import restaurant.CookRole;

public interface Market {
	public abstract void msgOrder (Map<String, Integer> orderList, CookRole c, Cashier ca);
	public abstract void msgPayMarketBill (double amount, Cashier ca);
	public abstract String getName();
}
