package restaurant_wc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
//import java.util.List;
//import java.util.Timer;
import java.util.TimerTask;   
import java.util.Map;   


import java.util.concurrent.Semaphore;













import restaurant_wc.gui.CookGui;
//import restaurant_wc.CustomerAgent.AgentEvent;
//aurant.WaiterAgent.AgentState;
import agent.Agent;

public class CookAgent extends Agent{
	//variables
	public Collection<Order> pendingOrders = Collections.synchronizedList(new ArrayList<Order>());
	public List<MarketAgent> Markets = Collections.synchronizedList(new ArrayList<MarketAgent>());
	public List<MarketOrder> pendingMOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
	private int defaultLevel = 5;
	private int MaxLevel = 5;
	private int needFoodLevel = 2;
	private CookGui cookGui;
//	private Semaphore Waiting = new Semaphore(0,true);
	public enum OrderState 
	{pending,cooking,cooked, tempNull	};
	Food SteakDish;
	Food SaladDish;
	Food ChickenDish;
	Food PizzaDish;
	String name;
	public List<Food> FoodDishes = Collections.synchronizedList(new ArrayList<Food>());
	Map<String, Food> FoodTypes = new HashMap<String, Food>();
	boolean RequestAgain = true;
	int tempLevel = 0;
	private Semaphore moving = new Semaphore(0, true);
	private Semaphore plating = new Semaphore(0,true);
	
	public CookAgent(String name){
		super();
		this.name = name;
		SteakDish= new Food("Steak", 5000, defaultLevel);
		SaladDish = new Food("Salad", 2000, defaultLevel);
		ChickenDish = new Food("Chicken", 7000, defaultLevel);
		PizzaDish = new Food ("Pizza", 4000, defaultLevel);
		FoodDishes.add(SteakDish);
		FoodDishes.add(SaladDish);
		FoodDishes.add(ChickenDish);
		FoodDishes.add(PizzaDish);
		FoodTypes.put("Steak", SteakDish);
		FoodTypes.put("Salad", SaladDish);
		FoodTypes.put("Chicken", ChickenDish);
		FoodTypes.put("Pizza", PizzaDish);
		/*Markets.add(new MarketAgent("First", 0));
		Markets.add(new MarketAgent("Second", 1));
		Markets.add(new MarketAgent("Third", 2));*/
		
		
	}
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	public void addMarket(MarketAgent m){
		Markets.add(m);
		//m.setCook(this);
	}
	
	public void setGui(CookGui cookGui) {
		this.cookGui = cookGui;
		
	}
	
	protected boolean pickAndExecuteAnAction() {
		synchronized(FoodTypes){
		for(int i = 0; i < FoodTypes.size(); i++)
		{
			if(FoodTypes.get(FoodDishes.get(i).Choice).InventoryLevel <= needFoodLevel){
				if(!(Markets.isEmpty()))
				{
				if(!(FoodTypes.get(FoodDishes.get(i).Choice).BeingOrdered))
				{
					FoodTypes.get(FoodDishes.get(i).Choice).BeingOrdered = true;
					RequestFood(FoodDishes.get(i));
					/*print("Food is Low");
					pendingMOrders.add(new MarketOrder(FoodDishes.get(i).Choice, (MaxLevel - FoodTypes.get(FoodDishes.get(i)).InventoryLevel), 0));
					FoodTypes.get(FoodDishes.get(i).Choice).BeingOrdered = true;
					stateChanged();*/
					return true;
				}
				}
				else 
				{
					print("There are no Markets! Please add Markets.");
				}
			}
		}
		}
		try{
		for(int j = 0; j < pendingMOrders.size();)
		{
			CallMarket(pendingMOrders.get(j).Choice, pendingMOrders.get(j).amt, pendingMOrders.get(j).MarketNum);
			pendingMOrders.remove(pendingMOrders.get(j));
			return true;
		//}
			/*
		for (MarketOrder m :pendingMOrders){
			CallMarket(m.Choice, m.amt, m.MarketNum);
			pendingMOrders.remove(m);
			return true;
		}*/
		}
		}
		catch(ConcurrentModificationException e)
		{
			System.out.println("here is a concurrent modification");
			return true;
		}
		synchronized(pendingOrders){
		for( final Order o : pendingOrders){
			if(o.state == OrderState.pending)
			{
				this.CookOrder(o);
				o.state = OrderState.cooking;
				stateChanged();
				return true;
			}
			if(o.state == OrderState.cooked) {
				o.state = OrderState.tempNull;
				this.DoneCooking(o);
				return true;
				
			}
		}
		}
		return false;
	}



	//messages
	
	public void msgDestinationArrival()
	{
		moving.release();
	}
	
	public void msgDonePlating()
	{
		plating.release();
	}
	public void msgHereIsAnOrder(Order o)
	{
		pendingOrders.add(o);
		print("I have recieved an order");
		stateChanged();
	}
	
	public void msgOrderFulFillment(String choice, int i) {
		// TODO Auto-generated method stub
		if(i != 0)
		{
		print("Recieving Orders!");
		print("Inventory Level now: " + (i +FoodTypes.get(choice).InventoryLevel));
		}
		FoodTypes.get(choice).InventoryLevel += i;
		FoodTypes.get(choice).BeingOrdered = false;
		//Waiting.release();
	}
	
	public void msgPartialFulfillment(String choice, int i,
			int MarketNum) {
		// TODO Auto-generated method stub
		pendingMOrders.add(new MarketOrder(choice, i, MarketNum + 1));
		stateChanged();
		
	}
	
	
	//actions
	
	private void RequestFood(Food food) {
		// TODO Auto-generated method stub
		print("Food is Low");
		pendingMOrders.add(new MarketOrder(food.Choice, (MaxLevel - food.InventoryLevel), 0));
		FoodTypes.get(food.Choice).BeingOrdered = true;
		stateChanged();
		
	}
	
	private void CallMarket(String c, int a, int m) {
		Do("Calling Market.");
		if(m < Markets.size())
		{
		Markets.get(m).msgBuyFood(c, a);
		}
		else
		{
			print("All Markets have been contacted. Unable to get all the necessary foods.");
		}
	}
	
	public void CookOrder(final Order o)
	{
		//DoCooking
	
		if(FoodTypes.get(o.choice).InventoryLevel <= 0 )
		{
			print("I've run out of " + o.choice);
			while(pendingOrders.contains(o)){
				pendingOrders.remove(o);
			}
			//TODO
			o.waiter.msgOutOfChoice(o.choice, o.table.occupiedBy);
			//message Market;
			return;
		}
		
		print("Cooking " + o.choice);
		FoodTypes.get(o.choice).InventoryLevel--;
		cookGui.msgAddToGrill(o.choice);
		try {
			moving.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		o.timer.schedule(new TimerTask() {
			public void run() {
				o.state = OrderState.cooked;
				stateChanged();
				print("Done Cooking");
			}
		},
		FoodTypes.get(o.choice).CookTime);
		/*if(FoodTypes.get(o.choice).InventoryLevel <= needFoodLevel){
			if(!(Markets.isEmpty()))
			{
			if(!(FoodTypes.get(o.choice).BeingOrdered))
			{
				print("Food is Low");
				pendingMOrders.add(new MarketOrder(o.choice, (defaultLevel - FoodTypes.get(o.choice).InventoryLevel), 0));
				FoodTypes.get(o.choice).BeingOrdered = true;
				stateChanged();
			}
			}
			else 
			{
				print("There are no Markets! Please add Markets.");
			}
		}*/
			/*for(MarketAgent m: Markets){
				if(tempLevel < defaultLevel)
				{
					if(RequestAgain){
					RequestAgain = false;
					Do("Requesting Food");
					m.msgBuyFood(o.choice, defaultLevel - needFoodLevel);
					try {
						Waiting.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					}
				}
			}
			RequestAgain = true;
		}*/
	}
	
	public void DoneCooking(final Order o)
	{
		//Remove O from list
		//print ("sending message to waiter");
		cookGui.msgAddToPlating(o.choice);
		try {
			plating.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		o.waiter.msgOrderIsReady(o);

		synchronized(pendingOrders){
		while(pendingOrders.contains(o)){
			pendingOrders.remove(o);
		}
		}
	}
	
	class Food{
		String Choice;
		int CookTime;
		int InventoryLevel;
		boolean BeingOrdered;
		
		public Food(String c, int t, int i)
		{
			Choice = c;
			CookTime = t;
			InventoryLevel = i;
			BeingOrdered = false;
		}
		
	}
	
	class MarketOrder{
		public String Choice;
		public int amt;
		public int MarketNum;
		
		public MarketOrder(String c, int a, int m)
		{
			Choice = c;
			amt = a;
			MarketNum = m;
		}
	}

	//Hack to deplete inventory
	public void depleteInventory() {
		synchronized(FoodDishes){
		for(Food f: FoodDishes)
		{
			f.InventoryLevel = 0;		
		}
		}
		stateChanged();
		
	}


}

