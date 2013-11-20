package transportation;

import static org.junit.Assert.*;
import transportation.CarPassengerRole.State;
import junit.framework.TestCase;

import org.junit.Test;

import people.PeopleAgent;

public class CarPassengerRoleTest extends TestCase{

	CarPassengerRole cpr = new CarPassengerRole();
	PeopleAgent p = new PeopleAgent();
	Car c = new Car();
	CarGui cg = new CarGui();
	public void setUp() throws Exception{
		super.setUp();
		cpr.setPersonAgent(p);
		c.setGui(cg);
		cpr.setCar(c);
	}
	
	public void testingCaseWithCarAndOnePassenger() {
		
		cpr.msgIsActive();
		assertEquals("CarPassengerRole's state should be readyToLeave but its not", State.readyToLeave, cpr.myState);
		cpr.destination = "Bank";
		assertTrue("CarPassengerRole's scheduler should return true to react to the state change but it didn't",cpr.pickAndExecuteAnAction());
		assertEquals("Car's list of passengers should now be one, but its not",1,c.myCarPassengers.size());
		assertTrue("Car's scheduler should return true to react to the new passenger, but it didn't",c.pickAndExecuteAnAction());
		assertTrue("Car's scheduler should return true to tell passenger the it arrived, but it didn't",c.pickAndExecuteAnAction());
		assertTrue("CarPassengerRole's scheduler should return true to react to the car arriving, but it didnt",cpr.pickAndExecuteAnAction());
		assertEquals("Car's list of passengers should now be zero, but it's not",0,c.myCarPassengers.size());
	}
	
	
}
