package restaurant_es;

import restaurant_es.gui.RestaurantGuiEs;
import restaurant.CashierRole.MarketBill;
import restaurant.interfaces.Cashier;
import restaurant_es.interfaces.Customer;
import restaurant_es.interfaces.Host;
import restaurant_es.interfaces.Waiter;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;

import java.util.*;
import java.util.concurrent.Semaphore;

import bank.interfaces.Teller;
import market.interfaces.MarketEmployee;
import people.People;
import people.Role;

public class CashierRoleEs extends Role implements Cashier {

	public boolean inTest = false;

	private Map<String, Double> price =Collections.synchronizedMap(new HashMap<String, Double>());

	private List<MarketBill> marketBills = Collections.synchronizedList(new ArrayList<MarketBill>());

	private Map<Customer, Double> balance = Collections.synchronizedMap(new HashMap<Customer, Double>());
	public enum checkState {COMPUTED, SENT_TO_WAITER, BEING_PAID};

	public enum bankActivityState {NONE, ASKED_FOR_HELP, ASKED_DEPOSIT, ASKED_WITHDRAW, DONE};
	public bankActivityState bankState;
	public enum bankActivityEvent {NONE, READY_TO_HELP, LOAN_GIVEN, DEPOSIT_SUCCESSFUL, WITHDRAW_SUCCESSFUL, BANK_CLOSED};
	public bankActivityEvent bankEvent;

	RestaurantGuiEs restGui;
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


	


	public CashierRoleEs(RestaurantGuiEs gui) {
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
			print("Market order is delivered, ready to pay");
			boolean orderFound = false;
			synchronized(marketBills){
				for (MarketBill mb : marketBills) {
					if (mb.orderNumber == orderNumber && mb.marketNumber == marketNumber) {
						mb.itemsReceived = true;
						orderFound = true;
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
			print("Must now pay the market");
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


	public void msgHereIsChange(double change) {
		print("Market cashier gave back: " + change);
		working_capital += change;
		getPersonAgent().CallstateChanged();

	}
	
	public void msgHereIsBill (Customer c, String food, Waiter w) {
		print("Got bill from waiter. Will compute now");
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
		print("Received money from Customer: " + c.getName() + " and the amount is: " + amount);
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

	public void msgReadyToHelp(Teller teller) {
		print("Bank teller is now ready to help me");
		bankEvent = bankActivityEvent.READY_TO_HELP;
		getPersonAgent().CallstateChanged();
	}

	public void msgGiveLoan(double balance, double amount) {
		print("Did not have enough money in the bank so I took out a loan");
		bankEvent = bankActivityEvent.WITHDRAW_SUCCESSFUL;
		working_capital += amount;
		getPersonAgent().CallstateChanged();
	}

	public void msgWithdrawSuccessful(double funds, double amount){
		print("Bank withdraw successful for: " + amount);
		bankEvent = bankActivityEvent.WITHDRAW_SUCCESSFUL;
		working_capital += amount;
		getPersonAgent().CallstateChanged();
	}

	public void msgDepositSuccessful(double funds){
		print("Bank deposit successful. Balance is: " + funds);
		bankEvent = bankActivityEvent.DEPOSIT_SUCCESSFUL;
		working_capital = min_working_capital;
		getPersonAgent().CallstateChanged();
	}
	
	public void msgGetOut() {
		print("Bank has been robbed. Teller told me to get out!");
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
		host = (Host) getPersonAgent().getHost(4);
		teller = (Teller) getPersonAgent().getTeller(0);
		host.setCashier(this);
		turnActive = false;
		deposit = false;
		withdraw = false;
		bankState = bankActivityState.NONE;
	}

	private void sendCheckToWaiter (final Check c) {
		c.waiter.msgHereIsCheck (c.customer, c.due);
		c.setState(checkState.SENT_TO_WAITER);
	}

	private void giveChangeToCustomer (Check c) {
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
		if (!inTest) {
		((MarketEmployee) getPersonAgent().getMarketEmployee(0)).getCashier().msgHereIsPayment(working_capital, bill.orderNumber, this);
		}
		setMyMoney(0);
		marketBills.remove(bill);
	}
	
	private void leaveWork() {
		isActive = false;
		getPersonAgent().msgDone("RestaurantCashierRole");
	}
	
	private void prepareToClose() {
		leaveWork = false;
		double total = getTotalSalary() + min_working_capital;
		if (working_capital >= total) {
			payWorkers(); 	
			teller.msgNeedHelp(this, "blah");
			bankState = bankActivityState.ASKED_FOR_HELP;	
			deposit = true;
		}

		else {
			teller.msgNeedHelp(this, "blah");
			bankState = bankActivityState.ASKED_FOR_HELP;
			withdraw = true;
		}
	}

	private void payWorkers() {
		working_capital -= getTotalSalary();
		for (People p : ((HostRoleEs)host).getWorkers()) {
			double money = p.getMoney();
			money += salary;
			p.setMoney(money);
		}
	}

	
	private void closeRestaurant() {
		teller.msgDoneAndLeaving();
		deposit = withdraw = false;
		isActive = false;
		leaveWork = false;
		bankEvent = bankActivityEvent.NONE;
		bankState = bankActivityState.NONE;
		getPersonAgent().msgDone("RestaurantCashierRole");
	}

	private double getTotalSalary() {
		return (((HostRoleEs)host).getWorkers().size() * salary);
	}

	private void depositExcessMoney() {
		double amount = working_capital - min_working_capital;
		teller.msgDeposit(getPersonAgent().getRestaurant(0).bankAccountID, amount);
		bankState = bankActivityState.ASKED_DEPOSIT;
	}

	private void withdrawMoney() {
		double amount = getTotalSalary() + min_working_capital - working_capital;
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
}