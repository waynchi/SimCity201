package market.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import market.interfaces.MarketCustomer;

public class MarketCustomerGui implements Gui{

	private MarketCustomer marketCustomer;
	MarketGui gui;
	AnimationPanel animationPanel = null;
	
	private int xPos = 470, yPos = 150;
	private int xDestination = 470, yDestination = 150;
	private int xCounter = 240, yCounter = 150;
	private int xRegister = 380, yRegister = 70;
	private int xExit = 470, yExit = 70;
	
	public MarketCustomerGui(MarketCustomer mc){
		marketCustomer = mc;
		animationPanel = mc.getAnimationPanel();
	}
	private boolean isPresent;
	
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;
		if (xPos == xDestination && yPos == yDestination && xDestination == xCounter && yDestination == yCounter) {
			marketCustomer.msgAtCounter();
		}
		if (xPos == xDestination && yPos == yDestination && xDestination == xRegister && yDestination == yRegister) {
			marketCustomer.msgAtRegister();
		}
		if (xPos == xDestination && yPos == yDestination && xDestination == xExit && yDestination == yExit) {
			marketCustomer.msgAtExit();
		}

	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(Color.BLUE);
		g.fillRect(xPos, yPos, 30, 30);
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return isPresent;
	}

	public void DoLineUp() {
		
	}

	public void DoGoToMarketEmployee() {
		// TODO Auto-generated method stub
		xDestination = xCounter; 
		yDestination = yCounter;
	}


	public void DoGoToRegister() {
		// TODO Auto-generated method stub
		xDestination = xRegister;
		yDestination = yRegister;
	}


	public void DoGoToExit() {
		// TODO Auto-generated method stub
		xDestination = xExit;
		yDestination = yExit;
	}
	
	public void setPresent(boolean p) {
		isPresent = p;
	}

}
