package restaurant.test.mock;

import java.util.Map;

import people.People;
import people.Role;
import restaurant.gui.CookGui;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Waiter;

public class MockCook extends Role implements Cook{
	
	public MockCook(String name) {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public People getPerson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgHereIsYourOrder(Map<String, Integer> items) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CookGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgHereIsAnOrder(String choice, Waiter normalWaiterRole,
			int tableNumber) {
		// TODO Auto-generated method stub
		
	}


}
