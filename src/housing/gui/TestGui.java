package housing.gui;

import java.awt.CardLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class TestGui extends JFrame {
	public HouseAnimationPanel hp = new HouseAnimationPanel();
	HousingPanel h = new HousingPanel(this, hp);
	CardLayout cl = new CardLayout();
	JPanel bhp = new JPanel();
	
	public TestGui() {
		this.setLayout(new GridLayout());
		bhp.setLayout(cl);
//		bhp.add(hp);
		this.add(h.a.gui.ap);
		h.a.gui.ap.setTestGui(this);
		this.add(bhp);
		this.setSize(1100, 570);
		for (int i = 0; i < 25; i++) {
			JScrollPane houseContainer = new JScrollPane(h.a.houses.get(i).gui.hp);
			houseContainer.setOpaque(true);
			bhp.add(houseContainer, i + "");
		}
	}
	
	public void displayApartment(int num) {
		cl.show(bhp, num + "");
	}
	
	public static void main(String[] arg) {
		TestGui gui = new TestGui();
		gui.setTitle("Test");
		gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}