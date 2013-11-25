package housing.gui;

import housing.RepairShop;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class RepairShopGui implements HGui {
	RepairShop s;
	List<HGui> guis = new ArrayList<HGui>();
	
	public RepairShopGui(RepairShop s) {
		this.s = s;
	}

	@Override
	public void updatePosition() {
	}

	@Override
	public void draw(Graphics2D g) {
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
	
	public void add(HGui g) {
		guis.add(g);
	}
}