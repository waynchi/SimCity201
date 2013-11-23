package transportation;
import transportation.BusPassengerRole;
import transportation.interfaces.Bus;
import transportation.interfaces.BusPassenger;

import java.util.*;

public class BusStop {
public List<BusPassengerRole> waitingPassengers = new ArrayList<BusPassengerRole>();
public List<BusPassengerRole> boardingPassengers = new ArrayList<BusPassengerRole>();
Bus currentBus;
public BusStop(){
	
}


public void msgWaitingHere(BusPassengerRole bpr){
System.out.println("BusStop recieved message that a bus passenger arrived");
waitingPassengers.add(bpr);
}

public void msgBusArrived(Bus b){
	System.out.println("BusStop recieved message that bus has arrived");
	currentBus = b;
	boardingPassengers = waitingPassengers;
for(BusPassenger passenger : boardingPassengers) 
        passenger.msgBusArrived(b);
}

public void msgLeavingBusStop(BusPassenger bpr){
if(boardingPassengers.contains(bpr))
	boardingPassengers.remove(bpr);
waitingPassengers.remove(bpr);
if(boardingPassengers.isEmpty())
{
	currentBus.msgAllBusStopPassengersNotified();
	currentBus = null;
}

}

}

