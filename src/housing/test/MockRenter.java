package housing.test;

import java.util.List;

import people.People;
import housing.interfaces.Owner;
import housing.interfaces.Renter;

public class MockRenter implements Renter {

	// Messages
	
	@Override
	public void payPenalty(double penalty) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rentReminder() {
		// TODO Auto-generated method stub
		
	}
	
	//-----------------------------------------------------------//

	// Utilities
	
	@Override
	public People getAgent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startRentTimer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMoney(double m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getMoney() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTimesRentDue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isRentDue() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Double> getPenalties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOwner(Owner o) {
		// TODO Auto-generated method stub
		
	}

}
