package restaurant_vk.interfaces;

import java.util.List;

public interface Host {
	public void IWantToEat(Customer cust);
	
	public void tableIsFree(int table);
	
	public void wantABreak(Waiter w);
	
	public void ImBackToWork(Waiter w);
	
	public void addWaiter(Waiter w);
	
	public void ICantWait(Customer c);
	
	public void customerIsLeavingWithoutEating(Customer c, int table);
	
	public List<Customer> getCustomers();
	
	public List<Waiter> getWaiters();
	
	public boolean pickAndExecuteAnAction();
}
