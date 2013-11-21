package housing.interfaces;

import housing.House;
import housing.RenterRole;
import housing.OwnerRole.MyHouse;
import people.People;

public interface Owner {
	// Messages

	public void hereIsRent(House h, double money);
	public void hereIsPenalty(House h, double money);
	public void generateRent(MyHouse m);
	public void addRenterToHouse(House h, RenterRole r);
	public MyHouse getMyHouse(House h);

	//-----------------------------------------------------------//

	// Scheduler

	public boolean pickAndExecuteAnAction();

	//-----------------------------------------------------------//

	// Utilities
	
	public People getAgent();
	
	public int getTimesRentDue(House h);	
	public int getHousesNumber();
	public void generate(House h);
	public int getTotalRents() ;
	public void addHouse(House h, RenterRole r);
}