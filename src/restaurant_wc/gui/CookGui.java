package restaurant_wc.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import restaurant_wc.CookRoleWc;



public class CookGui implements Gui {

	private CookRoleWc role = null;
	Boolean isCooking;
	boolean goingBack = false;
	boolean leavingWork = false;
	boolean goingToFridge = false;
	List<String> foodsBeingCooked = new ArrayList<String>();
	String foodBeingCooked = null;
	RestaurantGuiWc gui;
	private List<String> foodPlated = new ArrayList<String>();

	 
    private int xDestination = 150, 
    		xPos = 0;
    private int yDestination = 270,
    		yPos = 0;
    
    private int cookX = 150;
    private int cookY = 270;
    
    private int revolvingStandX = 350, revolvingStandY = 250;
    
    private int exitX = 0, exitY = 0;
    private int fridgeX = 175, fridgeY = 350;
    
    boolean isPresent;
	
	
	public CookGui (CookRoleWc cook) {
		isCooking = false;
		role = cook;
		for (int i=0;i<3;i++) {
			foodPlated.add("");
		}
	}
	

	@Override
	public void updatePosition() {
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
        
        if (xPos == xDestination && yPos == yDestination
        		&& (xDestination == fridgeX) && (yDestination == fridgeY) && goingToFridge) {
        	goingToFridge = false;
        	role.msgAtFridge();
        }
        
        if (xPos == xDestination && yPos == yDestination
        		&& (xDestination == exitX) && (yDestination == exitY) && leavingWork) {
        	leavingWork = false;
        	role.msgAtExit();
        }
        }

	@Override
	public void draw(Graphics2D g) {
        g.setColor(Color.blue);
		g.fillRect(xPos, yPos, 20, 20);
        g.setColor(Color.black);
        g.drawString("Cook", xPos, yPos+20);
        if (isCooking) {
        	for (int j=0; j<foodsBeingCooked.size(); j++) {
        		g.drawString(foodsBeingCooked.get(j), 225, 255+(j*35));
            }
//        	g.drawString(foodBeingCooked, 225, 252);
        }
        else g.drawString("", 50, 265);
        for (int i=0; i<foodPlated.size(); i++) {
        	g.drawString(foodPlated.get(i), 110, 250+(i*35));
        }
	}

	@Override
	public boolean isPresent() {
		return isPresent;
	}

	public void cookFood (String food) {
		isCooking = true;
		foodBeingCooked = food;
		foodsBeingCooked.add(food);
	}
	
	public void plateFood (String food, int i){
		foodPlated.set(i-1, food);	
	}
	
	public void foodPickedUp(int table) {
		foodPlated.set((table-1),"");
	}
	
	public void finishCooking () {
		foodsBeingCooked.remove(0);
		if(foodsBeingCooked.isEmpty())
		{
			isCooking = false;
		}
	}

	public void DoGoToRevolvingStand() {
		xDestination = revolvingStandX;
		yDestination = revolvingStandY;
	}
	
	public void setPresent(boolean b) {
		isPresent = b;
	}

	public void DoGoToCookingPlace() {
		goingBack = true;
		xDestination = cookX;
		yDestination = cookY;
		// TODO Auto-generated method stub
		
	}


	public void DoLeaveWork() {
		leavingWork = true;
		xDestination = exitX;
		yDestination = exitY;
		
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
	
	public void setXDest(int x) {
		xDestination = x;
	}
	
	public void setYDest(int y) {
		yDestination = y;
	}


	public void setDefaultDestination() {
		// TODO Auto-generated method stub
		goingBack = true;
		xDestination = cookX;
		yDestination = cookY;
	}


	public void goToFridge() {
		// TODO Auto-generated method stub
		xDestination = fridgeX;
		yDestination = fridgeY;
		goingToFridge = true;
	}
}
