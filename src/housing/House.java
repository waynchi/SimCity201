package housing;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import java.util.ArrayList;

public class House {
	public List<Item> items;
	public int number;
	public Resident occupant;
	public HouseGui gui;
	public boolean isLocked;
	public TestGui testGui;

	public House(int houseNum, TestGui g) {
		number = houseNum;
		items = new ArrayList<Item>();
		occupant = null;
		isLocked = false;
		testGui = g;
		setItems();
	}

	public boolean isLocked() {
		return isLocked;
	}

	public void lock() {
		isLocked = true;
	}

	public void unlock() {
		isLocked = false;
	}

	public void setOccupant(Resident occ) {
		this.occupant = occ;
	}

	public void addItem(Item i) {
		items.add(i);
	}

	public List<Item> getItems() {
		return items;
	}
	
	public void setGui(HouseGui gui) {
		this.gui = gui;
	}
	
	private void setItems() {
		Item i1 = new Item("Bed", new Dimension(15, 15), this, testGui);
		ItemGui g1 = new ItemGui(i1, 15, 15, 40, 90, Color.blue);
		i1.setGui(g1);
		items.add(i1);
		
		Item i2 = new Item("Table", new Dimension(70, 15), this, testGui);
		ItemGui g2 = new ItemGui(i2, 70, 15, 40, 40, Color.red);
		i2.setGui(g2);
		items.add(i2);
	}
}

