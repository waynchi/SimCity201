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
	
	private double wallet;
	private int accountID = -1; //Initialize with an impossible value that will be checked later
	
	public enum CustomerState
	{none, waiting, ready, needAccount, finished, done, inline};
	
	public enum CustomerAction 
	{deposit, withdraw}
	
	private Semaphore atTeller = new Semaphore(0,true);
	
	private CustomerState state;
	private CustomerAction action;
	
	private double withdraw = 100;
	private double deposit = 100;
	private BankGui bgui;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public BankCustomerRole(BankGui b){
		super();
		this.bgui = b;
		b.addPerson(this);
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
		bgui.gotoLine(gui);
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
		myPerson.Money += money;
		state = CustomerState.done;
		stateChanged();
	}
	
	public void msgGiveLoan(double balance, double money) {
		print("Account has a balance of: " + balance + ". Must pay teller next time for loan");
		myPerson.Money += money;
		state = CustomerState.done;
		stateChanged();
	}
	
	public void msgWithdrawSuccessful(double balance, double money) {
		myPerson.Money += money;
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
	protected boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine
		if (isActive) {
			if (state == CustomerState.inline) {
				CallTeller();
			}
			if (state == CustomerState.ready) {
				if (myPerson.event == AgentEvent.GoingToDepositMoney) {
					DepositMoney();
					return true;
				}
				if (myPerson.event == AgentEvent.GoingToRetrieveMoney) {
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
		myPerson.Banks.get(0).t.msgHere(this, name);
		state = CustomerState.none;
	}

	private void DepositMoney(){
		atTeller.drainPermits();
		bgui.popCustomer();
		gui.DoGoToTeller();
		try {
			atTeller.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (accountID == -1) {
			wallet -= deposit;
			teller.msgDeposit(deposit);
			deposit = 0;
			state = CustomerState.finished;
		}
		else {
			wallet -= deposit;
			teller.msgDeposit(accountID, deposit);
			deposit = 0;
			state = CustomerState.finished;
		}
	}

	private void WithdrawMoney(){
		atTeller.drainPermits();
		bgui.popCustomer();
		gui.DoGoToTeller();
		try {
			atTeller.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (accountID == -1) {
			print("Cannot withdraw money without an account");
			state = CustomerState.done;
		}
		else {
			teller.msgWithdraw(accountID, withdraw);
			withdraw = 0;
			state = CustomerState.finished;
		}
	}

	private void LeaveBank(){
		gui.DoLeaveBank();
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

