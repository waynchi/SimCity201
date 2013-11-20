package bank.gui;

import restaurant.CookAgent;
import restaurant.CustomerAgent;
import restaurant.MarketAgent;
import restaurant.WaiterAgent;

import javax.swing.*;

import agent.Agent;

import java.awt.*;
import java.awt.event.*;
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
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel
    private JCheckBox stateWB;//part of infoLabel
    private JCheckBox pause;
    private JPanel controlPanel;
    

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

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
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) {
        currentPerson = person;

        if (person instanceof CustomerAgent) {
        	stateWB.setVisible(false);
        	stateCB.setVisible(true);
            CustomerAgent customer = (CustomerAgent) person;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        } 
        infoPanel.validate();
    }
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stateCB) {
            if (currentPerson instanceof CustomerAgent) {
                CustomerAgent c = (CustomerAgent) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
        }
        if (e.getSource() == pause) {
        	if (pause.isSelected()) {
        		for(Agent agent : restPanel.agents) {
        			agent.pause();
        		}
        	}
        	if (!pause.isSelected()) {
        		for(Agent agent : restPanel.agents) {
        			agent.restart();
        		}        		
        	}
        }
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(CustomerAgent c) {
        if (currentPerson instanceof CustomerAgent) {
            CustomerAgent cust = (CustomerAgent) currentPerson;
            if (c.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
    
    
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
