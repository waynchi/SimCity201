package restaurant_zt.gui;
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
public class RestaurantGuiZt extends JPanel implements ActionListener {
    /* The GUI has only one frame and two panels within, the control panel 
     * and the animation panel
     */
	public static AnimationPanelZt animationPanel;
    
    public static List<CustomerGuiZt> customers = new ArrayList<CustomerGuiZt>();
    
	
	
    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGuiZt(Timer timer) {
    	 
    	int WINDOWX = 500;
        int WINDOWY = 400;

        Dimension animationDim = new Dimension(WINDOWX, WINDOWY);
        animationPanel = new AnimationPanelZt(timer);
        animationPanel.setPreferredSize(animationDim);
        animationPanel.setMinimumSize(animationDim);
        animationPanel.setMaximumSize(animationDim);

    	
    	setBounds(50, 50, WINDOWX, WINDOWY);
    	
        //setLayout(new BorderLayout());
        
        add(animationPanel);
     }
    public AnimationPanelZt getAnimationPanel(){
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