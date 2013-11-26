package bank.test.mock;

import bank.interfaces.BankCustomer;

/**
 * teller
 */

public interface MockTeller {

	public abstract void msgHere(BankCustomer cust);
	
	public abstract void msgCreateAccount(String name, double initialFund);
	
	public abstract void msgWithdraw(int accountID, double moneyNeeded);
	
	public abstract void msgDeposit(int accountID, double moneyGiven);
	
	public abstract void msgDeposit(double moneyGiven);
	
	public abstract void msgDoneAndLeaving();

}

