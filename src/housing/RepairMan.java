package housing;

import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import people.PeopleAgent;
import people.Role;

public class RepairMan extends Role {
	// Data

	private List<MyHouse> houses = new ArrayList<MyHouse>();
	private Timer salaryTimer;
	private double salary;
	private double money;
	private double homeMoney;
	private int salaryTime;
	private MyHouse currentHouse = null;

	public RepairMan() {
		super();
	}

	//-----------------------------------------------------------//

	// Actions

	public void goToHouse(final MyHouse mh) {
		// Animation implementation.
		if (mh.h.isLocked()) {
			mh.s = HouseState.ToBeRechecked;
			currentHouse = null;
			// Animation implementation.
			// Going back.
			mh.recheck.schedule(new TimerTask() {
				public void run() {
					timeToRecheck(mh);
				}
			}, 10000);
		}
		else {
			mh.r.ImHere();
			mh.s = HouseState.Reached;
		}
	}

	public void repairItems(MyHouse mh) {
		List<Item> brokenItems = mh.brokenItems;
		for (Item i : brokenItems) {
			i.repair();
		}
		mh.s = HouseState.DoneRepairing;
		mh.r.repairDone();
		mh.brokenItems = null;
	}

	//-----------------------------------------------------------//

	// Messages

	public void needHelp(House h) {
		MyHouse mh = find(h);
		mh.s = HouseState.NeedsRepair;
		stateChanged();
	}

	public void thingsAreBroken(House h, List<Item> brokenItems) {
		MyHouse mh = find(h);
		mh.brokenItems = brokenItems;
		stateChanged();
	}

	public void thankYou(House h) {
		MyHouse mh = find(h);
		mh.s = HouseState.None;
		currentHouse = null;
		stateChanged();
	}

	public void timeToRecheck(MyHouse mh) {
		mh.s = HouseState.NeedsRepair;
		stateChanged();
	}

	// Ensure thread safety for homeMoney.
	public void salaryArrives() {
		homeMoney += salary;
		stateChanged();
	}

	//-----------------------------------------------------------//

	// Scheduler

	public boolean pickAndExecuteAnAction() {
		if (currentHouse == null) {
			currentHouse = findMyHouseByState(HouseState.NeedsRepair);
		}
		if (currentHouse != null) {
			if (currentHouse.s == HouseState.NeedsRepair) {
				goToHouse(currentHouse);
				return true;
			}
			if (currentHouse.s == HouseState.ItemsGiven) {
				repairItems(currentHouse);
				return true;
			}
		}
		return false;
	}

	//-----------------------------------------------------------//

	// Utilities

	// Ensure thread safety for homeMoney.
	public void pickUpSalaryMoney() {
		money += homeMoney;
		homeMoney = 0;
	}

	public MyHouse find(House h) {
		for (MyHouse mh : houses) {
			if (mh.h == h)
				return mh;
		}
		return null;
	}

	public MyHouse findMyHouseByState(HouseState s) {
		for (MyHouse mh : houses) {
			if (mh.s == s)
				return mh;
		}
		return null;
	}

	public void addHouse(House h, Resident r) {
		houses.add(new MyHouse(h, r));
	}
	
	public PeopleAgent getAgent() {
		return myPerson;
	}
	
	public boolean doesItNeedRepair(House h) {
		MyHouse mh = find(h);
		if (mh.s == HouseState.NeedsRepair)
			return true;
		return false;
	}
	
	public boolean anyCurrentHouse() {
		if (currentHouse != null)
			return true;
		return false;
	}

	//-----------------------------------------------------------//

	// Helper Data Structures

	private class MyHouse {
		House h;
		Resident r;
		List<Item> brokenItems;
		HouseState s;
		Timer recheck;

		public MyHouse(House h, Resident r) {
			this.h = h;
			this.r = r;
			s = HouseState.None;
			recheck = new Timer();
		}
	}

	enum HouseState {None, NeedsRepair, ToBeRechecked, Reached, ItemsGiven, DoneRepairing};
}

