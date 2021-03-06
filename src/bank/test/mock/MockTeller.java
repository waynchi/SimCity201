package bank.test.mock;

import market.interfaces.MarketCashier;
import restaurant.interfaces.Cashier;
import bank.interfaces.BankCustomer;
import bank.interfaces.Robber;
import bank.interfaces.Teller;
import bank.test.LoggedEvent;
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
		log.add(new LoggedEvent("received msgHere from customer"));
	}
	
	public void msgCreateAccount(String name, double initialFund){
		
	}
	
	public void msgWithdraw(int accountID, double moneyNeeded){
		log.add(new LoggedEvent("Received msgWithdraw for: " + moneyNeeded));
	}
	
	public void msgDeposit(int accountID, double moneyGiven){
		log.add(new LoggedEvent("Received msgDeposit for: " + moneyGiven));
	}
	
	public void msgDeposit(double moneyGiven){
		
		log.add(new LoggedEvent("Received msgDeposit for: " + moneyGiven));
		
	}
	
	public void msgDoneAndLeaving(){
		
		log.add(new LoggedEvent("received msgDoneAndLeaving from customer"));
		
	}

	@Override
	public void msgWithdraw(double moneyNeeded) {
		log.add(new LoggedEvent("Received msgWithdraw for: " + moneyNeeded));
		
	}

	@Override
	public void msgGiveMoney() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHere(Robber robber, String name) {
		// TODO Auto-generated method stub
		
	}

}

