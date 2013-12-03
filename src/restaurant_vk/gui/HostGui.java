package restaurant_vk.gui;


import restaurant_vk.CustomerAgent;
import restaurant_vk.HostAgent;
//import restaurant.gui.CustomerGui.TableCoordinates;

import java.awt.*;

/*
 * Don't look at this thing. I just left it as it was in V1.
 */
public class HostGui implements Gui {

    private HostAgent agent = null;

    private int xPos = 20, yPos = 130;//default waiter position
    private int xDestination = 20, yDestination = 130;//default start position

    public static final int xTable = 200;
    public static final int yTable = 250;
    private final int HOST_WIDTH = 20;
    private final int HOST_HEIGHT = 20;
    private final int tableWidth = 50;
	private final int tableHeight = 50;
	
	private int destinationTable = 0;
	
	private CustomerGui customerGui;
	
	private TableCoordinates[] tables = new TableCoordinates[4];
	
	private MyState state = MyState.NotMoving;

    public HostGui(HostAgent agent) {
        this.agent = agent;
        
        tables[0] = new TableCoordinates(1, xTable, yTable);
		tables[1] = new TableCoordinates(2, xTable + tableWidth + 10, yTable);
		tables[2] = new TableCoordinates(3, xTable + (2 * (tableWidth + 10)), yTable);
		tables[3] = new TableCoordinates(4, xTable + (3 * (tableWidth + 10)), yTable);
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

        if (xPos == xDestination && yPos == yDestination) {
        	if ((xDestination == tables[0].x + 20) & (yDestination == tables[0].y - 20) || 
    				(xDestination == tables[1].x + 20) & (yDestination == tables[1].y - 20) || 
    				(xDestination == tables[2].x + 20) & (yDestination == tables[2].y - 20) || 
    				(xDestination == tables[3].x + 20) & (yDestination == tables[3].y - 20)) {
        		if (state == MyState.MovingToTable)
        			agent.msgAtTable();
        		else if (state == MyState.EscortingCustomer) {
        			xDestination = -20;
        			yDestination = -20;
        		}
        	}
        	else if (xDestination == -20 && yDestination == -20) {
        		if (state != MyState.NotMoving) { 
        			agent.msgAtTable();
        			state = MyState.NotMoving;
        		}
        	}
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, HOST_WIDTH, HOST_HEIGHT);
        g.setColor(Color.BLACK);
        g.drawString("H", xPos + 4, yPos + 14);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoGoToTable(CustomerAgent customer) {
    	state = MyState.MovingToTable;
    	setDestinationCoordinates();
    }
    
    public void DoBringToTable(CustomerAgent customer) {
    	state = MyState.EscortingCustomer;
    	setDestinationCoordinates();
    	customerGui.goTo(new Dimension(xDestination - 20, yDestination + 20));
    }

    public void DoLeaveCustomer() {
        xDestination = -20;
        yDestination = -20;
        System.out.println("Customer leaving. Host speaking.");
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public void setCustomerGui(CustomerGui gui) {
    	customerGui = gui;
    }
    
    private void setDestinationCoordinates() {
    	if (destinationTable == 1) {
			xDestination = tables[0].x + 20;
			yDestination = tables[0].y - 20;
		}
		else if (destinationTable == 2) {
			xDestination = tables[1].x + 20;
			yDestination = tables[1].y - 20;
		}
		else if (destinationTable == 3) {
			xDestination = tables[2].x + 20;
			yDestination = tables[2].y - 20;
		}
		else if (destinationTable == 4) {
			xDestination = tables[3].x + 20;
			yDestination = tables[3].y - 20;
		}
    }
    
    private class TableCoordinates {
		public int tableNumber;
		public int x;
		public int y;
		
		public TableCoordinates(int number, int x, int y) {
			tableNumber = number;
			this.x = x;
			this.y = y;
		}
	}
    
    public void setDestinationTable(int table) {
    	destinationTable = table;
    }
    
    enum MyState {NotMoving, EscortingCustomer, MovingToTable};
}
