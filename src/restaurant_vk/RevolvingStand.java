package restaurant_vk;

import java.util.ArrayList;
import java.util.List;
import restaurant_vk.VkCookRole.Order;
import restaurant_vk.interfaces.Waiter;

public class RevolvingStand {
	private List<Order> orders = new ArrayList<Order>();
	private VkCookRole cook = new VkCookRole(this);

	synchronized public void addOrder(Waiter waiter, String food, int table) {
		orders.add(cook.new Order(waiter, food, table));
	}

	synchronized public Order removeOrder() {
		Order temp;
		temp = orders.get(0);
		orders.remove(0);
		return temp;
	}

	public int size() {
		return orders.size();
	}
}
