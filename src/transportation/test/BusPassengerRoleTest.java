package transportation.test;

import java.util.ArrayList;
import java.util.Arrays;

import market.test.MockPeople;
import junit.framework.TestCase;


import people.People;
import people.PeopleAgent;
import transportation.BusPassengerRole;
import transportation.BusStop;
import transportation.BusPassengerRole.State;
import transportation.gui.BusGui;
import transportation.gui.BusPassengerGui;
import transportation.mock.MockBus;
import transportation.BusStop.MyBusPassenger;

public class BusPassengerRoleTest extends TestCase{
	
	BusPassengerRole bpr = new BusPassengerRole();
	MockBus mockBus = new MockBus("mockbus");
	BusPassengerGui bprGui = new BusPassengerGui();
	//BusGui bg = new BusGui();
	BusStop currentBusStop = new BusStop(null,220,180,30,30,220,152,new ArrayList<String>(Arrays.asList("Home 1","Home 2",
			"Home 3","Home 4","Home 5","Home 6","Home 7","Home 8","Home 9","Home 10","Home 11","Home 12","Apartment 1","Apartment 2")), "BusStop 1");
	BusStop destinationBusStop = new BusStop(null,650,90,30,30,660,132,new ArrayList<String>(Arrays.asList("Restaurant 2", "Restaurant 5","Bank")), "BusStop 4");
	People p = new PeopleAgent("people", 0, false);
	MyBusPassenger mbp;
	
	
	public void setUp() throws Exception{
		super.setUp();
		//mockBus.busGui = bg;
		bpr.setCurrentBusStop(currentBusStop);
		bpr.myGui = bprGui;
		bpr.setPerson(p);
		bpr.setDestinationPlace("Bank");
	}
	
	public void testBusWithMultiplePassengers(){
		currentBusStop.addBoardingPassenger(bpr);
		currentBusStop.msgBusArrived(mockBus);
		assertEquals("BusPassengerRole's bus variable should be set to the bus that arrived, but it's not",mockBus,bpr.myBus);
		//assertTrue("BusPassengerRole's scheduler should return true to react to the bus arriving, but it didnt",bpr.pickAndExecuteAnAction());
		assertTrue("Bus's log should record that bus passenger is boarding",mockBus.log.containsString("Recieved message that busPassenger is boarding"));
		assertTrue("Bus's log should record that all bus stop passengers have been notified that bus arrived",mockBus.log.containsString("Recieved message that all passengers have been notified"));
		//assertEquals("BusPassengerRole's state should be waitingInBus, but it's not",bpr.myState,State.waitingInBus);
		//bpr.msgArrivedAtStop(destinationBusStop);
		//assertTrue("BusPassengerRole's scheudler should return true to react to arriving to destination, but it didnt",bpr.pickAndExecuteAnAction());
		assertEquals("Bus's log should record that bus passenger is leaving, but it didnt",3,mockBus.log.size());
		
		
	}

}
