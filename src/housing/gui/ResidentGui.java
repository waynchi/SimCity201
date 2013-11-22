package housing.gui;

import housing.interfaces.Resident;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Random;

public class ResidentGui implements HGui{
	int xDestination;
	int yDestination;
	int xPos;
	int yPos;
	State state;
	
	enum State {};
	
	// Use timers to implement cooking, and then call
	// r.activityDone().
	// Also, use timers to implement eating, then call
	// r.activityDone().
	
	public Resident r;
	HouseGui hGui;
	
	public ResidentGui(Resident r) {
		this.r = r;
	}

	@Override
	public void updatePosition() {
		if (xPos < xDestination)
			xPos++;
		if (yPos < yDestination)
			yPos++;
		if (xPos > xDestination)
			xPos--;
		if (yPos > yDestination)
			yPos--;
		
		if (xPos == xDestination && yPos == yDestination) {
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillOval(xPos, yPos, 5, 5);
	}

	@Override
	public boolean isPresent() {
		return false;
	}
	
	public void setHouseGui(HouseGui hGui) {
		this.hGui = hGui;
	}
	
	public void DoPoop() {
	}
	
	public void DoBathe() {
	}

	public void DoPee() {
	}
	
	public void DoWatchTV() {
		Random generator = new Random();
		int num = generator.nextInt(3);
		String sofa = "Sofa" + (num + 1);
		Dimension d = hGui.getPosition(sofa);
		goToLocation(d);
	}
	
	public void DoCook() {
		Dimension d = hGui.getPosition("Shelves");
		goToLocation(d);
		Random generator = new Random();
		int num = generator.nextInt(2);
		String s = "CookingSlab" + (num + 1);
		d = hGui.getPosition(s);
		goToLocation(d);
		num = generator.nextInt(2);
		s = "CookingGrill" + (num + 1);
		d = hGui.getPosition(s);
		goToLocation(d);
	}
	
	public void DoEat() {
		Random generator = new Random();
		int num = generator.nextInt(4);
		num++;
		Dimension d = hGui.getPosition("Chair" + num);
		goToLocation(d);
	}
	
	public void DoSleep() {
		Dimension d = hGui.getPosition("Bed");
		goToLocation(d);
	}
	
	public void DoRelaxOnSofa() {
		Dimension d = hGui.getPosition("Sofa1");
		goToLocation(d);
	}
	
	public void DoRead() {
		Dimension d = hGui.getPosition("StudyChair");
		goToLocation(d);
	}
	
	public void goToLocation(Dimension d) {
		xDestination = d.width;
		yDestination = d.height;
	}
}