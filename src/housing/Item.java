package housing;

import java.awt.Dimension;

public class Item {
	public String name;
	public Dimension position;
	public boolean isBroken;
	public ItemGui gui = null;
	public House h;

	public Item(String name, Dimension pos, House h) {
		this.name = name;
		this.position = pos;
		this.h = h;
		isBroken = false;
	}

	public void breakIt() {
		h.occupant.somethingBroke();
		isBroken = true;
	}

	public void repair() {
		isBroken = false;
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
}

