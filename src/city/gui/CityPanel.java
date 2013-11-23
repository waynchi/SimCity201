package city.gui;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class CityPanel extends JPanel implements MouseListener,ActionListener {
	ArrayList<Building> buildings;
	ArrayList<Lane> lanes;
	int count = 0;


	public CityPanel() {
		buildings = new ArrayList<Building>();
		
		//Create grid of lanes
		lanes = new ArrayList<Lane>();
		
		Lane l = new Lane( 150, 10, 600, 20, 1, 0, true, Color.green, Color.black );
		lanes.add( l );
		l = new Lane( 150, 100, 600, 20, 1, 0, true, Color.green, Color.black );
		lanes.add(l);
		l = new Lane( 150, 200, 600, 20, 1, 0, true, Color.green, Color.black );
		lanes.add(l);
		
		l = new Lane( 130, 10, 20, 210, 0, 1, false, Color.green, Color.black );
		lanes.add(l);
		l = new Lane( 350, 10, 20, 210, 0, 1, false, Color.green, Color.black );
		lanes.add(l);
		
		l = new Lane( 550, 10, 20, 210, 0, 1, false, Color.green, Color.black );
		lanes.add(l);
		
		l = new Lane( 750, 10, 20, 210, 0, 1, false, Color.green, Color.black );
		lanes.add(l);
		
		//Start animation for the timer
		javax.swing.Timer t = new javax.swing.Timer( 25, this );
		t.start();
		
		//Add grid of homes (temp loc)
		for ( int i=0; i<2; i++ ) {
			for ( int j=0; j<5; j++ ) {
				Building home = new Building( i*40+ 10, j*40 + 10, 20, 20 );
				buildings.add( home );
			}
		}
		
		//First Section, Top Row
		Building restaurant1 = new Building( 170, 30, 30, 30 );
		buildings.add(restaurant1);
		
		Building restaurant2 = new Building( 370, 30, 30, 30 );
		buildings.add(restaurant2);
		
		Building restaurant3 = new Building( 570, 30, 30, 30 );
		buildings.add(restaurant3);
		
		//First Section, Bottom Row
		Building market = new Building( 320, 70, 30, 30 );
		buildings.add(market);
		
		Building bank = new Building( 520, 70, 30, 30 );
		buildings.add(bank);
		
		Building restaurant4 = new Building( 720, 70, 30, 30 );
		buildings.add(restaurant4);
		
		//Second Section, Top Row 
		Building restaurant5 = new Building( 170, 120, 30, 30 );
		buildings.add(restaurant5);
		
		Building restaurant7 = new Building( 370, 120, 30, 30 );
		buildings.add(restaurant7);
		
		//Second Section, Bottom Row
		Building restaurant6 = new Building( 320, 170, 30, 30 );
		buildings.add(restaurant6);
		
		l = lanes.get(0);
		Vehicle vehicle = new Vehicle( 15, 15, 16, 16);
		l.addVehicle( vehicle );
		
		addMouseListener( this );
	}

	
	public void actionPerformed( ActionEvent ae ) {
		count++;
		
//		if ( count % 40 == 0 ) {
//			Lane l = lanes.get(0);
//			l.addVehicle( new Vehicle( 15, 15, 16, 16) );
//		}
//		
//		if ( count % 80 == 0 ) {
//			Lane l = lanes.get(1);
//			l.addVehicle( new Vehicle( 15, 15, 16, 16) );
//		}
//		
		
		
		//Make them all lanes stop
		if ( count % 500 == 0 ) {
			System.out.println("RED LIGHT");
			for ( int i=0; i<lanes.size(); i++ ) {
				lanes.get(i).redLight();
			}
		}
		
		if ( count % 1000 == 0 ) {
			System.out.println("GREEN LIGHT");

			for ( int i=0; i<lanes.size(); i++ ) {
				lanes.get(i).greenLight();
			}
		}
		
		repaint();
	}
	
	public void paint( Graphics g ) {
		Graphics2D g2 = (Graphics2D)g;
		
		for ( int i=0; i<lanes.size(); i++ ) {
			Lane l = lanes.get(i);
			l.draw( g2 );
		}
		for ( int i=0; i<buildings.size(); i++ ) {
			Building b = buildings.get(i);
			g2.fill( b );
		}
	}
	

	public ArrayList<Building> getBuildings() {
		return buildings;
	}
	
	public void mouseClicked(MouseEvent me) {
		//Check to see which building was clicked
		for ( int i=0; i<buildings.size(); i++ ) {
			Building b = buildings.get(i);
			if ( b.contains( me.getX(), me.getY() ) ) {
				b.displayBuilding();
			}
		}
		
		
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}
}
