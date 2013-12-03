package housing.gui;

import housing.Apartments;
import housing.House;
import housing.HouseType;

import javax.swing.JPanel;

public class HousingPanel extends JPanel {
	public HouseAnimationPanel hp = new HouseAnimationPanel();
	public Apartments a = new Apartments("Apartments");
	TestGui testGui;
	
	public HousingPanel(TestGui g, HouseAnimationPanel hp) {
		testGui = g;
		this.hp = hp;
		
		House h1 = new House("Residence1", 1, HouseType.Apartment);
		h1.setItems();
		HouseGui gui1 = new HouseGui(h1);
		h1.setGui(gui1);
		hp.addGui(gui1);
		
//		House h2 = new House("Residence2", 2, HouseType.Apartment);
//		h2.setItems();
//		HouseGui gui2 = new HouseGui(h2);
//		h2.setGui(gui2);
//		p.addGui(gui2);
	}
}