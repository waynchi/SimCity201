package restaurant_ps;

import agent.Agent;
import bank.interfaces.Teller;
import restaurant_ps.Check;
import restaurant.interfaces.Cashier;
import restaurant_ps.gui.CashierGui;
import restaurant_ps.interfaces.Customer;
//import restaurant.interfaces.Market;
import restaurant_ps.interfaces.Waiter;

import restaurant.test.mock.EventLog;


import java.util.*;

import market.interfaces.MarketCashier;

import people.PeopleAgent;
import people.Role;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class CashierAgent extends Role implements Cashier {
	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public enum Status {done, customerEatingFood, customerReadyForcheck, customerBilled , customerPaid, customerAskingToPayNextTime, customerWillPayNextTime, notPaidYet, attemptingToPay};
	public enum CashierState {none,left,personNotified};
	CashierState cashierState = CashierState.none;
	
	public EventLog log = new EventLog();
	
	Timer timer = new Timer();
	
	Double restaurantGain = 10000.0;
	private double minCapital = 1000.0;
	private double loanedMoney = 0;
	public double waiterSalary = 100;
	public double cashierSalary = 100;
	public double cookSalary = 100;
	public double hostSalary = 100;
	private boolean leave = false;
	private boolean enter = false;
	private boolean deposit = false;
	private boolean withdraw = false;
	public Teller teller;
	public HostAgent host;
	public CashierGui cashierGui;
	public List<MyBill> myBills
	= new ArrayList<MyBill>();
	
	public List<CustomerAgent> customersRequestingCheck
	= Collections.synchronizedList(new ArrayList<CustomerAgent>());
	
//	public class MyMarketBill {
//		public Market market;
//		public double moneyOwed;
//		public Status st;
//		MyMarketBill(Market m, double due)
//		{
//			market = m;
//			moneyOwed = due;
//			st = Status.notPaidYet;
//		}
//	}
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
	
	enum BankActivity {None, HelpRequested, ReadyToHelp, DepositRequested, WithdrawalRequested};
	
	BankActivity bankActivity = BankActivity.None;
	
	enum ClosingState {None, ToBeClosed, Preparing, Closed};
	
	public ClosingState closingState;
	
	enum BillState {Unpaid, Verified, Fraud, Paid, Verifying};
	
	public class MarketBill {
		Map<String, Integer> itemsFromCook = null;
		Map<String, Integer> itemsFromMarket = null;
		BillState s;
		int orderNumber;
		double cost;
		
		public MarketBill(int orderNumber) {
			this.orderNumber = orderNumber;
			s = BillState.Unpaid;
		}
	}
	
	public class MyBill {
		public Waiter waiterOfCustomer;
		public Check c;
		public Status st;
		
		MyBill(Waiter w,Check c)
		{
			this.c = c;
			st = Status.customerEatingFood;
			waiterOfCustomer = w;
		}
	};
	
	
	//public List<MyMarketBill> myMarketBills = Collections.synchronizedList(new ArrayList<MyMarketBill>());
	public List<MarketBill> myMarketBills = Collections.synchronizedList(new ArrayList<MarketBill>());
	public Map<String,Food> foods = new HashMap<String,Food>();
	public List<Shift> shiftRecord = Collections.synchronizedList(new ArrayList<Shift>());
	
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented

	private String name;
	//public HostGui hostGui = null;

	public MarketCashier marketCashier;
	//private Market market;
	
	public CashierAgent(String name, double restaurantGain) {
		super();
		this.name = name;
		this.restaurantGain = restaurantGain;
	}

	/* (non-Javadoc)
	 * @see restaurant.Cashier#getMaitreDName()
	 */
	
	public String getMaitreDName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see restaurant.Cashier#getName()
	 */
	
	public String getName() {
		return name;
	}

	
	// Messages

	
	/* (non-Javadoc)
	 * @see restaurant.Cashier#msgPleaseMakeCheck(restaurant.WaiterAgent, restaurant.interfaces.Customer, restaurant.Choice)
	 */
	
	public void msgIsActive(){
		enter = true;
		isActive = true;
		stateChanged();
	}
	
	public void msgIsInActive(){
		leave = true;
		this.recordShift(((PeopleAgent) myPerson), "Cashier");
		stateChanged();
	}
	
	@Override
	public void msgGotMarketOrder(Map<String, Integer> marketOrder,
			int orderNumber) {
		// TODO Auto-generated method stub
		boolean found = false;
		for (MarketBill b : myMarketBills) {
			if (b.orderNumber == orderNumber) {
				found = true;
				b.itemsFromCook = marketOrder;
				break;
			}
		}
		if (found == false) {
			MarketBill b = new MarketBill(orderNumber);
			b.itemsFromCook = marketOrder;
			myMarketBills.add(b);
		}
		stateChanged();
		
	}

	@Override
	public void msgHereIsWhatIsDue(double price, Map<String, Integer> items,
			int orderNumber) {
		// TODO Auto-generated method stub
		boolean found = false;
		for (MarketBill b : myMarketBills) {
			if (b.orderNumber == orderNumber) {
				found = true;
				b.itemsFromMarket = items;
				b.cost = price;
				break;
			}
		}
		if (found == false) {
			MarketBill b = new MarketBill(orderNumber);
			b.cost = price;
			b.itemsFromMarket = items;
			myMarketBills.add(b);
		}
		stateChanged();
	}

	@Override
	public void msgHereIsChange(double change) {
		// TODO Auto-generated method stub
		restaurantGain += change;
		stateChanged();
	}

	@Override
	public void msgReadyToHelp(Teller teller) {
		// TODO Auto-generated method stub
		bankActivity = BankActivity.ReadyToHelp;
		stateChanged();
	}

	@Override
	public void msgGiveLoan(double funds, double amount) {
		// TODO Auto-generated method stub
		restaurantGain += amount;
		loanedMoney += amount;
		bankActivity = BankActivity.None;
		stateChanged();
	}

	@Override
	public void msgWithdrawSuccessful(double funds, double amount) {
		// TODO Auto-generated method stub
		restaurantGain += amount;
		bankActivity = BankActivity.None;
		stateChanged();
	}

	@Override
	public void msgDepositSuccessful(double funds) {
		// TODO Auto-generated method stub
		restaurantGain = minCapital;
		bankActivity = BankActivity.None;
		stateChanged();
	}
	
	
	
//	public void msgHereIsYourBill(Market marketAgent, double amountDue) {
//		// TODO Auto-generated method stub
//		MyMarketBill bill = new MyMarketBill(marketAgent, amountDue);
//		myMarketBills.add(bill);
//		stateChanged();
//	}

	public void msgAnimationFinishedDoLeaveRestaruant() {
		// TODO Auto-generated method stub
		cashierState = CashierState.left;
		stateChanged();
	}
	
	public void msgPleaseMakeCheck(Waiter w, Customer c, Choice o) {
		// TODO Auto-generated method stub
		myBills.add(new MyBill(w,new Check(c,o.food)));
		
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Cashier#msgCheckPlease(restaurant.interfaces.Customer)
	 */
	
	public void msgCheckPlease(Customer cust) {
		// TODO Auto-generated method stub
		MyBill bill = findBillForCustomer(cust);
		bill.st = Status.customerReadyForcheck;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Cashier#msgCanIPayNextTime(restaurant.interfaces.Customer)
	 */
	
	public void msgCanIPayNextTime(Customer cust) {
		// TODO Auto-generated method stub
		MyBill bill = findBillForCustomer(cust);
		bill.st = Status.customerAskingToPayNextTime;
		stateChanged();
	}
	
	

	/* (non-Javadoc)
	 * @see restaurant.Cashier#msgHereIsMyPayment(double, restaurant.interfaces.Customer)
	 */
	
	public void msgHereIsMyPayment(double payment, Customer cust) {
		// TODO Auto-generated method stub
		//restaurantGain += payment; 
		MyBill bill = findBillForCustomer(cust);
		bill.c.setMoneyOwed(bill.c.getMoneyOwed() - payment);
		bill.st = Status.customerPaid;
		stateChanged();
	}
	


	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() throws ConcurrentModificationException{
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
           
		 */
		// Do the entering activity.
				if (enter == true) {
					enterRestaurant();
					return true;
				}
				
				if(cashierState == CashierState.left)
				{
					cashierState = CashierState.personNotified;
					NotifyPerson();
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
				if(closingState == ClosingState.ToBeClosed && host.anyCustomer() == false && isAnyCheckThere() == false) {
					prepareToClose();
				}
	
		try{		
		synchronized(shiftRecord)
		{
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
					if (restaurantGain >= total)
					{
						PayShift(s);
						return true;
					}
				}
			}
		}
		synchronized(myBills)
		{
		for(MyBill bill : myBills)
		{
			if(bill.st == Status.customerReadyForcheck)
			{
				bill.st = Status.customerBilled;
				GiveCheck(bill);
				return true;
			}
		}
		
		for(MyBill bill : myBills)
		{
			if(bill.st == Status.customerPaid)
			{
				bill.st = Status.done;
				StoreMoney(bill);
				return true;
			}
		}
		
		for(MyBill bill : myBills)
		{
			if(bill.st == Status.customerAskingToPayNextTime)
			{
				bill.st = Status.customerWillPayNextTime;
				RecordThis(bill);
				return true;
			}
		}
		}
		synchronized(myMarketBills)
		{
		for(MarketBill bill : myMarketBills)
		{
			if(bill.s == BillState.Unpaid && bill.itemsFromCook != null && bill.itemsFromMarket != null)
			{
				bill.s = BillState.Verifying;
				Verify(bill);
				return true;
			}
		}
		for(MarketBill bill : myMarketBills)
		{
			if(bill.s == BillState.Verified && bill.cost <= restaurantGain)
			{
				bill.s = BillState.Paid;
				PayThis(bill);
				return true;
			}
		}
		}
		}catch(ConcurrentModificationException e)
		{
			return false;
		}
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

//	private void TryToPayThis(MyMarketBill bill) {
//		// TODO Auto-generated method stub
//		if(bill.moneyOwed > this.restaurantGain)
//		{
//			bill.st = Status.notPaidYet;
//			//do something
//			return;
//		}
//		Do("Paid market bill");
//		double payment = bill.moneyOwed;
//		restaurantGain -= payment;
//		bill.market.msgHereIsMyPayment(bill.moneyOwed);
//	}

	private void prepareToClose() {
		// TODO Auto-generated method stub
		if (restaurantGain > minCapital && shiftRecord.isEmpty()) {
			teller.msgNeedHelp(this, "help");
			bankActivity = BankActivity.HelpRequested;
			deposit = true;
		}
		else {
			teller.msgNeedHelp(this, "help");
			bankActivity = BankActivity.HelpRequested;
			withdraw = true;
		}
		closingState = ClosingState.Preparing;
	}

	private boolean isAnyCheckThere() {
		// TODO Auto-generated method stub
		for(MyBill bill : myBills)
		{
			if(bill.st != Status.customerPaid && bill.st!= Status.customerWillPayNextTime)
				return true;
		}
		return false;
	}

	private void orderBank() {
		// TODO Auto-generated method stub
		if(deposit == true) {
			bankActivity = BankActivity.DepositRequested;
			deposit = false;
			teller.msgDeposit(getPersonAgent().getRestaurant(2).bankAccountID,restaurantGain - minCapital);
		}
		else if(withdraw = true) {
			bankActivity = BankActivity.WithdrawalRequested;
			withdraw = false;
			double total = 0.0;
			total += (computeRequiredMoney() - restaurantGain);
			teller.msgWithdraw(getPersonAgent().getRestaurant(2).bankAccountID, total);
		}
	}

	private Double computeRequiredMoney() {
		// TODO Auto-generated method stub
		double result = minCapital;
		for(Shift s: shiftRecord)
		{
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

	private void NotifyPerson() {
		// TODO Auto-generated method stub
		isActive = false;
		leave = false;
		myPerson.msgDone("Cashier");
	}

	private void leaveRestaurant() {
		// TODO Auto-generated method stub
		cashierGui.DoLeaveRestaurant();
		
	}

	private void enterRestaurant() {
		// TODO Auto-generated method stub
		enter = false;
		if(closingState == ClosingState.Closed) {
			closingState = ClosingState.None;
		}
		cashierGui.DoEnterRestaurant();
	}

	private void PayShift(Shift s) {
		// TODO Auto-generated method stub
		if (s.role.equals("Waiter")) {
			s.p.Money += waiterSalary;
			restaurantGain -= waiterSalary;
		}
		else if (s.role.equals("Cook")) {
			s.p.Money += cookSalary;
			restaurantGain -= cookSalary;
		}
		else if (s.role.equals("Host")) {
			s.p.Money += hostSalary;
			restaurantGain -= hostSalary;
		}
		else if (s.role.equals("Cashier")) {
			s.p.Money += cashierSalary;
			restaurantGain -= cashierSalary;
		}
	}

	private void PayThis(MarketBill bill) {
		// TODO Auto-generated method stub
		marketCashier.msgHereIsPayment(bill.cost, bill.itemsFromMarket, this);
		restaurantGain -= bill.cost;
		
	}

	private void Verify(MarketBill bill) {
		// TODO Auto-generated method stub
		for (Map.Entry<String, Integer> ci : bill.itemsFromCook.entrySet()) {
			boolean found = false;
			for (Map.Entry<String, Integer> mi : bill.itemsFromMarket.entrySet()) {
				if (mi.getKey().equals(ci.getKey()) && mi.getValue().equals(ci.getValue())) {
					found = true;
					break;
				}
			}
			if (found == false) {
				bill.s = BillState.Fraud;
				return;
			}
		}
		bill.s = BillState.Verified;
		stateChanged();
	}

	private void RecordThis(MyBill bill) {
		// TODO Auto-generated method stub
		Do("Customer will pay next time...");
		bill.c.getCustomer().msgPayNextTime();
	}

	private void StoreMoney(MyBill bill) {
		// TODO Auto-generated method stub
		Do("Payment accepted and stored");
		restaurantGain += bill.c.foodChoice.price - bill.c.getMoneyOwed();
		bill.c.getCustomer().msgAcceptedPayment();
		myBills.remove(bill);
		
		
	}

	private void GiveCheck(MyBill bill) {
		Do("Giving check to waiter");
		bill.waiterOfCustomer.msgHereIsCheck(bill.c);
	}
	
	public MyBill findBillForCustomer(Customer cust) {
		// TODO Auto-generated method stub
		for(MyBill bill : myBills)
		{
			if(bill.c.getCustomer() == cust)
				return bill;
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Cashier#pause()
	 */
	
	public void pause() {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see restaurant.Cashier#getMarket()
	 */
	
//	public Market getMarket() {
//		return market;
//	}

	/* (non-Javadoc)
	 * @see restaurant.Cashier#setMarket(restaurant.MarketAgent)
	 */
	
//	public void setMarket(Market market) {
//		this.market = market;
//	}

	public double getRestaurantMoney() {
		// TODO Auto-generated method stub
		
		return this.restaurantGain;
	}

	public void recordShift(PeopleAgent myPerson, String role) {
		// TODO Auto-generated method stub
		shiftRecord.add(new Shift(myPerson, role));
		stateChanged();
	}



	

	
	

	
	
	

	

	

}

