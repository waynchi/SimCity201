package housing;

import housing.gui.ItemGui;

import java.awt.Dimension;

public class Item {
	public String name;
	public Dimension position;
	public boolean isBroken = false;
	public ItemGui gui = null;
	public House h;

	public Item(String name, House h) {
		this.name = name;
		this.h = h;
		isBroken = false;
	}

	public void breakIt() {
		h.occupant.somethingBroke();
		isBroken = true;
		gui.breakIt();
	}

	public void repair() {
		isBroken = false;
		gui.repair();
	}

	public boolean isBroken() {
		return isBroken;
	}
	
	public void setGui(ItemGui gui) {
		this.gui = gui;
	}
	
	public ItemGui getGui() {
		return gui;
	}
	
	public void testBreak() {
		isBroken = true;
		gui.breakIt();
	}
}

