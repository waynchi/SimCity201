package transportation.test;

import transportation.CarPassengerRole;
import transportation.mock.MockCar;
import junit.framework.TestCase;


import people.People;
import people.PeopleAgent;
import transportation.gui.CarPassengerGui;

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
		assertEquals("Car should have log size one to record recieving message that car passenger whats to go somewhere, but it didnt",1,mockCar.log.size());
		cpr.msgArrivedToDestination("Bank");
		assertEquals("Car should have log size two to record that passenger is leaving, but it didn't",2,mockCar.log.size());
		
	}
	
	
}
