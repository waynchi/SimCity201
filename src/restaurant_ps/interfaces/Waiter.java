package restaurant_ps.interfaces;

import restaurant.interfaces.Cashier;
import restaurant_ps.Check;
import restaurant_ps.Choice;
import restaurant_ps.Order;
import restaurant_ps.Table;
import restaurant_ps.WaiterAgent.MyCust;
import restaurant_ps.gui.WaiterGui;

public interface Waiter {

	public abstract String getMaitreDName();

	public abstract String getName();

	public abstract void msgAnimationFinishedGoToCustomer(Customer cust);

	public abstract void msgAnimationFinishedGoToSeat(int table);

	public abstract void msgAnimationFinishedGoToCook(Customer lookingForFoodFor);

	public abstract void msgAnimationFinishedGoToCashier(Customer cust);

	public abstract void msgGoOnBreakAfterFinishingCustomers();

	public abstract void msgDoNotGoOnBreak();

	public abstract void msgSeatCustomer(Customer cust, Table t);

	public abstract void msgReadyToOrder(Customer cust);

	public abstract void msgHereIsMyChoice(Customer cust, Choice c);

	public abstract void msgOutOfFood(Order o);

	public abstract void msgOrderReady(Order o);

	public abstract void msgPleaseComeHere(Customer customerAgent);

	public abstract void msgCheckPlease(Customer customerAgent);

	public abstract void msgHereIsCheck(Check c);

	public abstract void msgLeavingTable(Customer customer);

	public abstract void WantsToGoOnBreak();

	public abstract void goOffBreak();

	public abstract boolean isWantingToGoOnBreak();

	public abstract boolean isOnBreak();

	public abstract void setGui(WaiterGui gui);

	public abstract WaiterGui getGui();

	public abstract boolean isAvailable();

	public abstract MyCust findCust(Customer cust);

	public abstract void setCashier(Cashier c);

	public abstract int numberOfCustomersBeingServed();

	public abstract void pauseCustomers();

	public abstract void restartCustomers();

	public abstract void setBreak(boolean b);

	public abstract void msgAnimationFinishedGoToHomePosition();

}