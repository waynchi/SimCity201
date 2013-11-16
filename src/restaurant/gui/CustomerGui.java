package restaurant.gui;

import restaurant.RestaurantCustomerRole;
import restaurant.HostRole;
import restaurant.interfaces.Customer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JLabel;

public class CustomerGui implements Gui{

	private Customer customer = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	//private HostAgent host;
	RestaurantGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;//??
	private enum Command {noCommand, GoToSeat, LeaveRestaurant, WaitingForFood, Eating};
	private Command command=Command.noCommand;
	private String choice = new String("");
	
	private Boolean MsgGoToSeatFromAgent = false;
    private BufferedImage img = null;

	
	
	// set the initial position of customer
	public CustomerGui(Customer c, RestaurantGui gui){ //HostAgent m) {
		customer = c;
		xPos = -40;
		yPos = -40;
		xDestination = 20;
		yDestination = 10;
		this.gui = gui;
		try {
            img = ImageIO.read(getClass().getResource("customer.png"));
        } catch (IOException e) {}
        
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
			else if (xDestination == 250 && yDestination == 230) customer.msgAtCashier();
			else if (command==Command.LeaveRestaurant) {
				customer.msgAnimationFinishedLeaveRestaurant();
				isHungry = false;
				gui.setCustomerEnabled((RestaurantCustomerRole) customer);
			}
			//command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
    	g.drawImage(img,xPos,yPos,null);

    	// Display customer's order with text
		g.setColor(Color.BLACK);
		if (command == Command.WaitingForFood) g.drawString(choice+"?",xPos+20, yPos+30);
		if (command == Command.Eating) g.drawString(choice,xPos+20, yPos+30);
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
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}
}
