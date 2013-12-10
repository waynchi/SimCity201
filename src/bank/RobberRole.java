package bank;


import agent.Agent;
import bank.gui.BankCustomerGui;
import bank.gui.BankGui;
import bank.gui.RobberGui;
import bank.interfaces.BankCustomer;
import bank.interfaces.Robber;
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
public class RobberRole extends Role implements Robber {
	private String name;

	Timer timer = new Timer();
	private RobberGui gui;

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
	public RobberRole(BankGui b){
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
	
	public void msgPleaseDontHurtMe(double Money){
		print("I stole " + Money + " dollars from the bank, time to leave");
		myPerson.setMoney(myPerson.getMoney()+Money);
		state = CustomerState.done;
		stateChanged();
	}
	
	public void msgGetOut() {
		print("Teller asked me to leave");
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
				RobBank();
				return true;
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
	
	private void RobBank() {
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
		teller.msgGiveMoney();
		state = CustomerState.finished;
	}

	private void LeaveBank(){
		if (!isTest) gui.DoLeaveBank();
		myPerson.msgDone("RobberRole");
		isActive = false;
	}

	// Accessors, etc.

	public String getName() {
		return name;
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(RobberGui g) {
		gui = g;
	}

	public RobberGui getGui() {
		return gui;
	}
	
}

