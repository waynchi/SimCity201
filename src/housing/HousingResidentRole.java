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

	public void cookAtHome() {
		myState = State.Cooking;
		gui.DoCook();
		try {
			activity.acquire();
		} catch (InterruptedException e) {}
	}

	public void eatFood() {
		myState = State.Eating;
		gui.DoEat();
	}

	public void sleep() {
		myState = State.Sleeping;
		gui.DoSleep();
	}

	public void repairMyHomeItems() {
		List<Item> brokenItems = house.getBrokenItems();
		for (Item i : brokenItems) {
			i.repair();
		}
		brokenItems.clear();
		repairStage = RepairStage.None;
		System.out.println("Repaired my own items.");
	}
	
	public void watchTV() {
		gui.DoWatchTV();
	}
	
	public void doMorningStuff() {
		myState = State.DoingMorningStuff;
		gui.DoPoop();
		try {
			activity.acquire();
		} catch (InterruptedException e) {}
		
		gui.DoBathe();
		try {
			activity.acquire();
		} catch (InterruptedException e) {}
	}
	
	public void read() {
		gui.DoRead();
	}
	
	public void relaxOnSofa() {
		gui.DoRelaxOnSofa();
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
		repairStage = RepairStage.None;
		stateChanged();
	}

	public void foodCooked() {
		myState = State.FoodCooked;
		activity.release();
		stateChanged();
	}
	
	public void activityDone() {
		myState = State.Idle;
		activity.release();
		stateChanged();
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
		if (myPerson.getState == AgentState.EatingAtHome && myState == State.Idle  && myPerson.getHunger() == HungerState.Hungry) {
			cookAtHome();
			return true;
		}
		if (myPerson.getState == AgentState.EatingAtHome && myState == State.FoodCooked) {
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

	enum RepairStage {None, NeedsRepair, HelpRequested, RepairManIsHere};

	enum State {Idle, Sleeping, Cooking, FoodCooked, Eating, DoingMorningStuff};
	
	enum Activity {RelaxOnSofa, Read, WatchTV};
}

