package restaurant.test.mock;

import java.util.Map;

import people.People;
import restaurant.gui.CookGui;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Waiter;

public class MockCook extends Mock implements Cook{
	
	public MockCook(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public People getPerson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgHereIsYourOrder(Map<String, Integer> items) {
		log.add(new LoggedEvent("received msgHereIsYourOrder from market"));		
	}

	@Override
	public CookGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgHereIsAnOrder(String choice, Waiter normalWaiterRole, int tableNumber) {
		log.add(new LoggedEvent("received msgHereIsAnOrder from waiter " + normalWaiterRole.getName()));		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
}
