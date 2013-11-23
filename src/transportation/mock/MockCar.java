package transportation.mock;

import people.Role;
import test.EventLog;
import test.LoggedEvent;
import transportation.CarGui;
import transportation.interfaces.Car;
import transportation.interfaces.CarPassenger;

public class MockCar extends Role implements Car {

	public EventLog log = new EventLog();
	
	public MockCar(String name) {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgTakeMeHere(CarPassenger c, String place) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Recieved message that car passenger wants to go somewhere"));
	}

	@Override
	public void msgAnimationFinishedArrivedAtDestination(String place) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgImLeaving(CarPassenger cpr) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Recieved message the car passenger is leaving"));
	}

	@Override
	public void setGui(CarGui cg) {
		// TODO Auto-generated method stub

	}

}
