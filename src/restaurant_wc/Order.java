package restaurant_wc;

import java.util.Timer;

import restaurant_wc.CookAgent.OrderState;
import restaurant_wc.WaiterAgent.OrderWaiter;
import restaurant_wc.interfaces.Waiter;

public class Order {
		Waiter waiter;
		String choice;
		Table table;
		Timer timer = new Timer();
		
		public OrderState state = OrderState.pending;
		public OrderWaiter waiterState = OrderWaiter.pending;
		
		Order(Waiter waiter, String Choice, Table t)
		{
			this.waiter = waiter;
			this.choice = Choice;
			this.table = t;
		}
		
	}
