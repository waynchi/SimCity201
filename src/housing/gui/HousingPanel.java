package housing.gui;

import housing.Apartments;
import housing.House;

import javax.swing.JPanel;

public class HousingPanel extends JPanel {
	public HouseAnimationPanel p = new HouseAnimationPanel();
	TestGui testGui;
	
	public HousingPanel(TestGui g, HouseAnimationPanel p) {
		testGui = g;
		this.p = p;
		House h = new House("Residence", 44);
		h.setItems(testGui);
		HouseGui gui1 = new HouseGui(h, testGui);
		h.setGui(gui1);
		p.addGui(gui1);
		Apartments a = new Apartments("Death Star");
		ApartmentsGui gui2 = new ApartmentsGui(a);
		p.addGui(gui2);
	}
}
