package restaurant_vk.gui;

import java.awt.Color;
import java.awt.Graphics2D;

public class CashierGui implements Gui{

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.RED);
		g.fillRect(620, 150, 20, 20);
		g.setColor(Color.BLACK);
		g.drawString("CA", 622, 165);
	}

	@Override
	public boolean isPresent() {
		return true;
	}

}
