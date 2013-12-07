package market.interfaces;

import java.util.Map;

import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;

public interface MarketEmployee {

	public abstract void msgIsActive();

	public abstract void msgIsInActive();

	// order from regular market customer
	public abstract void msgHereIsAnOrder(MarketCustomer customer, Map<String, Integer> chosenItems);

	// order from restaurant cook
	public abstract void msgOrder(Map<String, Integer> order, Cook cook, Cashier cashier);

	public abstract void setCashier(MarketCashier marketCashier);

	public abstract void msgAtCabinet();

	public abstract void msgAtCounter();
	
	public abstract String getName();

	public abstract void msgAtExit();

	public abstract MarketCashier getCashier();

	public abstract void msgOrderDelivered(int orderNumber);

	public abstract void msgOrderNotDelivered(int orderNumber);


}
