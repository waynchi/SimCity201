package bank.gui;

import javax.swing.*;

import bank.BankCustomerRole;
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
public class BankGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	JFrame animationFrame = new JFrame("Bank Animation");
	public static AnimationPanel animationPanel = new AnimationPanel();
    
    public static List<BankCustomerGui> customers = new ArrayList<BankCustomerGui>();

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public BankGui() {
        int WINDOWX = 450;
        int WINDOWY = 450;

        Dimension animationDim = new Dimension(WINDOWX, WINDOWY);
        animationPanel.setPreferredSize(animationDim);
        animationPanel.setMinimumSize(animationDim);
        animationPanel.setMaximumSize(animationDim);

    	
    	setBounds(50, 50, WINDOWX, WINDOWY);
    	
        setLayout(new BorderLayout());
        
        add(animationPanel, BorderLayout.EAST);
        
    }
    
    public void addPerson(String type, BankCustomerRole c) {

    	BankCustomerGui g = new BankCustomerGui(c, customers.size());
    	animationPanel.addGui(g);
    	c.setGui(g);
    	customers.add(g);
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
    
    
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        BankGui gui = new BankGui();
        BankCustomerRole b = new BankCustomerRole("bob");
    	BankCustomerGui g = new BankCustomerGui(b, customers.size());
    	animationPanel.addGui(g);
    	b.setGui(g);
    	customers.add(g);
    	
        BankCustomerRole b1 = new BankCustomerRole("bob1");
    	BankCustomerGui g1 = new BankCustomerGui(b1, customers.size());
    	animationPanel.addGui(g1);
    	b1.setGui(g1);
    	customers.add(g1);
    	
        gui.setTitle("SimCity Bank");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
