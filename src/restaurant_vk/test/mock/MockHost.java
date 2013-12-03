package restaurant_vk.test.mock;

import java.util.List;
import restaurant_vk.interfaces.Customer;
import restaurant_vk.interfaces.Host;
import restaurant_vk.interfaces.Waiter;

public class MockHost extends Mock implements Host{

	public MockHost(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void IWantToEat(Customer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tableIsFree(int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wantABreak(Waiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ImBackToWork(Waiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addWaiter(Waiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ICantWait(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void customerIsLeavingWithoutEating(Customer c, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Customer> getCustomers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Waiter> getWaiters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

}
