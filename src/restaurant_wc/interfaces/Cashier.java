/**
 * 
 */
package restaurant_wc.interfaces;

import java.util.TimerTask;

import restaurant_wc.WcCustomerRole;
import restaurant_wc.MBill;
import restaurant_wc.test.mock.EventLog;

/**
 * @author Wayne
 *
 */
public interface Cashier {
	
	public abstract void msgHereIsACheck(String choice, Customer cust, Waiter w);
	
	public abstract void  msgHereIsMyPayment(double check, double Pay, Customer cust);

	public abstract void msgHereIsMarketBill(MBill bill, Market market);
	
	
}
