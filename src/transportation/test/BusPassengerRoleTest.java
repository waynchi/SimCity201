package transportation.test;

import static org.junit.Assert.*;
import junit.framework.TestCase;

import org.junit.Test;

import people.PeopleAgent;
import transportation.BusAgent;
import transportation.BusGui;
import transportation.BusPassengerRole;
import transportation.BusStop;
import transportation.BusAgent.BusState;
import transportation.mock.MockBus;

public class BusPassengerRoleTest extends TestCase{
	
	BusPassengerRole bpr = new BusPassengerRole();
	MockBus mockBus = new MockBus("mockbus");
//	BusAgent busAgent = new BusAgent();
	BusGui bg = new BusGui();
	BusStop bs = new BusStop();
	PeopleAgent p = new PeopleAgent();
	BusStop destinationBusStop = new BusStop();
	
	public void setUp() throws Exception{
		super.setUp();
		bpr.setPerson(p);
		bpr.currentBusStop = bs;
		mockBus.busGui = bg;
		bpr.destination = destinationBusStop;
	}
	
	public void testBusWithMultiplePassengers(){
		bpr.msgIsActive();
		assertEquals("Bus stop's passenger list should be 1, but it's not",1,bs.waitingPassengers.size());
		bs.msgBusArrived(mockBus);
		assertEquals("BusPassengerRole's bus variable should be set to the bus that arrived, but it's not",mockBus,bpr.myBus);
		assertTrue("BusPassengerRole's scheduler should return true to react to the bus arriving, but it didnt",bpr.pickAndExecuteAnAction());
		assertTrue("Bus's log should record that bus passenger is boarding",mockBus.log.containsString("Recieved message that busPassenger is boarding"));
		assertTrue("Bus's log should record that all bus stop passengers have been notified that bus arrived",mockBus.log.containsString("Recieved message that all passengers have been notified"));
		mockBus.msgAnimationFinishedArrivedAtStop(destinationBusStop);
		assertTrue("Bus's log should record that bus animation finished arriving to new stop",mockBus.log.containsString("Recieved message that animation finished arriving to bus stop"));
		
		//assertEquals("Bus's passenger list should now be one, but it's not",1,mockBus.myBusPassengers.size());
		//assertEquals("Bus's state should now be readyToLeave, but it's not",BusState.readyToLeave,mockBus.busState);
		//assertTrue("Bus's scheduler should return true to start driving to the next stop, but it didnt",mockBus.pickAndExecuteAnAction());
		//mockBus.msgAnimationFinishedArrivedAtStop(destinationBusStop);
		//assertTrue("Bus's scheduler should return true to react to arriving to busStop, but it didnt",busAgent.pickAndExecuteAnAction());
		//assertTrue("BusPassengerRole's scheduler should return true to react to arriving to destination bus stop, but it didnt",bpr.pickAndExecuteAnAction());
		//assertEquals("Bus's passenger list should now be zero because passenger left, but its not",0,busAgent.myBusPassengers.size());
		//assertTrue("Bus's scheduler should return true to wait for new customers, but it didnt",busAgent.pickAndExecuteAnAction());
		
	}

}
