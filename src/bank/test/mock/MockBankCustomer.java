package bank.test.mock;

import bank.interfaces.BankCustomer;
import bank.interfaces.Teller;
import bank.test.LoggedEvent;
import bank.test.Mock;

/**
 * Bank customer.
 */
public class MockBankCustomer extends Mock implements BankCustomer {
	
	public double money = 1000;
	public int accountID = -1;

	public MockBankCustomer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public void msgAccountAndLoan(int accountID, double balance, double money) {
		log.add(new LoggedEvent("received Loan of: " + money + " balance is: " + balance + "account number is: " + accountID));
	}

	public void needMoney(double money) {
		
	}
	
	public void depositMoney(double money) {
		
	}
	
	public void msgReadyToHelp(Teller t){ 
		log.add(new LoggedEvent("received msgReadyToHelp from teller"));
	}
	
	public void msgAccountBalance(int accountID, double balance) {
		log.add(new LoggedEvent("received account and balance " + accountID + " " + balance));
	}
	
	public void msgGiveLoan(double balance, double money) {
		log.add(new LoggedEvent("received Loan of: " + money + " balance is: " + balance));
	}
	
	public void msgWithdrawSuccessful(double balance, double money) {
		log.add(new LoggedEvent("received successful withdraw of: " + money + " balance is: " + balance));
	}
	
	public void msgDepositSuccessful(double balance) {
		log.add(new LoggedEvent("received account balance " + balance));
	}

	@Override
	public void msgGetOut() {
		// TODO Auto-generated method stub
		
	}
}