package transportation.mock;

import test.*;
import transportation.BusGui;
import transportation.BusStop;
import transportation.interfaces.Bus;
import transportation.interfaces.BusPassenger;

public class MockBus extends Mock implements Bus{

	public BusGui busGui;
	public EventLog log = new EventLog();
	
	public MockBus(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	

	@Override
	public void msgImBoarding(BusPassenger p) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Recieved message that busPassenger is boarding"));
	}

	@Override
	public void msgImLeaving(BusPassenger p) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Recieved message that busPassenger is leaving"));
	}

	@Override
	public void msgAnimationFinishedArrivedAtStop(BusStop S) {
		// TODO Auto-generated method stub
		//System.out.println("Bus recieved message that it arrived to bus stop");
		log.add(new LoggedEvent("Recieved message that animation finished arriving to bus stop"));
	}

	@Override
	public void msgAllBusStopPassengersNotified() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Recieved message that all passengers have been notified"));
	}

	@Override
	public void setGui(BusGui bg) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void GoToNextStop(){
		//\\System.out.println("Driving to next stop");
	}
	
	

}
