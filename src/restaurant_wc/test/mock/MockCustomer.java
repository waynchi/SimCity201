package restaurant_wc.test.mock;


import java.util.concurrent.Semaphore;

import restaurant_wc.Menu;
import restaurant_wc.gui.CustomerGui;
import restaurant.interfaces.Cashier;
import restaurant_wc.interfaces.Customer;
import restaurant_wc.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a WcCashierRole.
 *
 * @author Wayne Chi
 *
 */
public class MockCustomer extends Mock implements Customer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public EventLog log = new EventLog();
	public Cashier cashier;

	public MockCustomer(String name) {
		super(name);
	}

	@Override
	public void msgHereIsYourTotal(double total) {
		log.add(new LoggedEvent("Received HereIsYourTotal from waiter. Total = "+ total));

		if(this.getName().toLowerCase().contains("thief")){
			//test the non-normative scenario where the customer has no money if their name contains the string "theif"
			//cashier.IAmShort(this, 0);

		}else if (this.getName().toLowerCase().contains("rich")){
			//test the non-normative scenario where the customer overpays if their name contains the string "rich"
			cashier.msgHereIsMyPayment(Math.ceil(total), Math.ceil(total),this);

		}else{
			//test the normative scenario
			cashier.msgHereIsMyPayment(total, total, this);
		}
	}

	@Override
	public void msgHereIsYourChange(double total) {
		log.add(new LoggedEvent("Received HereIsYourChange from cashier. Change = "+ total));
	}

	/*@Override
	public void YouOweUs(double remaining_cost) {
		log.add(new LoggedEvent("Received YouOweUs from cashier. Debt = "+ remaining_cost));
	}*/

	@Override
	public CustomerGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Semaphore getFollowing() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgHereIsYourFood() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatWouldYouLike(Menu menu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatElseWouldYouLike(String outOfChoice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSitAtTable(Waiter waiterAgent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgYouNeedToWait(int customerNum) {
		// TODO Auto-generated method stub
		
	}

}
