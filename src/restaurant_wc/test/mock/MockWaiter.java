package restaurant_wc.test.mock;


import java.util.concurrent.Semaphore;

import restaurant_wc.WcCookRole;
import restaurant_wc.WcHostAgent;
import restaurant_wc.Menu;
import restaurant_wc.Order;
import restaurant_wc.Table;
import restaurant_wc.WaiterAgent;
import restaurant_wc.WaiterAgent.MyCustomer;
import restaurant_wc.gui.CustomerGui;
import restaurant_wc.gui.WaiterGui;
import restaurant_wc.interfaces.Cashier;
import restaurant_wc.interfaces.Customer;
import restaurant_wc.interfaces.Waiter;

/**
 * A sample MockWaiter built to unit test a WcCashierRole.
 *
 * @author Wayne Chi
 *
 */
public class MockWaiter extends Mock implements Waiter {
	
	public EventLog log = new EventLog();

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;

	public MockWaiter(String name) {
		super(name);
	}

	@Override
	public Semaphore getTableSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMaitreDName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setHost(WcHostAgent host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCook(WcCookRole cook) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCashier(Cashier cashier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSitAtTable(Customer cust, Table t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoIWantToGoOnBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWantToGoWork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeavingTable(Customer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderIsReady(Order o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtTable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyAtTable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtDoor() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtCook() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyToOrder(Customer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMyChoice(String choice, Customer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPermissionToGoOnBreak(boolean t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWantMyCheck(String Choice, Customer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereisACheck(double amt, Customer customer) {
		log.add(new LoggedEvent("Received msgHereisACheck from cashier. Total = "+ amt));
	}

	@Override
	public void seatCustomer(Customer customer, Table table, MyCustomer myCust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfChoice(String choice, Customer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(WaiterGui gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WaiterGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

}
