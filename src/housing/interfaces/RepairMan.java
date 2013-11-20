package housing.interfaces;

import java.util.List;

import people.PeopleAgent;

import housing.House;
import housing.Item;
import housing.OwnerRole.MyHouse;

public interface RepairMan {
	// Actions

	public void goToHouse(final MyHouse mh);
	public void repairItems(MyHouse mh);

	//-----------------------------------------------------------//

	// Messages

	public void needHelp(House h);
	public void thingsAreBroken(House h, List<Item> brokenItems);
	public void thankYou(House h);
	public void timeToRecheck(MyHouse mh);
	public void salaryArrives();

	//-----------------------------------------------------------//

	// Scheduler

	public boolean pickAndExecuteAnAction();

	//-----------------------------------------------------------//

	// Utilities

	public void pickUpSalaryMoney();
	public MyHouse find(House h);
	public MyHouse findMyHouseByState(HouseState s);
	public void addHouse(House h, Resident r);
	public boolean doesItNeedRepair(House h);
	public boolean anyCurrentHouse();
}
