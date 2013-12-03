package transportation;

import java.util.ArrayList;

import city.gui.CityPanel;
import city.gui.Lane;
import city.gui.VehicleGui;
import transportation.interfaces.Car;

public class CarGui extends VehicleGui{

	
	String destination;
	Car carAgent;
	
	public CarGui(int i, int j, int k, int l, ArrayList<Lane> road2, Lane lane, ArrayList<ArrayList<Lane>> allRoads, CityPanel cityPanel){
		super(5, 5, 10, 10, road2, road2.get(0), allRoads, cityPanel,"Car");
	}
	
	public void msgGoToThisPlace(Car carAgent,String place)
	{
		System.out.println("CarGui recieved message to go to " + place);
		this.destination = place;
		this.carAgent = carAgent;
		super.driveHere(place);
	}
	@Override
	public void reachedDestination(){
		carAgent.msgAnimationFinishedArrivedAtDestination(destination);
	}

}
