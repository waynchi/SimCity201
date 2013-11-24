package housing.gui;


import javax.swing.JFrame;

public class TestGui extends JFrame {
	public HouseAnimationPanel p = new HouseAnimationPanel();
	HousingPanel hp = new HousingPanel(this, p);
	
	public TestGui() {
		this.add(p);
		this.setSize(500, 570);
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