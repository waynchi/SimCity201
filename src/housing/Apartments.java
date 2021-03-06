package housing;

import housing.gui.ApartmentsGui;
import housing.gui.HouseGui;

import java.util.List;
import java.util.ArrayList;

public class Apartments {
	public List<House> houses;
	public ApartmentsGui gui;
	public String name;
	public int TOTAL_APARTMENTS = 25;

	public Apartments(String name) {
		houses = new ArrayList<House>();
		this.name = name;
		createApartments();
		gui = new ApartmentsGui(this);
	}
	
	public void setGui(ApartmentsGui gui) {
		this.gui = gui;
	}
	
	private void createApartments() {
		for (int i = 0; i < 25; i++) {
			int num = i + 1;
			House h = new House("Apartment" + num, num, HouseType.Apartment);
			HouseGui g = new HouseGui(h);
			h.setGui(g);
			h.setItems();
			houses.add(h);
			h.setApartments(this);
		}
	}
	
	public House getAvailableApartment() {
		for (House h : houses) {
			if (!h.isOccupied())
				return h;
		}
		return null;
	}
	
	public int availableApartments() {
		int result = 0;
		for (House h : houses) {
			if (h.isOccupied())
				result++;
		}
		return result;
	}
}