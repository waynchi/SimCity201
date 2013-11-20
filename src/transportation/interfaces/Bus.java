package transportation.interfaces;

import transportation.BusGui;
import transportation.BusStop;

public interface Bus {

	public abstract void msgImBoarding(BusPassenger p);

	public abstract void msgImLeaving(BusPassenger p);

	public abstract void msgAnimationFinishedArrivedAtStop(BusStop S);

	public abstract void msgAllBusStopPassengersNotified();

	public abstract void setGui(BusGui bg);

	public abstract void GoToNextStop();

}