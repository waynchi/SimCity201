package restaurant_ps.gui;


import javax.swing.*;

import restaurant.interfaces.Host;
import restaurant_ps.WaiterAgent;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
@SuppressWarnings("serial")
public class ListPanel extends JPanel implements ActionListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private JButton addPersonB = new JButton("Add");

    private RestaurantPanel restPanel;
    private String type;
    
    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(RestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;

        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));

        addPersonB.addActionListener(this);
        add(addPersonB);

        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        add(pane);
        
        
        
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonB && type == "Customers") {
        	// Chapter 2.19 describes showInputDialog()
        	JCheckBox checkbox = new JCheckBox("Hungry?"); 
        	checkbox.setSelected(true);
        	String message = "Please enter a name:";  
        	Object[] options = {message, checkbox};
        	addPerson(JOptionPane.showInputDialog(options),checkbox.isSelected());
            
        }
        else if(e.getSource() == addPersonB && type == "Waiters") {
        	String message = "Please enter the waiter's name:";  
        	//Object[] options = {message, checkbox};
        	addWaiter(JOptionPane.showInputDialog(message));
        }
        else {
        	// Isn't the second for loop more beautiful?
            /*for (int i = 0; i < list.size(); i++) {
                JButton temp = list.get(i);*/
        	
        	for (JButton temp:list){
                if (e.getSource() == temp)
                    restPanel.showInfo(type, temp.getText());
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
    
    public void addWaiter(String waiterName) {
		// TODO Auto-generated method stub
    	if(waiterName == null)
   			return;
    	
    	 JButton button = new JButton(waiterName);
         button.setBackground(Color.white);

         Dimension paneSize = pane.getSize();
         Dimension buttonSize = new Dimension(paneSize.width - 20,
                 (int) (paneSize.height / 7));
         button.setPreferredSize(buttonSize);
         button.setMinimumSize(buttonSize);
         button.setMaximumSize(buttonSize);
         button.addActionListener(this);
         list.add(button);
         view.add(button);
         restPanel.showInfo(type, waiterName);//puts hungry button on panel
         validate();
    	
    	
    	restPanel.restaurantWaiters.add(new WaiterAgent(waiterName,restPanel.cook,(restaurant_ps.interfaces.Host) restPanel.host,restPanel.inventory));
    	restPanel.showWaiters();
	}
    
    
    public void addPerson(String name, boolean isHungry) {
        if (name != null) {
        	
            JButton button = new JButton(name);
            button.setBackground(Color.white);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 7));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            list.add(button);
            view.add(button);
            restPanel.addPerson(type, name, isHungry);//puts customer on list
            restPanel.showInfo(type, name);//puts hungry button on panel
            validate();
        }
    }
}
