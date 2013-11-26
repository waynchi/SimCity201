package city.gui;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;


public class Lane {
	Rectangle2D.Double rectangle;
	ArrayList<Line2D.Double> sides;
	public int xVelocity;
	public int yVelocity;
	public boolean redLight;
	public int xOrigin;
	public int yOrigin;
	int width;
	int height;
	public boolean isHorizontal;
	boolean startAtOrigin;
	Color laneColor;
	Color sideColor;
	ArrayList<Vehicle> vehicles;
	public String name;
	boolean hasCar;
	
	public Lane(int xo, int yo, int w, int h, int xv, int yv, boolean ish, Color lc, Color sc, String name ) {
		this.name = name;
		redLight = false;
		width = w;
		height = h;
		xVelocity = xv;
		yVelocity = yv;
		xOrigin = xo;
		yOrigin = yo;
		isHorizontal = ish;
		laneColor = lc;
		sideColor = sc;
		hasCar = false;
		
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
		
		vehicles = new ArrayList<Vehicle>();
	}
	public void draw( Graphics2D g2 ) {
		g2.setColor( laneColor );
		g2.fill( rectangle );
		
		for ( int i=0; i<sides.size(); i++ ) {
			g2.setColor( sideColor );
			g2.draw( sides.get(i) );
		}
	
	}
	
	public void redLight() {
		redLight = true;
	}
	
	public void greenLight() {
		redLight = false;
	}
	public boolean isHorizontal() {
		return isHorizontal;
	}
}
