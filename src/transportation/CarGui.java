package transportation;

import transportation.interfaces.Car;

public class CarGui {

	/**
	 * @param args
	 */
	
	public CarGui(){
		
	}
	
	public void msgGoToThisPlace(Car carAgent,String place)
	{
		System.out.println("CarGui recieved message to go to " + place);
		carAgent.msgAnimationFinishedArrivedAtDestination(place);
	}

}
