package restaurant_ps;

import java.util.ArrayList;
import java.util.List;
import restaurant_ps.Order;

import restaurant_ps.interfaces.Waiter;

public class RevolvingStand {
	private List<Order> orders = new ArrayList<Order>();
	private CookAgent cook;

	synchronized public void addOrder(Waiter waiter, Choice o, Table table) {
		orders.add(new Order(waiter, o, table));
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
