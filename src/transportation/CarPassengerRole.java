package transportation;

import people.People;
import people.Role;
import transportation.interfaces.Car;
import transportation.interfaces.CarPassenger;

public class CarPassengerRole extends Role implements CarPassenger{

	
	
    public String destination;
	Car myCar;
	CarPassengerGui myGui;
	public enum State {readyToLeave,goingToDestination,leaving};
	public enum Event {carIsReady,carArrivedToDestination};
	public State myState;
	public Event event;
	People myPerson;

	public CarPassengerRole(){
		
		
	}
	
	public void setDestination(String destination)
	{
		this.destination = destination; 
	}
	/* (non-Javadoc)
	 * @see transportation.CarPassenger#msgIsActive()
	 */
	@Override
	public void msgIsActive(){
	System.out.println("CarPassenger is now active");
	myState = State.readyToLeave;
	event = Event.carIsReady;
	//destination = myPerson.state.toString();
	stateChanged();
	}

	/* (non-Javadoc)
	 * @see transportation.CarPassenger#msgArrivedToDestination(java.lang.String)
	 */
	@Override
	public void msgArrivedToDestination(String place){
 	System.out.println("Received message that Car arrived to destination: " + place);
	if(destination.equals(place)){
	event = Event.carArrivedToDestination;
	stateChanged();
	}
	}
	
	@Override
	public void msgAnimationFinishedDoLeaveCar() {
		// TODO Auto-generated method stub
		System.out.println("Recieved message that gui finished leaving car, sending done message to myPerson");
		myPerson.msgDone("CarPassenger");
	}

	/* (non-Javadoc)
	 * @see transportation.CarPassenger#pickAndExecuteAnAction()
	 */
	@Override
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
//	myGui.DoLeaveCar(this);
	myPerson.msgDone("CarPassenger");
	}
	
	

	/* (non-Javadoc)
	 * @see transportation.CarPassenger#setCar(transportation.interfaces.Car)
	 */
	@Override
	public void setCar(Car c)
	{
		myCar = c;
	}
	
	/* (non-Javadoc)
	 * @see transportation.CarPassenger#setPersonAgent(people.PeopleAgent)
	 */
	@Override
	public void setPerson(People p)
	{
		myPerson = p;
	}
	
	public void setGui(CarPassengerGui g)
	{
		myGui = g;
	}

	
}
