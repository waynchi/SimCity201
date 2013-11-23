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


}
