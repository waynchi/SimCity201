package transportation.mock;

import transportation.CarGui;
import transportation.interfaces.Car;
import transportation.interfaces.CarPassenger;

public class MockCar extends Mock implements Car {

	public MockCar(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgTakeMeHere(CarPassenger c, String place) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgAnimationFinishedArrivedAtDestination(String place) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgImLeaving(CarPassenger cpr) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setGui(CarGui cg) {
		// TODO Auto-generated method stub

	}

}
