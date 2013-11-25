package people.test.mock;

import bank.interfaces.BankCustomer;
import bank.interfaces.Teller;
import people.Role;

public class MockBankCustomer extends Role implements BankCustomer{

	public MockBankCustomer(String name) {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgReadyToHelp(Teller t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAccountBalance(int accountID, double balance) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGiveLoan(double balance, double money) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWithdrawSuccessful(double balance, double money) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDepositSuccessful(double balance) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAccountAndLoan(int accountID, double balance, double money) {
		// TODO Auto-generated method stub
		
	}

}
