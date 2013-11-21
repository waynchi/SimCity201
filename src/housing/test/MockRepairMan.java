package housing.test;

import java.util.List;
import people.People;
import housing.House;
import housing.Item;
import housing.interfaces.RepairMan;
import housing.interfaces.Resident;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;

public class MockRepairMan implements RepairMan {
	
	// Data
	
	EventLog log = new EventLog();
	
	//-----------------------------------------------------------//

	// Messages
	
	@Override
	public void needHelp(House h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void thingsAreBroken(House h, List<Item> brokenItems) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void thankYou(House h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void salaryArrives() {
		// TODO Auto-generated method stub
		
	}
	
	//-----------------------------------------------------------//

	// Utilities
	
	@Override
	public void addHouse(House h, Resident r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean doesItNeedRepair(House h) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean anyCurrentHouse() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public People getPersonAgent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPerson(People p) {
		// TODO Auto-generated method stub
		
	}

}
