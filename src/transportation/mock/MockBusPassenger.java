package transportation.mock;

import people.People;
import people.Role;
import transportation.BusStop;
import transportation.interfaces.Bus;
import transportation.interfaces.BusPassenger;

public class MockBusPassenger extends Role implements BusPassenger {

	public MockBusPassenger(String name) {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgIsActive() {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgBusArrived(Bus b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgArrivedAtStop(BusStop bs) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPerson(People p) {
		// TODO Auto-generated method stub

	}

}
