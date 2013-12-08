package restaurant_ps.gui;

import restaurant_ps.Choice;
import restaurant_ps.interfaces.Customer;
import restaurant_ps.interfaces.Waiter;

import java.awt.*;

public class WaiterGui implements Gui{

	private Waiter agent = null;
	private boolean isPresent = false;
	Choice holdingOrder = null;
	int waiterGuiWidth = 30;
	int waiterGuiHeight = 30;
	int waiterHomeX ;
	int waiterHomeY ;
	//private HostAgent host;
	public RestaurantGui gui;
	
	public boolean isHomePositionPresent = false;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToCustomer, GoToSeat, GoToSeatWithFood, GoToCook, LeaveRestaurant, GoToCashier, GoToSeatWithCheck, GoToHomePosition};
	private Command command=Command.noCommand;
	
	
	int[] xTable = new int[3]; //3 tables
    int[] yTable = new int[3]; //3 tables
    
    Customer lastSeatedCustomer = null;
    Customer customerToBeSeated = null;
    Customer customerOrderToGiveToCashier = null;
    Customer lookingForFoodFor = null;
    int startX = waiterHomeX - 50;
    int startY = waiterHomeY - 10;
    
    int lastVisitedTable = -1;
    
    boolean holdingCheck = false;
	private boolean isAtHomePosition = true;

	public WaiterGui(Waiter w, RestaurantGui gui, int waiterHomeX, int waiterHomeY){ //HostAgent m) {
		agent = w;
		xPos = -20;
		yPos = -20;
		xDestination = xPos;
		yDestination = yPos;
		//maitreD = m;
		this.gui = gui;
		xTable[0] = 250;
        yTable[0] = 250;
        xTable[1] = 250;
        yTable[1] = 150;
        xTable[2] = 250;
        yTable[2] = 50;
        this.waiterHomeX = waiterHomeX;
        this.waiterHomeY = waiterHomeY;
	}

	public void updatePosition() {
		if(xPos == waiterHomeX-50 && yPos == waiterHomeY-10)
			isAtHomePosition = true;
		else
			isAtHomePosition = false;
		
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;

		if (xPos == xDestination && yPos == yDestination) {
			if(command == Command.GoToCustomer && customerToBeSeated != null)
			{
				agent.msgAnimationFinishedGoToCustomer(customerToBeSeated);
				customerToBeSeated = null;
				command = null;
			}
			else if(command == Command.GoToSeat && lastVisitedTable != -1)
			{
				agent.msgAnimationFinishedGoToSeat(lastVisitedTable);
				lastVisitedTable = -1;
				command = null;
			}
			else if(command == Command.GoToSeatWithCheck && holdingCheck)
			{
				agent.msgAnimationFinishedGoToSeat(lastVisitedTable);
				lastVisitedTable = -1;
				holdingCheck = false;
				command = null;
			}
			else if(command == Command.GoToSeatWithFood && lastVisitedTable != -1)
			{
				agent.msgAnimationFinishedGoToSeat(lastVisitedTable);
				lastVisitedTable = -1;
				holdingOrder = null;
				command = null;
			}
			else if(command == Command.GoToCook)
			{
				agent.msgAnimationFinishedGoToCook(lookingForFoodFor);
				lookingForFoodFor = null;
				command = null;
			
			}
			else if(command == Command.GoToCashier && customerOrderToGiveToCashier != null)
			{
				agent.msgAnimationFinishedGoToCashier(customerOrderToGiveToCashier);
				customerOrderToGiveToCashier = null;
				command = null;
			}
			else if(command == Command.GoToHomePosition)
			{
				agent.msgAnimationFinishedGoToHomePosition();
				command = null;
			}
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(xPos, yPos, waiterGuiWidth, waiterGuiHeight);
		g.setColor(Color.RED);
		g.drawString(agent.getName(), xPos, yPos);
		if(holdingOrder != null)
		{
			g.drawString(holdingOrder.food.foodname.substring(0, 3)+"?",xPos,yPos+15);
		}
		if(holdingCheck)
		{
			g.drawString("Check?",xPos,yPos+15);
		}
		if(agent.isOnBreak())
		{
			g.drawString("OnBreak", xPos, yPos+40);
		}
		
		g.drawString(getWaiterName() + "'s Home", waiterHomeX, waiterHomeY);
	}

	public boolean isPresent() {
		return isPresent;
	}
	

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToSeat(int seatnumber) {//later you will map seatnumber to table coordinates.
		//System.out.println("GOING TO SEAT " + seatnumber);
			
			xDestination = xTable[seatnumber-1] - 30;
			yDestination = yTable[seatnumber-1];
		
		command = Command.GoToSeat;
		lastVisitedTable = seatnumber;
	}
	
	public void DoGoToSeatWithOrder(int seatnumber,Choice o) {//later you will map seatnumber to table coordinates.
		//System.out.println("GOING TO SEAT " + seatnumber);
			holdingOrder = o;
			xDestination = xTable[seatnumber-1] - 30;
			yDestination = yTable[seatnumber-1];
		
		command = Command.GoToSeatWithFood;
		lastVisitedTable = seatnumber;
	}
	
	public void DoGoToSeatWithCheck(int seatnumber) {
		// TODO Auto-generated method stub
		xDestination = xTable[seatnumber-1] - 30;
		yDestination = yTable[seatnumber-1];
	
	command = Command.GoToSeatWithCheck;
	lastVisitedTable = seatnumber;
	System.out.println("GOING TO SEAT: " + seatnumber);
	holdingCheck = true;
	}


	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}

	public void DoGoToCustomer(Customer cust) {
		// TODO Auto-generated method stub
		xDestination = cust.getGui().getXPos();
		yDestination = cust.getGui().getYPos() + 30;
		
		command = Command.GoToCustomer;
		customerToBeSeated = cust;
	}
	
	

	public void DoGoToCook(Customer c) {
		// TODO Auto-generated method stub
		yDestination = 330;
		xDestination = 170;
		
		command = Command.GoToCook;
		lookingForFoodFor = c;
		
	}

	public void DoGoToBreakArea() {
		// TODO Auto-generated method stub
		yDestination = 200;
		xDestination = 100;
	}

	public void DoGoToCashier(Customer cust) {
		// TODO Auto-generated method stub
		
		xDestination = 90;
		yDestination = 250;
		
		command = Command.GoToCashier;
		customerOrderToGiveToCashier = cust;
	}

	public String getWaiterName() {
		return agent.getName();
	}

	public boolean isAtHomePosition() {
		// TODO Auto-generated method stub
		return isAtHomePosition;
	}

	public void DoGoToHomePosition() {
		// TODO Auto-generated method stub
		xDestination = waiterHomeX-50;
		yDestination = waiterHomeY-10;
		command = Command.GoToHomePosition;
	}

	public void DoEnterRestaurant() {
		// TODO Auto-generated method stub
		xDestination = startX;
		yDestination = startY;
	}

	public void DoLeaveRestaurant() {
		// TODO Auto-generated method stub
		xDestination = -20;
		yDestination = -20;
	}

	
	
	

	
}
