package bank.gui;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import bank.BankCustomerRole;

public class BankCustomerGui implements Gui{

	private BankCustomerRole agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
	private int custNum;
	boolean isWaiting;
	

	//private HostAgent host;
	public BankGui gui;
    private int xTable, yTable;
    private int xFinal, yFinal;
	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;
	private ImageIcon custsprite = new ImageIcon("res/bank/custsprite.png");
	
	static final int CUST_SIZE = 20;

	public BankCustomerGui(BankCustomerRole c){ //HostAgent m) {
		agent = c;
		xPos = -20;
		yPos = 125;
		isWaiting = false;
		xDestination = -20;
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
		
		if (xPos == 280 && yPos == 90) {
			agent.msgAtTeller();
		}
	}

	public void draw(Graphics2D g) {
		g.drawImage(custsprite.getImage(),xPos,yPos,null);
			//g.setColor(Color.GREEN);
			//g.fillRect(xPos, yPos, CUST_SIZE, CUST_SIZE);
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
	
	public void DoGoToTeller() {
		isWaiting = false;
		xDestination = 280;
		yDestination = 90;
	}
	
	public void DoLeaveBank() {
		xDestination = -20;
		yDestination = 125;
	}
	
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public void setCust(int num) {
    	custNum = num;
    }
}
