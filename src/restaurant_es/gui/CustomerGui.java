package restaurant_es.gui;

import restaurant_es.interfaces.Customer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class CustomerGui implements Gui{

	private Customer customer = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
	
	RestaurantGuiEs gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant, WaitingForFood, Eating};
	private Command command=Command.noCommand;
	private String choice = new String("");
	
	private boolean MsgGoToSeatFromAgent = false;
	private boolean leaving = false;
	private boolean goingToCashier = false;
    private ImageIcon img = new ImageIcon("res/cust_1.png");
    
    private int xFinal = -20;
    private int yFinal = -20;

	public final int CUST_SIZE = 20;
	
	// set the initial position of customer
	public CustomerGui(Customer c){ //HostAgent m) {
		xPos = -20;
		yPos = -20;
		xDestination = 20;
		yDestination = 20;
		setPresent(true);
		customer = c;
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
			if (command==Command.GoToSeat) customer.msgAtTable();
			else if (xDestination == 250 && yDestination == 230 && goingToCashier) {
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
    	g.drawImage(img.getImage(), xPos, yPos, null);

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
	
	public void GoToCashier() {
		command = Command.noCommand;
		goingToCashier = true;
		xDestination = 250;
		yDestination = 230;
		
	}
	
	public void madeDecision (String c) {
		choice = c.substring(0,2);
		command = Command.WaitingForFood;
	}
	
	public void eatFood() {
		command = Command.Eating;
	}

	public void DoExitRestaurant() {
		xDestination = xFinal;
		yDestination = yFinal;
		leaving = true;
	}
}
