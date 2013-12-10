package restaurant_vk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import people.PeopleAgent;
import people.Role;
import bank.interfaces.Teller;
import restaurant.interfaces.Cashier;
import market.interfaces.*;
import restaurant_vk.interfaces.Customer;
import restaurant_vk.interfaces.Host;
import restaurant_vk.interfaces.Waiter;
import restaurant_vk.gui.VkCashierGui;
import restaurant_vk.gui.RestaurantVkAnimationPanel;

/**
 * @author Vikrant Singhal
 *
 * A class representing the Cashier. This cashier prepares checks on requests
 * of the waiter and gives them to the respective waiters. Also, customers come
 * to the cashier and pay the money.
 */
public class VkCashierRole extends Role implements Cashier {
	// Data
	
	private List<MyCheck> checks = Collections.synchronizedList(new ArrayList<MyCheck>());
	private List<Bill> bills = Collections.synchronizedList(new ArrayList<Bill>());
	private Menu m = new Menu();
	private MyCheck currentCheck = null;
	private VkCashierGui gui = null;
	private double workingCapital = 10000.0;
	private double minCapital = 1000;
	private Timer timer = new Timer();
	private final int TIME_TO_CHECK_OUT = 1000;
	private MarketCashier mCashier;
	private boolean leave = false;
	private boolean enter = false;
	private boolean deposit = false;
	private boolean withdraw = false;
	private BankActivity bankActivity = BankActivity.None;
	private ClosingState closingState = ClosingState.Closed;
	public List<Shift> shiftRecord = new ArrayList<Shift>();
	public Teller teller;
	private double loanedMoney = 0;
	public VkHostRole host;
	private Semaphore movingAround = new Semaphore(0, true);
	
	public double waiterSalary = 100;
	public double cashierSalary = 100;
	public double cookSalary = 100;
	public double hostSalary = 100;
	
	public VkCashierRole(RestaurantVkAnimationPanel p) {
		gui = new VkCashierGui(this);
		gui.setAnimationPanel(p);
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
	
	// Messages
	
	/*
	 * This message is called by the waiter asking the cashier to
	 * compute the bill;
	 */
	public void computeBill(Customer cust, String choice, Waiter w) {
		double p = m.getPrice(choice);
		CustomerRestaurantCheck c = new CustomerRestaurantCheck(cust, p, choice);
		MyCheck mc = new MyCheck(c, cust, w);
		synchronized (checks) {
			checks.add(mc);
		}
		stateChanged();
	}
	
	/*
	 * This message is called by the customer to pay for the food.
	 */
	public void hereIsPayment(CustomerRestaurantCheck c, double cash, List<CustomerRestaurantCheck> l) {
		MyCheck mc = find(c);
		mc.amountPaid = cash;
		mc.l = l;
		mc.s = CheckState.BeingPaid;
		stateChanged();
	}
	
	/*
	 * A message called by the cook to tell the cashier that he has received
	 * the items he had requested from the market.
	 */
	@Override
	public void msgGotMarketOrder(Map<String, Integer> marketOrder, int orderNumber) {
		boolean found = false;
		for (Bill b : bills) {
			if (b.orderNumber == orderNumber) {
				found = true;
				b.itemsFromCook = marketOrder;
				break;
			}
		}
		if (found == false) {
			Bill b = new Bill(orderNumber);
			b.itemsFromCook = marketOrder;
			bills.add(b);
		}
		stateChanged();
	}

	/*
	 * A message called by the market cashier to give the bill to the cook.
	 */
	@Override
	public void msgHereIsWhatIsDue(double price, Map<String, Integer> items, int orderNumber) {
		boolean found = false;
		for (Bill b : bills) {
			if (b.orderNumber == orderNumber) {
				found = true;
				b.itemsFromMarket = items;
				b.cost = price;
				break;
			}
		}
		if (found == false) {
			Bill b = new Bill(orderNumber);
			b.cost = price;
			b.itemsFromMarket = items;
			bills.add(b);
		}
		stateChanged();
	}

	/*
	 * A message called by the market cashier to give back the change to
	 * the cashier.
	 */
	@Override
	public void msgHereIsChange(double change) {
		workingCapital += change;
		stateChanged();
	}

	/*
	 * A message called by the teller to inform that it is ready to help
	 * the cashier.
	 */
	@Override
	public void msgReadyToHelp(Teller teller) {
		bankActivity = BankActivity.ReadyToHelp;
		stateChanged();
	}

	/*
	 * A message called by the teller to tell the amount of loaned money.
	 */
	@Override
	public void msgGiveLoan(double funds, double amount) {
		workingCapital += amount;
		loanedMoney += amount;
		bankActivity = BankActivity.None;
		stateChanged();
	}

	/*
	 * A message called by the cashier to tell the amount of money withdrawn.
	 */
	@Override
	public void msgWithdrawSuccessful(double funds, double amount) {
		workingCapital += amount;
		bankActivity = BankActivity.None;
		stateChanged();
	}

	/*
	 * A message called by the teller to tell the amount of money deposited. 
	 */
	@Override
	public void msgDepositSuccessful(double funds) {
		workingCapital = minCapital;
		bankActivity = BankActivity.None;
		stateChanged();
	}
	
	/*
	 * A message that each role calls when he/ she is leaving. This is to
	 * record their shift so that they could be paid by the cashier.
	 */
	public void recordShift(PeopleAgent p, String role) {
		shiftRecord.add(new Shift(p, role));
		stateChanged();
	}
	
	public void closeRestaurant() {
		closingState = ClosingState.ToBeClosed;
		this.recordShift(((PeopleAgent)myPerson), "Cashier");
		stateChanged();
	}
	
	public void msgIsActive() {
		if (mCashier == null) {
			this.mCashier = ((MarketEmployee)myPerson.getMarketEmployee(0)).getCashier();
		}
		if (teller == null) {
			this.teller = (Teller) myPerson.getTeller(0);
		}
		enter = true;
		isActive = true;
		stateChanged();
	}
	
	public void msgIsInActive() {
		leave = true;
		stateChanged();
	}
	
	public void activityDone() {
		movingAround.release();
	}
	
	@Override
	public void msgGetOut() {
		bankActivity = BankActivity.Robbed;
		stateChanged();
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
	
	// Actions
	
	/*
	 * An action to deliver the newly prepared bill to the waiter.
	 */
	private void deliverCheckToWaiter(MyCheck mc) {
		print("Delivering check to waiter.");
		mc.w.hereIsCheck(mc.c, mc.cust);
		mc.s = CheckState.Delivered;
	}
	
	/*
	 * An action to decide the response to the customer being handled.
	 */
	private void decideResponseForCheck(final MyCheck mc) {
		final Semaphore s = new Semaphore(0, true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				s.release();
			}
		}, TIME_TO_CHECK_OUT);
		
		try {
			s.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		List<CustomerRestaurantCheck> approvedPayments = new ArrayList<CustomerRestaurantCheck>();
		double cashLeft = mc.amountPaid;
		List<CustomerRestaurantCheck> customerChecks = mc.l;
		for (MyCheck m : checks) {
			for (CustomerRestaurantCheck c : customerChecks) {
				if (m.c == c && (m.s == CheckState.PaidLess || m.s == CheckState.BeingPaid)) {
					if (cashLeft >= m.c.getCost()) {
						cashLeft = cashLeft - m.c.getCost();
						workingCapital += m.c.getCost();
						m.amountPaid = m.c.getCost();
						m.s = CheckState.Paid;
						approvedPayments.add(c);
					}
					else {
						m.amountPaid = 0.0;
						m.s = CheckState.PaidLess;
					}
					m.l = null;
				}
			}
		}
		mc.cust.hereIsChangeAndApprovedPayments(cashLeft, approvedPayments);
		print("Payment done. Here are your change and pending checks.");
	}
	
	/*
	 * An action to verify a bill before paying it.
	 */
	public void verifyBill(Bill b) {
		print("Verifying market bill.");
		Set<Map.Entry<String, Integer>> cookItems = b.itemsFromCook.entrySet();
		Set<Map.Entry<String, Integer>> marketItems = b.itemsFromMarket.entrySet();
		for (Map.Entry<String, Integer> ci : cookItems) {
			boolean found = false;
			for (Map.Entry<String, Integer> mi : marketItems) {
				if (mi.getKey().equals(ci.getKey()) && mi.getValue().equals(ci.getValue())) {
					found = true;
					break;
				}
			}
			if (found == false) {
				b.s = BillState.Fraud;
				return;
			}
		}
		b.s = BillState.Verified;
	}
	
	/*
	 * An action to pay bill to a market.
	 */
	private void payBill(Bill b) {
		print("Paying market bill.");
		mCashier.msgHereIsPayment(b.cost, b.orderNumber, this);
		b.s = BillState.Paid;
		workingCapital -= b.cost;
	}
	
	/*
	 * Action to pay an employee his/ her salary.
	 */
	public void payEmployee(Shift s) {
		if (s.role.equals("Waiter")) {
			s.p.Money += waiterSalary;
		}
		else if (s.role.equals("Cook")) {
			s.p.Money += cookSalary;
		}
		else if (s.role.equals("Host")) {
			s.p.Money += hostSalary;
		}
		else if (s.role.equals("Cashier")) {
			s.p.Money += cashierSalary;
		}
		s.s = ShiftState.Done;
	}
	
	/*
	 * Place request to the market to either deposit or withdraw money.
	 */
	public void orderBank() {
		if (deposit == true) {
			bankActivity = BankActivity.DepositRequested;
			deposit = false;
			System.out.println("Asking teller to deposit.");
			teller.msgDeposit(getPersonAgent().getRestaurant(1).bankAccountID, workingCapital - minCapital);
		}
		else if (withdraw == true) {
			bankActivity = BankActivity.WithdrawalRequested;
			withdraw = false;
			double total = 0.0;
			total += (computeRequiredMoney() - workingCapital);
			System.out.println("Asking teller to withdraw.");
			teller.msgWithdraw(getPersonAgent().getRestaurant(1).bankAccountID, total);
		}
	}
	
	/*
	 * Preparing to close down.
	 */
	public void prepareToClose() {
		if (myPerson.getBank(0).isClosed == false) {
			if (workingCapital > minCapital && shiftRecord.isEmpty()) {
				teller.msgNeedHelp(this, "blah");
				bankActivity = BankActivity.HelpRequested;
				deposit = true;
			}
			else {
				teller.msgNeedHelp(this, "blah");
				bankActivity = BankActivity.HelpRequested;
				withdraw = true;
			}
		}
		closingState = ClosingState.Preparing;
	}
	
	/*
	 * An action to shut the restaurant down.
	 */
	public void shutDown() {
		closingState = ClosingState.Closed;
	}
	
	/*
	 * An action to leave the restaurant.
	 */
	public void leaveRestaurant() {
		if (closingState == ClosingState.None)
			this.recordShift(((PeopleAgent)myPerson), "Cashier");
		gui.DoLeaveRestaurant();
		try {
			movingAround.acquire();
		} catch (InterruptedException e) {}
		isActive = false;
		leave = false;
		myPerson.msgDone("Cashier");
	}
	
	public void enterRestaurant() {
		enter = false;
		if (closingState == ClosingState.Closed) {
			closingState = ClosingState.None;
		}
		gui.DoEnterRestaurant();
		try {
			movingAround.acquire();
		} catch (InterruptedException e) {}
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
	
	// Scheduler

	@Override
	public boolean pickAndExecuteAnAction() {
		// Do the entering activity.
		if (enter == true) {
			enterRestaurant();
			return true;
		}
		
		// If the bank is ready to help, make the required request from it.
		if (bankActivity == BankActivity.ReadyToHelp) {
			orderBank();
			return true;
		}
		
		// If have to leave, or shift is over (and the restaurant is not
		// being closed down), then leave.
		if (leave == true && closingState == ClosingState.None) {
			leaveRestaurant();
			return true;
		}
		
		// Pay money to an employee.
		Shift s = findPayableShift();
		if (s != null) {
			payEmployee(s);
			return true;
		}
		
		// If the restaurant has been closed and there is no customer in the restaurant
		// then prepare to close down.
		if (closingState == ClosingState.ToBeClosed && host.anyCustomer() == false && isAnyCheckThere() == false) {
			prepareToClose();
			return true;
		}
		
		// If there is no customer in the restaurant and preparation is complete, then
		// check if there is anybody left to be paid. If there isn't, and there is excess
		// money, then deposit it. Else, shut down the restaurant..
		if (closingState == ClosingState.Preparing && host.anyCustomer() == false && isAnyCheckThere() == false && (bankActivity == BankActivity.None || bankActivity == BankActivity.Robbed)) {
			if (shiftRecord.isEmpty() && workingCapital > minCapital && bankActivity != BankActivity.Robbed && myPerson.getBank(0).isClosed == false) {
				prepareToClose();
				return true;
			}
			else if (workingCapital < computeRequiredMoney() && bankActivity != BankActivity.Robbed && myPerson.getBank(0).isClosed == false) {
				prepareToClose();
				return true;
			}
			else {
				if (leave == true) {
					shutDown();
					leaveRestaurant();
					return true;
				}
			}
		}
		
		// Simply decides which is the current check being taken care of or which
		// customer is being handled right now.
		if (currentCheck == null || currentCheck.s == CheckState.Paid || currentCheck.s == CheckState.PaidLess) {
			currentCheck = findCheckByState(CheckState.BeingPaid);
		}
		
		// Decides response for the current customer who is paying.
		if (currentCheck != null) {	
			if (currentCheck.s == CheckState.BeingPaid) {
				decideResponseForCheck(currentCheck);
				return true;
			}
		}
		
		// Finds an undelivered check and delivers it to the waiter.
		MyCheck mc = findCheckByState(CheckState.Undelivered);
		if (mc != null) {
			deliverCheckToWaiter(mc);
			return true;
		}
		
		// Find a bill that can be verified and verify it to make it ready
		// for payment.
		Bill b = findBillThatCanBeVerified();
		if (b != null) {
			verifyBill(b);
			return true;
		}
		
		// Find a pending and affordable bill to be paid to the market and pay.
		b = findBillThatCanBePaid();
		if (b != null) {
			payBill(b);
			return true;
		}
		
		return false;
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
	
	// Utilities
	
	private MyCheck find(CustomerRestaurantCheck c) {
		synchronized (checks) {
			for (MyCheck mc : checks) {
				if (mc.c == c) {
					return mc;
				}
			}
		}
		return null;
	}
	
	private MyCheck findCheckByState(CheckState s) {
		synchronized (checks) {
			for (MyCheck mc : checks) {
				if (mc.s == s) {
					return mc;
				}
			}
		}
		return null;
	}
	
	private Bill findBillThatCanBeVerified() {
		for (Bill b : bills) {
			if (b.s == BillState.Unpaid && b.itemsFromCook != null && b.itemsFromMarket != null) {
				return b;
			}
		}
		return null;
	}
	
	private Bill findBillThatCanBePaid() {
		synchronized (bills) {
			for (Bill b : bills) {
				if (b.s == BillState.Verified && workingCapital >= b.cost) {
					return b;
				}
			}
		}
		return null;
	}
	
	private Shift findPayableShift() {
		for (Shift s : shiftRecord) {
			if (s.s == ShiftState.Pending) {
				double total = minCapital;
				if (s.role.equals("Cashier"))
					total += cashierSalary;
				else if (s.role.equals("Cook"))
					total += cookSalary;
				else if (s.role.equals("Waiter"))
					total += waiterSalary;
				else if (s.role.equals("Host"))
					total += hostSalary;
				if (workingCapital >= total)
					return s;
			}
		}
		return null;
	}
	
	public void setGui(VkCashierGui g) {
		gui = g;
	}
	
	@Override
	public String toString() {
		return "Cashier";
	}
	
	public double computeRequiredMoney() {
		double result = minCapital;
		for (Shift s : shiftRecord) {
			if (s.role.equals("Cashier"))
				result += cashierSalary;
			else if (s.role.equals("Cook"))
				result += cookSalary;
			else if (s.role.equals("Waiter"))
				result += waiterSalary;
			else if (s.role.equals("Host"))
				result += hostSalary;
		}
		return result;
	}
	
	public boolean isAnyCheckThere() {
		for (MyCheck mc : checks) {
			if (mc.s != CheckState.Paid && mc.s != CheckState.PaidLess) {
				return true;
			}
		}
		return false;
	}
	
	public void setHost(VkHostRole h) {
		this.host = h;
	}
	
	public void setMarketCashier(MarketCashier c) {
		this.mCashier = c;
	}
	
	public String getName() {
		return myPerson.getName();
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
	
	// Helper Data Structures
	
	enum CheckState {Undelivered, Delivered, BeingPaid, Paid, PaidLess};
	
	private class MyCheck {
		private CustomerRestaurantCheck c;
		private CheckState s;
		private Customer cust;
		private Waiter w;
		private double amountPaid = 0.0;
		private List<CustomerRestaurantCheck> l = new ArrayList<CustomerRestaurantCheck>();
		
		public MyCheck(CustomerRestaurantCheck check, Customer cust, Waiter w) {
			c = check;
			s = CheckState.Undelivered;
			this.cust = cust;
			this.w = w;
		}
	}
	
	enum BillState {Unpaid, Verified, Fraud, Paid};
	
	public class Bill {
		Map<String, Integer> itemsFromCook = null;
		Map<String, Integer> itemsFromMarket = null;
		BillState s;
		int orderNumber;
		double cost;
		
		public Bill(int orderNumber) {
			this.orderNumber = orderNumber;
			s = BillState.Unpaid;
		}
	}
	
	enum ShiftState {Pending, Done};
	
	public class Shift {
		PeopleAgent p;
		String role;
		ShiftState s;
		
		public Shift(PeopleAgent p, String role) {
			this.p = p;
			this.role = role;
			s = ShiftState.Pending;
		}
	}
	
	enum BankActivity {None, HelpRequested, ReadyToHelp, DepositRequested, WithdrawalRequested, Robbed};
	
	enum ClosingState {None, ToBeClosed, Preparing, Closed}
}