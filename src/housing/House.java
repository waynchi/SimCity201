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

	public House(int houseNum, TestGui testGui) {
		number = houseNum;
		items = new ArrayList<Item>();
		occupant = null;
		isLocked = false;
		setItems(testGui);
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
	
	private void setItems(TestGui testGui) {
		Item i1 = new Item("Bed", new Dimension(15, 15), this);
		ItemGui g1 = new ItemGui(i1, 15, 15, 40, 90, Color.blue, testGui);
		i1.setGui(g1);
		items.add(i1);
		
		Item i2 = new Item("Table", new Dimension(70, 15), this);
		ItemGui g2 = new ItemGui(i2, 70, 15, 40, 40, Color.red, testGui);
		i2.setGui(g2);
		items.add(i2);
		
		Item i3 = new Item("Fridge", new Dimension(15, 425), this);
		ItemGui g3 = new ItemGui(i3, 15, 425, 30, 60, Color.gray, testGui);
		i3.setGui(g3);
		items.add(i3);
		
		Item i4 = new Item("TV", new Dimension(15, 270), this);
		ItemGui g4 = new ItemGui(i4, 15, 270, 20, 30, Color.black, testGui);
		i4.setGui(g4);
		items.add(i4);
		
		Item i5 = new Item("CookingSlab1", new Dimension(100, 435), this);
		ItemGui g5 = new ItemGui(i5, 100, 435, 70, 50, Color.CYAN, testGui);
		i5.setGui(g5);
		items.add(i5);
		
		Item i6 = new Item("CookingGrill", new Dimension(170, 435), this);
		ItemGui g6 = new ItemGui(i6, 170, 435, 40, 50, Color.MAGENTA, testGui);
		i6.setGui(g6);
		items.add(i6);
		
		Item i7 = new Item("CookingSlab2", new Dimension(210, 435), this);
		ItemGui g7 = new ItemGui(i7, 210, 435, 70, 50, Color.CYAN, testGui);
		i7.setGui(g7);
		items.add(i7);
		
		Item i8 = new Item("Basin", new Dimension(280, 435), this);
		ItemGui g8 = new ItemGui(i8, 280, 435, 40, 50, Color.DARK_GRAY, testGui);
		i8.setGui(g8);
		items.add(i8);
		
		Item i9 = new Item("Shelves", new Dimension(320, 435), this);
		ItemGui g9 = new ItemGui(i9, 320, 435, 130, 50, Color.pink, testGui);
		i9.setGui(g9);
		items.add(i9);
		
		Item i10 = new Item("FussballTable", new Dimension(380, 220), this);
		ItemGui g10 = new ItemGui(i10, 380, 220, 50, 30, Color.GREEN, testGui);
		i10.setGui(g10);
		items.add(i10);
		
		Item i11 = new Item("Sofa1", new Dimension(150, 250), this);
		ItemGui g11 = new ItemGui(i11, 150, 250, 30, 80, Color.LIGHT_GRAY, testGui);
		i11.setGui(g11);
		items.add(i11);
		
		Item i12 = new Item("Sofa2", new Dimension(100, 210), this);
		ItemGui g12 = new ItemGui(i12, 100, 210, 40, 30, Color.LIGHT_GRAY, testGui);
		i12.setGui(g12);
		items.add(i12);
		
		Item i13 = new Item("Sofa3", new Dimension(100, 340), this);
		ItemGui g13 = new ItemGui(i13, 100, 340, 40, 30, Color.LIGHT_GRAY, testGui);
		i13.setGui(g13);
		items.add(i13);
		
		Item i14 = new Item("CenterTable", new Dimension(90, 260), this);
		ItemGui g14 = new ItemGui(i14, 90, 260, 40, 60, Color.BLUE, testGui);
		i14.setGui(g14);
		items.add(i14);
		
		Item i15 = new Item("DiningTable", new Dimension(310, 260), this);
		ItemGui g15 = new ItemGui(i15, 310, 260, 50, 50, Color.RED, testGui);
		i15.setGui(g15);
		items.add(i15);
		
		Item i16 = new Item("Chair1", new Dimension(325, 230), this);
		ItemGui g16 = new ItemGui(i16, 325, 230, 20, 20, Color.RED, testGui);
		i16.setGui(g16);
		items.add(i16);
		
		Item i17 = new Item("Chair2", new Dimension(325, 320), this);
		ItemGui g17 = new ItemGui(i17, 325, 320, 20, 20, Color.RED, testGui);
		i17.setGui(g17);
		items.add(i17);
		
		Item i18 = new Item("Chair3", new Dimension(370, 275), this);
		ItemGui g18 = new ItemGui(i18, 370, 275, 20, 20, Color.RED, testGui);
		i18.setGui(g18);
		items.add(i18);
		
		Item i19 = new Item("Chair4", new Dimension(280, 275), this);
		ItemGui g19 = new ItemGui(i19, 280, 275, 20, 20, Color.RED, testGui);
		i19.setGui(g19);
		items.add(i19);
	}
}

