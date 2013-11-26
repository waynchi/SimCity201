package housing.gui;

import housing.RepairShop;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class RepairShopGui implements HGui {
	RepairShop s;
	List<ItemGui> itemGuis = new ArrayList<ItemGui>();
	public Dimension entranceCoordinatesInternal = new Dimension(455, 345);
	public Dimension entranceCoordinatesExternal = new Dimension();
	public RepairManGui rg;
	
	public RepairShopGui(RepairShop s) {
		this.s = s;
	}

	@Override
	public void updatePosition() {
	}

	@Override
	public void draw(Graphics2D g) {
		for (ItemGui gui : itemGuis) {
			gui.draw(g);
		}
		if (rg.isPresentInShop())
			rg.draw(g);
	}

	@Override
	public boolean isPresent() {
		return true;
	}
	
	public void add(HGui g) {
		if (g instanceof RepairManGui)
			rg = (RepairManGui) g;
		else if (g instanceof ItemGui)
			itemGuis.add((ItemGui) g);
	}
	
	public Dimension getPosition(String s) {
		Dimension d = null;
		for (ItemGui g : itemGuis) {
			if (g.i.name.equals(s)) {
				d = new Dimension(g.x, g.y);
				return d;
			}
		}
		return null;
	}
	
	public void setExternalCoordinates(Dimension d) {
		entranceCoordinatesExternal.width = d.width;
		entranceCoordinatesExternal.height = d.height;
	}
}