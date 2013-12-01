package housing.gui;

import housing.Apartments;
import housing.House;
import housing.HouseType;

import javax.swing.JPanel;

public class HousingPanel extends JPanel {
	public HouseAnimationPanel hp = new HouseAnimationPanel();
	public ApartmentsAnimationPanel ap = new ApartmentsAnimationPanel();
	TestGui testGui;
	
	public HousingPanel(TestGui g, HouseAnimationPanel hp, ApartmentsAnimationPanel ap) {
		testGui = g;
		this.hp = hp;
		this.ap = ap;
		
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
		
		Apartments a = new Apartments("Apartments");
		ApartmentsGui gui3 = new ApartmentsGui(a);
		ap.addGui(gui3);
	}
}