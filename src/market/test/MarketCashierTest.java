package market.test;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Timer;

import market.MarketCashierRole;
import market.gui.MarketGui;
import people.PeopleAgent;
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
	Timer timer;
	MarketGui gui;
	
	
	public static void main(String args[]) {
		junit.textui.TestRunner.run(suite());
	}

	public static TestSuite suite ( ) {
		return new TestSuite(MarketCashierTest.class);
	}

	public void setUp() throws Exception{
		super.setUp();  
		timer = new Timer(20,null);
		gui = new MarketGui(timer);
		people = new PeopleAgent("people", 0.0, false);
		cashier = new MarketCashierRole(gui);
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
		assertTrue(cashier.log.containsString("clock in"));
		assertFalse(cashier.turnActive);
		
		cashier.msgIsInActive();
		cashier.pickAndExecuteAnAction();
		assertTrue(cashier.log.containsString("in action done"));
		assertFalse(cashier.isActive());
		assertFalse(cashier.leaveWork);
	}
	
	
	public void testOneCustomer (){
		cashier.msgIsActive();
		cashier.pickAndExecuteAnAction();
		
		assertEquals(cashier.checks.size(),0);
		cashier.msgHereIsACheck(mmc, items);
		assertEquals(cashier.checks.size(),1);
		
		cashier.pickAndExecuteAnAction();
		assertTrue("cahsier should be sending a check to customer but it's not. log reads " + cashier.log.toString(),
				cashier.log.containsString("sending check to customer and total due is 20000.0"));
		assertEquals(cashier.checks.get(0).totalDue, 20000.0);
		
		//check if message is sent to mock customer
		assertTrue(mmc.log.containsString("received msgHereIsWhatIsDue from market cashier"));
		
		//make customer send message
		assertEquals(cashier.working_capital,10000.0);
		cashier.msgHereIsPayment(mmc, 100000.0);
		cashier.pickAndExecuteAnAction();
		assertTrue("cashier log reads: " + cashier.log.toString(),
				cashier.log.containsString("giving change to customer and the amount is 80000"));
		assertEquals(cashier.working_capital,30000.0);
	}
		
	
	
}
