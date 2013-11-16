package transportation;

public class CarGui {

	/**
	 * @param args
	 */
	public void msgGoToThisPlace(Car carAgent,String place)
	{
		carAgent.msgAnimationFinishedArrivedAtDestination(place);
	}

}
