package housing;

import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import people.PeopleAgent;

public class Renter extends Resident {
	// Data

	private Owner owner;
	private double money;
	private double rent;
	private boolean rentDue;
	private int timesRentDue;
	private int period = 10000;
	private Timer rentTimer;
	private List<Double> penalties = new ArrayList<Double>();

	public Renter(double rent) {
		super();
		this.rent = rent;
		rentDue = false;
		timesRentDue = 0;
		rentTimer = new Timer();
//		startRentTimer();
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
	
	public void startRentTimer() {
		rentTimer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				rentReminder();
			}
		}, 0, period);
	}
	
	public void setMoney(double m) {
		this.money = m;
	}
	
	public double getMoney() {
		return money;
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
}

