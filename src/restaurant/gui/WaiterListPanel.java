package restaurant.gui;

import restaurant.HostRole;
import restaurant.RestaurantCustomerRole;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantGui.
 * This holds the scroll panes for the waiters
 */
public class WaiterListPanel extends JPanel implements ActionListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> waiterList = new ArrayList<JButton>();
    private JButton addWaiterButton = new JButton("Add Waiter");
    
    private JTextField addWaiterName = new JTextField(10);
    private JPanel addWaiterPanel = new JPanel();
    
    private RestaurantPanel restPanel;
    private String type;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public WaiterListPanel(RestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;

        setLayout(new GridLayout(2,1));
        
        addWaiterButton.addActionListener(this);
        addWaiterPanel.setLayout(new GridLayout(3,1));
        addWaiterPanel.add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));
        addWaiterPanel.add(addWaiterName);
        addWaiterPanel.add(addWaiterButton);
        
        
        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        
        add(addWaiterPanel);
        add(pane);
    }

    
    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addWaiterButton) {
            //addPerson(JOptionPane.showInputDialog("Please enter a name:"));
        	
        		addPerson(addWaiterName.getText());
        
        }
        else { // a waiter's name in the list is clicked, show his info in the info panel            
        	for (JButton temp:waiterList){
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
    public void addPerson(String name) {
        if (name != null) {
        	
            JButton waiterButton = new JButton(name);
            waiterButton.setBackground(Color.white);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 5));
            waiterButton.setPreferredSize(buttonSize);
            waiterButton.setMinimumSize(buttonSize);
            waiterButton.setMaximumSize(buttonSize);
            waiterButton.addActionListener(this);
            waiterList.add(waiterButton);
            view.add(waiterButton);
            restPanel.addPerson(type, name);//puts customer on list
            restPanel.showInfo(type, name, false);//puts hungry button on panel
            validate();
        }
    }
}


