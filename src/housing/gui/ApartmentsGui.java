package housing.gui;

import housing.Apartments;
import housing.House;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

public class ApartmentsGui implements HGui{
	public Apartments a;
	int houseWidth = 76;
	int houseHeight = 76;
	public Dimension entranceCoordinates = new Dimension(250, 530);

	public ApartmentsGui(Apartments a) {
		this.a = a;
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
	}

	@Override
	public void draw(Graphics2D g) {
		int x = 0;
		int y = 0;
		g.setColor(Color.BLUE);
		int k = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				g.fillRect(x, y, houseWidth, houseHeight);
				House h = a.houses.get(k);
				h.gui.setExternalCoordinates(new Dimension(x + (houseWidth / 2), y + houseHeight));
				y += (houseHeight + 30);
				k++;
			}
			y = 0;
			x += (houseHeight + 30);
		}
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}
}