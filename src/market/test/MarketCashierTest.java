package market.test;

import java.util.HashMap;
import java.util.Map;

import market.MarketCashierRole;
import market.interfaces.MarketCashier;
import market.interfaces.MarketCustomer;
import people.PeopleAgent;
import restaurant.test.mock.LoggedEvent;
import restaurant.test.mock.MockCashier;
import restaurant.test.mock.MockCook;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class MarketCashierTest extends TestCase{
	PeopleAgent people;
	MarketCashierRole cashier;
	MockMarketEmployee mme;
	MockMarketCustomer mmc;
	MockCook mcook;
	MockCashier mcashier ;
	Map<String, Integer> items = new HashMap<String, Integer>();
	
	
	public static void main(String args[]) {
		junit.textui.TestRunner.run(suite());
	}

	public static TestSuite suite ( ) {
		return new TestSuite(MarketCashierTest.class);
	}

	public void setUp() throws Exception{
		super.setUp();  
		people = new PeopleAgent("people", 0.0, false);
		cashier = new MarketCashierRole();
		people.addRole(cashier, "MarketCashierRole");
		cashier.setPerson(people);
		mme = new MockMarketEmployee("mme");
		cashier.setMarketEmployee(mme);

		mmc = new MockMarketCustomer("mmc");
		mcook = new MockCook("mcook");
		mcashier = new MockCashier("mcashier");
		items.put("Car", 1);
		cashier.inTest = true;
	}
	
	public void testPeopleMessage (){
		//check precondition
		assertFalse(cashier.isActive());
		cashier.msgIsActive();
		cashier.pickAndExecuteAnAction();
		assertTrue(cashier.log.containsString("in action clockIn"));
		assertFalse(cashier.turnActive);
		
		cashier.msgIsInActive();
		cashier.pickAndExecuteAnAction();
		assertTrue(cashier.log.containsString("in action done"));
		assertFalse(cashier.isActive());
		assertFalse(cashier.leaveWork);
	}
	
	
	public void testOneEmployeeOneCustomer (){
		cashier.msgIsActive();
		cashier.pickAndExecuteAnAction();
		
		assertEquals(cashier.checks.size(),0);
		cashier.msgHereIsACheck(mmc, items);
		assertEquals(cashier.checks.size(),1);
		
		cashier.pickAndExecuteAnAction();
		assertTrue(cashier.log.containsString("in action compute and send check"));
		assertEquals(cashier.checks.get(0).totalDue, 100000.0);
		
		//check if message is sent to mock customer
		assertTrue(mmc.log.containsString("received msgHereIsWhatIsDue from market cashier"));
		
		//make customer send message
		assertEquals(cashier.marketMoney,10000.0);
		cashier.msgHereIsPayment(mmc, 100050.0);
		cashier.pickAndExecuteAnAction();
		assertTrue(cashier.log.containsString("in action give change to customer"));
		assertEquals(cashier.marketMoney,110000.0);
		
		
	}
		
	
	
}
