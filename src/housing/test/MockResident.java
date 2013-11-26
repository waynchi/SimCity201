package housing.test;

import java.util.List;
import people.People;
import housing.House;
import housing.Item;
import housing.interfaces.RepairMan;
import housing.interfaces.Resident;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;

public class MockResident implements Resident {
	
	// Data
	
	EventLog log = new EventLog();
	
	//-----------------------------------------------------------//
	
	// Messages

	@Override
	public void somethingBroke() {
	}

	@Override
	public void ImHere() {
		log.add(new LoggedEvent("RepairMan is here."));
	}

	@Override
	public void repairDone() {
		log.add(new LoggedEvent("Repair has been done."));
	}

	@Override
	public void foodCooked() {
	}
	
	//-----------------------------------------------------------//
	
	// Scheduler
	
	@Override
	public boolean pickAndExecuteAnAction() {
		return false;
	}
	
	//-----------------------------------------------------------//

	// Utilities
	
	@Override
	public void setRepairMan(RepairMan r) {
	}

	@Override
	public People getAgent() {
		return null;
	}

	@Override
	public void setHouse(House h) {
	}

	@Override
	public void activityDone() {
	}
	
	@Override
	public boolean leisure() {
		return false;
	}
}