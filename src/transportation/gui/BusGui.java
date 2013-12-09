package transportation.gui;
import java.util.*;

import city.gui.CityPanel;
import city.gui.Lane;
import city.gui.VehicleGui;

import transportation.BusStop;
import transportation.interfaces.Bus;

public class BusGui extends VehicleGui{

	/**
	 * @param args
	 */
	
	List<BusStop> busStops;
	public Bus busAgent;
	BusStop nextStop;
	public enum State {idle,arrived,driving};
	State busState;
	InsideBusGui myGui;
	
	public BusGui(InsideBusGui g, int i, int j, int k, int l, ArrayList<Lane> road2, Lane lane, ArrayList<ArrayList<Lane>> allRoads, CityPanel cityPanel){
		super(5, 5, 10, 10, road2, road2.get(0), allRoads, cityPanel,"Bus");
		myGui = g;
		busStops = cityPanel.busStops;
		busState = State.idle;
		
	}
	
	public void msgGoToNextStop(Bus busAgent,BusStop currentStop)
	{
		this.busAgent = busAgent;
		for(int k = 0 ; k < busStops.size() ; k ++)
		{
			if(busStops.get(k) == currentStop)
			{
				if((k+1) != busStops.size())
					this.nextStop = busStops.get(k+1);
				else
					this.nextStop = busStops.get(0);
				break;
			}
		}
		busState = State.driving;
		super.driveHere(nextStop.name);
	}
	
	@Override
	public void reachedDestination()
	{
		if(busState == State.driving)
		{
		busState = State.arrived;
		busAgent.msgAnimationFinishedArrivedAtStop(nextStop);
		}
	}
	
	public InsideBusGui getGui(){
		return myGui;
	}
	
	public void setGui(InsideBusGui g){
		myGui = g;
	}

	public void displayBuilding() {
		// TODO Auto-generated method stub
		
	}

}
