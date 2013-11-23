package bank;

import agent.Agent;
import bank.interfaces.BankCustomer;
import bank.interfaces.Teller;

import java.util.*;
import java.util.concurrent.Semaphore;

import people.Role;


/**
 * Bank Host Agent
 */

public class TellerRole extends Role implements Teller {

	public List<myBankCustomer> waitingCustomers
	= Collections.synchronizedList(new ArrayList<myBankCustomer>()); //For this prototype there is one teller who will store every waiting customer

	private String name;
	
	public enum CustomerState
	{none, waiting, beingHelped, deposit, newAccount, withdraw, loan, done};
	
	private myBankCustomer currentCustomer = null;
	
	public Map<Integer, Account> accounts = new HashMap<Integer, Account>();

	public TellerRole(String name) {
		super();

		this.name = name;
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
	
	public void msgHere(BankCustomer cust) {
		waitingCustomers.add(new myBankCustomer(cust));
		stateChanged();
	}

	public void msgCreateAccount(String name, double initialFund) {
		currentCustomer.account = new Account(name, accounts.size()+1); //Initializes an account with a customer name and a unique account id.
		currentCustomer.depositAmount = initialFund;
		currentCustomer.state = CustomerState.deposit;
		stateChanged();
	}
	
	public void msgWithdraw(int accountID, double moneyNeeded) {
		currentCustomer.account = accounts.get(accountID);
		currentCustomer.withdrawAmount = moneyNeeded;
		currentCustomer.state = CustomerState.withdraw;
		stateChanged();
	}
	
	public void msgDeposit(int accountID, double moneyGiven) {
		currentCustomer.account = accounts.get(accountID);
		currentCustomer.depositAmount = moneyGiven;
		currentCustomer.state = CustomerState.deposit;
		stateChanged();
	}
	
	public void msgDeposit(double moneyGiven) {
		currentCustomer.depositAmount = moneyGiven;
		currentCustomer.state = CustomerState.newAccount;
		stateChanged();
	}
	
	public void msgDoneAndLeaving() {
		currentCustomer.state = CustomerState.done;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
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
				if (currentCustomer.state == CustomerState.deposit) {
					depositMoney(currentCustomer);
					return true;
				}
				if (currentCustomer.state == CustomerState.withdraw) {
					withdrawMoney(currentCustomer);
					return true;
				}
				if (currentCustomer.state == CustomerState.done) {
					removeCustomer(currentCustomer);
					return true;
				}
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
		customer.customer.msgReadyToHelp(this);
	}
	
	private void newAccount(myBankCustomer customer) {
		customer.state = CustomerState.beingHelped;
		customer.account.funds += customer.depositAmount;
		customer.depositAmount = 0;
		customer.customer.msgAccountBalance(customer.account.id, customer.account.funds);
	}
	
	private void withdrawMoney(myBankCustomer customer) {
		customer.state = CustomerState.beingHelped;
		if (customer.withdrawAmount > customer.account.funds) {
			customer.account.funds -= customer.withdrawAmount;
			customer.customer.msgGiveLoan(customer.account.funds, customer.withdrawAmount);
			customer.withdrawAmount = 0;
		}
		else {
			customer.account.funds -= customer.withdrawAmount;
			customer.customer.msgWithdrawSuccessful(customer.account.funds, customer.withdrawAmount);
			customer.withdrawAmount = 0;
		}
	}
	
	private void depositMoney(myBankCustomer customer) {
		customer.state = CustomerState.beingHelped;
		customer.account.funds += customer.depositAmount;
		customer.depositAmount = 0;
		customer.customer.msgDepositSuccessful(customer.account.funds);
	}
	
	private void removeCustomer(myBankCustomer customer) {
		waitingCustomers.remove(customer);
		currentCustomer = null;
	}
	
	//Utilities

	private class Account {
		int id;
		double funds;
		String customerName;
		
		Account(String name, int id) {
			this.id = id;
			this.customerName = name;
		}
	}
	
	private class myBankCustomer {
		BankCustomer customer;
		private CustomerState state = CustomerState.none;
		Account account;
		double withdrawAmount = 0;
		double depositAmount = 0;
		String name;
		
		myBankCustomer(BankCustomer customer) {
			this.customer = customer;
			this.state = CustomerState.none;
		}
	}
}

