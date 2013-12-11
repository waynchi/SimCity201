package restaurant.gui;

import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import restaurant.CashierRole;

public class RestaurantCashierGui implements Gui{
	boolean isPresent;
	CashierRole cashier;
	RestaurantGui gui;
	
	int xDestination = 400, yDestination = 165;
	int xPos = 0, yPos = 0;
	int xExit = 0, yExit = 0;
	boolean goingToWorkPlace= false;
	boolean leaving = false;
    private ImageIcon cashierImage = new ImageIcon("res/restaurant/cashier.png");

	
	public RestaurantCashierGui(CashierRole cashierRole) {
		// TODO Auto-generated constructor stub
		cashier = cashierRole;
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		if (xPos < xDestination && Math.abs(xDestination - xPos) > 1)
			xPos += 2;
		else if (xPos > xDestination && Math.abs(xDestination - xPos) > 1)
			xPos -= 2;
		
		if (yPos < yDestination && Math.abs(yDestination - yPos) > 1)
			yPos += 2;
		else if (yPos > yDestination && Math.abs(yDestination - yPos) > 1)
			yPos -= 2;

		if (Math.abs(xPos - xDestination) < 2 && Math.abs(yPos - yDestination) < 2) {
			xPos = xDestination;
			yPos = yDestination;
	        if (goingToWorkPlace){
	        	cashier.msgAtPosition();
	        	goingToWorkPlace = false;
	        }
	        if (leaving){
	        	cashier.msgAtExit();
	        	xDestination = 400;
	        	yDestination = 165;
	        	leaving = false;
	        }
		}
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
        g.drawImage(cashierImage.getImage(), xPos, yPos, 30, 40, null);

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
		xDestination = 400;
		yDestination = 165;
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
		xDestination = 365;
		yDestination = 210;
		goingToWorkPlace = true;
	}

}
