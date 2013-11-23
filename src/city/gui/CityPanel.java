package city.gui;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class CityPanel extends JPanel implements MouseListener {
	ArrayList<Building> buildings;
	ArrayList<CityRoad> roads;


	public CityPanel() {
		buildings = new ArrayList<Building>();
		roads = new ArrayList<CityRoad>();



		Building restaurant = new Building( 120, 100, 60, 60 );
		Building bank = new Building( 430, 100, 60, 60 );
		for ( int i=0; i<2; i++ ) {
			for ( int j=0; j<5; j++ ) {
				Building home = new Building( i*40+ 10, j*40 + 10, 20, 20 );
				buildings.add( home );
			}
		}


		buildings.add(restaurant);
		buildings.add(bank);

		CityRoad road1 = new CityRoad(200,100, 200, 20 );
		CityRoad road2 = new CityRoad(325,100, 20, 200 );
		CityRoad road3 = new CityRoad(325,230, 200, 20 );


		roads.add(road1);
		roads.add(road2);
		roads.add(road3);


		

		addMouseListener( this );
	}

		public void paintComponent( Graphics g ) {

			Graphics2D g2 = (Graphics2D)g;
			g2.setColor( Color.black );
			
			for ( int i=0; i<buildings.size(); i++ ) {
				Building b = buildings.get(i);
				g2.fill( b );
			}
			for(int i=0; i<roads.size();i++) {
				CityRoad road = roads.get(i);
				g2.fill(road);
			}
		}

		public ArrayList<Building> getBuildings() {
			return buildings;
		}
		
		public void mouseClicked(MouseEvent me) {
			//Check to see which building was clicked
			System.out.println("hello2");

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
