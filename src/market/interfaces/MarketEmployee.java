package market.interfaces;

import java.util.List;
import java.util.Map;

import people.People;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import restaurant.test.mock.EventLog;

public interface MarketEmployee {

	public abstract void msgIsActive();

	public abstract void msgIsInActive();

	// order from regular market customer
	public abstract void msgHereIsAnOrder(MarketCustomer customer, Map<String, Integer> chosenItems);

	// order from restaurant cook
	public abstract void msgHereIsAnOrder(Map<String, Integer> order, Cook cook, Cashier cashier);

	public abstract void setCashier(MarketCashier marketCashier);

	public abstract void msgAtCabinet();

	public abstract void msgAtCounter();
	
	public abstract String getName();

	public abstract void msgAtExit();

	public abstract MarketCashier getCashier();

	public abstract void msgOrderDelivered(int orderNumber);

	public abstract void msgOrderNotDelivered(int orderNumber);

	public abstract List<People> getWorkers();


}
