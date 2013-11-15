package housing;

import java.awt.Dimension;

public class Item {
	public String name;
	public Dimension position;
	public boolean isBroken;
	public ItemGui gui;
	public House h;
	public TestGui testGui;

	public Item(String name, Dimension pos, House h, TestGui g) {
		this.name = name;
		this.position = pos;
		this.h = h;
		isBroken = false;
		testGui = g;
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
	
	public void setGui(ItemGui gui) {
		this.gui = gui;
		testGui.addGui(gui);
	}
	
	public ItemGui getGui() {
		return gui;
	}
}

