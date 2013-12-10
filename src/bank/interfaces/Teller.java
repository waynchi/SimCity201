package bank.interfaces;

import market.interfaces.MarketCashier;
import restaurant.interfaces.Cashier;

/**
 * teller
 */

public interface Teller {
	
	public abstract void msgGiveMoney();

	public abstract void msgHere(BankCustomer cust, String name);
	
	public abstract void msgHere(Robber robber, String name);
	
	public abstract void msgNeedHelp(Cashier cashier, String name);
	
	public abstract void msgNeedHelp(MarketCashier mcashier, String name);
	
	public abstract void msgWithdraw(int accountID, double moneyNeeded);
	
	public abstract void msgWithdraw(double moneyNeeded);
	
	public abstract void msgDeposit(int accountID, double moneyGiven);
	
	public abstract void msgDeposit(double moneyGiven);
	
	public abstract void msgDoneAndLeaving();

}

