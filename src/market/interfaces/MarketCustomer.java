package market.interfaces;

import java.util.Map;


import people.People;

public interface MarketCustomer {

	public abstract void msgIsActive ();
	
	public abstract void msgIsInActive();
	
	public abstract void msgBuy(Map<String,Integer> items);

	public abstract void msgHereIsYourOrder(Map<String, Integer> _itemsReceived);

	public abstract void msgHereIsWhatIsDue(double _totalDue, MarketCashier c);

	public abstract void msgHereIsChange(double totalChange);
	
	public abstract People getPerson();


}
