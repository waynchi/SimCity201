package market;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javax.smartcardio.ATR;

import bank.interfaces.Teller;
import people.Role;
import restaurant.CashierRole;
import restaurant.interfaces.Cashier;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;
import market.gui.MarketCashierGui;
import market.gui.MarketEmployeeGui;
import market.gui.MarketGui;
import market.interfaces.MarketCashier;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketEmployee;

public class MarketCashierRole extends Role implements MarketCashier{

	// data
	public boolean turnActive = false;
	public boolean leaveWork = false;
	private MarketEmployee marketEmployee;
	public EventLog log = new EventLog();
	public boolean inTest = false;
	MarketGui marketGui = null;
	MarketCashierGui marketCashierGui = null;

	private Semaphore atExit = new Semaphore(0,true);
	private Semaphore atPosition = new Semaphore(0,true);
	
	Map<String, Double> priceList = new HashMap<String, Double>();
	public double marketMoney = 10000.0;

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

	public void msgIsActive() {
		log.add(new LoggedEvent("received msgIsActive"));
		isActive = true;
		turnActive = true;
		getPersonAgent().CallstateChanged();
	}//tested

	public void msgIsInActive() {
		log.add(new LoggedEvent("received msgIsInActive"));
		leaveWork = true;
		getPersonAgent().CallstateChanged();
	}//tested
	
	
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
	}//tested

	// for restaurant Cashier
	public void msgHereIsACheck(Cashier restCashier, Map<String, Integer> items, int orderNumber) {
		if (!inTest)	log.add(new LoggedEvent("got a check for restaurant cashier " + ((CashierRole) restCashier).getName()));
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
				marketMoney += c.totalPaid;
				break;
			}
		}
		getPersonAgent().CallstateChanged();
	}


	// from restaurant cashier
	public void msgHereIsPayment(Double amount, Map<String, Integer> items, Cashier cashier) {
		log.add(new LoggedEvent("restaurant cashier " + ((CashierRole) cashier).getName() + " is paying " + amount));
		for (Check c : checks) {
			if (c.restaurantCashier == cashier && c.items == items) {
				c.state = checkState.PAID;
				c.totalPaid = amount;
				marketMoney += c.totalPaid;
				break;
			}
		}
		getPersonAgent().CallstateChanged();

	}
	
	// from bank teller

	@Override
	public void msgReadyToHelp(Teller teller) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void msgGiveLoan(double balance, double amount) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void msgWithdrawSuccessful(double funds, double amount) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void msgDepositSuccessful(double funds) {
		// TODO Auto-generated method stub
		
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
		
		if (leaveWork) {
			done();
			return true;
		}

		return false;
	}




	// action
	private void clockIn() {
		log.add(new LoggedEvent("clock in"));
		if (!inTest){
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
			check.totalDue += priceList.get(entry.getKey());
		}

		// if check is for restaurant
		if (check.restaurantCashier != null) {
			log.add(new LoggedEvent("sending check to restaurant cashier " + ((CashierRole) check.restaurantCashier).getName() + " and total due is " + check.totalDue));
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
		marketMoney -= change;
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
	
	private void done() {
		log.add(new LoggedEvent("in action done"));
		isActive = false;
		leaveWork = false;
		marketCashierGui.DoLeaveWork();
		try {
			atExit.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		marketCashierGui.setPresent(false);
		marketCashierGui.setDefaultDestination();
		getPersonAgent().msgDone("MarketCashierRole");
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


}
