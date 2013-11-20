package bank.interfaces;

/**
 * Bank customer.
 */
public interface BankCustomer {

	public abstract void needMoney(double money);
	
	public abstract void depositMoney(double money);
	
	public abstract void msgReadyToHelp(Teller t);
	
	public abstract void msgAccountBalance(int accountID, double balance);
	
	public abstract void msgGiveLoan(double balance, double money);
	
	public abstract void msgWithdrawSuccessful(double balance, double money);
	
	public abstract void msgDepositSuccessful(double balance);
}