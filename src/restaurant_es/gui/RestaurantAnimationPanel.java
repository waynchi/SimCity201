package restaurant_es.gui;

import javax.swing.*;

import restaurant_es.gui.Gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class RestaurantAnimationPanel extends JPanel implements ActionListener {
	
	private final int WINDOWX = 450;
    private final int WINDOWY = 400;
    private Image bufferImage;
    private Dimension bufferSize;
    
    private static final int COOKX = 200;
    private static final int COOKY = 225;
    private static final int COOKSIZE = 25;
    
    private static final int PLATEX = 200;
    private static final int PLATEY = 275;
    private static final int PLATESIZE = 25;
    
    private static final int TABLEX = 100;
    private static final int TABLEY = 250;
    private static final int TABLESIZE = 50;
    static final int NTABLES = 3;

    private List<Gui> guis = new ArrayList<Gui>();

    public RestaurantAnimationPanel(Timer t) {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = t;
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        //Here are the tables
        g2.setColor(Color.ORANGE);
        
        g2.fillRect(100, 100, TABLESIZE, TABLESIZE);
        
        g2.fillRect(200, 100, TABLESIZE, TABLESIZE);
        
        g2.fillRect(300, 100, TABLESIZE, TABLESIZE);
        
        g2.fillOval(350, 250, 70, 70); //revolving
        
        g2.setColor(Color.RED);
        g2.fillRect(COOKX, COOKY, 100, COOKSIZE);
        
        g2.setColor(Color.BLUE);
        g2.fillRect(PLATEX, PLATEY, 100, PLATESIZE);
        
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

    public void addGui(WaiterGui gui) {
        guis.add(gui);
    }
    
    public void addGui(CookGui gui) {
    	guis.add(gui);
    }
	
	public void updatePosition() {
		for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
		repaint();
	}

}
