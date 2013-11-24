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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ImHere() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void repairDone() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void foodCooked() {
		// TODO Auto-generated method stub
		
	}
	
	//-----------------------------------------------------------//

	// Utilities
	
	@Override
	public void setRepairMan(RepairMan r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public People getAgent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setHouse(House h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activityDone() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void leftHouse() {
		// TODO Auto-generated method stub
		
	}

}
