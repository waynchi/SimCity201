package transportation;

import people.People;
import people.Role;
import transportation.interfaces.Bus;
import transportation.interfaces.BusPassenger;

public class BusPassengerRole extends Role implements BusPassenger{

	public BusStop currentBusStop;
	public BusStop destination;
	public Bus myBus;
	public enum Event {busArrived,busArrivedAtDestination};
	public enum State {waitingAtBusStop,waitingInBus,leavingBus};
	public Event event;
	public State myState;
	public BusPassengerGui myGui;
	People myPerson;
	
	public BusPassengerRole(){
		myPerson = getPersonAgent();
	}
	
	/* (non-Javadoc)
	 * @see transportation.BusPassenger#msgIsActive()
	 */
	@Override
	public void msgIsActive(){
	//destination = myPerson.state.toString();
	
	currentBusStop.msgWaitingHere(this);
	//myState = State.waitingAtBusStop;
	}

	/* (non-Javadoc)
	 * @see transportation.BusPassenger#msgBusArrived(transportation.interfaces.Bus)
	 */
	@Override
	public void msgBusArrived(Bus b){
	System.out.println("Bus passenger recieved message that bus arrived");
	myBus = b;
	//event = Event.busArrived;
	//stateChanged();
	myBus.msgImBoarding(this);
	currentBusStop.msgLeavingBusStop(this);
	}
	
	

	/* (non-Javadoc)
	 * @see transportation.BusPassenger#msgArrivedAtStop(transportation.BusStop)
	 */
	@Override
	public void msgArrivedAtStop(BusStop bs){
	System.out.println("Bus passenger recieved message that bus arrived at new bus stop");
	if (bs == destination)
	{
//		event = Event.busArrivedAtDestination;
//	    stateChanged();    
		myBus.msgImLeaving(this);
		//myGui.DoLeaveBus(this);
		if(myPerson != null)
		myPerson.msgDone("BusPassenger");
	}
	}
	
	public void msgAnimationFinishedDoLeaveBus() {
		// TODO Auto-generated method stub
		System.out.println("Bus passenger recieved message that gui left bus, now messaging myPerson");
		myPerson.msgDone("BusPassenger");
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
	myGui.DoLeaveBus(this);
	//add gui for leaving bus
	}
	

	
	@Override
	public void setPerson(People p) {
		// TODO Auto-generated method stub
		this.myPerson = p;
	}
	
	public void setGui(BusPassengerGui g)
	{
		myGui = g;
	}

	

	


}
