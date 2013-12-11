package restaurant_zt.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanelZt extends JPanel implements ActionListener {
    private final int WINDOWX = 400;
    private final int WINDOWY = 200;
	static final int TABLEFILLX = 80;
	static final int TABLEFILLX2 = 180;
	static final int TABLEFILLX3 = 280;
	static final int TABLEFILLY = 100;
	static final int TABLEWIDTH = 50;
	static final int TABLEHEIGHT = 50;
	static final int NULLX = 0;
	static final int NULLY = 0;
    private Image bufferImage;
    private Dimension bufferSize;
    private List<Gui> guis = new ArrayList<Gui>();
    private ImageIcon floor = new ImageIcon("res/restaurant_zt/floor.png");


    public AnimationPanelZt(Timer timer) {
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
        g2.fillRect(NULLX, NULLY, WINDOWX, WINDOWY );
        
        g2.drawImage(floor.getImage(), 0, 0, 500, 500, null);


        //Here is the table
        g2.setColor(Color.ORANGE);
        g2.fillRect(TABLEFILLX, TABLEFILLY, TABLEWIDTH, TABLEHEIGHT);
        
        g2.setColor(Color.BLACK);
        g2.fillRect(100, 200, TABLEWIDTH, 30); //Grill
        
        g2.setColor(Color.GRAY);
        g2.fillRect(20, 200, 20, 100); //FRIDGE

        g2.setColor(Color.ORANGE);
        g2.fillRect(TABLEFILLX2, TABLEFILLY, TABLEWIDTH, TABLEHEIGHT);
        
        g2.setColor(Color.ORANGE);
        g2.fillRect(TABLEFILLX3, TABLEFILLY, TABLEWIDTH, TABLEHEIGHT);

        //revolving stand
        g2.fillOval(130, 250, 30, 30);
        
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
                gui.draw(g2);
            }
        }
    }

    public void addGui(CustomerGuiZt gui) {
        guis.add(gui);
    }

    public void addGui(WaiterGuiZt gui) {
        guis.add(gui);
    }
    
    public void addGui(CookGui gui) {
    	guis.add(gui);
    }

	public void addGui(CashierGuiZt cashierGui) {
		guis.add(cashierGui);	
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
