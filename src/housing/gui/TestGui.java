package housing.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;

public class TestGui extends JFrame {
	public HouseAnimationPanel hp = new HouseAnimationPanel();
	public ApartmentsAnimationPanel ap = new ApartmentsAnimationPanel();
	HousingPanel h = new HousingPanel(this, hp, ap);
	CardLayout cl = new CardLayout();
	
	public TestGui() {
		this.setLayout(new GridLayout());
		this.add(ap);
		this.add(hp);
		this.setSize(1100, 570);
	}
	
//	public void addGui(HGui gui) {
//		p.addGui(gui);
//	}
	
	public static void main(String[] arg) {
		TestGui gui = new TestGui();
		gui.setTitle("Test");
		gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}