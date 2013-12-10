package market.test;

import market.interfaces.MarketCashier;
import restaurant.interfaces.Cashier;
import bank.interfaces.BankCustomer;
import bank.interfaces.Robber;
import bank.interfaces.Teller;

public class MockTeller extends Mock implements Teller{
	String name;
	
	public MockTeller(String _name) {
		super(_name);
		name = _name;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgGiveMoney() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHere(BankCustomer cust, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHere(Robber robber, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgNeedHelp(Cashier cashier, String name) {
		log.add(new LoggedEvent("received message need help from cashier "+cashier.getName()));
		
	}

	@Override
	public void msgNeedHelp(MarketCashier mcashier, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWithdraw(int accountID, double moneyNeeded) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWithdraw(double moneyNeeded) {
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
	
	public String getName() {
		return name;
	}

}
