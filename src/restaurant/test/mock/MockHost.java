package restaurant.test.mock;

import java.util.List;

import people.People;
import people.Role;
import restaurant.BaseWaiterRole;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Host;

public class MockHost extends Role implements Host {

	@Override
	public void setCashier(Cashier cashierRole) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<BaseWaiterRole> getWaiters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cook getCook() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public People getPerson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cashier getCashier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgTableIsFree(int tableNumber) {
		// TODO Auto-generated method stub
		
	}

	

}
