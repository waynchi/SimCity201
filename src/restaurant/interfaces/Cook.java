package restaurant.interfaces;

import java.util.Map;

import people.People;
import restaurant.gui.CookGui;

public interface Cook {

	abstract People getPerson();

	abstract void msgHereIsYourOrder(Map<String, Integer> items);

	abstract CookGui getGui();

	abstract void msgHereIsAnOrder(String choice,
			Waiter normalWaiterRole, int tableNumber);

}
