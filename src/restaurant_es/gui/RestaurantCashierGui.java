package restaurant_es.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import restaurant_es.CashierRoleEs;

public class RestaurantCashierGui implements Gui{
	boolean isPresent;
	CashierRoleEs cashier;
	RestaurantGuiEs gui;
	
	int xDestination = 250, yDestination = 250;
	int xPos = 0, yPos = 0;
	int xExit = 0, yExit = 0;
	boolean goingToWorkPlace= false;
	boolean leaving = false;
	
	
	public RestaurantCashierGui(CashierRoleEs cashierRole) {
		// TODO Auto-generated constructor stub
		cashier = cashierRole;
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
	        	xDestination = 250;
	        	yDestination = 250;
	        	leaving = false;
	        }
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(Color.yellow);
		g.fillRect(xPos, yPos, 20, 20);
        g.setColor(Color.white);
        g.drawString("Cashier", xPos, yPos+20);
        
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
		xDestination = 250;
		yDestination = 250;
		goingToWorkPlace = true;
		
	}

	public void DoLeaveWork() {
		// TODO Auto-generated method stub
		xDestination = xExit;
		yDestination = yExit;
		leaving = true;
	}
	
	public void setX(int x) {
		xDestination = x;
	}
	
	public void setY(int y) {
		yDestination = y;
	}
	
	public int getX() {
		return xPos;
	}
	
	public int getY() {
		return yPos;
	}
	
	public int getXDest() {
		return xDestination;
	}
	
	public int getYDest() {
		return yDestination;
	}

	public void setDefaultDestination() {
		// TODO Auto-generated method stub
		xDestination = 250;
		yDestination = 250;
		goingToWorkPlace = true;
	}

}
