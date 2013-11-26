package bank.gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import bank.BankCustomerRole;
import bank.TellerRole;

public class TellerGui implements Gui{

	private TellerRole agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
	private int custNum;
	public boolean isAtDesk;
	

	//private HostAgent host;
	BankGui gui;
    private int xTable, yTable;
    private int xFinal, yFinal;
	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;
	private ImageIcon host = new ImageIcon("res/bank/tellersprite.png");
	public Boolean leave = false;
	
	static final int CUST_SIZE = 20;

	public TellerGui(TellerRole c){ //HostAgent m) {
		agent = c;
		this.custNum = custNum;
		xPos = 520;
		yPos = 90;
		isAtDesk = true;
		xDestination = 312;
		yDestination = 90;
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

		if (isAtDesk) {
			xDestination = 312;
			yDestination = 90;
		}
		
		if (xPos == 520 && yPos == 90 && leave) {
			agent.msgGone();
		}
	}

	public void draw(Graphics2D g) {
		g.drawImage(host.getImage(),xPos,yPos,null);
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
		xDestination = 520;
		yDestination = 90;
	}
	
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
