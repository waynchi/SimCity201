package city.gui;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Vehicle extends Rectangle2D.Double {
	public int xDestination;
	public int yDestination;
	public double xPos;
	public double yPos;
	public Lane lane;
	public CityPanel cityPanel;
	boolean redLight;
	Rectangle2D.Double rectangle;
	ArrayList<Lane> lanes;
	ArrayList<Lane> bestRoute;
	HashMap<Integer,Lane> distances;
	//ArrayList<Integer> distances;
	
	

	
	Color vehicleColor;
	
	public Vehicle( int x, int y, int width, int height, Lane l, ArrayList<Lane>lanes, CityPanel cityPanel ) {
		super( x, y, width, height );
		this.lane = l;
		this.lanes = lanes;
		rectangle = new Rectangle2D.Double( 100, 100, 20, 20 );
		this.setOrientation();
		distances = new HashMap<Integer,Lane>();
		redLight = false;
		this.cityPanel = cityPanel;


	}
	public void setLane(Lane l) {
		this.lane = l;
	}
	public void setLocation( int x, int y ) {
		setRect( x, y, getWidth(), getHeight() );
	}
	
	public Color getColor() {
		return vehicleColor;
	}
	
	public void move( int xv, int yv ) {
		setRect( x+xv, y+yv, getWidth(), getHeight() );
		this.xPos = x+xv;
		this.yPos = y+yv;
	}
	public void setDestination(int xd, int yd) {
		xDestination = xd;
		yDestination = yd;
	}
	public void setOrientation() {
		if ( lane.xVelocity > 0 ) {
			this.setRect( lane.xOrigin, lane.yOrigin+2, this.getWidth(), this.getHeight() ); 
		} else if ( lane.yVelocity > 0 ) {
			this.setRect( lane.xOrigin+2, lane.yOrigin, this.getWidth(), this.getHeight() ); 
		} else {
			if ( lane.isHorizontal ) {
				this.setRect( lane.xOrigin + width - this.getWidth(), lane.yOrigin + 2, this.getWidth(), this.getHeight() );
			} else {
				this.setRect( lane.xOrigin + 2, lane.yOrigin + height - this.getHeight(), this.getWidth(), this.getHeight() ) ;
			}
		}
	}
	public void shortestDistance() {
		for(Lane lane : lanes) {
			if((xPos - 2) == lane.xOrigin) {
				double distance = Math.sqrt(Math.pow(lane.xOrigin - xPos,2) + (Math.pow(lane.yOrigin - yPos,2)));
				distances.put((int) distance,lane);
			}
			
		}
		
		for(Map.Entry<Integer,Lane> e:distances.entrySet()) {
			//System.out.println(e.getKey() + " " + e.getValue());
		}
		
// 		Iterator it = distances.keySet().iterator();
//		while(it.hasNext()) {
//			System.out.println("DISTANCES:" + it.next());
//		}
//		if(distances.size() > 0) {
//			System.out.println(this.min(distances.get(0),distances.get(1),distances.get(2)));
//		}
		
	}
	public static double min(double a, double b, double c) {
	    return Math.min(Math.min(a, b), c);
	}
	public void draw(Graphics2D g2) {
		//this.shortestDistance();
		g2.setColor( Color.blue );
		g2.fill( this );
		g2.draw(this);
		if(!redLight) {
			this.move(lane.xVelocity,lane.yVelocity);
		}
		//Crosswalks, X Coordinates:
		//330  & 550 & 750 & 950
		//Intersections
		//9 = first; 10 = second; 11=third
		


		if(xPos >= 550 && xPos <= 552) {
			
			//570 = Second Row Buildings
			if(Math.abs(xDestination - xPos) == 20) {
				this.lane = lanes.get(10);
				this.setOrientation();
			}
			if(yPos == yDestination) {
				cityPanel.removeVehicle(this);
				
			}
			
		}
		if(xPos >= 750 && xPos <= 752) {
			//770 3rd row buildings
			if(Math.abs(xDestination - xPos) == 20) {
				this.lane = lanes.get(11);
				this.setOrientation();
			}
			
			if(yPos == yDestination) {
				cityPanel.removeVehicle(this);
				
			}
		}
		
		if(xPos >= 970 && xPos <= 982) {
			//990 4th row buildings
			if(Math.abs(xDestination - xPos) == 20) {
				this.lane = lanes.get(12);
				this.setOrientation();
			}
			
			if(yPos == yDestination) {
				cityPanel.removeVehicle(this);
				
			}
		}
		
		
	}
	public void redLight() {
		redLight = true;
	}
	
	public void greenLight() {
		redLight = false;
	}
}
