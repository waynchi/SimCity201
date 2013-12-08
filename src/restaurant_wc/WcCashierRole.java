package restaurant_wc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
//import java.util.List;
//import java.util.Timer;
import java.util.TimerTask;
import java.util.Map;

import bank.interfaces.Teller;
import people.Role;
import restaurant.interfaces.*;
import restaurant_wc.interfaces.Customer;
import restaurant_wc.interfaces.Market;
import restaurant_wc.interfaces.Waiter;
import restaurant_wc.test.mock.EventLog;
import restaurant_wc.test.mock.LoggedEvent;
//import restaurant_wc.WcCustomerRole.AgentEvent;
//aurant.WaiterAgent.AgentState;
import agent.Agent;

public class WcCashierRole extends Role implements Cashier{
	//variables
	public List<Check> pendingChecks = Collections.synchronizedList(new ArrayList<Check>());
	public List<Payment> pendingPayments = Collections.synchronizedList(new ArrayList<Payment>());
	public List<Debt> Debts = Collections.synchronizedList(new ArrayList<Debt>());
	public List<MyMBill> MarketBills = Collections.synchronizedList(new ArrayList<MyMBill>());
	Food SteakDish;
	Food SaladDish;
	Food ChickenDish;
	Food PizzaDish;
	private double CashRegister = 20;
	private String name;
	Map<String, Food> FoodTypes = new HashMap<String, Food>();
	public EventLog log = new EventLog();
	
	public WcCashierRole(String name){
		
		super();
		this.name = name;
		SteakDish= new Food("Steak", 15.99);
		SaladDish = new Food("Salad", 5.99);
		ChickenDish = new Food("Chicken", 10.99);
		PizzaDish = new Food ("Pizza", 8.99);
		FoodTypes.put("Steak", SteakDish);
		FoodTypes.put("Salad", SaladDish);
		FoodTypes.put("Chicken", ChickenDish);
		FoodTypes.put("Pizza", PizzaDish);
		
	}
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	public boolean pickAndExecuteAnAction() {
		if(!(pendingChecks.size() == 0)){
			synchronized(pendingChecks){
			for(Check p : pendingChecks){
				ComputeCheck(p);
				return true;
			}
			}
		}
		if(!(pendingPayments.size() == 0)){
			synchronized(pendingPayments){
			for(Payment pay: pendingPayments){
				DecideAction(pay);
				return true;
			}
			}
		}
		if(!(MarketBills.isEmpty()))
		{
			synchronized(MarketBills){
			for(MyMBill bill: MarketBills){
				PayMarket(bill);
				return true;
			}
			}
		}
		return false;
	}
	
	public void depleteCashRegister() {
		// TODO Auto-generated method stub
		CashRegister = 0;
		
	}
	
	//messages


	@Override
	public void msgHereIsMarketBill(MBill bill, Market market) {
		print("Recieved message from Market " + market + " asking for check");
		MarketBills.add(new MyMBill(bill, market));
		log.add(new LoggedEvent("Received HereIsMarketBill from market. Total = " + bill.Payment));
		stateChanged();
	}

	public void msgHereIsACheck(String choice, Customer cust, Waiter w) {
		print("Recieved message asking for a check");
		pendingChecks.add(new Check(choice,cust,w));
		stateChanged();
		
	}
	

	/* (non-Javadoc)
	 * @see restaurant_wc.Cashier#msgHereIsMyPayment(double, double, restaurant_wc.interfaces.Customer)
	 */
	@Override
	public void msgHereIsMyPayment(double check, double Pay, Customer cust) {
		print("Recieving Payment from " + cust);
		pendingPayments.add(new Payment(cust, Pay, check));
		stateChanged();
	}

	
	//actions
	

	private void PayMarket(MyMBill bill) {
		// TODO Auto-generated method stub
		Do("Paying Back Market" + bill.market);
		if(CashRegister <= bill.bill.Payment)
		{
			print("I do not have enough money! I will take a loan of $100 from the Bank");
			CashRegister +=100;
		}
		bill.market.msgBillPayment(bill.bill.Payment);
		CashRegister -= bill.bill.Payment;
		log.add(new LoggedEvent("Paid Market back. Total = " + bill.bill.Payment));
		MarketBills.remove(bill);

		
	}
	private void ComputeCheck(Check p) {
		Do("Computing Check");
		double amt = 0;
		amt = FoodTypes.get(p.Choice).Price;
		synchronized(Debts){
		for(int i = 0; i < Debts.size(); i++)
		{
			print("You have debt!");
			if(Debts.get(i).customer == p.customer)
			{
				amt += Debts.get(i).debt;
				Debts.remove(Debts.get(i));
			}
		}
		}
		p.waiter.msgHereisACheck(amt, p.customer);
		pendingChecks.remove(p);
	}
	
	private void DecideAction(Payment p){
		Do("Deciding course of action");
		if(p.cash >= p.check){
			Do("Please come again.");
			p.customer.msgHereIsYourChange(p.cash - p.check);
			CashRegister += p.check;
		}
		else{
			//debt Mechanic
			Do("I'm adding this to your debt. Please pay next time.");
			/*boolean hasDebt = false;
			if(!(Debts.isEmpty()))
			{
				for(int i = 0; i < Debts.size(); i++)
				{
					if(Debts.get(i).customer == p.customer)
					{
						hasDebt = true;
					}
				}
			}
			if(!hasDebt)
			{
			Debts.add(new Debt(p.customer, p.check - p.cash));
			}*/
			Debts.add(new Debt(p.customer, p.check - p.cash));
			log.add(new LoggedEvent("Debt added for customer " + p.customer));
			p.customer.msgHereIsYourChange(0.00);
			CashRegister += p.cash;
		}
		pendingPayments.remove(p);
	}
	
	
	private class Food{
		@SuppressWarnings("unused")
		String Choice;
		double Price;
		
		public Food(String c, double p)
		{
			Choice = c;
			Price = p;
		}
		
	}
	
	public class Check{
		String Choice;
		Customer customer;
		Waiter waiter;
		
		public Check(String c, Customer cust, Waiter w){
			Choice = c;
			customer = cust;
			waiter = w;
		}		
	}

	public class Payment{
		Customer customer;
		public double cash;
		public double check;
		
		public Payment(Customer cust, double c, double ch){
			customer = cust;
			cash = c;
			check = ch;
		}
	}
	
	public class Debt{
		Customer customer;
		double debt;
		
		public Debt(Customer cust, double d)
		{
			customer = cust;
			debt = d;
		}
	}
	
	private class MyMBill{
		MBill bill;
		Market market;
		public MyMBill(MBill bill, Market market)
		{
			this.bill = bill;
			this.market = market;
		}
	}

	@Override
	public void msgGotMarketOrder(Map<String, Integer> marketOrder,
			int orderNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsWhatIsDue(double price, Map<String, Integer> items,
			int orderNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsChange(double change) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyToHelp(Teller teller) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGiveLoan(double funds, double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWithdrawSuccessful(double funds, double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDepositSuccessful(double funds) {
		// TODO Auto-generated method stub
		
	}
}

