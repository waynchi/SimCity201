package housing;

import housing.HousingResidentRole.State;
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
	public boolean isActive;
	public boolean needToLeave = false;

	public HousingRepairManRole() {
		super();
		gui = new RepairManGui(this);
	}

	//-----------------------------------------------------------//

	// Actions

	public void repairItems(MyHouse mh) {
		List<Item> brokenItems = mh.h.getBrokenItems();
		for (Item i : brokenItems) {
			gui.DoRepairItem(mh.h.gui.getPosition(i.name));
			try {
				activity.acquire();
			} catch (InterruptedException e) {}
			i.repair();
		}
		mh.r.repairDone();
		mh.s = HouseState.None;
		currentLocationHouse = currentHouse;
		currentHouse = null;
		gui.DoLeaveHouse();
		try {
			activity.acquire();
		} catch (InterruptedException e) {}
	}
	
	public void leaveShop(MyHouse mh) {
		gui.DoLeaveShop(mh.h.gui);
		try {
			activity.acquire();
		} catch (InterruptedException e) {}
		location = Location.OutsideFixing;
		myPerson.msgDone("RepairManFixing");
	}
	
	public void goToApartmentInSameComplexFromApartment(MyHouse mh) {
		gui.DoGoToApartmentInSameComplexFromApartment(mh.h.gui);
		try {
			activity.acquire();
		} catch (InterruptedException e) {}
		mh.r.ImHere();
	}
	
	public void returnToShop() {
		gui.DoReturnToShop();
		try {
			activity.acquire();
		} catch (InterruptedException e) {}
		location = Location.OutsideReturning;
		myPerson.msgDone("RepairManFixed");
	}
	
	public void leaveApartmentComplexToFixFromApartment(MyHouse mh) {
		gui.DoLeaveApartmentComplexToFixFromApartment(mh.h.gui);
		try {
			activity.acquire();
		} catch (InterruptedException e) {}
		location = Location.OutsideFixing;
		myPerson.msgDone("RepairManFixing");
	}
	
//	public void goToHouseInDifferentPlaceToFixFromVilla(MyHouse mh) {
//		gui.DoGoToHouseInDifferentPlaceToFixFromVilla(mh.h.gui);
//		location = Location.OutsideFixing;
//		myPerson.msgDone("RepairManFixing");
//	}
//	
//	public void goToVillaFromVilla(MyHouse mh) {
//		gui.DoGoToVillaFromVilla(mh.h.gui);
//		location = Location.OutsideFixing;
//		myPerson.msgDone("RepairManFixing");
//	}
	
	public void goToHouseFromVilla(MyHouse mh) {
		gui.DoGoToHouseFromVilla(mh.h.gui);
		location = Location.OutsideFixing;
		myPerson.msgDone("RepairManFixing");
	}
	
	public void enterHouse(MyHouse mh) {
		gui.DoEnterHouse(mh.h.gui);
		try {
			activity.acquire();
		} catch (InterruptedException e) {}
		location = Location.Resident;
		mh.s = HouseState.Reached;
		mh.r.ImHere();
	}
	
	public void enterShop() {
		location = Location.Shop;
		gui.DoEnterShop();
	}
	
	public void leaveJob() {
		gui.DoLeaveJob();
		try {
			activity.acquire();
		} catch (InterruptedException e) {}
		location = Location.Nowhere;
		isActive = false;
		needToLeave = false;
		myPerson.msgDone("RepairManRole");
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
		activity.release();
	}
	
	public void msgIsActive() {
		isActive = true;
		stateChanged();
	}
	
	public void msgIsInActive() {
		needToLeave = true;
		stateChanged();
	}
	
	public void doneLeaving() {
		activity.release();
	}

	//-----------------------------------------------------------//

	// Scheduler

	public boolean pickAndExecuteAnAction() {
		if (location == Location.Nowhere && isActive == true) {
			enterShop();
			return true;
		}
		if (location == Location.OutsideReturning && myPerson.getAgentEvent().equals("RepairManArrivedShop")) {
			enterShop();
			return true;
		}
		if (location == Location.OutsideFixing && myPerson.getAgentEvent().equals("RepairManArrived") && currentHouse.s == HouseState.NeedsRepair) {
			enterHouse(currentHouse);
			return true;
		}
		if ((currentHouse == null || currentHouse.s != HouseState.Reached) && (location != Location.OutsideFixing || location != Location.OutsideReturning) && needToLeave == true) {
			leaveJob();
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
							goToApartmentInSameComplexFromApartment(currentHouse);
							return true;
						}
						else {
							leaveApartmentComplexToFixFromApartment(currentHouse);
							return true;
						}
					}
					else {
						leaveApartmentComplexToFixFromApartment(currentHouse);
						return true;
					}
				}
				else {
//					if (currentHouse.h.type == HouseType.Apartment) {
//						goToHouseInDifferentPlaceToFixFromVilla(currentHouse);
//						return true;
//					}
//					else {
//						goToVillaFromVilla(currentHouse);
//						return true;
//					}
					goToHouseFromVilla(currentHouse);
					return true;
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
	public enum Location {Shop, OutsideFixing, OutsideReturning, Resident, Nowhere};
}