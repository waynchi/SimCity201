package housing;

import housing.gui.ResidentGui;
import housing.interfaces.RepairMan;
import housing.interfaces.Resident;
import java.util.concurrent.Semaphore;
import java.util.List;
import java.util.Random;
import people.People;
import people.PeopleAgent;
import people.Role;

public class HousingResidentRole extends Role implements Resident {
	// Data

	protected House house;
	private RepairMan repairMan;
	private RepairStage repairStage;
	protected State myState;
	public Location location = Location.Home;
	private boolean leisure = false;
	protected boolean isActive = true;
	private boolean needToLeave = false;
	public ResidentGui gui = null;
	public Semaphore activity = new Semaphore(0, true);
	public boolean testMode = false;

	public HousingResidentRole() {
		house = null;
		repairMan = null;
		repairStage = RepairStage.None;
		myState = State.Sleeping;
		gui = new ResidentGui(this);
	}

	//-----------------------------------------------------------//

	// Actions

	public void callRepairMan() {
		repairMan.needHelp(house, 20);
		repairStage = RepairStage.HelpRequested;
		if (testMode == false) {
			gui.DoUseCellPhone();
			try {
				activity.acquire();
			} catch (InterruptedException e) {}
		}
		myState = State.Idle;
	}

	public void cookAtHome() {
		myState = State.Cooking;
		if (testMode == false) {
			gui.DoCook();
			try {
				activity.acquire();
			} catch (InterruptedException e) {}
		}
		myState = State.Idle;
	}

	public void eatFood() {
		myState = State.Eating;
		if (testMode == false) {
			gui.DoEat();
			try {
				activity.acquire();
			} catch (InterruptedException e) {}
		}
		this.myState = State.Idle;
		myPerson.msgDone("DoneEating");
	}

	public void sleep() {
		myState = State.Sleeping;
		if (testMode == false)
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
		if (testMode == false)
			gui.DoWatchTV();
	}
	
	public void doMorningStuff() {
		myState = State.DoingMorningStuff;
		if (testMode == false) {
			gui.DoPoop();
			try {
				activity.acquire();
			} catch (InterruptedException e) {}
		}
		myState = State.Idle;
		
		if (testMode == false) {
			gui.DoBathe();
			try {
				activity.acquire();
			} catch (InterruptedException e) {}
		}
		myState = State.Idle;
	}
	
	public void read() {
		if (testMode == false)
			gui.DoRead();
	}
	
	public void relaxOnSofa() {
		if (testMode == false)
			gui.DoRelaxOnSofa();
	}
	
	public void playVideoGames() {
		if (testMode == false)
			gui.DoPlayVideoGames();
	}
	
	public void playFussball() {
		if (testMode == false)
			gui.DoPlayFussball();
	}
	
	public void leaveHome() {
		leisure = false;
		needToLeave = false;
		isActive = false;
		if (testMode == false) {
			gui.DoLeaveHome();
			try {
				activity.acquire();
			} catch (InterruptedException e) {}
		}
		myState = State.Idle;
		myPerson.msgDone("ResidentRole");
	}
	
	public void enterHome() {
		if (testMode == false) {
			gui.DoEnterHome();
			try {
				activity.acquire();
			} catch (InterruptedException e) {}
		}
		myState = State.Idle;
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
	
	public void leftHouse() {
		myState = State.Idle;
		activity.release();
		isActive = false;
		if (myPerson != null) {
			myPerson.msgDone("Resident");
		}
		stateChanged();
	}
	
	@Override
	public void msgIsActive() {
		myState = State.Entering;
		isActive = true;
		stateChanged();
	}
	
	@Override
	public void msgIsInActive() {
		needToLeave = true;
		stateChanged();
	}

	//-----------------------------------------------------------//

	// Scheduler

	public boolean pickAndExecuteAnAction() {
		if(myState == State.Entering) {
			enterHome();
			return true;
		}
		if (((PeopleAgent)myPerson).getAgentEvent().equals("WakingUp") && myState == State.Sleeping) {
			doMorningStuff();
			leisure = false;
			return true;
		}
		if (((PeopleAgent)myPerson).getAgentEvent().equals("GoingToSleep") && myState == State.Idle) {
			sleep();
			leisure = false;
			return true;
		}
		if (needToLeave == true) {
			leaveHome();
			return true;
		}
		if (((PeopleAgent)myPerson).getAgentState().equals("EatingAtHome") && myState == State.Idle  && ((PeopleAgent)myPerson).getHunger().equals("Eating")) {
			cookAtHome();
			leisure = false;
			return true;
		}
		if (((PeopleAgent)myPerson).getAgentState().equals("EatingAtHome") && myState == State.FoodCooked) {
			eatFood();
			leisure = false;
			return true;
		}
		if (this.myPerson != repairMan.getPersonAgent()) {
			if (!house.getBrokenItems().isEmpty() && (repairStage == RepairStage.None || repairStage == RepairStage.NeedsRepair) && (myState != State.Sleeping && myState != State.DoingMorningStuff)) {
				callRepairMan();
				leisure = false;
				return true;
			}
		}
		else {
			if (!house.getBrokenItems().isEmpty() && (repairStage == RepairStage.None || repairStage == RepairStage.NeedsRepair) && (myState != State.Sleeping && myState != State.DoingMorningStuff)) {
				repairMyHomeItems();
				leisure = false;
				return true;
			}
		}
		if (myState == State.Idle && leisure == false) {
			leisure = true;
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
			if (a == Activity.PlayVideoGames) {
				playVideoGames();
				return true;
			}
			if (a == Activity.PlayFussball) {
				playFussball();
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
		h.gui.add(gui);
	}
	
	public void setRepairMan(RepairMan r) {
		this.repairMan = r;
	}
	
	public void setGui(ResidentGui g) {
		this.gui = g;
	}
	
	public Activity selectRandomActivity() {
		Random generator = new Random();
		int t;
		if (house.type == HouseType.Villa)
			t = 5;
		else
			t = 4;
		int num = generator.nextInt(t);
		if (num == 0)
			return Activity.RelaxOnSofa;
		if (num == 1)
			return Activity.Read;
		if (num == 2)
			return Activity.WatchTV;
		if (num == 3)
			return Activity.PlayVideoGames;
		return Activity.PlayFussball;
	}
	
	public boolean isActive() {
		return isActive();
	}
	
	public void testModeOn() {
		testMode = true;
	}
	
	public void testModeOff() {
		testMode = false;
	}

	//-----------------------------------------------------------//

	// Helper Data Structures

	enum RepairStage {None, NeedsRepair, HelpRequested, RepairManIsHere};

	protected enum State {Idle, Sleeping, Cooking, FoodCooked, Eating, DoingMorningStuff, Entering};
	
	enum Activity {RelaxOnSofa, Read, WatchTV, PlayVideoGames, PlayFussball};
	
	public enum Location {Home, NotHome};
}