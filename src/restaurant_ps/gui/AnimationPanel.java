package restaurant_ps.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class AnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 650;
    private final int WINDOWY = 450;
    int[] xTable = new int[3]; //3 tables
    int[] yTable = new int[3]; //3 tables
    private final int rectWidth = 50;
    private final int rectHeight = 50;
    private final int waitingAreaX = 40;
    private final int waitingAreaY = 175;
    private final int waitingAreaWidth = 80;
    private final int waitingAreaHeight = 40;
    private List<Gui> guis = new ArrayList<Gui>();

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        this.getSize();
 
    	Timer timer = new Timer(20, this );
    	timer.start();
    	xTable[0] = 250;
        yTable[0] = 250;
        xTable[1] = 250;
        yTable[1] = 150;
        xTable[2] = 250;
        yTable[2] = 50;
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        //Here is the table 1
        g2.setColor(Color.ORANGE);
        g2.fillRect(xTable[0], yTable[0], rectWidth, rectHeight);//200 and 250 need to be table params
        g2.setColor(Color.RED);
        g2.drawString("Table 1", xTable[0], yTable[0]+30);
        //Here is table 2
        g2.setColor(Color.GRAY);
        g2.fillRect(xTable[1], yTable[1], rectWidth, rectHeight);
        g2.setColor(Color.RED);
        g2.drawString("Table 2", xTable[1], yTable[1]+30);
        //Here is table 3
        g2.setColor(Color.CYAN);
        g2.fillRect(xTable[2], yTable[2], rectWidth, rectHeight);
        g2.setColor(Color.RED);
        g2.drawString("Table 3", xTable[2], yTable[2]+30);
        
        //Here is waiting area
        g2.setColor(Color.YELLOW);
        g2.fillRect(waitingAreaX, waitingAreaY, waitingAreaWidth, waitingAreaHeight);
        g2.setColor(Color.RED);
        g2.drawString("Waiting Area", waitingAreaX, waitingAreaY);
        
        
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
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

  
    
    public void addWaiterGui(WaiterGui wg) {
        guis.add(wg);
        
    }

    public void addHostGui(HostGui gui) {
        guis.add(gui);
    }
    public void addCustomerGui(CustomerGui gui) {
        guis.add(gui);
    }
    
    public void addCashierGui(CashierGui gui) {
    	guis.add(gui);
    }

	public void addCookGui(CookGui gui) {
		// TODO Auto-generated method stub
		guis.add(gui);
		
		
	}
}
