package restaurant_wc.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 600;
    private final int WINDOWY = 600;
    private final int xIncrement = 100;
    private final int yIncrement = -50;
    public int KitchenX = 400;
    public int KitchenY = 350;
    public int KitchenSize = 150;
    public int GrillX = 525;
    public int GrillY = 350;
    public int GrillSize = 25;
    public int FridgeX = 475;
    public int FridgeY = 475;
    public int tableNum = 0;
    private int xPos = 200;
    private int yPos = 250;
    private int Sides = 50;
    private Image bufferImage;
    private Dimension bufferSize;
    private Graphics2D g2;
    int i = 0;  

    private List<Gui> guis = new ArrayList<Gui>();

    public AnimationPanel() {
    	this.setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(20, this );
    	timer.start();

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

    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(WaiterGui gui) {
        guis.add(gui);
    }
    
    public void addGui(CookGui gui) {
    	guis.add(gui);
    }
}
