package restaurant;

import restaurant.gui.RestaurantCashierGui;
import restaurant.gui.RestaurantGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Host;
import restaurant.interfaces.Waiter;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;

import java.awt.Component;
import java.util.*;
import java.util.concurrent.Semaphore;

import bank.TellerRole;
import bank.interfaces.Teller;
import market.interfaces.MarketEmployee;
import people.People;
import people.Role;


// Cashier keeps track of the balance of every customer that once comes to the restaurant
// Cashier computes checks when he gets the request from waiter.
// waiter needs to go to cashier to take the bill
// customer needs to go to cashier to pay

public class CashierRole extends Role implements Cashier {

	public EventLog log = new EventLog();
	public boolean inTest = false;

	private Map<String, Double> price =Collections.synchronizedMap(new HashMap<String, Double>());

	private List<MarketBill> marketBills = Collections.synchronizedList(new ArrayList<MarketBill>());

	private Map<Customer, Double> balance = Collections.synchronizedMap(new HashMap<Customer, Double>());
	public enum checkState {COMPUTED, SENT_TO_WAITER, BEING_PAID};

	public enum bankActivityState {NONE, ASKED_FOR_HELP, ASKED_DEPOSIT, ASKED_WITHDRAW, DONE};
	public bankActivityState bankState;
	public enum bankActivityEvent {NONE, READY_TO_HELP, LOAN_GIVEN, DEPOSIT_SUCCESSFUL, WITHDRAW_SUCCESSFUL, BANK_CLOSED};
	public bankActivityEvent bankEvent;

	RestaurantGui restGui;
	RestaurantCashierGui cashierGui;
	private Semaphore atExit = new Semaphore(0,true);
	private Semaphore atPosition = new Semaphore(0,true);

	private List<Check> checks = Collections.synchronizedList(new ArrayList<Check>());

	private Host host;
	private Teller teller;

	private Boolean leaveWork;
	private Boolean turnActive;
	private Boolean deposit = false;
	private Boolean withdraw = false;

	private double min_working_capital = 1000.0;
	private double working_capital = 100000.0;
	private double salary = 200.0;


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
		Double amount;
		public boolean itemsReceived = false;
		public boolean checkReceived = false;
		public int orderNumber = -1;
		public int marketNumber = -1;
		Map<String, Integer> itemsOrdered = new HashMap<String, Integer>();

		public MarketBill (double a, Map<String, Integer> items, int number, int market) {
			amount = a;
			itemsOrdered = items;
			checkReceived = true;
			orderNumber = number;
			marketNumber = market;
		}

		public MarketBill ( Map<String, Integer> items, int number, int market) {
			itemsOrdered = items;
			itemsReceived = true;
			orderNumber = number;
			marketNumber = market;
		}

	}


	public CashierRole(RestaurantGui gui) {
		if (!inTest) {
			cashierGui = new RestaurantCashierGui(this);
			restGui = gui;
			restGui.getAnimationPanel().addGui(cashierGui);
			cashierGui.setPresent(false);
		}
		price.put("Steak", 15.99);
		price.put("Chicken", 10.99);
		price.put("Salad", 5.99);
		price.put("Pizza", 8.99);

		min_working_capital = 1000;
		working_capital = 100000;
		leaveWork = false;
		isActive = false;
		turnActive = false;
	}

	// messages

	// from gui
	public void msgAtExit() {
		atExit.release();
		getPersonAgent().CallstateChanged();
	}

	public void msgAtPosition() {
		atPosition.release();
		getPersonAgent().CallstateChanged();
	}

	// from people agent
	public void msgIsActive() {
		print ("received msgIsActive");
		turnActive = true;
		isActive = true;
		getPersonAgent().CallstateChanged();
	}

	public void msgIsInActive() {
		print("received msgIsInActive");
		leaveWork = true;
		getPersonAgent().CallstateChanged();
	}

	
	// market interaction
	
	// from cook
	public void msgGotMarketOrder(Map<String, Integer> marketOrder, int orderNumber, int marketNumber) {
		print("notified by cook that market order " + orderNumber + "has been delivered");
		log.add(new LoggedEvent("told by cook that market order is delivered, ready to pay"));
		boolean orderFound = false;
		synchronized(marketBills){
			for (MarketBill mb : marketBills) {
				if (mb.orderNumber == orderNumber && mb.marketNumber == marketNumber) {
					print("verifying market order number");
					mb.itemsReceived = true;
					orderFound = true;
					print("");
				}
			}
			if (!orderFound) {
				marketBills.add(new MarketBill(marketOrder, orderNumber, marketNumber));
			}
		}
		getPersonAgent().CallstateChanged();
	}
	
	// from market cashier
	public void msgHereIsWhatIsDue(double price, Map<String, Integer> items,int orderNumber, int marketNumber) {
		print("got market bill for order number " + orderNumber);
		log.add(new LoggedEvent("Received msgHereIsWhatIsDue with price " + price + " and order number is " + orderNumber));

		boolean orderFound = false;
		synchronized(marketBills){
			for (MarketBill mb : marketBills) {
				if (mb.orderNumber == orderNumber && mb.marketNumber == marketNumber) {
					mb.checkReceived = true;
					orderFound = true;
				}
			}
		}
		if (!orderFound) {
			marketBills.add(new MarketBill(price ,items, orderNumber, marketNumber));
		}
		getPersonAgent().CallstateChanged();

	}

	// from market cashier
	public void msgHereIsChange(double change) {
		print("received change " + change + " from market cashier");
		log.add(new LoggedEvent("received change " + change + " from market cashier"));
		working_capital += change;
		getPersonAgent().CallstateChanged();

	}

	
	
	
	// restaurant interaction
	
	// from waiter
	public void msgHereIsBill (Customer c, String food, Waiter w) {
		print("received a customer bill from waiter");
		log.add(new LoggedEvent("received a bill from waiter " + w.getName() + " for customer " + c.getName()));
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

	// from restaurant customer
	public void msgPayMyCheck (Customer c, Double amount) {
		print("received payment from customer");
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
	public void msgReadyToHelp(Teller teller) {
		print("received ready to help from teller");
		log.add(new LoggedEvent("received msgReadyToHelp from teller"));
		bankEvent = bankActivityEvent.READY_TO_HELP;
		getPersonAgent().CallstateChanged();
	}

	public void msgGiveLoan(double balance, double amount) {
		log.add(new LoggedEvent("received loan successful from bank"));
		bankEvent = bankActivityEvent.WITHDRAW_SUCCESSFUL;
		working_capital += amount;
		getPersonAgent().CallstateChanged();
	}

	public void msgWithdrawSuccessful(double funds, double amount){
		print("received withdraw successful from teller");
		log.add(new LoggedEvent("received msgWithDrawSuccessful from teller"));
		bankEvent = bankActivityEvent.WITHDRAW_SUCCESSFUL;
		working_capital += amount;
		getPersonAgent().CallstateChanged();
	}

	public void msgDepositSuccessful(double funds){
		print("received deposit successful from teller");
		log.add(new LoggedEvent("received msgDepositSuccessful from teller"));
		bankEvent = bankActivityEvent.DEPOSIT_SUCCESSFUL;
		working_capital = min_working_capital;
		getPersonAgent().CallstateChanged();
	}
	
	public void msgGetOut() {
		print("received msg get out from teller");
		log.add(new LoggedEvent("received msgGetOut from teller"));
		bankEvent = bankActivityEvent.BANK_CLOSED;
		getPersonAgent().CallstateChanged();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {


		if (turnActive) {
			clockIn();
			return true;
		}

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
			synchronized (marketBills) {
				for (MarketBill mb : marketBills) {
					if (mb.itemsReceived && mb.checkReceived){
						payMarket(mb);
					return true;
					}

				}
			}
		}

		if (bankState == bankActivityState.ASKED_FOR_HELP && bankEvent == bankActivityEvent.READY_TO_HELP) {
			if (deposit) {
				depositExcessMoney();
				bankState = bankActivityState.ASKED_DEPOSIT;
				return true;
			}
			if (withdraw) {
				withdrawMoney();
				bankState = bankActivityState.ASKED_WITHDRAW;
				return true;
			}
		}


		if (bankState == bankActivityState.ASKED_DEPOSIT && bankEvent == bankActivityEvent.DEPOSIT_SUCCESSFUL) {
			bankState = bankActivityState.NONE;
			closeRestaurant();
			return true;
		}
		if (bankState == bankActivityState.ASKED_WITHDRAW && bankEvent == bankActivityEvent.WITHDRAW_SUCCESSFUL) {
			bankState = bankActivityState.NONE;
			payWorkers();
			closeRestaurant();
			return true;
		} 
		
		if (bankEvent == bankActivityEvent.BANK_CLOSED) {
			closeRestaurant();
			return true;
		}
		
		if (leaveWork) 	{
			if (getPersonAgent().getRestaurant(0).isClosed && host.getCustomerSize() == 0) {
				if (!getPersonAgent().getBank(0).isClosed) {
					prepareToClose();
				}
				else {
					payWorkers();
					closeRestaurant();
				}
			}
			else if (!getPersonAgent().getRestaurant(0).isClosed) leaveWork();
			return true;
		}

		return false;

		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}





	// Actions
	private void clockIn() {
		print("in clock in");
		log.add(new LoggedEvent("in clock in"));
		host = (Host) getPersonAgent().getHost(0);
		teller = (Teller) getPersonAgent().getTeller(0);
		if (!inTest){
			host.setCashier(this);
			cashierGui.setPresent(true);
			cashierGui.DoGoToWorkingPosition();
			try {
				atPosition.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		turnActive = false;
		deposit = false;
		withdraw = false;
		bankState = bankActivityState.NONE;
	}

	private void sendCheckToWaiter (final Check c) {
		print("giving customer check to waiter");
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
		print("giving change to customer");
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
		print("paying market bill number " + bill.orderNumber);
		log.add(new LoggedEvent("In action payMarket, amount due is "+bill.amount));
		//if (working_capital > bill.amount) {
		//print ("Paying " + bill.market.getName() + " "+ String.format("%.2f",bill.amount));
		if (!inTest) {
		((MarketEmployee) getPersonAgent().getMarketEmployee(0)).getCashier().msgHereIsPayment(working_capital, bill.orderNumber, this);
		}
		setMyMoney(0);

		//}
		/*else {
			print ("Paying " + bill.market.getName()+" , not able to pay full bill");
			bill.market.msgPayMarketBill(working_capital,this);
			setMyMoney(0);

		}*/
		marketBills.remove(bill);
	}

	// leave work in the middle of a day, i.e. the restaurant is not close yet
	private void leaveWork() {
		print("leaving work");
		isActive = false;
		if (!inTest){
			cashierGui.DoLeaveWork();
			try {
				atExit.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			leaveWork = false;
			cashierGui.setPresent(false);
			cashierGui.setDefaultDestination();
			getPersonAgent().msgDone("RestaurantCashierRole");
		}	
	}
	
	private void prepareToClose() {
		print("preparing to close");
		log.add(new LoggedEvent("In action prepareToClose"));
		leaveWork = false;
		double total = getTotalSalary() + min_working_capital;
		if (working_capital >= total) {
			payWorkers(); 	
			System.out.println("Depositing Money");
			teller.msgNeedHelp(this, "blah");
			bankState = bankActivityState.ASKED_FOR_HELP;	
			deposit = true;
		}

		else {
			teller.msgNeedHelp(this, "blah");
			System.out.println("Withdrawing Money");
			bankState = bankActivityState.ASKED_FOR_HELP;
			withdraw = true;
		}
	}

	private void payWorkers() {
		print("paying restaurant workers");
		log.add(new LoggedEvent("in action payWorkers, paying everybody"));
		working_capital -= getTotalSalary();
		for (People p : ((HostRole)host).getWorkers()) {
			double money = p.getMoney();
			money += salary;
			p.setMoney(money);
		}
	}

	
	private void closeRestaurant() {
		print("closing restaurant");
		log.add(new LoggedEvent("in action closeRestaurant, goint to the exit"));
		if (!getPersonAgent().getBank(0).isClosed){
			teller.msgDoneAndLeaving();
		}
		deposit = withdraw = false;
		isActive = false;
		if (!inTest){
			cashierGui.DoLeaveWork();
			try {
				atExit.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			leaveWork = false;
			bankEvent = bankActivityEvent.NONE;
			bankState = bankActivityState.NONE;
			cashierGui.setPresent(false);
			cashierGui.setDefaultDestination();
			getPersonAgent().msgDone("RestaurantCashierRole");
		}
	}

	private double getTotalSalary() {
		log.add(new LoggedEvent("in action getTotalSalary, calculating total salary for workeres"));
		return (((HostRole)host).getWorkers().size() * salary);
	}

	private void depositExcessMoney() {
		print("deposit money");
		double amount = working_capital - min_working_capital;
		log.add(new LoggedEvent("in action depositExcessMoney, about to deposit " + amount));
		teller.msgDeposit(getPersonAgent().getRestaurant(0).bankAccountID, amount);
		bankState = bankActivityState.ASKED_DEPOSIT;
	}

	private void withdrawMoney() {
		print("withdraw money");
		double amount = getTotalSalary() + min_working_capital - working_capital;
		log.add(new LoggedEvent("in action withdrawMoney, about to withdraw " + amount));
		teller.msgWithdraw(getPersonAgent().getRestaurant(0).bankAccountID,amount);
		bankState = bankActivityState.ASKED_WITHDRAW;
	}


	//utilities

	public String getMaitreDName() {
		return getPersonAgent().getName();
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
		return working_capital;
	}


	public void setMyMoney(double myMoney) {
		this.working_capital = myMoney;
	}

	public Boolean isActive(){
		return isActive;
	}

	public void setTeller (Teller t) {
		teller = t;
	}

	public void setHost (Host host2) {
		host = host2;
	}

	public String getName() {
		return getPersonAgent().getName();
	}



}