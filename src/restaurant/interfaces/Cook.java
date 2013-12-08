package restaurant.interfaces;

import java.util.Map;

public interface Cook {
	public abstract void msgHereIsYourOrder(Map<String, Integer> items, int orderNumber);

	public abstract void msgHereIsYourOrderNumber(Map<String, Integer> items, int orderNumber);

	public abstract int getRestaurantIndex();

	public abstract String getName();
}
