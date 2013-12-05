package transportation.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;
import java.util.concurrent.Semaphore;

import transportation.BusPassengerRole;
import transportation.BusStop;
import transportation.Gui;
import transportation.interfaces.BusPassenger;

public class BusStopPassengerGui implements Gui{
	int xDestination = 0, yDestination = 0;
	int xPos = 0, yPos = 0;
	int xExit = -10, yExit = -10;
	boolean isPresent;
	BusPassenger bpr;
	public enum Command {nothing,leaving};
	Command command;
	BusStop currentBusStop;
	
	public BusStopPassengerGui(BusPassenger bpr, BusStop currentBusStop){
		Random rand = new Random();
		int  randomX = rand.nextInt(400) + 1;
		int randomY = rand.nextInt(300) + 1;
		xDestination = randomX;
		yDestination = randomY;
		this.bpr = bpr;
		this.currentBusStop = currentBusStop;
	}
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		//System.out.println(xPos+","+yPos);
		 if (xPos < xDestination)
	            xPos++;
	     else if (xPos > xDestination)
	            xPos--;

	     if (yPos < yDestination)
	            yPos++;
	     else if (yPos > yDestination)
	            yPos--;
	     if(xPos == xDestination && yPos == yDestination){
	    	 if(command == Command.leaving)
	    	 {
	    		 currentBusStop.msgAnimationFinishedDoLeaveBusStop(bpr);
	    		 command = Command.nothing;
	    		 setPresent(false);
	    	 }
	     }
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(Color.yellow);
		g.fillRect(xPos, yPos, 20, 20);
        g.setColor(Color.BLACK);
        g.drawString("BusPassenger", xPos, yPos+20);
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return isPresent;
	}

	/**
	 * @param args
	 */
	public void setPresent(boolean b) {
		isPresent = b;
		// TODO Auto-generated method stub
		
	}
	public void DoLeaveBusStop() {
		// TODO Auto-generated method stub
		xDestination = xExit;
		yDestination = yExit;
		command = Command.leaving;
	}

}
