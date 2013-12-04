package transportation.gui;

import javax.swing.*;

import transportation.AnimationPanel;

import city.gui.CityGui;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class BusStopGui extends JFrame implements ActionListener {
    
	AnimationPanel animationPanel = new AnimationPanel();

    public BusStopGui() {
        int WINDOWX = 500;
        int WINDOWY = 400;
        
        Dimension animationDim = new Dimension(WINDOWX, WINDOWY);
        animationPanel.setPreferredSize(animationDim);
        animationPanel.setMinimumSize(animationDim);
        animationPanel.setMaximumSize(animationDim);

    	setBounds(50, 50, WINDOWX, WINDOWY);
        add(animationPanel);
    	setVisible(true);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
   
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public AnimationPanel getAnimationPanel() {
		return animationPanel;
	}
	
	 public void updatePosition() {
	    	animationPanel.updatePosition();
	}
	 public static void main(String[] args) {
			BusStopGui gs = new BusStopGui();
			
		}
	    
}
