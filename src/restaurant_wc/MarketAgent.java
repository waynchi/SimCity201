package restaurant_wc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
//import java.util.List;
//import java.util.Timer;
import java.util.TimerTask;   
import java.util.Map;   











import restaurant_wc.CookAgent.Food;
import restaurant_wc.CustomerAgent.AgentEvent;
import restaurant_wc.interfaces.Cashier;
import restaurant_wc.interfaces.Market;
//import restaurant_wc.CustomerAgent.AgentEvent;
//aurant.WaiterAgent.AgentState;
import agent.Agent;

public class MarketAgent extends Agent implements Market{
	//variables
	private List<Request> Orders = Collections.synchronizedList(new ArrayList<Request>());
	Food SteakDish;
	Food SaladDish;
	Food ChickenDish;
	Food PizzaDish;
	private CookAgent cook;  
	private Cashier cashier;
	private MarketAgent market = this ;
	private String name;
	Map<String, Food> FoodTypes = new HashMap<String, Food>();
	Timer timer = new Timer();
	int MarketNumber;
	int LoadTime = 30000;
	
	public MarketAgent(String n, int m){
		super();
		name = n;
		MarketNumber = m;
		SteakDish= new Food("Steak",10, 8);
		SaladDish = new Food("Salad", 10, 2);
		ChickenDish = new Food("Chicken", 10,5);
		PizzaDish = new Food ("Pizza", 10, 3);
		
		FoodTypes.put("Steak", SteakDish);
		FoodTypes.put("Salad", SaladDish);
		FoodTypes.put("Chicken", ChickenDish);
		FoodTypes.put("Pizza", PizzaDish);
		
		//hack to edit LoadTime
		if(n.equalsIgnoreCase("20000"))
		{
			LoadTime = 20000;
		}
		if(n.equalsIgnoreCase("10000"))
		{
			LoadTime = 10000;
		}
		if(n.equalsIgnoreCase("40000"))
		{
			LoadTime = 40000;
		}
		if(n.equalsIgnoreCase("50000"))
		{
			LoadTime = 50000;
		}
		if(n.contains("three"))
		{
			LoadTime = 10000;
			SteakDish.InventoryLevel = 3;
			SaladDish.InventoryLevel = 3;
			PizzaDish.InventoryLevel = 3;
			ChickenDish.InventoryLevel = 3;
		}
		
	}
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see restaurant_wc.Market#setCook(restaurant_wc.CookAgent)
	 */
	@Override
	public void setCook(CookAgent c){
		cook = c;
	}
	
	/* (non-Javadoc)
	 * @see restaurant_wc.Market#setCashier(restaurant_wc.interfaces.Cashier)
	 */
	@Override
	public void setCashier(Cashier c){
		cashier = c;
	}
	
	protected boolean pickAndExecuteAnAction() {
		if(!(Orders.isEmpty())){
			synchronized(Orders){
			for(Request r : Orders){
				Fulfill(r);
				return true;
			}
			}
		}
		return false;
	}
	
	//messages
	/* (non-Javadoc)
	 * @see restaurant_wc.Market#msgBuyFood(java.lang.String, int)
	 */
	@Override
	public void msgBuyFood(String choice, int i) {
		print("I have received a request");
		Orders.add(new Request(choice, i));
		stateChanged();
	}
	
	
	//actions
	
	private void Fulfill(final Request r) {
		if(FoodTypes.get(r.Choice).InventoryLevel >= r.amt){
			print("Order Can Be Fully Fulfilled");
			FoodTypes.get(r.Choice).InventoryLevel -= r.amt;
			Orders.remove(r);
			timer.schedule(new TimerTask() {
				public void run() {
					Do("Order Fulfilled");
					cook.msgOrderFulFillment(r.Choice,r.amt);
					cashier.msgHereIsMarketBill(new MBill(r.Choice, r.amt, FoodTypes.get(r.Choice).Cost), market);
				}
			},
			LoadTime);
		}
		else{
			print("Order can be Partially Fulfilled or not Fulfilled");
			cook.msgPartialFulfillment(r.Choice,( r.amt - FoodTypes.get(r.Choice).InventoryLevel), this.MarketNumber);
			final int Inventory = FoodTypes.get(r.Choice).InventoryLevel;
			FoodTypes.get(r.Choice).InventoryLevel = 0;
			Orders.remove(r);	
			timer.schedule(new TimerTask() {
				public void run() {
					Do("Order partially fulfilled or not fulfilled.");
					cook.msgOrderFulFillment(r.Choice, Inventory);
					if(Inventory != 0)
					{
						cashier.msgHereIsMarketBill(new MBill(r.Choice, Inventory, FoodTypes.get(r.Choice).Cost), market);
					}
				}
			},
			LoadTime);
		}	
	}

	
	private class Food{
		String Choice;
		int InventoryLevel;
		int Cost;
		
		public Food(String c, int i, int cost)
		{
			Choice = c;
			InventoryLevel = i;
			Cost = cost;			
		}	
	}
	
	private class Request{
		String Choice;
		int amt;
		
		public Request(String c, int i){
			Choice = c;
			amt = i;
		}
	}

	/* (non-Javadoc)
	 * @see restaurant_wc.Market#msgBillPayment(double)
	 */
	@Override
	public void msgBillPayment(double payment) {
		// TODO Auto-generated method stub
		print("I have recieved Payment of " + payment + " from the cashier");
		
	}
}

