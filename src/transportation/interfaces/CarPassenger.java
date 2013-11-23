package transportation.interfaces;

import people.People;

public interface CarPassenger {

	public abstract void msgIsActive();

	public abstract void msgArrivedToDestination(String place);

	public abstract boolean pickAndExecuteAnAction();

	public abstract void setCar(Car c);

	public abstract void setPerson(People p);

	public abstract void msgAnimationFinishedDoLeaveCar();

}