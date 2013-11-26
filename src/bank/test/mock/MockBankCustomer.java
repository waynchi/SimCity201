package bank.test.mock;

import bank.interfaces.Teller;

/**
 * Bank customer.
 */
public interface MockBankCustomer {

	public abstract void needMoney(double money);
	
	public abstract void depositMoney(double money);
	
	public abstract void msgReadyToHelp(Teller t);
	
	public abstract void msgAccountBalance(int accountID, double balance);
	
	public abstract void msgGiveLoan(double balance, double money);
	
	public abstract void msgWithdrawSuccessful(double balance, double money);
	
	public abstract void msgDepositSuccessful(double balance);
}