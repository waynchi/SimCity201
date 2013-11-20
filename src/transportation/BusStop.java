package transportation;
import transportation.BusPassengerRole;
import java.util.*;
import java.util.concurrent.Semaphore;

public class BusStop {
List<BusPassengerRole> waitingPassengers = new ArrayList<BusPassengerRole>();
List<BusPassengerRole> boardingPassengers = new ArrayList<BusPassengerRole>();
Bus currentBus;
BusStop(){
	
}


public void msgWaitingHere(BusPassengerRole bpr){
System.out.println("BusStop recieved message that a bus passenger arrived");
waitingPassengers.add(bpr);
}

public void msgBusArrived(Bus b){
	System.out.println("Recieved message that bus has arrived");
	currentBus = b;
	boardingPassengers = waitingPassengers;
for(BusPassengerRole passenger : boardingPassengers) 
        passenger.msgBusArrived(b);
}

public void msgLeavingBusStop(BusPassengerRole bpr){
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

