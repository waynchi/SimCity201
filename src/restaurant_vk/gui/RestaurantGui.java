package restaurant_vk.gui;

import restaurant_vk.CustomerAgent;
import restaurant_vk.CustomerAgent.AgentState;
import restaurant_vk.WaiterNormalAgent;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame implements ActionListener {
	AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels initially
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     * 
     * In the middle of execution, when new customers and waiters are getting added,
     * two new panels would be added and removed from the restPanel.
     */    
    private RestaurantPanel restPanel = new RestaurantPanel(this);
    
    // infoPanel holds information about the clicked customer, if there is one.
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel
    
    // List of food choices to select from once the customer has been seated.
    private JList<String> foodList;
    // This button calls the action of selecting the choice of food from the above list.
    private JButton selectButton = new JButton("Select");
    // This pane holds the foodList.
    private JScrollPane listHolder;

    // Holds the agent that the info is about. Seems like a hack
    private Object currentPerson;
    
    // A button that enables the customer to leave in certain situations.
    private JButton leaveButton = new JButton("Leave");

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
    	JPanel temp = new JPanel();
    	temp.setLayout(null);
    	
        int WINDOWX = 1500;
        int WINDOWY = 685;
        int WINDOW_LEFT = 50;
        int WINDOW_TOP = 50;
        
        animationPanel.setBounds(0, 0, 680, 480);
        temp.add(animationPanel);
    	
    	setBounds(WINDOW_LEFT, WINDOW_TOP, WINDOWX, WINDOWY);

        setLayout(new BoxLayout((Container) getContentPane(), BoxLayout.Y_AXIS));

        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .6));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        restPanel.setBounds(690, 0, 800, 400);
        restPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        temp.add(restPanel);
        
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

        infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Add customers and waiters.</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        add(infoPanel);
        
        add(temp);
        
        // Piece of code to set up list of food choices to select from.
        String[] foodChoices = new String[4];
        foodChoices[0] = "Steak";
        foodChoices[1] = "Chicken";
        foodChoices[2] = "Salad";
        foodChoices[3] = "Pizza";
        foodList = new JList<String>(foodChoices);
        foodList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        selectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String choice = (String) foodList.getSelectedValue();
				if (currentPerson instanceof CustomerAgent) {
					((CustomerAgent) currentPerson).msgDecideChoice(choice);
				}
				infoPanel.remove(listHolder);
				infoPanel.remove(selectButton);
				try {
					infoPanel.remove(leaveButton);
				} catch (Exception e) {
				}
				infoPanel.revalidate();
			}
        });
        
        leaveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (currentPerson instanceof CustomerAgent) {
					((CustomerAgent) currentPerson).msgWantToLeave();
				}
				infoPanel.remove(leaveButton);
				try {
					infoPanel.remove(listHolder);
					infoPanel.remove(selectButton);
				} catch (Exception e) {
				}
				infoPanel.revalidate();
			}
        });
        
        listHolder = new JScrollPane(foodList);
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) {
        stateCB.setVisible(true);
        currentPerson = person;

        if (person instanceof CustomerAgent) {
            CustomerAgent customer = (CustomerAgent) person;
            stateCB.setText("Hungry?");
            stateCB.setSelected(customer.getGui().isHungry());
            stateCB.setEnabled(!customer.getGui().isHungry());
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + "  Cash: $" + customer.getCash() + " </pre></html>");
            if (customer.getGui().isHungry() && customer.getState() == AgentState.DecidingOrder) {
            	foodList.setListData(customer.getGui().getMenuCopy());
            	infoPanel.add(listHolder);
            	infoPanel.add(selectButton);
            }
            else {
            	if (infoPanel.getComponentCount() > 2) {
            		infoPanel.remove(listHolder);
            		infoPanel.remove(selectButton);
            	}
            }
            if (customer.getGui().getLeaveOption() == true) {
            	infoPanel.add(leaveButton);
            }
            else {
            	try {
            		infoPanel.remove(leaveButton);
            	} catch (Exception e) {
            		System.out.println("Exception caught!");
            	}
            }
        }
        
        else if (person instanceof WaiterNormalAgent) {
        	WaiterNormalAgent waiter = (WaiterNormalAgent) person;
        	stateCB.setText("On a break?");
        	stateCB.setEnabled(waiter.getGui().isBreakEnabled());
            stateCB.setSelected(waiter.getGui().isBreakChecked());
            infoLabel.setText(
               "<html><pre>     Name: " + waiter.getName() + " </pre></html>");
            try {
        		infoPanel.remove(listHolder);
        		infoPanel.remove(selectButton);
        		infoPanel.remove(leaveButton);
        	} catch (Exception e) {}
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
            else if (currentPerson instanceof WaiterNormalAgent) {
            	WaiterNormalAgent w = (WaiterNormalAgent) currentPerson;
            	if (!w.getGui().isBreakChecked()) {
            		w.getGui().setBreakChecked();
            	}
            	else {
            		w.getGui().setBreakUnchecked();
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
    
    public void autoUpdate(Object person) {
    	if (person == currentPerson) {
    		updateInfoPanel(person);
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
}
