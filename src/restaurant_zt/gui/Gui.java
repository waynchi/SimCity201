package restaurant_zt.gui;

import java.awt.*;
import java.util.concurrent.Semaphore;

public interface Gui {
	
    public void updatePosition();
    public void draw(Graphics2D g);
    public boolean isPresent();

}
