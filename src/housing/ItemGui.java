package housing;

import java.awt.Color;
import java.awt.Graphics2D;

public class ItemGui implements HGui{
	public Item i;
	int x, y, width, height;
	Color c;
	TestGui testGui;

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
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}
}

