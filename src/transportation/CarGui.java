package transportation;

import java.util.ArrayList;

import people.People;

import city.gui.CityPanel;
import city.gui.Lane;
import city.gui.VehicleGui;
import transportation.interfaces.Car;

public class CarGui extends VehicleGui{

	
	String destination;
	Car carAgent;
	People person;
	
	public CarGui(int i, int j, int k, int l, ArrayList<Lane> road, Lane lane, ArrayList<ArrayList<Lane>> allRoads, CityPanel cityPanel){
		super(5, 5, 10, 10, road, lane, allRoads, cityPanel,"Car");
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
		if(carAgent != null)
		carAgent.msgAnimationFinishedArrivedAtDestination(destination);
	}

	public void setPersonAgent(People p)
	{
		person = p;
	}
	
	@Override
	public void tellPersonGuiToWalk(){
		person.setHasCar(false);
		person.getPersonGui().setDestination(destination);
		person.getPersonGui().setSideWalkSegment(cityPanel.allSidewalks.get(8));
		person.getPersonGui().setSidewalk(cityPanel.allSidewalks.get(8).get(9));
		person.getPersonGui().setDirection("left");
		cityPanel.people.add(person.getPersonGui());
	}
	
	public void setCarDestination(String destination){
		this.destination = destination;
		super.driveHere(destination);
	}
	
	@Override
	public void stopNow(){
		simulatingCrash = true;
		this.currentCell.simulatingCrash = true;
	}

}
