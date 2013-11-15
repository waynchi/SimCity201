package housing;

import javax.swing.JFrame;

public class TestGui extends JFrame {
	public HouseAnimationPanel p = new HouseAnimationPanel();
	
	public TestGui() {
		this.add(p);
		House h = new House(44, this);
		HouseGui gui1 = new HouseGui(h);
		p.addGui(gui1);
		Apartments a = new Apartments("Death Star");
		ApartmentsGui gui2 = new ApartmentsGui(a);
		p.addGui(gui2);
		this.setSize(500, 530);
	}
	
	public void addGui(HGui gui) {
		p.addGui(gui);
	}
	
	public static void main(String[] arg) {
		TestGui gui = new TestGui();
		gui.setTitle("Test");
		gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
