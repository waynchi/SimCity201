package restaurant_vk.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class RestaurantVkAnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 680;
    private final int WINDOWY = 480;
    private final int TABLE_LEFT_X = 350;
    private final int TABLE_TOP_Y = 350;
    private final int TABLE_WIDTH = 50;
    private final int TABLE_HEIGHT = 50;
    private final int GRILL_LEFT_X = 100;
    private final int GRILL_TOP_Y = 20;
    private final int GRILL_WIDTH = 100;
    private final int GRILL_HEIGHT = 20;
    private final int PLATE_LEFT_X = 100;
    private final int PLATE_TOP_Y = 110;
    private final int PLATE_WIDTH = 400;
    private final int PLATE_HEIGHT = 20;
    private final int FRIDGE_LEFT_X = 240;
    private final int FRIDGE_TOP_Y = 50;
    private final int FRIDGE_WIDTH = 20;
    private final int FRIDGE_HEIGHT = 40;
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();

    public RestaurantVkAnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
        
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
 
    	Timer timer = new Timer(20, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        // Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        // Here are the tables.
        g2.setColor(Color.ORANGE);
        g2.fillRect(TABLE_LEFT_X, TABLE_TOP_Y, TABLE_WIDTH, TABLE_HEIGHT);
        g2.fillRect(TABLE_LEFT_X + (TABLE_WIDTH + 10), TABLE_TOP_Y, TABLE_WIDTH, TABLE_HEIGHT);
        g2.fillRect(TABLE_LEFT_X + (2 * (TABLE_WIDTH + 10)), TABLE_TOP_Y, TABLE_WIDTH, TABLE_HEIGHT);
        g2.fillRect(TABLE_LEFT_X + (3 * (TABLE_WIDTH + 10)), TABLE_TOP_Y, TABLE_WIDTH, TABLE_HEIGHT);
        
        g2.setColor(Color.YELLOW);
        g2.fillRect(GRILL_LEFT_X, GRILL_TOP_Y, GRILL_WIDTH, GRILL_HEIGHT);
        g2.setColor(Color.BLACK);
        g2.drawString("GRILL", GRILL_LEFT_X + 30, GRILL_TOP_Y + 15);
        
        g2.setColor(Color.BLUE);
        g2.fillRect(PLATE_LEFT_X, PLATE_TOP_Y, PLATE_WIDTH, PLATE_HEIGHT);
        g2.setColor(Color.WHITE);
        g2.drawString("PLATING AREA", PLATE_LEFT_X + 160, PLATE_TOP_Y + 15);
        
        g2.setColor(Color.GRAY);
        g2.fillRect(FRIDGE_LEFT_X, FRIDGE_TOP_Y, FRIDGE_WIDTH, FRIDGE_HEIGHT);
        g2.setColor(Color.BLACK);
        g2.drawString("REFRIGERATOR", FRIDGE_LEFT_X + 20, FRIDGE_TOP_Y + 25);


        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }

    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(HostGui gui) {
        guis.add(gui);
    }
    
    public void addGui(WaiterGui gui) {
    	guis.add(gui);
    }
    
    public void addGui(CashierGui gui) {
    	guis.add(gui);
    }
    
    public void addGui(CookGui gui) {
    	guis.add(gui);
    }
    
    public void updatePosition() {
		for (Gui gui : guis) {
			gui.updatePosition();
		}
	}
}
