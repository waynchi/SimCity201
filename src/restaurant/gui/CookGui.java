package restaurant.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import restaurant.CookRole;


public class CookGui implements Gui {

	private CookRole role = null;
	Boolean isCooking;
	boolean goingBack = false;
	String foodBeingCooked = null;
	RestaurantGui gui;
	private List<String> foodPlated = new ArrayList<String>();

	 
    private int xDestination = 70, xPos = 70;
    private int yDestination = 270, yPos = 270;
    
    private int cookX = 70;
    private int cookY = 270;
    
    private int revolvingStandX = 350;
    private int revolvingStandY = 250;
    
    boolean isPresent;
	
	
	public CookGui (CookRole cook) {
		isCooking = false;
		role = cook;
		for (int i=0;i<3;i++) {
			foodPlated.add("");
		}
	}
	

	@Override
	public void updatePosition() {
		System.out.println("xdestin is " + xDestination + "ydestin is " + yDestination);
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

        if (xPos == xDestination && yPos == yDestination
        		&& (xDestination == revolvingStandX) && (yDestination == revolvingStandY)) {
        	xDestination = cookX;
        	yDestination = cookY;
           role.msgAtRevolvingStand();
        }
        
        if (xPos == xDestination && yPos == yDestination
        		&& (xDestination == cookX) && (yDestination == cookY) && goingBack) {
        	goingBack = false;
        	role.msgAtGrill();
        }
        }

	@Override
	public void draw(Graphics2D g) {
        g.setColor(Color.blue);
		g.fillRect(xPos, yPos, 20, 20);
        g.setColor(Color.BLACK);
        g.drawString("Cook", 65, 300);
        if (isCooking) {
        	g.drawString(foodBeingCooked, 50, 265);
        }
        else g.drawString("", 50, 265);
        for (int i=0; i<3;i++) {
        	g.drawString(foodPlated.get(i), 50+45*i, 225);
        }
	}

	@Override
	public boolean isPresent() {
		return isPresent;
	}

	public void cookFood (String food) {
		isCooking = true;
		foodBeingCooked = food;
	}
	
	public void plateFood (String food, int i){
		foodPlated.set(i-1, food);	
	}
	
	public void foodPickedUp(int table) {
		foodPlated.set((table-1),"");
	}
	
	public void finishCooking () {
		isCooking = false;
	}

	public void DoGoToRevolvingStand() {
		xDestination = revolvingStandX;
		yDestination = revolvingStandY;
		
	}
	
	public void setPresent(boolean b) {
		isPresent = b;
	}

	public void DoGoBack() {
		goingBack = true;
		xDestination = cookX;
		yDestination = cookY;
		// TODO Auto-generated method stub
		
	}
}
