package restaurant_vk.gui;

import javax.swing.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame implements ActionListener {
	AnimationPanel animationPanel = new AnimationPanel();

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
    	JPanel temp = new JPanel();
    	temp.setLayout(null);
    	
        int WINDOWX = 1500;
        int WINDOWY = 685;
        int WINDOW_LEFT = 50;
        int WINDOW_TOP = 50;
        
        animationPanel.setBounds(0, 0, 680, 480);
        temp.add(animationPanel);
    }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
