package transportation;
import transportation.BusPassengerRole;
import transportation.interfaces.Bus;
import transportation.interfaces.BusPassenger;

import java.util.*;

public class BusStop {
public List<BusPassenger> waitingPassengers = new ArrayList<BusPassenger>();
public List<BusPassenger> boardingPassengers = new ArrayList<BusPassenger>();
Bus currentBus;
public BusStop(){
	
}


public void msgWaitingHere(BusPassenger bpr){
System.out.println("BusStop recieved message that a bus passenger arrived");
waitingPassengers.add(bpr);
}

public void msgBusArrived(Bus b){
	System.out.println("BusStop recieved message that bus has arrived");
	currentBus = b;
	boardingPassengers = waitingPassengers;
	if(boardingPassengers.isEmpty())
	{
		currentBus.msgAllBusStopPassengersNotified();
		currentBus = null;
		return;
	}
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

