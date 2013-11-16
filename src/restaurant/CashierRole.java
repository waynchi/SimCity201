package restaurant;

import agent.Agent;
import restaurant.BaseWaiterRole;
import restaurant.gui.HostGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Market;
import restaurant.interfaces.Waiter;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;

import java.awt.Dimension;
import java.util.*;
import java.util.concurrent.Semaphore;


// Cashier keeps track of the balance of every customer that once comes to the restaurant
// Cashier computes checks when he gets the request from waiter.
// waiter needs to go to cashier to take the bill
// customer needs to go to cashier to pay

public class CashierRole extends Agent implements Cashier {

	private List<Check> checks = Collections.synchronizedList(new ArrayList<Check>());
	private List<MarketBill> marketBills = Collections.synchronizedList(new ArrayList<MarketBill>());
	private String name;
	private double myMoney = 107;
	private Map<Customer, Double> balance = Collections.synchronizedMap(new HashMap<Customer, Double>());
	private Map<String, Double> price =Collections.synchronizedMap(new HashMap<String, Double>());
	public enum checkState {COMPUTED, SENT_TO_WAITER, BEING_PAID};
	public EventLog log = new EventLog();
	
	

	public class Check {
		Waiter waiter;
		Customer customer;
		Double due;
		Double amountPaid;
		private checkState state;

		public Check (Customer c, Waiter w, double balance) {
			waiter = w;
			customer = c;
			due = balance;
			amountPaid = 0.00;
			setState(checkState.COMPUTED);
		}

		public checkState getState() {
			return state;
		}

		public void setState(checkState state) {
			this.state = state;
		}
	}

	public class MarketBill {
		public Market market;
		Double amount;

		public MarketBill (Market m , double a) {
			market = m;
			amount = a;
		}

		public String toString () {
			return market.getName() + " " + amount;
		}

	}



	/**
	 * Constructor for CookAgent class
	 *
	 * @param name name of the cook
	 */
	public CashierRole(String name) {
		super();
		this.name = name;
		price.put("Steak", 15.99);
		price.put("Chicken", 10.99);
		price.put("Salad", 5.99);
		price.put("Pizza", 8.99);



	}


	// Messages

	// Cook receives an order from the waiter and stores it into a list

	public void msgHereIsMarketBill (Market m, double price){
		log.add(new LoggedEvent("Received msgHereIsMarketBill from Market " + m.getName() + " and the "
				+ "amount is " + price));
		marketBills.add(new MarketBill(m,price));
		stateChanged();
	}

	public void msgHereIsBill (Customer c, String food, Waiter w) {
		log.add(new LoggedEvent("Received msgHereIsBill from Waiter " + w.getName() + " with Customer " + c.getName() + " and food " + food));
		synchronized(balance){
			if (balance.containsKey(c)){
				balance.put(c, balance.get(c)+price.get(food));
			}
			else {
				getBalance().put(c, price.get(food));
			}
		}
		checks.add(new Check (c, w, getBalance().get(c)));
		stateChanged();
	}

	public void msgPayMyCheck (Customer c, Double amount) {
		log.add(new LoggedEvent("Received msgPayMyCheck from Customer " + c.getName() + " and the amount is " + amount));
		synchronized (checks){
			for (Check check : checks) {
				if (check.customer == c) {
					check.setState(checkState.BEING_PAID);
					check.amountPaid = amount;
				}
			}
		}
		myMoney+=amount;

		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		synchronized (checks) {
			for (Check check : checks) {
				if (check.getState() == checkState.COMPUTED){
					sendCheckToWaiter(check);
					return true;
				}
			}
		}

		synchronized (checks) {
			for (Check check : checks) {
				if (check.getState() == checkState.BEING_PAID){
					giveChangeToCustomer(check);
					return true;
				}
			}
		}

		if (!marketBills.isEmpty()) {
			payMarket(marketBills.get(0));
			return true;
		}


		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	private void sendCheckToWaiter (final Check c) {
		log.add(new LoggedEvent("In action sendCheckToWaiter, ready to send Check to " + c.waiter.getName() + 
				". Customer " + c.customer.getName() + " needs to pay " + c.due));
		/*new java.util.Timer().schedule(
				new java.util.TimerTask(){
					public void run(){
						//print ("Here is the bill for " + c.customer);
						c.waiter.msgHereIsCheck (c.customer, c.due);
					}
				},
			    1000);*/
		c.waiter.msgHereIsCheck (c.customer, c.due);
		c.setState(checkState.SENT_TO_WAITER);
	}

	private void giveChangeToCustomer (Check c) {
		log.add(new LoggedEvent("In action giveChangeToCustomer, ready to give change to "+ c.customer.getName()));
		if (c.due > c.amountPaid) {
			c.customer.msgHereIsYourChange(0.00);
			balance.put(c.customer, getBalance().get(c.customer)-c.amountPaid);
		}
		else if (c.due < c.amountPaid){
			c.customer.msgHereIsYourChange(c.amountPaid-c.due);
			myMoney-=c.amountPaid-c.due;
			balance.put(c.customer, 0.00);
		}
		else if (c.due == c.amountPaid) {
			getBalance().put(c.customer, 0.00);
			c.customer.msgHereIsYourChange(0.00);
		}
		getChecks().remove(c);
	}

	private void payMarket(MarketBill bill){
		log.add(new LoggedEvent("In action payMarket, paying market " + bill.market.getName()));
		if (myMoney > bill.amount) {
			print ("Paying " + bill.market.getName() + " "+ String.format("%.2f",bill.amount));
			bill.market.msgPayMarketBill(bill.amount, this);
			setMyMoney(myMoney - bill.amount);

		}
		else {
			print ("Paying " + bill.market.getName()+" , not able to pay full bill");
			bill.market.msgPayMarketBill(myMoney,this);
			setMyMoney(0);

		}

		marketBills.remove(bill);


	}


	//utilities

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List<Check> getChecks() {
		return checks;
	}


	public void setChecks(List<Check> checks) {
		this.checks = checks;
	}

	public List<MarketBill> getMarketBills() {
		return marketBills;
	}

	public Map<Customer, Double> getBalance() {
		return balance;
	}


	public void setBalance(Map<Customer, Double> balance) {
		this.balance = balance;
	}


	public double getMyMoney() {
		return myMoney;
	}


	public void setMyMoney(double myMoney) {
		this.myMoney = myMoney;
	}

}