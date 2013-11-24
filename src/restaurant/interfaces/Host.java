package restaurant.interfaces;

import java.util.List;

import people.People;
import restaurant.BaseWaiterRole;

public interface Host {

	public abstract void setCashier(Cashier cashierRole);

	public abstract List<BaseWaiterRole> getWaiters();

	public abstract Cook getCook();

	public abstract People getPerson();

	public abstract Cashier getCashier();

	public abstract void msgTableIsFree(int tableNumber);

	public abstract void IWantABreak(Waiter w);

	public abstract List<Waiter> getAvailableWaiters();

	public abstract void IAmOffBreak(Waiter w);

}
