package restaurant;

import agent.Agent;
import restaurant.BaseWaiterRole;
import restaurant.gui.HostGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Market;
import restaurant.interfaces.Waiter;

import java.awt.Dimension;
import java.util.*;
import java.util.concurrent.Semaphore;

import people.Role;

/**
 * Restaurant Market Agent
 */
// When cook agent is low of some food, he orders it from the market. Market should fulfill the order as 
// best as it can
// Market and Cook pass each other maps that are not semantic enough, will be changed to lists if there's time


public class MarketRole extends Role implements Market{


	/*private String name;
	private boolean isActive = false;
	//private CookAgent cook;
	private class MyOrder {
		CookRole cook;
		Cashier cashier;
		Map<String, Integer> order = Collections.synchronizedMap(new HashMap<String,Integer>());

		private MyOrder(Map<String, Integer> o, CookRole c, Cashier ca){
			order = o;
			cook = c;
			cashier = ca;
		}
	}
	List<MyOrder> orders =Collections.synchronizedList( new ArrayList<MyOrder>());

	private class Food {
		String type;
		int inventory;
		double price;

		private Food(String t, int i, double p) {
			type = t;
			inventory = i;
			price = p;
		}
	}
	private Map<String, Food> myFood = Collections.synchronizedMap(new HashMap<String, Food>());
	private Map<Cashier, Double> cashierBalance = Collections.synchronizedMap(new HashMap<Cashier, Double>());



	public MarketRole(String name) {
		super();

		myFood.put("Steak", new Food("Steak", 1,7.99));
		myFood.put("Chicken", new Food("Chicken", 1, 5.99));
		myFood.put("Salad", new Food("Salad", 1, 2.99));
		myFood.put("Pizza", new Food("Pizza", 2, 3.99));

		this.name = name;

	}


	// Messages
	public void msgIsActive() {
		isActive = true;
		getPersonAgent().CallstateChanged();

	}
	
	public void msgIsInActive() {
		isActive = false;
		getPersonAgent().CallstateChanged();

	}
	
	public void msgOrder (Map<String, Integer> orderList, CookRole c, Cashier ca) {
		orders.add(new MyOrder(orderList, c, ca));
		stateChanged();
	}

	public void msgPayMarketBill (double amount, Cashier ca) {
		cashierBalance.put(ca, cashierBalance.get(ca)-amount);
		if (cashierBalance.get(ca) == 0.0) print ("Thanks for paying! Welcome next time!");
		else print("You still need to pay " +String.format("%.2f",cashierBalance.get(ca),2) + ". Please pay next time!");
		getPersonAgent().CallstateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 
	public boolean pickAndExecuteAnAction() {

		if (!orders.isEmpty()) {
			fulfillOrder(orders.get(0));
			//isOrder = false;
			return true;
		}


		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	public void fulfillOrder (MyOrder o) {
		Map<String,Integer> supplyList =Collections.synchronizedMap( new HashMap<String,Integer>());
		synchronized(o.order){
			for (Map.Entry<String, Integer> entry : o.order.entrySet()) {
				int orderAmount = entry.getValue();
				int inventoryAmount = myFood.get(entry.getKey()).inventory;
				if (orderAmount <= inventoryAmount) {
					supplyList.put(entry.getKey(),orderAmount);
					myFood.get(entry.getKey()).inventory = inventoryAmount - orderAmount;
					print ("We can fulfill the order for "+entry.getKey());
				}
				else {
					supplyList.put(entry.getKey(), inventoryAmount);
					myFood.get(entry.getKey()).inventory = 0;
					print ("Sorry we can't fulfill the order for "+entry.getKey());
				}
			}
		}

		double marketBill = 0;
		synchronized(supplyList){
			for (Map.Entry<String,Integer> entry: supplyList.entrySet()){
				marketBill += entry.getValue() * (myFood.get(entry.getKey()).price);
			}
		}
		if (cashierBalance.containsKey(o.cashier)) {
			cashierBalance.put(o.cashier, cashierBalance.get(o.cashier)+marketBill);
		}
		else {cashierBalance.put(o.cashier, marketBill);}
		
		o.cook.msgSupply(o.order,supplyList);
		if (cashierBalance.get(o.cashier)!=0.0)		o.cashier.msgHereIsMarketBill(this, cashierBalance.get(o.cashier));
		orders.remove(o);

	}


	//utilities


	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	public boolean isActive() {
		return isActive;
	}
	*/

}

