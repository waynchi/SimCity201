package transportation;

import people.Role;

public class CarPassengerRole extends Role{

    String destination;
	Car myCar;
	public enum State {readyToLeave,goingToDestination,leaving};
	public enum Event {carIsReady,carArrivedToDestination};
	State myState;
	Event event;
	
	public void msgIsActive(){
	myState = State.readyToLeave;
	event = Event.carIsReady;
	stateChanged();
	}

	public void msgArrivedToDestination(String place){
	if(destination.equals(place)){
	event = Event.carArrivedToDestination;
	stateChanged();
	}
	}

	protected boolean pickAndExecuteAnAction() {
		
	if(myState == State.readyToLeave && event == Event.carIsReady){
	myState = State.goingToDestination;
	GoToDestination();
	return true;
	}

	if(myState == State.goingToDestination && event == Event.carArrivedToDestination){
	myState = State.leaving;
	LeaveCar();
	return true;
	}
	
	return false;
	}

	private void GoToDestination(){
	myCar.msgTakeMeHere(this,destination);
	}
	
	private void LeaveCar(){
	myCar.msgImLeaving(this);
	}


}
