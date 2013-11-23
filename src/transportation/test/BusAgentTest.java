package transportation.test;

import transportation.BusAgent;
import transportation.BusGui;
import transportation.BusPassengerGui;
import transportation.BusStop;
import transportation.mock.MockBus;
import transportation.mock.MockBusPassenger;
import junit.framework.TestCase;

public class BusAgentTest extends TestCase{

	BusAgent busAgent = new BusAgent();
	MockBusPassenger mockBusPassenger = new MockBusPassenger("mockbuspassenger");
	MockBusPassenger mockBusPassenger1 = new MockBusPassenger("mockbuspassenger");
	MockBusPassenger mockBusPassenger2 = new MockBusPassenger("mockbuspassenger");
	BusGui busGui = new BusGui();
	BusStop starting = new BusStop();
	BusStop destination = new BusStop();
	public void setUp() throws Exception{
		super.setUp();
		busAgent.setGui(busGui);
		starting.msgWaitingHere(mockBusPassenger);
		destination.msgWaitingHere(mockBusPassenger1);
		destination.msgWaitingHere(mockBusPassenger2);
	}
	
	public void test(){
		starting.msgBusArrived(busAgent);
		assertEquals("BusPassenger's log should now be 1 after being notified that bus arrived to busstop, but it didnt",1,mockBusPassenger.log.size());
		busAgent.msgImBoarding(mockBusPassenger);
		starting.msgLeavingBusStop(mockBusPassenger);
		assertTrue("Bus's scheduler should return true to react to all bus passengers notified, but it didn't",busAgent.pickAndExecuteAnAction());
		busAgent.msgAnimationFinishedArrivedAtStop(destination);
		assertTrue("Bus's scheduler should return true to react to arriving to new stop, but it didn't",busAgent.pickAndExecuteAnAction());
		assertEquals("BusPassenger's log should now be 2 after being notified that bus arrived to new stop, but it didnt",2,mockBusPassenger.log.size());
		assertTrue("Bus's scheduler should return true to react to all passengers being notified, but it didn't",busAgent.pickAndExecuteAnAction());
		assertEquals("BusPassenger's log should now be 1 after being notified that bus arrived to busstop, but it didnt",1,mockBusPassenger1.log.size());
		assertEquals("BusPassenger's log should now be 1 after being notified that bus arrived to busstop, but it didnt",1,mockBusPassenger2.log.size());
		busAgent.msgImBoarding(mockBusPassenger1);
		busAgent.msgImBoarding(mockBusPassenger2);
		destination.msgLeavingBusStop(mockBusPassenger1);
		destination.msgLeavingBusStop(mockBusPassenger2);
		
		
	}
	

}
