package city.gui;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;

import people.People;
import transportation.BusPassengerRole;

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
	HashMap<Integer, Sidewalk> distances;
	String direction;
	Color vehicleColor;
	int time;
	public String typeOfVehicle;
	People person;
	boolean called;
	String destination;
	private ImageIcon custsprite = new ImageIcon("res/person_gui.png");
	public boolean simulatingCrash;

	public PersonGui(int x, int y, int width, int height,
			ArrayList<Sidewalk> sidewalkSegment, Sidewalk currentCell,
			ArrayList<ArrayList<Sidewalk>> allsidewalkSegments,
			CityPanel cityPanel, People person) {
		super(x, y, width, height);
		this.sidewalkSegment = sidewalkSegment;
		this.currentCell = currentCell;
		rectangle = new Rectangle2D.Double(100, 100, 20, 20);
		this.setOrientation();
		this.allSidewalks = allsidewalkSegments;
		redLight = false;
		this.cityPanel = cityPanel;
		this.direction = "right";
		this.person = person;
		called = false;
		simulatingCrash = false;

	}

	public void setSidewalk(Lane l) {
		this.currentCell = sidewalkSegment.get(sidewalkSegment.indexOf(l));
	}

	public void setLocation(int x, int y) {
		setRect(x, y, getWidth(), getHeight());
	}

	public Color getColor() {
		return vehicleColor;
	}

	public void move(double xVelocity, double yVelocity) {
		if(simulatingCrash)
			return;
		Sidewalk nextCell;
		if (this.direction.equals("right")) {
			nextCell = sidewalkSegment.get(sidewalkSegment
					.indexOf(this.currentCell) + 1);
		} else if (this.direction.equals("left")) {
			nextCell = sidewalkSegment.get(sidewalkSegment
					.indexOf(this.currentCell) - 1);
		} else if (this.direction.equals("up")) {
			nextCell = sidewalkSegment.get(sidewalkSegment
					.indexOf(this.currentCell) - 1);
		} else {
			nextCell = sidewalkSegment.get(sidewalkSegment
					.indexOf(this.currentCell) + 1);
		}
		if (!nextCell.hasPerson) {
			if (currentCell.yVelocity > 0) {
				if (this.direction.equals("up")) {
					this.currentCell.hasPerson = false;
					this.currentCell = sidewalkSegment.get(sidewalkSegment
							.indexOf(this.currentCell) - 1);
					this.currentCell.hasPerson = true;

				}
				if (this.direction.equals("down")) {
					this.currentCell.hasPerson = false;
					this.currentCell = sidewalkSegment.get(sidewalkSegment
							.indexOf(this.currentCell) + 1);
					this.currentCell.hasPerson = true;

				}

			}
			if (currentCell.xVelocity > 0) {
				if (this.direction.equals("left")) {
					this.currentCell.hasPerson = false;
					this.currentCell = sidewalkSegment.get(sidewalkSegment
							.indexOf(this.currentCell) - 1);
					this.currentCell.hasPerson = true;
				}
				if (this.direction.equals("right")) {
					this.currentCell.hasPerson = false;
					this.currentCell = sidewalkSegment.get(sidewalkSegment
							.indexOf(this.currentCell) + 1);
					this.currentCell.hasPerson = true;

				}
			}
			this.setOrientation();
		}
		nextCell.setPerson(person);
	}

	public void setDestination(String destination) {
		called = true;
		this.destination = destination;
		
		//BUS STOPS
		if(destination.equals("Bus Stop 1")) {
			xDestination = 240;
			yDestination = 152;
		}
		else if(destination.equals("Bus Stop 2")) {
			xDestination = 700;
			yDestination = 322;
		}
		else if(destination.equals("Bus Stop 3")) {
			xDestination = 860;
			yDestination = 102;
		}
		else if(destination.equals("Bus Stop 4")) {
			xDestination = 680;
			yDestination = 102;
		}
		//LEFT HOMES
		else if(destination.equals("Home 1")) {
			xDestination = 92;
			yDestination = 10;
		}
		else if(destination.equals("Home 2")) {
			xDestination = 92;
			yDestination = 20;
		}
		else if(destination.equals("Home 3")) {
			xDestination = 92;
			yDestination = 60;
		}
		else if(destination.equals("Home 4")) {
			xDestination = 92;
			yDestination = 180;
		}
		else if(destination.equals("Home 5")) {
			xDestination = 92;
			yDestination = 220;
		}
		else if(destination.equals("Home 6")) {
			xDestination = 92;
			yDestination = 260;
		}
		
		//RIGHT HOMES
		else if(destination.equals("Home 7")) {
			xDestination = 142;
			yDestination = 10;
		}
		else if(destination.equals("Home 8")) {
			xDestination = 142;
			yDestination = 20;
		}
		else if(destination.equals("Home 9")) {
			xDestination = 142;
			yDestination = 60;
		}
		else if(destination.equals("Home 10")) {
			xDestination = 142;
			yDestination = 180;
		}
		else if(destination.equals("Home 11")) {
			xDestination = 142;
			yDestination = 220;
		}
		else if(destination.equals("Home 12")) {
			xDestination = 142;
			yDestination = 260;
		}
		else if (destination.equals("Apartment 1")) {
			xDestination = 142;
			yDestination = 340;
		}
		else if (destination.equals("Apartment 2")) {
			xDestination = 92;
			yDestination = 340;
		}
		else if(destination.equals("Black Abyss")) {
			xDestination = 0;
			yDestination = 0;
		}
		else if(destination.equals("Wander")) {
			xDestination = 1;
			yDestination = 1;
		}
		else if(destination.equals("Restaurant 1")) {
			xDestination = 840;
			yDestination = 42;
		}
		else if(destination.equals("Restaurant 2")) {
			xDestination = 500;
			yDestination = 42;
		}
		else if(destination.equals("Restaurant 3")) {
			xDestination = 840;
			yDestination = 152;
		}
		else if(destination.equals("Restaurant 4")) {
			xDestination = 792;
			yDestination = 200;
		}
		else if(destination.equals("Restaurant 5")) {
			xDestination = 720;
			yDestination = 42;
		}
		else if(destination.equals("Restaurant 6")) {
			xDestination = 1002;
			yDestination = 343;
		}
		else if(destination.equals("Market")) {
			xDestination = 580;
			yDestination = 322;
		}
		else if(destination.equals("Bank")) {
			xDestination = 580;
			yDestination = 152;
		}
	}

	public void setOrientation() {
		if (currentCell.xVelocity > 0) {
			this.setRect(currentCell.xOrigin, currentCell.yOrigin + 2,
					this.getWidth(), this.getHeight());
		} else if (currentCell.yVelocity > 0) {
			this.setRect(currentCell.xOrigin + 2, currentCell.yOrigin,
					this.getWidth(), this.getHeight());
		} else {
			if (currentCell.isHorizontal) {
				this.setRect(currentCell.xOrigin + width - this.getWidth(),
						currentCell.yOrigin + 2, this.getWidth(),
						this.getHeight());
			} else {
				this.setRect(currentCell.xOrigin + 2, currentCell.yOrigin
						+ height - this.getHeight(), this.getWidth(),
						this.getHeight());
			}
		}
	}

	public String getCurrentLane() {
		int l = sidewalkSegment.indexOf(currentCell);
		return sidewalkSegment.get(l).name;

	}

	public Sidewalk getSidewalkInformation(String name) {
		Sidewalk sidewalkReturn = null;
		for (ArrayList<Sidewalk> sidewalk : this.allSidewalks) {
			for (Sidewalk cell : sidewalk) {
				if (cell.name.equals(name)) {
					sidewalkReturn = cell;
				}
			}
		}
		return sidewalkReturn;
	}

	public void draw(Graphics2D g2) {
		if (xDestination > 0 && yDestination > 0) {
			time++;
			//g2.setColor(Color.red);
			//g2.fill(this);
			//g2.draw(this);
			
			g2.drawImage(custsprite.getImage(), (int)x, (int)y, null);

			if (x == xDestination && y == (yDestination + 20) && called == true) {
				this.reachedDestination(this.destination);
				this.currentCell.hasPerson = false;
				called = false;
				cityPanel.removePerson(this);
				person.msgDone("PersonGui");
				this.destination = null;
				
			}
			else if (x == 1002.0 && y == 210.0 && called == true) { //hack for peppy
				this.reachedDestination(this.destination);
				this.currentCell.hasPerson = false;
				called = false;
				cityPanel.removePerson(this);
				person.msgDone("PersonGui");
				this.destination = null;
			}
			
			//Exiting Building Logic
			
			//Home 1
			if(getCurrentLane().equals("28_1")) {
				this.direction = "down";				
			}
			//Home 2
			if(getCurrentLane().equals("28_5")) {
				this.direction = "down";
			}
			//Home 3
			if(getCurrentLane().equals("28_10")) {
				this.direction = "down";
			}
			//Home 4
			if(getCurrentLane().equals("26_1")) {
				this.currentCell.hasPerson = false;
				sidewalkSegment = allSidewalks.get(28);
				currentCell = sidewalkSegment.get(1);
				this.currentCell.hasPerson = true;
			}
			//Home 5
			if(getCurrentLane().equals("26_5")) {
				this.direction="up";
			}
			//Home 6
			if(getCurrentLane().equals("26_10")) {
				this.direction="up";
				
			}
			//Home 7
			if(getCurrentLane().equals("29_8")) {
				this.direction="down";
			}
			//Home 8
			if(getCurrentLane().equals("29_12")) {
				this.direction="down";
			}
			//Home 9
			if(getCurrentLane().equals("29_16")) {
				this.direction="down";
			}
			//Home 10
			if(getCurrentLane().equals("27_2")) {
				this.direction="up";
			}
			//Home 11
			if(getCurrentLane().equals("27_6")) {
				this.direction="up";
			}
			//Home 12
			if(getCurrentLane().equals("27_10")) {
				this.direction = "up";
			}
			//Apartment 1
			if(getCurrentLane().equals("27_18")) {
				this.direction="up";
			}
			//Apartment 2
			if(getCurrentLane().equals("29_24")) {
				this.direction="down";
			}
			if(getCurrentLane().equals("28_11")) {
				this.currentCell.hasPerson = false;
				this.direction="down";
				sidewalkSegment = allSidewalks.get(29);
				this.currentCell = sidewalkSegment.get(0);
				this.currentCell.hasPerson = true;
			}
			//Bottom Cross
			if(getCurrentLane().equals("29_29")) {
				this.currentCell.hasPerson = false;
				this.direction="up";
				sidewalkSegment = allSidewalks.get(27);
				this.currentCell = sidewalkSegment.get(23);
				this.currentCell.hasPerson = true;
			}
			if(getCurrentLane().equals("27_0")) {
				this.currentCell.hasPerson = false;
				this.direction="right";
				sidewalkSegment = allSidewalks.get(0);
				this.currentCell = sidewalkSegment.get(0);
				this.currentCell.hasPerson = true;
			}
			//Restaurant Wayne
			if(getCurrentLane().equals("20_5")) {
				this.currentCell.hasPerson = false;
				this.direction = "up";
			}
			if(getCurrentLane().equals("12_15")) {
				this.currentCell.hasPerson = false;
				this.direction = "up";
			}
			if(getCurrentLane().equals("6_20")) {
				this.currentCell.hasPerson = false;
				this.direction = "up";
				sidewalkSegment = allSidewalks.get(12);
				currentCell = sidewalkSegment.get(15);
				this.currentCell.hasPerson = true;

			}
			if(getCurrentLane().equals("24_3")) {
				this.currentCell.hasPerson = false;
				if(yDestination < y) {
					this.direction = "up";
					sidewalkSegment = allSidewalks.get(20);
					currentCell = sidewalkSegment.get(sidewalkSegment.size()-1);
					this.currentCell.hasPerson = true;

				}
			}
			if (getCurrentLane().equals("7_0")) {
				this.currentCell.hasPerson = false;
				this.direction = "left";
				sidewalkSegment = allSidewalks.get(23);
				currentCell = sidewalkSegment.get(sidewalkSegment.size()-1);
				this.currentCell.hasPerson = true;


			}
			if (getCurrentLane().equals("22_0")) {
				this.currentCell.hasPerson = false;
				this.direction = "left";
				sidewalkSegment = allSidewalks.get(2);
				currentCell = sidewalkSegment.get(sidewalkSegment.size()-1);
				this.currentCell.hasPerson = true;


			}
			if (getCurrentLane().equals("20_0")) {
				this.currentCell.hasPerson = false;
				if (yDestination > y) {
					this.direction = "right";
					sidewalkSegment = allSidewalks.get(6);
					currentCell = sidewalkSegment.get(0);
					this.currentCell.hasPerson = true;

				} else {
					this.direction = "left";
					sidewalkSegment = allSidewalks.get(22);
					currentCell = sidewalkSegment.get(sidewalkSegment.size()-1);
					this.currentCell.hasPerson = true;

				}
			}

			if (getCurrentLane().equals("0_20")) {
				this.currentCell.hasPerson = false;
				this.direction = "right";
				sidewalkSegment = allSidewalks.get(2);
				currentCell = sidewalkSegment.get(0);
				this.currentCell.hasPerson = true;
			

			}

			if(getCurrentLane().equals("2_10")) {
				this.currentCell.hasPerson = false;
				//Crossed the first street. Up or down?
				if(yDestination < 152) {
					this.direction = "up";
					sidewalkSegment = allSidewalks.get(16);
					currentCell = sidewalkSegment.get(16);
					this.currentCell.hasPerson = true;

				}
				if(yDestination == 152) {
					this.direction = "right";
					sidewalkSegment = allSidewalks.get(22);
					currentCell = sidewalkSegment.get(0);
					this.currentCell.hasPerson = true;

				}
							
			}
			if (getCurrentLane().equals("2_5")) {
				// Intersection
				if(xDestination > 200) {
					if (yDestination < 152) {
						// We need to cross
						this.direction = "right";
						
					}
					if (yDestination == 152) {
						this.direction = "right";
					}
					if (yDestination > 152) {
						this.direction = "down";
						sidewalkSegment = allSidewalks.get(18);
						this.currentCell.hasPerson = false;
						currentCell = sidewalkSegment.get(0);
						this.currentCell.hasPerson = true;

					}
				} else { //Go backwards, we want to go to the residential area
					this.direction="left";
					sidewalkSegment = allSidewalks.get(3);
					currentCell = sidewalkSegment.get(sidewalkSegment.size()-1);
					this.currentCell.hasPerson = true;
				}
			}
			if (getCurrentLane().equals("16_5")) {
				this.currentCell.hasPerson = false;
				this.direction = "right";
				sidewalkSegment = allSidewalks.get(10);
				currentCell = sidewalkSegment.get(4);
				this.currentCell.hasPerson = true;
			}
			if(getCurrentLane().equals("10_36")) {
				this.currentCell.hasPerson = false;
				//Crossed the sidewalk. Continue to next segment
				this.direction = "right";
				sidewalkSegment = allSidewalks.get(11);
				currentCell = sidewalkSegment.get(0);
				this.currentCell.hasPerson = true;
			}
			if (getCurrentLane().equals("10_32")) {
				this.currentCell.hasPerson = false;
				if (xDestination > x) {
					this.direction = "right";
				} else {
					this.direction = "down";
					sidewalkSegment = allSidewalks.get(15);
					currentCell = sidewalkSegment.get(4);
					this.currentCell.hasPerson = true;
				}

			}
			if (getCurrentLane().equals("15_5")) {
				this.currentCell.hasPerson = false;
				this.direction = "left";
				sidewalkSegment = allSidewalks.get(23);
				currentCell = sidewalkSegment.get(28);
				this.currentCell.hasPerson = true;
			}
			if (getCurrentLane().equals("23_0")) {
				this.currentCell.hasPerson = false;
				this.direction = "left";
				sidewalkSegment = allSidewalks.get(3);
				currentCell = sidewalkSegment.get(sidewalkSegment.size()-1);
				this.currentCell.hasPerson = true;
			}
			if (getCurrentLane().equals("3_0")) {
				this.currentCell.hasPerson = false;
				this.direction = "left";
				sidewalkSegment = allSidewalks.get(1);
				currentCell = sidewalkSegment.get(sidewalkSegment.size()-1);
				this.currentCell.hasPerson = true;
			}
			if (getCurrentLane().equals("18_17")) {
				this.currentCell.hasPerson = false;
				this.direction = "right";
				sidewalkSegment = allSidewalks.get(25);
				currentCell = sidewalkSegment.get(0);
				this.currentCell.hasPerson = true;
			}
			if (getCurrentLane().equals("25_33")) {
				this.currentCell.hasPerson = false;
				this.direction = "right";
				sidewalkSegment = allSidewalks.get(24);
				currentCell = sidewalkSegment.get(0);
				this.currentCell.hasPerson = true;
			}			
			if (getCurrentLane().equals("24_24")) {
				this.currentCell.hasPerson = false;
				this.direction = "up";
				sidewalkSegment = allSidewalks.get(12);
				currentCell = sidewalkSegment.get(sidewalkSegment.size()-1);
				this.currentCell.hasPerson = true;
			}
			if (getCurrentLane().equals("12_0")) {
				this.currentCell.hasPerson = false;
				this.direction = "left";
				sidewalkSegment = allSidewalks.get(5);
				currentCell = sidewalkSegment.get(sidewalkSegment.size()-1);
				this.currentCell.hasPerson = true;
			}
			if (getCurrentLane().equals("5_0")) {
				this.currentCell.hasPerson = false;
				this.direction = "left";
				sidewalkSegment = allSidewalks.get(4);
				currentCell = sidewalkSegment.get(sidewalkSegment.size()-1);
				this.currentCell.hasPerson = true;
			}
			if (getCurrentLane().equals("4_0")) {
				this.currentCell.hasPerson = false;
				this.direction = "down";
				sidewalkSegment = allSidewalks.get(17);
				currentCell = sidewalkSegment.get(0);
				this.currentCell.hasPerson = true;
			}
			if (getCurrentLane().equals("17_10")) {
				this.currentCell.hasPerson = false;
				this.direction = "left";
				sidewalkSegment = allSidewalks.get(3);
				currentCell = sidewalkSegment.get(sidewalkSegment.size()-1);
				this.currentCell.hasPerson = true;
			}
			if(getCurrentLane().equals("22_36")) {
				this.currentCell.hasPerson = false;
				this.direction="right";
				sidewalkSegment = allSidewalks.get(6);
				currentCell = sidewalkSegment.get(0);
				this.currentCell.hasPerson = true;
			}
			if (getCurrentLane().equals("22_32")) {
				this.currentCell.hasPerson = false;
				if (yDestination < y) {
					this.direction = "right";

				} else {
					this.direction = "down";
					sidewalkSegment = allSidewalks.get(20);
					currentCell = sidewalkSegment.get(0);
					this.currentCell.hasPerson = true;
				}
			}
			if (getCurrentLane().equals("9_0")) {
				this.currentCell.hasPerson = false;
				this.direction = "up";
				sidewalkSegment = allSidewalks.get(16);
				currentCell = sidewalkSegment.get(sidewalkSegment.size()-1);
				this.currentCell.hasPerson = true;
			}
			if(getCurrentLane().equals("11_19")) {
				this.currentCell.hasPerson = false;
				this.direction = "up";
				sidewalkSegment = allSidewalks.get(12);
				currentCell = sidewalkSegment.get(4);
				this.currentCell.hasPerson = true;
			}
			if (getCurrentLane().equals("11_15")) {
				//Cross the street
				this.currentCell.hasPerson = false;
				this.direction = "right";
			}
			if (getCurrentLane().equals("1_0")) {
				this.currentCell.hasPerson = false;
				this.direction = "up";
				sidewalkSegment = allSidewalks.get(26);
				currentCell = sidewalkSegment.get(12);
				currentCell.hasPerson = true;
			}

			boolean canMove = true;
			if (time % 10 == 0) {
				if (getCurrentLane().equals("2_5")) {
					Sidewalk intersection = getSidewalkInformation("2_5");
					if (intersection.redLight) {
						canMove = false;
					}
				}
				else if (getCurrentLane().equals("22_32")) {
					Sidewalk intersection = getSidewalkInformation("22_32");
					if (intersection.redLight) {
						canMove = false;
					}
				}
				else if (getCurrentLane().equals("6_16")) {
					Sidewalk intersection = getSidewalkInformation("6_16");
					if (intersection.redLight) {
						canMove = false;
					}
				}
				else if (getCurrentLane().equals("11_15")) {
					Sidewalk intersection = getSidewalkInformation("11_15");
					if (intersection.redLight) {
						canMove = false;
					}
				}
				else if (getCurrentLane().equals("10_32")) {
					Sidewalk intersection = getSidewalkInformation("10_32");
					if (intersection.redLight) {
						canMove = false;
					}
				}
				else if (getCurrentLane().equals("23_4")) {
					Sidewalk intersection = getSidewalkInformation("23_4");
					if (intersection.redLight) {
						canMove = false;
					}
				}
				else if (getCurrentLane().equals("16_15")) {
					Sidewalk intersection = getSidewalkInformation("16_15");
					if (intersection.redLight) {
						canMove = false;
					}
				}
				else if (getCurrentLane().equals("24_3")) {
					Sidewalk intersection = getSidewalkInformation("24_3");
					if (intersection.redLight) {
						canMove = false;
					}
				}
				

				if (canMove) {
					this.move(currentCell.xVelocity, currentCell.yVelocity);
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

	public void reachedDestination(String reachedDestination) {
		// TODO Auto-generated method stub
		if(reachedDestination.equals("Bus Stop 1"))
		{
			BusPassengerRole bpr = new BusPassengerRole();
			bpr.setCurrentBusStop(cityPanel.busStops.get(0));
			bpr.setDestinationPlace("Bank");
			bpr.msgIsActive();
			person.addRole(bpr, "BusPassenger");
			
		}
	}

	public void stopNow() {
		// TODO Auto-generated method stub
		simulatingCrash = true;
		this.currentCell.simulatingCrash = true;
	}
}
