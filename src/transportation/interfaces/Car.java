package transportation.interfaces;

import transportation.CarGui;

public interface Car {

	public abstract void msgTakeMeHere(CarPassenger c, String place);

	public abstract void msgAnimationFinishedArrivedAtDestination(String place);

	public abstract void msgImLeaving(CarPassenger cpr);

	public abstract void setGui(CarGui cg);

}