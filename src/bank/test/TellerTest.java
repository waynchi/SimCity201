package bank.test;

import people.PeopleAgent.AgentEvent;
import bank.BankCustomerRole;
import bank.TellerRole.CustomerState;
import bank.TellerRole;
import bank.gui.BankGui;
import bank.test.mock.MockBankCustomer;
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
		
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		bgui = new BankGui();
		customer = new MockBankCustomer("mockcustomer");		
		teller = new TellerRole(null);
		person = new MockPeopleBank("teller");
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
	
}
