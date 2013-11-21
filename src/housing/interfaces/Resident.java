package housing.interfaces;

import housing.House;
import housing.Item;
import java.util.List;
import people.People;

public interface Resident {
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

	// Utilities

	public void setRepairMan(RepairMan r);
	public List<Item> getBrokenItems();
	public People getAgent();
	public void setHouse(House h);



}
