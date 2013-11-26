package market.gui;

import java.awt.Graphics2D;

import people.People;
import market.MarketTruckAgent;

public class MarketTruckGui implements Gui{

	private MarketTruckAgent agent;
	public MarketTruckGui(MarketTruckAgent marketTruckAgent) {
		agent = marketTruckAgent;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public void deliver(People person) {
		// deliver items to customer at specific location
		//GoToPosition (person.getPosition);
		new java.util.Timer().schedule(
				new java.util.TimerTask(){
					public void run(){
						agent.msgOrderDelivered();;
					}
				},
				2000);
	}

}
