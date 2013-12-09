package restaurant_ps.interfaces;

import people.People;
import restaurant.interfaces.Cashier;

public interface Waiter {
	//public abstract void goOnBreak();
	
	public abstract void setCashier(Cashier cashier);
	
	public abstract void msgAtTable();
	
	public abstract void msgAtCook();
	
	public abstract void msgAtCashier();
	
	public abstract void msgAtWaitingCustomer();
	
	public abstract void msgAskForBreak();
	
	public abstract void msgOffBreak();
	
	public abstract void msgBreakApproved();
	
	public abstract void msgBreakDenied();
	
	public abstract void SitAtTable(Customer customer, int table);
	
	public abstract void msgIAmReadyToOrder(Customer cust);

	public abstract void msgHereIsMyOrder (Customer cust, String choice);
	
	public abstract void msgOrderIsReady (String order, int t);
	
	public abstract void msgOutOfFood (String order, int t);
	
	public abstract void msgHereIsCheck (Customer cust, Double d);
	
	public abstract void msgDoneEatingAndLeaving (Customer cust);

	public abstract String getName();

	public abstract People getPerson();
	
}
