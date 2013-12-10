package restaurant_wc;

import restaurant_wc.gui.RestaurantCashierGui;
import restaurant_wc.gui.RestaurantGuiWc;
import restaurant.interfaces.Cashier;
import restaurant_wc.interfaces.Customer;
import restaurant_wc.interfaces.Host;
import restaurant_wc.interfaces.Waiter;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;

import java.util.*;
import java.util.concurrent.Semaphore;

import com.sun.xml.internal.bind.marshaller.MinimumEscapeHandler;

import bank.TellerRole;
import bank.interfaces.Teller;
import market.interfaces.MarketEmployee;
import people.People;
import people.Role;


// Cashier keeps track of the balance of every customer that once comes to the restaurant
// Cashier computes checks when he gets the request from waiter.
// waiter needs to go to cashier to take the bill
// customer needs to go to cashier to pay

public class CashierRoleWc extends Role implements Cashier {

	public EventLog log = new EventLog();
	public boolean inTest = false;

	private Map<String, Double> price =Collections.synchronizedMap(new HashMap<String, Double>());

	private List<MarketBill> marketBills = Collections.synchronizedList(new ArrayList<MarketBill>());

	private Map<Customer, Double> balance = Collections.synchronizedMap(new HashMap<Customer, Double>());
	public enum checkState {COMPUTED, SENT_TO_WAITER, BEING_PAID};

	public enum bankActivityState {NONE, ASKED_FOR_HELP, ASKED_DEPOSIT, ASKED_WITHDRAW, DONE}
	public bankActivityState bankState;

	public enum bankActivityEvent {NONE, READY_TO_HELP, LOAN_GIVEN, DEPOSIT_SUCCESSFUL, WITHDRAW_SUCCESSFUL}
	public bankActivityEvent bankEvent;

	RestaurantGuiWc restGui;
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
		public int orderNumber;
		Map<String, Integer> itemsOrdered = new HashMap<String, Integer>();

		public MarketBill (double a, Map<String, Integer> items, int number) {
			amount = a;
			itemsOrdered = items;
			checkReceived = true;
			orderNumber = number;
		}

		public MarketBill ( Map<String, Integer> items, int number) {
			itemsOrdered = items;
			itemsReceived = true;
			orderNumber = number;
		}

	}


	public CashierRoleWc(RestaurantGuiWc gui) {
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
		print ("received msgIsAtExit from gui");
		atExit.release();
		getPersonAgent().CallstateChanged();
	}

	public void msgAtPosition() {
		print ("received msgAtPosition from gui");
		atPosition.release();
		getPersonAgent().CallstateChanged();
	}

	// from people agent
	public void msgIsActive() {
		turnActive = true;
		isActive = true;
		getPersonAgent().CallstateChanged();
	}

	public void msgIsInActive() {
		leaveWork = true;
		getPersonAgent().CallstateChanged();
	}

	
	
	
	// market interaction
	
	// from cook
	public void msgGotMarketOrder(Map<String, Integer> marketOrder, int orderNumber, int marketNumber) {
		print("told by cook that market order is delivered, ready to pay");
		boolean orderFound = false;
		synchronized(marketBills){
			for (MarketBill mb : marketBills) {
				if (mb.orderNumber == orderNumber) {
					mb.itemsReceived = true;
					orderFound = true;
				}
			}
			if (!orderFound) {
				marketBills.add(new MarketBill(marketOrder, orderNumber));
			}
		}
		getPersonAgent().CallstateChanged();
	}
	
	// from market cashier
	public void msgHereIsWhatIsDue(double price, Map<String, Integer> items,int orderNumber, int marketNumber) {
		log.add(new LoggedEvent("Received msgHereIsWhatIsDue with price " + price + " and order number is " + orderNumber));

		boolean orderFound = false;
		synchronized(marketBills){
			for (MarketBill mb : marketBills) {
				if (mb.orderNumber == orderNumber) {
					mb.checkReceived = true;
					orderFound = true;
				}
			}
		}
		if (!orderFound) {
			marketBills.add(new MarketBill(price ,items, orderNumber));
		}
		getPersonAgent().CallstateChanged();

	}

	// from market cashier
	public void msgHereIsChange(double change) {
		print ("received change " + change + " from market cashier");
		working_capital += change;
		getPersonAgent().CallstateChanged();

	}

	
	
	
	// restaurant interaction
	
	// from waiter
	public void msgHereIsBill (Customer c, String food, Waiter w) {
		print("received a bill from waiter " + w.getName() + " for customer " + c.getName());
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

	// from restaurant customer
	public void msgPayMyCheck (Customer c, Double amount) {
		print ("received payment from customer " + c.getName());
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
		print("received msgReadyToHelp from teller");
		bankEvent = bankActivityEvent.READY_TO_HELP;
		System.out.println("got msgreadytohelp from teller");

		getPersonAgent().CallstateChanged();
	}

	public void msgGiveLoan(double balance, double amount) {
		print("received loan successful from bank");
		bankEvent = bankActivityEvent.WITHDRAW_SUCCESSFUL;
		working_capital += amount;
		getPersonAgent().CallstateChanged();
	}

	public void msgWithdrawSuccessful(double funds, double amount){
		print("received msgWithDrawSuccessful from teller");
		bankEvent = bankActivityEvent.WITHDRAW_SUCCESSFUL;
		working_capital += amount;
		getPersonAgent().CallstateChanged();
	}

	public void msgDepositSuccessful(double funds){
		print("received msgDepositSuccessful from teller");
		bankEvent = bankActivityEvent.DEPOSIT_SUCCESSFUL;
		working_capital = min_working_capital;
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

		/*if (!marketBills.isEmpty()) {
			payMarket(marketBills.get(0));
			return true;
		}*/

		/*if (loanRequested && loanGranted) {
			payWorkers();
			closeRestaurant();
			return true;
		}
		if (loanRequested && loanRefused) {
			depositExcessMoney();
			closeRestaurant();	
			return true;
		}*/
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
		if (leaveWork) 	{
			if (getPersonAgent().getRestaurant(0).isClosed && host.getCustomerSize() == 0) prepareToClose();
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
		log.add(new LoggedEvent("in clock in"));
		host = (Host) getPersonAgent().getHost(3);
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
		print("in action giveChangeToCustomer");
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
		log.add(new LoggedEvent("in action payWorkers, paying everybody"));
		working_capital -= getTotalSalary();
		for (People p : ((HostRoleWc)host).getWorkers()) {
			double money = p.getMoney();
			money += salary;
			p.setMoney(money);
		}
	}

	
	private void closeRestaurant() {
		log.add(new LoggedEvent("in action closeRestaurant, goint to the exit"));
		teller.msgDoneAndLeaving();
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
			cashierGui.setPresent(false);
			cashierGui.setDefaultDestination();
			getPersonAgent().msgDone("RestaurantCashierRole");
		}
	}

	private double getTotalSalary() {
		print ("in action getTotalSalary, calculating total salary for workeres");
		return (((HostRoleWc)host).getWorkers().size() * salary);
	}

	private void depositExcessMoney() {
		print ("in action depositExcessMoney, will message teller deposit");
		double amount = working_capital - min_working_capital;
		System.out.println("deposit monely " + amount + " to bank");
		teller.msgDeposit(getPersonAgent().getRestaurant(0).bankAccountID, working_capital - min_working_capital);
		bankState = bankActivityState.ASKED_DEPOSIT;
	}

	private void withdrawMoney() {
		print ("in action withdrawMoney, will message teller withdraw");
		double amount = getTotalSalary() + min_working_capital - working_capital;
		System.out.println("withdraw monely " + amount + " to bank");
		teller.msgWithdraw(getPersonAgent().getRestaurant(0).bankAccountID,getTotalSalary() + min_working_capital - working_capital);
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

	@Override
	public void msgGetOut() {
		// TODO Auto-generated method stub
		
	}


}