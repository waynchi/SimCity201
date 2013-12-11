package bank.test;

import javax.swing.Timer;

import people.PeopleAgent.AgentEvent;
import bank.BankCustomerRole;
import bank.BankCustomerRole.CustomerState;
import bank.gui.BankGui;
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
	BankGui bgui;
		
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();	
		Timer t = null;
		bgui = new BankGui(t);
		customer = new BankCustomerRole(bgui);		
		teller = new MockTeller("mockcustomer");
		person = new MockPeopleBank("teller");
		customer.setPerson(person);
		person.teller = teller;
		customer.isTest = true;
	}	

	public void testFirstDepositScenario()
	{
		//setUp() runs first before this test!			
		
		person.event = AgentEvent.GoingToDepositMoney;
		//check preconditions
		assertEquals("BankCustomer should have an account id = -1. It doesn't",customer.accountID, -1);	
		
		assertEquals("Person should have 1000 dollars in their wallet. They don't",person.money, 1000.0);
		
		assertFalse("BankCustomer's role isActive variable should be false but is not.", 
				customer.isActive);
		
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
		
		assertEquals("BankCustomer state should be none, it isn't",customer.state, CustomerState.none);
		
		assertFalse("BankCustomer's scheduler should have returned false (no actions to do), but didn't.", 
				customer.pickAndExecuteAnAction());
		
		//check msg
		
		customer.msgReadyToHelp(teller);
		
		assertEquals("BankCustomer state should be ready, it isn't",customer.state, CustomerState.ready);
		
		assertTrue("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				customer.pickAndExecuteAnAction());
		
		assertEquals("Person should have 900 dollars in their wallet. They don't",person.money, 900.0);
		
		assertTrue("Teller should have logged \"Received msgDeposit\" but didn't. His log reads instead: "
                + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("Received msgDeposit for: " + 100.0));
		
		assertEquals("BankCustomer state should be finished, it isn't",customer.state, CustomerState.finished);
		
		assertFalse("BankCustomer's scheduler should have returned false (no actions to do), but didn't.", 
				customer.pickAndExecuteAnAction());
		
		customer.msgAccountBalance(0, 100);
		
		assertEquals("BankCustomer should have an account id = 0. It doesn't",customer.accountID, 0);	
		
		assertEquals("BankCustomer state should be done, it isn't",customer.state, CustomerState.done);
		
		assertTrue("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				customer.pickAndExecuteAnAction());
		
		assertTrue("Teller should have logged \"Received msgDoneAndLeaving\" but didn't. His log reads instead: "
                + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("received msgDoneAndLeaving from customer"));
		
		assertTrue("Person should have logged \"Received msgDone\" but didn't. His log reads instead: "
                + person.log.getLastLoggedEvent().toString(), person.log.containsString("BankCustomerRole"));
		
		assertFalse("BankCustomer's role isActive variable should be false but is not.", 
				customer.isActive);
		
	}
	
	public void testSecondDepositScenario()
	{
		//setUp() runs first before this test!			
		
		person.event = AgentEvent.GoingToDepositMoney;
		
		customer.accountID = 0;
		
		//check preconditions
		assertEquals("BankCustomer should have an account id = 0. It doesn't",customer.accountID, 0);	
		
		assertEquals("Person should have 1000 dollars in their wallet. They don't",person.money, 1000.0);
		
		assertFalse("BankCustomer's role isActive variable should be false but is not.", 
				customer.isActive);
		
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
		
		assertEquals("BankCustomer state should be none, it isn't",customer.state, CustomerState.none);
		
		assertFalse("BankCustomer's scheduler should have returned false (no actions to do), but didn't.", 
				customer.pickAndExecuteAnAction());
		
		//check msg
		
		customer.msgReadyToHelp(teller);
		
		assertEquals("BankCustomer state should be ready, it isn't",customer.state, CustomerState.ready);
		
		assertTrue("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				customer.pickAndExecuteAnAction());
		
		assertEquals("Person should have 900 dollars in their wallet. They don't",person.money, 900.0);
		
		assertTrue("Teller should have logged \"Received msgDeposit\" but didn't. His log reads instead: "
                + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("Received msgDeposit for: " + 100.0));
		
		assertEquals("BankCustomer state should be finished, it isn't",customer.state, CustomerState.finished);
		
		assertFalse("BankCustomer's scheduler should have returned false (no actions to do), but didn't.", 
				customer.pickAndExecuteAnAction());
		
		customer.msgAccountBalance(0, 100);
		
		assertEquals("BankCustomer should have an account id = 0. It doesn't",customer.accountID, 0);	
		
		assertEquals("BankCustomer state should be done, it isn't",customer.state, CustomerState.done);
		
		assertTrue("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				customer.pickAndExecuteAnAction());
		
		assertTrue("Teller should have logged \"Received msgDoneAndLeaving\" but didn't. His log reads instead: "
                + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("received msgDoneAndLeaving from customer"));
		
		assertTrue("Person should have logged \"Received msgDone\" but didn't. His log reads instead: "
                + person.log.getLastLoggedEvent().toString(), person.log.containsString("BankCustomerRole"));
		
		assertFalse("BankCustomer's role isActive variable should be false but is not.", 
				customer.isActive);
		
	}
	
	public void testFirstWithdrawScenario()
	{
		//setUp() runs first before this test!			
		
		person.event = AgentEvent.GoingToRetrieveMoney;
		//check preconditions
		assertEquals("BankCustomer should have an account id = -1. It doesn't",customer.accountID, -1);	
		
		assertEquals("Person should have 1000 dollars in their wallet. They don't",person.money, 1000.0);
		
		assertFalse("BankCustomer's role isActive variable should be false but is not.", 
				customer.isActive);
		
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
		
		assertEquals("BankCustomer state should be none, it isn't",customer.state, CustomerState.none);
		
		assertFalse("BankCustomer's scheduler should have returned false (no actions to do), but didn't.", 
				customer.pickAndExecuteAnAction());
		
		//check msg
		
		customer.msgReadyToHelp(teller);
		
		assertEquals("BankCustomer state should be ready, it isn't",customer.state, CustomerState.ready);
		
		assertTrue("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				customer.pickAndExecuteAnAction());
		
		assertTrue("Teller should have logged \"Received msgWithdraw\" but didn't. His log reads instead: "
                + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("Received msgWithdraw for: " + 30000.0));
		
		assertEquals("BankCustomer state should be finished, it isn't",customer.state, CustomerState.finished);
		
		assertFalse("BankCustomer's scheduler should have returned false (no actions to do), but didn't.", 
				customer.pickAndExecuteAnAction());
		
		customer.msgAccountAndLoan(0, -30000, 30000);
		
		assertEquals("Person should have 31000 dollars in their wallet. They don't",person.money, 31000.0);
		
		assertEquals("BankCustomer should have an account id = 0. It doesn't",customer.accountID, 0);	
		
		assertEquals("BankCustomer state should be done, it isn't",customer.state, CustomerState.done);
		
		assertTrue("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				customer.pickAndExecuteAnAction());
		
		assertTrue("Teller should have logged \"Received msgDoneAndLeaving\" but didn't. His log reads instead: "
                + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("received msgDoneAndLeaving from customer"));
		
		assertTrue("Person should have logged \"Received msgDone\" but didn't. His log reads instead: "
                + person.log.getLastLoggedEvent().toString(), person.log.containsString("BankCustomerRole"));
		
		assertFalse("BankCustomer's role isActive variable should be false but is not.", 
				customer.isActive);
		
	}
	public void testSecondWithdrawScenario()
	{
		//setUp() runs first before this test!			
		
		person.event = AgentEvent.GoingToRetrieveMoney;
		
		customer.accountID = 0;
		//check preconditions
		assertEquals("BankCustomer should have an account id = 0. It doesn't",customer.accountID, 0);	
		
		assertEquals("Person should have 1000 dollars in their wallet. They don't",person.money, 1000.0);
		
		assertFalse("BankCustomer's role isActive variable should be false but is not.", 
				customer.isActive);
		
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
		
		assertEquals("BankCustomer state should be none, it isn't",customer.state, CustomerState.none);
		
		assertFalse("BankCustomer's scheduler should have returned false (no actions to do), but didn't.", 
				customer.pickAndExecuteAnAction());
		
		//check msg
		
		customer.msgReadyToHelp(teller);
		
		assertEquals("BankCustomer state should be ready, it isn't",customer.state, CustomerState.ready);
		
		assertTrue("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				customer.pickAndExecuteAnAction());
		
		assertTrue("Teller should have logged \"Received msgWithdraw\" but didn't. His log reads instead: "
                + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("Received msgWithdraw for: " + 30000.0));
		
		assertEquals("BankCustomer state should be finished, it isn't",customer.state, CustomerState.finished);
		
		assertFalse("BankCustomer's scheduler should have returned false (no actions to do), but didn't.", 
				customer.pickAndExecuteAnAction());
		
		customer.msgWithdrawSuccessful(-30000, 30000);
		
		assertEquals("Person should have 31000 dollars in their wallet. They don't",person.money, 31000.0);
		
		assertEquals("BankCustomer should have an account id = 0. It doesn't",customer.accountID, 0);	
		
		assertEquals("BankCustomer state should be done, it isn't",customer.state, CustomerState.done);
		
		assertTrue("Customer's scheduler should have returned true (needs to call teller to let him know he is at the bank), but didn't.", 
				customer.pickAndExecuteAnAction());
		
		assertTrue("Teller should have logged \"Received msgDoneAndLeaving\" but didn't. His log reads instead: "
                + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("received msgDoneAndLeaving from customer"));
		
		assertTrue("Person should have logged \"Received msgDone\" but didn't. His log reads instead: "
                + person.log.getLastLoggedEvent().toString(), person.log.containsString("BankCustomerRole"));
		
		assertFalse("BankCustomer's role isActive variable should be false but is not.", 
				customer.isActive);
		
	}
}
