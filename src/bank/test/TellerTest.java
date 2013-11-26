package bank.test;

import restaurant.CashierAgent;
import restaurant.CashierAgent.CheckState;
import restaurant.CashierAgent.marketBill;
import restaurant.CashierAgent.myCheck;
import restaurant.Check;
import restaurant.WaiterAgent;
import restaurant.test.mock.MockCustomer;
import restaurant.test.mock.MockMarket;
import restaurant.test.mock.MockWaiter;
import junit.framework.*;

/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * @author Monroe Ekilah
 */
public class TellerTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	CashierAgent cashier;
	MockCustomer customer;
	MockMarket market;
	MockMarket market2;
	MockWaiter waiter;
	MockCustomer customer2;
	
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		cashier = new CashierAgent("cashier");		
		customer = new MockCustomer("mockcustomer");
		customer2 = new MockCustomer("customer2");
		market = new MockMarket("market", cashier);
		market2 = new MockMarket("market2", cashier);
		waiter = new MockWaiter("waiter");
	}	
	//This scenario tests one market order that the cashier has to pay.
	public void testOneNormalMarketScenario()
	{
		//setUp() runs first before this test!			
		
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.mBills.size(), 0);	
		
		assertEquals("Cashier's wallet should have $1000. It doesn't.",cashier.wallet, 1000.0);
		
		cashier.msgPayMarket(market, 120.80, "Steak");

		assertEquals("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ market.log.toString(), 0, market.log.size());
		
		assertEquals("Cashier should have 1 market bill in it. It doesn't.", cashier.mBills.size(), 1);
		
		assertEquals("The bill should contain the amount given in the message", cashier.mBills.get(0).amount, 120.8);
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to the new market bill), but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		assertEquals("Cashier's wallet should have $1000 - 120.80. It doesn't.",cashier.wallet, 879.2);
	
		assertTrue("Market should have logged \"Received msgHereIsMoney\" but didn't. His log reads instead: "
                + market.log.getLastLoggedEvent().toString(), market.log.containsString("Received msgHereIsMoney from cashier. Total = 120.8"));
		
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.mBills.size(), 0);
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
	}
	
	//this scenario tests when two markets ask the cashier for payment at the same time.
	public void testTwoNonNormalMarketScenario()
	{
		//setUp() runs first before this test!			
		
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.mBills.size(), 0);	
		
		assertEquals("Cashier's wallet should have $1000. It doesn't.",cashier.wallet, 1000.0);
		
		//send the cashier two different bills
		cashier.msgPayMarket(market, 120.80, "Steak");
		
		cashier.msgPayMarket(market2, 55, "Salad");

		assertEquals("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ market.log.toString(), 0, market.log.size());
		
		assertEquals("MockMarket2 should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
				+ market2.log.toString(), 0, market2.log.size());
		
		assertEquals("Cashier should have 2 market bill in it. It doesn't.", cashier.mBills.size(), 2);
		
		assertEquals("The bill should contain the amount given in the message", cashier.mBills.get(0).amount, 120.8);
		
		assertEquals("The bill should contain the amount given in the message", cashier.mBills.get(1).amount, 55.0);
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to the new market bill), but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		assertEquals("Cashier's wallet should have $1000 - 120.80. It doesn't.",cashier.wallet, 879.2);
	
		assertTrue("Market1 should have logged \"Received msgHereIsMoney\" but didn't. His log reads instead: "
                + market.log.getLastLoggedEvent().toString(), market.log.containsString("Received msgHereIsMoney from cashier. Total = 120.8"));
		
		assertEquals("Cashier should have 1 bills in it. It doesn't.",cashier.mBills.size(), 1);
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to the new market bill), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		assertEquals("Cashier's wallet should have $879.2 - 55. It doesn't.",cashier.wallet, 824.2);
		
		assertTrue("Market2 should have logged \"Received msgHereIsMoney\" but didn't. His log reads instead: "
                + market2.log.getLastLoggedEvent().toString(), market2.log.containsString("Received msgHereIsMoney from cashier. Total = 55.0"));
		
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.mBills.size(), 0);
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
	}
	
	//this tests the most basic flow of one customer one waiter and their interaction with the cashier
	public void testOneNormalCustomerScenario()
	{
		//setUp() runs first before this test!			
		
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.bills.size(), 0);	
		
		cashier.msgComputeCheck(waiter, "Steak", customer);
		
		assertEquals("Cashier should have 1 bills in it. It doesn't.",cashier.bills.size(), 1);
		
		assertEquals("Existing bill cost should equal the cost of the item on the menu. It doesn't.",cashier.bills.get(0).check.balance, cashier.m.getCost("Steak"));
		
		assertEquals("Bill state should be pending.",cashier.bills.get(0).state, CheckState.pending);
		
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the Waiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to the new bill), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		assertTrue("Waiter should have logged \"Received msgHereIsCheck\" but didn't. His log reads instead: "
                + waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received msgHereIsCheck from cashier. Balance = 15.0"));
		
		assertEquals("Bill state should be sent.",cashier.bills.get(0).state, CheckState.sent);
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		cashier.msgHereIsMoney(customer, 15.0);
		
		assertEquals("The payment for the check should equal the amount given by the customer, but it doesnt",cashier.bills.get(0).payment, 15.0);
		
		assertEquals("Bill state should be paid.",cashier.bills.get(0).state, CheckState.paid);
		
		assertEquals("MockCustomer should have an empty event log before the Cashier's scheduler is called. Instead, the Customer's event log reads: "
				+ customer.log.toString(), 0, customer.log.size());
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		assertTrue("Customer should have logged \"Received msgCanLeave\" but didn't. His log reads instead: "
                + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received msgCanLeave from cashier."));
		
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.bills.size(), 0);	
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
	}
	
	//this tests the case when a customer does not have enough money to pay so he is allowed to leave but has to pay his total tab next time
	public void testCustomerDashScenario()
	{
		//setUp() runs first before this test!			
		
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.bills.size(), 0);	
		
		cashier.msgComputeCheck(waiter, "Steak", customer);
		
		assertEquals("Cashier should have 1 bills in it. It doesn't.",cashier.bills.size(), 1);
		
		assertEquals("Existing bill cost should equal the cost of the item on the menu. It doesn't.",cashier.bills.get(0).check.balance, cashier.m.getCost("Steak"));
		
		assertEquals("Bill state should be pending.",cashier.bills.get(0).state, CheckState.pending);
		
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the Waiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to the new bill), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		assertTrue("Waiter should have logged \"Received msgHereIsCheck\" but didn't. His log reads instead: "
                + waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received msgHereIsCheck from cashier. Balance = 15.0"));
		
		assertEquals("Bill state should be sent.",cashier.bills.get(0).state, CheckState.sent);
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		//Customer pays less than is required for his meal
		cashier.msgHereIsMoney(customer, 10.0);
		
		assertEquals("The payment for the check should equal the amount given by the customer, but it doesnt",cashier.bills.get(0).payment, 10.0);
		
		assertEquals("Bill state should be paid.",cashier.bills.get(0).state, CheckState.paid);
		
		assertEquals("MockCustomer should have an empty event log before the Cashier's scheduler is called. Instead, the Customer's event log reads: "
				+ customer.log.toString(), 0, customer.log.size());
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		assertTrue("Customer should have logged \"Received msgPayNextTime\" but didn't. His log reads instead: "
                + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received msgPayNextTime from cashier. Allowed to leave but have to pay remaining bill next time."));
		
		assertEquals("Bill state should be unpaid.",cashier.bills.get(0).state, CheckState.unPaid);
		
		assertEquals("Cashier should have 1 bills in it. It doesn't.",cashier.bills.size(), 1);	
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
	}
	
	//this tests the case when a customer who dashed previously returns and orders again. his bill will contain the money he owed from last time and the money he owes from the new order
	public void testReturningDashCustomerScenario()
	{
		//setUp() runs first before this test!			
		
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.bills.size(), 0);	
		
		//customer dined and dashed before and has now returned
		Check check = new Check(customer, cashier.m.getCost("Steak"));
		
		myCheck mycheck = new myCheck(check, waiter, CheckState.unPaid);
		
		cashier.bills.add(mycheck);
		
		assertEquals("Cashier should have 1 bills in it. It doesn't.",cashier.bills.size(), 1);	
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		cashier.msgComputeCheck(waiter, "Steak", customer);
		
		assertEquals("Cashier should have 1 bills in it. It doesn't.",cashier.bills.size(), 1);
		
		assertEquals("Existing bill cost should equal the cost of the item on the menu and the cost of his original order. It doesn't.",cashier.bills.get(0).check.balance, cashier.m.getCost("Steak")*2);
		
		assertEquals("Bill state should be pending.",cashier.bills.get(0).state, CheckState.pending);
		
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the Waiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to the new bill), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		assertTrue("Waiter should have logged \"Received msgHereIsCheck\" but didn't. His log reads instead: "
                + waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received msgHereIsCheck from cashier. Balance = 30.0"));
		
		assertEquals("Bill state should be sent.",cashier.bills.get(0).state, CheckState.sent);
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		cashier.msgHereIsMoney(customer, 30.0);
		
		assertEquals("The payment for the check should equal the amount given by the customer, but it doesnt",cashier.bills.get(0).payment, 30.0);
		
		assertEquals("Bill state should be paid.",cashier.bills.get(0).state, CheckState.paid);
		
		assertEquals("MockCustomer should have an empty event log before the Cashier's scheduler is called. Instead, the Customer's event log reads: "
				+ customer.log.toString(), 0, customer.log.size());
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		assertTrue("Customer should have logged \"Received msgCanLeave\" but didn't. His log reads instead: "
                + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received msgCanLeave from cashier."));
		
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.bills.size(), 0);	
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
	}
	
	//this tests the case when the cashier has one returning dashing customer and one new customer at the same time. because there were preexisitng bills, this case is important to test
	public void testMultipleCustomerScenario()
	{
		//setUp() runs first before this test!			
		
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.bills.size(), 0);	
		
		//customer dined and dashed before and has now returned
		Check check = new Check(customer, cashier.m.getCost("Steak"));
		
		myCheck mycheck = new myCheck(check, waiter, CheckState.unPaid);
		
		cashier.bills.add(mycheck);
		
		assertEquals("Cashier should have 1 bills in it. It doesn't.",cashier.bills.size(), 1);	
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		cashier.msgComputeCheck(waiter, "Pizza", customer2);
		
		assertEquals("Existing bill cost should equal the cost of the item on the menu. It doesn't.",cashier.bills.get(1).check.balance, cashier.m.getCost("Pizza"));
		
		assertEquals("Bill state should be pending.",cashier.bills.get(1).state, CheckState.pending);
		
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the Waiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());
		
		assertEquals("Cashier should have 1 bills in it. It doesn't.",cashier.bills.size(), 2);	
		
		cashier.msgComputeCheck(waiter, "Steak", customer);
		
		assertEquals("Cashier should have 1 bills in it. It doesn't.",cashier.bills.size(), 2);
		
		assertEquals("Existing bill cost should equal the cost of the item on the menu and the cost of his original order. It doesn't.",cashier.bills.get(0).check.balance, cashier.m.getCost("Steak")*2);
		
		assertEquals("Bill state should be pending.",cashier.bills.get(0).state, CheckState.pending);
		
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the Waiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());
		
		assertEquals("First check should be the prexisitng one.",cashier.bills.get(0).check.customer, customer);
		
		assertEquals("Second check should be the newest one.",cashier.bills.get(1).check.customer, customer2);
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to the new bill), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		assertTrue("Waiter should have logged \"Received msgHereIsCheck\" but didn't. His log reads instead: "
                + waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received msgHereIsCheck from cashier. Balance = 30.0"));
		
		assertEquals("Bill state should be sent.",cashier.bills.get(0).state, CheckState.sent);
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to the new bill), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		//customer2 check
		assertTrue("Waiter should have logged \"Received msgHereIsCheck\" but didn't. His log reads instead: "
                + waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received msgHereIsCheck from cashier. Balance = 9.0"));
		
		assertEquals("Bill state should be sent.",cashier.bills.get(0).state, CheckState.sent);
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		cashier.msgHereIsMoney(customer, 30.0);
		
		assertEquals("The payment for the check should equal the amount given by the customer, but it doesnt",cashier.bills.get(0).payment, 30.0);
		
		assertEquals("Bill state should be paid.",cashier.bills.get(0).state, CheckState.paid);
		
		assertEquals("MockCustomer should have an empty event log before the Cashier's scheduler is called. Instead, the Customer's event log reads: "
				+ customer.log.toString(), 0, customer.log.size());
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		assertTrue("Customer should have logged \"Received msgCanLeave\" but didn't. His log reads instead: "
                + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received msgCanLeave from cashier."));
		
		assertEquals("Cashier should have 1 bills in it. It doesn't.",cashier.bills.size(), 1);	
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		cashier.msgHereIsMoney(customer2, 9.0);
		
		assertEquals("The payment for the check should equal the amount given by the customer, but it doesnt",cashier.bills.get(0).payment, 9.0);
		
		assertEquals("Bill state should be paid.",cashier.bills.get(0).state, CheckState.paid);
		
		assertEquals("MockCustomer should have an empty event log before the Cashier's scheduler is called. Instead, the Customer's event log reads: "
				+ customer2.log.toString(), 0, customer2.log.size());
	
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		assertTrue("Customer should have logged \"Received msgCanLeave\" but didn't. His log reads instead: "
                + customer2.log.getLastLoggedEvent().toString(), customer2.log.containsString("Received msgCanLeave from cashier."));
		
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.bills.size(), 0);	
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
	}
}
