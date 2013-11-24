package housing.gui;

import housing.Apartments;

import java.awt.Color;
import java.awt.Graphics2D;

public class ApartmentsGui implements HGui{
	public Apartments a;
	int houseWidth = 76;
	int houseHeight = 76;

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
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				g.fillRect(x, y, houseWidth, houseHeight);
				y += (houseHeight + 30);
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