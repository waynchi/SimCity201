package market.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements ActionListener {
	
	private final int WINDOWX = 500;
    private final int WINDOWY = 400;
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(20, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        //Here is the main entrance
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(500,120, 40, 100);
        
        //Here is the counter
        g2.setColor(Color.black);
        g2.fillRect(200, 120, 40, 100);//200 and 250 need to be table params

        // here is the register
        g2.setColor(Color.orange);
        g2.fillRect(350, 30, 100, 40);
        
        // here are the cabinets
        g2.setColor(Color.blue);
        g2.fillRect(50, 50, 100, 30);
        g2.setColor(Color.cyan);
        g2.fillRect(50, 100, 100, 30);
        g2.setColor(Color.green);
        g2.fillRect(50, 150, 100, 30);
        g2.setColor(Color.gray);
        g2.fillRect(50, 200, 100, 30);
        g2.setColor(Color.pink);
        g2.fillRect(50, 250, 100, 30);


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

    public void addGui(MarketCustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(MarketEmployeeGui gui) {
        guis.add(gui);
    }
    
    public void addGui(MarketTruckGui gui) {
    	guis.add(gui);
    }
}
