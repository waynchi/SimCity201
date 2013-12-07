package restaurant_ps.gui;

import restaurant_ps.Choice;
import restaurant_ps.CookAgent;
import restaurant_ps.Order;
import restaurant_ps.interfaces.Customer;
import restaurant.interfaces.Cook;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CookGui implements Gui{

	private CookAgent agent = null;
	private boolean isPresent = false;
	Choice holdingOrder = null;
	int cookGuiWidth = 30;
	int cookGuiHeight = 30;
	int cookGuiX = 100;
	int cookGuiY = 350;
	int fridgeX = 70;
	int fridgeY = 430;
	int platingAreaX = 170;
	int platingAreaY = 350;
	int[] xGrill = new int[3]; //3 grills
	int[] yGrill = new int[3];
	//private HostAgent host;
	public RestaurantGui gui;
	
	public boolean isHomePositionPresent = false;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToCustomer, GoToSeat, GoToSeatWithFood, GoToCook, LeaveRestaurant, GoToCashier, GoToSeatWithCheck, GoToHomePosition, GoToFridge, GoToGrill, GoToGrillToPickUpFood, GoToPlatingArea, nothing};
	private Command command=Command.noCommand;
	
	
	int[] xTable = new int[3]; //3 tables
    int[] yTable = new int[3]; //3 tables
    
    Customer lastSeatedCustomer = null;
    Customer customerToBeSeated = null;
    Customer customerOrderToGiveToCashier = null;
    
    int lastVisitedTable = -1;
    
    boolean holdingCheck = false;
    
    class FoodOnGrill{
    	Order o;
    	int grillnumber;
    	boolean onGrill;
    	FoodOnGrill(Order or, int gn){
    		o = or;
    		grillnumber = gn;
    		onGrill = false;
    	}
    }
    
    FoodOnGrill mostRecent = null;
    FoodOnGrill foodToRemove = null;
    Order foodInHand = null;
    public List<FoodOnGrill> myFoodOnGrill = Collections.synchronizedList(new ArrayList<FoodOnGrill>());
    public List<Order> platedFood = Collections.synchronizedList(new ArrayList<Order>());
	public CookGui(CookAgent c, RestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = cookGuiX;
		yPos = cookGuiY;
		xDestination = xPos;
		yDestination = yPos;
		
		xGrill[0] = 20;
		xGrill[1] = 20;
		xGrill[2] = 20;
		yGrill[0] = 330;
		yGrill[1] = 370;
		yGrill[2] = 410;
	}

	public void updatePosition() {
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;

		if (xPos == xDestination && yPos == yDestination) {
			if(command == Command.GoToFridge)
			{
				agent.msgAnimationFinishedGoToFridge();
				command = Command.nothing;
			}
			else if(command == Command.GoToGrill)
			{
				agent.msgAnimationFinishedGoToGrill(mostRecent.grillnumber,mostRecent.o);
				mostRecent.onGrill = true;
				command = Command.nothing;
			}
			else if(command == Command.GoToGrillToPickUpFood)
			{
				agent.msgAnimationFinishedGoToGrillToPickUpFood(mostRecent.o);
				mostRecent.onGrill = false;
				myFoodOnGrill.remove(mostRecent);
				mostRecent = null;
				command = Command.nothing;
			}
			else if(command == Command.GoToPlatingArea)
			{
				agent.msgAnimationFinishedGoToPlatingArea(foodInHand);
				platedFood.add(foodInHand);
				foodInHand = null;
				command = Command.nothing;
			}
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(xPos, yPos, cookGuiWidth, cookGuiHeight);
		g.setColor(Color.RED);
		g.drawString(agent.getName(), xPos, yPos);
		g.drawString("Grill 1",xGrill[0],yGrill[0]);
		g.drawString("Grill 2",xGrill[1],yGrill[1]);
		g.drawString("Grill 3",xGrill[2],yGrill[2]);
		
		g.setColor(Color.GRAY);
		//Grill 1
		g.fillRect(xGrill[0],yGrill[0],cookGuiWidth,cookGuiHeight);
		
		
		//Grill 2
		g.fillRect(xGrill[1],yGrill[1],cookGuiWidth,cookGuiHeight);
		
		//Grill 3
		g.fillRect(xGrill[2],yGrill[2],cookGuiWidth,cookGuiHeight);
		
		//Refrigerator
		g.fillRect(fridgeX, fridgeY, cookGuiWidth+30, 10);
		g.drawString("Fridge", fridgeX, fridgeY);
		
		//Plating area
		g.fillRect(platingAreaX,platingAreaY,30,80);
		
		synchronized(myFoodOnGrill)
		{
		for(FoodOnGrill fog : myFoodOnGrill)
		{
			if(fog.onGrill)
			{
				g.setColor(Color.RED);
				g.drawString(fog.o.o.food.foodname, xGrill[fog.grillnumber-1], yGrill[fog.grillnumber-1]+15);
			}
		}
		}
		if(foodInHand != null)
		{
			g.setColor(Color.RED);
			g.drawString(foodInHand.o.food.foodname, xPos, yPos+10);
		}
		synchronized(platedFood)
		{
		for(Order o : platedFood)
		{
			g.setColor(Color.RED);
			g.drawString(o.o.food.foodname, platingAreaX, platingAreaY);
		}
		}
	}
	

	public boolean isPresent() {
		return isPresent;
	}
	

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToFridge() {
		// TODO Auto-generated method stub
		xDestination = fridgeX;
		yDestination = fridgeY;
		command = Command.GoToFridge;
	}

	public void DoGoToGrill(int grillNumber, Order o) {
		// TODO Auto-generated method stub
		xDestination = xGrill[grillNumber-1] + 30;
		yDestination = yGrill[grillNumber-1];
		command = Command.GoToGrill;
		mostRecent = new FoodOnGrill(o,grillNumber);
		myFoodOnGrill.add(mostRecent);
	}

	public void DoGoToGrillToPickUpFood(int grillNumber, Order order) {
		// TODO Auto-generated method stub
		xDestination = xGrill[grillNumber-1] + 30;
		yDestination = yGrill[grillNumber-1];
		command = Command.GoToGrillToPickUpFood;
		mostRecent = findGrill(grillNumber);
		
		
	}

	private FoodOnGrill findGrill(int grillNumber) {
		// TODO Auto-generated method stub
		for(FoodOnGrill fog : myFoodOnGrill)
		{
			if(fog.grillnumber == grillNumber)
				return fog;
		}
		return null;
	}

	public void DoGoToPlateAreaWithFood(Order order) {
		// TODO Auto-generated method stub
		xDestination = platingAreaX-30;
		yDestination = platingAreaY+40;
		command = Command.GoToPlatingArea;
		foodInHand = order;
	}

	public void msgDoRemoveOrderFromPlatingArea(Order o) {
		// TODO Auto-generated method stub
		Order plated = findPlatedFood(o); 
		platedFood.remove(plated);
	}

	private Order findPlatedFood(Order o) {
		// TODO Auto-generated method stub
		for(Order platedFoods : platedFood)
		{
			if(platedFoods == o)
				return platedFoods;
		}
		return null;
	}

	
	
	

	
}
