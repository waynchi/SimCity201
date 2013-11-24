package housing.interfaces;

import people.People;
import people.PeopleAgent;
import housing.House;

public interface RepairMan {
	// Messages

	public void needHelp(House h);
//	public void thingsAreBroken(House h, List<Item> brokenItems);
//	public void thankYou(House h);
	public void salaryArrives();

	//-----------------------------------------------------------//
	
	// Utilities

	public void addHouse(House h, Resident r);
	public boolean doesItNeedRepair(House h);
	public boolean anyCurrentHouse();
	public People getPersonAgent();
	public void setPerson(PeopleAgent p);
}