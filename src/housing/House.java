package housing;

import housing.gui.HouseGui;
import housing.gui.ItemGui;
import housing.interfaces.Resident;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

public class House extends Item{
	public List<Item> items;
	public int number;
	public Resident occupant = null;
	public HouseGui gui;
	public boolean isBroken = false;
	public HouseType type;
	public int CHAIRS_NUMBER;
	public int COOKING_SLABS_NUMBER;
	public int SOFAS_NUMBER;
	public int FUSSBALL_TABLE;
	public int SHOE_RACK;
	public Apartments a = null;
	public boolean testMode = false;

	public House(String name, int houseNum, HouseType type) {
		super(name, null);
		number = houseNum;
		items = new ArrayList<Item>();
		occupant = null;
		this.type = type;
		
		if (type == HouseType.Apartment) {
			CHAIRS_NUMBER = 2;
			COOKING_SLABS_NUMBER = 1;
			SOFAS_NUMBER = 1;
			FUSSBALL_TABLE = 0;
			SHOE_RACK = 0;
		}
		else if (type == HouseType.Apartment) {
			CHAIRS_NUMBER = 4;
			COOKING_SLABS_NUMBER = 2;
			SOFAS_NUMBER = 3;
			FUSSBALL_TABLE = 1;
			SHOE_RACK = 1;
		}
	}

	public void setOccupant(Resident occ) {
		this.occupant = occ;
		if (testMode == false) {
			gui.add(((HousingResidentRole)occ).gui);
			((HousingResidentRole)occ).gui.hGui = this.gui;
			if (this.type == HouseType.Apartment) {
				this.a.gui.add(((HousingResidentRole)occ).gui);
				((HousingResidentRole)occ).gui.setApartmentsGui(this.a.gui);
			}
		}
	}
	
	public void removeOccupant() {
		this.occupant = null;
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
	
	public void testModeOn() {
		testMode = true;
	}
	
	public void testModeOff() {
		testMode = false;
	}
	
	public void setApartments(Apartments a) {
		if (this.type == HouseType.Apartment)
			this.a = a;
	}
	
	public List<Item> getBrokenItems() {
		List<Item> result = new ArrayList<Item>();
		for (Item i : items) {
			if (i.isBroken())
				result.add(i);
		}
		if (this.isBroken())
			result.add(this);
		return result;
	}
	
	public boolean isOccupied() {
		if (occupant != null)
			return true;
		return false;
	}
	
	public void setItems() {
		Item i1 = new Item("Bed", this);
		ItemGui g1 = new ItemGui(i1, 15, 135, 120, 60, "res/housingItemImages/bed.png");
		i1.setGui(g1);
		items.add(i1);
		
		Item i2 = new Item("SideStool", this);
		ItemGui g2 = new ItemGui(i2, 15, 75, 40, 40, "res/housingItemImages/sidestool.png");
		i2.setGui(g2);
		items.add(i2);
		
		Item i3 = new Item("Fridge", this);
		ItemGui g3 = new ItemGui(i3, 15, 425, 30, 60, "res/housingItemImages/fridge.png");
		i3.setGui(g3);
		items.add(i3);
		
		Item i4 = new Item("TV", this);
		ItemGui g4 = new ItemGui(i4, 15, 270, 20, 30, "res/housingItemImages/tv.png");
		i4.setGui(g4);
		items.add(i4);
		
		Item i5 = new Item("CookingSlab1", this);
		ItemGui g5 = new ItemGui(i5, 210, 435, 70, 50, "res/housingItemImages/cookingSlab.png");
		i5.setGui(g5);
		items.add(i5);
				
		Item i6 = new Item("CookingGrill", this);
		ItemGui g6 = new ItemGui(i6, 170, 435, 40, 50, "res/housingItemImages/cookingGrill.png");
		i6.setGui(g6);
		items.add(i6);
		
		if (type == HouseType.Villa) {
			Item i7 = new Item("CookingSlab2", this);
			ItemGui g7 = new ItemGui(i7, 100, 435, 70, 50, "res/housingItemImages/cookingSlab.png");
			i7.setGui(g7);
			items.add(i7);
		}
		
		Item i8 = new Item("Basin", this);
		ItemGui g8 = new ItemGui(i8, 280, 435, 40, 50, "res/housingItemImages/basin.png");
		i8.setGui(g8);
		items.add(i8);
		
		Item i9 = new Item("Shelves", this);
		ItemGui g9 = new ItemGui(i9, 320, 435, 130, 50, Color.pink);
		i9.setGui(g9);
		items.add(i9);
		
		if (type == HouseType.Villa) {
			Item i10 = new Item("FussballTable", this);
			ItemGui g10 = new ItemGui(i10, 190, 100, 60, 40, Color.GREEN);
			i10.setGui(g10);
			items.add(i10);
		}
		
		Item i11 = new Item("Sofa1", this);
		ItemGui g11 = new ItemGui(i11, 150, 250, 40, 80, "res/housingItemImages/sofa1.png");
		i11.setGui(g11);
		items.add(i11);
		
		if (type == HouseType.Villa) {
			Item i12 = new Item("Sofa2", this);
			ItemGui g12 = new ItemGui(i12, 100, 210, 40, 30, "res/housingItemImages/sofa2.png");
			i12.setGui(g12);
			items.add(i12);
		
			Item i13 = new Item("Sofa3", this);
			ItemGui g13 = new ItemGui(i13, 100, 340, 40, 30, "res/housingItemImages/sofa3.png");
			i13.setGui(g13);
			items.add(i13);
		}
		
		Item i14 = new Item("CenterTable", this);
		ItemGui g14 = new ItemGui(i14, 90, 260, 40, 60, "res/housingItemImages/centerTable.jpg");
		i14.setGui(g14);
		items.add(i14);
		
		Item i15 = new Item("DiningTable", this);
		ItemGui g15 = new ItemGui(i15, 310, 260, 50, 50, "res/housingItemImages/diningTable.jpg");
		i15.setGui(g15);
		items.add(i15);
		
		Item i16 = new Item("Chair1", this);
		ItemGui g16 = new ItemGui(i16, 370, 275, 20, 20, "res/housingItemImages/chair1.png");
		i16.setGui(g16);
		items.add(i16);
		
		Item i17 = new Item("Chair2", this);
		ItemGui g17 = new ItemGui(i17, 280, 275, 20, 20, "res/housingItemImages/chair2.png");
		i17.setGui(g17);
		items.add(i17);
				
		if (type == HouseType.Villa) {
			Item i18 = new Item("Chair3", this);
			ItemGui g18 = new ItemGui(i18, 325, 230, 20, 20, "res/housingItemImages/chair3.png");
			i18.setGui(g18);
			items.add(i18);
		
			Item i19 = new Item("Chair4", this);
			ItemGui g19 = new ItemGui(i19, 325, 320, 20, 20, "res/housingItemImages/chair4.png");
			i19.setGui(g19);
			items.add(i19);
		}
		
		Item i20 = new Item("BathTub", this);
		ItemGui g20 = new ItemGui(i20, 295, 15, 40, 100, "res/housingItemImages/bathtub.jpg");
		i20.setGui(g20);
		items.add(i20);
		
		Item i21 = new Item("Toilet", this);
		ItemGui g21 = new ItemGui(i21, 400, 165, 30, 30, "res/housingItemImages/toilet.png");
		i21.setGui(g21);
		items.add(i21);
		
		Item i22 = new Item("ToiletPaperDispenser", this);
		ItemGui g22 = new ItemGui(i22, 440, 175, 10, 20, Color.GRAY);
		i22.setGui(g22);
		items.add(i22);
		
		Item i23 = new Item("MirrorBasin", this);
		ItemGui g23 = new ItemGui(i23, 380, 15, 50, 30, 10, 10, Color.CYAN);
		i23.setGui(g23);
		items.add(i23);
		
		Item i24 = new Item("TowelHanger", this);
		ItemGui g24 = new ItemGui(i24, 345, 15, 25, 10, new Color(0, 150, 255));
		i24.setGui(g24);
		items.add(i24);
		
		Item i25 = new Item("BathroomShelves", this);
		ItemGui g25 = new ItemGui(i25, 420, 70, 30, 70, new Color(10, 100, 110));
		i25.setGui(g25);
		items.add(i25);
		
		Item i26 = new Item("Cupboard", this);
		ItemGui g26 = new ItemGui(i26, 160, 15, 120, 50, 10, 10, Color.darkGray);
		i26.setGui(g26);
		items.add(i26);
		
		Item i27 = new Item("StudyTable", this);
		ItemGui g27 = new ItemGui(i27, 70, 15, 60, 40, "res/housingItemImages/studyTable.jpg");
		i27.setGui(g27);
		items.add(i27);
		
		Item i28 = new Item("StudyChair", this);
		ItemGui g28 = new ItemGui(i28, 90, 65, 20, 20, "res/housingItemImages/chair.png");
		i28.setGui(g28);
		items.add(i28);
		
		Item i29 = new Item("Dustbin1", this);
		ItemGui g29 = new ItemGui(i29, 15, 15, 20, 20, "res/housingItemImages/dustbin1.png");
		i29.setGui(g29);
		items.add(i29);
		
		Item i30 = new Item("Dustbin2", this);
		ItemGui g30 = new ItemGui(i30, 430, 380, 20, 20, "res/housingItemImages/dustbin2.png");
		i30.setGui(g30);
		items.add(i30);
		
		if (type == HouseType.Villa) {
			Item i31 = new Item("ShoeRack", this);
			ItemGui g31 = new ItemGui(i31, 420, 210, 30, 60, new Color(150, 200, 0));
			i31.setGui(g31);
			items.add(i31);
		}
		
		gui.setItems();
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
		
		if (type == HouseType.Villa) {
			Item i5 = new Item("CookingSlab1", this);
			items.add(i5);
		}
		
		Item i6 = new Item("CookingGrill", this);
		items.add(i6);
		
		Item i7 = new Item("CookingSlab2", this);
		items.add(i7);
		
		Item i8 = new Item("Basin", this);
		items.add(i8);
		
		Item i9 = new Item("Shelves", this);
		items.add(i9);
		
		if (type == HouseType.Villa) {
			Item i10 = new Item("FussballTable", this);
			items.add(i10);
		}
		
		Item i11 = new Item("Sofa1", this);
		items.add(i11);
		
		if (type == HouseType.Villa) {
			Item i12 = new Item("Sofa2", this);
			items.add(i12);
		
			Item i13 = new Item("Sofa3", this);
			items.add(i13);
		}
		
		Item i14 = new Item("CenterTable", this);
		items.add(i14);
		
		Item i15 = new Item("DiningTable", this);
		items.add(i15);
		
		Item i16 = new Item("Chair1", this);
		items.add(i16);
		
		Item i17 = new Item("Chair2", this);
		items.add(i17);
		
		if (type == HouseType.Villa) {
			Item i18 = new Item("Chair3", this);
			items.add(i18);
		
			Item i19 = new Item("Chair4", this);
			items.add(i19);
		}
		
		Item i20 = new Item("BathTub", this);
		items.add(i20);
		
		Item i21 = new Item("Toilet", this);
		items.add(i21);
		
		Item i22 = new Item("ToiletPaperDispenser", this);
		items.add(i22);
		
		Item i23 = new Item("MirrorBasin", this);
		items.add(i23);
		
		Item i24 = new Item("TowelHanger", this);
		items.add(i24);
		
		Item i25 = new Item("BathroomShelves", this);
		items.add(i25);
		
		Item i26 = new Item("Cupboard", this);
		items.add(i26);
		
		Item i27 = new Item("StudyTable", this);
		items.add(i27);
		
		Item i28 = new Item("StudyChair", this);
		items.add(i28);
		
		Item i29 = new Item("Dustbin1", this);
		items.add(i29);
		
		Item i30 = new Item("Dustbin2", this);
		items.add(i30);
		
		if (type == HouseType.Villa) {
			Item i31 = new Item("ShoeRack", this);
			items.add(i31);
		}
	}
}