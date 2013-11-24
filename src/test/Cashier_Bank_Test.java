package test;

import java.util.Vector;

import people.PeopleAgent;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import restaurant.BaseWaiterRole;
import restaurant.CashierRole;
import restaurant.NormalWaiterRole;
import restaurant.gui.RestaurantPanel;
import restaurant.interfaces.Waiter;
import restaurant.test.mock.MockTeller;

public class Cashier_Bank_Test extends TestCase{
	CashierRole cashier;
	/*MockWaiter waiter1;
	MockWaiter waiter2;
	MockCustomer customer1;
	MockCustomer customer2;
	MockMarket market1;
	MockMarket market2;
	enum checkState {COMPUTED, SENT_TO_WAITER, BEING_PAID};*/
	MockTeller teller;
	PeopleAgent person, person2,person3,person4,person5;
	BaseWaiterRole waiter1;
	BaseWaiterRole waiter2;
	Vector<Waiter> waiters;


	public static void main(String args[]) {
		junit.textui.TestRunner.run(suite());
	}

	public static TestSuite suite ( ) {
		return new TestSuite(Cashier_Bank_Test.class);
	}

	public void setUp() throws Exception{
		super.setUp();          
		
		waiter1 = new NormalWaiterRole("waiter1");
		waiter2 = new NormalWaiterRole("waiter2");
		
		cashier = new CashierRole("cashier");
		
		/*customer1 = new MockCustomer("mockcustomer1");  
		customer2 = new MockCustomer("mockcustomer2");
		waiter1 = new MockWaiter("mockwaiter1");
		waiter2 = new MockWaiter("mockwaiter2");
		market1 = new MockMarket("mockmarket1");
		market2 = new MockMarket("mockmarket2");*/

		teller = new MockTeller("teller");
	}      
	
	//scenario: make PeopleAgent a CashierRole, then Cashier will do the opening
	public void testPeopleAddNewCashierRole() {
		assertEquals(person.roles.size(),0);
		assertFalse(cashier.isActive());
		
		cashier.setPerson(person);
		person.addRole(cashier, "Cashier");	
		cashier.setTeller(teller);
		
		waiter1.setPerson(person2);
		waiter2.setPerson(person3);
		
		
		assertEquals(person.roles.size(),1);
		cashier.msgIsActive();
		assertTrue(cashier.isActive());
		
		//teller.msgCreateAccount("restaurant1", 100);
		cashier.onClose = true;
		//person.pickAndExecuteAnAction();
		cashier.pickAndExecuteAnAction();


		assertTrue(cashier.log.containsString("In action prepareToClose"));
		assertEquals(cashier.getMyMoney(),50.0);
		

		assertTrue(teller.log.containsString("received message from customer id 0. Customer wants to deposit 40.0"));

		cashier.msgDepositSuccessful(40.0);
		
		assertEquals(cashier.getMyMoney(),10.0);
		assertEquals(cashier.getBankBalance(),40.0);

		cashier.pickAndExecuteAnAction();
		assertTrue(cashier.log.containsString("In action closeRestaurant"));
		
		

	}
	
}
