package restaurant_ps.interfaces;

import restaurant.interfaces.Cashier;
import restaurant_ps.Check;
import restaurant_ps.Choice;
import restaurant_ps.HostAgent;
import restaurant_ps.Menu;
import restaurant_ps.gui.CustomerGui;

public interface Customer {

	/**
	 * hack to establish connection to Host agent.
	 */
	public abstract void setHost(HostAgent host);

	public abstract void setWaiter(Waiter waiter);

	public abstract String getCustomerName();

	public abstract void gotHungry();

	public abstract void msgAllTablesAreOccupied();

	public abstract void msgAnimationFinishedGoToHost();

	public abstract void msgFollowMe(Menu m, int table);

	public abstract void msgWhatWouldYouLikeToOrder();

	public abstract void msgOutOfFoodPleaseReOrder(Menu menu);

	public abstract void msgHereIsYourFood(Choice o);

	public abstract void msgWhatDoYouNeed();

	public abstract void msgHereIsYourCheck(Check bill);

	public abstract void msgPayNextTime();

	public abstract void msgAcceptedPayment();

	public abstract void msgAnimationFinishedGoToSeat();

	public abstract void msgAnimationFinishedGoToCashier();

	public abstract void msgAnimationFinishedLeaveRestaurant();

	public abstract String getName();

	public abstract int getHungerLevel();

	public abstract void setHungerLevel(int hungerLevel);

	public abstract String toString();

	public abstract void setGui(CustomerGui g);

	public abstract CustomerGui getGui();

	public abstract void setCashier(Cashier c);

	public abstract Waiter getWaiter();

	public abstract int getMoney();

	public abstract void msgAnimationFinishedGoToWaiting();

	public abstract void restartAgent();

	public abstract void pauseAgent();

	

}