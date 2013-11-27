package market.test;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import test.NewCashierTest;

public class MarketTruckTest extends TestCase{
	public static void main(String args[]) {
		junit.textui.TestRunner.run(suite());
	}

	public static TestSuite suite ( ) {
		return new TestSuite(NewCashierTest.class);
	}

	public void setUp() throws Exception{
		super.setUp();  
	}
	
	public void test (){
		
	}

}
