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
	ArrayList<PersonGui> people;
	CityGui city;
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
	
	
	


	public CityPanel(CityGui city) {
		buildings = new ArrayList<Building>();
		lanes = new ArrayList<Lane>();
		sidewalks = new ArrayList<Sidewalk>();
		vehicles = new ArrayList<Vehicle>();
		people = new ArrayList<PersonGui>();
		this.city = city;
		
	
		


		//Create grid of lanes
		
		//Center Horizontal Top Lanes
		Sidewalk s = new Sidewalk( hozX - 210, hozY + 130 , hozWidth, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		Lane l = new Lane( hozX - 210, hozY + 90, hozWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black );
		lanes.add( l );
		l = new Lane( hozX - 210, hozY + 110, hozWidth + 60, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black );
		lanes.add(l);
		s = new Sidewalk( hozX - 210, hozY + 80 , hozWidth, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		s = new Sidewalk( hozX, hozY + 130 , hozWidth - 150, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		l = new Lane( hozX, hozY + 90, hozWidth - 150, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black );
		lanes.add( l );
		s = new Sidewalk( hozX, hozY + 80 , hozWidth - 150, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		//Beginning of main city
		
		//Top of city
		s = new Sidewalk( hozX + 70, hozY - 30 , hozWidth + 120, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		l = new Lane( hozX + 210, hozY, hozWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black );
		lanes.add( l );
		l = new Lane( hozX + 210, hozY - 20, hozWidth , hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black );
		lanes.add( l );

		s = new Sidewalk( hozX + 400, hozY - 30 , hozWidth + 50, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		l = new Lane( hozX + 420, hozY, hozWidth - 20, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black );
		lanes.add( l );
		l = new Lane( hozX + 420, hozY-20, hozWidth - 20, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black );
		lanes.add( l );
		
		s = new Sidewalk( hozX + 440, hozY + 140 , hozWidth - 40, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		s = new Sidewalk( hozX + 440, hozY + 80 , hozWidth - 40, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		//TOP AND BOTTOM OF HORIZONTAL ROADS
		s = new Sidewalk( hozX + 440, hozY + 250 , hozWidth - 40, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		s = new Sidewalk( hozX + 120, hozY + 250 , hozWidth + 70, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		s = new Sidewalk( hozX + 110, hozY + 20 , hozWidth + 70, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		s = new Sidewalk( hozX + 450, hozY + 20 , hozWidth - 50, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		
		
		//FAR RIGHT VERTICAL
		s = new Sidewalk( hozX + 650, hozY - 20 , sidewalkHeight, hozWidth + 120, 0, 0.5, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		//SMALL INNER BLOCKS - TOP
		s = new Sidewalk( hozX + 600, hozY + 20 , sidewalkHeight, 60, 0, 0.5, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		s = new Sidewalk( hozX + 440, hozY + 20 , sidewalkHeight, 60, 0, 0.5, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		s = new Sidewalk( hozX + 390, hozY + 20 , sidewalkHeight, 60, 0, 0.5, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		s = new Sidewalk( hozX + 110, hozY + 20 , sidewalkHeight, 240, 0, 0.5, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		//FAR LEFT VERTICAL
		s = new Sidewalk( hozX + 60, hozY - 30 , sidewalkHeight, 110, 0, 0.5, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		s = new Sidewalk( hozX + 60, hozY + 130 , sidewalkHeight, 180, 0, 0.5, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		//SMALL INNER BLOCKS - BOTTOM
		s = new Sidewalk( hozX + 600, hozY + 150 , sidewalkHeight, 100, 0, 0.5, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		s = new Sidewalk( hozX + 440, hozY + 150 , sidewalkHeight, 100, 0, 0.5, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		s = new Sidewalk( hozX + 390, hozY + 150 , sidewalkHeight, 100, 0, 0.5, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		
		

		
		//Middle
		s = new Sidewalk( hozX + 120, hozY + 140 , hozWidth + 70, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		l = new Lane( hozX + 210, hozY + 90, hozWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black );
		lanes.add(l);
		s = new Sidewalk( hozX + 120, hozY + 80 , hozWidth + 70, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		
		l = new Lane( hozX + 420, hozY + 90, hozWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black );
		lanes.add(l);
		//Bottom
		l = new Lane( hozX + 210, hozY + 260, hozWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black );
		lanes.add(l);
		l = new Lane( hozX + 420, hozY + 260, hozWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black );
		lanes.add(l);
		l = new Lane( hozX + 210, hozY + 280, hozWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black );
		lanes.add(l);
		l = new Lane( hozX + 420, hozY + 280, hozWidth, hozHeight, 1, 0, true, Color.DARK_GRAY, Color.black );
		lanes.add(l);
		
		s = new Sidewalk( hozX + 410, hozY + 300 , hozWidth + 10, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		s = new Sidewalk( hozX + 70, hozY + 300 , hozWidth + 130, sidewalkHeight, 0.5, 0, true, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		//Vertical Cross Lanes
		l = new Lane( crossX + 90, crossY, crossWidth, crossHeight, 0, 1, false, Color.DARK_GRAY, Color.black );
		lanes.add(l);
		l = new Lane( crossX + 110, crossY, crossWidth, crossHeight, 0, 1, false, Color.DARK_GRAY, Color.black );
		lanes.add(l);
		
		l = new Lane( crossX + 420, crossY, crossWidth, crossHeight, 0, 1, false, Color.DARK_GRAY, Color.black );
		lanes.add(l);
		l = new Lane( crossX + 440, crossY, crossWidth, crossHeight, 0, 1, false, Color.DARK_GRAY, Color.black );
		//lanes.add(l);
		
		l = new Lane( crossX + 650, crossY, crossWidth, crossHeight, 0, 1, false, Color.DARK_GRAY, Color.black );
		lanes.add(l);
		
		
		//Residential
		l = new Lane( crossX - 210, crossY - 50, crossWidth, crossHeight - 50, 0, 1, false, Color.DARK_GRAY, Color.black );
		lanes.add(l);
		l = new Lane( crossX - 230, crossY - 50, crossWidth, crossHeight - 50, 0, 1, false, Color.DARK_GRAY, Color.black );
		lanes.add(l);
		//Right sidewalks
		s = new Sidewalk( hozX - 210, hozY - 40 , sidewalkHeight, 120, 0.5, 0, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		s = new Sidewalk( hozX - 210, hozY + 140 , sidewalkHeight, 120, 0.5, 0, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		//Left sidewalks
		s = new Sidewalk( hozX - 260, hozY - 40 , sidewalkHeight, 120, 0, 0.5, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		s = new Sidewalk( hozX - 260, hozY + 80 , sidewalkHeight, 200, 0, 0.5, false, Color.gray, Color.black ); 
		sidewalks.add(s);
		
		l = new Lane( crossX - 210, crossY + 110, crossWidth, crossHeight - 50, 0, 1, false, Color.DARK_GRAY, Color.black );
		lanes.add(l);
		l = new Lane( crossX - 230, crossY + 110, crossWidth, crossHeight - 50, 0, 1, false, Color.DARK_GRAY, Color.black );
		lanes.add(l);
		
		//Start animation for the timer
		javax.swing.Timer t = new javax.swing.Timer( 25, this );
		t.start();
		
		//Add grid of homes on left
		for ( int i=0; i<=1; i++ ) {
			for ( int j=0; j<3; j++ ) {
				Building home = new Building( i*80+ 70, j*40 + 10, 20, 20, i*60 + 90, j*40 + 10, "Home " + i );
				buildings.add( home );
			}
		}
		
		for ( int i=0; i<=1; i++ ) {
			for ( int j=0; j<3; j++ ) {
				Building home = new Building( i*80+ 70, j*40 + 200, 20, 20, i*60 + 90, j*40 + 18, "Home " + i );
				buildings.add( home );
			}
		}
		
		//First Section, Top Row
		
		Building restaurant2 = new Building( hozX + 230, hozY + 30, 20, 20, 570, 60, "Restaurant 2" );
		buildings.add(restaurant2);
		Building restaurant7 = new Building( hozX + 230, hozY + 120, 20, 20, 570, 100, "Restaurant 7" );
		buildings.add(restaurant7);
		Building restaurant1 = new Building( hozX + 230, hozY + 160, 20, 20, 570, 200, "Restaurant 1" );
		buildings.add(restaurant1);
	
		Building restaurant3 = new Building( hozX + 430, hozY + 30, 20, 20, 770, 60, "Restaurant 3" );
		buildings.add(restaurant3);
		Building bank = new Building( hozX + 370, hozY + 60, 20, 20, 770, 100, "Bank" );
		buildings.add(bank);
		Building restaurant6 = new Building( hozX + 460, hozY + 120, 20, 20, 770, 150, "Restaurant 6" );
		buildings.add(restaurant6);
		Building market = new Building( hozX + 370, hozY + 160, 20, 20, 770, 200, "Market" );
		buildings.add(market);
		
	
		Building restaurant4 = new Building( hozX + 600, hozY + 60, 20, 20, 990, 100, "Restaurant 4" );
		buildings.add(restaurant4);		
		Building restaurant5 = new Building( hozX + 600, hozY + 160, 20, 20, 990, 200, "Restaurant 5" );
		buildings.add(restaurant5);
		


		
		addMouseListener( this );

	}
	

	
	public void actionPerformed( ActionEvent ae ) {
		count++;
		
		Vehicle vehicle;
		PersonGui person;
//		if ( count == 100) {
//			//Second Row -- First Building
//			vehicle = new Vehicle( 15, 15, 16, 16, lanes.get(12),lanes,this);
//			vehicle.setDestination(570, 60);
//			vehicles.add(vehicle);
//			
//	
//		}
//		
//		if( count == 200) {
//			//First Row -- Second Building
//			vehicle = new Vehicle( 15, 15, 16, 16, lanes.get(12),lanes,this);
//			vehicle.setDestination(570, 150);
//			vehicles.add(vehicle);
//
//		}
//		if( count == 250) {
//			//First Row -- Third Building
//			vehicle = new Vehicle( 15, 15, 16, 16, lanes.get(11),lanes,this);
//			vehicle.setDestination(570, 200);
//			vehicles.add(vehicle);
//			
//		
//		}
//
//		if( count == 300) {
//			//Second Row -- First Building
//			vehicle = new Vehicle( 15, 15, 16, 16, lanes.get(11),lanes, this);
//			vehicle.setDestination(770, 60);
//			vehicles.add(vehicle);
//	
//		}
//		if( count == 350) {
//			//Second Row -- Second Building
//			vehicle = new Vehicle( 15, 15, 16, 16, lanes.get(12),lanes, this);
//			vehicle.setDestination(770, 100);
//			vehicles.add(vehicle);
//			
//			
//		}
//		if( count == 400) {
//			//Second Row -- Third Building
//			vehicle = new Vehicle( 15, 15, 16, 16, lanes.get(12),lanes, this);
//			vehicle.setDestination(770, 150);
//			vehicles.add(vehicle);
//			
//		}
//		if( count == 450) {
//			//Second Row -- Fourth Building
//			vehicle = new Vehicle( 15, 15, 16, 16, lanes.get(11),lanes, this);
//			vehicle.setDestination(770, 200);
//			vehicles.add(vehicle);
//			
//	
//
//		}
//		if( count == 500) {
//			//Fourth row -- First building
//			vehicle = new Vehicle( 15, 15, 16, 16, lanes.get(11),lanes, this);
//			vehicle.setDestination(990, 100);
//			vehicles.add(vehicle);
//			
//
//		}
//		if( count == 550) {
//			//Fourth row -- First building
//			vehicle = new Vehicle( 15, 15, 16, 16, lanes.get(12),lanes, this);
//			vehicle.setDestination(990, 200);
//			vehicles.add(vehicle);
//			
//
//		}
		
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
		for(int i=0;i<people.size();i++) {
			PersonGui p = people.get(i);
			p.draw(g2);
		}
		
		
		
		
//		Lane l = lanes.get(0);
//		Vehicle vehicle = new Vehicle( 15, 15, 16, 16);
//		l.addVehicle( vehicle );
	}
	
	public void removeVehicle(Vehicle v) {
		vehicles.remove(v);
	}
	public void removePerson(PersonGui p) {
		people.remove(p);
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
