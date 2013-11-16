package transportation;
import transportation.BusPassengerRole;
import java.util.*;

public class BusStop {
List<BusPassengerRole> waitingPassengers;

BusStop(){
	
}


public void msgWaitingHere(BusPassengerRole bpr){
waitingPassengers.add(bpr);
}

public void msgBusArrived(Bus b){
for(BusPassengerRole passenger : waitingPassengers) 
        passenger.msgBusArrived(b);
b.msgAllBusStopPassengersNotified();
}

public void msgLeavingBusStop(BusPassengerRole bpr){
waitingPassengers.remove(bpr);
}

}

