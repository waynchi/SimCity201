package restaurant_wc.interfaces;

import java.util.concurrent.Semaphore;

import restaurant_wc.Menu;
import restaurant_wc.gui.CustomerGui;
import restaurant_wc.gui.Gui;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Customer {

	/**
	 * @param total The cost according to the cashier
	 *
	 * Sent by the cashier prompting the customer's money after the customer has approached the cashier.
	 */
	public abstract void msgHereIsYourTotal(double total);

	/**
	 * @param total change (if any) due to the customer
	 *
	 * Sent by the cashier to end the transaction between him and the customer. total will be >= 0 .
	 */
	public abstract void msgHereIsYourChange(double total);

	public abstract void msgYouNeedToWait(int customerNum);

	public abstract String getName();

	public abstract CustomerGui getGui();

	public abstract Semaphore getFollowing();

	public abstract void msgHereIsYourFood();

	public abstract void msgWhatWouldYouLike(Menu menu);

	public abstract void msgWhatElseWouldYouLike(String outOfChoice);

	public abstract void msgSitAtTable(Waiter waiterAgent);



	/**
	 * @param remaining_cost how much money is owed
	 * Sent by the cashier if the customer does not pay enough for the bill (in lieu of sending {@link #HereIsYourChange(double)}
	 */
	//public abstract void YouOweUs(double remaining_cost);

}