package restaurant.interfaces;

import java.util.Map;

import people.People;
import restaurant.gui.CookGui;

public interface Cook {

	public abstract People getPerson();

	public abstract void msgHereIsYourOrder(Map<String, Integer> items);

	public abstract CookGui getGui();

	public abstract void msgHereIsAnOrder(String choice,
			Waiter normalWaiterRole, int tableNumber);

}
