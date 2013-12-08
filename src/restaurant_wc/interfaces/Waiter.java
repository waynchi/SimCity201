package restaurant_wc.interfaces;

import java.util.concurrent.Semaphore;

import restaurant.interfaces.Cashier;
import restaurant_wc.WcCookRole;
import restaurant_wc.WcHostAgent;
import restaurant_wc.Order;
import restaurant_wc.Table;
import restaurant_wc.WaiterAgent.MyCustomer;
import restaurant_wc.gui.WaiterGui;

public interface Waiter {

	public abstract Semaphore getTableSet();

	public abstract String getMaitreDName();

	public abstract String getName();

	public abstract void setHost(WcHostAgent host);

	public abstract void setCook(WcCookRole cook);

	public abstract void setCashier(Cashier cashier);

	// Messages

	public abstract void msgSitAtTable(Customer cust, Table t);

	public abstract void msgDoIWantToGoOnBreak();

	public abstract void msgIWantToGoWork();

	public abstract void msgLeavingTable(Customer cust);

	public abstract void msgOrderIsReady(Order o);

	public abstract void msgAtTable();

	public abstract void msgReadyAtTable();

	public abstract void msgAtDoor();

	public abstract void msgAtCook();

	public abstract void msgReadyToOrder(Customer cust);

	public abstract void msgHereIsMyChoice(String choice, Customer cust);

	public abstract void msgPermissionToGoOnBreak(boolean t);

	public abstract void msgIWantMyCheck(String Choice, Customer cust);

	public abstract void msgHereisACheck(double amt, Customer customer);

	public abstract void seatCustomer(Customer customer, Table table,
			MyCustomer myCust);

	public abstract void msgOutOfChoice(String choice, Customer cust);

	public abstract void setGui(WaiterGui gui);

	public abstract WaiterGui getGui();

}