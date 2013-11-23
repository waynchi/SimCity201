package transportation.test;

import people.mock.*;
import junit.framework.TestCase;


import people.People;
import transportation.BusGui;
import transportation.BusPassengerGui;
import transportation.BusPassengerRole;
import transportation.BusStop;
import transportation.BusPassengerRole.State;
import transportation.mock.MockBus;

public class BusPassengerRoleTest extends TestCase{
	
	BusPassengerRole bpr = new BusPassengerRole();
	MockBus mockBus = new MockBus("mockbus");
	BusPassengerGui bprGui = new BusPassengerGui();
	BusGui bg = new BusGui();
	BusStop bs = new BusStop();
	People p = new MockPeople("mockpeople",100,true);
	BusStop destinationBusStop = new BusStop();
	
	public void setUp() throws Exception{
		super.setUp();
		bpr.currentBusStop = bs;
		mockBus.busGui = bg;
		bpr.destination = destinationBusStop;
		bpr.setGui(bprGui);
		bpr.setPerson(p);
	}
	
	public void testBusWithMultiplePassengers(){
		bpr.msgIsActive();
		assertEquals("Bus stop's passenger list should be 1, but it's not",1,bs.waitingPassengers.size());
		bs.msgBusArrived(mockBus);
		assertEquals("BusPassengerRole's bus variable should be set to the bus that arrived, but it's not",mockBus,bpr.myBus);
		assertTrue("BusPassengerRole's scheduler should return true to react to the bus arriving, but it didnt",bpr.pickAndExecuteAnAction());
		assertTrue("Bus's log should record that bus passenger is boarding",mockBus.log.containsString("Recieved message that busPassenger is boarding"));
		assertTrue("Bus's log should record that all bus stop passengers have been notified that bus arrived",mockBus.log.containsString("Recieved message that all passengers have been notified"));
		assertEquals("BusPassengerRole's state should be waitingInBus, but it's not",bpr.myState,State.waitingInBus);
		bpr.msgArrivedAtStop(destinationBusStop);
		assertTrue("BusPassengerRole's scheudler should return true to react to arriving to destination, but it didnt",bpr.pickAndExecuteAnAction());
		assertEquals("Bus's log should record that bus passenger is leaving, but it didnt",3,mockBus.log.size());
		
		
	}

}
