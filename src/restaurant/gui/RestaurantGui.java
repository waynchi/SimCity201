package restaurant.gui;
//this is the main class for gui stuff
import restaurant.BaseWaiterRole;
import restaurant.RestaurantCustomerRole;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JPanel {
    /* The GUI has only one frame and two panels within, the control panel 
     * and the animation panel
     */
	AnimationPanel animationPanel = new AnimationPanel();
	
	// boundaries and coordinates of the two panels.
	private int RESTPANELX = 50;
	private int RESTPANELY = 50;
	private int RESTPANELW = 450;
	private int RESTPANELH = 350;
	
	
    private RestaurantPanel restPanel = new RestaurantPanel(this);
    
    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
    	 
     	setBounds(50, 50, RESTPANELW, RESTPANELH);
        
     	
         Dimension restDim = new Dimension(RESTPANELX, (int) (RESTPANELY * .6));
         restPanel.setPreferredSize(restDim);
         restPanel.setMinimumSize(restDim);
         restPanel.setMaximumSize(restDim);
         
         add(animationPanel);
         
     }
   
    public AnimationPanel getAnimationPanel(){
    	return animationPanel;
    }
}
