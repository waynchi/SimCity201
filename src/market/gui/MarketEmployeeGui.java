package market.gui;

import java.awt.Graphics2D;

import market.interfaces.MarketCustomer;

public class MarketEmployeeGui implements Gui{

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
	
	public void doGetItem(String item, int amount){}

	public void doWalkToCustomer(MarketCustomer customer) {
		//get gui object from customer and walk to the position
	}

}
