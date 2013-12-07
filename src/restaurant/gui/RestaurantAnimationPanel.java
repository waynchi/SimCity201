package restaurant.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class RestaurantAnimationPanel extends JPanel implements ActionListener {
	//static final int?
    private final int WINDOWX = 500;
    private final int WINDOWY = 400;
    private final int TABLENUM = 3;
    //private final int TABLEX = 200;
    //private final int TABLEY = 250;
    
    
    private final int TABLEWIDTH = 50;
    
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();
    private ImageIcon rest_floor = new ImageIcon("res/restaurant/rest_floor.jpeg");
    private ImageIcon rest_table = new ImageIcon("res/restaurant/rest_table.png");
    private ImageIcon fridge = new ImageIcon("res/restaurant/fridge.png");



    public RestaurantAnimationPanel(Timer timer) {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
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
        
        g2.drawImage(rest_floor.getImage(), 0, 0, 500, 400, null);


        //Here is the table
        g2.setColor(Color.ORANGE);
        for (int i=1; i<4; i++){
            g2.drawImage(rest_table.getImage(),50+100*(i-1), 100,50,50, null);
        }

        // grilling
        g2.setColor(Color.yellow);
        g2.fillRect(50, 250, 60, 20);//cooking
        
        // plating
    	g2.setColor(Color.lightGray);
        for (int i=1; i<4; i++) {
        	g2.fillRect(50+45*(i-1), 210, 40, 20);
        }
        
        // fridge
        g2.drawImage(fridge.getImage(),150,320,30,60, null);
        
        //revolving stand
        g2.fillOval(350, 250, 70, 70);
        
        
        g2.setColor(Color.white);
        g2.drawString("Cooking", 0, 265);
        g2.drawString("Plating",0,225);
                
        
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

	public void addGui(RestaurantCashierGui cashierGui) {
		guis.add(cashierGui);
		// TODO Auto-generated method stub
		
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
