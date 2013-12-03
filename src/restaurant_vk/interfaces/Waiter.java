package restaurant_vk.interfaces;

import restaurant_vk.CustomerRestaurantCheck;

public interface Waiter {
	public void sitAtTable(Customer c, int table);
	
	public void readyToOrder(Customer c);
	
	public void hereIsMyChoice(String item, Customer c);
	
	public void orderIsReady(String choice, int table);
	
	public void doneEatingAndLeaving(Customer c);
	
	public void wantBreak();
	
	public void getBackToWork();
	
	public void noBreak();
	
	public void takeABreak();
	
	public void outOf(String choice, int table);
	
	public void hereIsCheck(CustomerRestaurantCheck ch, Customer c);
	
	public void leavingWithoutEating(Customer c);
}
