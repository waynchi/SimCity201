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
	
	// Scheduler
	
	public boolean pickAndExecuteAnAction();
	
	//-----------------------------------------------------------//

	// Utilities

	public void setRepairMan(RepairMan r);
	public People getAgent();
	public void setHouse(House h);
	public boolean leisure();
}