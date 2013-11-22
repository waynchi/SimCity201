package housing.gui;

import housing.Item;

import java.awt.Color;
import java.awt.Graphics2D;

public class ItemGui implements HGui{
	public Item i;
	int x, y, width, height;
	Color c;
	TestGui testGui;
	boolean isBroken = false;

	public ItemGui(Item i, int x, int y, int width, int height, Color c, TestGui g) {
		this.i = i;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.c = c;
		testGui = g;
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(c);
		g.fillRect(x, y, width, height);
		if (isBroken()) {
			g.setColor(Color.WHITE);
			g.drawLine(x, ((height / 2) + y), x + width, ((height / 2) + y));
		}
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public void breakIt() {
		isBroken = true;
	}
	
	public void repair() {
		isBroken = false;
	}
	
	public boolean isBroken() {
		return isBroken;
	}
}

