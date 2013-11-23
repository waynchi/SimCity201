package market.gui;

import java.awt.Graphics2D;

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
	
	public void deliver() {
		// deliver items to customer at specific location
	}

}
