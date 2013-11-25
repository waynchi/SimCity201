package restaurant.gui;
//this is the main class for gui stuff
import restaurant.BaseWaiterRole;
import restaurant.RestaurantCustomerRole;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame implements ActionListener {
    /* The GUI has only one frame and two panels within, the control panel 
     * and the animation panel
     */
	AnimationPanel animationPanel = new AnimationPanel();
	
	// boundaries and coordinates of the two panels.
	private int RESTPANELX = 50;
	private int RESTPANELY = 50;
	private int RESTPANELW = 1200;
	private int RESTPANELH = 600;
	/* restPanel holds 3 panels
     * 1) the staff listing, menu, and and image of restaurant together with pause/restart button
     * 2) lists of current customers and waiters all constructed in RestaurantPanel()
     * 3) the infoPanel about the clicked Customer or Waiter
     */    
    private RestaurantPanel restPanel = new RestaurantPanel(this);
    private CustomerListPanel customerPanel = new CustomerListPanel(restPanel, "Customers");
    private WaiterListPanel waiterPanel = new WaiterListPanel(restPanel, "Waiters");
    private JPanel listPanel = new JPanel();
    
    
    /* infoPanel holds information about the clicked customer and waiter, if there is one*/
    private JPanel infoPanel;
    private JLabel customerInfoLabel; //part of infoPanel
    private JCheckBox customerStateCB;//part of infoLabel
    private JLabel waiterInfoLabel;//part of infoPanel
    private JButton onBreakButton;//part of infoPanel
    private JButton offBreakButton;
    
    private JPanel customerInfoPanel;//part of infoPanel
    private JPanel waiterInfoPanel;//part of infoPanel
    
    
    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
    	 
     	setBounds(50, 50, RESTPANELW, RESTPANELH);
        
     	JPanel controlPanel = new JPanel();
     	controlPanel.setLayout(new GridLayout(3,1,10,10));

     	setLayout(new GridLayout(1,2,10,10));
     	
         Dimension restDim = new Dimension(RESTPANELX, (int) (RESTPANELY * .6));
         restPanel.setPreferredSize(restDim);
         restPanel.setMinimumSize(restDim);
         restPanel.setMaximumSize(restDim);
         
         // Now, setup the info panel
         Dimension infoDim = new Dimension(RESTPANELX, (int) (RESTPANELY * .25));
         infoPanel = new JPanel();
         infoPanel.setPreferredSize(infoDim);
         infoPanel.setMinimumSize(infoDim);
         infoPanel.setMaximumSize(infoDim);
         infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

         customerStateCB = new JCheckBox();
         customerStateCB.setVisible(false);
         customerStateCB.addActionListener(this);
         
         onBreakButton = new JButton("Go On Break");
         onBreakButton.setVisible(false);
         onBreakButton.requestFocus(true);
         onBreakButton.addActionListener(this);
         
         offBreakButton = new JButton("Go Off Break");
         offBreakButton.setVisible(false);
         offBreakButton.requestFocus();
         offBreakButton.addActionListener(this);
         
         infoPanel.setLayout(new GridLayout(2, 1, 30, 0));
         
         customerInfoLabel = new JLabel(); 
         waiterInfoLabel = new JLabel();
         
         
         customerInfoPanel = new JPanel();
         waiterInfoPanel = new JPanel();
         customerInfoPanel.add(customerInfoLabel);
         customerInfoPanel.add(customerStateCB);
         waiterInfoPanel.add(waiterInfoLabel);
         waiterInfoPanel.add(onBreakButton);
         waiterInfoPanel.add(offBreakButton);
         
         infoPanel.add(customerInfoPanel);
         infoPanel.add(waiterInfoPanel);
         
         
         listPanel.setLayout(new GridLayout(1,2,10,10));
         listPanel.add(customerPanel);
         listPanel.add(waiterPanel);
 		
         
         // add three major panels to controlPanel
         controlPanel.add(restPanel);
         controlPanel.add(listPanel);
         controlPanel.add(infoPanel);
         
         add(controlPanel);
         add(animationPanel);
         
     }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person, Boolean hungryCheck) {
        currentPerson = person;

        if (person instanceof RestaurantCustomerRole) {
            customerStateCB.setVisible(true);
            RestaurantCustomerRole customer = (RestaurantCustomerRole) person;
            customerStateCB.setText("Hungry?");            
            
            // if hungry is checked when the customer is added to the list
            if (hungryCheck) {
            	RestaurantCustomerRole c = (RestaurantCustomerRole) currentPerson;
                c.getGui().setHungry();
                customerStateCB.setEnabled(false);
            }
            
            customerStateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            customerStateCB.setEnabled(!customer.getGui().isHungry());
            customerInfoLabel.setText(
               "<html><pre> Customer Name:  " + customer.getName() + " </pre></html>");
        }
        
        if (person instanceof BaseWaiterRole) {
        	BaseWaiterRole waiter = (BaseWaiterRole) person;
        	waiterInfoLabel.setText(               
        			"<html><pre> Waiter Name:  " + waiter.getName() + " </pre></html>");
        	if (waiter.isOnBreak()){
        		offBreakButton.setVisible(true);
        		onBreakButton.setVisible(false);
        	}
        	else {
        		onBreakButton.setVisible(true);
        		offBreakButton.setVisible(false);

        	}
        	// something else needs to be done here
        }
        
        infoPanel.validate();
    }
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == customerStateCB) {
            if (currentPerson instanceof RestaurantCustomerRole) {
                RestaurantCustomerRole c = (RestaurantCustomerRole) currentPerson;
                c.getGui().setHungry();
                customerStateCB.setEnabled(false);
            }
        }
        
        if (e.getSource() == onBreakButton) {
        	if (currentPerson instanceof BaseWaiterRole) {
        		final BaseWaiterRole w = (BaseWaiterRole) currentPerson;
        		w.msgAskForBreak();
        		w.getHost().IWantABreak(w);
        		onBreakButton.setEnabled(false);
        		
        		new java.util.Timer().schedule(
        				new java.util.TimerTask(){
        					public void run(){
        						if (w.getHost().getAvailableWaiters().size() != 1) {
        							System.out.println(w.getHost().getWaiters().size());
        		        			onBreakButton.setVisible(false);
        		        		    onBreakButton.setEnabled(true);
        		        			offBreakButton.setVisible(true);
        		        		}
        						else {
        		        		    onBreakButton.setEnabled(true);
        						}
        					}
        				},
        			   500);
        		
        		
        		
        	}
        }
        
       
        if (e.getSource() == offBreakButton) {
        	if (currentPerson instanceof BaseWaiterRole) {
        		BaseWaiterRole w = (BaseWaiterRole) currentPerson;
        		w.getHost().IAmOffBreak(w);
        		w.msgOffBreak();
        		onBreakButton.setVisible(true);
    			offBreakButton.setVisible(false);
        	}
        }
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(RestaurantCustomerRole c) {
        if (currentPerson instanceof RestaurantCustomerRole) {
            RestaurantCustomerRole cust = (RestaurantCustomerRole) currentPerson;
            if (c.equals(cust)) {
                customerStateCB.setEnabled(true);
                customerStateCB.setSelected(false);
            }
        }
    }
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        RestaurantGui gui = new RestaurantGui();
        gui.setTitle("csci201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public AnimationPanel getAnimationPanel(){
    	return animationPanel;
    }
}
