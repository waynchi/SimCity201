package restaurant_ps.gui;

import restaurant_ps.Choice;
import restaurant_ps.interfaces.Customer;

import java.awt.*;

public class CustomerGui implements Gui{

	private Customer agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
	
	Choice foodOnTable = null;

	//private HostAgent host;
	public RestaurantGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToHost, WaitingForWaiter, GoToSeat, LeaveRestaurant, GoToCashier, GoToWaiting};
	private Command command=Command.noCommand;
	
	int[] xTable = new int[3]; //3 tables
    int[] yTable = new int[3]; //3 tables
    
    boolean holdingCheck = false;

	public CustomerGui(Customer c, RestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = -40;
		yDestination = -40;
		//maitreD = m;
		this.gui = gui;
		xTable[0] = 250;
        yTable[0] = 250;
        xTable[1] = 250;
        yTable[1] = 150;
        xTable[2] = 250;
        yTable[2] = 50;
        
        
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
			if(command==Command.GoToHost)
			{
				//goToHost
				agent.msgAnimationFinishedGoToHost();
				command = Command.WaitingForWaiter;
			}
			else if (command==Command.GoToSeat) 
			{
				agent.msgAnimationFinishedGoToSeat();
			}
			else if (command == Command.GoToCashier)
			{
				holdingCheck = false;
				agent.msgAnimationFinishedGoToCashier();
			}
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				//System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				gui.setCustomerEnabled((restaurant_ps.interfaces.Customer) agent);
				
			}
			else if(command == Command.GoToWaiting) {
				agent.msgAnimationFinishedGoToWaiting();
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, 20, 20);
		g.setColor(Color.RED);
		g.drawString(agent.getCustomerName(), xPos, yPos);
		
		if(foodOnTable != null)
		{
			g.drawString(foodOnTable.food.foodname.substring(0, 3), xPos, yPos+45);
		}
		if(holdingCheck)
		{
			g.drawString("Check",xPos,yPos+15);
		}
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void DoGoToHost(int hostX, int hostY) {
		//System.out.println("Going to Host");
		xDestination = hostX-5;
		yDestination = hostY;
		
		command = Command.GoToHost;
	}

	public void DoGoToSeat(int seatnumber) {//later you will map seatnumber to table coordinates.
		//System.out.println("GOING TO SEAT " + seatnumber);
			
			xDestination = xTable[seatnumber-1];
			yDestination = yTable[seatnumber-1];
		
		command = Command.GoToSeat;
	}

	public void DoExitRestaurant() {
		foodOnTable = null;
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}
	public void DoEatFood(Choice myChoice) {
		// TODO Auto-generated method stub
		foodOnTable = myChoice;
	}
	
	public int getXPos() {
		return xPos;
	}
	
	public int getYPos() {
		return yPos;
	}

	public void DoGoToCashier() {
		// TODO Auto-generated method stub
		foodOnTable = null;
		holdingCheck = true;
		command = Command.GoToCashier;
		xDestination = 90;
		yDestination = 250;
	}

	public void DoNoFoodOnTable() {
		// TODO Auto-generated method stub
		foodOnTable = null;
	}

	public void DoGoToWaiting() {
		// TODO Auto-generated method stub
		xDestination = 50;
		yDestination = 200;
		command = Command.GoToWaiting;
	}

	
}
