package city.gui;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import people.People;
import people.PeopleAgent;


public class Sidewalk {
	Rectangle2D.Double rectangle;
	ArrayList<Line2D.Double> sides;
	double xVelocity;
	double yVelocity;
	int xOrigin;
	int yOrigin;
	int width;
	int height;
	boolean isHorizontal;
	boolean startAtOrigin;
	Color laneColor;
	Color sideColor;
	ArrayList<PersonGui> people;
	public String name;
	public boolean redLight;
	boolean hasPerson;
	People person;
	public boolean simulatingCrash;
	
	public Sidewalk(int xo, int yo, int w, int h, double xv, double yv, boolean ish, Color lc, Color sc, String name ) {
		width = w;
		height = h;
		xVelocity = xv;
		yVelocity = yv;
		xOrigin = xo;
		yOrigin = yo;
		isHorizontal = ish;
		laneColor = lc;
		sideColor = sc;
		this.name = name;
		hasPerson = false;
		simulatingCrash = false;
		
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
		
		people = new ArrayList<PersonGui>();
	}
	public void setPerson(People person) {
		this.person = person;
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
	public PersonGui getPersonGui() {
		return person.getPersonGui();
	}
	
	public void greenLight() {
		redLight = false;
	}
	

}
