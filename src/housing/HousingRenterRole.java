package housing;

import housing.interfaces.Owner;
import housing.interfaces.Renter;
import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import people.People;
import people.PeopleAgent;

public class HousingRenterRole extends HousingResidentRole implements Renter{
	// Data

	private Owner owner;
	private double rent;
	private boolean rentDue;
	private int timesRentDue;
	private int period = 10000;
	private Timer rentTimer;
	private List<Double> penalties = new ArrayList<Double>();
	private boolean testMode = false;

	public HousingRenterRole(double rent) {
		super();
		this.rent = rent;
		rentDue = false;
		timesRentDue = 0;
		rentTimer = new Timer();
	}

	//-----------------------------------------------------------//

	// Actions

	public void payRent() {
		owner.hereIsRent(house, rent);
		timesRentDue -= 1;
		if (timesRentDue == 0)
			rentDue = false;
		((PeopleAgent)myPerson).Money -= rent;
		if (testMode == false) {
			gui.DoUseCellPhone();
			try {
				activity.acquire();
			} catch (InterruptedException e) {}
		}
	}

	public void payPenalty(Double p) {
		double amount = p;
		owner.hereIsPenalty(house, amount);
		((PeopleAgent)myPerson).Money -= amount;
		penalties.remove(p);
		if (testMode == false) {
			gui.DoUseCellPhone();
			try {
				activity.acquire();
			} catch (InterruptedException e) {}
		}
	}

	//-----------------------------------------------------------//

	// Messages

	public void payPenalty(double penalty) {
		penalties.add(new Double(penalty));
		stateChanged();
	}

	public void rentReminder() {
		timesRentDue++;
		rentDue = true;
		stateChanged();
	}

	//-----------------------------------------------------------//

	// Scheduler

	public boolean pickAndExecuteAnAction() {
		if (myState != State.Sleeping || testMode == true) {
			if (rentDue == true && ((PeopleAgent)myPerson).Money >= rent) {
				payRent();
				return true;
			}
			Double p = findPayablePenalty();
			if (p != null) {
				payPenalty(p);
				return true;
			}
		}
		if (testMode == false)
			return super.pickAndExecuteAnAction();
		return false;
	}

	//-----------------------------------------------------------//

	// Utilities

	private Double findPayablePenalty() {
		for (Double i : penalties) {
			if (i <= ((PeopleAgent)myPerson).Money)
				return i;
		}
		return null;
	}
	
	public People getAgent() {
		return super.getAgent();
	}
	
	public void startRentTimer() {
		rentTimer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				rentReminder();
			}
		}, 0, period);
	}
	
	public void setMoney(double m) {
		((PeopleAgent)myPerson).Money = m;
	}
	
	public double getMoney() {
		return ((PeopleAgent)myPerson).Money;
	}
	
	public int getTimesRentDue() {
		return timesRentDue;
	}
	
	public boolean isRentDue() {
		return rentDue;
	}
	
	public List<Double> getPenalties() {
		return penalties;
	}
	
	public void setOwner(Owner o) {
		this.owner = o;
	}
	
	public void testModeOn() {
		testMode = true;
	}
	
	public void testModeOff() {
		testMode = false;
	}
}