package bank;


import agent.Agent;
import bank.gui.BankCustomerGui;
import bank.interfaces.BankCustomer;
import bank.interfaces.Teller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import people.PeopleAgent.AgentEvent;
import people.Role;

/**
 * Bank customer agent.
 */
public class BankCustomerRole extends Role implements BankCustomer {
	private String name;

	Timer timer = new Timer();
	private BankCustomerGui bankCustomerGui;

	// agent correspondents
	private Teller teller;
	
	private double wallet;
	private int accountID = -1; //Initialize with an impossible value that will be checked later
	
	public enum CustomerState
	{none, waiting, ready, needAccount, finished, done};
	
	public enum CustomerAction 
	{deposit, withdraw}
	
	private CustomerState state;
	private CustomerAction action;
	
	private double withdraw;
	private double deposit;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public BankCustomerRole(String name){
		super();
		this.name = name;
	}

	public String getCustomerName() {
		return name;
	}
	
	public void setTeller(Teller t) {
		teller = t;
	}
	// Messages
	
	public void msgIsActive() {
		isActive = true;
		stateChanged();
	}
	
	public void msgIsInactive() {
		isActive = false;
		stateChanged();
	}
	
	public void msgReadyToHelp(Teller t) {
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
		
	}
	
	public void msgGiveLoan(double balance, double money) {
		print("Account has a balance of: " + balance + ". Must pay teller next time for loan");
		wallet += money;
		state = CustomerState.done;
		stateChanged();
	}
	
	public void msgWithdrawSuccessful(double balance, double money) {
		wallet += money;
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
			if (state == CustomerState.ready) {
				if (myPerson.event == AgentEvent.GoingToDepositMoney) {
					DepositMoney();
					return true;
				}
				if (action == CustomerAction.withdraw) {
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

	private void DepositMoney(){
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
		//Do Leave Bank
		teller.msgDoneAndLeaving();
	}

	// Accessors, etc.

	public String getName() {
		return name;
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(BankCustomerGui g) {
		bankCustomerGui = g;
	}

	public BankCustomerGui getGui() {
		return bankCustomerGui;
	}
	
}

