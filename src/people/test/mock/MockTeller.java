package people.test.mock;

import market.interfaces.MarketCashier;
import bank.interfaces.BankCustomer;
import bank.interfaces.Teller;
import people.Role;
import restaurant.interfaces.Cashier;

public class MockTeller extends Role implements Teller{

	public MockTeller(String name) {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgWithdraw(int accountID, double moneyNeeded) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDeposit(int accountID, double moneyGiven) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDeposit(double moneyGiven) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneAndLeaving() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHere(BankCustomer cust, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgNeedHelp(Cashier cashier, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgNeedHelp(MarketCashier mcashier, String name) {
		// TODO Auto-generated method stub
		
	}

}
