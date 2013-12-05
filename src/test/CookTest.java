package test;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CookTest extends TestCase{
	public static void main(String args[]) {
		junit.textui.TestRunner.run(suite());
	}

	public static TestSuite suite ( ) {
		return new TestSuite(CookTest.class);
	}

	public void setUp() throws Exception{
		super.setUp();  
	}
	
	public void test (){
		
	}

}
