package housing.gui;

import housing.interfaces.RepairMan;

import java.awt.Dimension;
import java.awt.Graphics2D;

public class RepairManGui implements HGui{
	public RepairMan r;
	public RepairShopGui gui;
	int xPos, yPos, xDestination, yDestination;
	
	public enum State {EnteringShop};
	public enum Location {};

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
		}
	}

	@Override
	public void draw(Graphics2D g) {
	}

	@Override
	public boolean isPresent() {
		return false;
	}
	
	public void DoLeaveShop() {
	}
	
	public void DoEnterShop() {
	}
	
	private void goToLocation(Dimension d) {
		xDestination = d.width;
		yDestination = d.height;
	}
}