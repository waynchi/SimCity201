package city.gui;
import javax.swing.*;

import restaurant.gui.AnimationPanel;
import restaurant.gui.RestaurantGui;

import java.awt.*;
import java.awt.geom.*;

public class BuildingPanel extends JPanel {
	Rectangle2D myRectangle;
	String myName;
	CityGui myCity;
	
	public BuildingPanel( Rectangle2D r, int i, CityGui sc ) {
		myRectangle = r;
		myName = "" + i;
		myCity = sc;
		
		setBackground( Color.LIGHT_GRAY );
		setMinimumSize( new Dimension( 500, 250 ) );
		setMaximumSize( new Dimension( 500, 250 ) );
		setPreferredSize( new Dimension( 500, 250 ) );
		
		RestaurantGui restaurantGui = myCity.restaurantGui;
		restaurantGui.setPreferredSize(new Dimension(500,500));
		System.out.println(restaurantGui);
		restaurantGui.setVisible(true);
        //JLabel j = new JLabel( myName );
        //add( animationPanel );
        
        JPanel panel = new JPanel();
        panel.add(restaurantGui);
        panel.setPreferredSize(new Dimension(500,500));

        add(panel);
        
        
      
        //add(j);
        
		
	}
	
	public String getName() {
		return myName;
	}

	public void displayBuildingPanel() {
		myCity.displayBuildingPanel( this );
		
	}
}
