package market.test;

import market.MarketCashierRole;
import market.interfaces.MarketCashier;
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
		mmc = new MockMarketCustomer("mmc");
		mcook = new MockCook("mcook");
		mcashier = new MockCashier("mcashier");
	}
	
	public void testPeopleMessage (){
		//check pre-condition
		cashier.msgIsActive();
		
		
	}
	
}
