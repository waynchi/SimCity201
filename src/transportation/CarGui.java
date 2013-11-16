package transportation;

public class CarGui {

	/**
	 * @param args
	 */
	
	public CarGui(){
		
	}
	
	public void msgGoToThisPlace(Car carAgent,String place)
	{
		carAgent.msgAnimationFinishedArrivedAtDestination(place);
	}

}
