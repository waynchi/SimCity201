package restaurant.test.mock;

import java.util.Map;

import people.People;
import restaurant.gui.CookGui;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Host;
import restaurant.interfaces.Waiter;

public class MockCook extends Mock implements Cook{
	
	int restaurantIndex;
	
	public void setRestaurantIndex(int i) {
		restaurantIndex = i;
	}
	
	public MockCook(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public People getPerson() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public CookGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	public void msgHereIsAnOrder(String choice, Waiter normalWaiterRole, int tableNumber) {
		log.add(new LoggedEvent("received msgHereIsAnOrder from waiter " + normalWaiterRole.getName()));		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	public void setHost(Host host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourOrder(Map<String, Integer> items, int orderNumber) {
		
	}

	@Override
	public void msgHereIsYourOrderNumber(Map<String, Integer> items,int orderNumber) {
		log.add(new LoggedEvent("order confirmed, get order number " + orderNumber));
		
	}

	@Override
	public int getRestaurantIndex() {
		// TODO Auto-generated method stub
		return restaurantIndex;
	}
}
