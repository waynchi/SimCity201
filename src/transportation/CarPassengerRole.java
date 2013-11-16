package transportation;

import people.PeopleAgent;
import people.Role;

public class CarPassengerRole extends Role{

	
	
    String destination;
	Car myCar;
	public enum State {readyToLeave,goingToDestination,leaving};
	public enum Event {carIsReady,carArrivedToDestination};
	State myState;
	Event event;
	
	public CarPassengerRole(){
		
		
	}
	
	public void msgIsActive(){
	print("Is Active");
	myState = State.readyToLeave;
	event = Event.carIsReady;
	destination = myPerson.state.toString();
	stateChanged();
	}

	public void msgArrivedToDestination(String place){
	print("Arrived at Destination");
	if(destination.equals(place)){
	event = Event.carArrivedToDestination;
	stateChanged();
	}
	}

	public boolean pickAndExecuteAnAction() {
		
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
	
	public void setCar(Car car) {
		myCar = car;
	}


}
