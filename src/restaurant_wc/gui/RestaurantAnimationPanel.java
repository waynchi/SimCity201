package restaurant_wc.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class RestaurantAnimationPanel extends JPanel implements ActionListener {
	
	private final int WINDOWX = 500;
    private final int WINDOWY = 400;
    private final int xIncrement = 100;
    private final int yIncrement = -50;
    public int KitchenX = 100;
    public int KitchenY = 250;
    public int KitchenSize = 150;
    public int GrillX = 225;
    public int GrillY = 250;
    public int GrillSize = 25;
    public int FridgeX = 175;
    public int FridgeY = 375;
    public int tableNum = 0;
    private int xPos = 100;
    private int yPos = 150;
    private int Sides = 50;
    private Image bufferImage;
    private Dimension bufferSize;
    private Graphics2D g2;
    int i = 0;  
    
    private ImageIcon floor = new ImageIcon("res/restaurantWc/background.png");

    private List<Gui> guis = new ArrayList<Gui>();

    public RestaurantAnimationPanel(Timer t) {
    	this.setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = t;
//    	timer.start();

    }
    
    public void editValues(int x, int y, int Side)
    {
    	xPos = x;
    	yPos = y;
    	Sides = Side;
    	System.out.println("CHANGED");
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}
	boolean dim = false;
    public void paintComponent(Graphics g) {
    	//this.setSize(600,600);
        g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );
        
        g2.drawImage(floor.getImage(), 0, 0, 500, 400, null);

        //Here is the table
        for(int i = 0; i <3; i++)
        {
        	g2.setColor(Color.ORANGE);
            g2.fillRect(xPos+ (i*xIncrement), yPos+(i*yIncrement), Sides, Sides);
        }
        
        //Here is the Kitchen area
        	g2.setColor(Color.GRAY);
        	g2.fillRect(KitchenX, KitchenY, KitchenSize, KitchenSize);
        	g2.setColor(Color.BLUE);
        	for(int j = 0; j < 3; j++)
        	{
        	g2.fillRect(GrillX, GrillY + j*35, GrillSize, GrillSize);
        	}
        	g2.setColor(Color.WHITE);
        	g2.fillRect(KitchenX, KitchenY, GrillSize, KitchenSize);
        	g2.setColor(Color.BLACK);
        	g2.fillRect(FridgeX, FridgeY, GrillSize, GrillSize);
        	//Revolving stand
        	g2.fillOval(250, 330, 70, 70);
      //  add(t1Label);
      //  System.out.println(this.getSize().height + "," + this.getSize().width);
        //drawForMe(xPos, yPos, Sides );
        //test.paintIcon(this, g2, 500, 500);
        	
        	
        
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
    
   /* public void drawForMe(int x, int y, int s )
    {
    	g2.setColor(Color.ORANGE);
    	g2.fillRect(x, y, s, s);
    }*/

    public void addGui(RestaurantCashierGui cashierGui) {
        guis.add(cashierGui);
    }

    public void addGui(WaiterGui gui) {
        guis.add(gui);
    }
    
    public void addGui(CookGui gui) {
    	guis.add(gui);
    }
    
    public void addGui(CustomerGui gui) {
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
