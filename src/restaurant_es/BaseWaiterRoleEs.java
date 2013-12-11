package restaurant_es;

import restaurant_es.gui.RestaurantGuiEs;
import restaurant_es.gui.RestaurantPanelEs.CookWaiterMonitorEs;
import restaurant_es.gui.WaiterGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import restaurant_es.interfaces.Customer;
import restaurant_es.interfaces.Host;
import restaurant_es.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

import people.People;
import people.Role;
/**
 * Restaurant Base Waiter Role
 * @param <MyCustomer>
 */

public abstract class BaseWaiterRoleEs extends Role implements Waiter {

	protected List<MyCustomer> customers = new ArrayList<MyCustomer>();
	protected List<FoodOnMenu> menu = new ArrayList<FoodOnMenu>();

	protected Host host;
	protected Cook cook;
	private Cashier cashier;
	protected Boolean onBreak = false; 
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore atCook = new Semaphore(0,true);
	private Semaphore atWaitingCustomer = new Semaphore(0,true);
	protected Semaphore atRevolvingStand = new Semaphore (0,true);
	protected Semaphore atExit = new Semaphore(0,true);
	
	protected int currentCustomerNum;
	
	private boolean turnActive = false;
	protected boolean leaveWork = false;
	
	protected enum customerState {waiting, seated, readyToOrder, askedToOrder, ordered, 
		waitingForFood, outOfChoice, foodIsReady, checkIsReady, needsToPay, eating, doneLeaving};
	protected enum agentState {WORKING, ASKING_FOR_BREAK, ON_BREAK};
	protected agentState state;
	protected CookWaiterMonitorEs theMonitor;
	public WaiterGui waiterGui = null;
	protected RestaurantGuiEs restGui = null;
	
	// messages
	
	public void msgIsActive() {
		print("Person agent sent msgIsActive");
		isActive = true;
		turnActive = true;
		getPersonAgent().CallstateChanged();
	}
	
	public void msgIsInActive () {
		print("Person agent sent msgIsInactive");
		leaveWork = true;
		getPersonAgent().CallstateChanged();
	}
	
	public void msgAtTable() {
		atTable.release();
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
	
	public void msgAtWaitingCustomer() {
		atWaitingCustomer.release();
		getPersonAgent().CallstateChanged();
	}
	

	public void msgAtRevolvingStand() {
		atRevolvingStand.release();
		myPerson.CallstateChanged();
		
	}
	
	public void SitAtTable(Customer customer, int table) {
		print("Host has a customer waiting to be seated. I will go pick up: " + customer.getName());
		currentCustomerNum++;
		customers.add(new MyCustomer(customer, table, "waiting"));
		getPersonAgent().CallstateChanged();
	}

	public void msgIAmReadyToOrder(Customer cust) {
		print("Customer: " + cust.getName() + " is ready to order");
		for (MyCustomer customer : customers) {
			if (customer.c == cust){
				customer.state = customerState.readyToOrder;
			}
		}
		getPersonAgent().CallstateChanged();
	}

	public void msgHereIsMyOrder (Customer cust, String choice) {
		print("Customer: " + cust.getName() + " ordered: " + choice);
		for (MyCustomer customer : customers) {
			if (customer.c == cust){
				customer.state = customerState.ordered;
				customer.choice = choice;
			}
		}
		getPersonAgent().CallstateChanged();
	}
	
	public void msgOrderIsReady (String order, int t) {
		print("There is an order ready for pickup");
		for (MyCustomer customer : customers) {
			if (customer.tableNumber == t){
				customer.state = customerState.foodIsReady;
			}
		}
		getPersonAgent().CallstateChanged();
	}
	
	public void msgOutOfFood (String order, int t) {
		print("Cook is out of: " + order);
		for (MyCustomer customer : customers) {
			if (customer.tableNumber == t){
				customer.state = customerState.outOfChoice;
			}
		}
		getPersonAgent().CallstateChanged();
	}
	
	public void msgHereIsCheck (Customer customer2, Double d) {
		print("Received check for: " + customer2.getName());
		for (MyCustomer customer : customers) {
			if (customer.c == customer2){
				customer.due = d;
				customer.state = customerState.checkIsReady;
			}
		}
		getPersonAgent().CallstateChanged();
	}
	
	public void msgDoneEatingAndLeaving (Customer cust){
		print("Customer: " + cust.getName() + " is done eating and leaving.");
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
					goTakeOrder(customer);
					return true;
				}
			}
			
			for (MyCustomer customer : customers) {
				if (customer.state == customerState.outOfChoice){
					goTellCustomerToReorder (customer);
					return true;
				}
			}
			
			for (MyCustomer customer : customers) {	
				if (customer.state == customerState.foodIsReady){
					serveFoodToCustomer (customer);
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
					giveCheckToCustomer(customer);
					return true;
				}
			} 
			
			for (MyCustomer customer : customers) {
				if (customer.state == customerState.doneLeaving){
					UpdateTableInfo(customer.c);
					customers.remove(customer);
					return true;
				}
			}
			
			if (leaveWork && customers.size() == 0) {
				done();
			}
			
		}catch (ConcurrentModificationException e) {return false;}
			
			
			//if (state == agentState.ON_BREAK) {
			//	updateGuiButton();
			//	return true;
			//}
	
			waiterGui.DoGoRest();	
			return false;
			
		
	}

	
	
	// Actions

	private void clockIn() {
		host = (Host) getPersonAgent().getHost(4);
		host.addWaiter(this);
		waiterGui.setHomePosition(host.getWaiters().indexOf(this));
		waiterGui.setPresent(true);
		waiterGui.DoGoRest();
		cook = host.getCook();
		cashier = host.getCashier();
		turnActive = false;
	}
	
	private void seatCustomer(MyCustomer customer) {
		waiterGui.DoGoToWaitingCustomer();
		try {
			atWaitingCustomer.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		customer.c.msgFollowMeToTable(this, customer.tableNumber, menu);
		waiterGui.DoSeatCustomer(customer.c, customer.tableNumber);
		try {
			atTable.acquire();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		customer.state = customerState.seated;
	}

	public void goTakeOrder (MyCustomer customer) {
		waiterGui.DoApproachCustomer(customer.c);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
			}
		customer.c.msgWhatWouldYouLike();
		customer.state = customerState.askedToOrder;	
	} 

	public abstract void goPlaceOrder(MyCustomer customer) throws InterruptedException;
	
	public void goTellCustomerToReorder (MyCustomer customer) {
		waiterGui.DoApproachCustomer(customer.c);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
			}
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
	
	public void serveFoodToCustomer(MyCustomer customer){
		atCook.drainPermits();
		waiterGui.DoGoToCook();
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
			}
		((CookRoleEs) cook).getGui().foodPickedUp(customer.tableNumber);
		waiterGui.DoBringFoodToCustomer(customer.c);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
			}
		customer.c.msgHereIsYourFood();
		cashier = host.getCashier();
		((CashierRoleEs) cashier).msgHereIsBill(customer.c, customer.choice, this);
		customer.state = customerState.eating;
	}
	
	public void giveCheckToCustomer(MyCustomer customer) {
		waiterGui.DoApproachCustomer(customer.c);
		try {
			atTable.drainPermits();
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		customer.c.msgHereIsCheck(customer.due, cashier);
		customer.state = customerState.needsToPay;
	}
	
	public void UpdateTableInfo(Customer c) {
		currentCustomerNum--;
		host.msgTableIsFree(((RestaurantCustomerRoleEs) c).getTableNumber());
	}
	
	public abstract void done();

	//utilities
	
	public String getMaitreDName() {
		return getPersonAgent().getName();

	}

	public List<MyCustomer> getCustomers() {
		return customers;
	}

	public int getCustomerNum() {
		return currentCustomerNum;
	}

	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	public WaiterGui getGui() {
		return waiterGui;
	}
	
	public void setHost (HostRoleEs h) {
		host = h;
	}
	
	public void setCook (CookRoleEs k) {
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
		return onBreak;
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

	public class FoodOnMenu {
		String type;
		Double price;
		
		public FoodOnMenu (String t, Double p) {
			type = t;
			price = p;
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