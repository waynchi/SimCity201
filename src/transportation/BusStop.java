package transportation;
import transportation.gui.BusStopGui;
import transportation.gui.BusStopPassengerGui;
import transportation.interfaces.Bus;
import transportation.interfaces.BusPassenger;

import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.concurrent.Semaphore;

import city.gui.BuildingPanel;

public class BusStop extends Rectangle2D.Double{
	class MyBusPassenger{
		BusPassenger bp;
		BusStopPassengerGui bg;
		MyBusPassenger(BusPassenger bp, BusStopPassengerGui bg){
			this.bp = bp;
			this.bg = bg;
		}
	}
public List<MyBusPassenger> myWaitingPassengers = Collections.synchronizedList(new ArrayList<MyBusPassenger>());
public List<MyBusPassenger> myBoardingPassengers = Collections.synchronizedList(new ArrayList<MyBusPassenger>());
Bus currentBus;
public int xLocation;
public int yLocation;
public String name;
public BusStopGui myGui;
public BuildingPanel bp;

public BusStop( BusStopGui bg, int x, int y, int width, int height, int xLoc, int yLoc, String name){
	super(x,y,width,height);
	this.xLocation = xLoc;
	this.yLocation = yLoc;
	this.name = name;
	this.myGui = bg;
}


public void msgWaitingHere(BusPassenger bpr){
System.out.println("BusStop recieved message that a bus passenger arrived");
MyBusPassenger waitingPassenger = new MyBusPassenger(bpr, new BusStopPassengerGui(bpr,this));
myGui.getAnimationPanel().addGui(waitingPassenger.bg);
waitingPassenger.bg.setPresent(true);
myWaitingPassengers.add(waitingPassenger);
}

public void msgBusArrived(Bus b){
	try{
	System.out.println("BusStop recieved message that bus has arrived");
	currentBus = b;
	myBoardingPassengers = myWaitingPassengers;
	b.msgNumberOfBoardingPassengers(myBoardingPassengers.size());
	if(myBoardingPassengers.isEmpty())
	{
		currentBus.msgAllBusStopPassengersNotified();
		currentBus = null;
		return;
	}
	
	for(MyBusPassenger passenger : myBoardingPassengers) 
	{
		passenger.bp.msgBusArrived(b);
	}
	
	}catch(ConcurrentModificationException e){
		return;
	}
	
}
public void msgLeavingBusStop(BusPassenger bpr){
	
//	for(MyBusPassenger passenger : myBoardingPassengers) 
//	{
//       if(passenger.bp == bpr){
//    	   myBoardingPassengers.remove(passenger);
//       }
//	}
	
	for(MyBusPassenger passenger : myWaitingPassengers) 
	{
       if(passenger.bp == bpr){
    	   passenger.bg.DoLeaveBusStop();
       }
	}	



	

}


public void msgAnimationFinishedDoLeaveBusStop(BusPassenger bpr) {
	// TODO Auto-generated method stub
	try{
	currentBus.msgImBoarding(bpr);
	for(MyBusPassenger passenger : myWaitingPassengers) 
	{
		if(passenger.bp == bpr)
		{
			myWaitingPassengers.remove(passenger);
    	   if(myBoardingPassengers.contains(passenger))
    		   myBoardingPassengers.remove(passenger);
       }
	}	
	
//	System.out.println("size of boarding passengers: " + myBoardingPassengers.size());
//	if(myBoardingPassengers.isEmpty())
//	{
//		currentBus.msgAllBusStopPassengersNotified();
//		currentBus = null;
//	}
}catch(ConcurrentModificationException e){
	return;
}
}

public void setGui(BusStopGui bg)
{
	myGui = bg;
}

public BusStopGui getGui(){
	return myGui;
}


public void displayBuilding() {
	// TODO Auto-generated method stub
	bp.displayBuildingPanel();
}


public void setBuildingPanel(BuildingPanel bp) {
	// TODO Auto-generated method stub
	this.bp = bp;
}

}


