package housing;

import java.util.List;
import java.util.ArrayList;

public class Apartments {
	public List<House> houses;
	public ApartmentsGui gui;
	public String name;

	public Apartments(ApartmentsGui gui, String name) {
		houses = new ArrayList<House>();
		this.gui = gui;
		this.name = name;
	}
}

