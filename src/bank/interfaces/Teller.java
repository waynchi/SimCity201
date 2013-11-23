package bank.interfaces;

/**
 * teller
 */

public interface Teller {

	public abstract void msgHere(BankCustomer cust, String name);
	
	public abstract void msgWithdraw(int accountID, double moneyNeeded);
	
	public abstract void msgDeposit(int accountID, double moneyGiven);
	
	public abstract void msgDeposit(double moneyGiven);
	
	public abstract void msgDoneAndLeaving();

}

