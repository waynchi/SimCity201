package housing.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class HouseAnimationPanel extends JPanel implements ActionListener {
	List<HGui> guis = new ArrayList<HGui>();
	
	public HouseAnimationPanel() {
		super();
		this.setSize(500, 570);
		this.setPreferredSize(new Dimension(500,570));
		this.setMaximumSize(new Dimension(500,570));
		this.setMinimumSize(new Dimension(500,570));
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}
	
	public void paintComponent(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		for (HGui gui : guis) {
			gui.updatePosition();
		}
		for (HGui gui : guis) {
			if (gui.isPresent())
				gui.draw(g);
		}
	}
	
	public void addGui(HGui gui) {
		guis.add(gui);
	}
	
	public void updatePosition() {
		for (HGui gui : guis) {
			gui.updatePosition();
		}
//		repaint();
	}
}