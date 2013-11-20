package transportation;

import people.People;
import transportation.interfaces.Bus;
import transportation.interfaces.BusPassenger;

public class BusPassengerRole extends Role implements BusPassenger{

	public BusStop currentBusStop;
	public BusStop destination;
	public Bus myBus;
	public enum Event {busArrived,busArrivedAtDestination};
	public enum State {waitingAtBusStop,waitingInBus,leavingBus};
	Event event;
	State myState;
	People myPerson;
	
	public BusPassengerRole(){
		myPerson = getPersonAgent();
	}
	
	/* (non-Javadoc)
	 * @see transportation.BusPassenger#msgIsActive()
	 */
	@Override
	public void msgIsActive(){
	
	currentBusStop.msgWaitingHere(this);
	myState = State.waitingAtBusStop;
	}

	/* (non-Javadoc)
	 * @see transportation.BusPassenger#msgBusArrived(transportation.interfaces.Bus)
	 */
	@Override
	public void msgBusArrived(Bus b){
	System.out.println("Bus passenger recieved message that bus arrived");
	myBus = b;
	event = Event.busArrived;
	stateChanged();
	}
	
	

	/* (non-Javadoc)
	 * @see transportation.BusPassenger#msgArrivedAtStop(transportation.BusStop)
	 */
	@Override
	public void msgArrivedAtStop(BusStop bs){
	System.out.println("Bus passenger recieved message that bus arrived at new bus stop");
	if (bs == destination)
	{
		event = Event.busArrivedAtDestination;
	    stateChanged();        
	}
	}

	/* (non-Javadoc)
	 * @see transportation.BusPassenger#pickAndExecuteAnAction()
	 */
	@Override
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
	System.out.println("Bus passenger preparing to board bus");
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
