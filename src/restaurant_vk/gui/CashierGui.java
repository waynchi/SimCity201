package restaurant_vk.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import restaurant_vk.CashierAgent;

public class CashierGui implements Gui {
	private int jobPosX = 620;
	private int jobPosY = 150;
	private int entranceX = 430;
	private int entranceY = -20;
	private int xPos = entranceX;
	private int yPos = entranceY;
	private int xDestination = entranceX;
	private int yDestination = entranceY;
	private State state = State.None;
	public CashierAgent cashier;
	
	enum State {None, Entering, OnDuty, Exiting};
	
	public CashierGui(CashierAgent c) {
		this.cashier = c;
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
			if (state == State.Entering) {
				state = State.OnDuty;
				cashier.activityDone();
			}
			else if (state == State.Exiting) {
				state = State.None;
				cashier.activityDone();
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.RED);
		g.fillRect(xPos, yPos, 20, 20);
		g.setColor(Color.BLACK);
		g.drawString("CA", xPos + 2, yPos + 15);
	}

	@Override
	public boolean isPresent() {
		if (state != State.None)
			return true;
		return false;
	}
	
	public void DoEnterRestaurant() {
		xDestination = jobPosX;
		yDestination = jobPosY;
		state = State.Entering;
	}
	
	public void DoLeaveRestaurant() {
		xDestination = entranceX;
		yDestination = entranceY;
		state = State.Exiting;
	}
}