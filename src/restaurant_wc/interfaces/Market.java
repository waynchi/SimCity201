package restaurant_wc.interfaces;

import restaurant_wc.CookAgent;

public interface Market {

	public abstract void setCook(CookAgent c);

	public abstract void setCashier(Cashier c);

	//messages
	public abstract void msgBuyFood(String choice, int i);

	public abstract void msgBillPayment(double payment);

}