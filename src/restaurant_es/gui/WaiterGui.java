package restaurant_es.gui;


import java.util.*;

import restaurant_es.BaseWaiterRoleEs;
import restaurant_es.RestaurantCustomerRoleEs;
import restaurant_es.interfaces.Customer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class WaiterGui implements Gui {

    private BaseWaiterRoleEs role = null;
    private boolean isPresent = false;

    private int xPos = 0, yPos = 0;//default waiter position
    private int currentTableX = -100;
    private int currentTableY = -100; // position of current table
    
    private int homeIndex;
    
    private int cookX = 205;
    private int cookY = 200;
    
    private int revolvingStandX = 350;
    private int revolvingStandY = 250;
    
    private int xExit = 0, yExit = 0;
    
    private int xDestination = 20, yDestination = 20;//default start position
    private Customer currentCustomer;
    private ImageIcon img = new ImageIcon("res/bank/tellersprite.png");
    private boolean BringingFoodToCustomer = false;
    private boolean leaving =false;
    
    private Map<Integer, Dimension> tableMap = new HashMap<Integer, Dimension>();
    
    public WaiterGui(BaseWaiterRoleEs role) {
        this.role = role;
        tableMap.put (1,new Dimension(100,100));
        tableMap.put (2,new Dimension(200,100));
        tableMap.put (3,new Dimension(300,100));  
    }

    public void setHomePosition(int x) {
    	homeIndex = x;
    }
    
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
        		&& (xDestination == currentTableX -20) && (yDestination == currentTableY -20)) {
        	BringingFoodToCustomer = false;
           role.msgAtTable();
        }
        if (xPos == xDestination && yPos == yDestination
        		&& (xDestination == cookX) && (yDestination == cookY)) {
           role.msgAtCook();
        }
        if (xPos == xDestination && yPos == yDestination
        		&& (xDestination == 20) && (yDestination == 25)) {
           role.msgAtWaitingCustomer();
        }
        if (xPos == xDestination && yPos == yDestination
        		&& (xDestination == revolvingStandX) && (yDestination == revolvingStandY)) {
           role.msgAtRevolvingStand();
        }
        if (xPos == xDestination && yPos == yDestination
        		&& (xDestination == xExit) && (yDestination == yExit && leaving)) {
           role.msgAtExit();
           leaving = false;
        }
    }

    public void draw(Graphics2D g) {
        
    	g.drawImage(img.getImage(),xPos,yPos,null);
        g.setColor(Color.white);
        g.drawString(role.getName(), xPos, yPos+10);
        if (BringingFoodToCustomer) {
    		g.setColor(Color.BLACK);
			g.drawString(currentCustomer.getChoice().substring(0,2),xPos, yPos);
        }
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void DoSeatCustomer(Customer c, int tableNum) {
        Dimension dm = tableMap.get(tableNum);
    	xDestination = (int)(dm.getWidth()) - 20;
        yDestination = (int)(dm.getHeight()) - 20;
        currentTableX = (int)(dm.getWidth());
        currentTableY = (int)(dm.getHeight());
        c.getGui().DoGoToPosition(currentTableX+60, currentTableY);
    }

    public void DoApproachCustomer(Customer c) {
    	currentCustomer = c;
    	Dimension dm = tableMap.get(((RestaurantCustomerRoleEs) c).getTableNumber());
    	xDestination = (int)(dm.getWidth()) - 20;
        yDestination = (int)(dm.getHeight()) - 20;
        currentTableX = (int)(dm.getWidth());
        currentTableY = (int)(dm.getHeight());
    }
    
    public void DoGoToCook() {
    	xDestination = cookX;
    	yDestination = cookY;
    }
    
    public void DoBringFoodToCustomer(Customer c) {
    	BringingFoodToCustomer = true;
    	DoApproachCustomer(c);
    }
    
    public void DoGoRest () {
    	xDestination = 50+30*homeIndex;
    	yDestination = 10;
    }
    
    public void DoAskCustomer() {
    	
    	
    }
  
    
    public void DoGoToWaitingCustomer() {
    	xDestination = 20;
    	yDestination = 25;
    }
    
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

	public void DoGoToRevolvingStand() {
		// TODO Auto-generated method stub
		xDestination = revolvingStandX;
		yDestination = revolvingStandY;
	}
	
	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoExit() {
		xDestination = xExit;
		yDestination = yExit;
		leaving = true;
		// TODO Auto-generated method stub
		
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
		xDestination = 50+30*homeIndex;
    	yDestination = 10;
	}
}
