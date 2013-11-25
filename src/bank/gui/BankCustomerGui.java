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
	
	private String choice = null;

	//private HostAgent host;
	BankGui gui;
    private int xTable, yTable;
    private int xFinal, yFinal;
	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;
	
	static final int CUST_SIZE = 20;

	public BankCustomerGui(BankCustomerRole c, BankGui gui, int custNum){ //HostAgent m) {
		agent = c;
		this.custNum = custNum;
		xPos = -20;
		yPos = 100 + (CUST_SIZE +10)*custNum;
		xDestination = -20;
		yDestination = 100 + (CUST_SIZE +10)*custNum;
		xFinal = -20;
		yFinal = 100 + (CUST_SIZE +10)*custNum;
		//maitreD = m;
		this.gui = gui;
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

		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat) {}
			else if (command==Command.LeaveRestaurant) {
				System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		if (choice == null) {
			g.setColor(Color.GREEN);
			g.fillRect(xPos, yPos, CUST_SIZE, CUST_SIZE);
		}
		else {
			g.setColor(Color.GREEN);
			g.fillRect(xPos, yPos, CUST_SIZE, CUST_SIZE);
			g.setColor(Color.BLACK);
			g.drawString(choice, xPos, yPos+10);
		}
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		xDestination = 0;
		yDestination = 100 + (CUST_SIZE +10)*custNum;
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void Goto(Dimension dim) {
		xTable= dim.width;
		yTable = dim.height;
	}

	public void DoGoToSeat() {//later you will map seatnumber to table coordinates.
		while(true) {
			if(xTable != 0 && yTable != 0) {
		        xDestination = xTable;
		        yDestination = yTable;
		        command = Command.GoToSeat;
		        break;
			}
		}
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
