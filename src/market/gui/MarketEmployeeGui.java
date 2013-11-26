package market.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import market.gui.MarketEmployeeGui;
import market.interfaces.MarketEmployee;

public class MarketEmployeeGui implements Gui{
	private Map<String,Dimension> itemMap = new HashMap<String, Dimension>();
	MarketEmployee employee;
	boolean goToCounter = false;
	boolean isPresent = false;
	boolean leaving = false;
	
	public MarketEmployeeGui(MarketEmployee me){
		this.employee = me;
		itemMap.put("Steak", new Dimension(150,50));
		itemMap.put("Chicken", new Dimension(150,100));
		itemMap.put("Pizza", new Dimension(150,150));
		itemMap.put("Salad", new Dimension(150,200));
		itemMap.put("Car", new Dimension(150,250));
	}
	//170,150,30,30
	int xPos= 0, xDestination = 170;
	int yPos= 0, yDestination = 150;
	
	int xCounter = 170;
	int yCounter = 150;
	
	int xExit = 0;
	int yExit = 0;
	
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
		if (xPos == xDestination && yPos == yDestination && goToCounter) {
			employee.msgAtCounter();
			goToCounter = false;
		}
		if (xPos == xExit && yPos == yExit && leaving) {
			employee.msgAtExit();
			leaving = false;
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
		return isPresent;
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
		goToCounter = true;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void doExit() {
		// TODO Auto-generated method stub
		xDestination = xExit;
		yDestination = yExit;
		leaving = true;
		
	}
}
