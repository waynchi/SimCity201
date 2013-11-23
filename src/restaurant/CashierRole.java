package restaurant;

import restaurant.BaseWaiterRole;
import restaurant.gui.RestaurantPanel;
import restaurant.interfaces.BankTeller;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Market;
import restaurant.interfaces.Waiter;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;

import java.util.*;
import people.Role;


// Cashier keeps track of the balance of every customer that once comes to the restaurant
// Cashier computes checks when he gets the request from waiter.
// waiter needs to go to cashier to take the bill
// customer needs to go to cashier to pay

public class CashierRole extends Role implements Cashier {

	private String name;
	public EventLog log = new EventLog();
	private Map<String, Double> price =Collections.synchronizedMap(new HashMap<String, Double>());

	private List<MarketBill> marketBills = Collections.synchronizedList(new ArrayList<MarketBill>());

	private Map<Customer, Double> balance = Collections.synchronizedMap(new HashMap<Customer, Double>());
	public enum checkState {COMPUTED, SENT_TO_WAITER, BEING_PAID};
	private List<Check> checks = Collections.synchronizedList(new ArrayList<Check>());

	private RestaurantPanel restaurantPanel;
	private double min_working_capital, working_capital, bank_balance;
	private double waiter_salary, cook_salary, host_salary, cashier_salary;

	private HostRole host;
	
	private BankTeller teller;
	private int bankAccount;
	//private Semaphore depositCompleted = new Semaphore(0,true);
	//private Semaphore withdrawalCompleted = new Semaphore(0,true);
	private Boolean loanRequested, loanGranted, loanRefused;

	private Vector<BaseWaiterRole> waiters = null;

	public Boolean onClose;
	private Boolean isActive;
	private Boolean turnActive;
	private Boolean depositSuccessful = false;
	private Boolean withdrawalSuccessful = false;


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




	public CashierRole(String name, RestaurantPanel rp) {
		super();
		this.name = name;
		price.put("Steak", 15.99);
		price.put("Chicken", 10.99);
		price.put("Salad", 5.99);
		price.put("Pizza", 8.99);

		restaurantPanel = rp;

		min_working_capital = rp.getMinWorkingCapital();
		working_capital = rp.getWorkingCapital();
		bank_balance = rp.getBankBalance();
		waiter_salary = cook_salary = host_salary = cashier_salary = 10;
		//BankTellerRole teller;
		bankAccount = rp.getBankAccount();
		loanRequested = loanGranted = loanRefused = false;
		onClose = false;
		isActive = false;
		turnActive = false;
	}


	public CashierRole(String name) {
		super();
		this.name = name;
		price.put("Steak", 15.99);
		price.put("Chicken", 10.99);
		price.put("Salad", 5.99);
		price.put("Pizza", 8.99);

		waiter_salary = cook_salary = host_salary = cashier_salary = 10;
		working_capital = 100;

		//BankTellerRole teller;
		loanRequested = loanGranted = loanRefused = false;
		onClose = false;
		isActive = false;
		turnActive = false;
	}

	// messages

	public void msgIsActive() {
		turnActive = true;
		isActive = true;
		getPersonAgent().CallstateChanged();
	}

	public void msgIsInActive() {
		// onClose = true
		isActive = false;
		getPersonAgent().CallstateChanged();
	}


	public void msgHereIsMarketBill (Market m, double price){
		log.add(new LoggedEvent("Received msgHereIsMarketBill from Market " + m.getName() + " and the "
				+ "amount is " + price));
		marketBills.add(new MarketBill(m,price));
		getPersonAgent().CallstateChanged();
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
		getPersonAgent().CallstateChanged();
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
		working_capital+=amount;

		getPersonAgent().CallstateChanged();
	}

	// from BankTellerRole
	public void msgDepositSuccessful(double balance){
		working_capital -= (balance - bank_balance);
		bank_balance = balance;
		//depositCompleted.release();
		depositSuccessful = true;
		getPersonAgent().CallstateChanged();
	}
	public void msgWithdrawalSuccessful(double balance){
		working_capital += (bank_balance - balance);
		bank_balance -= balance;
		//withdrawalCompleted.release();
		withdrawalSuccessful = true;
		getPersonAgent().CallstateChanged();
	}
	public void loanGranted(double amount) {
		working_capital += amount;
		bank_balance -= amount;
		loanGranted = true;
		getPersonAgent().CallstateChanged();
	}
	public void loanRefused(){
		loanRefused = true;
		getPersonAgent().CallstateChanged();
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
		
		if (turnActive) {
			clockIn();
			return true;
		}

		if (!marketBills.isEmpty()) {
			payMarket(marketBills.get(0));
			return true;
		}

		if (loanRequested && loanGranted) {
			payWorkers();
			closeRestaurant();
			return true;
		}
		if (loanRequested && loanRefused) {
			depositExcessMoney();
			closeRestaurant();	
			return true;
		}
		if (onClose) {
			if (depositSuccessful) {
				closeRestaurant();
				return true;
			}
			if (withdrawalSuccessful) {
				payWorkers();
				closeRestaurant();
				return true;
			} 
			prepareToClose();
			return true;
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	
	private void clockIn() {
		host.setCashier(this);
		turnActive = false;
	}
	
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
			working_capital-=c.amountPaid-c.due;
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
		if (working_capital > bill.amount) {
			print ("Paying " + bill.market.getName() + " "+ String.format("%.2f",bill.amount));
			bill.market.msgPayMarketBill(bill.amount, this);
			setMyMoney(working_capital - bill.amount);

		}
		else {
			print ("Paying " + bill.market.getName()+" , not able to pay full bill");
			bill.market.msgPayMarketBill(working_capital,this);
			setMyMoney(0);

		}

		marketBills.remove(bill);


	}

	private void prepareToClose() {
				
		log.add(new LoggedEvent("In action prepareToClose"));
		double total = getTotalSalary() + min_working_capital;
		if (working_capital >= total) {
			payWorkers(); 	
			depositExcessMoney();	
		
			//closeRestaurant();
		}
		//else if (bank_balance + working_capital) >= total {
		//	teller.msgWithdraw(bankAccount, total - working_capital);
		//	withdrawalCompleted.acquire();
		//	payWorkers();
		//	closeRestaurant();
		//}
		else {
			teller.msgWithdraw(bankAccount, total - working_capital);
			/*try {
				withdrawalCompleted.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			//payWorkers();
			//closeRestaurant();
		}
	}

	private void payWorkers() {
		working_capital -= getTotalSalary();
		//waiters = restaurantPanel.getWaiters();

		//for (BaseWaiterRole w : restaurantPanel.getWaiters()){
		//	w.getPersonAgent().Money += waiter_salary;
		//}
		
		for (int i=0; i<restaurantPanel.getWaiters().size(); i++) {
			restaurantPanel.getWaiters().get(i).getPersonAgent().Money += waiter_salary;
		}
		restaurantPanel.getCook().getPersonAgent().Money+= cook_salary;
		restaurantPanel.getHost().getPersonAgent().Money+= host_salary;
		this.getPersonAgent().Money +=cashier_salary;
	}

	private void closeRestaurant() {
		log.add(new LoggedEvent("In action closeRestaurant"));
		restaurantPanel.setWorkingCapital (working_capital);
		restaurantPanel.setBankBalance (bank_balance);
		loanRequested = false;
		loanGranted = false;
		loanRefused = false;
		onClose = false;
		getPersonAgent().msgDone(this);
		//deactivate roles;
		//DoCloseRestaurant(); //gui stuff
	}

	private double getTotalSalary() {
		return (restaurantPanel.getWaiters().size()) * waiter_salary + cook_salary + host_salary + cashier_salary;
	}

	private void depositExcessMoney() {
		if (working_capital - min_working_capital >0){
			teller.msgDeposit(bankAccount, working_capital - min_working_capital);
			/*try {
				depositCompleted.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
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

	public double getBankBalance() {
		return bank_balance;
	}

	public void setBalance(Map<Customer, Double> balance) {
		this.balance = balance;
	}


	public double getMyMoney() {
		return working_capital;
	}


	public void setMyMoney(double myMoney) {
		this.working_capital = myMoney;
	}

	public Boolean isActive(){
		return isActive;
	}
	
	public void setTeller (BankTeller t) {
		teller = t;
	}
	
	public void setHost (HostRole h) {
		host = h;
	}

}