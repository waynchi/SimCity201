package restaurant.gui;
//this is the main class for gui stuff


import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JPanel implements ActionListener {
    /* The GUI has only one frame and two panels within, the control panel 
     * and the animation panel
     */
	public static RestaurantAnimationPanel animationPanel;
    
    public static List<CustomerGui> customers = new ArrayList<CustomerGui>();
    
	
	
    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui(Timer timer) {
    	 
    	int WINDOWX = 500;
        int WINDOWY = 400;

        Dimension animationDim = new Dimension(WINDOWX, WINDOWY);
        animationPanel = new RestaurantAnimationPanel(timer);
        animationPanel.setPreferredSize(animationDim);
        animationPanel.setMinimumSize(animationDim);
        animationPanel.setMaximumSize(animationDim);

    	
    	setBounds(50, 50, WINDOWX, WINDOWY);
    	
        //setLayout(new BorderLayout());
        
        add(animationPanel);
     }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
   /* public void popCustomer() {
    	for(int i = 0; i < customers.size(); i++) {
    		customers.get(i).setCust(i-1);
    	}
    	customers.remove(0);
    }
    
    public void gotoLine(BankCustomerGui b) {
    	b.setCust(customers.size());
    	customers.add(b);
    	b.isWaiting = true;
    }
    */
    public RestaurantAnimationPanel getAnimationPanel(){
    	return animationPanel;
    }
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	 public void updatePosition() {
	    	animationPanel.updatePosition();
	}
	    
	
}