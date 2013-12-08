package restaurant_ps.interfaces;

import java.util.Collection;

import restaurant_ps.Table;
import restaurant_ps.WaiterAgent;
import restaurant_ps.gui.HostGui;

public interface Host {

	public abstract String getMaitreDName();

	public abstract String getName();

	public abstract void addWaiter(WaiterAgent w);

	public abstract Collection<Table> getTables();

	// Messages

	public abstract void msgIWantFood(Customer cust);

	public abstract void msgImLeaving(Customer customerAgent);

	public abstract void msgLeavingTable(Customer cust);

	public abstract void msgAtTable();

	public abstract void msgTableIsFree(Table t);

	public abstract void msgWantsToGoOnBreak(Waiter waiterAgent);

	public abstract void setGui(HostGui gui);

	public abstract HostGui getGui();

	public abstract void pauseWaitersAndTheirCustomers();

	public abstract void restartWaiters();

	public abstract void newWaiter();

}