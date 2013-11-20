package housing;

import java.util.concurrent.Semaphore;
import java.util.List;
import java.util.ArrayList;
import people.PeopleAgent;
import people.Role;

public class ResidentRole extends Role{
	// Data

	protected House house;
	private RepairManRole repairMan;
	private RepairStage repairStage;
	private State myState;
	private Semaphore busy = new Semaphore(0, true);

	public ResidentRole() {
		house = null;
		repairMan = null;
		repairStage = RepairStage.None;
		myState = State.Idle;
	}

	//-----------------------------------------------------------//

	// Actions

	public void callRepairMan() {
		repairMan.needHelp(house);
		repairStage = RepairStage.HelpRequested;
	}

	public void giveBrokenItems(List<Item> brokenItems) {
		repairMan.thingsAreBroken(house, getBrokenItems());
		repairStage = RepairStage.BeingRepaired;
	}

	public void thankRepairMan() {
		repairMan.thankYou(house);
		repairStage = RepairStage.None;
	}

	public void cookAtHome() {
		myState = State.Cooking;
		// Animation implementation.
	}

	public void eatFood() {
		myState = State.Eating;
		// Animation implementation.
	}

	public void sleep() {
		myState = State.Sleeping;
		// Animation implementation.
	}

	public void repairMyHomeItems() {
		List<Item> brokenItems = getBrokenItems();
		for (Item i : brokenItems) {
			i.repair();
		}
		repairStage = RepairStage.None;
	}

	//-----------------------------------------------------------//

	// Messages

	public void somethingBroke() {
		if (repairStage == RepairStage.None) {
			repairStage = RepairStage.NeedsRepair;
		}
		stateChanged();
	}

	public void ImHere() {
		repairStage = RepairStage.RepairManIsHere;
		stateChanged();
	}

	public void repairDone() {
		repairStage = RepairStage.RepairDone;
		stateChanged();
	}

	public void eatAtHome() {
		myState = State.WantToCook;
		stateChanged();
	}

	public void foodCooked() {
		myState = State.FoodCooked;
		stateChanged();
	}

	public void activityComplete() {
		busy.release();
		stateChanged();
	}

	public void eatAtRestaurant() {
		myState = State.WantToEatAtRestaurant;
		stateChanged();
	}

	public void doneEating() {
		myState = State.Idle;
		activityComplete();
		stateChanged();
	}

	//-----------------------------------------------------------//

	// Scheduler

	public boolean pickAndExecuteAnAction() {
		if (this.myPerson != repairMan.getAgent()) {
			if (repairStage == RepairStage.RepairDone) {
				thankRepairMan();
				return true;
			}
			if (repairStage == RepairStage.RepairManIsHere) {
				giveBrokenItems(getBrokenItems());
				return true;
			}
			if (!getBrokenItems().isEmpty() && (repairStage == RepairStage.None || repairStage == RepairStage.NeedsRepair) && myState != State.Sleeping) {
				callRepairMan();
				return true;
			}
		}
		else {
			if (!getBrokenItems().isEmpty() && (repairStage == RepairStage.None || repairStage == RepairStage.NeedsRepair) && myState != State.Sleeping) {
				repairMyHomeItems();
				return true;
			}
		}
		if (myState == State.FoodCooked) {
			eatFood();
			return true;
		}
		if (myState == State.WantToCook) {// And grill is not broken
			cookAtHome();
			return true;
		}
		if (myState == State.WantToSleep && repairStage != RepairStage.RepairManIsHere && repairStage != RepairStage.BeingRepaired && repairStage != RepairStage.RepairDone){
			sleep();
			return true;
		}
		return false;
	}

	//-----------------------------------------------------------//

	// Utilities

	public void setRepairMan(RepairManRole r) {
		this.repairMan = r;
	}

	public List<Item> getBrokenItems() {
		List<Item> result = new ArrayList<Item>();
		List<Item> list = house.getItems();
		for (Item i : list) {
			if (i.isBroken()) {
				result.add(i);
			}
		}
		if (this.house.isBroken())
			result.add(house);
		return result;
	}
	
	public PeopleAgent getAgent() {
		return myPerson;
	}
	
	public void setHouse(House h) {
		this.house = h;
	}

	//-----------------------------------------------------------//

	// Helper Data Structures

	enum RepairStage {None, NeedsRepair, HelpRequested, RepairManIsHere, BeingRepaired, RepairDone};

	enum State {Idle, WantToSleep, Sleeping, WantToCook, Cooking, FoodCooked, Eating, WantToEatAtRestaurant};
}

