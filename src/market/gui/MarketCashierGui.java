package market.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import market.MarketCashierRole;

public class MarketCashierGui implements Gui{
	boolean isPresent;
	MarketCashierRole cashier;
	MarketGui gui;
	
	int xDestination = 380, yDestination = 0;
	int xPos = 170, yPos = 0;
	int xExit = 170, yExit = 0;
	boolean goingToWorkPlace= false;
	boolean leaving = false;
	
	
	public MarketCashierGui(MarketCashierRole cashier) {
		// TODO Auto-generated constructor stub
		this.cashier = cashier;
	}

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
	        if (xPos == xDestination && yPos == yDestination && goingToWorkPlace){
	        	cashier.msgAtPosition();
	        	goingToWorkPlace = false;
	        }
	        if (xPos == xExit && yPos == yExit && leaving){
	        	cashier.msgAtExit();
	        	leaving = false;
	        }
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(Color.yellow);
		g.fillRect(xPos, yPos, 20, 20);
        g.setColor(Color.BLACK);
        g.drawString("Cahsier", xPos, yPos+20);
        
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return isPresent;
	}

	public void setPresent(boolean b) {
		isPresent = b;
		// TODO Auto-generated method stub
		
	}

	public void DoGoToWorkingPosition() {
		// TODO Auto-generated method stub
		xDestination = 380;
		yDestination = 0;
		goingToWorkPlace = true;
		
	}

	public void DoLeaveWork() {
		// TODO Auto-generated method stub
		xDestination = xExit;
		yDestination = yExit;
		leaving = true;
	}

	public void setDefaultDestination() {
		// TODO Auto-generated method stub
		xPos = 170; 
		yPos = 0;
		xDestination = 380;
		yDestination = 0;
		goingToWorkPlace = true;
	}

}
