package housing;

import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import people.PeopleAgent;

public class Renter extends Resident {
	// Data

	private Owner owner;
	private double money;
	private double rent;
	private boolean rentDue;
	private int timesRentDue;
	private Timer rentTimer;
	private List<Double> penalties = new ArrayList<Double>();

	public Renter(double rent) {
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
		money -= rent;
	}

	public void payPenalty(Double p) {
		double amount = p;
		owner.hereIsPenalty(house, amount);
		money -= amount;
		penalties.remove(p);
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

	public boolean pickAnExecuteAnAction() {
		if (rentDue == true && money >= rent) {
			payRent();
			return true;
		}
		Double p = findPayablePenalty();
		if (p != null) {
			payPenalty(p);
			return true;
		}
		return super.pickAndExecuteAnAction();
	}

	//-----------------------------------------------------------//

	// Utilities

	private Double findPayablePenalty() {
		for (Double i : penalties) {
			if (i <= money)
				return i;
		}
		return null;
	}
	
	public PeopleAgent getAgent() {
		return super.getAgent();
	}

	//-----------------------------------------------------------//

	// Helper Data Structures

	// Create a class that extends Thread. Make it run in the same way as the
	// Agent code does. Modify its run() method. Make it use the rentTimer
	// to generate reminder messages.
}

