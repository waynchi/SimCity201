package transportation.mock;

import people.People;
import people.Role;
import transportation.BusStop;
import transportation.interfaces.Bus;
import transportation.interfaces.BusPassenger;
import test.EventLog;
import test.LoggedEvent;

public class MockBusPassenger extends Role implements BusPassenger {

	public EventLog log = new EventLog();
	
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
		log.add(new LoggedEvent("Receieved message that bus arrived to the bus stop im waiting in"));
	}

	@Override
	public void msgArrivedAtStop(BusStop bs) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Recieved message that bus arrived at new stop"));
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
