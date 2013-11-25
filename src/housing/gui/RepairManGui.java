package housing.gui;

import housing.House;
import housing.HouseType;
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
	
	public enum State {Idle, EnteringHouse, LeavingHouse, Repairing, LeavingApartmentComplex};
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
				goToLocation(d);
				r.activityDone();
			}
			else if (state == State.LeavingHouse) {
				if (targetGui.h.type == HouseType.Apartment) {
					location = Location.ApartmentComplex;
				}
				else {
					location = Location.Outside;
				}
				state = State.Idle;
				xPos = targetGui.entranceCoordinatesExternal.width;
				yPos = targetGui.entranceCoordinatesExternal.height;
				r.activityDone();
			}
			else if (state == State.Repairing) {
				state = State.Idle;
				r.activityDone();
			}
			else if (state == State.LeavingApartmentComplex && location == Location.ApartmentComplex) {
				location = Location.Outside;
				state = State.Idle;
				r.activityDone();
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
		if (location == Location.ApartmentComplex)
			return false;
		else
		return true;
	}
	
	public boolean isPresentInShop() {
		if (location == Location.Shop)
			return true;
		return false;
	}
	
	public void DoLeaveShop() {
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
			Dimension d = g.entranceCoordinatesExternal;
			goToLocation(d);
		}
		else {
			state = State.Idle;
			Dimension d = g.getPosition("DiningTable");
			goToLocation(d);
			r.activityDone();
		}
		targetGui = g;
	}
	
	public void DoLeaveHouse() {
		state = State.LeavingHouse;
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
	
	public void DoGoToApartmentInSamePlaceFromApartment(HouseGui g) {
		this.targetGui = g;
		location = Location.ApartmentComplex;
		state = State.EnteringHouse;
		goToLocation(targetGui.entranceCoordinatesExternal);
	}
	
	public void DoLeaveApartmentComplexToFixFromApartment(HouseGui g) {
		state = State.LeavingApartmentComplex;
		location = Location.ApartmentComplex;
		goToLocation(targetGui.h.a.gui.entranceCoordinates);
		this.targetGui = g;
	}
	
	public void DoGoToHouseInDifferentPlaceToFixFromVilla(HouseGui g) {
		targetGui = g;
	}
	
	public void DoGoToVillaFromVilla(HouseGui g) {
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
}