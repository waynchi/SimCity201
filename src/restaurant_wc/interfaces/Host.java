package restaurant_wc.interfaces;

import java.util.List;

import people.People;
import restaurant_wc.BaseWaiterRole;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;

public interface Host {

	public abstract void setCashier(Cashier cashierRole);

	public abstract List<Waiter> getWaiters();

	public abstract restaurant.interfaces.Cook getCook();

	public abstract People getPerson();

	public abstract restaurant.interfaces.Cashier getCashier();

	public abstract void msgTableIsFree(int tableNumber);

	public abstract void IWantABreak(Waiter w);

	public abstract List<Waiter> getAvailableWaiters();

	public abstract void IAmOffBreak(Waiter w);

	public abstract void addWaiter(Waiter Waiter);

	public abstract void setCook(Cook cookRole);

	public abstract int getCustomerSize();

}
