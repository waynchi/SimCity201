package restaurant_ps.gui;



import restaurant.interfaces.Cashier;
import restaurant_ps.CashierAgent;
import restaurant_ps.interfaces.Customer;

import java.awt.*;

public class CashierGui implements Gui {

    private Cashier agent = null;

    private int xPos = -20, yPos = -20;
    private int jobxPos = 90, jobyPos = 250;
    private int xDestination = 90, yDestination = 250;//default start position
    private int guiWidth = 70, guiHeight = 70;
    int[] xTable = new int[3]; //3 tables
    int[] yTable = new int[3]; //3 tables
    private RestaurantGuiPS restGui;
    
    public enum Command {leaving,entering,none};
    Command command;
    
   
    public CashierGui(Cashier cashier, RestaurantGuiPS restG) {
        this.agent = cashier;
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
        
        if(xPos == xDestination && yPos == yDestination)
        {
        	if(command == Command.leaving)
        	{
        		command = Command.none;
        		((CashierAgent) agent).msgAnimationFinishedDoLeaveRestaruant();
        	}
        }

       restGui.updateMoneyOutput(((CashierAgent) agent).getRestaurantMoney());
    }

    public void draw(Graphics2D g) 
    {
        g.setColor(Color.BLUE);
        g.fillRect(xPos, yPos, guiWidth, guiHeight);
        g.setColor(Color.WHITE);
        g.drawString(((CashierAgent) agent).getName(), xPos, yPos+10);
        
        
    }

    public boolean isPresent() {
        return true;
    }

//    public void DoBringToTable(Customer customer,int table) {
//    	xDestination = xTable[table-1] + 20;
//        yDestination = yTable[table-1] - 20;
//    	
//    }
//
//    public void DoLeaveCustomer() {
//        xDestination = -20;
//        yDestination = -20;
//    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }



	public void DoEnterRestaurant() {
		// TODO Auto-generated method stub
		xDestination = jobxPos;
		yDestination = jobyPos;
		
	}



	public void DoLeaveRestaurant() {
		// TODO Auto-generated method stub
		xDestination = -20;
		yDestination = -20;
		command = Command.leaving;
	}
}









