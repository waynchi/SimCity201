package housing.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class RepairShopAnimationPanel extends JPanel implements ActionListener {
	
	public List<HGui> humanGuis = new ArrayList<HGui>();
	public List<HGui> guis = new ArrayList<HGui>();
	
	public RepairShopAnimationPanel() {
		super();
		this.setSize(500, 570);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}
	
	public void paintComponent(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		for (HGui gui : humanGuis) {
			if (!gui.isPresent())
				gui.draw(g);
		}
		for (HGui gui : guis) {
			if (gui.isPresent())
				gui.draw(g);
		}
	}
	
	public void addGui(HGui gui) {
		if (!(gui instanceof RepairShopGui))
			humanGuis.add(gui);
		else {
			guis.add(gui);
		}
	}

}
