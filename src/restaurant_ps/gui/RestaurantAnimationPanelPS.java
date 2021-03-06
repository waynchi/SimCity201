package restaurant_ps.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class RestaurantAnimationPanelPS extends JPanel implements ActionListener {
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
    private ImageIcon rest_floor = new ImageIcon("src/restaurant_ps/gui/myfloor.jpg");
    private ImageIcon rest_table = new ImageIcon("src/restaurant_ps/gui/mysprite.png");
    private ImageIcon fridge = new ImageIcon("src/restaurant_ps/gui/myfridge.jpg");



    public RestaurantAnimationPanelPS(Timer timer) {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
     	Timer t = timer;
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
//        g2.setColor(Color.ORANGE);
//        for (int i=1; i<4; i++){
//            g2.drawImage(rest_table.getImage(),50+100*(i-1), 100,50,50, null);
//        }

        g2.drawImage(rest_table.getImage(),300,50,50,50, null);
        g2.drawImage(rest_table.getImage(),400,50,50,50, null);
        g2.drawImage(rest_table.getImage(),350,125,50,50, null);
        // grilling
        g2.setColor(Color.yellow);
        g2.fillRect(50, 250, 60, 20);//cooking
        
        // plating
    	g2.setColor(Color.green);
        for (int i=1; i<4; i++) {
        	g2.fillRect(20, 130+30*i, 40, 20);
        }
        
        // fridge
        g2.drawImage(fridge.getImage(),20,320,30,60, null);
        
        //revolving stand
        g2.setColor(Color.white);
        g2.fillOval(300, 250, 70, 70);
        
        
        g2.setColor(Color.white);
//        g2.drawString("Cooking", 0, 265);
//        g2.drawString("Plating",0,225);
                
        
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

    public void addGui(CustomerGuiPS gui) {
        guis.add(gui);
    }

    public void addGui(WaiterGuiPS gui) {
        guis.add(gui);
    }
    
    public void addGui(CookGuiPS gui) {
    	guis.add(gui);
    }

	public void addGui(RestaurantCashierGuiPS cashierGui) {
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
