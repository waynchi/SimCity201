package bank.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 450;
    private final int WINDOWY = 400;
    private Image bufferImage;
    private Dimension bufferSize;
    
    private static final int COOKX = 300;
    private static final int COOKY = 0;
    private static final int COOKSIZE = 25;
    
    private static final int PLATEX = 300;
    private static final int PLATEY = 50;
    private static final int PLATESIZE = 25;
    
    private static final int TABLEX = 100;
    private static final int TABLEY = 250;
    private static final int TABLESIZE = 50;
    static final int NTABLES = 3;

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

        //Here are the tables
        g2.setColor(Color.ORANGE);
        
        for(int i = 0; i < NTABLES; i++) {
        	g2.fillRect(TABLEX*(i + 1), TABLEY, TABLESIZE, TABLESIZE); //Draw all of the tables with a 50px gap in between them
        }
        
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

    public void addGui(BankCustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(TellerGui gui) {
        guis.add(gui);
    }
}
