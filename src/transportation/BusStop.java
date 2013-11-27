package transportation;
import transportation.interfaces.Bus;
import transportation.interfaces.BusPassenger;

import java.awt.geom.Rectangle2D;
import java.util.*;

public class BusStop extends Rectangle2D.Double{
public List<BusPassenger> waitingPassengers = Collections.synchronizedList(new ArrayList<BusPassenger>());
public List<BusPassenger> boardingPassengers = Collections.synchronizedList(new ArrayList<BusPassenger>());
Bus currentBus;
public int xLocation;
public int yLocation;
public String name;

public BusStop( int x, int y, int width, int height, int xLoc, int yLoc, String name){
	super(x,y,width,height);
	this.xLocation = xLoc;
	this.yLocation = yLoc;
	this.name = name;
}


public void msgWaitingHere(BusPassenger bpr){
System.out.println("BusStop recieved message that a bus passenger arrived");
waitingPassengers.add(bpr);
}

public void msgBusArrived(Bus b){
	try{
	//System.out.println("BusStop recieved message that bus has arrived");
	currentBus = b;
	boardingPassengers = waitingPassengers;
	if(boardingPassengers.isEmpty())
	{
		currentBus.msgAllBusStopPassengersNotified();
		currentBus = null;
		return;
	}
	
	for(BusPassenger passenger : boardingPassengers) 
        passenger.msgBusArrived(b);
	}catch(ConcurrentModificationException e){
		return;
	}
	
}
public void msgLeavingBusStop(BusPassenger bpr){
	
if(boardingPassengers.contains(bpr))
	boardingPassengers.remove(bpr);
waitingPassengers.remove(bpr);
if(boardingPassengers.isEmpty())
{
	currentBus.msgAllBusStopPassengersNotified();
	currentBus = null;
}
	

}

}

