package bank.gui;

import javax.swing.*;

import bank.BankCustomerRole;
import bank.TellerRole;
import agent.Agent;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;
import java.util.List;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class BankGui extends JPanel implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	public static AnimationPanel animationPanel = new AnimationPanel();
    
    public static List<BankCustomerGui> customers = new ArrayList<BankCustomerGui>();
    
    public static TellerGui teller;

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public BankGui() {
        int WINDOWX = 500;
        int WINDOWY = 250;

        Dimension animationDim = new Dimension(WINDOWX, WINDOWY);
        animationPanel.setPreferredSize(animationDim);
        animationPanel.setMinimumSize(animationDim);
        animationPanel.setMaximumSize(animationDim);

    	
    	setBounds(50, 50, WINDOWX, WINDOWY);
    	
        setLayout(new BorderLayout());
        
        add(animationPanel, BorderLayout.EAST);
        
    }
    
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
        
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    
    public BankCustomerGui addPerson(BankCustomerRole b) {
    	BankCustomerGui g = new BankCustomerGui(b);
    	animationPanel.addGui(g);
    	b.setGui(g);
    	return g;
    }
    
    public TellerGui addPerson(TellerRole t) {
     	TellerGui g2 = new TellerGui(t);
     	animationPanel.addGui(g2);
     	teller = g2;
     	return g2;
    }
    
    public void popCustomer() {
    	for(int i = 0; i < customers.size(); i++) {
    		customers.get(i).setCust(i-1);
    	}
    	customers.remove(0);
    }
    
    public void gotoLine(BankCustomerGui b) {
    	b.setCust(customers.size());
    	customers.add(b);
    	b.isWaiting = true;
    }
    
    /**
     * Main routine to get gui started
//     */
//    public static void main(String[] args) {
//        BankGui gui = new BankGui();
//    	
//        gui.setTitle("SimCity Bank");
//        gui.setVisible(true);
//        gui.setResizable(false);
//        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }
}
