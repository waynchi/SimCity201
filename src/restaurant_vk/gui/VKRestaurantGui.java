package restaurant_vk.gui;

import javax.swing.*;

import restaurant.gui.RestaurantAnimationPanel;

import java.awt.Dimension;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class VKRestaurantGui extends JFrame implements ActionListener {
	RestaurantVkAnimationPanel animationPanel = new RestaurantVkAnimationPanel();

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public VKRestaurantGui() {
    	JPanel temp = new JPanel();
    	temp.setLayout(null);
    	
    	int WINDOWX = 500;
        int WINDOWY = 400;

        Dimension animationDim = new Dimension(WINDOWX, WINDOWY);
        animationPanel.setPreferredSize(animationDim);
        animationPanel.setMinimumSize(animationDim);
        animationPanel.setMaximumSize(animationDim);
        
        setBounds(50, 50, WINDOWX, WINDOWY);
        
        add(animationPanel);

    }

    public RestaurantVkAnimationPanel getAnimationPanel(){
    	return animationPanel;
    }
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
