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
	public void foodCooked();
	public void activityDone();

	//-----------------------------------------------------------//

	// Utilities

	public void setRepairMan(RepairMan r);
	public People getAgent();
	public void setHouse(House h);



}
