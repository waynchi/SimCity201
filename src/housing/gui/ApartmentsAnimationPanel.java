package housing.gui;

import housing.House;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

import city.gui.BuildingPanel;
import city.gui.CityGui;

public class ApartmentsAnimationPanel extends JPanel implements ActionListener {
	List<HGui> humanGuis = new ArrayList<HGui>();
	List<HGui> nonLivingGuis = new ArrayList<HGui>();
	CityGui g;
	public ApartmentsGui ag;
	public final String apartmentsPrefix;
	
	public ApartmentsAnimationPanel(ApartmentsGui ag) {
		super();
		this.ag = ag;
		apartmentsPrefix = ag.a.name;
		this.setSize(500, 570);
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				int xPos = e.getX();
				int yPos = e.getY();
				int houseWidth = 76;
				int houseHeight = 76;
				int x = 0;
				int y = 0;
				int k = 0;
				for (int i = 0; i < 5; i++) {
					for (int j = 0; j < 5; j++) {
						if (xPos >= x && xPos <= (x + houseWidth) && yPos >= y && yPos <= (y + houseHeight)) {
							g.displayBuildingPanel(apartmentsPrefix + k);
							return;
						}
						y += (houseHeight + 30);
						k++;
					}
					y = 0;
					x += (houseHeight + 30);
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}
	
	public void paintComponent(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		for (HGui gui : humanGuis) {
			if (gui instanceof ResidentGui && ((ResidentGui)gui).isPresentInComplex())
				gui.draw(g);
		}
		for (HGui gui : nonLivingGuis) {
			if (gui.isPresent())
				gui.draw(g);
		}
		ag.draw(g);
	}
	
	public void addGui(HGui gui) {
		if (!(gui instanceof ApartmentsGui))
			humanGuis.add(gui);
		else {
			nonLivingGuis.add(gui);
		}
	}
	
	public void setCityGui(CityGui g) {
		this.g = g;
		List<House> houses = ag.a.houses;
		for (House h : houses) {
			h.gui.hp.setCityGui(g);
		}
	}
	
	public void updatePosition() {
		for (HGui gui : humanGuis) {
			gui.updatePosition();
		}
		for (HGui gui : nonLivingGuis) {
			gui.updatePosition();
		}
		ag.updatePosition();
	}
}