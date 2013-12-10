package market.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import market.MarketCashierRole;

public class MarketCashierGui implements Gui{
	boolean isPresent;
	MarketCashierRole cashier;
	MarketGui gui;
    private ImageIcon market_cashier = new ImageIcon("res/market/cashier.png");

	
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
		g.setColor(Color.blue);
		g.drawRect(xPos-2, yPos-2, 34, 34);
        g.drawImage(market_cashier.getImage(), xPos, yPos, 30, 30, null);
        
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
