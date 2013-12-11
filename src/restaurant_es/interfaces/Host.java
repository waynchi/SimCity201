package restaurant_es.interfaces;

import java.util.List;

import people.People;
import restaurant_es.BaseWaiterRoleEs;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;

public interface Host {

	public abstract void setCashier(Cashier cashierRole);

	public abstract List<Waiter> getWaiters();

	public abstract Cook getCook();

	public abstract People getPerson();

	public abstract Cashier getCashier();

	public abstract void msgTableIsFree(int tableNumber);

	public abstract List<Waiter> getAvailableWaiters();

	public abstract void addWaiter(Waiter Waiter);

	public abstract void setCook(Cook cookRole);

	public abstract int getCustomerSize();

}
