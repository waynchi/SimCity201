package housing;

import housing.interfaces.Owner;
import housing.interfaces.Renter;
import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import people.People;
import people.PeopleAgent;

public class HousingOwnerRole extends HousingResidentRole implements Owner {
	// Data

	private List<MyHouse> myHouses = new ArrayList<MyHouse>();
	private List<RentOrder> rents = new ArrayList<RentOrder>();
	private double PEN_INCREMENT = 50.0;
	private boolean testMode = false;

	public HousingOwnerRole() {
		super();
	}

	//-----------------------------------------------------------//

	// Actions

	public void applyPenalty(RentOrder ro) {
		ro.mh.penalty += PEN_INCREMENT;
		ro.mh.r.payPenalty(PEN_INCREMENT);
		ro.s = RentOrderState.AppliedPenalty;
		if (testMode == false) {
			gui.DoUseCellPhone();
			try {
				activity.acquire();
			} catch (InterruptedException e) {}
		}
	}

	public void applyPenaltyAndRemove(RentOrder ro) {
		ro.mh.penalty += PEN_INCREMENT;
		ro.mh.r.payPenalty(PEN_INCREMENT);
		rents.remove(ro);
		if (testMode == false) {
			gui.DoUseCellPhone();
			try {
				activity.acquire();
			} catch (InterruptedException e) {}
		}
	}

	//-----------------------------------------------------------//

	// Messages

	public void hereIsRent(House h, double money) {
		MyHouse m = find(h);
		((PeopleAgent)myPerson).Money += money;
		for (RentOrder ro : rents) {
			if (ro.mh == m && ro.s == RentOrderState.Due) {
				rents.remove(ro);
				return;
			}
		}
		for (RentOrder ro : rents) {
			if (ro.mh == m && ro.s == RentOrderState.AppliedPenalty) {
				rents.remove(ro);
				return;
			}
		}
		for (RentOrder ro : rents) {
			if (ro.mh == m && ro.s == RentOrderState.ApplyPenalty) {
				ro.s = RentOrderState.ApplyPenaltyAndRemove;
				return;
			}
		}
	}

	public void hereIsPenalty(House h, double money) {
		MyHouse mh = find(h);
		((PeopleAgent)myPerson).Money += money;
		mh.penalty -= money;
	}

	public void generateRent(MyHouse m) {
		for (RentOrder ro : rents) {
			if (ro.mh == m && ro.s == RentOrderState.Due) {
				ro.s = RentOrderState.ApplyPenalty;
			}
		}
		rents.add(new RentOrder(m));
	}
	
	public void addRenterToHouse(House h, Renter r) {
		MyHouse mh = find(h);
		mh.setOccupant(r);
	}
	
	public MyHouse getMyHouse(House h) {
		MyHouse mh = find(h);
		return mh;
	}

	//-----------------------------------------------------------//

	// Scheduler

	public boolean pickAndExecuteAnAction() {
		if (super.myState != State.Sleeping || testMode == true) {
			RentOrder ro =  null;

			ro = findRentOrderByState(RentOrderState.ApplyPenaltyAndRemove);
			if (ro != null) {
				applyPenaltyAndRemove(ro);
				return true;
			}

			ro = findRentOrderByState(RentOrderState.ApplyPenalty);
			if (ro != null) {
				applyPenalty(ro);
				return true;
			}
		}
		if (testMode == false)
			return super.pickAndExecuteAnAction();
		return false;
	}

	//-----------------------------------------------------------//

	// Utilities

	private MyHouse find(House h1) {
		for (MyHouse mh : myHouses) {
			if (h1 == mh.h) {
				return mh;
			}
		}
		return null;
	}

	public RentOrder findRentOrderByState(RentOrderState s1) {
		for (RentOrder ro : rents) {
			if (ro.s == s1)
				return ro;
		}
		return null;
	}
	
	public People getAgent() {
		return super.getAgent();
	}
	
	public int getTimesRentDue(House h) {
		MyHouse mh = find(h);
		int result = 0;
		for (RentOrder ro : rents) {
			if (ro.mh == mh) {
				result++;
			}
		}
		return result;
	}
	
	public int getHousesNumber() {
		return myHouses.size();
	}
	
	public void generate(House h) {
		MyHouse mh = find(h);
		generateRent(mh);
	}
	
	public int getTotalRents() {
		return rents.size();
	}
	
	public void addHouse(House h, Renter r) {
		MyHouse mh = new MyHouse(h, r);
		myHouses.add(mh);
	}
	
	public void testModeOn() {
		testMode = true;
	}
	
	public void testModeOff() {
		testMode = false;
	}

	//-----------------------------------------------------------//

	// Helper Data Structures

	public class MyHouse {
		House h;
		Renter r = null;
		double penalty;
		double rent;
		Timer rentTimer;
		long period;

		public MyHouse(House h, Renter r) {
			this.h = h;
			this.period = 10000;
			this.r = r;
			this.penalty = 0.0;
			rent = 200.0;
			rentTimer = new Timer();
			if (testMode == false) {
				final MyHouse mh = this;
				if (r != null) {
					rentTimer.scheduleAtFixedRate(new TimerTask() {
						public void run() {
							generateRent(mh);
						}
					}, period, period);
				}
			}
		}
		
		public void setOccupant(Renter r) {
			this.r = r;
			if (testMode == false) {
				final MyHouse mh = this;
				rentTimer.scheduleAtFixedRate(new TimerTask() {
					public void run() {
						generateRent(mh);
					}
				}, period, period);
			}
		}
	}

	private class RentOrder {
		MyHouse mh;
		RentOrderState s;

		public RentOrder(MyHouse mh) {
			this.mh = mh;
			s = RentOrderState.Due;
		}
	}

	public enum RentOrderState {Due, ApplyPenalty, ApplyPenaltyAndRemove, AppliedPenalty};
}