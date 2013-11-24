package housing.gui;

import java.awt.*;

public interface HGui {
	public void updatePosition();
    public void draw(Graphics2D g);
    public boolean isPresent();
}