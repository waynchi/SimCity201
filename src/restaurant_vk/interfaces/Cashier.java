package restaurant_vk.interfaces;

import java.util.List;
import restaurant_vk.CustomerRestaurantCheck;

public interface Cashier {
	public void computeBill(Customer cust, String choice, Waiter w);
	
	public void hereIsPayment(CustomerRestaurantCheck c, double cash, List<CustomerRestaurantCheck> l);
	
	public void hereIsBillForMaterials(Market m, double cost);
}
