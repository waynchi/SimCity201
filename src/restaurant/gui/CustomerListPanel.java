package restaurant.gui;

import restaurant.HostRole;
import restaurant.RestaurantCustomerRole;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class CustomerListPanel extends JPanel implements ActionListener, KeyListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> customerList = new ArrayList<JButton>();
    private JButton addCustomerButton = new JButton("Add Customer");
    
    private JTextField addCustomerText = new JTextField(10);
    private JCheckBox addCustomerCheckHungry = new JCheckBox("Hungry?");
    private JPanel addCustomerPanel = new JPanel();
    
    private RestaurantPanel restPanel;
    private String type;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public CustomerListPanel(RestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;

        setLayout(new GridLayout(2,1));
        
        addCustomerText.addKeyListener(this);
        addCustomerCheckHungry.addActionListener(this);
        addCustomerButton.addActionListener(this);

        
        addCustomerPanel.setLayout(new GridLayout(3,1));
        
        JPanel combo = new JPanel();
        combo.add(addCustomerText);
        combo.add(addCustomerCheckHungry);
      
        addCustomerPanel.add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));
        addCustomerPanel.add(combo);
        addCustomerPanel.add(addCustomerButton);
        
        
        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        
        add(addCustomerPanel);
        add(pane);
    }
    
    // only enables the check hungry when a name is entered
    // now needs to input TWO Characters
    public void keyTyped(KeyEvent e) {
        if( !addCustomerText.getText().equals("")){
        	addCustomerCheckHungry.setEnabled(true);
        }
        else{
        	addCustomerCheckHungry.setEnabled(false);
        }
    }
        
    
    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addCustomerButton) {
        	// Chapter 2.19 describes showInputDialog()
            //addPerson(JOptionPane.showInputDialog("Please enter a name:"));
        	
        	// if hungry check box is selected
        		addPerson(addCustomerText.getText(), addCustomerCheckHungry.isSelected());
        
        }
        else if (e.getSource() == addCustomerCheckHungry) {}
        else { // a customer's name in the list is clicked, show his info in the info panel            
        	for (JButton temp:customerList){
                if (e.getSource() == temp)
                    restPanel.showInfo(type, temp.getText(), false);
            }
        }
    }

    /**
     * If the add button is pressed, this function creates
     * a spot for it in the scroll pane, and tells the restaurant panel
     * to add a new person.
     *
     * @param name name of new person
     */
    public void addPerson(String name, Boolean isHungry) {
        if (name != null) {
        	addCustomerCheckHungry.setEnabled(false);
        	addCustomerCheckHungry.setSelected(false);
        	
            JButton customerButton = new JButton(name);
            customerButton.setBackground(Color.white);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 5));
            customerButton.setPreferredSize(buttonSize);
            customerButton.setMinimumSize(buttonSize);
            customerButton.setMaximumSize(buttonSize);
            customerButton.addActionListener(this);
            customerList.add(customerButton);
            view.add(customerButton);
            restPanel.addPerson(type, name);//puts customer on list
            restPanel.showInfo(type, name, isHungry);//puts hungry button on panel
            validate();
        }
    }


	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}