package restaurant.interfaces;

import java.util.Map;

import people.Role;
import restaurant.CashierRole.MarketBill;
import restaurant.CashierRole.bankActivityEvent;
import restaurant.test.mock.LoggedEvent;
import bank.interfaces.Teller;
import market.MarketCashierRole;
import market.interfaces.MarketCashier;
import market.interfaces.MarketEmployee;

public interface Cashier {
	//from cook
	public abstract void msgGotMarketOrder(Map<String, Integer> marketOrder, int orderNumber, int marketNumber);
	
	// from market cashier
	public abstract void msgHereIsWhatIsDue(double price, Map<String, Integer> items, int orderNumber, int marketNumber);

	// from market cashier
	public abstract void msgHereIsChange(double change);
	
	public abstract void msgGetOut();

	// from bank teller
	public void msgReadyToHelp(Teller teller);

	public void msgGiveLoan(double funds, double amount);

	public void msgWithdrawSuccessful(double funds, double amount);

	public void msgDepositSuccessful(double funds);

	public abstract String getName();


}
