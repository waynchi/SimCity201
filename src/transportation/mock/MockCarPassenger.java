package transportation.mock;

import people.PeopleAgent;
import transportation.interfaces.Car;
import transportation.interfaces.CarPassenger;

public class MockCarPassenger extends Mock implements CarPassenger {

	public MockCarPassenger(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgIsActive() {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgArrivedToDestination(String place) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCar(Car c) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPersonAgent(PeopleAgent p) {
		// TODO Auto-generated method stub

	}

	

}
