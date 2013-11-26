package restaurant.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import restaurant.CashierRole;

public class RestaurantCashierGui implements Gui{
	boolean isPresent;
	CashierRole cashier;
	RestaurantGui gui;
	
	int xDestination = 250, yDestination = 250;
	int xPos = 0, yPos = 0;
	int xExit = 0, yExit = 0;
	boolean goingToWorkPlace= false;
	boolean leaving = false;
	
	
	public RestaurantCashierGui(CashierRole cashierRole) {
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
		
	}

	public void DoLeaveWork() {
		// TODO Auto-generated method stub
		
	}

}
