package transportation;

import people.People;
import people.Role;

public class BusPassengerRole extends Role{

	BusStop currentBusStop;
	BusStop destination;
	Bus myBus;
	public enum Event {busArrived,busArrivedAtDestination};
	public enum State {waitingAtBusStop,waitingInBus,leavingBus};
	Event event;
	State myState;
	People myPerson;
	
	public BusPassengerRole(){
		myPerson = getPersonAgent();
	}
	
	public void msgIsActive(){
	
	currentBusStop.msgWaitingHere(this);
	myState = State.waitingAtBusStop;
	}

	public void msgBusArrived(Bus b){
	System.out.println("Bus passenger recieved message that bus arrived");
	myBus = b;
	event = Event.busArrived;
	stateChanged();
	}
	
	

	public void msgArrivedAtStop(BusStop bs){
	if (bs == destination)
	{
		event = Event.busArrivedAtDestination;
	    stateChanged();        
	}
	}

	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
	
		if(event == Event.busArrived && myState == State.waitingAtBusStop) {
		        myState = State.waitingInBus;
		        BoardBus();
		        return true;
		}
	
		if(event == Event.busArrivedAtDestination && myState == State.waitingInBus) {
		        myState = State.leavingBus;
		        LeaveBus();
		        return true;
		}
		return false;
	}
	
	private void BoardBus(){
	myBus.msgImBoarding(this);
	currentBusStop.msgLeavingBusStop(this);
	}

	private void LeaveBus() {
	myBus.msgImLeaving(this);
	}
	
	public void setPersonAgent(People p){
		myPerson = p;
	}

	


}
