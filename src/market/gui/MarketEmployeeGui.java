package market.gui;

import java.awt.Graphics2D;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import market.gui.MarketEmployeeGui;
import market.interfaces.MarketCustomer;

public class MarketEmployeeGui implements Gui{
	private Map<String,Dimension> itemMap = new HashMap<String, Dimension>();
	
	MarketEmployeeGui(){
		itemMap.put("Steak", new Dimension(100,100));
		itemMap.put("Chicken", new Dimension(140,100));
		itemMap.put("Pizza", new Dimension(180,100));
		itemMap.put("Salad", new Dimension(220,100));
		itemMap.put("Car", new Dimension(260,100));
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
	
	public void doGetItem(String item){
		//
	}

	public void doWalkToCustomer(MarketCustomer customer) {
		//get gui object from customer and walk to the position
	}

}
