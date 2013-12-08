package restaurant_ps;

import java.util.List;

import restaurant.interfaces.Cook;
import restaurant_ps.interfaces.Host;
import restaurant_ps.interfaces.Waiter;

public class WaiterNormalAgent extends WaiterAgent implements Waiter {

	public WaiterNormalAgent(String name, Cook cook, Host host,
			List<Food> inventory) {
		super(name, cook, host, inventory);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	protected void HandleOrder(Choice o,Table table) {
		// TODO Auto-generated method stub
		Do("Giving order to the cook"+table.tableNumber);
		
		((CookAgent) cook).msgHereIsOrder(new Order(this,o,table));
	}

}
