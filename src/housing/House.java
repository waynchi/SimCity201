package housing;

import java.util.List;
import java.util.ArrayList;

public class House {
	public List<Item> items;
	public int number;
	public Resident occupant;
	public HouseGui gui;
	public boolean isLocked;

	public House(int houseNum, HouseGui gui) {
		number = houseNum;
		this.gui = gui;
		items = new ArrayList<Item>();
		occupant = null;
		isLocked = false;
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
}

