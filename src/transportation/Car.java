package transportation;
import java.util.*;

import agent.Agent;
public class Car extends Agent{

	public enum PassState {waitingInCar,leaving};
	public enum CarState {stopped,driving,arrivedToDestination};
	
	class Passenger {
	        CarPassengerRole cpr;
	        PassState st;
	        String destination;
	        Passenger(CarPassengerRole c, String d)
	        {
	                cpr = c;
	                destination = d;
	                st = PassState.waitingInCar;
	        }
	}
	List<Passenger> myCarPassengers = new ArrayList<Passenger>();
	CarGui carGui;
	CarState carState;

	
	public Car(){
		carState = CarState.stopped;
	}
	
	public void msgTakeMeHere(CarPassengerRole c, String place) {
	System.out.println("Car recieved message to go to: " + place);
	myCarPassengers.add(new Passenger(c,place));
	}

	public void msgAnimationFinishedArrivedAtDestination(String place){
	System.out.println("Car reieved message cargui arrived to destination");
	carState = CarState.arrivedToDestination;
	stateChanged();
	}

	public void msgImLeaving(CarPassengerRole cpr){
	System.out.println("Car recieved message that carpassenger is leaving");
	Passenger p = findPassenger(cpr);
	myCarPassengers.remove(p);
	carState = CarState.stopped;
	}

	

	protected boolean pickAndExecuteAnAction() {
		
	for(Passenger myPassenger : myCarPassengers)
	{
		if (myPassenger.st == PassState.waitingInCar && carState == CarState.stopped)
		{
				carState = CarState.driving;
	            GoToDestination(myPassenger);
	            return true;
		}
	}
	for(Passenger myPassenger : myCarPassengers)
	{
		if(carState == CarState.arrivedToDestination && myPassenger.st == PassState.waitingInCar)
		{
	        myPassenger.st = PassState.leaving;
	        TellPassengerWeArrived(myPassenger);
	        return true;
		}
	}

	return false;
	} 

	
	private void GoToDestination(Passenger myPassenger){
	carGui.msgGoToThisPlace(this,myPassenger.destination);
	}

	private void TellPassengerWeArrived(Passenger myPassenger){
	myPassenger.cpr.msgArrivedToDestination(myPassenger.destination);
	}

	private Passenger findPassenger(CarPassengerRole cpr) {
		// TODO Auto-generated method stub
		for(Passenger p : myCarPassengers)
		{
			if(p.cpr == cpr)
				return p;
		}
		return null;
	}

	public void setGui(CarGui cg){
		carGui = cg;
	}
}
