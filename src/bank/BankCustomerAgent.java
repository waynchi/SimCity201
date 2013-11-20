package bank;


import agent.Agent;
import bank.gui.BankCustomerGui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Bank customer agent.
 */
public class BankCustomerAgent extends Agent {
	private String name;

	Timer timer = new Timer();
	private BankCustomerGui bankCustomerGui;

	// agent correspondents
	private TellerAgent teller;
	
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
	public BankCustomerAgent(String name){
		super();
		this.name = name;
	}

	public String getCustomerName() {
		return name;
	}
	// Messages

	public void needMoney(double money) {//from animation
		print("I need money");
		action = CustomerAction.withdraw;
		withdraw = money;
		stateChanged();
	}
	
	public void depositMoney(double money) {//from animation
		print("I need to deposit money");
		action = CustomerAction.deposit;
		deposit = money;
		stateChanged();
	}
	
	public void msgReadyToHelp(TellerAgent t) {
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

		if (state == CustomerState.ready) {
	        if (action == CustomerAction.deposit) {
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
		if (state == CustomerState.needAccount) {
	        CreateAccount();
	        return true;
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

	private void CreateAccount(){
		teller.msgCreateAccount(name, deposit);
		state = CustomerState.finished;
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

