package housing.test;

import java.util.List;
import people.People;
import housing.interfaces.Owner;
import housing.interfaces.Renter;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;

public class MockRenter extends MockResident implements Renter {
	
	// Data
	
	EventLog log = new EventLog();
	
	//-----------------------------------------------------------//

	// Messages
	
	@Override
	public void payPenalty(double penalty) {
		log.add(new LoggedEvent("Penalty of $" + penalty + " applied"));
	}

	@Override
	public void rentReminder() {
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
	public void startRentTimer() {
	}

	@Override
	public void setMoney(double m) {
	}

	@Override
	public double getMoney() {
		return 0;
	}

	@Override
	public int getTimesRentDue() {
		return 0;
	}

	@Override
	public boolean isRentDue() {
		return false;
	}

	@Override
	public List<Double> getPenalties() {
		return null;
	}

	@Override
	public void setOwner(Owner o) {
		log.add(new LoggedEvent("Owner added"));
	}
}