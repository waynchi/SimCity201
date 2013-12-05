package transportation.test;

import transportation.CarGui;
import transportation.CarPassengerRole;
import transportation.CarPassengerRole.State;
import transportation.mock.MockCar;
import junit.framework.TestCase;


import people.People;
import people.PeopleAgent;
import transportation.gui.CarPassengerGui;
import transportation.interfaces.*;

public class CarPassengerRoleTest extends TestCase{

	CarPassengerRole cpr = new CarPassengerRole();
	People p = new PeopleAgent("people", 0, false);
	MockCar mockCar = new MockCar("mockCar");
	CarPassengerGui cpg = new CarPassengerGui();
	//CarGui cg = new CarGui();
	public void setUp() throws Exception{
		super.setUp();
		cpr.setPerson(p);
		cpr.setGui(cpg);
		//mockCar.setGui(cg);
		cpr.setCar(mockCar);
	}
	
	public void testingCaseWithCarAndOnePassenger() {
		
		cpr.msgIsActive();
		cpr.destination = "Bank";
		//assertEquals("CarPassengerRole's state should be readyToLeave but its not", State.readyToLeave, cpr.myState);
		//assertTrue("CarPassengerRole's scheduler should return true to react to the state change but it didn't",cpr.pickAndExecuteAnAction());
		assertEquals("Car should have log size one to record recieving message that car passenger whats to go somewhere, but it didnt",1,mockCar.log.size());
		cpr.msgArrivedToDestination("Bank");
		//assertTrue("CarPassengerRole's scheduler should return true to react to the car arriving, but it didnt",cpr.pickAndExecuteAnAction());
		assertEquals("Car should have log size two to record that passenger is leving, but it didn't",2,mockCar.log.size());
		
	}
	
	
}
