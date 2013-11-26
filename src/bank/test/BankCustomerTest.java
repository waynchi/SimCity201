package bank.test;

import bank.BankCustomerRole;
import bank.BankCustomerRole.CustomerState;
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
public class BankCustomerTest extends TestCase
{

	MockPeopleBank person;
	MockTeller teller;
	BankCustomerRole customer;
		
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		customer = new BankCustomerRole(null);		
		teller = new MockTeller("mockcustomer");
		person = new MockPeopleBank("teller");
		customer.setPerson(person);
		person.teller = teller;
	}	
	//This scenario tests one market order that the cashier has to pay.
	public void testFirstDepositScenario()
	{
		//setUp() runs first before this test!			
		
		//check preconditions
		assertEquals("BankCustomer should have an account id = -1. It doesn't",customer.accountID, -1);	
		
		assertEquals("Person should have 1000 dollars in their wallet. They don't",person.money, 1000.0);
		
		assertFalse("BankCustomer's scheduler should have returned false (no actions to do), but didn't.", 
				customer.pickAndExecuteAnAction());
		
		//check teller ready msg
		
		customer.msgIsActive();
		
		assertTrue("BankCustomer's role isActive variable should be true but is not.", 
				customer.isActive);
		
		assertEquals("BankCustomer state should be inline, it isn't",customer.state, CustomerState.inline);

		assertEquals("MockTeller should have an empty event log before the Customer's scheduler is called. Instead, the MockTeller's event log reads: "
						+ teller.log.toString(), 0, teller.log.size());
		
		assertTrue("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				customer.pickAndExecuteAnAction());
		
		assertTrue("Teller should have logged \"Received msgHere\" but didn't. His log reads instead: "
                + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("received msgHere from customer"));
		
		assertEquals("BankCustomer state should be inline, it isn't",customer.state, CustomerState.none);
		
		assertFalse("BankCustomer's scheduler should have returned false (no actions to do), but didn't.", 
				customer.pickAndExecuteAnAction());
		
	}
}
