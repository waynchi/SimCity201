package transportation.interfaces;

import people.PeopleAgent;
import transportation.BusStop;

public interface BusPassenger {

	public abstract void msgIsActive();

	public abstract void msgBusArrived(Bus b);

	public abstract void msgArrivedAtStop(BusStop bs);

	public abstract boolean pickAndExecuteAnAction();

	public abstract void setPersonAgent(PeopleAgent p);

}