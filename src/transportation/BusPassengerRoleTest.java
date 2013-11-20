package transportation;

import static org.junit.Assert.*;
import junit.framework.TestCase;

import org.junit.Test;

import people.People;
import people.PeopleAgent;
import transportation.Bus.BusState;

public class BusPassengerRoleTest extends TestCase{

	Bus busAgent = new Bus();
	BusGui bg = new BusGui();
	BusPassengerRole bpr = new BusPassengerRole();
	BusStop bs = new BusStop();
	People p = new PeopleAgent();
	BusStop destinationBusStop = new BusStop();
	
	public void setUp() throws Exception{
		super.setUp();
		bpr.setPerson(p);
		bpr.currentBusStop = bs;
		busAgent.busGui = bg;
		bpr.destination = destinationBusStop;
	}
	
	public void testBusWithMultiplePassengers(){
		bpr.msgIsActive();
		assertEquals("Bus stop's passenger list should be 1, but it's not",1,bs.waitingPassengers.size());
		bs.msgBusArrived(busAgent);
		assertEquals("BusPassengerRole's bus variable should be set to the bus that arrived, but it's not",busAgent,bpr.myBus);
		assertTrue("BusPassengerRole's scheduler should return true to react to the bus arriving, but it didnt",bpr.pickAndExecuteAnAction());
		assertEquals("Bus's passenger list should now be one, but it's not",1,busAgent.myBusPassengers.size());
		assertEquals("Bus's state should now be readyToLeave, but it's not",BusState.readyToLeave,busAgent.busState);
		assertTrue("Bus's scheduler should return true to start driving to the next stop, but it didnt",busAgent.pickAndExecuteAnAction());
		busAgent.msgAnimationFinishedArrivedAtStop(destinationBusStop);
		assertTrue("Bus's scheduler should return true to react to arriving to busStop, but it didnt",busAgent.pickAndExecuteAnAction());
		assertTrue("BusPassengerRole's scheduler should return true to react to arriving to destination bus stop, but it didnt",bpr.pickAndExecuteAnAction());
		assertEquals("Bus's passenger list should now be zero because passenger left, but its not",0,busAgent.myBusPassengers.size());
		assertTrue("Bus's scheduler should return true to wait for new customers, but it didnt",busAgent.pickAndExecuteAnAction());
		
	}

}
