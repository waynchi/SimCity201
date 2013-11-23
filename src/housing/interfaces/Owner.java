package housing.interfaces;

import housing.House;
import housing.HousingRenterRole;
import housing.HousingOwnerRole.MyHouse;
import people.People;

public interface Owner {
	// Messages

	public void hereIsRent(House h, double money);
	public void hereIsPenalty(House h, double money);
	public void generateRent(MyHouse m);
	public void addRenterToHouse(House h, Renter r);
	public MyHouse getMyHouse(House h);

	//-----------------------------------------------------------//

	// Utilities
	
	public People getAgent();
	public int getTimesRentDue(House h);	
	public int getHousesNumber();
	public void generate(House h);
	public int getTotalRents() ;
	public void addHouse(House h, Renter r);
}
