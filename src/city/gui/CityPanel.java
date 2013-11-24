package city.gui;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class CityPanel extends JPanel implements MouseListener,ActionListener {
	ArrayList<Building> buildings;
	ArrayList<Lane> lanes;
	ArrayList<Sidewalk> sidewalks;
	ArrayList<Vehicle> vehicles;
	int count = 0;
	static final int hozX = 350;
	static final int hozY = 40;
	static final int crossX = 330;
	static final int crossY = 40;
	static final int crossWidth = 20;
	static final int crossHeight = 210;
	static final int hozWidth = 600;
	static final int hozHeight = 20;
	
	
	


	public CityPanel() {
		buildings = new ArrayList<Building>();
		lanes = new ArrayList<Lane>();
		sidewalks = new ArrayList<Sidewalk>();
		vehicles = new ArrayList<Vehicle>();
	
		
		//Create sidewalks
		Sidewalk s = new Sidewalk( hozX - 40, hozY - 20 , hozWidth + 80, hozHeight, 1, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		s = new Sidewalk( hozX - 40, hozY + 210 , hozWidth + 80, hozHeight, 1, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		s = new Sidewalk( crossX - 20, crossY, crossWidth, crossHeight, 1, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		s = new Sidewalk( crossX + 640, crossY, crossWidth, crossHeight, 1, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		//Create grid of lanes
		//Horizontal Top Lanes
		Lane l = new Lane( hozX, hozY, hozWidth, hozHeight, 1, 0, true, Color.green, Color.black );
		lanes.add( l );
		l = new Lane( hozX, hozY + 90, hozWidth, hozHeight, 1, 0, true, Color.green, Color.black );
		lanes.add(l);
		l = new Lane( hozX, hozY + 190, hozWidth, hozHeight, 1, 0, true, Color.green, Color.black );
		lanes.add(l);
		
		//Vertical Cross Lanes
		l = new Lane( crossX, crossY, crossWidth, crossHeight, 0, 1, false, Color.green, Color.black );
		lanes.add(l);
		l = new Lane( crossX + 220, crossY, crossWidth, crossHeight, 0, 1, false, Color.green, Color.black );
		lanes.add(l);
		l = new Lane( crossX + 420, crossY, crossWidth, crossHeight, 0, 1, false, Color.green, Color.black );
		lanes.add(l);
		
		l = new Lane( crossX + 620, crossY, crossWidth, crossHeight, 0, 1, false, Color.green, Color.black );
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
		Building restaurant1 = new Building( hozX + 20, hozY + 20, 30, 30 );
		buildings.add(restaurant1);
		
		Building restaurant2 = new Building( hozX + 220, hozY + 20, 30, 30 );
		buildings.add(restaurant2);
		
		Building restaurant3 = new Building( hozX + 420, hozY + 20, 30, 30 );
		buildings.add(restaurant3);
		
		//First Section, Bottom Row
		Building market = new Building( hozX + 170, 70, 30, 30 );
		buildings.add(market);
		
		Building bank = new Building( hozX + 370, 70, 30, 30 );
		buildings.add(bank);
		
		Building restaurant4 = new Building( hozX + 570, 70, 30, 30 );
		buildings.add(restaurant4);
		
		//Second Section, Top Row 
		Building restaurant5 = new Building( hozX + 20, hozY + 110, 30, 30 );
		buildings.add(restaurant5);
		
		Building restaurant7 = new Building( hozX + 220, hozY + 110, 30, 30 );
		buildings.add(restaurant7);
		
		//Second Section, Bottom Row
		Building restaurant6 = new Building( hozX + 420, hozY + 110, 30, 30 );
		buildings.add(restaurant6);
		
		Vehicle vehicle = new Vehicle( 15, 15, 16, 16, lanes.get(0),lanes);
		vehicle.setDestination(520, 150);
		vehicles.add(vehicle);
		addMouseListener( this );
	}

	
	public void actionPerformed( ActionEvent ae ) {
		count++;
		
		
		
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
		

		for ( int i=0; i<sidewalks.size(); i++ ) {
			Sidewalk s = sidewalks.get(i);
			s.draw( g2 );
		}
		for ( int i=0; i<buildings.size(); i++ ) {
			Building b = buildings.get(i);
			g2.fill( b );
		}
		
		for ( int i=0; i<lanes.size(); i++ ) {
			Lane l = lanes.get(i);
			l.draw( g2 );
		}
		for(int i=0;i<vehicles.size();i++) {
			Vehicle v = vehicles.get(i);
			v.draw(g2);
		}
		
		
		
		
//		Lane l = lanes.get(0);
//		Vehicle vehicle = new Vehicle( 15, 15, 16, 16);
//		l.addVehicle( vehicle );
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
