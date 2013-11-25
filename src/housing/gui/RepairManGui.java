package housing.gui;

import housing.House;
import housing.HouseType;
import housing.HousingRepairManRole;
import housing.interfaces.RepairMan;

import java.awt.Dimension;
import java.awt.Graphics2D;

public class RepairManGui implements HGui{
	public RepairMan r;
	public RepairShopGui gui;
	int xPos, yPos, xDestination, yDestination;
	public Location location = Location.Shop;
	public State state = State.Idle;
	public HouseGui targetGui;
	public HouseGui ref;
	
	public enum State {Idle, EnteringHouse, LeavingHouse, Repairing, LeavingApartmentComplex, LeavingShop, LeavingJob};
	public enum Location {Outside, Shop, ApartmentComplex, Apartment, Villa};

	public RepairManGui(RepairMan r) {
		this.r = r;
	}
	
	@Override
	public void updatePosition() {
		if (xPos < xDestination)
			xPos++;
		else if (yPos < yDestination)
			yPos++;
		else if (xPos > xDestination)
			xPos--;
		else if (yPos > yDestination)
			yPos--;
		
		if (xPos == xDestination && yPos == yDestination) {
			if (state == State.EnteringHouse && location == Location.ApartmentComplex) {
				location = Location.Apartment;
				state = State.Idle;
				xPos = targetGui.entranceCoordinatesInternal.width;
				yPos = targetGui.entranceCoordinatesInternal.height;
				Dimension d = targetGui.getPosition("DiningTable");
				targetGui.h.a.gui.remove(this);
				goToLocation(d);
				targetGui.add(this);
				r.activityDone();
			}
			else if (state == State.LeavingHouse) {
				if (targetGui.h.type == HouseType.Apartment) {
					location = Location.ApartmentComplex;
					targetGui.h.a.gui.add(this);
				}
				else {
					location = Location.Outside;
				}
				state = State.Idle;
				xPos = targetGui.entranceCoordinatesExternal.width;
				yPos = targetGui.entranceCoordinatesExternal.height;
				targetGui.remove(this);
				r.activityDone();
			}
			else if (state == State.Repairing) {
				state = State.Idle;
				r.activityDone();
			}
			else if (state == State.LeavingApartmentComplex && location == Location.ApartmentComplex) {
				location = Location.Outside;
				state = State.Idle;
				ref.h.a.gui.remove(this);
				r.activityDone();
			}
			else if (state == State.LeavingShop) {
				state = State.Idle;
				location = Location.Outside;
				r.activityDone();
			}
			else if (state == State.LeavingJob) {
				if (location == Location.ApartmentComplex) {
					ref.h.a.gui.remove(this);
				}
				location = Location.Outside;
				state = State.Idle;
				((HousingRepairManRole)r).doneLeaving();
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		if (location != Location.Outside) {
			g.fillOval(xPos, yPos, 15, 15);
		}
	}

	@Override
	public boolean isPresent() {
		return true;
	}
	
	public boolean isPresentInShop() {
		if (location == Location.Shop)
			return true;
		return false;
	}
	
	public void DoLeaveShop(HouseGui g) {
		ref = targetGui;
		targetGui = g;
		state = State.LeavingShop;
		goToLocation(gui.entranceCoordinatesInternal);
	}
	
	public void DoEnterShop() {
		location = Location.Shop;
		state = State.Idle;
		xPos = gui.entranceCoordinatesInternal.width;
		yPos = gui.entranceCoordinatesInternal.height;
		Dimension d = gui.getPosition("Chair");
		goToLocation(d);
		r.activityDone();
	}
	
	public void DoEnterHouse(HouseGui g) {
		House h = g.h;
		if (h.type == HouseType.Apartment) {
			state = State.EnteringHouse;
			location = Location.ApartmentComplex;
			xPos = g.h.a.gui.entranceCoordinates.width;
			yPos = g.h.a.gui.entranceCoordinates.height;
			g.h.a.gui.add(this);
			Dimension d = g.entranceCoordinatesExternal;
			goToLocation(d);
		}
		else {
			state = State.Idle;
			Dimension d = g.getPosition("DiningTable");
			goToLocation(d);
			g.add(this);
			r.activityDone();
		}
		targetGui = g;
	}
	
	public void DoLeaveHouse() {
		state = State.LeavingHouse;
		ref = targetGui;
		goToLocation(targetGui.entranceCoordinatesInternal);
	}
	
	public void goToLocation(Dimension d) {
		xDestination = d.width;
		yDestination = d.height;
	}
	
	public void DoRepairItem(Dimension d) {
		state = State.Repairing;
		xDestination = d.width;
		yDestination = d.height;
	}
	
	public void DoGoToApartmentInSameComplexFromApartment(HouseGui g) {
		this.targetGui = g;
		location = Location.ApartmentComplex;
		state = State.EnteringHouse;
		goToLocation(targetGui.entranceCoordinatesExternal);
	}
	
	public void DoLeaveApartmentComplexToFixFromApartment(HouseGui g) {
		state = State.LeavingApartmentComplex;
		location = Location.ApartmentComplex;
		goToLocation(targetGui.h.a.gui.entranceCoordinates);
		ref = targetGui;
		this.targetGui = g;
	}
	
//	public void DoGoToHouseInDifferentPlaceToFixFromVilla(HouseGui g) {
//		targetGui = g;
//	}
//	
//	public void DoGoToVillaFromVilla(HouseGui g) {
//		targetGui = g;
//	}
	
	public void DoGoToHouseFromVilla(HouseGui g) {
		targetGui = g;
	}
	
	public void DoReturnToShop() {
		if (targetGui.h.type == HouseType.Apartment) {
			goToLocation(targetGui.h.a.gui.entranceCoordinates);
			state = State.LeavingApartmentComplex;
			location = Location.ApartmentComplex;
		}
		else {
			state = State.Idle;
			location = Location.Outside;
		}
	}
	
	public void DoLeaveJob() {
		state = State.LeavingJob;
		if (location == Location.Outside) {
			((HousingRepairManRole)r).doneLeaving();
		}
		else if (location == Location.ApartmentComplex) {
			goToLocation(ref.h.a.gui.entranceCoordinates);
		}
		else if (location == Location.Shop) {
			goToLocation(gui.entranceCoordinatesInternal);
		}
	}
}