package restaurant_vk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import restaurant_vk.interfaces.Cashier;
import restaurant_vk.interfaces.Cook;
import restaurant_vk.interfaces.Market;
import agent.Agent;

/**
 * @author Vikrant Singhal
 * 
 * A class representing the Market. The market delivers food items to the
 * cook when asked for something. But when it does not have as many items
 * as the cook requested, it sends all the items of the type requested. When
 * it has zero items of the type requested, it simply informs the cook that
 * the item is not available.
 */
public class MarketAgent extends Agent implements Market{

	// Data
	
	private Cook cook = null;
	private Cashier cashier = null;
	private List<Food> inventory = Collections.synchronizedList(new ArrayList<Food>());
	private List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	private int marketNumber = 0;
	private double myCash = 0.0;
	
	public MarketAgent(int n) {
		marketNumber = n;
		setInventory();
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
	
	// Messages
	
	/*
	 * A message called by the cook when he is running low on a certain
	 * food item.
	 */
	public void runningLow(String food, int qty) {
		Order o = new Order(food, qty);
		synchronized (orders) {
			orders.add(o);
		}
		stateChanged();
	}
	
	/*
	 * A message called by the cashier to pay the bills.
	 */
	public void hereIsMoney(double cash) {
		myCash += cash;
		stateChanged();
	}
		
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
		
	// Actions
	
	/*
	 * Action to decide what is to be done with a pending order.
	 */
	private void decide(Order o) {
		Food f = fFind(o.name);
		if (o.qty <= f.current) {
			o.s = OrderState.Supply;
		}
		else if (f.current != 0) {
			o.s = OrderState.Supply;
		}
		else {
			cook.dontHaveIt(o.name, marketNumber);
			print("Cook, I don't have " + o.name + ".");
			o.s = OrderState.Unavailable;
		}
	}
	
	/*
	 * Action to supply an order.
	 */
	private void supply(Order o) {
		Food f = fFind(o.name);
		if (o.qty <= f.current) {
			cook.hereAreMaterials(o.name, o.qty, marketNumber);
			cashier.hereIsBillForMaterials(this, (o.qty * f.unitCost));
			f.current = f.current - o.qty;
		}
		else if (f.current != 0) {
			cook.hereAreMaterials(o.name, f.current, marketNumber);
			cashier.hereIsBillForMaterials(this, (f.current * f.unitCost));
			f.current = 0;
		}
		o.s = OrderState.Done;
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
		
	// Scheduler
	
	@Override
	protected boolean pickAndExecuteAnAction() {
		Order o = null;
		
		// If there is a pending order, take a decision about it. Decide whether it
		// is available or not.
		o = oFind(OrderState.Pending);
		if (o != null) {
			decide(o);
			return true;
		}
		
		// If there is an order that can be supplied, supply it.
		o = oFind(OrderState.Supply);
		if (o != null) {
			supply(o);
			return true;
		}
		
		return false;
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
	
	// Utilities
	
	public void setCook(Cook c) {
		this.cook = c;
	}
	
	private void setInventory() {
		Food f1 = new Food("Steak", 4.0);
		Food f2 = new Food("Chicken", 30.0);
		Food f3 = new Food("Salad", 1.0);
		Food f4 = new Food("Pizza", 2.0);
		inventory.add(f1);
		inventory.add(f2);
		inventory.add(f3);
		inventory.add(f4);
	}
	
	private Order oFind(OrderState st) {
		synchronized (orders) {
			for (Order o : orders) {
				if (o.s == st) {
					return o;
				}
			}
		}
		return null;
	}
	
	private Food fFind(String choice) {
		for (Food f : inventory) {
			if (f.name.equals(choice)) {
				return f;
			}
		}
		return null;
	}
	
	public void setInitialFoodQuantities(int st, int ch, int sa, int pi) {
		inventory.get(0).current = st;
		inventory.get(1).current = ch;
		inventory.get(2).current = sa;
		inventory.get(3).current = pi;
	}
	
	public String toString() {
		return "Market #" + marketNumber;
	}
	
	public void setCashier(Cashier c) {
		cashier = c;
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
	
	// Helper Data Structures
	
	private enum OrderState {Pending, Done, Supply, Unavailable};
	
	private class Food {
		String name;
		int current;
		double unitCost;
		
		public Food(String name, double cost) {
			this.name = name;
			this.current = 0;
			unitCost = cost;
		}
	}
	
	private class Order {
		String name;
		OrderState s;
		int qty;
		
		public Order(String name, int qty) {
			this.name = name;
			this.qty = qty;
			s = OrderState.Pending;
		}
	}
}