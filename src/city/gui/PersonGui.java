package city.gui;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.HashMap;

import people.People;


public class PersonGui extends Rectangle2D.Double {
	Color personColor;
	public Building home;
	public Sidewalk sidewalk;
	public int xDestination;
	public int yDestination;
	public double xPos;
	public double yPos;
	public CityPanel cityPanel;
	boolean redLight;
	Rectangle2D.Double rectangle;
	ArrayList<Sidewalk> sidewalks;
	String direction;
	Color vehicleColor;
	People person;
	
	public void GoToRestaurantOne()
	{
		setDestination(cityPanel.buildings.get(13).xLocation, cityPanel.buildings.get(13).yLocation);
	}
	

	public void GoToHouse()
	{
		setDestination(home.xLocation, home.yLocation);
	}
	

	public void GoToMarket() {
		setDestination(cityPanel.buildings.get(19).xLocation, cityPanel.buildings.get(19).yLocation);
	}
	

	public void goToBank() {
		setDestination(cityPanel.buildings.get(17).xLocation, cityPanel.buildings.get(17).yLocation);
	}
	
	public PersonGui( int x, int y, int width, int height, Sidewalk s, ArrayList<Sidewalk>sidewalks, CityPanel cityPanel, People person ) {
		super( x, y, width, height );
		this.sidewalk = s;
		this.sidewalks = sidewalks;
		rectangle = new Rectangle2D.Double( 100, 100, 20, 20 );
		this.setOrientation();
		this.cityPanel = cityPanel;
		this.direction = "up";
		this.person = person;
		xDestination =(int)this.x;
		yDestination = (int)this.y;

	}

	public void setLocation( int x, int y ) {
		setRect( x, y, getWidth(), getHeight() );
	}
	
	public void setDestination(int xd, int yd) {
		xDestination = xd;
		yDestination = yd;
	}
	
	public Color getColor() {
		return personColor;
	}
	
	public void setOrientation() {
		if ( sidewalk.xVelocity > 0 ) {
			this.setRect( sidewalk.xOrigin - 30, sidewalk.yOrigin+2, this.getWidth(), this.getHeight() ); 
		} else if ( sidewalk.yVelocity > 0 ) {
			this.setRect( sidewalk.xOrigin+2, sidewalk.yOrigin + 60, this.getWidth(), this.getHeight() ); 
			this.xDestination = xDestination + 2;
		} else {
			if ( sidewalk.isHorizontal ) {
				this.setRect( sidewalk.xOrigin + width - this.getWidth(), sidewalk.yOrigin + 2, this.getWidth(), this.getHeight() );
			} else {
				this.setRect( sidewalk.xOrigin + 2, sidewalk.yOrigin + height - this.getHeight(), this.getWidth(), this.getHeight() ) ;
				this.xDestination = xDestination + 2;
			}
		}
	}
	
	public void move( double xVelocity, double yVelocity ) {
		if(this.direction.equals("up")) {
			setRect( x+xVelocity, y-yVelocity, getWidth(), getHeight() );
			this.xPos = x+xVelocity;
			this.yPos = y-yVelocity;	
		} else {
			setRect( x+xVelocity, y+yVelocity, getWidth(), getHeight() );
			this.xPos = x+xVelocity;
			this.yPos = y+yVelocity;
		}
	}
	public void draw(Graphics2D g2) {
		if(x != xDestination || y != yDestination) {
			System.out.println(x + " " + xDestination + " " + y + " " +yDestination);
			g2.setColor( Color.red );
			g2.fill( this );
			g2.draw(this);
			if(!redLight &&( x != xDestination || y != yDestination)) {
				this.move(sidewalk.xVelocity,sidewalk.yVelocity);
			}
			if(yPos >= 212 && xPos == 112) { 
				this.direction = "up";
			}
			if(yPos <= 120 && xPos == 112) {
				this.direction = "down";
			}
			//Residential Intersection
			if(yPos == 120 && xPos == 112) {
				this.sidewalk = sidewalks.get(1);
				this.setOrientation();
			}
			
			//First Intersection
			if(xPos >= 550 && xPos <= 572) {
				//Up or down?
				if(yDestination <= 100) {
					this.direction = "up";
				} else {
					this.direction = "down";
				}
	
				//570 = Second Row Buildings
				if(Math.abs(xDestination - xPos) == 20) {
					this.sidewalk = sidewalks.get(18);
					this.setOrientation();
				}
				if(yPos == yDestination) {
					person.Arrived();
					
				}
			}		
			//Second Intersection
			if(xPos >= 742 && xPos <= 750) {
				
				if(yDestination <= 100) {
					this.direction = "up";
				} else {
					this.direction = "down";
				}
	
				
				//770 3rd row buildings
				if(Math.abs(xDestination - xPos) == 20) {
					this.sidewalk = sidewalks.get(17);
					this.setOrientation();
				}
				
				if(yPos == yDestination) {
					person.Arrived();
				}
			}
			//Third Intersection
			if(xPos >= 970 && xPos <= 982) {
				
				if(yDestination <= 100) {
					this.direction = "up";
				} else {
					this.direction = "down";
				}
	
				//990 4th row buildings
				if(Math.abs(xDestination - xPos) == 20) {
					this.sidewalk = sidewalks.get(15);
					this.setOrientation();
				}
				
				if(yPos == yDestination) {
					person.Arrived();
				}
			}
		
		}
	}


}
