package restaurant_ps.gui;


import restaurant_ps.HostRolePS;
import restaurant_ps.RestaurantCustomerRolePS;

import java.awt.*;

public class HostGuiPS implements Gui {

    private HostRolePS agent = null;

    private int xPos = -20, yPos = -20;//default waiter position
    private int currentTableX, currentTableY; // position of current table
    
    private int xDestination = -20, yDestination = -20;//default start position

    //public static final int xTable = 200;
    //public static final int yTable = 250;

    public HostGuiPS(HostRolePS agent) {
        this.agent = agent;
    }

    public void updatePosition() {
        
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable(RestaurantCustomerRolePS customer, int tableX, int tableY) {
        xDestination = tableX + 20;
        yDestination = tableY - 20;
        currentTableX = tableX;
        currentTableY = tableY;
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
