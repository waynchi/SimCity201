package transportation;
import java.util.*;

import agent.Agent;
import transportation.interfaces.Bus;
import transportation.interfaces.BusPassenger;
public class BusAgent extends Agent implements Bus{
	public enum PassState {waiting,waitingAndNotified};
class Passenger {
	PassState st;
    BusPassenger bpr;
    Passenger(BusPassenger p)
    {
            bpr = p;
            st = PassState.waiting;
    }
}
List<Passenger> myBusPassengers = new ArrayList<Passenger>();
BusGui busGui;
public enum BusState {driving, newStop, newStopAndPassengersNotified, waitingForNewPassengers, readyToLeave, off};
BusState busState;
BusStop currentStop;
List<BusStop> myBusStops = new ArrayList<BusStop>();

public BusAgent(){
	super();
}


/* (non-Javadoc)
 * @see transportation.Bus#msgImBoarding(transportation.BusPassengerRole)
 */
@Override
public void msgImBoarding(BusPassenger p){ //remove place
System.out.println("Bus recieved message that passenger is boarding");
myBusPassengers.add(new Passenger(p));
}

/* (non-Javadoc)
 * @see transportation.Bus#msgImLeaving(transportation.BusPassengerRole)
 */
@Override
public void msgImLeaving(BusPassenger p){
	System.out.println("Bus recieved message that passenger is leaving");
Passenger toRemove = findPassenger(p);
myBusPassengers.remove(toRemove);
}



/* (non-Javadoc)
 * @see transportation.Bus#msgAnimationFinishedArrivedAtStop(transportation.BusStop)
 */
@Override
public void msgAnimationFinishedArrivedAtStop(BusStop S){
System.out.println("Recieved message that bus arrived to new stop");
currentStop = S;
busState = BusState.newStop;
stateChanged();
}

/* (non-Javadoc)
 * @see transportation.Bus#msgAllBusStopPassengersNotified()
 */
@Override
public void msgAllBusStopPassengersNotified(){
System.out.println("Bus recieved message that all BusStop Passengers have been notified, and is now ready to leave");
busState = BusState.readyToLeave;
stateChanged();
}



public boolean pickAndExecuteAnAction() {
	
for (Passenger passenger : myBusPassengers)
{
    if (busState == BusState.newStop && passenger.st == PassState.waiting)
    {
            passenger.st = PassState.waitingAndNotified;
            NotifyPassengerAboutCurrentStop(passenger);
            return true;
    }
}
if(myBusPassengers.isEmpty() && busState == BusState.newStop)
{
	busState = BusState.waitingForNewPassengers;
	WaitForNewPassengers();
	return true;
}


if(busState == BusState.newStopAndPassengersNotified)
{
    busState = BusState.waitingForNewPassengers;
    WaitForNewPassengers();
    return true;
}

if(busState == BusState.readyToLeave) {
    busState = BusState.driving;
    GoToNextStop();
    return true;
}

return false;
}

private void WaitForNewPassengers(){
currentStop.msgBusArrived(this);
}

private void NotifyPassengerAboutCurrentStop(Passenger p){
p.bpr.msgArrivedAtStop(currentStop);
if(areAllBusPassengersNotified()) //function that loops through passengers, checking if each state says notified
{
	busState = BusState.newStopAndPassengersNotified;
	stateChanged();
}
    
}

public void GoToNextStop(){
busGui.msgGoToNextStop(this,currentStop);
}

private Passenger findPassenger(BusPassenger target) {
	// TODO Auto-generated method stub
	for(Passenger p : myBusPassengers)
	{
		if(p.bpr == target)
			return p;
	}
	return null;
}
private boolean areAllBusPassengersNotified(){
	for(Passenger p : myBusPassengers)
	{
		if(p.st != PassState.waitingAndNotified)
			return false;
	}
	return true;
}

/* (non-Javadoc)
 * @see transportation.Bus#setGui(transportation.BusGui)
 */
@Override
public void setGui(BusGui bg){
	busGui = bg;
}
}

