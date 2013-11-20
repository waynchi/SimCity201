package transportation;

import people.PeopleAgent;
import people.Role;

public class CarPassengerRole extends Role{

	
	
    public String destination;
	Car myCar;
	public enum State {readyToLeave,goingToDestination,leaving};
	public enum Event {carIsReady,carArrivedToDestination};
	public State myState;
	public Event event;
	PeopleAgent myPerson;
	
	public CarPassengerRole(){
		myPerson = getPersonAgent();
		
	}
	
	public void msgIsActive(){
	System.out.println("CarPassenger is now active");
	myState = State.readyToLeave;
	event = Event.carIsReady;
	destination = myPerson.state.toString();
	stateChanged();
	}

	public void msgArrivedToDestination(String place){
		System.out.println("Received message that Car arrived to destination: " + place);
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


	public void setCar(Car c)
	{
		myCar = c;
	}
	
	public void setPersonAgent(PeopleAgent p)
	{
		myPerson = p;
		setPerson(p);
	}
}
