package bank;

import agent.Agent;
import bank.gui.BankCustomerGui;
import bank.gui.BankGui;
import bank.gui.TellerGui;
import bank.interfaces.BankCustomer;
import bank.interfaces.Robber;
import bank.interfaces.Teller;

import java.util.*;
import java.util.concurrent.Semaphore;

import city.Market;
import city.Restaurant;
import market.interfaces.MarketCashier;
import people.Role;
import restaurant.interfaces.Cashier;


/**
 * Bank Host Agent
 */

public class TellerRole extends Role implements Teller {

	public List<myBankCustomer> waitingCustomers
	= Collections.synchronizedList(new ArrayList<myBankCustomer>()); //For this prototype there is one teller who will store every waiting customer

	private String name;
	
	public enum CustomerState
	{none, waiting, beingHelped, deposit, newAccount, newAccountLoan, withdraw, loan, done, robbing};
	
	public myBankCustomer currentCustomer = null;
	
	public Map<Integer, Account> accounts = new HashMap<Integer, Account>();
	
	Boolean LeavePost = false;
	
	public Boolean isTest = false;
	
	private BankGui bgui;
	
	private TellerGui gui;
	
	private Semaphore atExit = new Semaphore(0,true);

	public TellerRole(BankGui b) {
		super();
		if (!isTest) this.bgui = b;
	}
	
	public void addAccount(Market m) {
		Account market = new Account("Market 1", accounts.size()+1); //Initializes an account for the restaurant
		accounts.put(market.id, market);
		market.funds = 10000;
		m.bankAccountID = market.id;
	}
	
	public void addAccount(Restaurant r) {
		Account rest = new Account("Restaurant 1", accounts.size()+1); //Initializes an account for the restaurant
		accounts.put(rest.id, rest);
		rest.funds = 10000;
		r.bankAccountID = rest.id;
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List getWaitingCustomers() {
		return waitingCustomers;
	}

	// Messages
	
	public void msgGone(){
		atExit.release();
		gui.leave = false;
	}
	
	public void msgIsActive(){
		print("Received msgIsActive");
		isActive = true;
		if (!isTest) {
			if (gui == null) gui = bgui.addPerson(this);
			else gui.isAtDesk = true;
		}
		stateChanged();
	}
	
	public void msgIsInActive(){
		print("Received msgIsInactive");
		myPerson.setMoney(myPerson.getMoney()+100);
		LeavePost = true;
		if (!isTest) gui.isAtDesk = false;
		stateChanged();
	}
	
	public void msgHere(BankCustomer cust, String name) {
		print("New customer. Added him to queue");
		waitingCustomers.add(new myBankCustomer(cust, name, "customer"));
		stateChanged();
	}
	
	public void msgHere(Robber cust, String name) {
		print("New customer. Added him to queue");
		waitingCustomers.add(new myBankCustomer(cust, name, "robber"));
		stateChanged();
	}
	
	public void msgGiveMoney(){
		print("I am being robbed");
		currentCustomer.state = CustomerState.robbing;
		stateChanged();
	}
	
	public void msgNeedHelp(Cashier cashier, String name) {
		print("Restaurant cashier called. Added him to queue");
		waitingCustomers.add(new myBankCustomer(cashier, name, "cashier"));
		stateChanged();
	}
	
	public void msgNeedHelp(MarketCashier mcashier, String name) {
		print("Market cashier called. Added him to queue");
		waitingCustomers.add(new myBankCustomer(mcashier, name, "mcashier"));
		stateChanged();
	}
	
	public void msgWithdraw(int accountID, double moneyNeeded) {
		print("Current customer has account and wants to withdraw");
		currentCustomer.account = accounts.get(accountID);
		currentCustomer.withdrawAmount = moneyNeeded;
		currentCustomer.state = CustomerState.withdraw;
		stateChanged();
	}
	
	public void msgWithdraw(double moneyNeeded) {
		print("Current customer has no account and wants to withdraw. Customer must take a loan");
		currentCustomer.withdrawAmount = moneyNeeded;
		currentCustomer.state = CustomerState.newAccountLoan;
		stateChanged();
	}
	
	public void msgDeposit(int accountID, double moneyGiven) {
		print("Current customer has account and wants to deposit");
		currentCustomer.account = accounts.get(accountID);
		currentCustomer.depositAmount = moneyGiven;
		currentCustomer.state = CustomerState.deposit;
		stateChanged();
	}
	
	public void msgDeposit(double moneyGiven) {
		print("Current customer has no account and wants to deposit");
		currentCustomer.depositAmount = moneyGiven;
		currentCustomer.state = CustomerState.newAccount;
		stateChanged();
	}
	
	public void msgDoneAndLeaving() {
		print("Current customer is finished and has left");
		currentCustomer.state = CustomerState.done;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		if (isActive) {
			if (waitingCustomers.size() != 0) {
				if (currentCustomer == null) {
					currentCustomer = waitingCustomers.get(0);
					callCustomer(currentCustomer);
					return true;
				}
				else {
					if (currentCustomer.state == CustomerState.newAccount) {
						newAccount(currentCustomer);
						return true;
					}
					if (currentCustomer.state == CustomerState.newAccountLoan) {
						newAccountLoan(currentCustomer);
						return true;
					}
					if (currentCustomer.state == CustomerState.deposit) {
						depositMoney(currentCustomer);
						return true;
					}
					if (currentCustomer.state == CustomerState.withdraw) {
						withdrawMoney(currentCustomer);
						return true;
					}
					if (currentCustomer.state == CustomerState.robbing) {
						giveMoney(currentCustomer);
						return true;
					}
					if (currentCustomer.state == CustomerState.done) {
						removeCustomer(currentCustomer);
						return true;
					}
				}
			}
			if (LeavePost && waitingCustomers.size() == 0 && currentCustomer == null) {
				Leave();
				return true;
			}
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void callCustomer(myBankCustomer customer) {
		customer.state = CustomerState.beingHelped;
		print("" +waitingCustomers.size());
		if (customer.type.equals("customer")) customer.customer.msgReadyToHelp(this);
		if (customer.type.equals("mcashier")) customer.mcashier.msgReadyToHelp(this);
		if (customer.type.equals("cashier")) customer.cashier.msgReadyToHelp(this);
	}
	
	private void newAccount(myBankCustomer customer) {
		customer.state = CustomerState.beingHelped;
		customer.account = new Account(customer.name, accounts.size()+1); //Initializes an account with a customer name and a unique account id.
		accounts.put(customer.account.id, customer.account);
		customer.account.funds += customer.depositAmount;
		customer.depositAmount = 0;
		customer.customer.msgAccountBalance(customer.account.id, customer.account.funds);
	}
	
	private void newAccountLoan(myBankCustomer customer) {
		customer.state = CustomerState.beingHelped;
		customer.account = new Account(customer.name, accounts.size()+1); //Initializes an account with a customer name and a unique account id.
		accounts.put(customer.account.id, customer.account);
		customer.account.funds -= customer.withdrawAmount;
		customer.customer.msgAccountAndLoan(customer.account.id, customer.account.funds, customer.withdrawAmount);
		customer.withdrawAmount = 0;
	}
	
	private void giveMoney(myBankCustomer customer) {
		customer.state = CustomerState.beingHelped;
		customer.robber.msgPleaseDontHurtMe(1000000);
		waitingCustomers.remove(customer);
		for(myBankCustomer cust:waitingCustomers){
			if (customer.type.equals("customer")) customer.customer.msgGetOut();
			if (customer.type.equals("mcashier")) customer.mcashier.msgGetOut();
			if (customer.type.equals("cashier")) customer.cashier.msgGetOut();
			if (customer.type.equals("robber")) customer.robber.msgGetOut();
		}
	}
	
	private void withdrawMoney(myBankCustomer customer) {
		customer.state = CustomerState.beingHelped;
		if (customer.withdrawAmount > customer.account.funds) {
			customer.account.funds -= customer.withdrawAmount;
			if (customer.type.equals("customer")) customer.customer.msgGiveLoan(customer.account.funds, customer.withdrawAmount);
			if (customer.type.equals("mcashier")) customer.mcashier.msgGiveLoan(customer.account.funds, customer.withdrawAmount);
			if (customer.type.equals("cashier")) customer.cashier.msgGiveLoan(customer.account.funds, customer.withdrawAmount);
			customer.withdrawAmount = 0;
		}
		else {
			customer.account.funds -= customer.withdrawAmount;
			if (customer.type.equals("customer")) customer.customer.msgWithdrawSuccessful(customer.account.funds, customer.withdrawAmount);
			if (customer.type.equals("mcashier")) customer.mcashier.msgWithdrawSuccessful(customer.account.funds, customer.withdrawAmount);
			if (customer.type.equals("cashier")) customer.cashier.msgWithdrawSuccessful(customer.account.funds, customer.withdrawAmount);
			customer.withdrawAmount = 0;
		}
	}
	
	private void depositMoney(myBankCustomer customer) {
		customer.state = CustomerState.beingHelped;
		customer.account.funds += customer.depositAmount;
		customer.depositAmount = 0;
		if (customer.type.equals("customer")) customer.customer.msgDepositSuccessful(customer.account.funds);
		if (customer.type.equals("mcashier")) customer.mcashier.msgDepositSuccessful(customer.account.funds);
		if (customer.type.equals("cashier")) customer.cashier.msgDepositSuccessful(customer.account.funds);
	}
	
	private void removeCustomer(myBankCustomer customer) {
		waitingCustomers.remove(customer);
		currentCustomer = null;
	}
	private void Leave() {
		isActive = false;
		LeavePost = false;
		if (!isTest) {
			gui.leave = true;
			gui.DoExitRestaurant();
			try {
				atExit.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		myPerson.msgDone("TellerRole");
	}
	
	//Utilities

	public class Account {
		int id;
		public double funds;
		String customerName;
		
		public Account(String name, int id) {
			this.id = id;
			this.customerName = name;
		}
	}
	
	public void setGui(TellerGui g) {
		gui = g;
	}
	
	public class myBankCustomer {
		BankCustomer customer;
		Cashier cashier;
		MarketCashier mcashier;
		Robber robber;
		public CustomerState state = CustomerState.none;
		Account account;
		double withdrawAmount = 0;
		double depositAmount = 0;
		String name;
		String type;
		
		myBankCustomer(BankCustomer customer, String name, String type) {
			this.customer = customer;
			this.state = CustomerState.none;
			this.name = name;
			this.type = type;
		}
		myBankCustomer(Cashier cashier, String name, String type) {
			this.cashier = cashier;
			this.state = CustomerState.none;
			this.name = name;
			this.type = type;
		}
		myBankCustomer(MarketCashier mcashier, String name, String type) {
			this.mcashier = mcashier;
			this.state = CustomerState.none;
			this.name = name;
			this.type = type;
		}
		myBankCustomer(Robber robber, String name, String type) {
			this.robber = robber;
			this.state = CustomerState.none;
			this.name = name;
			this.type = type;
		}
	}
}

