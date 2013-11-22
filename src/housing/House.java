package housing;

import housing.gui.HouseGui;
import housing.gui.ItemGui;
import housing.gui.TestGui;
import housing.interfaces.Resident;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import java.util.ArrayList;

public class House extends Item{
	public List<Item> items;
	public int number;
	public Resident occupant;
	public HouseGui gui;
	public boolean isLocked = false;
	public boolean isBroken = false;

	public House(String name, int houseNum) {
		super(name, null);
		number = houseNum;
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
	
	public void setGui(HouseGui gui) {
		this.gui = gui;
	}
	
	public void breakIt() {
		isBroken = true;
		this.occupant.somethingBroke();
	}
	
	public void repair() {
		isBroken = false;
	}
	
	public boolean isBroken() {
		return isBroken;
	}
	
	public Item findItem(String name) {
		for (Item i : items) {
			if (i.name.equals(name))
				return i;
		}
		return null;
	}
	
	public List<Item> getBrokenItems() {
		List<Item> result = new ArrayList<Item>();
		for (Item i : items) {
			if (i.isBroken())
				result.add(i);
		}
		return result;
	}
	
	public void setItems(TestGui testGui) {
		Item i1 = new Item("Bed", this);
		ItemGui g1 = new ItemGui(i1, 15, 15, 40, 90, Color.blue, testGui);
		i1.setGui(g1);
		items.add(i1);
		
		Item i2 = new Item("Table", this);
		ItemGui g2 = new ItemGui(i2, 70, 15, 40, 40, Color.red, testGui);
		i2.setGui(g2);
		items.add(i2);
		
		Item i3 = new Item("Fridge", this);
		ItemGui g3 = new ItemGui(i3, 15, 425, 30, 60, Color.gray, testGui);
		i3.setGui(g3);
		items.add(i3);
		
		Item i4 = new Item("TV", this);
		ItemGui g4 = new ItemGui(i4, 15, 270, 20, 30, Color.black, testGui);
		i4.setGui(g4);
		items.add(i4);
		
		Item i5 = new Item("CookingSlab1", this);
		ItemGui g5 = new ItemGui(i5, 100, 435, 70, 50, Color.CYAN, testGui);
		i5.setGui(g5);
		items.add(i5);
		
		Item i6 = new Item("CookingGrill", this);
		ItemGui g6 = new ItemGui(i6, 170, 435, 40, 50, Color.MAGENTA, testGui);
		i6.setGui(g6);
		items.add(i6);
		
		Item i7 = new Item("CookingSlab2", this);
		ItemGui g7 = new ItemGui(i7, 210, 435, 70, 50, Color.CYAN, testGui);
		i7.setGui(g7);
		items.add(i7);
		
		Item i8 = new Item("Basin", this);
		ItemGui g8 = new ItemGui(i8, 280, 435, 40, 50, Color.DARK_GRAY, testGui);
		i8.setGui(g8);
		items.add(i8);
		
		Item i9 = new Item("Shelves", this);
		ItemGui g9 = new ItemGui(i9, 320, 435, 130, 50, Color.pink, testGui);
		i9.setGui(g9);
		items.add(i9);
		
		Item i10 = new Item("FussballTable", this);
		ItemGui g10 = new ItemGui(i10, 380, 220, 50, 30, Color.GREEN, testGui);
		i10.setGui(g10);
		items.add(i10);
		
		Item i11 = new Item("Sofa1", this);
		ItemGui g11 = new ItemGui(i11, 150, 250, 30, 80, Color.LIGHT_GRAY, testGui);
		i11.setGui(g11);
		items.add(i11);
		
		Item i12 = new Item("Sofa2", this);
		ItemGui g12 = new ItemGui(i12, 100, 210, 40, 30, Color.LIGHT_GRAY, testGui);
		i12.setGui(g12);
		items.add(i12);
		
		Item i13 = new Item("Sofa3", this);
		ItemGui g13 = new ItemGui(i13, 100, 340, 40, 30, Color.LIGHT_GRAY, testGui);
		i13.setGui(g13);
		items.add(i13);
		
		Item i14 = new Item("CenterTable", this);
		ItemGui g14 = new ItemGui(i14, 90, 260, 40, 60, Color.BLUE, testGui);
		i14.setGui(g14);
		items.add(i14);
		
		Item i15 = new Item("DiningTable", this);
		ItemGui g15 = new ItemGui(i15, 310, 260, 50, 50, Color.RED, testGui);
		i15.setGui(g15);
		items.add(i15);
		
		Item i16 = new Item("Chair1", this);
		ItemGui g16 = new ItemGui(i16, 325, 230, 20, 20, Color.RED, testGui);
		i16.setGui(g16);
		items.add(i16);
		
		Item i17 = new Item("Chair2", this);
		ItemGui g17 = new ItemGui(i17, 325, 320, 20, 20, Color.RED, testGui);
		i17.setGui(g17);
		items.add(i17);
		
		Item i18 = new Item("Chair3", this);
		ItemGui g18 = new ItemGui(i18, 370, 275, 20, 20, Color.RED, testGui);
		i18.setGui(g18);
		items.add(i18);
		
		Item i19 = new Item("Chair4", this);
		ItemGui g19 = new ItemGui(i19, 280, 275, 20, 20, Color.RED, testGui);
		i19.setGui(g19);
		items.add(i19);
	}
	
	public void setItemsWithoutGui() {
		Item i1 = new Item("Bed", this);
		items.add(i1);
		
		Item i2 = new Item("Table", this);
		items.add(i2);
		
		Item i3 = new Item("Fridge", this);
		items.add(i3);
		
		Item i4 = new Item("TV", this);
		items.add(i4);
		
		Item i5 = new Item("CookingSlab1", this);
		items.add(i5);
		
		Item i6 = new Item("CookingGrill", this);
		items.add(i6);
		
		Item i7 = new Item("CookingSlab2", this);
		items.add(i7);
		
		Item i8 = new Item("Basin", this);
		items.add(i8);
		
		Item i9 = new Item("Shelves", this);
		items.add(i9);
		
		Item i10 = new Item("FussballTable", this);
		items.add(i10);
		
		Item i11 = new Item("Sofa1", this);
		items.add(i11);
		
		Item i12 = new Item("Sofa2", this);
		items.add(i12);
		
		Item i13 = new Item("Sofa3", this);
		items.add(i13);
		
		Item i14 = new Item("CenterTable", this);
		items.add(i14);
		
		Item i15 = new Item("DiningTable", this);
		items.add(i15);
		
		Item i16 = new Item("Chair1", this);
		items.add(i16);
		
		Item i17 = new Item("Chair2", this);
		items.add(i17);
		
		Item i18 = new Item("Chair3", this);
		items.add(i18);
		
		Item i19 = new Item("Chair4", this);
		items.add(i19);
	}
}

