package restaurant_vk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import bank.interfaces.Teller;
import restaurant.interfaces.Cashier;
import restaurant_vk.interfaces.Customer;
import restaurant.interfaces.Cashier;
import restaurant_vk.interfaces.Waiter;
import restaurant_vk.gui.CashierGui;
import agent.Agent;

/**
 * @author Vikrant Singhal
 *
 * A class representing the Cashier. This cashier prepares checks on requests
 * of the waiter and gives them to the respective waiters. Also, customers come
 * to the cashier and pay the money.
 */
public class CashierAgent extends Agent implements Cashier {
	
	// Data
	
	private List<MyCheck> checks = Collections.synchronizedList(new ArrayList<MyCheck>());
	private List<Bill> bills = Collections.synchronizedList(new ArrayList<Bill>());
	private Menu m = new Menu();
	private MyCheck currentCheck = null;
	private CashierGui gui = null;
	private double myCash = 20.0;
	private Timer timer = new Timer();
	private final int TIME_TO_CHECK_OUT = 1000;
	
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
	public void msgGotMarketOrder(Map<String, Integer> marketOrder) {
	}

	/*
	 * A message called by the market cashier to give the bill to the cook.
	 */
	@Override
	public void msgHereIsWhatIsDue(double price, Map<String, Integer> items) {
	}

	/*
	 * A message called by the market cashier to give back the change to
	 * the cashier.
	 */
	@Override
	public void msgHereIsChange(double change) {
	}

	@Override
	public void msgReadyToHelp(Teller teller) {
	}

	@Override
	public void msgGiveLoan(double funds, double amount) {
	}

	@Override
	public void msgWithdrawSuccessful(double funds, double amount) {
	}

	@Override
	public void msgDepositSuccessful(double funds) {
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
						myCash += m.c.getCost();
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
	 * An action to pay bill to a market.
	 */
	private void payBill(Bill b) {
//		b.m.hereIsMoney(b.cost);
//		myCash = myCash - b.cost;
//		b.state = BillState.Paid;
//		print("Paid " + b.m + " $" + b.cost + ".");
	}
	
	/**--------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------*/
	
	// Scheduler

	@Override
	public boolean pickAndExecuteAnAction() {
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
		
		// Find a pending and affordable bill to be paid to the market and pay.
		Bill b = findBillThatCanBePaid();
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
	
	private Bill findBillThatCanBePaid() {
//		synchronized (bills) {
//			for (Bill b : bills) {
//				if (b.state == BillState.Unpaid && myCash >= b.cost) {
//					return b;
//				}
//			}
//		}
		return null;
	}
	
	public void setGui(CashierGui g) {
		gui = g;
	}
	
	@Override
	public String toString() {
		return "Cashier";
	}
	
	public List<Bill> getBills() {
		return bills;
	}
	
	public List<CustomerRestaurantCheck> getChecks() {
		List<CustomerRestaurantCheck> result = new ArrayList<CustomerRestaurantCheck>();
		synchronized (checks) {
			for (MyCheck mc : checks) {
				result.add(mc.c);
			}
		}
		return result;
	}
	
	/*
	 * Hack for testing.
	 */
	public void addCash(double cash) {
		myCash += cash;
	}
	
	/*
	 * Hack for testing.
	 */
	public double getCash() {
		return myCash;
	}
	
	/*
	 * Hack for testing.
	 */
	public int getLessPaidChecksNumber(Customer c) {
		int result = 0;
		for (MyCheck mc : checks) {
			if (mc.cust == c && mc.s == CheckState.PaidLess) {
				result++;
			}
		}
		return result;
	}
	
	/*
	 * Hack for testing.
	 */
	public int getPaidChecksNumber(Customer c) {
		int result = 0;
		for (MyCheck mc : checks) {
			if (mc.cust == c && mc.s == CheckState.Paid) {
				result++;
			}
		}
		return result;
	}
	
	/*
	 * Hack for testing.
	 */
	public boolean isCheckBeingPaid(Customer c) {
		for (MyCheck mc : checks) {
			if (mc.cust == c && mc.s == CheckState.BeingPaid) {
				return true;
			}
		}
		return false;
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
	
	enum BillState {Unpaid, Paid};
	
	public class Bill {
//		private Market m;
//		private double cost;
//		private BillState state;
//		
//		public Bill(Market m, double cst) {
//			this.m = m;
//			this.cost = cst;
//			state = BillState.Unpaid;
//		}
	}
}