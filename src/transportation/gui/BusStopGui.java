package transportation.gui;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

import transportation.BusAgent;
import transportation.BusPassengerRole;
import transportation.BusStop;
import transportation.interfaces.Bus;


import city.gui.CityGui;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class BusStopGui extends JFrame implements ActionListener {
    
	BusStopPanel animationPanel = new BusStopPanel();
	BusStop bs;
	BusPassengerRole bpr,bpr1,bpr2;
	Bus busAgent;
    public BusStopGui() {
        int WINDOWX = 500;
        int WINDOWY = 400;
        
        Dimension animationDim = new Dimension(WINDOWX, WINDOWY);
        animationPanel.setPreferredSize(animationDim);
        animationPanel.setMinimumSize(animationDim);
        animationPanel.setMaximumSize(animationDim);

    	setBounds(50, 50, WINDOWX, WINDOWY);
        add(animationPanel);
    	setVisible(true);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	busAgent = new BusAgent();
    	bs = new BusStop(this,0,0,0,0,0,0,"busstop");
    	bpr = new BusPassengerRole();
    	bpr1 = new BusPassengerRole();
    	bpr2 = new BusPassengerRole();
    	bpr.setCurrentBusStop(bs);
    	bpr1.setCurrentBusStop(bs);
    	bpr2.setCurrentBusStop(bs);
    	bs.msgWaitingHere(bpr);
    	bs.msgWaitingHere(bpr1);
    	bs.msgWaitingHere(bpr2);
    	Timer timer = new Timer();
    	timer.schedule(new TimerTask() {
    		  @Override
    		  public void run() {
    		    // Your database code here
    			  Test();
    		  }
    		}, 6000);
    	
    	
    }
   
	
	
	protected void Test() {
		// TODO Auto-generated method stub
		bs.msgBusArrived(busAgent);
	}



	public BusStopPanel getAnimationPanel() {
		return animationPanel;
	}
	
	 public void updatePosition() {
	    	animationPanel.updatePosition();
	}
	 public static void main(String[] args) {
			BusStopGui gs = new BusStopGui();
			
		}



	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	    
}
