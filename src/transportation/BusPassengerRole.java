package transportation;

import java.util.concurrent.Semaphore;

import people.People;
import people.Role;
import transportation.gui.BusPassengerGui;
import transportation.interfaces.Bus;
import transportation.interfaces.BusPassenger;
import city.gui.Sidewalk;

public class BusPassengerRole extends Role implements BusPassenger{

	public BusStop currentBusStop;
	//public BusStop destinationBusStop;
	public String destinationPlace;
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
	//System.out.println("SDFS");
	currentBusStop.msgWaitingHere(this);
	//myState = State.waitingAtBusStop;
	}

	/* (non-Javadoc)
	 * @see transportation.BusPassenger#msgBusArrived(transportation.interfaces.Bus)
	 */
	@Override
	public void msgBusArrived(Bus b){
	//print("Bus passenger recieved message that bus arrived");
	myBus = b;
	//event = Event.busArrived;
	//stateChanged();
	currentBusStop.msgLeavingBusStop(this);
//	System.out.println("Calling bus");
//	myBus.msgImBoarding(this);
//	
	}
	
	

	/* (non-Javadoc)
	 * @see transportation.BusPassenger#msgArrivedAtStop(transportation.BusStop)
	 */
	@Override
	public void msgArrivedAtStop(BusStop bs){
	//print("Bus passenger recieved message that bus arrived at new bus stop");
	boolean getOffHere = false;
	for(String place : bs.getNearbyPlaces())
	{
		if(place.equals(this.destinationPlace))
			getOffHere = true;
	}
	if (getOffHere)
	{
//		event = Event.busArrivedAtDestination;
//	    stateChanged(); 
		//print("Bus passenger getting off here");
		myBus.msgImLeaving(this);
		//myGui.DoLeaveBus(this);
		if(myPerson != null)
		{
			if(bs.name.equals("BusStop 1"))
			{
				myPerson.getPersonGui().setSidewalk(bs.bp.myCity.cityPanel.allSidewalks.get(0).get(9));
				myPerson.getPersonGui().setSideWalkSegment(bs.bp.myCity.cityPanel.allSidewalks.get(0));
				myPerson.getPersonGui().setDirection("right");
			}
			else if(bs.name.equals("BusStop 2"))
			{
				myPerson.getPersonGui().setSidewalk(bs.bp.myCity.cityPanel.allSidewalks.get(25).get(27));
				myPerson.getPersonGui().setSideWalkSegment(bs.bp.myCity.cityPanel.allSidewalks.get(25));
				myPerson.getPersonGui().setDirection("right");		
			}
			else if(bs.name.equals("BusStop 3"))
			{
				myPerson.getPersonGui().setSidewalk(bs.bp.myCity.cityPanel.allSidewalks.get(7).get(10));
				myPerson.getPersonGui().setSideWalkSegment(bs.bp.myCity.cityPanel.allSidewalks.get(7));
				myPerson.getPersonGui().setDirection("left");
				
			}
			else if(bs.name.equals("BusStop 4"))
			{
				myPerson.getPersonGui().setSidewalk(bs.bp.myCity.cityPanel.allSidewalks.get(23).get(24));
				myPerson.getPersonGui().setSideWalkSegment(bs.bp.myCity.cityPanel.allSidewalks.get(23));
				myPerson.getPersonGui().setDirection("left");
				if(myPerson.getPersonGui().simulatingBusStop)
					myPerson.getPersonGui().setDestination("Bank");
			}
			myPerson.msgDone("BusPassenger");
		}
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
	
//		if(event == Event.busArrived && myState == State.waitingAtBusStop) {
//		        myState = State.waitingInBus;
//		        BoardBus();
//		        return true;
//		}
//	
//		if(event == Event.busArrivedAtDestination && myState == State.waitingInBus) {
//		        myState = State.leavingBus;
//		        LeaveBus();
//		        return true;
//		}
		return false;
	}
//	
//	private void BoardBus(){
//	System.out.println("Bus passenger preparing to board bus");
//	myBus.msgImBoarding(this);
//	currentBusStop.msgLeavingBusStop(this);
//	}
//
//	private void LeaveBus() {
//	myBus.msgImLeaving(this);
//	myGui.DoLeaveBus(this);
//	//add gui for leaving bus
//	}
//	
//
//	
	@Override
	public void setPerson(People p) {
		// TODO Auto-generated method stub
		this.myPerson = p;
	}
//	
//	public void setGui(BusPassengerGui g)
//	{
//		myGui = g;
//	}
//
	public void setCurrentBusStop(BusStop S)
	{
		this.currentBusStop = S;
	}
//	
//	public void setDestinationBusStop(BusStop D)
//	{
//		this.destinationBusStop = D;
//	}
	
	public void setDestinationPlace(String place)
	{
		this.destinationPlace = place;
	}

	


}
