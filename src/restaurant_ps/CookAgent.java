package restaurant_ps;

import agent.Agent;
import restaurant_ps.gui.CookGui;
import restaurant_ps.interfaces.Host;

import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
//import restaurant.interfaces.Market;
import market.interfaces.MarketEmployee;

import java.util.*;
import java.util.Map.Entry;

import people.PeopleAgent;
import people.Role;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class CookAgent extends Role implements Cook {
	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public enum Status {pending, cooking, done, doingNothing, goingToFridge, aboutToCheckForInFridge, arrivedAtFridge, checkingInFridge, readyToMoveToGrill, arrivedAtGrill, onGrill, doneCooking, readyToBePlated, aboutToBePlated, beingPlated, goingToPlateArea, arrivedAtPlatingArea, Plated, tellWaiterFoodIsPlated, hasBeenPickedUp, PlatedAndWaiterNotified, goingToGrill, arrivedAtGrillToPickUpFood, aboutToPutOnGrill,left,personNotified};
	Status cookState = Status.doingNothing;
	public enum RestockState {readyToBeChecked,checking,done};
	public enum ClosingState {None, ToBeClosed, Preparing, Closed};
	Random generator = new Random();
	Timer timer = new Timer();
	private final int period = 5000;
	
	enum MarketOrderState {Requested, Supplied, InformedCashier};

	public class MarketOrder {
		public Map<String, Integer> itemsRequested = new HashMap<String, Integer>();
		public Map<String, Integer> itemsSupplied = new HashMap<String, Integer>();
		public int orderNumber;
		public MarketOrderState s;
		
		public MarketOrder(Map<String, Integer> items) {
			itemsRequested = items;
			s = MarketOrderState.Requested;
		}
	}
	
	class MyOrder {
		Order order;
		Status st;
		MyOrder(Order or)
		{
			order = or;
			st = Status.pending;
		}
	}
	
	class Grill {

		int grillNumber;
		boolean cookingFood;
		MyOrder occupiedBy = null;
		
		Grill(int grillnumber){
			grillNumber = grillnumber;
			cookingFood = false;
		}

		public void setOccupiedBy(MyOrder order) {
			// TODO Auto-generated method stub
			occupiedBy = order;
		}
	}
	
	public List<MyOrder> myOrders = Collections.synchronizedList(new ArrayList<MyOrder>());
	public List<Grill> grills = Collections.synchronizedList(new ArrayList<Grill>());
//	public List<MyFoodsToRestock> myFoodsToRestock = Collections.synchronizedList(new ArrayList<MyFoodsToRestock>());
	public List<MarketOrder> myMarketOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
	public Map<String,Food> foods = new HashMap<String,Food>();
	private Timer standTimer;
	
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented

	private String name;
	public CookGui cookGui = null;
	public MarketEmployee marketEmployee;
	public Cashier cashier;
	public Host host;
	boolean enter = false;
	boolean leave = false;
	public ClosingState closingState = ClosingState.Closed;
	public RevolvingStand revolvingStand;

//	private Market market;
//	public List<MarketAgent> markets = new ArrayList<MarketAgent>();
	
	public CookAgent(String name, List<Food> inventory) {
		super();
		this.name = name;
		for(Food f : inventory)
		{
			foods.put(f.foodname, f);
		}
		grills.add(new Grill(1));
		grills.add(new Grill(2));
		grills.add(new Grill(3));
	}
	
	public void msgIsActive() {
		isActive = true;
		enter = true;
		stateChanged();
	}
	
	public void msgIsInActive() {
		leave = true;
		stateChanged();
	}
	
	public void msgAnimationFinishedEntering() {
		// TODO Auto-generated method stub
		enter = false;
	}

	public void msgAnimationFinishedLeaveRestaurant() {
		// TODO Auto-generated method stub
		cookState = Status.left;
		stateChanged();
	}
	

	/* (non-Javadoc)
	 * @see restaurant.Cook#getMaitreDName()
	 */
//	@Override
//	public String getMaitreDName() {
//		return name;
//	}
//
//	/* (non-Javadoc)
//	 * @see restaurant.Cook#getName()
//	 */
//	@Override
//	public String getName() {
//		return name;
//	}

	
	// Messages

	
	/* (non-Javadoc)
	 * @see restaurant.Cook#msgHereIsOrder(restaurant.Order)
	 */
	@Override
	public void msgHereIsYourOrder(Map<String, Integer> items, int orderNumber) {
		// TODO Auto-generated method stub
		MarketOrder o = null;
		for(MarketOrder order : myMarketOrders){
			if(order.itemsRequested == items && order.orderNumber == orderNumber)
			{
				o = order;
				break;
			}
		}
		o.itemsSupplied = items;
		o.s = MarketOrderState.Supplied;
		stateChanged();
	}


	@Override
	public void msgHereIsYourOrderNumber(Map<String, Integer> items,
			int orderNumber) {
		// TODO Auto-generated method stub
		for(MarketOrder order : myMarketOrders){
			if(order.itemsRequested == items)
			{
				order.orderNumber = orderNumber;
				break;
			}
		}
	}
	
	public void msgHereIsOrder(Order o) {
		// TODO Auto-generated method stub
		Do("Recieved order: " + o.o.food.foodname + " from table " + o.table.tableNumber);
		myOrders.add(new MyOrder(o));
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Cook#msgOrderWasFulfilled()
	 */
	
//	public void msgOrderWasFulfilled(Food f) {
//		// TODO Auto-generated method stub
//		Do("There are now " + foods.get(f.foodname).amount + f.foodname + "s");
//		//stateChanged();
//	}
	
	/* (non-Javadoc)
	 * @see restaurant.Cook#msgOrderCannotBeFulfilled(restaurant.MarketAgent, restaurant.Food)
	 */
	
//	public void msgOrderCannotBeFulfilled(MarketAgent m,Food f) {
//		// TODO Auto-generated method stub
//		Do("Order for more " + f.foodname + " from " + m.getName() + " could not be fulfilled");
//		MyFoodsToRestock r = findRestockOrder(f);
//		r.marketsChecked.put(m, true);
//		r.state = RestockState.readyToBeChecked;
//		stateChanged();
//	}

	
	public void msgPickedUpFood(Choice c) {
		// TODO Auto-generated method stub
		Do("Recieved msg that " + c.food.foodname + " has been picked up");
		
		MyOrder mo = findOrderThatWasPlated(c);
		mo.st = Status.hasBeenPickedUp;
		
		stateChanged();
	}

	

	
	public void msgAnimationFinishedGoToFridge() {
		cookState = Status.arrivedAtFridge;
		stateChanged();
	}

	
	public void msgAnimationFinishedGoToGrill(int grillNumber, Order o) {
		// TODO Auto-generated method stub
		Do("Arrived at grill number " + grillNumber + " for " + o.o.food.foodname);
		this.findMyOrder(o).st = Status.aboutToPutOnGrill;
		cookState = Status.arrivedAtGrill;
		stateChanged();
	}
	
	
	public void msgAnimationFinishedGoToGrillToPickUpFood(Order o) {
		// TODO Auto-generated method stub
		Do("Arrived at the grill where the cooked " + o.o.food.foodname + " is");
		cookState = Status.arrivedAtGrillToPickUpFood;
		findMyOrder(o).st = Status.aboutToBePlated;
		stateChanged();
	}

	
	public void msgAnimationFinishedGoToPlatingArea(Order o) {
		// TODO Auto-generated method stub
		cookState = Status.tellWaiterFoodIsPlated;
		findMyOrder(o).st = Status.Plated;
		stateChanged();
		
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		if(cookState == Status.left){
			cookState = Status.personNotified;
			NotifyPerson();
		}
		if (enter == true) {
			enterRestaurant();
			return true;
		}
		
		if (closingState == ClosingState.ToBeClosed) {
			prepareToClose();
			return true;
		}
		
		if (closingState == ClosingState.Preparing && !((HostAgent) host).anyCustomer() && leave == true) {
			shutDown();
			leaveRestaurant();
			return true;
		}
		
		if (leave == true && closingState == ClosingState.None) {
			leaveRestaurant();
			return true;
		}
		synchronized(myOrders)
		{
		for(MyOrder o : myOrders)
		{
			if(o.st == Status.doneCooking && cookState == Status.doingNothing)
			{
				o.st = Status.readyToBePlated;
				GoToGrill(o);
				return true;
			}
		}
		
		for(MyOrder o : myOrders)
			{
				if(o.st == Status.pending && cookState == Status.doingNothing)
				{
					cookState = Status.goingToFridge;
					o.st = Status.aboutToCheckForInFridge;
					//TryAndCookOrder(o);
					GoToFridge();
					return true;
				}
			}
		
		for(MyOrder o : myOrders)
		{
			if(o.st == Status.aboutToCheckForInFridge && cookState == Status.arrivedAtFridge)
			{
				o.st = Status.checkingInFridge;
				CheckFridge(o);
				return true;
			}
		}
		
		
		for(MyOrder o : myOrders)
		{
			if(o.st == Status.aboutToPutOnGrill && cookState == Status.arrivedAtGrill)
			{
				o.st = Status.onGrill;
				CookOrder(o);
				return true;
			}
		}
		
		for(MyOrder o : myOrders)
		{
			if(o.st == Status.aboutToBePlated && cookState == Status.arrivedAtGrillToPickUpFood)
			{
				o.st = Status.beingPlated;
				GoPlateThis(o);
				return true;
			}
		}
		
		
		
		
		for(MyOrder o : myOrders)
		{
			if(o.st == Status.Plated && cookState == Status.tellWaiterFoodIsPlated)
			{
				o.st = Status.PlatedAndWaiterNotified;
				TellWaiterFoodIsReady(o);
				return true;
			}
		}
		
		for(MyOrder o : myOrders)
		{
			if(o.st == Status.hasBeenPickedUp)
			{
				o.st = Status.done;
				RemoveOrder(o);
				return true;
			}
		}
		}
		
	
		synchronized(myMarketOrders)
		{
		for(MarketOrder order : myMarketOrders)
		{
			if(order.s == MarketOrderState.Supplied)
			{
				order.s = MarketOrderState.InformedCashier;
				InformCashier(order);
				return true;
			}
		}
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

//	private void seatCustomer(CustomerAgent customer, Table table) {
//		customer.msgSitAtTable(table.tableNumber);
//		DoSeatCustomer(customer, table);
//		
//		try {
//			atTable.acquire();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		table.setOccupant(customer);
//		waitingCustomers.remove(customer);
//		hostGui.DoLeaveCustomer();
//	} f

	private void shutDown() {
		// TODO Auto-generated method stub
		standTimer.cancel();
		closingState = ClosingState.Closed;
	}

	private void NotifyPerson() {
		// TODO Auto-generated method stub
		isActive = false;
		leave = false;
		myPerson.msgDone("Cook");
	}

	private void leaveRestaurant() {
		// TODO Auto-generated method stub
		((CashierAgent) cashier).recordShift((PeopleAgent)myPerson, "Cook");
		cookGui.DoLeaveRestaurant();
//		try {
//			movingAround.acquire();
//		} catch (InterruptedException e) {}
//		isActive = false;
//		leave = false;
//		myPerson.msgDone("Cook");
	}

	public void closeRestaurant() {
		closingState = ClosingState.ToBeClosed;
		stateChanged();
	}
	
	private void prepareToClose() {
		// TODO Auto-generated method stub
		closingState = ClosingState.Preparing;
	}

	private void enterRestaurant() {
		// TODO Auto-generated method stub
		if (closingState == ClosingState.Closed) {
			startStandTimer();
			closingState = ClosingState.None;
		}
		cookGui.DoEnterRestaurant();
//		try {
//			movingAround.acquire();
//		} catch (InterruptedException e) {}
		//enter = false;
	}

	private void startStandTimer() {
		// TODO Auto-generated method stub
		standTimer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				checkStand();
			}
		}, period, period);
	}

	protected void checkStand() {
		// TODO Auto-generated method stub
		if (revolvingStand.size() > 0) {
			synchronized(myOrders) {
				myOrders.add(new MyOrder(revolvingStand.removeOrder()));
			}
			stateChanged();
		}
	}

	private void InformCashier(MarketOrder order) {
		// TODO Auto-generated method stub
		this.cashier.msgGotMarketOrder(order.itemsSupplied, order.orderNumber);
	}


	private void RemoveOrder(MyOrder o) {
		// TODO Auto-generated method stub
		cookGui.msgDoRemoveOrderFromPlatingArea(o.order);
		myOrders.remove(o);
	}


	private void TellWaiterFoodIsReady(MyOrder o) {
		// TODO Auto-generated method stub
		o.order.waiter.msgOrderReady(o.order);
		cookState = Status.doingNothing;
	}


	

	private void GoPlateThis(MyOrder o) {
		// TODO Auto-generated method stub
		Do("Taking order of " + o.order.o.food.foodname + " to be plated");
		Grill g = findGrillOccupiedBy(o);
		g.setOccupiedBy(null);
		cookGui.DoGoToPlateAreaWithFood(o.order);
	}


	private void GoToGrill(MyOrder o) {
		// TODO Auto-generated method stub
		Do("Going to grill to picked up the cooked " + o.order.o.food.foodname);
		cookState = Status.goingToGrill;
		Grill g = findGrillOccupiedBy(o);
		cookGui.DoGoToGrillToPickUpFood(g.grillNumber,o.order);
	}


	


	private Grill findGrillOccupiedBy(MyOrder order) {
		// TODO Auto-generated method stub
		for(Grill g : grills)
		{
			if(g.occupiedBy == order)
				return g;
		}
		return null;
	}


	private void CheckFridge(MyOrder o) {
		// TODO Auto-generated method stub
		Do("Checking Fridge for " + o.order.o.food.foodname);
		Food f = foods.get(o.order.o.food.foodname);
		if(f.amount == 0)
		{
			//do out of food stuff
			//System.out.println("Out of " + f.foodname + "!!!!!! Reordering...");
			OutOfFood(o.order);
			return;
		}
		Grill g = this.availableGrill();
		g.setOccupiedBy(o);
		cookGui.DoGoToGrill(g.grillNumber,o.order);
	}


	private void GoToFridge() {
		// TODO Auto-generated method stub
		cookGui.DoGoToFridge();
		
	}


//	private void CheckAnotherMarket(MyFoodsToRestock restock) {
//		// TODO Auto-generated method stub
//		List<MarketAgent> marketsNotChecked = new ArrayList<MarketAgent>();
////		int marketsAlreadyChecked = 0;
////		for (MarketAgent m: restock.marketsChecked.keySet()) {
////		    if(restock.marketsChecked.containsKey(m))
////		    	marketsAlreadyChecked++;
////		    else
////		    	marketsNotChecked.add(m);
////		}
//		
//		for (Entry<MarketAgent, Boolean> entry : restock.marketsChecked.entrySet()) {
//            if (entry.getValue().equals(false)) {
//                marketsNotChecked.add(entry.getKey());
//            }
//        }
//		if(marketsNotChecked.isEmpty())
//		{
//			Do("Checked all markets, cannot restock order: " + restock.f.foodname);
//			myFoodsToRestock.remove(restock);
//			return;
//		}
//		int marketNumber = generator.nextInt(marketsNotChecked.size());
//		Market market = marketsNotChecked.get(marketNumber);
//		Do("Ordering more " + restock.f.foodname + "s from " + market.getName());
//		market.msgOrderFoodThatIsLow(restock.f,restock.f.capacity-restock.f.amount);
//	}


	private void CookOrder(final MyOrder o) {
		// TODO Auto-generated method stub
		//guiCooking
		Do("Cooking " + o.order.o.food.foodname + " order for table: " + o.order.table.tableNumber);
		Food f = foods.get(o.order.o.food.foodname);
		
		f.amount--;
		//System.out.println("Now only have " + f.amount + " of " + f.foodname + "s!");
		if(f.amount <= f.low && !orderHasAlreadyBeenMade(f))
		{
			OrderFoodThatIsLow();
		}
		o.st = Status.cooking;
		//someTimer
		cookState = Status.doingNothing;
		timer.schedule(new TimerTask() {

		            @Override
		            public void run() {
		            	DoneCooking(o);
		            }

					
		        }, o.order.getCookingTime()*100);


		
		
	}

	private boolean orderHasAlreadyBeenMade(Food f) {
		// TODO Auto-generated method stub
		for(MarketOrder order : myMarketOrders)
		{
			if(order.itemsRequested.containsKey(f.foodname))
				return true;
		}
		return false;
	}


	private void DoneCooking(MyOrder o) {
		// TODO Auto-generated method stub
		System.out.println(o.order.o.food.foodname + " is done");
//    	CookingDone(o.order);
    	o.st = Status.doneCooking;
    	//System.out.println(cookState.toString());
    	stateChanged();
	}


	// The animation DoXYZ() routines

	private void OrderFoodThatIsLow() {
		// TODO Auto-generated method stub
//		int random = generator.nextInt(markets.size());
//		MarketAgent market = markets.get(random);
//		
//		MyFoodsToRestock restockThis = new MyFoodsToRestock(f,this.markets);
//		restockThis.marketsChecked.put(market, true);
//		myFoodsToRestock.add(restockThis);
//		Do("Ordering more " + f.foodname + "s from " + market.getName());
//		market.msgOrderFoodThatIsLow(f,f.capacity-f.amount);
		Map<String, Integer> marketOrder = Collections.synchronizedMap(new HashMap<String, Integer>());
		
		for(Food f : foods.values()){
			if(f.amount <= f.low && !orderHasAlreadyBeenMade(f))
				marketOrder.put(f.foodname, f.capacity-f.amount);
		}
		myMarketOrders.add(new MarketOrder(marketOrder));
		this.marketEmployee.msgOrder(marketOrder, this, cashier);
	}

	private void OutOfFood(Order o) {
		// TODO Auto-generated method stub
		Food f = foods.get(o.o.food.foodname);
		Do("Out of " + f.foodname);
		o.waiter.msgOutOfFood(o);
		myOrders.remove(o);
		if(!this.orderHasAlreadyBeenMade(o.o.food))
			OrderFoodThatIsLow();
		cookState = Status.doingNothing;
	}

	/* (non-Javadoc)
	 * @see restaurant.Cook#CookingDone(restaurant.Order)
	 */
	
	public void CookingDone(Order o){
		Do("Cooking done for " + o.o.food.foodname + o.table.tableNumber);
		o.waiter.msgOrderReady(o);
		o.foodStatus = Status.done;
		//myOrders.remove(o);
	}
	//utilities
	
//	private MyFoodsToRestock findRestockOrder(Food f) {
//		// TODO Auto-generated method stub
//		for(MyFoodsToRestock r : myFoodsToRestock)
//		{
//			if(r.f == f)
//				return r;
//		}
//		return null;
//	}

	/* (non-Javadoc)
	 * @see restaurant.Cook#setGui(restaurant.gui.HostGui)
	 */
	
	public void setGui(CookGui gui) {
		cookGui = gui;
	}

	/* (non-Javadoc)
	 * @see restaurant.Cook#getGui()
	 */
	

	


	/* (non-Javadoc)
	 * @see restaurant.Cook#getMarket()
	 */
	
//	public Market getMarket() {
//		return market;
//	}

	/* (non-Javadoc)
	 * @see restaurant.Cook#setMarket(restaurant.MarketAgent)
	 */
	
	public void setMarketEmployee(MarketEmployee me) {
		this.marketEmployee = me;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Cook#addMarket(restaurant.MarketAgent)
	 */
	
//	public void addMarket(MarketAgent market) {
//		markets.add(market);
//	}

	private Grill availableGrill(){
		for(Grill g: grills)
		{
			if(g.occupiedBy == null)
				return g;
		}
		return null;
	}


	

	private MyOrder findMyOrder(Order o) {
		// TODO Auto-generated method stub
		for(MyOrder mo : myOrders)
		{
			if(mo.order == o)
				return mo;
		}
		return null;
	}

	private MyOrder findOrderThatWasPlated(Choice c) {
		// TODO Auto-generated method stub
		for(MyOrder mo : myOrders)
		{
			if(mo.order.o == c && mo.st == Status.PlatedAndWaiterNotified)
				return mo;
		}
		return null;
	}





	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public int getRestaurantIndex() {
		// TODO Auto-generated method stub
		return 2;
	}

	public void setRevolvingStand(RevolvingStand stand)
	{
		this.revolvingStand = stand;
	}


	

	


	

	
	

	
	

}

