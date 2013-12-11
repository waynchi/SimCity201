package restaurant_zt;

import restaurant_zt.gui.RestaurantGuiZt;
import restaurant_zt.gui.RestaurantPanelZt.CookWaiterMonitorZt;
import restaurant_zt.gui.WaiterGuiZt;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import restaurant_zt.interfaces.Customer;
import restaurant_zt.interfaces.Host;
import restaurant_zt.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

import people.People;
import people.Role;
/**
 * Restaurant Host Agent
 * @param <MyCustomer>
 */
public abstract class BaseWaiterRole extends Role implements Waiter {

	protected List<MyCustomer> customers = new ArrayList<MyCustomer>();
	protected List<FoodOnMenu> menu = new ArrayList<FoodOnMenu>();

	protected Host host;
	protected Cook cook;
	private Cashier cashier;
	protected Boolean breaking = false; 
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore atCook = new Semaphore(0,true);
	private Semaphore atWaitingCustomer = new Semaphore(0,true);
	private Semaphore atCashier = new Semaphore(0,true);
	protected Semaphore atRevolvingStand = new Semaphore (0,true);
	protected Semaphore atExit = new Semaphore(0,true);
	
	protected int currentCustomerNum;
	
	private boolean turnActive = false;
	protected boolean leaveWork = false;
	
	protected enum customerState 
	{waiting, seated, readyToOrder, askedToOrder, ordered, waitingForFood, outOfChoice, foodIsReady, checkIsReady, needsToPay, eating, doneLeaving};
	protected enum agentState 
	{WORKING, ASKING_FOR_BREAK, ON_BREAK};
	protected agentState state;
	protected CookWaiterMonitorZt theMonitor;
	
	public class FoodOnMenu {
		String type;
		Double price;
		
		public FoodOnMenu (String t, Double p) {
			type = t;
			price = p;
		}
	}
	
	
	public WaiterGuiZt waiterGui = null;
	protected RestaurantGuiZt restGui = null;
	public String getMaitreDName() {
		return getPersonAgent().getName();

	}

	public List<MyCustomer> getCustomers() {
		return customers;
	}

	public int getCustomerNum() {
		return currentCustomerNum;
	}

	
	public void msgIsInActive () {
		leaveWork = true;
		getPersonAgent().CallstateChanged();
	}
	
	public void msgAtTable() {//from animation
		atTable.release();// = true;
		getPersonAgent().CallstateChanged();

	}
	public void msgIsActive() {
		isActive = true;
		turnActive = true;
		getPersonAgent().CallstateChanged();
	}
	
	public void msgAtExit() {
		atExit.release();
		getPersonAgent().CallstateChanged();
	}

	public void msgAtCook() {
		atCook.release();
		getPersonAgent().CallstateChanged();
	}
	
	public void msgAtCashier() {
		atCashier.release();
		getPersonAgent().CallstateChanged();
	}
	
	public void msgAtWaitingCustomer() {
		atWaitingCustomer.release();
		getPersonAgent().CallstateChanged();
	}
	

	public void msgAtRevolvingStand() {
		atRevolvingStand.release();
		myPerson.CallstateChanged();
		
	}
	
	public void msgAskForBreak() { //from gui onBreak button
		state = agentState.ASKING_FOR_BREAK;
		getPersonAgent().CallstateChanged();
	}
	
	public void msgOffBreak() { // from gui
		state = agentState.WORKING;
		breaking = false;
		getPersonAgent().CallstateChanged();
	}
	
	public void msgBreakApproved() { // from host Agent
		state = agentState.ON_BREAK;
		breaking = true;
		getPersonAgent().CallstateChanged();
	}
	
	public void msgBreakDenied() { // from host agent
		state = agentState.WORKING;
		breaking = false;
		getPersonAgent().CallstateChanged();
	}
	
	public void SitAtTable(Customer customer, int table) {
		print("Received SitAtTable from host. Picking up customer: " + customer.getName());
		currentCustomerNum++;
		customers.add(new MyCustomer(customer, table, "waiting"));
		getPersonAgent().CallstateChanged();
	}
	

	public void msgIAmReadyToOrder(Customer cust) {
		print("Recevied msgIAmReadyToOrder from " + cust.getName());
		for (MyCustomer customer : customers) {
			if (customer.c == cust){
				customer.state = customerState.readyToOrder;
			}
		}
		getPersonAgent().CallstateChanged();
	}

	public void msgHereIsOrder (Customer cust, String choice) {
		print("Received msgHereIsOrder from " + cust.getName());
		for (MyCustomer customer : customers) {
			if (customer.c == cust){
				customer.state = customerState.ordered;
				customer.choice = choice;
			}
		}
		getPersonAgent().CallstateChanged();

	}
	
	public void msgOrderIsReady (String order, int t) {
		print("Received msgOrderIsReady for table " + t);
		for (MyCustomer customer : customers) {
			if (customer.tableNumber == t){
				customer.state = customerState.foodIsReady;
			}
		}
		getPersonAgent().CallstateChanged();

	}
	
	public void msgOutOfFood (String order, int t) {
		for (MyCustomer customer : customers) {
			if (customer.tableNumber == t){
				customer.state = customerState.outOfChoice;
			}
		}
		getPersonAgent().CallstateChanged();
	}
	
	public void msgHereIsCheck (Customer customerIn, Double d) {
		print("received msgHereIsCheck (cashier) for customer " + customerIn.getName());
		for (MyCustomer customer : customers) {
			if (customer.c == customerIn){
				customer.due = d;
				customer.state = customerState.checkIsReady;
			}
		}
		getPersonAgent().CallstateChanged();
	}
	
	public void msgDoneEating (Customer cust){
		print("Receied msgDoneEating from " + cust.getName());
		for (MyCustomer customer : customers) {
			if (customer.c == cust){
				customer.state = customerState.doneLeaving;
			}
		}
		getPersonAgent().CallstateChanged();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 * @throws InterruptedException 
	 */
	public boolean pickAndExecuteAnAction() {
		try{
			if (turnActive) {
				clockIn();
				return true;
			}
	
			for (MyCustomer customer : customers) {
				if (customer.state == customerState.waiting){
					seatCustomer(customer);
					return true;
				}
			}
			
			for (MyCustomer customer : customers) {
				if (customer.state == customerState.readyToOrder){
					takeOrder(customer);
					return true;
				}
			}
			
			for (MyCustomer customer : customers) {
				if (customer.state == customerState.outOfChoice){
					tellCustomerReorder (customer);
					return true;
				}
			}
			
			for (MyCustomer customer : customers) {	
				if (customer.state == customerState.foodIsReady){
					deliverFoodToCustomer (customer);
					return true;
				}
			}

			
			for (MyCustomer customer : customers) {
				if (customer.state == customerState.ordered){
					try {
						goPlaceOrder(customer);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return true;
				}
			}
			
			for (MyCustomer customer : customers) {
				if (customer.state == customerState.checkIsReady){
					deliverCheckToCustomer(customer);
					return true;
				}
			} 
			
			for (MyCustomer customer : customers) {
				if (customer.state == customerState.doneLeaving){
					updateTableInfo(customer.c);
					customers.remove(customer);
					return true;
				}
			}
			
			if (leaveWork && customers.size() == 0) {
				msgDone();
			}
			
		}catch (ConcurrentModificationException e) {return false;}
			waiterGui.DoGoRest();	
			return false;
	}

	// Actions

	private void clockIn() {
		print("Clocking in");
		host = (Host) getPersonAgent().getHost(2);
		host.addWaiter(this);
		waiterGui.setHomePosition(host.getWaiters().indexOf(this));
		waiterGui.setPresent(true);
		waiterGui.DoGoRest();
		cook = host.getCook();
		cashier = host.getCashier();
		turnActive = false;
	}
	
	private void seatCustomer(MyCustomer customer) {
		print("Seating customer "+customer.c.getName());
		waiterGui.DoGoToWaitingCustomer();
		try {
			atWaitingCustomer.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		customer.c.msgFollowMeToTable(this, customer.tableNumber, menu);
		waiterGui.DoSeatCustomer(customer.c, customer.tableNumber);
		print("Seating " + customer.c + " at table " + customer.tableNumber);
		try {
			atTable.acquire();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		customer.state = customerState.seated;
	}

	public void takeOrder (MyCustomer customer) {
		print ("Approaching " + customer.c + " to take order.");
		waiterGui.DoGoToCustomer(customer.c);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
			}
		print ("What would you like?");
		customer.c.msgWhatWouldYouLike();
		//waiterGui.DoAskCustomer();
		customer.state = customerState.askedToOrder;	
	} 

	public abstract void goPlaceOrder(MyCustomer customer) throws InterruptedException;
	
	public void tellCustomerReorder (MyCustomer customer) {
		waiterGui.DoGoToCustomer(customer.c);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
			}
		print ("We have run out of. " + customer.choice + ". Please reorder");
		List<FoodOnMenu> newMenu = new ArrayList<FoodOnMenu>();
		FoodOnMenu f = new FoodOnMenu(" ",0.00);
		newMenu.add(new FoodOnMenu("Steak", 15.99));
		newMenu.add(new FoodOnMenu("Chicken", 10.99));
		newMenu.add(new FoodOnMenu("Salad", 5.99));
		newMenu.add(new FoodOnMenu("Pizza", 8.99));
		for (FoodOnMenu temp : newMenu) {
			if (temp.type.equals(customer.choice)){
				f = temp;
			}
		}
		newMenu.remove(f);
		customer.c.msgReorder(newMenu);
		customer.state = customerState.askedToOrder;
	}
	
	public void deliverFoodToCustomer(MyCustomer customer){
		print ("Going to get food for customer " + customer.c.getName());

		atCook.drainPermits();
		waiterGui.DoGoToCook();
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
			}
		((CookRoleZt) cook).getGui().foodPickedUp(customer.tableNumber);
		waiterGui.DoBringFoodToCustomer(customer.c);
		print ("Bringing food to table " + customer.tableNumber);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
			}
		print ("Food dropped off at table.");
		customer.c.msgHereIsYourFood();
		print ("Telling cashier to handle the bill for table # " + customer.tableNumber);
		cashier = host.getCashier();
		((CashierRoleZt) cashier).msgHereIsBill(customer.c, customer.choice, this);
		customer.state = customerState.eating;
	}
	
	public void deliverCheckToCustomer(MyCustomer customer) {
		print("Grabbing check for customer " + customer.c.getName());
		waiterGui.DoGoToCashier();
		try {
			atCashier.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		print("Approaching customer: " + customer.c.getName());

		waiterGui.DoGoToCustomer(customer.c);
		try {
			atTable.drainPermits();
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		print ("Delivering check to: " + customer.c);
		customer.c.msgHereIsCheck(customer.due, cashier);
		customer.state = customerState.needsToPay;
	}
	
	public void updateTableInfo(Customer c) {
		currentCustomerNum--;
		host.msgTableIsFree(((RestaurantCustomerRoleZt) c).getTableNumber());
	}
	
	public abstract void msgDone();

	//utilities

	public void setGui(WaiterGuiZt gui) {
		waiterGui = gui;
	}

	public WaiterGuiZt getGui() {
		return waiterGui;
	}
	
	public void setHost (HostRoleZt h) {
		host = h;
	}
	
	public void setCook (CookRoleZt k) {
		cook = k;
	}
	
	public void setCashier (Cashier c) {
		cashier = c;
	}
	
	public List<MyCustomer> getMyCustomer(){
		return customers;
	}
	
	public Host getHost () {
		return host;
	}

	public Boolean isOnBreak() {
		return breaking;
	}
	
	public class MyCustomer{
		protected Customer c;
		protected int tableNumber;
		protected String choice;
		customerState state;
		Double due;
		
		public MyCustomer(Customer customer, int tableNum, String s){

			c = customer;
			tableNumber = tableNum;
			state = customerState.waiting;
			stateChanged();
		}

		public Customer getCustomerAgent() {
			return this.c;
		}	
	}

	public Boolean isActive() {
		return isActive;
	}
	
	public People getPerson() {
		return getPersonAgent();
	}
	
	public String getName() {
		return getPersonAgent().getName();
	}


	
	
}