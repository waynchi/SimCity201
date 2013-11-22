package people.test;

import junit.framework.*;
import restaurant.CashierAgent;
//import restaurant.CashierAgent.cashierBillState;
//import restaurant.WaiterAgent.Bill;
import restaurant.CashierAgent.Check;
import restaurant.CashierAgent.Debt;
import restaurant.MBill;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.MockCustomer;
import restaurant.test.mock.MockMarket;
import restaurant.test.mock.MockWaiter;

//import junit.framework.*;

/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * @author Monroe Ekilah
 */
public class PeopleTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	CashierAgent cashier;
	MockWaiter waiter;
	MockCustomer customer;
	MockMarket market, market2;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		cashier = new CashierAgent("cashier");		
		customer = new MockCustomer("mockcustomer");		
		waiter = new MockWaiter("mockwaiter");
		market = new MockMarket("mockmarket");
		market2 = new MockMarket("mockmarket2");
	}	
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */
	public void testOneNormalCustomerScenario()
	{
		//setUp() runs first before this test!
		
		customer.cashier = cashier;//You can do almost anything in a unit test.			
		
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.pendingChecks.size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		//Testing Milestone v2.2A 
		//One Market with one bill
		System.out.println("Testing Market Interactions");
		assertEquals("Cashier should have 0 market bills in it", cashier.MarketBills.size(), 0);
		cashier.msgHereIsMarketBill(new MBill("Steak", 5, 8), market);
		assertFalse("CashierAgent should have something in MarketBills now.",cashier.MarketBills.isEmpty());
		System.out.println(cashier.log.getLastLoggedEvent().toString());
		assertTrue("Testing if msg is sent", cashier.log.getLastLoggedEvent().toString().equals("Received HereIsMarketBill from market. Total = 40.0"));
		assertTrue("Testing Scheduler", cashier.pickAndExecuteAnAction());
		assertTrue("Testing if payment is sent", cashier.log.getLastLoggedEvent().toString().equals("Paid Market back. Total = 40.0"));
		assertTrue("Testing if market recieved message", market.log.getLastLoggedEvent().toString().equals("Recieved message Bill Payment. Total = 40.0"));
		assertTrue("Testing if cashier MBill is now empty", cashier.MarketBills.isEmpty());
		assertFalse("Scheduler testing again", cashier.pickAndExecuteAnAction());
		
		//Two markets with two bills
		System.out.println("Testing Market Interactions");
		assertEquals("Cashier should have 0 market bills in it", cashier.MarketBills.size(), 0);
		cashier.msgHereIsMarketBill(new MBill("Steak", 3, 8), market);
		assertTrue("Testing if first market msg is sent", cashier.log.getLastLoggedEvent().toString().equals("Received HereIsMarketBill from market. Total = 24.0"));
		assertFalse("CashierAgent should have something in MarketBills now.",cashier.MarketBills.isEmpty());
		assertTrue("Testing Scheduler", cashier.pickAndExecuteAnAction());
		cashier.msgHereIsMarketBill(new MBill("Steak", 2, 8), market2);
		assertTrue("Testing Scheduler", cashier.pickAndExecuteAnAction());
		//these two asserts show that both markets receive their different bill payments
		assertTrue("Testing if market recieved message and payment", market.log.getLastLoggedEvent().toString().equals("Recieved message Bill Payment. Total = 24.0"));
		assertTrue("Testing if market2 recieved message and payment", market2.log.getLastLoggedEvent().toString().equals("Recieved message Bill Payment. Total = 16.0"));
		assertTrue("Testing if cashier MBill is now empty", cashier.MarketBills.isEmpty());
		assertFalse("Scheduler testing again", cashier.pickAndExecuteAnAction());
		
		
		//We need four extra scenarios.
		//Scenario #1. Normative. Waiter and cashier interact with the cashier. Everything goes correctly and the customer pays the exact value that he owes
		assertTrue("Cashier should have no Checks", cashier.pendingChecks.isEmpty());
		cashier.msgHereIsACheck("Steak", customer, waiter);
		assertTrue("Testing Scheuduler", cashier.pickAndExecuteAnAction());
		assertTrue("Cashier should have no Payments", cashier.pendingPayments.isEmpty());
		System.out.println(waiter.log.getLastLoggedEvent().toString());
		assertTrue("Testing", waiter.log.getLastLoggedEvent().toString().equals("Received msgHereisACheck from cashier. Total = 15.99"));
		//Here I interweave the markeet interactions, showing that it's okay
		System.out.println("Testing Market Interactions");
		assertEquals("Cashier should have 0 market bills in it", cashier.MarketBills.size(), 0);
		cashier.msgHereIsMarketBill(new MBill("Steak", 5, 8), market);
		assertFalse("CashierAgent should have something in MarketBills now.",cashier.MarketBills.isEmpty());
		System.out.println(cashier.log.getLastLoggedEvent().toString());
		assertTrue("Testing if msg is sent", cashier.log.getLastLoggedEvent().toString().equals("Received HereIsMarketBill from market. Total = 40.0"));
		assertTrue("Testing Scheduler", cashier.pickAndExecuteAnAction());
		assertTrue("Testing if payment is sent", cashier.log.getLastLoggedEvent().toString().equals("Paid Market back. Total = 40.0"));
		assertTrue("Testing if market recieved message", market.log.getLastLoggedEvent().toString().equals("Recieved message Bill Payment. Total = 40.0"));
		assertTrue("Testing if cashier MBill is now empty", cashier.MarketBills.isEmpty());
		assertFalse("Scheduler testing again", cashier.pickAndExecuteAnAction());
		cashier.msgHereIsMyPayment(15.99, 15.99, customer);
		assertFalse("Cashier should now have a Payment", cashier.pendingPayments.isEmpty());
		assertTrue("Testing Scheduler", cashier.pickAndExecuteAnAction());
		System.out.println(customer.log.getLastLoggedEvent().toString());
		assertTrue("Testing to see if the customer recieved change", customer.log.getLastLoggedEvent().toString().equals("Received HereIsYourChange from cashier. Change = 0.0"));
		assertTrue("Making sure the debt list is 0", cashier.Debts.isEmpty());
		assertFalse("Scheduler testing", cashier.pickAndExecuteAnAction());
		
		//Scenario #2. Normative. Waiter and cashier interact with the cashier. Everything goes correctly, but the customers pays MORE than what is required (For e.g. using a $20 bill)	
		assertTrue("Cashier should have no Checks", cashier.pendingChecks.isEmpty());
		cashier.msgHereIsACheck("Pizza", customer, waiter);
		assertTrue("Testing Scheuduler", cashier.pickAndExecuteAnAction());
		assertTrue("Cashier should have no Payments", cashier.pendingPayments.isEmpty());
		System.out.println(waiter.log.getLastLoggedEvent().toString());
		assertTrue("Testing", waiter.log.getLastLoggedEvent().toString().equals("Received msgHereisACheck from cashier. Total = 8.99"));
		cashier.msgHereIsMyPayment(8.99, 20, customer);
		assertFalse("Cashier should now have a Payment", cashier.pendingPayments.isEmpty());
		assertTrue("Testing Scheduler", cashier.pickAndExecuteAnAction());
		System.out.println(customer.log.getLastLoggedEvent().toString());
		assertTrue("Testing to see if the customer recieved change", customer.log.getLastLoggedEvent().toString().equals("Received HereIsYourChange from cashier. Change = 11.01"));
		assertTrue("Making sure the debt list is 0", cashier.Debts.isEmpty());
		assertFalse("Scheduler testing", cashier.pickAndExecuteAnAction());
		
		
		//Scenario #3. Non-normative. Customer does not have enough money.
		assertTrue("Cashier should have no Checks", cashier.pendingChecks.isEmpty());
		assertTrue("Cashier should have no Debts", cashier.Debts.isEmpty());
		cashier.msgHereIsACheck("Chicken", customer, waiter);
		assertTrue("Testing Scheduler", cashier.pickAndExecuteAnAction());
		assertTrue("Cashier should have no Payments", cashier.pendingPayments.isEmpty());
		assertTrue("testing if waiter recieved message from cashier", waiter.log.getLastLoggedEvent().toString().equals("Received msgHereisACheck from cashier. Total = 10.99"));
		cashier.msgHereIsMyPayment(10.99, 5.99, customer);
		assertFalse("Cashier should now have a Payment", cashier.pendingPayments.isEmpty());
		assertTrue("Testing Scheduler", cashier.pickAndExecuteAnAction());
		System.out.println(customer.log.getLastLoggedEvent().toString());
		assertTrue("Testing to see if the customer recieved change", customer.log.getLastLoggedEvent().toString().equals("Received HereIsYourChange from cashier. Change = 0.0"));
		assertFalse("Making sure a debt is added.", cashier.Debts.isEmpty());
		assertEquals("Seeing if the debt belongs to the customer", cashier.log.getLastLoggedEvent().toString(), "Debt added for customer " + customer);
		assertFalse("Scheduler testing", cashier.pickAndExecuteAnAction());
		
		//Scenario #4. Non-normative. Customer comes in. Customer has a debt from the last time he visited! (He pays it all off this time though)
		cashier.Debts.clear();
		assertTrue("Cashier should have no Debts", cashier.Debts.isEmpty());
		cashier.Debts.add(cashier.new Debt(customer, 9.99));
		assertFalse("Cashier should have a new Debt", cashier.Debts.isEmpty());
		assertTrue("Cashier should have no Checks", cashier.pendingChecks.isEmpty());
		cashier.msgHereIsACheck("Salad", customer, waiter);
		assertTrue("Testing Scheduler", cashier.pickAndExecuteAnAction());
		assertTrue("Cashier should have no Payments", cashier.pendingPayments.isEmpty());
		//this is the main part. The salad costs 5.99. But with the 9.99 debt, he is required to pay 15.98 which is correctly reflected in the test.
		assertTrue("testing if waiter recieved message from cashier", waiter.log.getLastLoggedEvent().toString().equals("Received msgHereisACheck from cashier. Total = 15.98"));
		cashier.msgHereIsMyPayment(15.98, 15.98, customer);
		assertFalse("Cashier should now have a Payment", cashier.pendingPayments.isEmpty());
		assertTrue("Testing Scheduler", cashier.pickAndExecuteAnAction());
		assertTrue("Testing to see if the customer recieved change", customer.log.getLastLoggedEvent().toString().equals("Received HereIsYourChange from cashier. Change = 0.0"));
		//Just plugging in restaurant interactions randomly again
		System.out.println("Testing Market Interactions");
		assertEquals("Cashier should have 0 market bills in it", cashier.MarketBills.size(), 0);
		cashier.msgHereIsMarketBill(new MBill("Steak", 3, 8), market);
		assertTrue("Testing if first market msg is sent", cashier.log.getLastLoggedEvent().toString().equals("Received HereIsMarketBill from market. Total = 24.0"));
		assertFalse("CashierAgent should have something in MarketBills now.",cashier.MarketBills.isEmpty());
		assertTrue("Testing Scheduler", cashier.pickAndExecuteAnAction());
		cashier.msgHereIsMarketBill(new MBill("Steak", 2, 8), market2);
		assertTrue("Testing Scheduler", cashier.pickAndExecuteAnAction());
		//these two asserts show that both markets receive their different bill payments
		assertTrue("Testing if market recieved message and payment", market.log.getLastLoggedEvent().toString().equals("Recieved message Bill Payment. Total = 24.0"));
		assertTrue("Testing if market2 recieved message and payment", market2.log.getLastLoggedEvent().toString().equals("Recieved message Bill Payment. Total = 16.0"));
		assertTrue("Testing if cashier MBill is now empty", cashier.MarketBills.isEmpty());
		assertFalse("Scheduler testing again", cashier.pickAndExecuteAnAction());
		assertTrue("Making sure the customer paid off his debts.", cashier.Debts.isEmpty());
		assertFalse("Scheduler testing", cashier.pickAndExecuteAnAction());

		/*//step 1 of the test
		//public Bill(Cashier, Customer, int tableNum, double price) {
		//Check bill = new Check("Steak", customer, waiter);
		cashier.msgHereIsACheck("Steak", customer, waiter);//send the message from a waiter

		//check postconditions for step 1 and preconditions for step 2
		// have to add things
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.pendingChecks.size(), 1);
		
		//assertFalse("Cashier's scheduler should have returned false (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		
		assertEquals(
				"MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		//step 2 of the test
		cashier.msgHereIsMyPayment(5, 5, customer);
		//cashier.ReadyToPay(customer, bill);
		
		//check postconditions for step 2 / preconditions for step 3
		/*assertTrue("CashierBill should contain a bill with state == customerApproached. It doesn't.",
				cashier.bills.get(0).state == cashierBillState.customerApproached);
		
		assertTrue("Cashier should have logged \"Received ReadyToPay\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received ReadyToPay"));

		assertTrue("CashierBill should contain a bill of price = $7.98. It contains something else instead: $" 
				+ cashier.bills.get(0).bill.netCost, cashier.bills.get(0).bill.netCost == 7.98);
		
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
					cashier.bills.get(0).bill.customer == customer);*/
		
		
		//step 3
		//NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
		/*assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 3 / preconditions for step 4
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourTotal\" with the correct balance, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourTotal from cashier. Total = 7.98"));
	
			
		assertTrue("Cashier should have logged \"Received HereIsMyPayment\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received HereIsMyPayment"));
		
		
		//assertTrue("CashierBill should contain changeDue == 0.0. It contains something else instead: $" 
			//	+ cashier.bill.get(0).changeDue, cashier.bill.get(0).changeDue == 0);
		assertTrue("CashierBill should contain changeDue == 0.0. It contains something else instead: $" 
				+ (cashier.pendingPayments.get(0).check - cashier.pendingPayments.get(0).cash), ( cashier.pendingPayments.get(0).check - - cashier.pendingPayments.get(0).cash) == 0);
		
		
		
		//step 4
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 4
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourChange\" with the correct change, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourChange from cashier. Change = 0.0"));
	
		
		/*assertTrue("CashierBill should contain a bill with state == done. It doesn't.",
				cashier.bills.get(0).state == cashierBillState.done);*/
		
		/*assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());*/
		
	
	}//end one normal customer scenario
	
	
}
