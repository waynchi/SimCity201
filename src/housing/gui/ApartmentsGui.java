package housing.gui;

import housing.Apartments;

import java.awt.Color;
import java.awt.Graphics2D;

public class ApartmentsGui implements HGui{
	public Apartments apartments;

	public ApartmentsGui(Apartments a) {
		this.apartments = a;
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
	}

	@Override
	public void draw(Graphics2D g) {
		int x = 15;
		int y = 15;
		g.setColor(Color.BLACK);
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				g.fillRect(x, y, 30, 30);
				y += 50;
			}
			y = 15;
			x += 50;
		}
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}
}