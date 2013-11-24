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
	static final int hozWidth = 210;
	static final int hozHeight = 20;
	static final int sidewalkHeight = 10;
	
	
	


	public CityPanel() {
		buildings = new ArrayList<Building>();
		lanes = new ArrayList<Lane>();
		sidewalks = new ArrayList<Sidewalk>();
		vehicles = new ArrayList<Vehicle>();
	
		


		//Create grid of lanes
		
		//Horizontal Top Lanes
		Sidewalk s = new Sidewalk( hozX - 210, hozY + 110 , hozWidth, sidewalkHeight, 1, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		Lane l = new Lane( hozX - 210, hozY + 90, hozWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black );
		lanes.add( l );
		s = new Sidewalk( hozX - 210, hozY + 80 , hozWidth, sidewalkHeight, 1, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		s = new Sidewalk( hozX, hozY + 110 , hozWidth, sidewalkHeight, 1, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		l = new Lane( hozX, hozY + 90, hozWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black );
		lanes.add( l );
		s = new Sidewalk( hozX, hozY + 80 , hozWidth, sidewalkHeight, 1, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		s = new Sidewalk( hozX + 200, hozY - 10 , hozWidth - 10, sidewalkHeight, 1, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		l = new Lane( hozX + 210, hozY, hozWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black );
		lanes.add( l );

		s = new Sidewalk( hozX + 400, hozY - 10 , hozWidth + 40, sidewalkHeight, 1, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		l = new Lane( hozX + 420, hozY, hozWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black );
		lanes.add( l );
		
		s = new Sidewalk( hozX + 400, hozY + 110 , hozWidth + 40, sidewalkHeight, 1, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		s = new Sidewalk( hozX + 400, hozY + 80 , hozWidth + 40, sidewalkHeight, 1, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		//TOP AND BOTTOM OF HORIZONTAL ROADS
		s = new Sidewalk( hozX + 410, hozY + 180 , hozWidth + 40, sidewalkHeight, 1, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		s = new Sidewalk( hozX + 190, hozY + 180 , hozWidth, sidewalkHeight, 1, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		s = new Sidewalk( hozX + 190, hozY + 20 , hozWidth, sidewalkHeight, 1, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		s = new Sidewalk( hozX + 410, hozY + 20 , hozWidth, sidewalkHeight, 1, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		s = new Sidewalk( hozX + 410, hozY + 210 , hozWidth + 40, sidewalkHeight, 1, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		s = new Sidewalk( hozX + 190, hozY + 210 , hozWidth + 40, sidewalkHeight, 1, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		//FAR RIGHT VERTICAL
		s = new Sidewalk( hozX + 650, hozY - 10 , sidewalkHeight, hozWidth + 10, 1, 0, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		//SMALL INNER BLOCKS - TOP
		s = new Sidewalk( hozX + 620, hozY + 20 , sidewalkHeight, 60, 1, 0, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		s = new Sidewalk( hozX + 420, hozY + 20 , sidewalkHeight, 60, 1, 0, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		s = new Sidewalk( hozX + 390, hozY + 20 , sidewalkHeight, 60, 1, 0, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		s = new Sidewalk( hozX + 220, hozY + 20 , sidewalkHeight, 60, 1, 0, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		//FAR LEFT VERTICAL
		s = new Sidewalk( hozX + 190, hozY - 10 , sidewalkHeight, 90, 1, 0, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		s = new Sidewalk( hozX + 190, hozY + 120 , sidewalkHeight, 90, 1, 0, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		//SMALL INNER BLOCKS - BOTTOM
		s = new Sidewalk( hozX + 620, hozY + 120 , sidewalkHeight, 70, 1, 0, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		s = new Sidewalk( hozX + 420, hozY + 120 , sidewalkHeight, 70, 1, 0, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		s = new Sidewalk( hozX + 390, hozY + 120 , sidewalkHeight, 70, 1, 0, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		s = new Sidewalk( hozX + 220, hozY + 120 , sidewalkHeight, 70, 1, 0, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		

		
		//Middle
		s = new Sidewalk( hozX + 210, hozY + 110 , hozWidth + 40, sidewalkHeight, 1, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		l = new Lane( hozX + 210, hozY + 90, hozWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black );
		lanes.add(l);
		s = new Sidewalk( hozX + 210, hozY + 80 , hozWidth + 40, sidewalkHeight, 1, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		
		l = new Lane( hozX + 420, hozY + 90, hozWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black );
		lanes.add(l);
		//Bottom
		l = new Lane( hozX + 210, hozY + 190, hozWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black );
		lanes.add(l);
		l = new Lane( hozX + 420, hozY + 190, hozWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black );
		lanes.add(l);
		
		//Vertical Cross Lanes
		l = new Lane( crossX + 220, crossY, crossWidth, crossHeight, 0, 1, false, Color.DARK_GRAY, Color.black );
		lanes.add(l);
		l = new Lane( crossX + 420, crossY, crossWidth, crossHeight, 0, 1, false, Color.DARK_GRAY, Color.black );
		lanes.add(l);
		
		l = new Lane( crossX + 650, crossY, crossWidth, crossHeight, 0, 1, false, Color.DARK_GRAY, Color.black );
		lanes.add(l);
		
		
		//Residential
		l = new Lane( crossX - 210, crossY - 50, crossWidth, crossHeight - 50, 0, 1, false, Color.DARK_GRAY, Color.black );
		lanes.add(l);
		//Right sidewalks
		s = new Sidewalk( hozX - 210, hozY - 40 , sidewalkHeight, 120, 1, 0, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		s = new Sidewalk( hozX - 210, hozY + 120 , sidewalkHeight, 140, 1, 0, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		//Left sidewalks
		s = new Sidewalk( hozX - 240, hozY - 40 , sidewalkHeight, 120, 1, 0, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		s = new Sidewalk( hozX - 240, hozY + 80 , sidewalkHeight, 200, 1, 0, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		l = new Lane( crossX - 210, crossY + 110, crossWidth, crossHeight - 50, 0, 1, false, Color.DARK_GRAY, Color.black );
		lanes.add(l);
	
		
		//Start animation for the timer
		javax.swing.Timer t = new javax.swing.Timer( 25, this );
		t.start();
		
		//Add grid of homes
		for ( int i=0; i<=1; i++ ) {
			for ( int j=0; j<3; j++ ) {
				Building home = new Building( i*60+ 90, j*40 + 10, 20, 20 );
				buildings.add( home );
			}
		}
		
		for ( int i=0; i<=1; i++ ) {
			for ( int j=0; j<3; j++ ) {
				Building home = new Building( i*60+ 90, j*40 + 180, 20, 20 );
				buildings.add( home );
			}
		}
		
		//First Section, Top Row
		Building restaurant1 = new Building( hozX + 230, hozY + 160, 20, 20 );
		buildings.add(restaurant1);
		
		Building restaurant2 = new Building( hozX + 230, hozY + 30, 20, 20 );
		buildings.add(restaurant2);
		
		Building restaurant3 = new Building( hozX + 430, hozY + 30, 20, 20 );
		buildings.add(restaurant3);
		
		//First Section, Bottom Row
		Building market = new Building( hozX + 370, hozY + 160, 20, 20 );
		buildings.add(market);
		
		Building bank = new Building( hozX + 370, hozY + 60, 20, 20 );
		buildings.add(bank);
		
		Building restaurant4 = new Building( hozX + 600, hozY + 60, 20, 20 );
		buildings.add(restaurant4);
		
		//Second Section, Top Row 
		Building restaurant5 = new Building( hozX + 600, hozY + 160, 20, 20 );
		buildings.add(restaurant5);
		
		Building restaurant7 = new Building( hozX + 230, hozY + 120, 20, 20 );
		buildings.add(restaurant7);
		
		//Second Section, Bottom Row
		Building restaurant6 = new Building( hozX + 430, hozY + 120, 20, 20 );
		buildings.add(restaurant6);
		
		addMouseListener( this );
	}

	
	public void actionPerformed( ActionEvent ae ) {
		count++;
		
		Vehicle vehicle;
		if ( count == 100) {
			//Second Row -- First Building
			vehicle = new Vehicle( 15, 15, 16, 16, lanes.get(12),lanes,this);
			vehicle.setDestination(570, 60);
			vehicles.add(vehicle);
			
		}
		
		if (count == 150) {
			//First Row -- Second Building
			vehicle = new Vehicle( 15, 15, 16, 16, lanes.get(12),lanes,this);
			vehicle.setDestination(570, 100);
			vehicles.add(vehicle);
		}
		if( count == 200) {
			//First Row -- Third Building
			vehicle = new Vehicle( 15, 15, 16, 16, lanes.get(12),lanes,this);
			vehicle.setDestination(570, 150);
			vehicles.add(vehicle);
		}
		if( count == 250) {
			//First Row -- Fourth Building
			vehicle = new Vehicle( 15, 15, 16, 16, lanes.get(11),lanes,this);
			vehicle.setDestination(570, 200);
			vehicles.add(vehicle);
		}

		if( count == 300) {
			//Second Row -- First Building
			vehicle = new Vehicle( 15, 15, 16, 16, lanes.get(11),lanes, this);
			vehicle.setDestination(770, 60);
			vehicles.add(vehicle);
		}
		if( count == 350) {
			//Second Row -- Second Building
			vehicle = new Vehicle( 15, 15, 16, 16, lanes.get(12),lanes, this);
			vehicle.setDestination(770, 100);
			vehicles.add(vehicle);
		}
		if( count == 400) {
			//Second Row -- Third Building
			vehicle = new Vehicle( 15, 15, 16, 16, lanes.get(12),lanes, this);
			vehicle.setDestination(770, 150);
			vehicles.add(vehicle);
		}
		if( count == 450) {
			//Second Row -- Fourth Building
			vehicle = new Vehicle( 15, 15, 16, 16, lanes.get(11),lanes, this);
			vehicle.setDestination(770, 200);
			vehicles.add(vehicle);
		}
		if( count == 500) {
			//Fourth row -- First building
			vehicle = new Vehicle( 15, 15, 16, 16, lanes.get(11),lanes, this);
			vehicle.setDestination(990, 100);
			vehicles.add(vehicle);
		}
		if( count == 550) {
			//Fourth row -- First building
			vehicle = new Vehicle( 15, 15, 16, 16, lanes.get(12),lanes, this);
			vehicle.setDestination(990, 200);
			vehicles.add(vehicle);
		}
		
//		//Make them all lanes stop
//		if ( count % 500 == 0 ) {
//			System.out.println("RED LIGHT");
//			for ( int i=0; i<vehicles.size(); i++ ) {
//				vehicles.get(i).redLight();
//			}
//		}
//		
//		if ( count % 1000 == 0 ) {
//			System.out.println("GREEN LIGHT");
//
//			for ( int i=0; i<vehicles.size(); i++ ) {
//				vehicles.get(i).greenLight();
//			}
//		}
		

		
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
	
	public void removeVehicle(Vehicle v) {
		vehicles.remove(v);
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
