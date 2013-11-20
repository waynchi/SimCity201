package test;

import people.People;
import people.PeopleAgent;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import restaurant.CashierRole;
import restaurant.gui.RestaurantPanel;
import restaurant.interfaces.Market;
import restaurant.test.mock.MockCustomer;
import restaurant.test.mock.MockMarket;
import restaurant.test.mock.MockWaiter;

public class CashierTest extends TestCase {
	CashierRole cashier;
	MockWaiter waiter1;
	MockWaiter waiter2;
	MockCustomer customer1;
	MockCustomer customer2;
	MockMarket market1;
	MockMarket market2;
	enum checkState {COMPUTED, SENT_TO_WAITER, BEING_PAID};
	RestaurantPanel restaurantPanel;
	People person;


	public static void main(String args[]) {
		junit.textui.TestRunner.run(suite());
	}

	public static TestSuite suite ( ) {
		return new TestSuite(CashierTest.class);
	}

	public class MarketBill {
		Market market;
		Double amount;

		public MarketBill (Market m , double a) {
			market = m;
			amount = a;
		}
	}

	public void setUp() throws Exception{
		super.setUp();                
		cashier = new CashierRole("cashier");                
		customer1 = new MockCustomer("mockcustomer1");  
		customer2 = new MockCustomer("mockcustomer2");
		waiter1 = new MockWaiter("mockwaiter1");
		waiter2 = new MockWaiter("mockwaiter2");
		market1 = new MockMarket("mockmarket1");
		market2 = new MockMarket("mockmarket2");
		person = new PeopleAgent();
		cashier.setPerson(person);
		person.addRole(cashier, "Cashier");
	}        






	public void testOneBillOneMarket() {
		//precondition: 
		assertEquals("Cashier should have 0 MarketBill in marketBills list. It doesn't.", cashier.getMarketBills().size(), 0);
		assertEquals("CashierAgent should have an empty event log before msgHereIsMarketBill is called. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 0, cashier.log.size());
		//assertEquals("Cashier should have only one permit for stateChange semaphore. It doesn't.", 
		//		cashier.getStateChange().availablePermits(),1);

		//post condition for messaging and precondition for scheduler
		cashier.msgHereIsMarketBill(market1, 14.99);
		assertTrue("CashierAgent should have an event log after the msgHereIsMarketBill is called. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("Received msgHereIsMarketBill from Market mockmarket1 and the amount is 14.99"));
		assertEquals("Cashier should have 1 MarketBill in marketBills list. It doesn't.", 
				cashier.getMarketBills().size(), 1);
		//assertEquals("Cashier should have only one permit for stateChange semaphore. It doesn't.", 
		//		cashier.getStateChange().availablePermits(), 2);

		//we directly call the scheduler
		cashier.pickAndExecuteAnAction();

		//check if the right action is called
		assertTrue("CashierAgent should have an event log after the scheduler is called. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("In action payMarket, paying market mockmarket1"));
		assertEquals("The money Cashier holds should be 100-14.99 = 85.01 right now. Instead it's "+ cashier.getMyMoney(),
				cashier.getMyMoney(), 85.01);
		assertEquals("this bill should be removed from marketBills list right now. It's not.", cashier.getMarketBills().size(),0);

		//check if the right message has been sent out.
		assertTrue("market1 should have received the message from the cashier, but the event log reads: " + 
				market1.log.toString(), market1.log.containsString("Received msgPayMarketBill "
						+ "from Cashier cashier and the amount is 14.99"));
	}






	public void testTwoBillsTwoMarkets() {
		//precondition: 
		assertEquals("Cashier should have 0 MarketBill in marketBills list. It doesn't.", cashier.getMarketBills().size(), 0);
		assertEquals("CashierAgent should have an empty event log before msgHereIsMarketBill is called. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 0, cashier.log.size());
		//assertEquals("Cashier should have only one permit for stateChange semaphore. It doesn't.", 
		//		cashier.getStateChange().availablePermits(),1);

		//received message from market1
		cashier.msgHereIsMarketBill(market1, 14.99);
		assertTrue("CashierAgent should have an event log after the msgHereIsMarketBill is called. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("Received msgHereIsMarketBill from Market mockmarket1 and the amount is 14.99"));
		assertEquals("Cashier should have 1 MarketBill in marketBills list. It doesn't.", 
				cashier.getMarketBills().size(), 1);
		//assertEquals("Cashier should have two permits for stateChange semaphore. It doesn't.", 
		//		cashier.getStateChange().availablePermits(), 2);

		//received another message from market2
		cashier.msgHereIsMarketBill(market2, 3.99);
		assertTrue("CashierAgent should have an event log after the msgHereIsMarketBill is called. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("Received msgHereIsMarketBill from Market mockmarket2 and the amount is 3.99"));
		assertEquals("Cashier should have 2 MarketBill in marketBills list. It doesn't.", 
				cashier.getMarketBills().size(), 2);
		assertTrue("the newly added MarketBill should be at the end of the marketBills list. It's not", 
				cashier.getMarketBills().get(cashier.getMarketBills().size()-1).toString().equals("mockmarket2 3.99"));
		//assertEquals("Cashier should have 3 permit for stateChange semaphore. It doesn't.", 
		//		cashier.getStateChange().availablePermits(), 3);


		//we directly call the scheduler
		cashier.pickAndExecuteAnAction();

		//cashier should be dealing with the bill from market1 right now
		//check if the right action is called
		assertTrue("CashierAgent should be paying market1. It doesn't. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("In action payMarket, paying market mockmarket1"));
		assertEquals("The money Cashier holds should be 100-14.99 = 85.01 right now. Instead it's "+ cashier.getMyMoney(),
				cashier.getMyMoney(), 85.01);
		assertTrue("this bill should be removed from marketBills list right now. It's not.", 
				!cashier.getMarketBills().contains(new MarketBill(market1, 14.99)));
		assertEquals("Right now the marketBills list should have size of 1. It doesn't.", cashier.getMarketBills().size(),1);

		//check if the right message has been sent to market1
		assertTrue("MockMarket should have received the message from the cashier to pay the bill, but the event log reads: " + 
				market1.log.toString(), market1.log.containsString("Received msgPayMarketBill "
						+ "from Cashier cashier and the amount is 14.99"));

		// we call the scheduler again
		cashier.pickAndExecuteAnAction();

		//this time cashier should pay for the bill sent by market2
		//check if the right action is executed
		assertTrue("CashierAgent should be paying market2. It doesn't. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("In action payMarket, paying market mockmarket2"));
		assertEquals("The money Cashier holds should be 85.01-3.99 = 81.02 right now. Instead it's "+ cashier.getMyMoney(),
				cashier.getMyMoney(), 85.01-3.99);
		assertTrue("this bill should be removed from marketBills list right now. It's not.", 
				!cashier.getMarketBills().contains(new MarketBill(market2, 3.99)));
		assertEquals("Right now the marketBills list should be empty. It is not.", cashier.getMarketBills().size(),0);

		//check if the right message has been sent to market1
		assertTrue("market2 should have received the message from the cashier to pay the bill, but the event log reads: " + 
				market2.log.toString(), market2.log.containsString("Received msgPayMarketBill "
						+ "from Cashier cashier and the amount is 3.99"));
	}







	// Scenario: waiter gives a bill to cashier to compute, cashier then sends the check back to waiter. The customer
	// involved will then pay his check and he has enough money to pay.
	public void testOneWaiterOneCustomer() {
		waiter1.setCashier(cashier);

		//precondition tests
		assertTrue("Cashier should have empty checks list. It doesn't.",cashier.getChecks().isEmpty());
		assertTrue("CashierAgent should have an empty balance entry in balance list. It doesn't.", 
				cashier.getBalance().isEmpty());
		assertEquals("CashierAgent should have an empty event log before any message calls. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 0, cashier.log.size());
		//assertEquals("Cashier should have only one permit for stateChange semaphore. It doesn't. Instead, it has " + 
		//		cashier.getStateChange().availablePermits(), cashier.getStateChange().availablePermits(),1);

		//precondition on market front 
		assertEquals("Cashier should have 0 MarketBill in marketBills list. It doesn't.", cashier.getMarketBills().size(), 0);
		assertEquals("CashierAgent should have an empty event log before msgHereIsMarketBill is called. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 0, cashier.log.size());
		//assertEquals("Cashier should have only one permit for stateChange semaphore. It doesn't.", 
		//		cashier.getStateChange().availablePermits(),1);

		//received message from waiter
		cashier.msgHereIsBill(customer1, "Steak", waiter1);
		assertTrue("CashierAgent should have received msgHereIsBill from mockwaiter1. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("Received msgHereIsBill from Waiter mockwaiter1 with Customer mockcustomer1 and food Steak"));
		assertEquals("Cashier should have 1 element in checks list. It doesn't.", cashier.getChecks().size(), 1);
		assertTrue("the newly added check in the check list should have the state of COMPUTED. It doesn't", 
				cashier.getChecks().get(0).getState() == CashierRole.checkState.COMPUTED);
		assertTrue("customer1 should be contained in the balance map as a key. It doesn't.", 
				cashier.getBalance().containsKey(customer1));
		assertEquals("Balance for the customer should be 15.99. It doesn't. Instead, the balance for customer1"
				+ " is " + cashier.getBalance().get(customer1), cashier.getBalance().get(customer1), 15.99);
		//assertEquals("Cashier should have two permit for stateChange semaphore. It doesn't. Instead, it has " + 
		//		cashier.getStateChange().availablePermits(), cashier.getStateChange().availablePermits(),2);

		//received message from market
		cashier.msgHereIsMarketBill(market1, 14.99);
		assertTrue("CashierAgent should have an event log after the msgHereIsMarketBill is called. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("Received msgHereIsMarketBill from Market mockmarket1 and the amount is 14.99"));
		assertEquals("Cashier should have 1 MarketBill in marketBills list. It doesn't.", 
				cashier.getMarketBills().size(), 1);
		//assertEquals("Cashier should have 3 permits for stateChange semaphore. It doesn't.", 
		//		cashier.getStateChange().availablePermits(), 3);

		//call the scheduler
		cashier.pickAndExecuteAnAction();

		//cashier should be ready to send the check to waiter1
		//check if the right action has been executed
		assertTrue("CashierAgent should be sending check to waiter1. It doesn't. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("In action sendCheckToWaiter, ready to send Check to mockwaiter1. Customer mockcustomer1 needs"
						+ " to pay 15.99"));		
		assertTrue("the check in the check list should have the state of SENT_TO_WAITER. It doesn't", 
				cashier.getChecks().get(0).getState() == CashierRole.checkState.SENT_TO_WAITER);

		//check if the right message has been sent to waiter1
		assertTrue("waiter1 should have received the message from the cashier with the computed check, but the event log reads: " + 
				waiter1.log.toString(), waiter1.log.containsString("Received message msgHereIsCheck from cashier. Customer mockcustomer1 "
						+ "needs to pay 15.99"));

		//we directly call the scheduler
		cashier.pickAndExecuteAnAction();

		//cashier should be paying the market bill right now
		assertTrue("CashierAgent should have an event log after the scheduler is called. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("In action payMarket, paying market mockmarket1"));
		assertEquals("The money Cashier holds should be 100-14.99 = 85.01 right now. Instead it's "+ cashier.getMyMoney(),
				cashier.getMyMoney(), 85.01);
		assertEquals("this bill should be removed from marketBills list right now. It's not.", cashier.getMarketBills().size(),0);

		//check if the right message has been sent out.
		assertTrue("market1 should have received the message from the cashier, but the event log reads: " + 
				market1.log.toString(), market1.log.containsString("Received msgPayMarketBill "
						+ "from Cashier cashier and the amount is 14.99"));

		//we then have the mockcustomer send message to pay for his bill
		cashier.msgPayMyCheck(customer1, 16.00);
		assertTrue("CashierAgent should have received msgPayMyCheck from customer1. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("Received msgPayMyCheck from Customer mockcustomer1 and the amount is 16"));
		assertTrue("the check in the check list should have the state of BEING_PAID. It doesn't", 
				cashier.getChecks().get(0).getState() == CashierRole.checkState.BEING_PAID);
		//assertEquals("Cashier should have four permit for stateChange semaphore. It doesn't. Instead, it has " + 
		//		cashier.getStateChange().availablePermits(), cashier.getStateChange().availablePermits(),4);
		assertEquals("The money Cashier has should be 85.01+16 = 101.01 right now. It's not, and instead it's "+ cashier.getMyMoney(), 
				cashier.getMyMoney(), 101.01); 

		// call the scheduler
		cashier.pickAndExecuteAnAction();

		//cashier should be ready to giveChangeToCustomer(), check if the right action is being called
		assertTrue("CashierAgent should be giving change to customer1. It is not. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("In action giveChangeToCustomer, ready to give change to mockcustomer1"));	
		assertEquals("The map for customer balance should be updated. It is not.", cashier.getBalance().get(customer1),0.00);
		assertTrue("The check should have been removed from check list and the list should be empty. It is not.", 
				cashier.getChecks().isEmpty());
		assertEquals("The money Cashier has should be 101.01-0.01 = 101 right now. It's not, and instead it's "+ cashier.getMyMoney(), 
				cashier.getMyMoney(), 101.00); 

		// check if the right message has been sent to customer1
		assertTrue("customer1 should have received the message from the cashier with the change. The amount should be 0.01 but it's not",
				customer1.log.containsString("Received msgHereIsYourChange from cashier and the amount is " + (16.00-15.99)));

	}





	//Scenario: a waiter will send a bill to cashier to compute, and the same waiter will send another bill to cashier. Two customers
	//are involved in this scenario, one of them is able to pay full amount and the other one is not.
	public void testOneWaiterTwoCustomers() {
		//precondition tests
		assertTrue("Cashier should have empty checks list. It doesn't.",cashier.getChecks().isEmpty());
		assertTrue("CashierAgent should have an empty balance entry in balance list. It doesn't.", 
				cashier.getBalance().isEmpty());
		assertEquals("CashierAgent should have an empty event log before any message calls. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 0, cashier.log.size());
		//assertEquals("Cashier should have only one permit for stateChange semaphore. It doesn't. Instead, it has " + 
		//		cashier.getStateChange().availablePermits(), cashier.getStateChange().availablePermits(),1);

		//precondition on market front 
		assertEquals("Cashier should have 0 MarketBill in marketBills list. It doesn't.", cashier.getMarketBills().size(), 0);
		assertEquals("CashierAgent should have an empty event log before msgHereIsMarketBill is called. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 0, cashier.log.size());
		//assertEquals("Cashier should have only one permit for stateChange semaphore. It doesn't.", 
		//		cashier.getStateChange().availablePermits(),1);

		//received message from waiter
		cashier.msgHereIsBill(customer1, "Steak", waiter1);
		assertTrue("CashierAgent should have received msgHereIsBill from mockwaiter1. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("Received msgHereIsBill from Waiter mockwaiter1 with Customer mockcustomer1 and food Steak"));
		assertEquals("Cashier should have 1 element in checks list. It doesn't.", cashier.getChecks().size(), 1);
		assertTrue("the newly added check in the check list should have the state of COMPUTED. It doesn't", 
				cashier.getChecks().get(0).getState() == CashierRole.checkState.COMPUTED);
		assertTrue("customer1 should be contained in the balance map as a key. It doesn't.", 
				cashier.getBalance().containsKey(customer1));
		assertEquals("Balance for the customer should be 15.99. It doesn't. Instead, the balance for customer1"
				+ " is " + cashier.getBalance().get(customer1), cashier.getBalance().get(customer1), 15.99);
		//assertEquals("Cashier should have two permit for stateChange semaphore. It doesn't. Instead, it has " + 
		//		cashier.getStateChange().availablePermits(), cashier.getStateChange().availablePermits(),2);

		//received message from market
		cashier.msgHereIsMarketBill(market1, 14.99);
		assertTrue("CashierAgent should have an event log after the msgHereIsMarketBill is called. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("Received msgHereIsMarketBill from Market mockmarket1 and the amount is 14.99"));
		assertEquals("Cashier should have 1 MarketBill in marketBills list. It doesn't.", 
				cashier.getMarketBills().size(), 1);
		//assertEquals("Cashier should have 3 permits for stateChange semaphore. It doesn't.", 
		//		cashier.getStateChange().availablePermits(), 3);


		//received another message from the same waiter but different customer is involved
		cashier.msgHereIsBill(customer2, "Pizza", waiter1);
		assertTrue("CashierAgent should have received msgHereIsBill from mockwaiter1. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("Received msgHereIsBill from Waiter mockwaiter1 with Customer mockcustomer2 and food Pizza"));
		assertEquals("Cashier should have 2 elements in checks list. It doesn't.", cashier.getChecks().size(), 2);
		assertTrue("the newly added check in the check list should have the state of COMPUTED. It doesn't", 
				cashier.getChecks().get(1).getState() == CashierRole.checkState.COMPUTED);
		assertTrue("customer1 should be contained in the balance map as a key. It doesn't.", 
				cashier.getBalance().containsKey(customer2));
		assertEquals("Balance for the customer2 should be 8.99. It doesn't. Instead, the balance for customer2"
				+ " is " + cashier.getBalance().get(customer2), cashier.getBalance().get(customer2), 8.99);
		//assertEquals("Cashier should have four permit for stateChange semaphore. It doesn't. Instead, it has " + 
		//		cashier.getStateChange().availablePermits(), cashier.getStateChange().availablePermits(),4);

		//run the scheduler
		cashier.pickAndExecuteAnAction();

		//cashier should be ready to send the check for customer1 to waiter1
		//check if the right action has been executed
		assertTrue("CashierAgent should be sending check to waiter1. It doesn't. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("In action sendCheckToWaiter, ready to send Check to mockwaiter1. Customer mockcustomer1"
						+ " needs to pay 15.99"));		
		assertTrue("the check in the check list should have the state of SENT_TO_WAITER. It doesn't", 
				cashier.getChecks().get(0).getState() == CashierRole.checkState.SENT_TO_WAITER);

		//check if the right message has been sent to waiter1
		assertTrue("waiter1 should have received the message from the cashier with the computed check, but the event log reads: " + 
				waiter1.log.toString(), waiter1.log.containsString("Received message msgHereIsCheck from cashier. Customer mockcustomer1 "
						+ "needs to pay 15.99"));

		//run the scheduler again
		cashier.pickAndExecuteAnAction();

		//cashier should be ready to send the check for customer2 to waiter1
		//check if the right action has been executed
		assertTrue("CashierAgent should be sending check to waiter1. It doesn't. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("In action sendCheckToWaiter, ready to send Check to mockwaiter1. Customer mockcustomer2"
						+ " needs to pay 8.99"));		
		assertTrue("the check in the check list should have the state of SENT_TO_WAITER. It doesn't", 
				cashier.getChecks().get(0).getState() == CashierRole.checkState.SENT_TO_WAITER);

		//check if the right message has been sent to waiter1
		assertTrue("waiter1 should have received the message from the cashier with the computed check, but the event log reads: " + 
				waiter1.log.toString(), waiter1.log.containsString("Received message msgHereIsCheck from cashier. Customer mockcustomer2 "
						+ "needs to pay 8.99"));

		//we then have the mockcustomer1 send message to pay for his bill
		cashier.msgPayMyCheck(customer1, 20.00);
		assertTrue("CashierAgent should have received msgPayMyCheck from customer1. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("Received msgPayMyCheck from Customer mockcustomer1 and the amount is 20"));
		assertTrue("the check in the check list should have the state of BEING_PAID. It doesn't", 
				cashier.getChecks().get(0).getState() == CashierRole.checkState.BEING_PAID);
		//assertEquals("Cashier should have five permit for stateChange semaphore. It doesn't. Instead, it has " + 
		//		cashier.getStateChange().availablePermits(), cashier.getStateChange().availablePermits(),5);
		assertEquals("The money Cashier has should be 100+20 = 120 right now. It's not, and instead it's "+ cashier.getMyMoney(), 
				cashier.getMyMoney(), 100+20.00); 

		//run the scheduler
		cashier.pickAndExecuteAnAction();

		//cashier should be ready to give change to customer1, check if the right action call is made
		assertTrue("CashierAgent should be giving change to customer1. It is not. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("In action giveChangeToCustomer, ready to give change to mockcustomer1"));	
		assertEquals("The map for customer balance should be updated. It is not.", cashier.getBalance().get(customer1),0.00);
		assertTrue("The check should have been removed from check list and the list should have size 1. It is not.", 
				cashier.getChecks().size() ==1);
		assertEquals("The money Cashier has should be 115.99 right now. It's not, and instead it's "+ cashier.getMyMoney(), 
				cashier.getMyMoney(), 115.99); 

		// check if the right message has been sent to customer1
		assertTrue("customer1 should have received the message from the cashier with the change. The amount should be 4.01 but it's not",
				customer1.log.containsString("Received msgHereIsYourChange from cashier and the amount is " + (20.00-15.99)));

		//we directly call the scheduler
		cashier.pickAndExecuteAnAction();

		//cashier should be paying the market bill right now
		assertTrue("CashierAgent should have an event log after the scheduler is called. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("In action payMarket, paying market mockmarket1"));
		assertEquals("The money Cashier holds should be 115.99-14.99 = 101 right now. Instead it's "+ cashier.getMyMoney(),
				cashier.getMyMoney(), 101.00);
		assertEquals("this bill should be removed from marketBills list right now. It's not.", cashier.getMarketBills().size(),0);

		//check if the right message has been sent out.
		assertTrue("market1 should have received the message from the cashier, but the event log reads: " + 
				market1.log.toString(), market1.log.containsString("Received msgPayMarketBill "
						+ "from Cashier cashier and the amount is 14.99"));

		//we then have the mockcustomer2 send message to pay for his bill
		cashier.msgPayMyCheck(customer2, 5.00);
		assertTrue("CashierAgent should have received msgPayMyCheck from customer2. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("Received msgPayMyCheck from Customer mockcustomer2 and the amount is 5.0"));
		assertTrue("the check in the check list should have the state of BEING_PAID. It doesn't", 
				cashier.getChecks().get(0).getState() == CashierRole.checkState.BEING_PAID);
		//assertEquals("Cashier should have six permit for stateChange semaphore. It doesn't. Instead, it has " + 
		//		cashier.getStateChange().availablePermits(), cashier.getStateChange().availablePermits(),6);
		assertEquals("The money Cashier has should be 101+5.00 = 106.00 right now. It's not, and instead it's "+ 
				cashier.getMyMoney(), cashier.getMyMoney(), 106.00); 

		//run the scheduler
		cashier.pickAndExecuteAnAction();
		//cashier should be ready to give change to customer2, check if the right action call is made
		assertTrue("CashierAgent should be giving change to customer2. It is not. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("In action giveChangeToCustomer, ready to give change to mockcustomer2"));	
		assertEquals("The map for customer balance should be updated. It is not.", cashier.getBalance().get(customer2),3.99);
		assertTrue("The check should have been removed from check list and the list should be empty. It is not.", 
				cashier.getChecks().isEmpty());
		assertEquals("The money Cashier has should be 106.00 right now. It's not, and instead it's "+ cashier.getMyMoney(), 
				cashier.getMyMoney(), 106.0); 

		// check if the right message has been sent to customer2
		assertTrue("customer2 should have received the message from the cashier with the change. The amount should be 0 but it's not",
				customer2.log.containsString("Received msgHereIsYourChange from cashier and the amount is 0.0"));

	}






	//Scenario: a waiter will send a bill to cashier to compute, and a different waiter will send another bill to cashier. Two customers
	//are involved in this scenario, one of them is able to pay full amount and the other one is not.
	public void testTwoWaitersTwoCustomers() {
		//precondition tests
		assertTrue("Cashier should have empty checks list. It doesn't.",cashier.getChecks().isEmpty());
		assertTrue("CashierAgent should have an empty balance entry in balance list. It doesn't.", 
				cashier.getBalance().isEmpty());
		assertEquals("CashierAgent should have an empty event log before any message calls. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 0, cashier.log.size());
		//assertEquals("Cashier should have only one permit for stateChange semaphore. It doesn't. Instead, it has " + 
		//		cashier.getStateChange().availablePermits(), cashier.getStateChange().availablePermits(),1);

		//received message from waiter
		cashier.msgHereIsBill(customer1, "Steak", waiter1);
		assertTrue("CashierAgent should have received msgHereIsBill from mockwaiter1. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("Received msgHereIsBill from Waiter mockwaiter1 with Customer mockcustomer1 and food Steak"));
		assertEquals("Cashier should have 1 element in checks list. It doesn't.", cashier.getChecks().size(), 1);
		assertTrue("the newly added check in the check list should have the state of COMPUTED. It doesn't", 
				cashier.getChecks().get(0).getState() == CashierRole.checkState.COMPUTED);
		assertTrue("customer1 should be contained in the balance map as a key. It doesn't.", 
				cashier.getBalance().containsKey(customer1));
		assertEquals("Balance for the customer should be 15.99. It doesn't. Instead, the balance for customer1"
				+ " is " + cashier.getBalance().get(customer1), cashier.getBalance().get(customer1), 15.99);
		//assertEquals("Cashier should have two permit for stateChange semaphore. It doesn't. Instead, it has " + 
		//		cashier.getStateChange().availablePermits(), cashier.getStateChange().availablePermits(),2);


		//precondition for market 
		assertEquals("Cashier should have 0 MarketBill in marketBills list. It doesn't.", cashier.getMarketBills().size(), 0);
		//assertEquals("CashierAgent should have an empty event log before msgHereIsMarketBill is called. "
		//		+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 0, cashier.log.size());
		//assertEquals("Cashier should have only two permit for stateChange semaphore. It doesn't.", 
		//		cashier.getStateChange().availablePermits(),2);



		//received another message from another waiter and involve a different customer
		cashier.msgHereIsBill(customer2, "Pizza", waiter2);
		assertTrue("CashierAgent should have received msgHereIsBill from mockwaiter2. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("Received msgHereIsBill from Waiter mockwaiter2 with Customer mockcustomer2 and food Pizza"));
		assertEquals("Cashier should have 2 elements in checks list. It doesn't.", cashier.getChecks().size(), 2);
		assertTrue("the newly added check in the check list should have the state of COMPUTED. It doesn't", 
				cashier.getChecks().get(1).getState() == CashierRole.checkState.COMPUTED);
		assertTrue("customer1 should be contained in the balance map as a key. It doesn't.", 
				cashier.getBalance().containsKey(customer2));
		assertEquals("Balance for the customer2 should be 8.99. It doesn't. Instead, the balance for customer2"
				+ " is " + cashier.getBalance().get(customer2), cashier.getBalance().get(customer2), 8.99);
		//assertEquals("Cashier should have three permit for stateChange semaphore. It doesn't. Instead, it has " + 
		//		cashier.getStateChange().availablePermits(), cashier.getStateChange().availablePermits(),3);

		//run the scheduler
		cashier.pickAndExecuteAnAction();

		//cashier should be ready to send the check for customer1 to waiter1
		//check if the right action has been executed
		assertTrue("CashierAgent should be sending check to waiter1. It doesn't. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("In action sendCheckToWaiter, ready to send Check to mockwaiter1. Customer mockcustomer1"
						+ " needs to pay 15.99"));		
		assertTrue("the check in the check list should have the state of SENT_TO_WAITER. It doesn't", 
				cashier.getChecks().get(0).getState() == CashierRole.checkState.SENT_TO_WAITER);

		//check if the right message has been sent to waiter1
		assertTrue("waiter1 should have received the message from the cashier with the computed check, but the event log reads: " + 
				waiter1.log.toString(), waiter1.log.containsString("Received message msgHereIsCheck from cashier. Customer mockcustomer1 "
						+ "needs to pay 15.99"));

		//run the scheduler again
		cashier.pickAndExecuteAnAction();

		//cashier should be ready to send the check for customer2 to waiter2
		//check if the right action has been executed
		assertTrue("CashierAgent should be sending check to waiter2. It doesn't. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("In action sendCheckToWaiter, ready to send Check to mockwaiter2. Customer mockcustomer2"
						+ " needs to pay 8.99"));		
		assertTrue("the check in the check list should have the state of SENT_TO_WAITER. It doesn't", 
				cashier.getChecks().get(0).getState() == CashierRole.checkState.SENT_TO_WAITER);

		//check if the right message has been sent to waiter1
		assertTrue("waiter2 should have received the message from the cashier with the computed check, but the event log reads: " + 
				waiter2.log.toString(), waiter2.log.containsString("Received message msgHereIsCheck from cashier. Customer mockcustomer2 "
						+ "needs to pay 8.99"));

		//have market send us a bill
		cashier.msgHereIsMarketBill(market1, 14.99);
		assertTrue("CashierAgent should have an event log after the msgHereIsMarketBill is called. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("Received msgHereIsMarketBill from Market mockmarket1 and the amount is 14.99"));
		assertEquals("Cashier should have 1 MarketBill in marketBills list. It doesn't.", 
				cashier.getMarketBills().size(), 1);
		//assertEquals("Cashier should have four permit for stateChange semaphore. It doesn't.", 
		//		cashier.getStateChange().availablePermits(), 4);

		//we then have the mockcustomer1 send message to pay for his bill
		cashier.msgPayMyCheck(customer1, 20.00);
		assertTrue("CashierAgent should have received msgPayMyCheck from customer1. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("Received msgPayMyCheck from Customer mockcustomer1 and the amount is 20"));
		assertTrue("the check in the check list should have the state of BEING_PAID. It doesn't", 
				cashier.getChecks().get(0).getState() == CashierRole.checkState.BEING_PAID);
		//assertEquals("Cashier should have five permit for stateChange semaphore. It doesn't. Instead, it has " + 
		//		cashier.getStateChange().availablePermits(), cashier.getStateChange().availablePermits(),5);
		assertEquals("The money Cashier has should be 100+20 = 120 right now. It's not, and instead it's "+ cashier.getMyMoney(), 
				cashier.getMyMoney(), 100+20.00); 

		//run the scheduler
		cashier.pickAndExecuteAnAction();

		//cashier should be ready to give change to customer1, check if the right action call is made
		assertTrue("CashierAgent should be giving change to customer1. It is not. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("In action giveChangeToCustomer, ready to give change to mockcustomer1"));	
		assertEquals("The map for customer balance should be updated. It is not.", cashier.getBalance().get(customer1),0.00);
		assertTrue("The check should have been removed from check list and the list should have size 1. It is not.", 
				cashier.getChecks().size() ==1);
		assertEquals("The money Cashier has should be 115.99 right now. It's not, and instead it's "+ cashier.getMyMoney(), 
				cashier.getMyMoney(), 115.99); 

		// check if the right message has been sent to customer1
		assertTrue("customer1 should have received the message from the cashier with the change. The amount should be 4.01 but it's not",
				customer1.log.containsString("Received msgHereIsYourChange from cashier and the amount is " + (20.00-15.99)));

		//call the scheduler
		cashier.pickAndExecuteAnAction();

		//check if the right action is called
		assertTrue("CashierAgent should have an event log after the scheduler is called. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("In action payMarket, paying market mockmarket1"));
		assertEquals("The money Cashier holds should be 115.99-14.99 = 101 right now. Instead it's "+ cashier.getMyMoney(),
				cashier.getMyMoney(), 101.0);
		assertEquals("this bill should be removed from marketBills list right now. It's not.", cashier.getMarketBills().size(),0);

		//check if the right message has been sent out.
		assertTrue("market1 should have received the message from the cashier, but the event log reads: " + 
				market1.log.toString(), market1.log.containsString("Received msgPayMarketBill "
						+ "from Cashier cashier and the amount is 14.99"));



		//we then have the mockcustomer2 send message to pay for his bill
		cashier.msgPayMyCheck(customer2, 5.00);
		assertTrue("CashierAgent should have received msgPayMyCheck from customer2. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("Received msgPayMyCheck from Customer mockcustomer2 and the amount is 5.0"));
		assertTrue("the check in the check list should have the state of BEING_PAID. It doesn't", 
				cashier.getChecks().get(0).getState() == CashierRole.checkState.BEING_PAID);
		//assertEquals("Cashier should have six permit for stateChange semaphore. It doesn't. Instead, it has " + 
		//		cashier.getStateChange().availablePermits(), cashier.getStateChange().availablePermits(),6);
		assertEquals("The money Cashier has should be 101.00+5.00 = 106.0 right now. It's not, and instead it's "+ 
				cashier.getMyMoney(), cashier.getMyMoney(), 106.00); 

		//run the scheduler
		cashier.pickAndExecuteAnAction();
		//cashier should be ready to give change to customer2, check if the right action call is made
		assertTrue("CashierAgent should be giving change to customer2. It is not. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("In action giveChangeToCustomer, ready to give change to mockcustomer2"));	
		assertEquals("The map for customer balance should be updated. It is not.", cashier.getBalance().get(customer2),3.99);
		assertTrue("The check should have been removed from check list and the list should be empty. It is not.", 
				cashier.getChecks().isEmpty());
		assertEquals("The money Cashier has should be 106.00 right now. It's not, and instead it's "+ cashier.getMyMoney(), 
				cashier.getMyMoney(), 106.00); 

		// check if the right message has been sent to customer2
		assertTrue("customer2 should have received the message from the cashier with the change. The amount should be 0 but it's not",
				customer2.log.containsString("Received msgHereIsYourChange from cashier and the amount is 0.0"));
	}






	//Scenario: a waiter will send a bill to cashier to compute, and a different waiter will send another bill to cashier. Two customers
	//are involved so far. And then we have waiter1 send another bill involving customer2 who is not able to 
	//pay the full bill for the first time and but now is able to

	public void testTwoWaitersThreeCustomers() {
		//precondition tests
		assertTrue("Cashier should have empty checks list. It doesn't.",cashier.getChecks().isEmpty());
		assertTrue("CashierAgent should have an empty balance entry in balance list. It doesn't.", 
				cashier.getBalance().isEmpty());
		assertEquals("CashierAgent should have an empty event log before any message calls. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 0, cashier.log.size());
		//assertEquals("Cashier should have only one permit for stateChange semaphore. It doesn't. Instead, it has " + 
		//		cashier.getStateChange().availablePermits(), cashier.getStateChange().availablePermits(),1);

		//received message from waiter
		cashier.msgHereIsBill(customer1, "Steak", waiter1);
		assertTrue("CashierAgent should have received msgHereIsBill from mockwaiter1. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("Received msgHereIsBill from Waiter mockwaiter1 with Customer mockcustomer1 and food Steak"));
		assertEquals("Cashier should have 1 element in checks list. It doesn't.", cashier.getChecks().size(), 1);
		assertTrue("the newly added check in the check list should have the state of COMPUTED. It doesn't", 
				cashier.getChecks().get(0).getState() == CashierRole.checkState.COMPUTED);
		assertTrue("customer1 should be contained in the balance map as a key. It doesn't.", 
				cashier.getBalance().containsKey(customer1));
		assertEquals("Balance for the customer should be 15.99. It doesn't. Instead, the balance for customer1"
				+ " is " + cashier.getBalance().get(customer1), cashier.getBalance().get(customer1), 15.99);
		//assertEquals("Cashier should have two permit for stateChange semaphore. It doesn't. Instead, it has " + 
		//		cashier.getStateChange().availablePermits(), cashier.getStateChange().availablePermits(),2);

		//received another message from another waiter and involve a different customer
		cashier.msgHereIsBill(customer2, "Pizza", waiter2);
		assertTrue("CashierAgent should have received msgHereIsBill from mockwaiter2. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("Received msgHereIsBill from Waiter mockwaiter2 with Customer mockcustomer2 and food Pizza"));
		assertEquals("Cashier should have 2 elements in checks list. It doesn't.", cashier.getChecks().size(), 2);
		assertTrue("the newly added check in the check list should have the state of COMPUTED. It doesn't", 
				cashier.getChecks().get(1).getState() == CashierRole.checkState.COMPUTED);
		assertTrue("customer1 should be contained in the balance map as a key. It doesn't.", 
				cashier.getBalance().containsKey(customer2));
		assertEquals("Balance for the customer2 should be 8.99. It doesn't. Instead, the balance for customer2"
				+ " is " + cashier.getBalance().get(customer2), cashier.getBalance().get(customer2), 8.99);
		//assertEquals("Cashier should have three permit for stateChange semaphore. It doesn't. Instead, it has " + 
			//	cashier.getStateChange().availablePermits(), cashier.getStateChange().availablePermits(),3);

		//run the scheduler
		cashier.pickAndExecuteAnAction();

		//cashier should be ready to send the check for customer1 to waiter1
		//check if the right action has been executed
		assertTrue("CashierAgent should be sending check to waiter1. It doesn't. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("In action sendCheckToWaiter, ready to send Check to mockwaiter1. Customer mockcustomer1"
						+ " needs to pay 15.99"));		
		assertTrue("the check in the check list should have the state of SENT_TO_WAITER. It doesn't", 
				cashier.getChecks().get(0).getState() == CashierRole.checkState.SENT_TO_WAITER);

		//check if the right message has been sent to waiter1
		assertTrue("waiter1 should have received the message from the cashier with the computed check, but the event log reads: " + 
				waiter1.log.toString(), waiter1.log.containsString("Received message msgHereIsCheck from cashier. Customer mockcustomer1 "
						+ "needs to pay 15.99"));

		//run the scheduler again
		cashier.pickAndExecuteAnAction();

		//cashier should be ready to send the check for customer2 to waiter2
		//check if the right action has been executed
		assertTrue("CashierAgent should be sending check to waiter2. It doesn't. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("In action sendCheckToWaiter, ready to send Check to mockwaiter2. Customer mockcustomer2"
						+ " needs to pay 8.99"));		
		assertTrue("the check in the check list should have the state of SENT_TO_WAITER. It doesn't", 
				cashier.getChecks().get(0).getState() == CashierRole.checkState.SENT_TO_WAITER);

		//check if the right message has been sent to waiter1
		assertTrue("waiter2 should have received the message from the cashier with the computed check, but the event log reads: " + 
				waiter2.log.toString(), waiter2.log.containsString("Received message msgHereIsCheck from cashier. Customer mockcustomer2 "
						+ "needs to pay 8.99"));

		// preconditions for market front
		assertEquals("Cashier should have 0 MarketBill in marketBills list. It doesn't.", cashier.getMarketBills().size(), 0);

		//assertEquals("Cashier should have three permit for stateChange semaphore. It doesn't.", 
		//		cashier.getStateChange().availablePermits(),3);

		//got message from market
		cashier.msgHereIsMarketBill(market1, 14.99);
		assertTrue("CashierAgent should have an event log after the msgHereIsMarketBill is called. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("Received msgHereIsMarketBill from Market mockmarket1 and the amount is 14.99"));
		assertEquals("Cashier should have 1 MarketBill in marketBills list. It doesn't.", 
				cashier.getMarketBills().size(), 1);
		//assertEquals("Cashier should have four permits for stateChange semaphore. It doesn't.", 
		//		cashier.getStateChange().availablePermits(), 4);

		//we directly call the scheduler
		cashier.pickAndExecuteAnAction();

		//check if the cashier is paying market bill
		assertTrue("CashierAgent should have an event log after the scheduler is called. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("In action payMarket, paying market mockmarket1"));
		assertEquals("The money Cashier holds should be 100-14.99 = 85.01 right now. Instead it's "+ cashier.getMyMoney(),
				cashier.getMyMoney(), 85.01);
		assertEquals("this bill should be removed from marketBills list right now. It's not.", cashier.getMarketBills().size(),0);

		//check if the right message has been sent out.
		assertTrue("market1 should have received the message from the cashier, but the event log reads: " + 
				market1.log.toString(), market1.log.containsString("Received msgPayMarketBill "
						+ "from Cashier cashier and the amount is 14.99"));

		//we then have the mockcustomer1 send message to pay for his bill
		cashier.msgPayMyCheck(customer1, 20.00);
		assertTrue("CashierAgent should have received msgPayMyCheck from customer1. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("Received msgPayMyCheck from Customer mockcustomer1 and the amount is 20"));
		assertTrue("the check in the check list should have the state of BEING_PAID. It doesn't", 
				cashier.getChecks().get(0).getState() == CashierRole.checkState.BEING_PAID);
		//assertEquals("Cashier should have five permit for stateChange semaphore. It doesn't. Instead, it has " + 
		//		cashier.getStateChange().availablePermits(), cashier.getStateChange().availablePermits(),5);
		assertEquals("The money Cashier has should be 85.01+20 = 105.01 right now. It's not, and instead it's "+ cashier.getMyMoney(), 
				cashier.getMyMoney(), 105.01); 

		//run the scheduler
		cashier.pickAndExecuteAnAction();

		//cashier should be ready to give change to customer1, check if the right action call is made
		assertTrue("CashierAgent should be giving change to customer1. It is not. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("In action giveChangeToCustomer, ready to give change to mockcustomer1"));	
		assertEquals("The map for customer balance should be updated. It is not.", cashier.getBalance().get(customer1),0.00);
		assertTrue("The check should have been removed from check list and the list should have size 1. It is not.", 
				cashier.getChecks().size() ==1);
		assertEquals("The money Cashier has should be 101.00 right now. It's not, and instead it's "+ cashier.getMyMoney(), 
				cashier.getMyMoney(), 101.0); 

		// check if the right message has been sent to customer1
		assertTrue("customer1 should have received the message from the cashier with the change. The amount should be 4.01 but it's not",
				customer1.log.containsString("Received msgHereIsYourChange from cashier and the amount is " + (20.00-15.99)));

		//we then have the mockcustomer2 send message to pay for his bill
		cashier.msgPayMyCheck(customer2, 5.00);
		assertTrue("CashierAgent should have received msgPayMyCheck from customer2. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("Received msgPayMyCheck from Customer mockcustomer2 and the amount is 5.0"));
		assertTrue("the check in the check list should have the state of BEING_PAID. It doesn't", 
				cashier.getChecks().get(0).getState() == CashierRole.checkState.BEING_PAID);
		//assertEquals("Cashier should have six permits for stateChange semaphore. It doesn't. Instead, it has " + 
		//		cashier.getStateChange().availablePermits(), cashier.getStateChange().availablePermits(),6);
		assertEquals("The money Cashier has should be 101.0+5.00 = 106.00 right now. It's not, and instead it's "+ 
				cashier.getMyMoney(), cashier.getMyMoney(), 106.0); 

		//run the scheduler
		cashier.pickAndExecuteAnAction();
		//cashier should be ready to give change to customer2, check if the right action call is made
		assertTrue("CashierAgent should be giving change to customer2. It is not. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("In action giveChangeToCustomer, ready to give change to mockcustomer2"));	
		assertEquals("The map for customer balance should be updated. It is not.", cashier.getBalance().get(customer2),3.99);
		assertTrue("The check should have been removed from check list and the list should be empty. It is not.", 
				cashier.getChecks().isEmpty());
		assertEquals("The money Cashier has should be 106.0 right now. It's not, and instead it's "+ cashier.getMyMoney(), 
				cashier.getMyMoney(), 106.0); 

		// check if the right message has been sent to customer2
		assertTrue("customer2 should have received the message from the cashier with the change. The amount should be 0 but it's not",
				customer2.log.containsString("Received msgHereIsYourChange from cashier and the amount is 0.0"));

		//received another message from another waiter and involve a different customer
		cashier.msgHereIsBill(customer2, "Steak", waiter2);
		assertTrue("CashierAgent should have received msgHereIsBill from mockwaiter2. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("Received msgHereIsBill from Waiter mockwaiter2 with Customer mockcustomer2 and food Steak"));
		assertEquals("Cashier should have 1 element in checks list. It doesn't.", cashier.getChecks().size(), 1);
		assertTrue("the newly added check in the check list should have the state of COMPUTED. It doesn't", 
				cashier.getChecks().get(0).getState() == CashierRole.checkState.COMPUTED);
		assertTrue("customer1 should be contained in the balance map as a key. It doesn't.", 
				cashier.getBalance().containsKey(customer2));
		assertEquals("Balance for the customer2 should be 3.99+15.99 = 19.98. It doesn't. Instead, the balance for customer2"
				+ " is " + cashier.getBalance().get(customer2), cashier.getBalance().get(customer2), 19.98);
		//assertEquals("Cashier should have seven permit for stateChange semaphore. It doesn't. Instead, it has " + 
		//		cashier.getStateChange().availablePermits(), cashier.getStateChange().availablePermits(),7);

		//run the scheduler
		cashier.pickAndExecuteAnAction();

		//cashier should be ready to send the check for customer2 to waiter2
		//check if the right action has been executed
		assertTrue("CashierAgent should be sending check to waiter2. It doesn't. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("In action sendCheckToWaiter, ready to send Check to mockwaiter2. Customer mockcustomer2"
						+ " needs to pay 19.98"));		
		assertTrue("the check in the check list should have the state of SENT_TO_WAITER. It doesn't", 
				cashier.getChecks().get(0).getState() == CashierRole.checkState.SENT_TO_WAITER);

		//check if the right message has been sent to waiter1
		assertTrue("waiter2 should have received the message from the cashier with the computed check, but the event log reads: " + 
				waiter2.log.toString(), waiter2.log.containsString("Received message msgHereIsCheck from cashier. Customer mockcustomer2 "
						+ "needs to pay 19.98"));

		//we then have the mockcustomer2 send message to pay for his bill, and this time he's able to pay his bill and previous balance as well
		cashier.msgPayMyCheck(customer2, 25.00);
		assertTrue("CashierAgent should have received msgPayMyCheck from customer2. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("Received msgPayMyCheck from Customer mockcustomer2 and the amount is 25.0"));
		assertTrue("the check in the check list should have the state of BEING_PAID. It doesn't", 
				cashier.getChecks().get(0).getState() == CashierRole.checkState.BEING_PAID);
		//assertEquals("Cashier should have eight permit for stateChange semaphore. It doesn't. Instead, it has " + 
		//		cashier.getStateChange().availablePermits(), cashier.getStateChange().availablePermits(),8);
		assertEquals("The money Cashier has should be 106+25 = 131 right now. It's not, and instead it's "+ 
				cashier.getMyMoney(), cashier.getMyMoney(), 131.0); 

		//run the scheduler
		cashier.pickAndExecuteAnAction();
		//cashier should be ready to give change to customer2, check if the right action call is made
		assertTrue("CashierAgent should be giving change to customer2. It is not. "
				+ "Instead, the Cashier's event log reads: " +cashier.log.toString(), 
				cashier.log.containsString("In action giveChangeToCustomer, ready to give change to mockcustomer2"));	
		assertEquals("The map for customer balance should be updated. It is not.", cashier.getBalance().get(customer2),0.00);
		assertTrue("The check should have been removed from check list and the list should be empty. It is not.", 
				cashier.getChecks().isEmpty());
		assertEquals("The money Cashier has should be 131-5.02=125.98 right now. It's not, and instead it's "+ cashier.getMyMoney(), 
				cashier.getMyMoney(), 125.98); 

		// check if the right message has been sent to customer2
		assertTrue("customer2 should have received the message from the cashier with the change. The amount should be 0 but it's not",
				customer2.log.containsString("Received msgHereIsYourChange from cashier and the amount is 5.02"));
	}
}
