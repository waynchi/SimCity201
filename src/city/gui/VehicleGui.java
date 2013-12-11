package city.gui;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


import transportation.BusStop;
import transportation.CarGui;


public class VehicleGui extends Rectangle2D.Double {
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
	public String typeOfVehicle;
	private ImageIcon img = null;
	protected boolean simulatingCrash = false;
	protected boolean simulatingPedestrianCrash = false;
	protected PersonGui simulatorPerson = null;
	
	public VehicleGui( int x, int y, int width, int height, ArrayList<Lane> laneSegment, Lane currentCell, ArrayList<ArrayList<Lane>> allLaneSegments, CityPanel cityPanel,String type ) {
		super( x, y, width, height );
		this.typeOfVehicle = type;
		this.laneSegment = laneSegment;
		this.currentCell = currentCell;
		rectangle = new Rectangle2D.Double( 100, 100, 20, 20 );
		this.setOrientation();
		this.allLanes = allLaneSegments;
		redLight = false;
		this.cityPanel = cityPanel;
		this.direction = "right";
		
		if(typeOfVehicle.equals("Bus"))
			img = new ImageIcon("res/transportation/busicon.png");
		else if(typeOfVehicle.equals("Car"))
			img = new ImageIcon("res/transportation/caricon.png");
			
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
		if(simulatingCrash)
			return;
		
		Lane nextCell;
		if(this.direction.equals("right")) {
			nextCell = laneSegment.get(laneSegment.indexOf(this.currentCell) + 1);
		}
		else if(this.direction.equals("left")) {
			nextCell = laneSegment.get(laneSegment.indexOf(this.currentCell) - 1);
		}
		else if(this.direction.equals("up")) {
			nextCell = laneSegment.get(laneSegment.indexOf(this.currentCell) - 1);
		}
		else {
			nextCell = laneSegment.get(laneSegment.indexOf(this.currentCell) + 1);
		}
		if(nextCell.hasCar && nextCell.simulatingCrash)
		{
			CarGui carToRemove = null;
			for(VehicleGui car : cityPanel.vehicles)
			{
				if(car.simulatingCrash)
				{
					carToRemove = (CarGui)car;
					break;
				}
			}
			cityPanel.vehicles.remove(carToRemove);
			carToRemove.currentCell.simulatingCrash = false;
			carToRemove.simulatingCrash = false;
			carToRemove.currentCell.hasCar = false;
			carToRemove.tellPersonGuiToWalk();
			cityPanel.city.cityControls.btnScenario7.setEnabled(true);
			if(this.typeOfVehicle.equals("Car"))
			{
				cityPanel.vehicles.remove(this);
				this.currentCell.hasCar = false;
				this.tellPersonGuiToWalk();
			}
			return;
		}
		if(!nextCell.hasCar) {
			if(currentCell.yVelocity > 0) {
				if(this.direction.equals("up")) {
					this.currentCell.hasCar = false;
					this.currentCell = laneSegment.get(laneSegment.indexOf(this.currentCell) - 1);
					this.currentCell.hasCar = true;

				} 
				if(this.direction.equals("down")) {
					this.currentCell.hasCar = false;
					this.currentCell = laneSegment.get(laneSegment.indexOf(this.currentCell) + 1);
					this.currentCell.hasCar = true;

				}
				
				
			} 
			if(currentCell.xVelocity > 0) {
				if(this.direction.equals("left")) {
					this.currentCell.hasCar = false;
					this.currentCell = laneSegment.get(laneSegment.indexOf(this.currentCell) - 1);
					this.currentCell.hasCar = true;

				} 
				if(this.direction.equals("right")) {
					this.currentCell.hasCar = false;
					this.currentCell = laneSegment.get(laneSegment.indexOf(this.currentCell) + 1);
					this.currentCell.hasCar = true;

				} 
			}
			this.setOrientation();
			
		}
		


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
		
		if(xDestination > 0 && yDestination > 0)
		{
			if(typeOfVehicle.equals("Car"))
				System.out.println(((CarGui)this).getDestination());	
			
		time++;
		if(typeOfVehicle.equals("Bus"))
			g2.setColor(Color.yellow);
		else
			g2.setColor( Color.blue );
		
		
		
//		g2.fill( this );
//		g2.draw(this);
//		
		g2.drawImage(img.getImage(), (int)x, (int)y, 20, 20, null);

		if(getCurrentLane().equals("19_1")) {
			this.direction="down";
		}
		if(getCurrentLane().equals("0_0")) {
			currentCell.hasCar = false;
			if(typeOfVehicle.equals("Bus")) {
				this.direction="right";
				laneSegment = allLanes.get(1);
				currentCell = laneSegment.get(0);
				
			} else {
				if(yDestination < y) {
					this.direction="up";
					laneSegment = allLanes.get(18);
					currentCell = laneSegment.get(6);
				}
				else{
					this.direction = "down";
					laneSegment = allLanes.get(21);
					currentCell = laneSegment.get(0);
				}
			}
		}
		if(getCurrentLane().equals("18_0")) {
			currentCell.hasCar = false;
			this.direction="down";
			laneSegment = allLanes.get(19);
			currentCell = laneSegment.get(0);
		}
		if(getCurrentLane().equals("19_5")) {
			this.direction="down";
		}
		if(getCurrentLane().equals("20_2") || getCurrentLane().equals("20_5") || getCurrentLane().equals("20_6") || getCurrentLane().equals("20_12")) {
			this.direction="up";
		}
		if(getCurrentLane().equals("21_4") || getCurrentLane().equals("21_6") || getCurrentLane().equals("21_8") || getCurrentLane().equals("21_12")) {
			this.direction="down";
		}
		if(getCurrentLane().equals("18_1") || getCurrentLane().equals("18_3") || getCurrentLane().equals("18_5")) {
			this.direction="up";
		}
		if(getCurrentLane().equals("19_7")) {
			currentCell.hasCar = false;
			this.direction="down";
			laneSegment = allLanes.get(21);
			currentCell = laneSegment.get(0);
		}
		if(getCurrentLane().equals("21_13")) {
			currentCell.hasCar = false;
			this.direction="up";
			laneSegment = allLanes.get(20);
			currentCell = laneSegment.get(laneSegment.size()-1);
		}
		if(getCurrentLane().equals("20_1")) {
			currentCell.hasCar = false;
			this.direction="right";
			laneSegment = allLanes.get(1);
			currentCell = laneSegment.get(0);
		}
		if(x == xDestination && y == yDestination) {

			if(typeOfVehicle.equals("Car")) {
				cityPanel.removeVehicle(this);
				currentCell.hasCar = false;

			}
			this.reachedDestination();
			return;
		}
		if(getCurrentLane().equals("12_4")) {
			currentCell.hasCar = false;

			
			this.direction="left";
			laneSegment = allLanes.get(0);
			currentCell = laneSegment.get(13);

		}
		else if(getCurrentLane().equals("3_0")) {
			currentCell.hasCar = false;


			this.direction="down";
			laneSegment = allLanes.get(12);
			currentCell = laneSegment.get(0);
			

		}
		else if(getCurrentLane().equals("5_1")) {
			currentCell.hasCar = false;


			//Intersection
			this.direction="left";
			laneSegment = allLanes.get(3);
			currentCell = laneSegment.get(16);

		}
		else if(getCurrentLane().equals("16_0")) {
			currentCell.hasCar = false;


			this.direction="left";
			laneSegment = allLanes.get(5);
			currentCell = laneSegment.get(9);

		}
		else if(getCurrentLane().equals("11_26")) {
			currentCell.hasCar = false;


			this.direction="up";
			laneSegment = allLanes.get(16);
			currentCell = laneSegment.get(15);

		}
		else if(getCurrentLane().equals("12_13")) {
			currentCell.hasCar = false;


			this.direction="right";
			laneSegment = allLanes.get(11);
			currentCell = laneSegment.get(0);

		}
		else if(getCurrentLane().equals("5_9") && this.direction!="left") {
			currentCell.hasCar = false;


			this.direction="down";
			laneSegment = allLanes.get(16);
			currentCell = laneSegment.get(0);

		}
		else if(getCurrentLane().equals("3_16") && this.direction!="left") {
			currentCell.hasCar = false;

			//Intersection
			this.direction="right";
			laneSegment = allLanes.get(5);
			currentCell = laneSegment.get(0);

		}
		else if(getCurrentLane().equals("12_0") && this.direction !="down") {
			currentCell.hasCar = false;


			this.direction = "right";
			laneSegment = allLanes.get(3);
			currentCell = laneSegment.get(0);

		}
		else if(getCurrentLane().equals("11_0")) {
			currentCell.hasCar = false;

			this.direction="up";
			laneSegment = allLanes.get(12);
			currentCell = laneSegment.get(13);

		}
		
		else if(getCurrentLane().equals("11_15")) {
			currentCell.hasCar = false;

			if(xDestination == 772 ) {
				this.direction="up";
				laneSegment = allLanes.get(15);
				currentCell = laneSegment.get(12);
			}
		}
		else if(getCurrentLane().equals("14_12")) {
			currentCell.hasCar = false;

			//Intersection
			if(x < xDestination) {
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
			
		
		else if(getCurrentLane().equals("9_9")) {
			currentCell.hasCar = false;

			//Intersection
			
			//Option #1
//			this.direction="down";
//			laneSegment = allLanes.get(17);
//			currentCell = laneSegment.get(7);
			
			//Option #2
			this.direction="up";
			laneSegment = allLanes.get(16);
			currentCell = laneSegment.get(6);

		}
		
		else if(getCurrentLane().equals("15_0")) {
			currentCell.hasCar = false;

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
		else if(getCurrentLane().equals("7_14")) {
			currentCell.hasCar = false;

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
		
		else if(getCurrentLane().equals("13_6")) {
			currentCell.hasCar = false;

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
		else if(getCurrentLane().equals("10_0")) {
			currentCell.hasCar = false;

			this.direction = "up";
			laneSegment = allLanes.get(13);
			currentCell = laneSegment.get(12);
		}
		else if(getCurrentLane().equals("10_16")) {
			currentCell.hasCar = false;

			//Intersection
			if((xDestination - x) < (yDestination - y)) {
				this.direction = "up";
				laneSegment = allLanes.get(15);
				currentCell = laneSegment.get(12);
			}
			
		}
		else if(getCurrentLane().equals("4_9")) {
			currentCell.hasCar = false;

			this.direction = "down";
			laneSegment = allLanes.get(17);
			currentCell = laneSegment.get(0);
		}
		else if(getCurrentLane().equals("17_4") && yDestination >= 132) {
			currentCell.hasCar = false;

			this.direction = "left";
			laneSegment = allLanes.get(8);
			int hackFirstCell = laneSegment.size() - 1;
			currentCell = laneSegment.get(hackFirstCell);
		}
		else if(getCurrentLane().equals("14_5"))
		{
			
			if(xDestination < x) //option 1
			{
			currentCell.hasCar = false;
			
			this.direction = "left";
			laneSegment = allLanes.get(6);
			int hackFirstCell = laneSegment.size() - 1;
			currentCell = laneSegment.get(hackFirstCell);
			}
			
		}
		else if(getCurrentLane().equals("17_14")) {
			currentCell.hasCar = false;

			this.direction = "left";
			int hackFirstCell = laneSegment.size() + 8;
			laneSegment = allLanes.get(10);
			currentCell = laneSegment.get(hackFirstCell);
		}
		else if(getCurrentLane().equals("2_16")) {
			currentCell.hasCar = false;

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
		else if(getCurrentLane().equals("13_0")) {
			currentCell.hasCar = false;

			this.direction = "right";
			laneSegment = allLanes.get(2);
			currentCell = laneSegment.get(0);
		
		}
		else if(getCurrentLane().equals("15_6")) {
			currentCell.hasCar = false;
			if(xDestination < x){
				this.direction = "left";
				laneSegment = allLanes.get(6);
				currentCell = laneSegment.get(laneSegment.size() - 1);
			}
			else if(xDestination > x){
				this.direction = "right";
				laneSegment = allLanes.get(9);
				currentCell = laneSegment.get(0);
			}
			
		}
		else if(getCurrentLane().equals("1_13")) {
			currentCell.hasCar = false;

			//Intersection
			//Option #1	
			if(yDestination < 152) {
				currentCell.hasCar = false;
				this.direction = "up";
				laneSegment = allLanes.get(13);
				currentCell = laneSegment.get(5);
				

			}
			else if(yDestination == 322) {
			//Option #3
				this.direction="down";
				laneSegment = allLanes.get(12);
				currentCell = laneSegment.get(6);
				//check if pedestrian is there
				if(simulatingPedestrianCrash && simulatorPerson != null)
				{
					ArrayList<Sidewalk> sidewalksegment = cityPanel.allSidewalks.get(8);
					Sidewalk sidewalk = sidewalksegment.get(9);
					simulatorPerson.setSideWalkSegment(sidewalksegment);
					simulatorPerson.setSidewalk(sidewalk);
					simulatorPerson.setDirection("left");
					simulatorPerson.simulatingCrash = false;
					cityPanel.city.pedestrianCrashSimulationFinished();
					
				}
			}
			//Option #2
			else {
				this.direction = "right";
				laneSegment = allLanes.get(7);
				currentCell = laneSegment.get(0);

			}
			
			
			
			
		}
		else if(getCurrentLane().equals("16_7")) {
			currentCell.hasCar = false;
			//Intersection
			
			//Option #1
			if(yDestination >= 132){
				this.direction="left";
				laneSegment = allLanes.get(8);
				currentCell = laneSegment.get(7);
			}
			else{
				this.direction="up";
				laneSegment = allLanes.get(16);
				currentCell = laneSegment.get(6);
			}
		}
		else if(getCurrentLane().equals("8_1")) {
			currentCell.hasCar = false;
			//Intersection
			
			//Option #1
			if(xDestination < x ){
				this.direction="left";
				laneSegment = allLanes.get(6);
				currentCell = laneSegment.get(laneSegment.size() - 1);
			}//Option#2
			else if(yDestination > y){
				this.direction="down";
				laneSegment = allLanes.get(15);
				currentCell = laneSegment.get(10);
				
			}//Option#2
			else if(yDestination < y){
				this.direction="up";
				laneSegment = allLanes.get(15);
				currentCell = laneSegment.get(6);
				
			}
		}
		else if(getCurrentLane().equals("6_0")) {
			currentCell.hasCar = false;
			//Intersection
			
			//Option #1
			if(xDestination < x ){
				this.direction="left";
				laneSegment = allLanes.get(0);
				currentCell = laneSegment.get(laneSegment.size() - 1);
			}
			else if(yDestination < y){
				this.direction ="up";
				laneSegment = allLanes.get(13);
				currentCell = laneSegment.get(1);
			}
			else{
				this.direction = "down";
				laneSegment = allLanes.get(12);
				currentCell = laneSegment.get(6);
				if(simulatingPedestrianCrash && simulatorPerson != null)
				{
					ArrayList<Sidewalk> sidewalksegment = cityPanel.allSidewalks.get(8);
					Sidewalk sidewalk = sidewalksegment.get(9);
					simulatorPerson.setSideWalkSegment(sidewalksegment);
					simulatorPerson.setSidewalk(sidewalk);	
					simulatorPerson.setDirection("left");
				}
			}
		}


		boolean canMove = true;
		if(time % 5 == 0) {
			if(getCurrentLane().equals("1_12")) {
				Lane intersection = getLaneInformation("1_13");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("7_13")) {
				Lane intersection = getLaneInformation("7_13");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("9_8")) {
				Lane intersection = getLaneInformation("9_8");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("5_2")) {
				Lane intersection = getLaneInformation("5_2");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("12_1")) {
				Lane intersection = getLaneInformation("12_1");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("15_7")) {
				Lane intersection = getLaneInformation("15_7");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("15_1")) {
				Lane intersection = getLaneInformation("15_1");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("17_4")) {
				Lane intersection = getLaneInformation("17_4");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("10_17")) {
				Lane intersection = getLaneInformation("10_17");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("2_15")) {
				Lane intersection = getLaneInformation("2_15");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("14_11")) {
				Lane intersection = getLaneInformation("14_11");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("14_4")) {
				Lane intersection = getLaneInformation("14_4");
				if(intersection.redLight) {
					canMove = false;
				}
			}

			else if(getCurrentLane().equals("16_8")) {
				Lane intersection = getLaneInformation("16_8");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("11_13")) {
				Lane intersection = getLaneInformation("11_13");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("8_2")) {
				Lane intersection = getLaneInformation("8_2");
				if(intersection.redLight) {
					canMove = false;
				}
			}
			else if(getCurrentLane().equals("6_1")) {
				Lane intersection = getLaneInformation("6_1");
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
	public void driveHere(String place) {
		// TODO Auto-generated method stub
		if(typeOfVehicle.equals("Car"))
		{
		for(Building b : cityPanel.buildings)
		{
			if(b.name.equals(place))
			{
				this.setDestination(b.xLocation, b.yLocation);
				return;
			}
		}
		}

//		else if(typeOfVehicle.equals("Bus"))
//		{
//			for(BusStop bs : cityPanel.busStops)
//			{
//				if(bs.name.equals(place))
//				{
//					this.setDestination(bs.xLocation, bs.yLocation);
//					return;
//				}
//			}
//		}

		else if(typeOfVehicle.equals("Bus"))
		{
			for(BusStop bs : cityPanel.busStops)
			{
				if(bs.name.equals(place))
				{
					this.setDestination(bs.xLocation, bs.yLocation);
					return;
				}
			}
		}

		
	}
	//gets overwritten by cargui
	protected void stopNow() {
		// TODO Auto-generated method stub
	}
	
	public void setSimulatorPerson(PersonGui p)
	{
		simulatorPerson = p;
	}
	
	public boolean isCurrentlyDriving(){
		if(xDestination > 0 && yDestination >0)
			return true;
		else
			return false;
	}
	//gets overwritten by cargui
	protected void tellPersonGuiToWalk(){
	}
}
