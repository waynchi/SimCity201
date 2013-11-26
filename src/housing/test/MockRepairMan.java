package housing.test;

import java.util.List;
import people.People;
import people.PeopleAgent;
import people.Role;
import housing.House;
import housing.Item;
import housing.interfaces.RepairMan;
import housing.interfaces.Resident;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;

public class MockRepairMan extends Role implements RepairMan {
	
	// Data
	
	EventLog log = new EventLog();
	
	//-----------------------------------------------------------//

	// Messages
	
	@Override
	public void needHelp(House h, double money) {
	}
	
	@Override
	public void activityDone() {
	}
	
	//-----------------------------------------------------------//

	// Utilities
	
	@Override
	public void addHouse(House h, Resident r) {	
	}

	@Override
	public boolean doesItNeedRepair(House h) {
		return false;
	}

	@Override
	public boolean anyCurrentHouse() {
		return false;
	}

	@Override
	public People getPersonAgent() {
		return null;
	}

	@Override
	public void setPerson(PeopleAgent p) {	
	}
}