package restaurant_ps;

import restaurant_ps.CookAgent.Status;
import restaurant_ps.interfaces.Waiter;


public class Order{
		public Choice o;
		public Waiter waiter;
		public Table table;
		public Status foodStatus;
		
		public Order(Waiter w, Choice o, Table table)
		{
			this.waiter = w;
			this.o = o;
			this.table = table;
			foodStatus = Status.pending;
		}
		
		public Status getFoodStatus()
		{
			return foodStatus;
		}
		
		public void setFoodStatus(Status set)
		{
			foodStatus = set;
		}
		
		public int getCookingTime()
		{
			return o.food.cookingTime;
		}
	};