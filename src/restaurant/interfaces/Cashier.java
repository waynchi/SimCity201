package restaurant.interfaces;

import java.util.Map;

import people.Role;
import restaurant.CashierRole.bankActivityEvent;
import bank.interfaces.Teller;
import market.interfaces.MarketCashier;
import market.interfaces.MarketEmployee;

public interface Cashier {
	// Cook receives an order from the waiter and stores it into a list
	public abstract void msgHereIsBill (Customer c, String food, Waiter w);
	public abstract void msgHereIsWhatIsDue (MarketEmployee marketEmployee, double price, Map<String, Integer> items);
	
	public abstract void msgPayMyCheck (Customer c, Double amount);
	public abstract String getName();
	public abstract void msgHereIsChange(double change);
	public abstract void msgGotMarketOrder(Role role, Map<String, Integer> marketOrder);
	public abstract void msgReadyToHelp(Teller teller);
	
	public abstract void msgGiveLoan(double balance, double amount);
	
	public abstract void msgWithdrawSuccessful(double funds, double amount);
	
	public abstract void msgDepositSuccessful(double funds);

}
