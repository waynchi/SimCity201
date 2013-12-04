package transportation.gui;


import javax.swing.*;

import transportation.Gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class BusStopPanel extends JPanel implements ActionListener {
	
	private final int WINDOWX = 500;
    private final int WINDOWY = 400;
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();
    //public List<MarketCustomerGui> marketCustomerGui = new ArrayList<MarketCustomerGui>();
    private ImageIcon market_background = new ImageIcon("res/market/market_background.jpeg");
    

    public BusStopPanel() {
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
        
        g2.drawImage(market_background.getImage(), 0, 0, 500, 400, null);



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
//
//    public void addGui(MarketCustomerGui gui) {
//        guis.add(gui);
//    }
//
//    public void addGui(MarketEmployeeGui gui) {
//        guis.add(gui);
//    }
//    
//    public void addGui(MarketTruckGui gui) {
//    	guis.add(gui);
//    }

	public void updatePosition() {
		// TODO Auto-generated method stub
		for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
		}
		repaint();
	}

//	public void addGui(MarketCashierGui marketCashierGui) {
//		// TODO Auto-generated method stub
//		guis.add(marketCashierGui);
//	}
}
