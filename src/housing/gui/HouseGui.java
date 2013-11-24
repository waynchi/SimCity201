package housing.gui;

import housing.House;
import housing.HouseType;
import housing.Item;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class HouseGui implements HGui{
	public House h;
	List<ItemGui> items = new ArrayList<ItemGui>();
	List<HGui> guis = new ArrayList<HGui>();
	public Dimension entranceCoordinates = new Dimension(455, 345);

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
		g.fillRect(450, 0, 15, 330);
		g.fillRect(450, 370, 15, 130);
		
		g.fillRect(280, 15, 15, 145);
		g.fillRect(280, 195, 170, 15);
		
		if (h.type == HouseType.Villa)
			g.fillRect(15, 195, 220, 15);
		
		for (ItemGui gui : items) {
			gui.draw(g);
		}
		for (HGui gui : guis) {
			if (gui.isPresent()) {
				gui.draw(g);
			}
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
	
	public Dimension getPosition(String s) {
		Dimension d = null;
		for (ItemGui g : items) {
			if (g.i.name.equals(s)) {
				d = new Dimension(g.x, g.y);
				return d;
			}
		}
		return null;
	}
	
	public void add(HGui g) {
		guis.add(g);
	}
}