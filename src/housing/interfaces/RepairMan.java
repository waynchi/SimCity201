package housing.interfaces;

import people.People;
import people.PeopleAgent;
import housing.House;

public interface RepairMan {
	// Messages

	public void needHelp(House h, double money);
	public void salaryArrives();

	//-----------------------------------------------------------//
	
	// Utilities

	public void addHouse(House h, Resident r);
	public boolean doesItNeedRepair(House h);
	public boolean anyCurrentHouse();
	public People getPersonAgent();
	public void setPerson(PeopleAgent p);
}