package restaurant_vk.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
    private final int STAND_LEFT_X = 210;
    private final int STAND_TOP_Y = 90;
    private final int STAND_DIAMETER = 20;
    private Image bufferImage;
    private Dimension bufferSize;
    public Image cookingGrill = new BufferedImage(100, 20, BufferedImage.TYPE_INT_BGR);

    private List<VkGui> guis = new ArrayList<VkGui>();

    public RestaurantVkAnimationPanel(Timer timer) {
    	setSize(WINDOWX, WINDOWY);
    	this.setSize(WINDOWX, WINDOWY);
		this.setPreferredSize(new Dimension(WINDOWX, WINDOWY));
		this.setMaximumSize(new Dimension(WINDOWX, WINDOWY));
		this.setMinimumSize(new Dimension(WINDOWX, WINDOWY));
        setVisible(true);
        
        bufferSize = this.getSize();
        
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));

    	try {
			cookingGrill = ImageIO.read(new File("res/restaurant_vk/cookingGrill.jpg"));
		} catch (IOException e) {
			System.out.println("Image not found.");
		}
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
        
        g.drawImage(cookingGrill, GRILL_LEFT_X, GRILL_TOP_Y, null);
        
        g2.setColor(Color.BLUE);
        g2.fillRect(PLATE_LEFT_X, PLATE_TOP_Y, PLATE_WIDTH, PLATE_HEIGHT);
        g2.setColor(Color.WHITE);
        g2.drawString("PLATING AREA", PLATE_LEFT_X + 160, PLATE_TOP_Y + 15);
        
        g2.setColor(Color.GRAY);
        g2.fillRect(FRIDGE_LEFT_X, FRIDGE_TOP_Y, FRIDGE_WIDTH, FRIDGE_HEIGHT);
        g2.setColor(Color.BLACK);
        g2.drawString("REFRIGERATOR", FRIDGE_LEFT_X + 20, FRIDGE_TOP_Y + 25);
        
        g2.setColor(Color.ORANGE);
        g2.fillOval(STAND_LEFT_X, STAND_TOP_Y, STAND_DIAMETER, STAND_DIAMETER);
        g2.setColor(Color.BLACK);
        g2.drawString("REVOLVING STAND", STAND_LEFT_X + 20, STAND_TOP_Y + 15);

        for(VkGui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(VkGui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }

    public void addGui(VkCustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(VkHostGui gui) {
        guis.add(gui);
    }
    
    public void addGui(VkWaiterGui gui) {
    	guis.add(gui);
    }
    
    public void addGui(VkCashierGui gui) {
    	guis.add(gui);
    }
    
    public void addGui(VkCookGui gui) {
    	guis.add(gui);
    }
    
    public void updatePosition() {
		for (VkGui gui : guis) {
			gui.updatePosition();
		}
	}
}
