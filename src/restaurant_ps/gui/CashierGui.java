package restaurant_ps.gui;



import restaurant.interfaces.Cashier;
import restaurant_ps.CashierAgent;
import restaurant_ps.interfaces.Customer;

import java.awt.*;

public class CashierGui implements Gui {

    private CashierAgent agent = null;

    private int xPos = 90, yPos = 250;//default waiter position
    private int xDestination = 90, yDestination = 250;//default start position
    private int guiWidth = 70, guiHeight = 70;
    int[] xTable = new int[3]; //3 tables
    int[] yTable = new int[3]; //3 tables
    private RestaurantGui restGui;
   
    public CashierGui(CashierAgent cashierAgent, RestaurantGui restG) {
        this.agent = cashierAgent;
        this.restGui = restG;
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

       restGui.updateMoneyOutput(agent.getRestaurantMoney());
    }

    public void draw(Graphics2D g) 
    {
        g.setColor(Color.BLUE);
        g.fillRect(xPos, yPos, guiWidth, guiHeight);
        g.setColor(Color.WHITE);
        g.drawString(agent.getName(), xPos, yPos+10);
        
        
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
}









