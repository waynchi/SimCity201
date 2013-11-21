package housing.test;

import people.People;
import housing.House;
import housing.OwnerRole.MyHouse;
import housing.interfaces.Owner;
import housing.interfaces.Renter;

public class MockOwner implements Owner {

	// Messages
	
	@Override
	public void hereIsRent(House h, double money) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hereIsPenalty(House h, double money) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generateRent(MyHouse m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRenterToHouse(House h, Renter r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MyHouse getMyHouse(House h) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//-----------------------------------------------------------//

	// Utilities
	
	@Override
	public People getAgent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTimesRentDue(House h) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHousesNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void generate(House h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getTotalRents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addHouse(House h, Renter r) {
		// TODO Auto-generated method stub
		
	}

}
