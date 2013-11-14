package housing;

import java.awt.Dimension;

public class Item {
	public String name;
	public Dimension position;
	public boolean isBroken;
	public ItemGui gui;
	public House h;

	public Item(String name, Dimension pos, ItemGui gui, House h) {
		this.name = name;
		this.position = pos;
		this.gui = gui;
		this.h = h;
		isBroken = false;
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

