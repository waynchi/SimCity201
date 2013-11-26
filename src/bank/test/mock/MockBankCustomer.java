package bank.test.mock;

import restaurant.test.mock.Mock;
import bank.interfaces.BankCustomer;
import bank.interfaces.Teller;

/**
 * Bank customer.
 */
public class MockBankCustomer extends Mock implements BankCustomer {

	public MockBankCustomer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public void msgAccountAndLoan(int accountID, double balance, double money) {
		
	}

	public void needMoney(double money) {
		
	}
	
	public void depositMoney(double money) {
		
	}
	
	public void msgReadyToHelp(Teller t){ 
		
	}
	
	public void msgAccountBalance(int accountID, double balance) {
		
	}
	
	public void msgGiveLoan(double balance, double money) {
		
	}
	
	public void msgWithdrawSuccessful(double balance, double money) {
		
	}
	
	public void msgDepositSuccessful(double balance) {
		
	}
}