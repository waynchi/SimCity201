package housing.interfaces;

import housing.House;
import housing.Item;

import java.util.List;

import people.PeopleAgent;

public interface Resident {
	// Actions

	public void callRepairMan();
	public void giveBrokenItems(List<Item> brokenItems);
	public void thankRepairMan();
	public void cookAtHome();
	public void eatFood();
	public void sleep();
	public void repairMyHomeItems();
	
	//-----------------------------------------------------------//

	// Messages

	public void somethingBroke();
	public void ImHere();
	public void repairDone();
	public void eatAtHome();
	public void foodCooked();
	public void activityComplete();
	public void eatAtRestaurant();
	public void doneEating();

	//-----------------------------------------------------------//

	// Scheduler

	public boolean pickAndExecuteAnAction();

	//-----------------------------------------------------------//

	// Utilities

	public void setRepairMan(RepairMan r);
	public List<Item> getBrokenItems();
	public PeopleAgent getAgent();
	public void setHouse(House h);



}
