package housing;

import housing.gui.ResidentGui;
import housing.interfaces.RepairMan;
import housing.interfaces.Resident;
import java.util.concurrent.Semaphore;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import people.People;
import people.PeopleAgent.AgentEvent;
import people.PeopleAgent.AgentState;
import people.PeopleAgent.HungerState;
import people.Role;

public class HousingResidentRole extends Role implements Resident {
	// Data

	protected House house;
	private RepairMan repairMan;
	private RepairStage repairStage;
	private State myState;
	public ResidentGui gui = null;
	public Semaphore activity = new Semaphore(0, true);

	public HousingResidentRole() {
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

//	public void giveBrokenItems(List<Item> brokenItems) {
//		repairMan.thingsAreBroken(house, getBrokenItems());
//		repairStage = RepairStage.BeingRepaired;
//	}
//
//	public void thankRepairMan() {
//		repairMan.thankYou(house);
//		repairStage = RepairStage.None;
//	}

	public void cookAtHome() {
		myState = State.Cooking;
		// Animation implementation.
	}

	public void eatFood() {
		myState = State.Eating;
		// Also needs to call a message to myPerson to change hunger state from hungry.
		// Else "infinite" loop till a different event takes place.
		// Animation implementation.
	}

	public void sleep() {
		myState = State.Sleeping;
		// Animation implementation.
	}

	public void repairMyHomeItems() {
		List<Item> brokenItems = house.getBrokenItems();
//		List<Item> brokenItems = getBrokenItems();
		for (Item i : brokenItems) {
			i.repair();
		}
		brokenItems.clear();
		repairStage = RepairStage.None;
	}
	
	public void watchTV() {
		// Animation
	}
	
	public void doMorningStuff() {
		myState = State.DoingMorningStuff;
		// Poop
		// Take a shower
		// Masturbate
	}
	
	public void read() {
		// Go to study table and read stuff.
	}
	
	public void relaxOnSofa() {
		// Animation of relaxing on sofa.
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
//		repairStage = RepairStage.RepairDone;
		repairStage = RepairStage.None;
		stateChanged();
	}

//	public void eatAtHome() {
//		myState = State.WantToCook;
//		stateChanged();
//	}

	public void foodCooked() {
		myState = State.FoodCooked;
		stateChanged();
	}

	public void doneEating() {
		myState = State.Idle;
		activityComplete();
		stateChanged();
	}
	
	public void activityDone() {
		myState = State.Idle;
		activity.release();
	}

	//-----------------------------------------------------------//

	// Scheduler

	public boolean pickAndExecuteAnAction() {
		if (myPerson.getEvent() == AgentEvent.WakingUp && myState == State.Sleeping) {
			doMorningStuff();
			return true;
		}
		if (myPerson.getEvent() == AgentEvent.GoingToSleep && myState == State.Idle) {
			sleep();
			return true;
		}
		if (myPerson.getState == AgentState.EatingAtHome && myState == State.Idle) {
			cookAtHome();
			return true;
		}
		if (myPerson.getState == AgentState.EatingAtHome && myState == State.FoodCooked && myPerson.getHunger() == HungerState.Hungry) {
			eatFood();
			return true;
		}
		if (this.myPerson != repairMan.getPersonAgent()) {
			if (!house.getBrokenItems().isEmpty() && (repairStage == RepairStage.None || repairStage == RepairStage.NeedsRepair) && (myState != State.Sleeping && myState != State.DoingMorningStuff)) {
				callRepairMan();
				return true;
			}
		}
		else {
			if (!house.getBrokenItems().isEmpty() && (repairStage == RepairStage.None || repairStage == RepairStage.NeedsRepair) && (myState != State.Sleeping && myState != State.DoingMorningStuff)) {
				repairMyHomeItems();
				return true;
			}
		}
		if (myState == State.Idle) {
			Activity a = selectRandomActivity();
			if (a == Activity.RelaxOnSofa) {
				relaxOnSofa();
				return true;
			}
			if (a == Activity.Read) {
				read();
				return true;
			}
			if (a == Activity.WatchTV) {
				watchTV();
				return true;
			}
		}
		return false;
	}

	//-----------------------------------------------------------//

	// Utilities

	public void setRepairMan(HousingRepairManRole r) {
		this.repairMan = r;
	}

//	public List<Item> getBrokenItems() {
//		List<Item> result = new ArrayList<Item>();
//		List<Item> list = house.getItems();
//		for (Item i : list) {
//			if (i.isBroken()) {
//				result.add(i);
//			}
//		}
//		if (this.house.isBroken())
//			result.add(house);
//		return result;
//	}
	
	public People getAgent() {
		return myPerson;
	}
	
	public void setHouse(House h) {
		this.house = h;
	}
	
	public void setRepairMan(RepairMan r) {
		this.repairMan = r;
	}
	
	public void setGui(ResidentGui g) {
		this.gui = g;
	}
	
	public Activity selectRandomActivity() {
		Random generator = new Random();
		int num = generator.nextInt(3);
		if (num == 0)
			return Activity.RelaxOnSofa;
		if (num == 1)
			return Activity.Read;
		return Activity.WatchTV;
	}

	//-----------------------------------------------------------//

	// Helper Data Structures

//	enum RepairStage {None, NeedsRepair, HelpRequested, RepairManIsHere, BeingRepaired, RepairDone};
	enum RepairStage {None, NeedsRepair, HelpRequested, RepairManIsHere};

//	enum State {Idle, WantToSleep, Sleeping, WantToCook, Cooking, FoodCooked, Eating, DoingMorningStuff};
	enum State {Idle, Sleeping, Cooking, FoodCooked, Eating, DoingMorningStuff};
	
	enum Activity {RelaxOnSofa, Read, WatchTV};
}

