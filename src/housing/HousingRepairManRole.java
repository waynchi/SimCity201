package housing;

import housing.gui.RepairManGui;
import housing.interfaces.RepairMan;
import housing.interfaces.Resident;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import people.People;
import people.PeopleAgent;
import people.Role;

public class HousingRepairManRole extends Role implements RepairMan {
	// Data

	private List<MyHouse> houses = new ArrayList<MyHouse>();
	private MyHouse currentHouse = null;
	public Location location = Location.Shop;
	public RepairManGui gui;
	public Semaphore activity = new Semaphore(0, true);
	public MyHouse currentLocationHouse;

	public HousingRepairManRole() {
		super();
	}

	//-----------------------------------------------------------//

	// Actions

	public void repairItems(MyHouse mh) {
		List<Item> brokenItems = mh.h.getBrokenItems();
		for (Item i : brokenItems) {
			i.repair();
		}
		mh.r.repairDone();
		mh.s = HouseState.None;
		currentLocationHouse = currentHouse;
		currentHouse = null;
		// Leaving.
	}
	
	public void leaveShop(MyHouse mh) {
		gui.DoLeaveShop();
		try {
			activity.acquire();
		} catch (InterruptedException e) {}
		location = Location.OutsideFixing;
		myPerson.msgDone("RepairManFixing");
	}
	
	public void goToApartmentInSamePlace(MyHouse mh) {
	}
	
	public void returnToShop() {
		if (currentLocationHouse.h.type == HouseType.Apartment) {	
		}
		else {
		}
		try {
			activity.acquire();
		} catch (InterruptedException e) {}
		location = Location.OutsideReturning;
		myPerson.msgDone("RepairManFixed");
	}
	
	public void leaveApartmentComplexToFix(MyHouse mh) {
		location = Location.OutsideFixing;
		myPerson.msgDone("RepairManFixing");
	}
	
	public void goToHouseInDifferentPlaceToFix(MyHouse mh) {
		location = Location.OutsideFixing;
		myPerson.msgDone("RepairManFixing");
	}
	
	public void goToVilla(MyHouse mh) {
		location = Location.OutsideFixing;
		myPerson.msgDone("RepairManFixing");
	}
	
	public void enterHouse(MyHouse mh) {
		mh.s = HouseState.Reached;
		mh.r.ImHere();
		// Gui stuff.
	}
	
	public void enterShop() {
		location = Location.Shop;
		gui.DoEnterShop();
	}

	//-----------------------------------------------------------//

	// Messages

	public void needHelp(House h, double money) {
		MyHouse mh = find(h);
		mh.s = HouseState.NeedsRepair;
		myPerson.Money += money;
		stateChanged();
	}
	
	public void activityDone() {
		location = Location.OutsideReturning;
		stateChanged();
	}

	//-----------------------------------------------------------//

	// Scheduler

	public boolean pickAndExecuteAnAction() {
		if (location == Location.OutsideReturning && myPerson.getAgentEvent().equals("RepairManArrivedShop")) {
			enterShop();
			return true;
		}
		if (location == Location.OutsideFixing && myPerson.getAgentEvent().equals("RepairManArrived") && currentHouse.s == HouseState.NeedsRepair) {
			enterHouse(currentHouse);
			return true;
		}
		if (currentHouse == null) {
			currentHouse = findMyHouseByState(HouseState.NeedsRepair);
		}
		if (currentHouse != null) {
			if (currentHouse.s == HouseState.Reached) {
				repairItems(currentHouse);
				return true;
			}
			if (location == Location.Shop && currentHouse.s == HouseState.NeedsRepair) {
				leaveShop(currentHouse);
				return true;
			}
			else if (location == Location.Resident && currentHouse.s == HouseState.NeedsRepair) {
				if (currentLocationHouse.h.type == HouseType.Apartment) {
					if (currentHouse.h.type == HouseType.Apartment) {
						if (currentHouse.h.a == currentLocationHouse.h.a) {
							goToApartmentInSamePlace(currentHouse);
							return true;
						}
						else {
							leaveApartmentComplexToFix(currentHouse);
							return true;
						}
					}
					else {
						leaveApartmentComplexToFix(currentHouse);
						return true;
					}
				}
				else {
					if (currentHouse.h.type == HouseType.Apartment) {
						goToHouseInDifferentPlaceToFix(currentHouse);
						return true;
					}
					else {
						goToVilla(currentHouse);
						return true;
					}
				}
			}
		}
		else {
			if (location == Location.Resident) {
				returnToShop();
				return true;
			}
		}
		return false;
	}

	//-----------------------------------------------------------//

	// Utilities

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
	
	public People getAgent() {
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
	
	@Override
	public void setPerson(PeopleAgent p) {
		myPerson = p;
	}

	//-----------------------------------------------------------//

	// Helper Data Structures

	private class MyHouse {
		House h;
		Resident r;
		HouseState s;

		public MyHouse(House h, Resident r) {
			this.h = h;
			this.r = r;
			s = HouseState.None;
		}
	}
	
	public enum HouseState {None, NeedsRepair, Reached}
	public enum Location {Shop, OutsideFixing, OutsideReturning, Resident};
}