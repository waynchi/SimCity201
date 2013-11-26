package bank.test.mock;

import market.interfaces.MarketCashier;
import restaurant.interfaces.Cashier;
import bank.interfaces.BankCustomer;
import bank.interfaces.Teller;
import bank.test.Mock;

/**
 * teller
 */

public class MockTeller extends Mock implements Teller {

	public MockTeller(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public void msgNeedHelp(Cashier cashier, String name) {
		
	}
	
	public void msgNeedHelp(MarketCashier mcashier, String name){
		
	}
	
	public void msgHere(BankCustomer cust, String name){
		
	}
	
	public void msgCreateAccount(String name, double initialFund){
		
	}
	
	public void msgWithdraw(int accountID, double moneyNeeded){
		
	}
	
	public void msgDeposit(int accountID, double moneyGiven){
		
	}
	
	public void msgDeposit(double moneyGiven){
		
	}
	
	public void msgDoneAndLeaving(){
		
	}

}

