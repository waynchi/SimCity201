package market;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import bank.interfaces.Teller;
import people.People;
import people.Role;
import restaurant.interfaces.Cashier;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;
import market.gui.MarketCashierGui;
import market.gui.MarketGui;
import market.interfaces.MarketCashier;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketEmployee;
import market.test.MockPeople;

public class MarketCashierRole extends Role implements MarketCashier{

	// data

	public EventLog log = new EventLog();
	public boolean inTest = false;

	public boolean turnActive = false;
	public boolean leaveWork = false;
	private MarketEmployee marketEmployee;
	MarketGui marketGui = null;
	MarketCashierGui marketCashierGui = null;

	public enum bankActivityState {NONE, ASKED_FOR_HELP, ASKED_DEPOSIT, ASKED_WITHDRAW, DONE}
	public bankActivityState bankState;
	public enum bankActivityEvent {NONE, READY_TO_HELP, LOAN_GIVEN, DEPOSIT_SUCCESSFUL, WITHDRAW_SUCCESSFUL, BANK_CLOSED}
	public bankActivityEvent bankEvent;

	private Teller teller;
	private Boolean deposit = false;
	private Boolean withdraw = false;

	public double working_capital = 10000.0;
	private double min_working_capital = 1000.0;
	private double salary = 200.0;

	private Semaphore atExit = new Semaphore(0,true);
	private Semaphore atPosition = new Semaphore(0,true);

	Map<String, Double> priceList = new HashMap<String, Double>();

	private enum checkState {PENDING, SENT, PAID};
	public List<Check> checks = new ArrayList<Check>();
	public class Check {
		MarketCustomer customer = null;
		Cashier restaurantCashier = null;
		Map<String, Integer> items = new HashMap<String, Integer>();
		double totalPaid;
		public double totalDue;
		checkState state;
		int orderNumber;

		// Check constructor for a regular MarketCustomer
		public Check (MarketCustomer cust, Map<String, Integer> _items) {
			customer = cust;
			items = _items;
			state = checkState.PENDING;
			totalPaid = totalDue = 0.0;
		}

		// Check constructor for a restaurant
		public Check(Cashier _restaurantCashier, Map<String, Integer> _items, int _orderNumber) {
			restaurantCashier = _restaurantCashier;
			items = _items;
			state = checkState.PENDING;
			totalPaid = totalDue = 0.0;
			orderNumber = _orderNumber;
		}
	}

	public MarketCashierRole(MarketGui gui) {
		marketGui = gui;
		marketCashierGui = new MarketCashierGui(this);
		marketGui.getAnimationPanel().addGui(marketCashierGui);
		marketCashierGui.setPresent(false);
		priceList.put("Steak", 7.99);
		priceList.put("Chicken", 7.99);
		priceList.put("Salad", 2.99);
		priceList.put("Pizza", 3.99);
		priceList.put("Car", 20000.0);
	}



	// messages

	// from people agent
	public void msgIsActive() {
		log.add(new LoggedEvent("received msgIsActive"));
		isActive = true;
		turnActive = true;
		getPersonAgent().CallstateChanged();
	}

	public void msgIsInActive() {
		log.add(new LoggedEvent("received msgIsInActive"));
		leaveWork = true;
		getPersonAgent().CallstateChanged();
	}

	// from gui
	public void msgAtExit() {
		atExit.release();
		getPersonAgent().CallstateChanged();
	}

	public void msgAtPosition() {
		atPosition.release();
		getPersonAgent().CallstateChanged();
	}


	// for regular Market Customer
	public void msgHereIsACheck(MarketCustomer customer, Map<String, Integer> items){
		if (!inTest)	log.add(new LoggedEvent("got a check for customer " + customer.getPerson().getName() ));
		checks.add(new Check(customer, items));
		getPersonAgent().CallstateChanged();
	}

	// for restaurant Cashier
	public void msgHereIsACheck(Cashier restCashier, Map<String, Integer> items, int orderNumber) {
		if (!inTest)	log.add(new LoggedEvent("got a check for restaurant cashier " + restCashier.getName()));
		checks.add(new Check(restCashier, items, orderNumber));
		getPersonAgent().CallstateChanged();

	}



	// from regular market customer
	public void msgHereIsPayment(MarketCustomer customer, double totalPaid) {
		if (!inTest) log.add(new LoggedEvent("marketCustomer " + customer.getPerson().getName() + " is paying " + totalPaid));
		for (Check c : checks) {
			if (c.customer == customer) {
				c.state = checkState.PAID;
				c.totalPaid = totalPaid;
				working_capital += c.totalPaid;
				break;
			}
		}
		getPersonAgent().CallstateChanged();
	}

	// from restaurant cashier
	public void msgHereIsPayment(Double amount, Map<String, Integer> items, Cashier cashier) {
		log.add(new LoggedEvent("restaurant cashier " + cashier.getName() + " is paying " + amount));
		for (Check c : checks) {
			if (c.restaurantCashier == cashier && c.items == items) {
				c.state = checkState.PAID;
				c.totalPaid = amount;
				working_capital += c.totalPaid;
				break;
			}
		}
		getPersonAgent().CallstateChanged();

	}



	// from BankTellerRole
	public void msgReadyToHelp(Teller teller) {
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
		log.add(new LoggedEvent("received msgWithDrawSuccessful from teller"));
		bankEvent = bankActivityEvent.WITHDRAW_SUCCESSFUL;
		working_capital += amount;
		getPersonAgent().CallstateChanged();
	}

	public void msgDepositSuccessful(double funds){
		log.add(new LoggedEvent("received msgDepositSuccessful from teller"));
		bankEvent = bankActivityEvent.DEPOSIT_SUCCESSFUL;
		working_capital = min_working_capital;
		getPersonAgent().CallstateChanged();
	}

	public void msgGetOut() {
		log.add(new LoggedEvent("received msgGetOut from teller"));
		bankEvent = bankActivityEvent.BANK_CLOSED;
		getPersonAgent().CallstateChanged();
	}



	// scheduler
	public boolean pickAndExecuteAnAction(){
		if (turnActive) {
			clockIn();
			return true;
		}

		for (Check c : checks) {
			if (c.state == checkState.PENDING) {
				computeAndSendCheck(c);
				return true;
			}
		}

		for (Check c : checks) {
			if (c.state == checkState.PAID) {
				giveChangeToCustomer(c);
				return true;
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
			closeMarket();
			return true;
		}
		if (bankState == bankActivityState.ASKED_WITHDRAW && bankEvent == bankActivityEvent.WITHDRAW_SUCCESSFUL) {
			bankState = bankActivityState.NONE;
			payWorkers();
			closeMarket();
			return true;
		} 

		if (bankEvent == bankActivityEvent.BANK_CLOSED) {
			closeMarket();
			return true;
		}

		if (leaveWork) {
			if (!inTest) {
				if (getPersonAgent().getMarket(0).isClosed && checks.isEmpty()) {
					if (getPersonAgent().getBank(0).isClosed) {
						closeMarket();
					}
					else {
						prepareToClose();
					}
				}
				else if (!getPersonAgent().getMarket(0).isClosed) {
					leaveWork();
				}
			}

			else {
				if (((MockPeople)getPersonAgent()).getMyMarket(0).isClosed && checks.isEmpty()) {
					prepareToClose();
				}
				else if (!((MockPeople)getPersonAgent()).getMyMarket(0).isClosed) {
					leaveWork();
				}
			}
			return true;
		}

		return false;
	}




	// action
	private void clockIn() {
		log.add(new LoggedEvent("clock in"));
		if (!inTest){
			teller = (Teller) getPersonAgent().getTeller(0);
			marketEmployee = (MarketEmployee) getPersonAgent().getMarketEmployee(0);
			marketEmployee.setCashier(this);
			marketCashierGui.setPresent(true);
			marketCashierGui.DoGoToWorkingPosition();
			try {
				atPosition.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		turnActive = false;
	}

	private void computeAndSendCheck(Check check) {
		//compute the total amount
		for (Map.Entry<String,Integer> entry : check.items.entrySet()) {
			check.totalDue += priceList.get(entry.getKey()) * entry.getValue();
		}

		// if check is for restaurant
		if (check.restaurantCashier != null) {
			log.add(new LoggedEvent("sending check to restaurant cashier " + check.restaurantCashier.getName() + " and total due is " + check.totalDue));
			check.restaurantCashier.msgHereIsWhatIsDue( check.totalDue, check.items, check.orderNumber);
		}

		// check is for market customer
		else { 
			log.add(new LoggedEvent("sending check to customer and total due is " + check.totalDue));
			check.customer.msgHereIsWhatIsDue(check.totalDue, this);
		}
		check.state = checkState.SENT;
	}


	private void giveChangeToCustomer(Check check) {
		double change = check.totalPaid - check.totalDue;
		working_capital -= change;
		if (check.restaurantCashier != null) {
			log.add(new LoggedEvent("giving change to restaurant cashier and the amount is " + change));
			check.restaurantCashier.msgHereIsChange (change);
		}

		else { // check is for regular customer
			log.add(new LoggedEvent("giving change to customer and the amount is " + change));
			check.customer.msgHereIsChange(change);
		}
		checks.remove(check);
	}

	private void prepareToClose() {
		log.add(new LoggedEvent("In action prepareToClose"));
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

	private double getTotalSalary() {
		log.add(new LoggedEvent("in action getTotalSalary, calculating total salary for workeres"));
		return (((MarketEmployeeRole)marketEmployee).getWorkers().size() * salary);
	}

	private void payWorkers() {
		log.add(new LoggedEvent("in action payWorkers, paying everybody"));
		working_capital -= getTotalSalary();
		for (People p : ((MarketEmployeeRole)marketEmployee).getWorkers()) {
			double money = p.getMoney();
			money += salary;
			p.setMoney(money);
		}
	}

	private void closeMarket() {
		log.add(new LoggedEvent("in action closeMarket, goint to the exit"));
		if (!getPersonAgent().getBank(0).isClosed) {
			teller.msgDoneAndLeaving();
		}
		deposit = withdraw = false;
		isActive = false;
		if (!inTest){
			marketCashierGui.DoLeaveWork();
			try {
				atExit.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			leaveWork = false;
			marketCashierGui.setPresent(false);
			marketCashierGui.setDefaultDestination();
			getPersonAgent().msgDone("MarketCashierRole");
		}

	}

	private void leaveWork() {
		log.add(new LoggedEvent("in action leave work"));
		isActive = false;
		leaveWork = false;
		if (!inTest) {
			marketCashierGui.DoLeaveWork();
			try {
				atExit.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			marketCashierGui.setPresent(false);
			marketCashierGui.setDefaultDestination();
		}
		getPersonAgent().msgDone("MarketCashierRole");
	}

	private void depositExcessMoney() {
		double amount = working_capital - min_working_capital;
		log.add(new LoggedEvent("in action depositExcessMoney, about to deposit " + amount + " from bank"));
		teller.msgDeposit(getPersonAgent().getMarket(0).bankAccountID, working_capital - min_working_capital);
		bankState = bankActivityState.ASKED_DEPOSIT;
	}

	private void withdrawMoney() {
		double amount = getTotalSalary() + min_working_capital - working_capital;
		log.add(new LoggedEvent("in action withdrawMoney, about to withdraw " + amount + " from bank"));
		teller.msgWithdraw(getPersonAgent().getMarket(0).bankAccountID,getTotalSalary() + min_working_capital - working_capital);
		bankState = bankActivityState.ASKED_WITHDRAW;
	}


	//utilities
	public Boolean isActive() {
		return isActive;
	}

	public String getName() {
		return getPersonAgent().getName();
	}

	public void setMarketEmployee(MarketEmployee me) {
		this.marketEmployee = me;
	}

	public void setTeller(Teller t) {
		this.teller = t;
	}


}
