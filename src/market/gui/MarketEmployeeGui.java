package market.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import market.gui.MarketEmployeeGui;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketEmployee;

public class MarketEmployeeGui implements Gui{
	private Map<String,Dimension> itemMap = new HashMap<String, Dimension>();
	MarketGui gui;
	MarketEmployee employee;
	boolean goBackToCounter = false;
	
	MarketEmployeeGui(MarketEmployee me, MarketGui gui){
		this.gui = gui;
		this.employee = me;
		itemMap.put("Steak", new Dimension(150,50));
		itemMap.put("Chicken", new Dimension(150,100));
		itemMap.put("Pizza", new Dimension(150,150));
		itemMap.put("Salad", new Dimension(150,200));
		itemMap.put("Car", new Dimension(150,250));
	}
	//170,150,30,30
	int xPos, xDestination = 170;
	int yPos, yDestination = 150;
	
	int xCounter = 170;
	int yCounter = 150;
	
	boolean atCabinet = false;
	

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;
		if (xPos == xDestination && yPos == yDestination && atCabinet) {
			employee.msgAtCabinet();
			atCabinet = false;
		}
		if (xPos == xDestination && yPos == yDestination && goBackToCounter) {
			employee.msgAtCounter();
			goBackToCounter = false;
		}
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(Color.BLUE);
		g.fillRect(xPos, yPos, 30, 30);
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}
	
	//Dimension(width, height)
	public void doGetItem(String item){
		xDestination = (int) itemMap.get(item).getWidth();
		yDestination = (int) itemMap.get(item).getHeight();
		atCabinet = true;
	}

	public void doGoToCounter() {
		// TODO Auto-generated method stub
		xDestination = xCounter;
		yDestination = yCounter;
		goBackToCounter = true;
	}

}
