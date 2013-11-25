package restaurant.test.mock;

import market.interfaces.MarketCashier;
import restaurant.interfaces.Cashier;
import bank.interfaces.BankCustomer;
import bank.interfaces.Teller;

public class MockTeller extends Mock implements Teller{

	public MockTeller(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	
	

	@Override
	public void msgWithdraw(int accountID, double moneyNeeded) {
		log.add(new LoggedEvent("Received msgWithdraw from ID " + accountID));
	}

	@Override
	public void msgDeposit(int accountID, double moneyGiven) {
		log.add(new LoggedEvent("Received msgDeposit from ID " + accountID));
	}

	@Override
	public void msgDeposit(double moneyGiven) {
		log.add(new LoggedEvent(""));		
	}

	@Override
	public void msgDoneAndLeaving() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
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
