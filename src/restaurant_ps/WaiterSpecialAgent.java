package restaurant_ps;

import java.util.List;

import restaurant.interfaces.Cook;
import restaurant_ps.interfaces.Host;
import restaurant_ps.interfaces.Waiter;

public class WaiterSpecialAgent extends WaiterAgent implements Waiter{

	public WaiterSpecialAgent(String name, Cook cook, Host host,
			List<Food> inventory) {
		super(name, cook, host, inventory);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	

	protected void HandleOrder(Choice o,Table table) {
		// TODO Auto-generated method stub
		Do("Adding order to revolving stand");
		super.revolvingStand.addOrder(this, o, table);
	}
}
