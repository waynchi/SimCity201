import java.util.concurrent.Semaphore;
import java.util.List;
import java.util.ArrayList;

public class Resident extends Role{
	// Data

	protected House house;
	private RepairMan repairMan;
	private RepairState repairState;
	private State myState;
	private Semaphore busy = new Semaphore(0, true);

	public Resident() {
		house = null;
		repairMan = null;
		repairState = RepairState.None;
		myState = State.Idle;
	}

	//-----------------------------------------------------------//

	// Actions

	public void callRepairMan() {
		repairMan.needHelp(house);
		repairState = RepairState.HelpRequested;
	}

	public void giveBrokenItems(List<Item> brokenItems) {
		repairMan.thingsAreBroken(house, getBrokenItems());
		repairState = RepairState.BeingRepaired;
	}

	public void thankRepairMan() {
		repairMan.thankYou(house);
		repairState = RepairState.None;
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
		repairState = RepairState.None;
	}

	//-----------------------------------------------------------//

	// Messages

	public void somethingBroke() {
		if (repairState == RepairState.None) {
			repairState = RepairState.NeedsRepair;
		}
		stateChanged();
	}

	public void ImHere() {
		repairState = RepairState.RepairManIsHere;
		stateChanged();
	}

	public void repairDone() {
		repairState = RepairState.RepairDone;
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
		if (!(this instanceof RepairMan)) {
			if (repairState == RepairState.RepairDone) {
				thankRepairMan();
				return true;
			}
			if (repairState == RepairState.RepairManIsHere) {
				giveBrokenItems(getBrokenItems());
				return true;
			}
			if (!getBrokenItems().isEmpty() && (repairState == RepairState.None || repairState == RepairState.NeedsRepair) && myState != State.Sleeping) {
				callRepairMan();
				return true;
			}
		}
		else {
			if (!getBrokenItems().isEmpty() && (repairState == RepairState.None || repairState == RepairState.NeedsRepair) && myState != State.Sleeping) {
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
		if (myState == State.WantToSleep && repairState != RepairState.RepairManIsHere && repairState != RepairState.BeingRepaired && repairState != RepairState.RepairDone){
			sleep();
			return true;
		}
		return false;
	}

	//-----------------------------------------------------------//

	// Utilities

	public void setRepairMan(RepairMan r) {
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
		return result;
	}

	//-----------------------------------------------------------//

	// Helper Data Structures

	enum RepairState {None, NeedsRepair, HelpRequested, RepairManIsHere, BeingRepaired, RepairDone};

	enum State {Idle, WantToSleep, Sleeping, WantToCook, Cooking, FoodCooked, Eating, WantToEatAtRestaurant};
}
