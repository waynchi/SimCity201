package bank.gui;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import bank.BankCustomerRole;

public class BankCustomerGui implements Gui{

	private BankCustomerRole agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
	private int custNum;
	private boolean isWaiting;
	

	//private HostAgent host;
	BankGui gui;
    private int xTable, yTable;
    private int xFinal, yFinal;
	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;
	
	static final int CUST_SIZE = 20;

	public BankCustomerGui(BankCustomerRole c, int custNum){ //HostAgent m) {
		agent = c;
		this.custNum = custNum;
		xPos = -20;
		yPos = 125;
		isWaiting = true;
		xDestination = 200 - (CUST_SIZE + 10)*custNum;
		yDestination = 125;
		setPresent(true);
		
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

		if (isWaiting) {
			xDestination = 200 - (CUST_SIZE + 10)*custNum;
			yDestination = 125;
		}
	}

	public void draw(Graphics2D g) {
			g.setColor(Color.GREEN);
			g.fillRect(xPos, yPos, CUST_SIZE, CUST_SIZE);
	}

	public boolean isPresent() {
		return isPresent;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void Goto(Dimension dim) {
		xTable= dim.width;
		yTable = dim.height;
	}

	public void DoExitRestaurant() {
		xDestination = xFinal;
		yDestination = yFinal;
		command = Command.LeaveRestaurant;
	}
	
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
