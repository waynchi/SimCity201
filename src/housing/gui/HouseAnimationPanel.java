package housing.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class HouseAnimationPanel extends JPanel implements ActionListener {
	List<HGui> guis = new ArrayList<HGui>();
	public HouseGui gui;
	public TestGui tg;
	
	public HouseAnimationPanel() {
		super();
		this.setSize(500, 570);
		this.setPreferredSize(new Dimension(500,570));
		this.setMaximumSize(new Dimension(500,570));
		this.setMinimumSize(new Dimension(500,570));
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				int xPos = e.getX();
				int yPos = e.getY();
				if (xPos >= 470 && xPos <= 500 && yPos >= 15 && yPos <= 75) {
					tg.display();
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
			
		});
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
		gui.draw(g);
	}
	
	public void addGui(HGui gui) {
		guis.add(gui);
	}
	
	public void addHouseGui(HouseGui g) {
		gui = g;
	}
	
	public void setTestGui(TestGui g) {
		tg = g;
	}
	
	public void updatePosition() {
		for (HGui gui : guis) {
			gui.updatePosition();
		}
	}
}