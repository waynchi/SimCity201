package transportation.interfaces;

import people.PeopleAgent;

public interface CarPassenger {

	public abstract void msgIsActive();

	public abstract void msgArrivedToDestination(String place);

	public abstract boolean pickAndExecuteAnAction();

	public abstract void setCar(Car c);

	public abstract void setPersonAgent(PeopleAgent p);

}