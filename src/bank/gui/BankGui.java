package bank.gui;

import javax.swing.*;

import bank.BankCustomerRole;
import agent.Agent;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class BankGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	JFrame animationFrame = new JFrame("Restaurant Animation");
	public AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private BankPanel bankPanel = new BankPanel(this);
    
    public void addPerson(String type, String name, String status, BankCustomerRole c) {

    	BankCustomerGui g = new BankCustomerGui(c, gui, customers.size());
    	animationPanel.addGui(g);// dw
    	c.setGui(g);
    	customers.add(c);
    }
    
    public Vector<BankCustomerRole> customers = new Vector<BankCustomerRole>();

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public BankGui() {
        int WINDOWX = 450;
        int WINDOWY = 450;

        Dimension animationDim = new Dimension(WINDOWX+100, WINDOWY+100);
        animationPanel.setPreferredSize(animationDim);
        animationPanel.setMinimumSize(animationDim);
        animationPanel.setMaximumSize(animationDim);

    	
    	setBounds(50, 50, WINDOWX*3, WINDOWY);
    	
        setLayout(new BorderLayout());
        
        add(animationPanel, BorderLayout.EAST);
        
        Dimension controlDim = new Dimension(WINDOWX+50, WINDOWY);
        controlPanel = new JPanel();
        controlPanel.setPreferredSize(controlDim);
        controlPanel.setMinimumSize(controlDim);
        controlPanel.setMaximumSize(controlDim);
        controlPanel.setLayout(new BorderLayout());
        

        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .6));
        bankPanel.setPreferredSize(restDim);
        bankPanel.setMinimumSize(restDim);
        bankPanel.setMaximumSize(restDim);
        controlPanel.add(bankPanel, BorderLayout.NORTH);
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * .25));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);
        
        stateWB = new JCheckBox();
        stateWB.setVisible(false);
        stateWB.addActionListener(this);
        
        pause = new JCheckBox("Pause");
        pause.setVisible(true);
        pause.addActionListener(this);

        infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        infoPanel.add(stateWB);
        controlPanel.add(infoPanel, BorderLayout.CENTER);
        controlPanel.add(pause, BorderLayout.WEST);
        
        add(controlPanel, BorderLayout.WEST);
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
        gui.setTitle("SimCity Bank");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
