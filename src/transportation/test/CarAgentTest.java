package transportation.test;

import transportation.CarAgent;
import transportation.CarGui;
import transportation.CarPassengerGui;
import transportation.mock.MockCarPassenger;
import junit.framework.TestCase;
import transportation.CarAgent.CarState;

public class CarAgentTest extends TestCase{

	CarAgent carAgent = new CarAgent();
	MockCarPassenger mockCPR = new MockCarPassenger();
	CarGui cg = new CarGui();
	
	public void setUp() throws Exception{
		super.setUp();
		carAgent.setGui(cg);
		mockCPR.setCar(carAgent);
	}

	public void testCar(){
		carAgent.msgTakeMeHere(mockCPR, "Bank");
		assertEquals("Car's list of passengers should now be 1, but it's not",1,carAgent.myCarPassengers.size());
		assertTrue("Car's scheduler should return true to react to the new passenger, but it's not",carAgent.pickAndExecuteAnAction());
		assertEquals("Car's state should be arrivedToDestination after driving to place, but it's not",CarState.arrivedToDestination,carAgent.carState);
		assertTrue("Car's scheduler should return true to react to arriving to destination, but it's not",carAgent.pickAndExecuteAnAction());
		assertEquals("CarPassenger's log should be size one to record arriving to destination, but it's not",1,mockCPR.log.size());
		carAgent.msgImLeaving(mockCPR);
		assertEquals("Car's list of passengers should now be 0 after passenger left, but it's not",0,carAgent.myCarPassengers.size());
		
		
		
	}
}
