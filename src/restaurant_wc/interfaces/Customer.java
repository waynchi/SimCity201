package restaurant_wc.interfaces;

import java.util.List;

import restaurant_wc.BaseWaiterRole.FoodOnMenu;
import restaurant_wc.gui.CustomerGui;
import restaurant.interfaces.Cashier;

public interface Customer {
	public abstract void gotHungry();

	public abstract void msgYouNeedToWait();
	
	// handles waiter follow me message and eventually sits down at the correct table
	public abstract void msgSitAtTable(Waiter waiter, int tableNumber, List<FoodOnMenu> m);

	// from animation, when customer has arrived at the table
	public abstract void msgAtTable();
	
	public abstract void msgAtCashier();
	
	//from animation, when customer has made the choice on pop up list
	public abstract void msgAnimationChoiceMade();
	
	//from waiter agent
	public abstract void msgWhatWouldYouLike();
	
	//from waiter agent
	public abstract void msgWhatElseWouldYouLike(List<FoodOnMenu> newMenu);
	
	//from waiter agent
	public abstract void msgHereIsYourFood();
	
	public abstract void msgHereIsYourTotal (Double d, Cashier cashier);
	
	public abstract void msgHereIsYourChange (Double change);

	//from animation

	public abstract String getChoice();

	public abstract CustomerGui getGui();

	public abstract String getName();

	public abstract void msgAtExit();

	public abstract String getState();
}
