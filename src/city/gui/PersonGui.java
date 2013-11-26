package city.gui;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.HashMap;

import transportation.BusStop;


public class PersonGui extends Rectangle2D.Double {
	public int xDestination;
	public int yDestination;
	public double xPos;
	public double yPos;
	public ArrayList<Sidewalk> sidewalkSegment;
	public Sidewalk currentCell;
	public CityPanel cityPanel;
	boolean redLight;
	Rectangle2D.Double rectangle;
	ArrayList<ArrayList<Sidewalk>> allSidewalks;
	HashMap<Integer,Sidewalk> distances;
	String direction;
	Color vehicleColor;
	int time;
	public String typeOfVehicle;
	
	public PersonGui( int x, int y, int width, int height, ArrayList<Sidewalk> sidewalkSegment, Sidewalk currentCell, ArrayList<ArrayList<Sidewalk>> allsidewalkSegments, CityPanel cityPanel) {
		super( x, y, width, height );
		this.sidewalkSegment = sidewalkSegment;
		this.currentCell = currentCell;
		rectangle = new Rectangle2D.Double( 100, 100, 20, 20 );
		this.setOrientation();
		this.allSidewalks = allsidewalkSegments;
		redLight = false;
		this.cityPanel = cityPanel;
		this.direction = "right";


	}
	public void setSidewalk(Lane l) {
		this.currentCell = sidewalkSegment.get(sidewalkSegment.indexOf(l));
	}
	public void setLocation( int x, int y ) {
		setRect( x, y, getWidth(), getHeight() );
	}
	
	public Color getColor() {
		return vehicleColor;
	}
	
	public void move( double xVelocity, double yVelocity ) {
		Sidewalk nextCell;
		if(this.direction.equals("right")) {
			nextCell = sidewalkSegment.get(sidewalkSegment.indexOf(this.currentCell) + 1);
		}
		else if(this.direction.equals("left")) {
			nextCell = sidewalkSegment.get(sidewalkSegment.indexOf(this.currentCell) - 1);
		}
		else if(this.direction.equals("up")) {
			nextCell = sidewalkSegment.get(sidewalkSegment.indexOf(this.currentCell) - 1);
		}
		else {
			nextCell = sidewalkSegment.get(sidewalkSegment.indexOf(this.currentCell) + 1);
		}
		if(currentCell.yVelocity > 0) {
			if(this.direction.equals("up")) {
				this.currentCell = sidewalkSegment.get(sidewalkSegment.indexOf(this.currentCell) - 1);

			} 
			if(this.direction.equals("down")) {
				this.currentCell = sidewalkSegment.get(sidewalkSegment.indexOf(this.currentCell) + 1);

			}
			
			
		} 
		if(currentCell.xVelocity > 0) {
			if(this.direction.equals("left")) {
				this.currentCell = sidewalkSegment.get(sidewalkSegment.indexOf(this.currentCell) - 1);

			} 
			if(this.direction.equals("right")) {
				this.currentCell = sidewalkSegment.get(sidewalkSegment.indexOf(this.currentCell) + 1);

			} 
		}
		this.setOrientation();
		
	}
	public void setDestination(int xd, int yd) {
		xDestination = xd;
		yDestination = yd;
	}
	public void setOrientation() {
		if ( currentCell.xVelocity > 0 ) {
			this.setRect( currentCell.xOrigin, currentCell.yOrigin+2, this.getWidth(), this.getHeight() ); 
		} else if ( currentCell.yVelocity > 0 ) {
			this.setRect( currentCell.xOrigin+2, currentCell.yOrigin, this.getWidth(), this.getHeight() ); 
		} else {
			if ( currentCell.isHorizontal ) {
				this.setRect( currentCell.xOrigin + width - this.getWidth(), currentCell.yOrigin + 2, this.getWidth(), this.getHeight() );
			} else {
				this.setRect( currentCell.xOrigin + 2, currentCell.yOrigin + height - this.getHeight(), this.getWidth(), this.getHeight() ) ;
			}
		}
	}
	
	public String getCurrentLane() {
		int l = sidewalkSegment.indexOf(currentCell);
		return sidewalkSegment.get(l).name; 
		
	}
	
	public Sidewalk getSidewalkInformation(String name) {
		Sidewalk sidewalkReturn = null;
		for(ArrayList<Sidewalk> sidewalk : this.allSidewalks) {
			for(Sidewalk cell : sidewalk) {
				if(cell.name.equals(name)) {
					sidewalkReturn = cell;
				}
			}
		}
		return sidewalkReturn;
	}
	
	public void draw(Graphics2D g2) {
		if(xDestination > 0 && yDestination > 0)
		{
		time++;
		g2.setColor( Color.red );
		g2.fill( this );
		g2.draw(this);

//		System.out.println(x+","+y + " destination: " + xDestination + "," + yDestination);
		//System.out.println(getCurrentLane());
		
		if(x == xDestination && y == (yDestination + 20)) {
			cityPanel.removePerson(this);
			this.reachedDestination();
		}
		
		if(getCurrentLane().equals("1_20")) {
			this.direction="right";
			sidewalkSegment = allSidewalks.get(2);
			currentCell = sidewalkSegment.get(0);
		}
		
		if(getCurrentLane().equals("3_5")) {
			//Intersection
			if(yDestination < 152) {
				//We need to cross
				this.direction="up";
				sidewalkSegment = allSidewalks.get(16);
				currentCell = sidewalkSegment.get(12);
			}
			if(yDestination == 152) {
				this.direction="right";
				sidewalkSegment = allSidewalks.get(22);
				currentCell = sidewalkSegment.get(0);
			}
			if(yDestination > 152) {
				this.direction="down";
				sidewalkSegment = allSidewalks.get(18);
				currentCell = sidewalkSegment.get(0);
			}
		}
		if(getCurrentLane().equals("17_0")) {
			this.direction="right";
			sidewalkSegment = allSidewalks.get(10);
			currentCell = sidewalkSegment.get(1);
		}
		if(getCurrentLane().equals("11_27")) {
			if(xDestination > x) {
				this.direction="right";
				sidewalkSegment = allSidewalks.get(11);
				currentCell = sidewalkSegment.get(0);
			} else {
				this.direction="down";
				sidewalkSegment = allSidewalks.get(15);
				currentCell = sidewalkSegment.get(0);
			}
			
		}
		if(getCurrentLane().equals("16_5")) {
			this.direction="left";
			sidewalkSegment = allSidewalks.get(23);
			currentCell = sidewalkSegment.get(25);
		}
		if(getCurrentLane().equals("24_0")) {
			this.direction="left";
			sidewalkSegment = allSidewalks.get(3);
			currentCell = sidewalkSegment.get(5);
		}
		if(getCurrentLane().equals("4_0")) {
			this.direction="left";
			sidewalkSegment = allSidewalks.get(1);
			currentCell = sidewalkSegment.get(17);
		}
		if(getCurrentLane().equals("19_17")) {
			this.direction="right";
			sidewalkSegment = allSidewalks.get(25);
			currentCell = sidewalkSegment.get(0);
		}
		if(getCurrentLane().equals("27_33")) {
			this.direction="right";
			sidewalkSegment = allSidewalks.get(24);
			currentCell = sidewalkSegment.get(0);
		}
		if(getCurrentLane().equals("26_4")) {
			if(xDestination > x) {
				this.direction="right";
				sidewalkSegment = allSidewalks.get(24);
				currentCell = sidewalkSegment.get(5);
			} else {
				this.direction="up";
				sidewalkSegment = allSidewalks.get(20);
				currentCell = sidewalkSegment.get(9);
			}
			
		}
		if(getCurrentLane().equals("26_24")) {
			this.direction="up";
			sidewalkSegment = allSidewalks.get(12);
			currentCell = sidewalkSegment.get(31);
		}
		if(getCurrentLane().equals("13_0")) {
			this.direction="left";
			sidewalkSegment = allSidewalks.get(5);
			currentCell = sidewalkSegment.get(25);
		}
		if(getCurrentLane().equals("6_0")) {
			this.direction="left";
			sidewalkSegment = allSidewalks.get(4);
			currentCell = sidewalkSegment.get(26);
		}
		if(getCurrentLane().equals("5_0")) {
			this.direction="down";
			sidewalkSegment = allSidewalks.get(17);
			currentCell = sidewalkSegment.get(0);
		}
		if(getCurrentLane().equals("18_10")) {
			this.direction="left";
			sidewalkSegment = allSidewalks.get(3);
			currentCell = sidewalkSegment.get(5);
		}
		if(getCurrentLane().equals("23_27")) {
			if(xDestination > x) {
				this.direction="right";
				sidewalkSegment = allSidewalks.get(6);
				currentCell = sidewalkSegment.get(0);
			}
			else {
				this.direction="down";
				sidewalkSegment = allSidewalks.get(20);
				currentCell = sidewalkSegment.get(0);
			}
		}
		if(getCurrentLane().equals("21_10")) {
			this.direction="left";
			sidewalkSegment = allSidewalks.get(9);
			currentCell = sidewalkSegment.get(26);
		}
		if(getCurrentLane().equals("10_0")) {
			this.direction="up";
			sidewalkSegment = allSidewalks.get(16);
			currentCell = sidewalkSegment.get(20);
		}
		if(getCurrentLane().equals("12_15")) {
			this.direction="down";
			sidewalkSegment = allSidewalks.get(13);
			currentCell = sidewalkSegment.get(0);
		}
		if(getCurrentLane().equals("14_5")) {
			this.direction="right";
			sidewalkSegment = allSidewalks.get(12);
			currentCell = sidewalkSegment.get(8);
		}
		if(getCurrentLane().equals("2_0")) {
			if(xDestination > 140) {
				//Cross
				if(yDestination < y) {
					//Go up
					this.direction="up";
					sidewalkSegment = allSidewalks.get(28);
					currentCell = sidewalkSegment.get(10);
					
				} else {
					//Go down
					this.direction="down";
					sidewalkSegment = allSidewalks.get(29);
					currentCell = sidewalkSegment.get(0);
				}
			} else {
				if(yDestination < y) {
					//Go up
					this.direction="up";
					sidewalkSegment = allSidewalks.get(26);
					currentCell = sidewalkSegment.get(10);

					
				} else {
					//Go down
					this.direction="down";
					sidewalkSegment = allSidewalks.get(27);
					currentCell = sidewalkSegment.get(0);
				}
			}
			
			
			
		}
		
		
	


		boolean canMove = true;
		if(time % 20 == 0) {
			if(getCurrentLane().equals("2_12")) {
				Sidewalk intersection = getSidewalkInformation("2_13");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("14_1")) {
				Sidewalk intersection = getSidewalkInformation("14_1");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("8_13")) {
				Sidewalk intersection = getSidewalkInformation("8_13");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("10_8")) {
				Sidewalk intersection = getSidewalkInformation("10_8");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("6_2")) {
				Sidewalk intersection = getSidewalkInformation("6_2");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("13_1")) {
				Sidewalk intersection = getSidewalkInformation("13_1");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("16_7")) {
				Sidewalk intersection = getSidewalkInformation("16_7");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("16_1")) {
				Sidewalk intersection = getSidewalkInformation("16_1");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("18_4")) {
				Sidewalk intersection = getSidewalkInformation("18_4");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("11_17")) {
				Sidewalk intersection = getSidewalkInformation("11_17");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("3_15")) {
				Sidewalk intersection = getSidewalkInformation("3_15");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("15_11")) {
				Sidewalk intersection = getSidewalkInformation("15_11");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("15_4")) {
				Sidewalk intersection = getSidewalkInformation("15_4");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			if(canMove) {
				this.move(currentCell.xVelocity,currentCell.yVelocity);
			}
		}
		}
	}
	public void redLight() {
		redLight = true;
	}
	
	public void greenLight() {
		redLight = false;
	}
	
	public void reachedDestination() {
		// TODO Auto-generated method stub
		
	}
}
