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
	public ArrayList<Lane> laneSegment;
	public Lane currentCell;
	public CityPanel cityPanel;
	boolean redLight;
	Rectangle2D.Double rectangle;
	ArrayList<Lane> bestRoute;
	ArrayList<ArrayList<Lane>> allLanes;
	HashMap<Integer,Lane> distances;
	String direction;
	Color vehicleColor;
	int time;
	
	public Vehicle( int x, int y, int width, int height, ArrayList<Lane> laneSegment, Lane currentCell, ArrayList<ArrayList<Lane>> allLaneSegments, CityPanel cityPanel ) {
		super( x, y, width, height );
		this.laneSegment = laneSegment;
		this.currentCell = currentCell;
		rectangle = new Rectangle2D.Double( 100, 100, 20, 20 );
		this.setOrientation();
		this.allLanes = allLaneSegments;
		redLight = false;
		this.cityPanel = cityPanel;
		this.direction = "right";


	}
	public void setLane(Lane l) {
		this.currentCell = laneSegment.get(laneSegment.indexOf(l));
	}
	public void setLocation( int x, int y ) {
		setRect( x, y, getWidth(), getHeight() );
	}
	
	public Color getColor() {
		return vehicleColor;
	}
	
	public void move( int xv, int yv ) {
		if(currentCell.yVelocity > 0) {
			if(this.direction.equals("up")) {
				this.currentCell = laneSegment.get(laneSegment.indexOf(this.currentCell) - 1);
			} 
			if(this.direction.equals("down")) {
				this.currentCell = laneSegment.get(laneSegment.indexOf(this.currentCell) + 1);
			}
			
			
		} 
		if(currentCell.xVelocity > 0) {
			if(this.direction.equals("left")) {
				this.currentCell = laneSegment.get(laneSegment.indexOf(this.currentCell) - 1);
			} 
			if(this.direction.equals("right")) {
				this.currentCell = laneSegment.get(laneSegment.indexOf(this.currentCell) + 1);
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
		int l = laneSegment.indexOf(currentCell);
		return laneSegment.get(l).name; 
		
	}
	
	public Lane getLaneInformation(String name) {
		Lane laneReturn = null;
		for(ArrayList<Lane> lane : this.allLanes) {
			for(Lane cell : lane) {
				if(cell.name.equals(name)) {
					laneReturn = cell;
				}
			}
		}
		return laneReturn;
	}
	
	public void draw(Graphics2D g2) {
		time++;
		g2.setColor( Color.blue );
		g2.fill( this );
		g2.draw(this);
		
		if(x == xDestination && y == yDestination) {
			cityPanel.removeVehicle(this);
		}
		if(getCurrentLane().equals("13_2")) {
			this.direction="left";
			laneSegment = allLanes.get(0);
			currentCell = laneSegment.get(13);
		}
		if(getCurrentLane().equals("4_0")) {
			this.direction="down";
			laneSegment = allLanes.get(12);
			currentCell = laneSegment.get(1);
		}
		if(getCurrentLane().equals("6_1")) {
			//Intersection
			this.direction="left";
			laneSegment = allLanes.get(3);
			currentCell = laneSegment.get(15);
		}
		if(getCurrentLane().equals("17_0")) {
			this.direction="left";
			laneSegment = allLanes.get(5);
			currentCell = laneSegment.get(8);
		}
		if(getCurrentLane().equals("12_24")) {
			this.direction="up";
			laneSegment = allLanes.get(16);
			currentCell = laneSegment.get(15);
		}
		if(getCurrentLane().equals("13_13")) {
			this.direction="right";
			laneSegment = allLanes.get(11);
			currentCell = laneSegment.get(1);
		}
		if(getCurrentLane().equals("6_9")) {
			this.direction="down";
			laneSegment = allLanes.get(16);
			currentCell = laneSegment.get(0);
		}
		if(getCurrentLane().equals("4_16")) {
			//Intersection
			this.direction="right";
			laneSegment = allLanes.get(5);
			currentCell = laneSegment.get(0);
		}
		if(getCurrentLane().equals("13_0")) {
			this.direction = "right";
			laneSegment = allLanes.get(3);
			currentCell = laneSegment.get(0);
		}
		if(getCurrentLane().equals("12_0")) {
			this.direction="up";
			laneSegment = allLanes.get(12);
			currentCell = laneSegment.get(13);
		}
		
		if(getCurrentLane().equals("12_15")) {
			if(xDestination == 772) {
				this.direction="up";
				laneSegment = allLanes.get(15);
				currentCell = laneSegment.get(12);
			}
		}
		if(getCurrentLane().equals("15_12")) {
			//Intersection
			if(x > xDestination) {
				//Option 1
				this.direction="right";
				laneSegment = allLanes.get(11);
				currentCell = laneSegment.get(15);
			} else {
				//Option #2
				this.direction="left";
				laneSegment = allLanes.get(10);
				currentCell = laneSegment.get(15);
				}
			}
			
		
		if(getCurrentLane().equals("10_9")) {
			//Intersection
			
			//Option #1
//			this.direction="down";
//			laneSegment = allLanes.get(17);
//			currentCell = laneSegment.get(7);
			
			//Option #2
			this.direction="up";
			laneSegment = allLanes.get(16);
			currentCell = laneSegment.get(7);
		}
		
		if(getCurrentLane().equals("16_0")) {
			//Intersection
			//Option #1
//			this.direction="left";
//			laneSegment = allLanes.get(3);
//			currentCell = laneSegment.get(15);
//			
			//Option #2
			this.direction="right";
			laneSegment = allLanes.get(4);
			currentCell = laneSegment.get(0);
		}
		if(getCurrentLane().equals("8_14")) {
			//Intersection
			
			if(yDestination < 152) {
				this.direction="up";
				laneSegment = allLanes.get(15);
				currentCell = laneSegment.get(5);
			}
			else if(yDestination == 152) {
				this.direction="right";
				laneSegment = allLanes.get(9);
				currentCell = laneSegment.get(0);
			}
			else if(yDestination > 152) {
				this.direction="down";
				laneSegment = allLanes.get(14);
				currentCell = laneSegment.get(5);
			
			}

			
		}
		
		if(getCurrentLane().equals("14_6")) {
			//Intersection
			//Option #1
			if(yDestination == 152) {
				this.direction = "right";
				laneSegment = allLanes.get(7);
				currentCell = laneSegment.get(0);
			} else {
				this.direction = "left";
				laneSegment = allLanes.get(0);
				currentCell = laneSegment.get(13);
			}
		}
		if(getCurrentLane().equals("11_0")) {
			this.direction = "up";
			laneSegment = allLanes.get(13);
			currentCell = laneSegment.get(12);
		}
		if(getCurrentLane().equals("11_16")) {
			//Intersection
			if((xDestination - x) < (yDestination - y)) {
				this.direction = "up";
				laneSegment = allLanes.get(15);
				currentCell = laneSegment.get(12);
			}
			
		}
		if(getCurrentLane().equals("5_9")) {
			this.direction = "down";
			laneSegment = allLanes.get(17);
			currentCell = laneSegment.get(0);
		}
		if(getCurrentLane().equals("18_14")) {
			this.direction = "left";
			int hackFirstCell = laneSegment.size() + 8;
			System.out.println(laneSegment.size());
			laneSegment = allLanes.get(10);
			currentCell = laneSegment.get(hackFirstCell);
		}
		if(getCurrentLane().equals("3_16")) {
			//Intersection
			if(xDestination > 752) {
				this.direction="right";
				laneSegment = allLanes.get(4);
				currentCell = laneSegment.get(0);
			} else {
				this.direction="down";
				laneSegment = allLanes.get(14);
				currentCell = laneSegment.get(0);
			}
		}
		if(getCurrentLane().equals("14_0")) {
			this.direction = "right";
			laneSegment = allLanes.get(2);
			currentCell = laneSegment.get(0);
		
		}

		if(getCurrentLane().equals("2_13")) {
			//Intersection
			//Option #1	
			if(yDestination < 152) {
				this.direction = "up";
				laneSegment = allLanes.get(13);
				currentCell = laneSegment.get(5);
			}
			//Option #2
			else if(yDestination == 152) {
				this.direction = "right";
				laneSegment = allLanes.get(7);
				currentCell = laneSegment.get(0);
			}
			else if(yDestination > 152) {
			//Option #3
				this.direction="down";
				laneSegment = allLanes.get(12);
				currentCell = laneSegment.get(3);
			}
			
			
		}


		boolean canMove = true;
		System.out.println(getCurrentLane());
		if(time % 20 == 0) {
			if(getCurrentLane().equals("2_12")) {
				Lane intersection = getLaneInformation("2_13");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("14_1")) {
				Lane intersection = getLaneInformation("14_1");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("8_13")) {
				Lane intersection = getLaneInformation("8_13");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("10_8")) {
				Lane intersection = getLaneInformation("10_8");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("6_2")) {
				Lane intersection = getLaneInformation("6_2");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("13_1")) {
				Lane intersection = getLaneInformation("13_1");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("16_7")) {
				Lane intersection = getLaneInformation("16_7");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("16_1")) {
				Lane intersection = getLaneInformation("16_1");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("18_4")) {
				Lane intersection = getLaneInformation("18_4");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("11_17")) {
				Lane intersection = getLaneInformation("11_17");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("3_15")) {
				Lane intersection = getLaneInformation("3_15");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("15_11")) {
				Lane intersection = getLaneInformation("15_11");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("15_4")) {
				Lane intersection = getLaneInformation("15_4");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			if(canMove) {
				this.move(currentCell.xVelocity,currentCell.yVelocity);
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
