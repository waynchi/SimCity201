package housing.gui;

import housing.Apartments;
import housing.House;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class ApartmentsGui implements HGui{
	public Apartments a;
	public int houseWidth = 76;
	public int houseHeight = 76;
	public Dimension entranceCoordinates = new Dimension(197, 520);
	public List<HGui> guis = new ArrayList<HGui>();
	public ApartmentsAnimationPanel ap;

	public ApartmentsGui(Apartments a) {
		this.a = a;
		ap = new ApartmentsAnimationPanel(this);
		setExternalCoordinatesOfHouses();
	}

	@Override
	public void updatePosition() {
		for (HGui g : guis) {
			g.updatePosition();
		}
	}

	@Override
	public void draw(Graphics2D g) {
		int x = 0;
		int y = 0;
		g.setColor(Color.BLUE);
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				g.fillRect(x, y, houseWidth, houseHeight);
				y += (houseHeight + 30);
			}
			y = 0;
			x += (houseHeight + 30);
		}
		for (HGui g1 : guis) {
			if (g1 instanceof ResidentGui && ((ResidentGui)g1).isPresentInComplex())
				g1.draw(g);
		}
	}

	@Override
	public boolean isPresent() {
		return true;
	}
	
	private void setExternalCoordinatesOfHouses() {
		int x = 0;
		int y = 0;
		int k = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				House h = a.houses.get(k);
				k++;
				if (i == 4) {
					h.gui.setExternalCoordinates(new Dimension(x, y + houseHeight));
				}
				else {
					h.gui.setExternalCoordinates(new Dimension(x + houseWidth, y + houseHeight));
				}
				y += (houseHeight + 30);
			}
			y = 0;
			x += (houseHeight + 30);
		}
	}
	
	public void add(HGui g) {
		guis.add(g);
	}
	
	public void remove(HGui g) {
		guis.remove(g);
	}
}