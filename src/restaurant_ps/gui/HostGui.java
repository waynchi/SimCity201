package restaurant_ps.gui;



import restaurant_ps.interfaces.Customer;
import restaurant_ps.interfaces.Host;

import java.awt.*;

public class HostGui implements Gui {

    private Host agent = null;

    private int xPos = 90, yPos = 10;//default waiter position
    private int xDestination = 90, yDestination = 10;//default start position
    private int guiWidth = 70, guiHeight = 70;
    int[] xTable = new int[3]; //3 tables
    int[] yTable = new int[3]; //3 tables
    
   
    public HostGui(Host agent) {
        this.agent = agent;
        xTable[0] = 250;
        yTable[0] = 250;
        xTable[1] = 250;
        yTable[1] = 150;
        xTable[2] = 250;
        yTable[2] = 50;
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
        		& (xDestination == xTable[0] + 20) & (yDestination == yTable[0] - 20)) {
           agent.msgAtTable();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTable[1] + 20) & (yDestination == yTable[1] - 20)) {
           agent.msgAtTable();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTable[2] + 20) & (yDestination == yTable[2] - 20)) {
           agent.msgAtTable();
        }
    }

    public void draw(Graphics2D g) 
    {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, guiWidth, guiHeight);
        g.setColor(Color.WHITE);
        g.drawString("Host " + agent.getName(), xPos, yPos+10);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable(Customer customer,int table) {
    	xDestination = xTable[table-1] + 20;
        yDestination = yTable[table-1] - 20;
    	
    }

    public void DoLeaveCustomer() {
        xDestination = -20;
        yDestination = -20;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

	public void DoEnterRestaurant() {
		// TODO Auto-generated method stub
		
	}

	
}









