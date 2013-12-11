package transportation.test;

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;


import people.People;
import people.PeopleAgent;
import transportation.BusPassengerRole;
import transportation.BusStop;
import transportation.gui.BusPassengerGui;
import transportation.mock.MockBus;
import transportation.BusStop.MyBusPassenger;

public class BusPassengerRoleTest extends TestCase{
	
	BusPassengerRole bpr1 = new BusPassengerRole();
	BusPassengerRole bpr2 = new BusPassengerRole();
	BusPassengerRole bpr3 = new BusPassengerRole();
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
		bpr1.setCurrentBusStop(currentBusStop);
		bpr1.myGui = bprGui;
		bpr1.setDestinationPlace("Bank");
		bpr2.setCurrentBusStop(currentBusStop);
		bpr2.myGui = bprGui;
		bpr2.setDestinationPlace("Bank");
		bpr3.setCurrentBusStop(currentBusStop);
		bpr3.myGui = bprGui;
		bpr3.setDestinationPlace("Bank");
		
	}
	
	public void testBusWithMultiplePassengers(){
		currentBusStop.addBoardingPassenger(bpr1);
		currentBusStop.addBoardingPassenger(bpr2);
		currentBusStop.addBoardingPassenger(bpr3);
		currentBusStop.msgBusArrived(mockBus);
		assertEquals("BusPassengerRole's bus variable should be set to the bus that arrived, but it's not",mockBus,bpr1.myBus);
		assertEquals("BusPassengerRole's bus variable should be set to the bus that arrived, but it's not",mockBus,bpr2.myBus);
		assertEquals("BusPassengerRole's bus variable should be set to the bus that arrived, but it's not",mockBus,bpr3.myBus);
		currentBusStop.msgAnimationFinishedDoLeaveBusStop(bpr1);
		currentBusStop.msgAnimationFinishedDoLeaveBusStop(bpr2);
		currentBusStop.msgAnimationFinishedDoLeaveBusStop(bpr3);
		assertEquals("Bus's log should record that bus passenger is boarding, but it didnt",3,mockBus.log.size());
		bpr1.msgArrivedAtStop(destinationBusStop);
		bpr2.msgArrivedAtStop(destinationBusStop);
		bpr3.msgArrivedAtStop(destinationBusStop);
		assertEquals("Bus's log should record that bus passenger is leaving, but it didnt",6,mockBus.log.size());
		
		
	}

}
