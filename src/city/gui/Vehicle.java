package city.gui;
import java.awt.*;
import java.awt.geom.*;


public class Vehicle extends Rectangle2D.Double {
	public int xDestination;
	public int yDestination;
	public double xPos;
	public double yPos;
	public Lane lane;
	Rectangle2D.Double rectangle;

	
	Color vehicleColor;
	
	public Vehicle( int x, int y, int width, int height, Lane l ) {
		super( x, y, width, height );
		this.lane = l;
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

		
		
		
		if(xPos == 550 || xPos == 750 || xPos == 950) {
			System.out.println("at cross");
		}
		
	}
}
