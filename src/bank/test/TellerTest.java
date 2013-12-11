package bank.test;

import javax.swing.Timer;

import people.PeopleAgent.AgentEvent;
import bank.BankCustomerRole;
import bank.TellerRole.Account;
import bank.TellerRole.CustomerState;
import bank.TellerRole;
import bank.gui.BankGui;
import bank.test.mock.MockBankCustomer;
import bank.test.mock.MockCashier;
import bank.test.mock.MockMarketCashier;
import bank.test.mock.MockPeopleBank;
import bank.test.mock.MockTeller;
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

	MockPeopleBank person;
	TellerRole teller;
	MockBankCustomer customer;
	BankGui bgui;
	MockCashier cashier;
	MockMarketCashier mcashier;
		
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();
		Timer t = null;
		bgui = new BankGui(t);
		customer = new MockBankCustomer("mockcustomer");		
		teller = new TellerRole(null);
		person = new MockPeopleBank("teller");
		cashier = new MockCashier("cashier");
		mcashier = new MockMarketCashier("mcashier");
		teller.setPerson(person);
		person.teller = teller;
		teller.isTest = true;
	}	

	public void testFirstDepositScenario()
	{
		//setUp() runs first before this test!			
		
		//check preconditions
		assertEquals("Person should have 1000 dollars in their wallet. They don't",customer.money, 1000.0);
		
		assertFalse("BankCustomer's role isActive variable should be false but is not.", 
				teller.isActive);
		
		assertFalse("BankCustomer's scheduler should have returned false (no actions to do), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		//check teller ready msg
		
		teller.msgIsActive();
		
		assertTrue("BankCustomer's role isActive variable should be true but is not.", 
				teller.isActive);
		
		assertFalse("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		assertEquals("Teller customer list should be empty, it isnt",teller.waitingCustomers.size(), 0);
		
		assertEquals("Teller should not have a current customer, it does",teller.currentCustomer, null);
		
		teller.msgHere(customer, customer.name);
		
		assertEquals("Teller customer list should have 1 customer, it isnt",teller.waitingCustomers.size(), 1);
		
		assertTrue("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		assertEquals("Teller current customer should equal first waiting customer",teller.currentCustomer, teller.waitingCustomers.get(0));
		
		assertTrue("Customer should have logged \"Received msgReadyToHelp\" but didn't. His log reads instead: "
                + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("received msgReadyToHelp from teller"));
		
		assertEquals("Teller should have no accounts",teller.accounts.size(), 0);
		
		assertFalse("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		teller.msgDeposit(100.0);
		
		assertEquals("BankCustomer state should be none, it isn't",teller.currentCustomer.state, CustomerState.newAccount);
		
		assertTrue("BankCustomer's scheduler should have returned false (no actions to do), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		assertEquals("Teller should have 1 accounts",teller.accounts.size(), 1);
		
		assertEquals("Account should have 100 dollars",teller.accounts.get(1).funds, 100.0);
		
		assertTrue("Customer should have logged \"Received account and balance\" but didn't. His log reads instead: "
                + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("received account and balance " + 1 + " " + 100.0));
		
		assertFalse("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		teller.msgDoneAndLeaving();
		
		assertEquals("BankCustomer state should be none, it isn't",teller.currentCustomer.state, CustomerState.done);
		
		assertTrue("BankCustomer's scheduler should have returned false (no actions to do), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		assertEquals("Teller customer list should be empty, it isnt",teller.waitingCustomers.size(), 0);
		
		assertEquals("Teller should not have a current customer, it does",teller.currentCustomer, null);
		
		assertFalse("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		
	}
	
	public void testSecondDepositScenario()
	{
		//setUp() runs first before this test!			
		
		//create new account
		
		teller.accounts.put(1, teller.new Account(customer.name, 1));
		customer.accountID = 1;
		
		//check preconditions
		assertEquals("Person should have 1000 dollars in their wallet. They don't",customer.money, 1000.0);
		
		assertFalse("BankCustomer's role isActive variable should be false but is not.", 
				teller.isActive);
		
		assertFalse("BankCustomer's scheduler should have returned false (no actions to do), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		//check teller ready msg
		
		teller.msgIsActive();
		
		assertTrue("BankCustomer's role isActive variable should be true but is not.", 
				teller.isActive);
		
		assertFalse("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		assertEquals("Teller customer list should be empty, it isnt",teller.waitingCustomers.size(), 0);
		
		assertEquals("Teller should not have a current customer, it does",teller.currentCustomer, null);
		
		teller.msgHere(customer, customer.name);
		
		assertEquals("Teller customer list should have 1 customer, it isnt",teller.waitingCustomers.size(), 1);
		
		assertTrue("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		assertEquals("Teller current customer should equal first waiting customer",teller.currentCustomer, teller.waitingCustomers.get(0));
		
		assertTrue("Customer should have logged \"Received msgReadyToHelp\" but didn't. His log reads instead: "
                + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("received msgReadyToHelp from teller"));
		
		assertEquals("Teller should have 1 accounts",teller.accounts.size(), 1);
		
		assertFalse("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		teller.msgDeposit(1, 100.0);
		
		assertEquals("BankCustomer state should be none, it isn't",teller.currentCustomer.state, CustomerState.deposit);
		
		assertTrue("BankCustomer's scheduler should have returned true , but didn't.", 
				teller.pickAndExecuteAnAction());
		
		assertEquals("Teller should have 1 accounts",teller.accounts.size(), 1);
		
		assertEquals("Account should have 100 dollars",teller.accounts.get(1).funds, 100.0);
		
		assertTrue("Customer should have logged \"Received account balance\" but didn't. His log reads instead: "
                + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("received account balance " + 100.0));
		
		assertFalse("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		teller.msgDoneAndLeaving();
		
		assertEquals("BankCustomer state should be none, it isn't",teller.currentCustomer.state, CustomerState.done);
		
		assertTrue("BankCustomer's scheduler should have returned false (no actions to do), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		assertEquals("Teller customer list should be empty, it isnt",teller.waitingCustomers.size(), 0);
		
		assertEquals("Teller should not have a current customer, it does",teller.currentCustomer, null);
		
		assertFalse("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		
	}
	
	public void testSecondWithdrawScenario()
	{
		//setUp() runs first before this test!			
		
		//create new account
		
		teller.accounts.put(1, teller.new Account(customer.name, 1));
		customer.accountID = 1;
		teller.accounts.get(1).funds = 200.0;
		
		//check preconditions
		assertEquals("Person should have 1000 dollars in their wallet. They don't",customer.money, 1000.0);
		
		assertFalse("BankCustomer's role isActive variable should be false but is not.", 
				teller.isActive);
		
		assertFalse("BankCustomer's scheduler should have returned false (no actions to do), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		//check teller ready msg
		
		teller.msgIsActive();
		
		assertTrue("BankCustomer's role isActive variable should be true but is not.", 
				teller.isActive);
		
		assertFalse("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		assertEquals("Teller customer list should be empty, it isnt",teller.waitingCustomers.size(), 0);
		
		assertEquals("Teller should not have a current customer, it does",teller.currentCustomer, null);
		
		teller.msgHere(customer, customer.name);
		
		assertEquals("Teller customer list should have 1 customer, it isnt",teller.waitingCustomers.size(), 1);
		
		assertTrue("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		assertEquals("Teller current customer should equal first waiting customer",teller.currentCustomer, teller.waitingCustomers.get(0));
		
		assertTrue("Customer should have logged \"Received msgReadyToHelp\" but didn't. His log reads instead: "
                + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("received msgReadyToHelp from teller"));
		
		assertEquals("Teller should have 1 accounts",teller.accounts.size(), 1);
		
		assertFalse("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		teller.msgWithdraw(1, 100.0);
		
		assertEquals("BankCustomer state should be none, it isn't",teller.currentCustomer.state, CustomerState.withdraw);
		
		assertTrue("BankCustomer's scheduler should have returned true , but didn't.", 
				teller.pickAndExecuteAnAction());
		
		assertEquals("Teller should have 1 accounts",teller.accounts.size(), 1);
		
		assertEquals("Account should have 100 dollars",teller.accounts.get(1).funds, 100.0);
		
		assertTrue("Customer should have logged \"Received successful withdrawl\" but didn't. His log reads instead: "
                + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("received successful withdraw of: " + 100.0 + " balance is: " + 100.0));
		
		assertFalse("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		teller.msgDoneAndLeaving();
		
		assertEquals("BankCustomer state should be none, it isn't",teller.currentCustomer.state, CustomerState.done);
		
		assertTrue("BankCustomer's scheduler should have returned false (no actions to do), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		assertEquals("Teller customer list should be empty, it isnt",teller.waitingCustomers.size(), 0);
		
		assertEquals("Teller should not have a current customer, it does",teller.currentCustomer, null);
		
		assertFalse("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		
	}
	
	public void testThirdWithdrawScenario()
	{
		//setUp() runs first before this test!			
		
		//create new account
		
		teller.accounts.put(1, teller.new Account(customer.name, 1));
		customer.accountID = 1;
		teller.accounts.get(1).funds = 200.0;
		
		//check preconditions
		assertEquals("Person should have 1000 dollars in their wallet. They don't",customer.money, 1000.0);
		
		assertFalse("BankCustomer's role isActive variable should be false but is not.", 
				teller.isActive);
		
		assertFalse("BankCustomer's scheduler should have returned false (no actions to do), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		//check teller ready msg
		
		teller.msgIsActive();
		
		assertTrue("BankCustomer's role isActive variable should be true but is not.", 
				teller.isActive);
		
		assertFalse("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		assertEquals("Teller customer list should be empty, it isnt",teller.waitingCustomers.size(), 0);
		
		assertEquals("Teller should not have a current customer, it does",teller.currentCustomer, null);
		
		teller.msgHere(customer, customer.name);
		
		assertEquals("Teller customer list should have 1 customer, it isnt",teller.waitingCustomers.size(), 1);
		
		assertTrue("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		assertEquals("Teller current customer should equal first waiting customer",teller.currentCustomer, teller.waitingCustomers.get(0));
		
		assertTrue("Customer should have logged \"Received msgReadyToHelp\" but didn't. His log reads instead: "
                + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("received msgReadyToHelp from teller"));
		
		assertEquals("Teller should have 1 accounts",teller.accounts.size(), 1);
		
		assertFalse("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		teller.msgWithdraw(1, 300.0);
		
		assertEquals("BankCustomer state should be none, it isn't",teller.currentCustomer.state, CustomerState.withdraw);
		
		assertTrue("BankCustomer's scheduler should have returned true , but didn't.", 
				teller.pickAndExecuteAnAction());
		
		assertEquals("Teller should have 1 accounts",teller.accounts.size(), 1);
		
		assertEquals("Account should have -100 dollars",teller.accounts.get(1).funds, -100.0);
		
		assertTrue("Customer should have logged \"Received successful withdrawl\" but didn't. His log reads instead: "
                + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("received Loan of: " + 300.0 + " balance is: " + -100.0));
		
		assertFalse("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		teller.msgDoneAndLeaving();
		
		assertEquals("BankCustomer state should be none, it isn't",teller.currentCustomer.state, CustomerState.done);
		
		assertTrue("BankCustomer's scheduler should have returned false (no actions to do), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		assertEquals("Teller customer list should be empty, it isnt",teller.waitingCustomers.size(), 0);
		
		assertEquals("Teller should not have a current customer, it does",teller.currentCustomer, null);
		
		assertFalse("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		
	}
	
	public void testFirstWithdrawScenario()
	{
		//setUp() runs first before this test!			
		
		//check preconditions
		assertEquals("Person should have 1000 dollars in their wallet. They don't",customer.money, 1000.0);
		
		assertFalse("BankCustomer's role isActive variable should be false but is not.", 
				teller.isActive);
		
		assertFalse("BankCustomer's scheduler should have returned false (no actions to do), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		//check teller ready msg
		
		teller.msgIsActive();
		
		assertTrue("BankCustomer's role isActive variable should be true but is not.", 
				teller.isActive);
		
		assertFalse("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		assertEquals("Teller customer list should be empty, it isnt",teller.waitingCustomers.size(), 0);
		
		assertEquals("Teller should not have a current customer, it does",teller.currentCustomer, null);
		
		teller.msgHere(customer, customer.name);
		
		assertEquals("Teller customer list should have 1 customer, it isnt",teller.waitingCustomers.size(), 1);
		
		assertTrue("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		assertEquals("Teller current customer should equal first waiting customer",teller.currentCustomer, teller.waitingCustomers.get(0));
		
		assertTrue("Customer should have logged \"Received msgReadyToHelp\" but didn't. His log reads instead: "
                + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("received msgReadyToHelp from teller"));
		
		assertEquals("Teller should have 0 accounts",teller.accounts.size(), 0);
		
		assertFalse("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		teller.msgWithdraw(100.0);
		
		assertEquals("BankCustomer state should be none, it isn't",teller.currentCustomer.state, CustomerState.newAccountLoan);
		
		assertTrue("BankCustomer's scheduler should have returned true , but didn't.", 
				teller.pickAndExecuteAnAction());
		
		assertEquals("Teller should have 1 accounts",teller.accounts.size(), 1);
		
		assertEquals("Account should have -100 dollars",teller.accounts.get(1).funds, -100.0);
		
		assertTrue("Customer should have logged \"Received loan and account\" but didn't. His log reads instead: "
                + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("received Loan of: " + 100.0 + " balance is: " + -100.0 + "account number is: " + 1));
		
		assertFalse("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		teller.msgDoneAndLeaving();
		
		assertEquals("BankCustomer state should be none, it isn't",teller.currentCustomer.state, CustomerState.done);
		
		assertTrue("BankCustomer's scheduler should have returned false (no actions to do), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		assertEquals("Teller customer list should be empty, it isnt",teller.waitingCustomers.size(), 0);
		
		assertEquals("Teller should not have a current customer, it does",teller.currentCustomer, null);
		
		assertFalse("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		
	}
	
	public void testCashierDepositScenario()
	{
		//setUp() runs first before this test!			
		
		//create new account
		
		teller.accounts.put(1, teller.new Account(cashier.name, 1));
		cashier.accountID = 1;
		
		//check preconditions
		
		assertFalse("Teller's role isActive variable should be false but is not.", 
				teller.isActive);
		
		assertFalse("Teller's scheduler should have returned false (no actions to do), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		//check teller ready msg
		
		teller.msgIsActive();
		
		assertTrue("BankCustomer's role isActive variable should be true but is not.", 
				teller.isActive);
		
		assertFalse("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		assertEquals("Teller customer list should be empty, it isnt",teller.waitingCustomers.size(), 0);
		
		assertEquals("Teller should not have a current customer, it does",teller.currentCustomer, null);
		
		teller.msgNeedHelp(cashier, cashier.name);
		
		assertEquals("Teller customer list should have 1 customer, it isnt",teller.waitingCustomers.size(), 1);
		
		assertTrue("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		assertEquals("Teller current customer should equal first waiting customer",teller.currentCustomer, teller.waitingCustomers.get(0));
		
		assertTrue("Cashier should have logged \"Received msgReadyToHelp\" but didn't. His log reads instead: "
                + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("received msgReadyToHelp from teller"));
		
		assertEquals("Teller should have 1 accounts",teller.accounts.size(), 1);
		
		assertFalse("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		teller.msgDeposit(1, 100.0);
		
		assertEquals("myBankCustomer state should be none, it isn't",teller.currentCustomer.state, CustomerState.deposit);
		
		assertTrue("Teller's scheduler should have returned true , but didn't.", 
				teller.pickAndExecuteAnAction());
		
		assertEquals("Teller should have 1 accounts",teller.accounts.size(), 1);
		
		assertEquals("Account should have 100 dollars",teller.accounts.get(1).funds, 100.0);
		
		assertTrue("Cashier should have logged \"Received account balance\" but didn't. His log reads instead: "
                + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("received account balance " + 100.0));
		
		assertFalse("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		teller.msgDoneAndLeaving();
		
		assertEquals("BankCustomer state should be none, it isn't",teller.currentCustomer.state, CustomerState.done);
		
		assertTrue("BankCustomer's scheduler should have returned false (no actions to do), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		assertEquals("Teller customer list should be empty, it isnt",teller.waitingCustomers.size(), 0);
		
		assertEquals("Teller should not have a current customer, it does",teller.currentCustomer, null);
		
		assertFalse("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		
	}
	
	public void testMarketCashierDepositScenario()
	{
		//setUp() runs first before this test!			
		
		//create new account
		
		teller.accounts.put(1, teller.new Account(cashier.name, 1));
		mcashier.accountID = 1;
		
		//check preconditions
		
		assertFalse("Teller's role isActive variable should be false but is not.", 
				teller.isActive);
		
		assertFalse("Teller's scheduler should have returned false (no actions to do), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		//check teller ready msg
		
		teller.msgIsActive();
		
		assertTrue("BankCustomer's role isActive variable should be true but is not.", 
				teller.isActive);
		
		assertFalse("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		assertEquals("Teller customer list should be empty, it isnt",teller.waitingCustomers.size(), 0);
		
		assertEquals("Teller should not have a current customer, it does",teller.currentCustomer, null);
		
		teller.msgNeedHelp(mcashier, mcashier.name);
		
		assertEquals("Teller customer list should have 1 customer, it isnt",teller.waitingCustomers.size(), 1);
		
		assertTrue("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		assertEquals("Teller current customer should equal first waiting customer",teller.currentCustomer, teller.waitingCustomers.get(0));
		
		assertTrue("Cashier should have logged \"Received msgReadyToHelp\" but didn't. His log reads instead: "
                + mcashier.log.getLastLoggedEvent().toString(), mcashier.log.containsString("received msgReadyToHelp from teller"));
		
		assertEquals("Teller should have 1 accounts",teller.accounts.size(), 1);
		
		assertFalse("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		teller.msgDeposit(1, 100.0);
		
		assertEquals("myBankCustomer state should be none, it isn't",teller.currentCustomer.state, CustomerState.deposit);
		
		assertTrue("Teller's scheduler should have returned true , but didn't.", 
				teller.pickAndExecuteAnAction());
		
		assertEquals("Teller should have 1 accounts",teller.accounts.size(), 1);
		
		assertEquals("Account should have 100 dollars",teller.accounts.get(1).funds, 100.0);
		
		assertTrue("Cashier should have logged \"Received account balance\" but didn't. His log reads instead: "
                + mcashier.log.getLastLoggedEvent().toString(), mcashier.log.containsString("received account balance " + 100.0));
		
		assertFalse("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		teller.msgDoneAndLeaving();
		
		assertEquals("BankCustomer state should be none, it isn't",teller.currentCustomer.state, CustomerState.done);
		
		assertTrue("BankCustomer's scheduler should have returned false (no actions to do), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		assertEquals("Teller customer list should be empty, it isnt",teller.waitingCustomers.size(), 0);
		
		assertEquals("Teller should not have a current customer, it does",teller.currentCustomer, null);
		
		assertFalse("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				teller.pickAndExecuteAnAction());
		
		
	}
	
}
