package restaurant_wc.interfaces;

import restaurant_wc.WcCookRole;

public interface Market {

	public abstract void setCook(WcCookRole c);

	public abstract void setCashier(Cashier c);

	//messages
	public abstract void msgBuyFood(String choice, int i);

	public abstract void msgBillPayment(double payment);

}