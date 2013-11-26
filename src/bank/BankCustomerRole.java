package bank;


import agent.Agent;
import bank.gui.BankCustomerGui;
import bank.gui.BankGui;
import bank.interfaces.BankCustomer;
import bank.interfaces.Teller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import city.gui.trace.AlertLog;
import city.gui.trace.AlertTag;
import people.PeopleAgent.AgentEvent;
import people.Role;

/**
 * Bank customer agent.
 */
public class BankCustomerRole extends Role implements BankCustomer {
	private String name;

	Timer timer = new Timer();
	private BankCustomerGui gui;

	// agent correspondents
	private Teller teller;
	
	public int accountID = -1; //Initialize with an impossible value that will be checked later
	
	public enum CustomerState
	{none, waiting, ready, needAccount, finished, done, inline};
	
	public enum CustomerAction 
	{deposit, withdraw}
	
	private Semaphore atTeller = new Semaphore(0,true);
	
	public CustomerState state;
	private CustomerAction action;
	
	private double withdraw = 30000;
	private double deposit = 100;
	private BankGui bgui;
	
	public Boolean isTest = false;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public BankCustomerRole(BankGui b){
		super();
		this.bgui = b;
		if (!isTest) {
			b.addPerson(this);
		}
	}

	public String getCustomerName() {
		return name;
	}
	
	public void setTeller(Teller t) {
		teller = t;
	}
	// Messages
	
	public void msgAtTeller() {
		atTeller.release();
	}
	
	public void msgIsActive() {
		print("Recveived msgIsActive");
		if (!isTest) bgui.gotoLine(gui);
		isActive = true;
		state = CustomerState.inline;
		stateChanged();
	}
	
	public void msgReadyToHelp(Teller t) {
		print("Received msgReadyToHelp from teller");
		this.teller = t;
		state = CustomerState.ready;
		stateChanged();
	}
	
	public void msgAccountBalance(int accountID, double balance) {
		this.accountID = accountID;
		print("Account created. Account has a balance of: " + balance);
		state = CustomerState.done;
		stateChanged();
	}
	
	public void msgAccountAndLoan(int accountID, double balance, double money) {
		this.accountID = accountID;
		print("Account created. Account has a balance of: " + balance + ". Must pay teller next time for loan");
		myPerson.setMoney(myPerson.getMoney()+money);
		state = CustomerState.done;
		stateChanged();
	}
	
	public void msgGiveLoan(double balance, double money) {
		print("Account has a balance of: " + balance + ". Must pay teller next time for loan");
		myPerson.setMoney(myPerson.getMoney()+money);
		state = CustomerState.done;
		stateChanged();
	}
	
	public void msgWithdrawSuccessful(double balance, double money) {
		myPerson.setMoney(myPerson.getMoney()+money);
		print("Withdraw successful. Account has a balance of: " + balance);
		state = CustomerState.done;
		stateChanged();
	}
	
	public void msgDepositSuccessful(double balance) {
		print("Deposit successful. Account has a balance of: " + balance);
		state = CustomerState.done;
		stateChanged();
	}
	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine
		if (isActive) {
			if (state == CustomerState.inline) {
				CallTeller();
				return true;
			}
			if (state == CustomerState.ready) {
				if (myPerson.getAgentEvent().equals("GoingToDepositMoney")) {
					DepositMoney();
					return true;
				}
				if (myPerson.getAgentEvent().equals("GoingToRetrieveMoney")) {
					WithdrawMoney();
					return true;
				}
			}
			if (state == CustomerState.done) {
				LeaveBank();
				return true;
			}
		}
		
		return false;
	}

	// Actions
	
	private void CallTeller() {
		if (!isTest) ((Teller) myPerson.getTeller(0)).msgHere(this, name);
		if (isTest) myPerson.getTeller().msgHere(this, name);
		state = CustomerState.none;
	}

	private void DepositMoney(){
		if (!isTest) {
			atTeller.drainPermits();
			bgui.popCustomer();
			gui.DoGoToTeller();
			try {
				atTeller.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (accountID == -1) {
			myPerson.setMoney(myPerson.getMoney()-100);
			teller.msgDeposit(deposit);
			state = CustomerState.finished;
		}
		else {
			myPerson.setMoney(myPerson.getMoney()-100);
			teller.msgDeposit(accountID, deposit);
			state = CustomerState.finished;
		}
	}

	private void WithdrawMoney(){
		if (!isTest) {
			atTeller.drainPermits();
			bgui.popCustomer();
			gui.DoGoToTeller();
			try {
				atTeller.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (accountID == -1) {
			teller.msgWithdraw(withdraw);
			state = CustomerState.finished;
		}
		else {
			teller.msgWithdraw(accountID, withdraw);
			state = CustomerState.finished;
		}
	}

	private void LeaveBank(){
		if (!isTest) gui.DoLeaveBank();
		teller.msgDoneAndLeaving();
		myPerson.msgDone("BankCustomerRole");
		isActive = false;
	}

	// Accessors, etc.

	public String getName() {
		return name;
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(BankCustomerGui g) {
		gui = g;
	}

	public BankCustomerGui getGui() {
		return gui;
	}
	
}

