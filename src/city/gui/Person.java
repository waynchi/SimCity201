package city.gui;
import java.awt.*;
import java.awt.geom.*;


public class Person extends Rectangle2D.Double {
	Color personColor;
	
	public Person( int x, int y, int width, int height ) {
		super( x, y, width, height );
	}
	public void setLocation( int x, int y ) {
		setRect( x, y, getWidth(), getHeight() );
	}
	
	public Color getColor() {
		return personColor;
	}
	
	public void move( int xv, int yv ) {
		setRect( x+xv, y+yv, getWidth(), getHeight() );
	}
}
