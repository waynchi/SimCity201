package city.gui;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;


public class Vehicle extends Rectangle2D.Double {
	public int xDestination;
	public int yDestination;
	public double xPos;
	public double yPos;
	public Lane lane;
	Rectangle2D.Double rectangle;
	ArrayList<Lane> lanes;

	
	Color vehicleColor;
	
	public Vehicle( int x, int y, int width, int height, Lane l, ArrayList<Lane>lanes ) {
		super( x, y, width, height );
		this.lane = l;
		this.lanes = lanes;
		rectangle = new Rectangle2D.Double( 100, 100, 20, 20 );
		this.setOrientation();
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
	public void draw(Graphics2D g2) {	
		g2.setColor( this.getColor() );
		g2.fill( this );
		g2.draw(this);
		this.move(lane.xVelocity,lane.yVelocity);
		//Crosswalks, X Coordinates:
		//330  & 550 & 750 & 950
		
		
		
		if(xPos == 550) {
			System.out.println("SWITCHED LANE");
			this.lane = lanes.get(4);
			this.setOrientation();
		}
		
	}
}
