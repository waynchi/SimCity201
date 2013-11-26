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
    
    private ImageIcon bank_divider = new ImageIcon("res/bank/bank_divider.png");
    private ImageIcon floor = new ImageIcon("res/bank/floortile.png");

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

        g2.drawImage(floor.getImage(), 0, 0, 500, 250, null);
        g2.drawImage(bank_divider.getImage(), 300, 0, 12, 250, null);
        
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
    
    public void updatePosition() {
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
        repaint();
        
    }

}
