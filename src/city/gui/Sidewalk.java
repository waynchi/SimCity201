package city.gui;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;


public class Sidewalk {
	Rectangle2D.Double rectangle;
	ArrayList<Line2D.Double> sides;
	int xVelocity;
	int yVelocity;
	int xOrigin;
	int yOrigin;
	int width;
	int height;
	boolean isHorizontal;
	boolean startAtOrigin;
	Color laneColor;
	Color sideColor;
	ArrayList<Person> people;
	
	public Sidewalk(int xo, int yo, int w, int h, int xv, int yv, boolean ish, Color lc, Color sc ) {
		width = w;
		height = h;
		xVelocity = xv;
		yVelocity = yv;
		xOrigin = xo;
		yOrigin = yo;
		isHorizontal = ish;
		laneColor = lc;
		sideColor = sc;
		
		//Make the lane surface
		rectangle = new Rectangle2D.Double( xOrigin, yOrigin, width, height );
		
		//Make the edges to the lane surface
		sides = new ArrayList<Line2D.Double>();
		if ( isHorizontal ) {
			sides.add( new Line2D.Double( xOrigin, yOrigin, xOrigin+width, yOrigin ) );
			sides.add( new Line2D.Double( xOrigin, yOrigin+height, xOrigin+width, yOrigin+height ) );
		} else {
			sides.add( new Line2D.Double( xOrigin, yOrigin, xOrigin, yOrigin+height ) );
			sides.add( new Line2D.Double( xOrigin+width, yOrigin, xOrigin+width, yOrigin+height ) );
		}
		
		people = new ArrayList<Person>();
	}
	public void addVehicle( Person p ) {
		//We need to set the proper origin for this new vehicle, given the lane starting geometry constraints
		//The +2 is due to my lanes being 20 pixels "wide" and vehicles being 16 pixels "wide". 
		if ( xVelocity > 0 ) {
			p.setRect( xOrigin, yOrigin+2, p.getWidth(), p.getHeight() ); 
		} else if ( yVelocity > 0 ) {
			p.setRect( xOrigin+2, yOrigin, p.getWidth(), p.getHeight() ); 
		} else {
			if ( isHorizontal ) {
				p.setRect( xOrigin + width - p.getWidth(), yOrigin + 2, p.getWidth(), p.getHeight() );
			} else {
				p.setRect( xOrigin + 2, yOrigin + height - p.getHeight(), p.getWidth(), p.getHeight() ) ;
			}
		}
		
		people.add( p );
	}
	
	public void draw( Graphics2D g2 ) {
		g2.setColor( laneColor );
		g2.fill( rectangle );
		
		for ( int i=0; i<sides.size(); i++ ) {
			g2.setColor( sideColor );
			g2.draw( sides.get(i) );
		}
		
		for ( int i=people.size()-1; i >= 0; i-- ) {
			Person p = people.get(i);
			
			p.move( xVelocity, yVelocity );
			
			
			double x = p.getX();
			double y = p.getY();

			//Remove the vehicle from the list if it is at the end of the lane
			if ( isHorizontal ) {
				//End of lane is xOrigin + width - person width
				double endOfLane = xOrigin + width - p.getWidth();
				if ( xVelocity > 0 && x >= endOfLane ) {
					people.remove(i);					
				} else if ( x <= xOrigin ) {
					people.remove(i);
				}
			} else {
				//End of lane is xOrigin + height - person height
				double endOfLane = yOrigin + height - p.getHeight();
				if ( yVelocity > 0 && y >= endOfLane ) {
					people.remove(i);					
				} else if ( y <= yOrigin ) {
					people.remove(i);
				}
			}
		}
		
		for ( int i=0; i<people.size(); i++ ) {
			Person p = people.get(i);
			g2.setColor( p.getColor() );
			g2.fill( p );
		}
	}
	

}
