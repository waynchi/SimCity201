package restaurant_vk.interfaces;

import restaurant_vk.CookAgent.Order;

public interface Cook {
	public void hereIsOrder(Waiter w, String choice, int table);
	
	public void foodCooked(Order o);
	
	public void dontHaveIt(String food, int mNum);
	
	public void hereAreMaterials(String food, int qty, int marketNum);
}
