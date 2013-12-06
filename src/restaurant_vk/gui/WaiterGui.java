package restaurant_vk.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import restaurant_vk.CustomerAgent;
import restaurant_vk.WaiterBaseAgent;
import restaurant_vk.WaiterNormalAgent;

public class WaiterGui implements Gui{
	private WaiterBaseAgent agent = null;

    private int xPos = 40, yPos = 40;//default waiter position
    private int xDestination = 40, yDestination = 40;//default start position

    public static final int xTable = 350;
    public static final int yTable = 350;
    private final int HOST_WIDTH = 20;
    private final int HOST_HEIGHT = 20;
    private final int tableWidth = 50;
	private final int tableHeight = 50;
	private int homePosX;
	private int homePosY;
	
	private int destinationTable = 0;
	
	private CustomerGui customerGui;
	
	private String thingInHand = "";
	
	private Map<String, String> symbols = new HashMap<String, String>();
	
	// An array containing the mapping of the table versus the table coordinates.
	private TableCoordinates[] tables = new TableCoordinates[4];
	
	private MyState state = MyState.None;
	
	private boolean breakCheckBox = false;
	private boolean breakEnabled = true;
	
	/*
     * States of the waiterGui while performing certain actions.
     */
    enum MyState {None, NotMoving, EscortingCustomer, MovingToTable, GoingToRevolvingStand, Entering, Exiting};

    public WaiterGui(WaiterBaseAgent agent, Dimension homePos) {
        this.agent = agent;
        
        homePosX = homePos.width;
        homePosY = homePos.height;
        
        xPos = -20;
        yPos = 200;
        xDestination = xPos;
        yDestination = yPos;
        
        // Initializing the table coordinates.
        tables[0] = new TableCoordinates(1, xTable, yTable);
		tables[1] = new TableCoordinates(2, xTable + tableWidth + 10, yTable);
		tables[2] = new TableCoordinates(3, xTable + (2 * (tableWidth + 10)), yTable);
		tables[3] = new TableCoordinates(4, xTable + (3 * (tableWidth + 10)), yTable);
		
		setSymbols();
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
        	if (state == MyState.Entering) {
        		state = MyState.NotMoving;
        		agent.atDestination();
        	}
        	else if (state == MyState.Exiting) {
        		state = MyState.None;
        		agent.atDestination();
        	}
        	else if (state == MyState.GoingToRevolvingStand) {
        		agent.atDestination();
        	}
        	else if (xDestination == 40 && yDestination == 170 && state == MyState.EscortingCustomer) {
				agent.atDestination();
			}
        	else if ((xDestination == tables[0].x + 20) & (yDestination == tables[0].y - 20) || 
    				(xDestination == tables[1].x + 20) & (yDestination == tables[1].y - 20) || 
    				(xDestination == tables[2].x + 20) & (yDestination == tables[2].y - 20) || 
    				(xDestination == tables[3].x + 20) & (yDestination == tables[3].y - 20)) {
        		// If just moving to a table, the waiter should simply stop.
        		if (state == MyState.MovingToTable) {
        			agent.atDestination();
        			thingInHand = "";
        		}
        		// After escorting the customer it should go back.
        		else if (state == MyState.EscortingCustomer) {
        			xDestination = homePosX;
        			yDestination = homePosY;
        		}
        	}
        	else if (xDestination == homePosX && yDestination == homePosY) {
        		// If the waiter is back at its original position, he should stop.
        		if (state != MyState.NotMoving) { 
        			agent.atDestination();
        			state = MyState.NotMoving;
        		}
        	}
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.CYAN);
        g.fillRect(xPos, yPos, HOST_WIDTH, HOST_HEIGHT);
        g.setColor(Color.BLACK);
        g.drawString("W", xPos + 4, yPos + 14);
        g.drawString(symbols.get(thingInHand), xPos + 1, yPos + 34);
    }

    public boolean isPresent() {
    	if (state != MyState.None)
    		return true;
    	return false;
    }

    /*
     * Move to target table.
     */
    public void DoGoToTable(int table) {
    	state = MyState.MovingToTable;
    	setDestinationTable(table);
    	setDestinationCoordinates();
    }
    
    /*
     * Bring the customer to the target table.
     */
    public void DoBringToTable(CustomerAgent customer, int table) {
    	state = MyState.EscortingCustomer;
    	setDestinationTable(table);
    	setDestinationCoordinates();
    	customerGui.goTo(new Dimension(xDestination - 20, yDestination + 20));
    }

    /*
     * Leave the customer after moving to a table except for the time when
     * the waiter is escorting the customer to the table.
     */
    public void DoLeaveCustomer() {
        xDestination = homePosX;
        yDestination = homePosY;
        System.out.println("Gui " + agent + ": I'm going back.");
    }
    
    public void DoPickUpCustomer() {
    	xDestination = 40;
    	yDestination = 170;
    	state = MyState.EscortingCustomer;
    }
    
    public void DoGoToRevolvingStand() {
    	state = MyState.GoingToRevolvingStand;
    	// Temporary.
    	xDestination = homePosX;
    	yDestination = homePosY;
    }
    
    public void DoEnterRestaurant() {
    	state = MyState.Entering;
    	xDestination = homePosX;
    	yDestination = homePosY;
    }
    
    public void DoLeaveRestaurant() {
    	state = MyState.Exiting;
    	xDestination = -20;
    	yDestination = 200;
    }
    
    public void setCustomerGui(CustomerGui gui) {
    	customerGui = gui;
    }
    
    /* Sets the destination coordinates of the waiter according to the
     * target table number.
     */
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
    
    /*
     * Encapsulation of the table numbers and their respective 
     * coordinates.
     */
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
    
    /*
     * This is kind of a setter that may never be used in the entire
     * program. Will delete it if it's useless at the end.
     */
    public void setDestinationTable(int table) {
    	destinationTable = table;
    }
    
    /*
	 * Sets up symbols inside the map.
	 */
	private void setSymbols() {
		symbols.put("Steak", "ST");
		symbols.put("Chicken", "CH");
		symbols.put("Salad", "SA");
		symbols.put("Pizza", "PI");
		symbols.put("", "");
		symbols.put("Check", "CHECK");
	}
	
	public void setFoodServed(String food) {
		thingInHand = food;
	}
	
	public void giveCheckCaption() {
		thingInHand = "Check";
	}
	
	public boolean isBreakChecked() {
		return breakCheckBox;
	}
	
	public boolean isBreakEnabled() {
		return breakEnabled;
	}
	
	public void setBreakChecked() {
		if (breakCheckBox == false) {
			breakCheckBox = true;
			breakEnabled = false;
			agent.wantBreak();
		}
	}
	
	public void setBreakUnchecked() {
		if (breakCheckBox == true) {
			breakCheckBox = false;
			agent.getBackToWork();
		}
	}
	
	public void setOnBreak() {
		breakCheckBox = true;
		breakEnabled = true;
	}
	
	public void setOffBreak() {
		breakCheckBox = false;
		breakEnabled = true;
	}
}
