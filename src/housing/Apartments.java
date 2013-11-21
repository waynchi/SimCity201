package housing;

import housing.gui.ApartmentsGui;

import java.util.List;
import java.util.ArrayList;

public class Apartments {
	public List<House> houses;
	public ApartmentsGui gui;
	public String name;

	public Apartments(String name) {
		houses = new ArrayList<House>();
		this.name = name;
	}
	
	public void setGui(ApartmentsGui gui) {
		this.gui = gui;
	}
}

