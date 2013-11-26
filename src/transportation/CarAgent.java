package transportation;
import java.util.*;

import transportation.interfaces.Car;
import transportation.interfaces.CarPassenger;

import agent.Agent;
public class CarAgent extends Agent implements Car{

	public enum PassState {waitingInCar,leaving};
	public enum CarState {stopped,driving,arrivedToDestination};
	
	class Passenger {
	        CarPassenger cpr;
	        PassState st;
	        String destination;
	        Passenger(CarPassenger c, String d)
	        {
	                cpr = c;
	                destination = d;
	                st = PassState.waitingInCar;
	        }
	}
	public List<Passenger> myCarPassengers = new ArrayList<Passenger>();
	CarGui carGui;
	public CarState carState;

	
	public CarAgent(){
		carState = CarState.stopped;
	}
	
	/* (non-Javadoc)
	 * @see transportation.Car#msgTakeMeHere(transportation.CarPassengerRole, java.lang.String)
	 */
	@Override
	public void msgTakeMeHere(CarPassenger c, String place) {
	System.out.println("Car recieved message to go to: " + place);
	myCarPassengers.add(new Passenger(c,place));
	stateChanged();
	}

	/* (non-Javadoc)
	 * @see transportation.Car#msgAnimationFinishedArrivedAtDestination(java.lang.String)
	 */
	@Override
	public void msgAnimationFinishedArrivedAtDestination(String place){
	System.out.println("Car reieved message cargui arrived to destination");
	carState = CarState.arrivedToDestination;
	stateChanged();
	}

	/* (non-Javadoc)
	 * @see transportation.Car#msgImLeaving(transportation.CarPassengerRole)
	 */
	@Override
	public void msgImLeaving(CarPassenger cpr){
	System.out.println("Car recieved message that carpassenger is leaving");
	Passenger p = findPassenger(cpr);
	myCarPassengers.remove(p);
	carState = CarState.stopped;
	}

	

	public boolean pickAndExecuteAnAction() {
		
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
		System.out.println("Driving to " + myPassenger.destination);
	carGui.msgGoToThisPlace(this,myPassenger.destination);
	}

	private void TellPassengerWeArrived(Passenger myPassenger){
	myPassenger.cpr.msgArrivedToDestination(myPassenger.destination);
	}

	private Passenger findPassenger(CarPassenger cpr) {
		// TODO Auto-generated method stub
		for(Passenger p : myCarPassengers)
		{
			if(p.cpr == cpr)
				return p;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see transportation.Car#setGui(transportation.CarGui)
	 */
	@Override
	public void setGui(CarGui cg){
		carGui = cg;
	}
}
