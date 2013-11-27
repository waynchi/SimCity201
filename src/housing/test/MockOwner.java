package housing.test;

import people.People;
import housing.House;
import housing.HousingOwnerRole.MyHouse;
import housing.interfaces.Owner;
import housing.interfaces.Renter;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;

public class MockOwner extends MockResident implements Owner {
	
	// Messages
	
	@Override
	public void hereIsRent(House h, double money) {
		log.add(new LoggedEvent("Received rent of $" + money + " from " + h));
	}

	@Override
	public void hereIsPenalty(House h, double money) {
		log.add(new LoggedEvent("Received penalty of $" + money + " from " + h));
	}

	@Override
	public void generateRent(MyHouse m) {
	}

	@Override
	public void addRenterToHouse(House h, Renter r) {
		log.add(new LoggedEvent("Added renter to " + h.toString()));
	}

	@Override
	public MyHouse getMyHouse(House h) {
		return null;
	}
	
	//-----------------------------------------------------------//
	
	// Scheduler
	
	@Override
	public boolean pickAndExecuteAnAction() {
		return false;
	}
	
	//-----------------------------------------------------------//

	// Utilities
	
	@Override
	public People getAgent() {
		return null;
	}

	@Override
	public int getTimesRentDue(House h) {
		return 0;
	}

	@Override
	public int getHousesNumber() {
		return 0;
	}

	@Override
	public void generate(House h) {
	}

	@Override
	public int getTotalRents() {
		return 0;
	}

	@Override
	public void addHouse(House h, Renter r) {
		log.add(new LoggedEvent("Added " + h.toString()));
	}
}