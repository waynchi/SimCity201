package transportation;
import java.util.*;

import city.gui.CityPanel;
import city.gui.Lane;
import city.gui.Vehicle;

import transportation.interfaces.Bus;

public class BusGui extends Vehicle{

	/**
	 * @param args
	 */
	List<BusStop> busStops = new ArrayList<BusStop>();
	Bus busAgent;
	
	public BusGui(int i, int j, int k, int l, ArrayList<Lane> road2, Lane lane, ArrayList<ArrayList<Lane>> allRoads, CityPanel cityPanel){
		super(5, 5, 10, 10, road2, road2.get(0), allRoads, cityPanel,"Bus");
	}
	
	public void msgGoToNextStop(Bus busAgent,BusStop currentStop)
	{
		this.busAgent = busAgent;
		super.driveHere(currentStop.name);
	}
	
	@Override
	public void reachedDestination()
	{
		//busAgent.msgAnimationFinishedArrivedAtStop(null);
	}

}
