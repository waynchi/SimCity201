package restaurant.gui;

import restaurant.interfaces.Customer;

import java.awt.*;
import javax.swing.ImageIcon;

public class CustomerGui implements Gui{

	private Customer customer = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	//private HostAgent host;
	RestaurantGui gui;

	private int xPos, yPos;
	private int xExit = 0, yExit = 0;
	private int xDestination, yDestination;//??
	private enum Command {noCommand, GoToSeat, LeaveRestaurant, WaitingForFood, Eating};
	private Command command=Command.noCommand;
	private String choice = new String("");
	
	private Boolean MsgGoToSeatFromAgent = false;
	private boolean leaving = false;
	private boolean goingToCashier = false;
    private ImageIcon restaurant_customer = new ImageIcon("res/custsprite_1.png");

	
	
	// set the initial position of customer
	public CustomerGui(Customer c){ //HostAgent m) {
		customer = c;
		xPos = yPos = 0;
		xDestination = 20;
		yDestination = 20;
	}

	
	public void updatePosition() {
		if (xPos < xDestination && Math.abs(xDestination - xPos) > 1)
			xPos += 2;
		else if (xPos > xDestination && Math.abs(xDestination - xPos) > 1)
			xPos -= 2;
		
		if (yPos < yDestination && Math.abs(yDestination - yPos) > 1)
			yPos += 2;
		else if (yPos > yDestination && Math.abs(yDestination - yPos) > 1)
			yPos -= 2;

		if (Math.abs(xPos - xDestination) < 2 && Math.abs(yPos - yDestination) < 2) {
			xPos = xDestination;
			yPos = yDestination;

			if (command==Command.GoToSeat) customer.msgAtTable();
			else if (xDestination == 340 && yDestination == 175 && goingToCashier) {
				goingToCashier = false;
				customer.msgAtCashier();
				
			}
			else if (leaving) {
				customer.msgAtExit();
				isHungry = false;
				leaving = false;
				//gui.setCustomerEnabled((RestaurantCustomerRole) customer);
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
        g.drawImage(restaurant_customer.getImage(), xPos, yPos, 20, 30, null);

    	// Display customer's order with text
		g.setColor(Color.black);
		if (customer.getState().equalsIgnoreCase("waiting_for_food")) g.drawString(choice+"?",xPos+20, yPos+30);
		if (customer.getState().equalsIgnoreCase("eating")) g.drawString(choice,xPos+20, yPos+30);
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		customer.gotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToSeat(int seatnumber) {//later you will map seatnumber to table coordinates.
		MsgGoToSeatFromAgent = true;
		command = Command.GoToSeat;
	}
	
	public void DoGoToPosition(int xDest, int yDest) {
		//if (MsgGoToSeatFromAgent) {
			xDestination = xDest-50;
			yDestination = yDest;
		//}
	}
	
	public void DoGoToCashier() {
		// how could customer know where cashier is? I don't know...
		command = Command.noCommand;
		goingToCashier = true;
		xDestination = 340;
		yDestination = 175;
		
	}
	
	public void madeDecision (String c) {
		choice = c.substring(0,2);
		command = Command.WaitingForFood;
	}
	
	public void eatFood() {
		command = Command.Eating;
	}

	public void DoExitRestaurant() {
		xDestination = xExit;
		yDestination = yExit;
		leaving = true;
	}
}
