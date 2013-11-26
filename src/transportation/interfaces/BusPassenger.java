package transportation.interfaces;

import people.People;
import transportation.BusStop;

public interface BusPassenger {

	public abstract void msgIsActive();

	public abstract void msgBusArrived(Bus b);

	public abstract void msgArrivedAtStop(BusStop bs);

	public abstract boolean pickAndExecuteAnAction();

	public abstract void setPerson(People p);

}