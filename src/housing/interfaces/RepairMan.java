package housing.interfaces;

import java.util.List;

import people.People;

import housing.House;
import housing.Item;

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
	public void setPerson(People p);
}
