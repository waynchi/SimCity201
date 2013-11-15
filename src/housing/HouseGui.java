package housing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class HouseGui implements HGui{
	public House h;
	List<ItemGui> items = new ArrayList<ItemGui>();

	public HouseGui(House h) {
		this.h = h;
		setItems();
	}

	@Override
	public void updatePosition() {
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.ORANGE);
		g.fillRect(0, 0, 15, 500);
		g.fillRect(0, 0, 500, 15);
		g.fillRect(0, 485, 500, 15);
		g.fillRect(450, 0, 15, 300);
		g.fillRect(450, 370, 15, 130);
		for (ItemGui gui : items) {
			gui.draw(g);
		}
	}

	@Override
	public boolean isPresent() {
		return true;
	}
	
	private void setItems() {
		List<Item> list = h.items;
		for (Item i : list) {
			items.add(i.getGui());
		}
	}
}
