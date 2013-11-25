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
 * This holds the scroll panes for the customers and for waiters
 */
public class ListPanel extends JPanel implements ActionListener, KeyListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private JButton addPersonB = new JButton("Add");
    
    private JTextField addPersonT = new JTextField(10);
    private JCheckBox addPersonC = new JCheckBox("Hungry?");
    private JPanel addPersonP = new JPanel();
    
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

        setLayout(new GridLayout(2,1));
        
        addPersonP.setLayout(new GridLayout(4,1));
        
        
        addPersonP.add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));
               
        addPersonT.addKeyListener(this);

        addPersonC.addActionListener(this);
        
        
        addPersonP.add(addPersonT);
        addPersonP.add(addPersonC);
                
        addPersonB.addActionListener(this);
        addPersonP.add(addPersonB, BorderLayout.SOUTH);
        
        
        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        
        add(addPersonP);
        add(pane);
    }
    
    
    public void keyTyped(KeyEvent e) {
        if( !addPersonT.getText().equals("")){
        	addPersonC.setEnabled(true);
        }
        else{
        	addPersonC.setEnabled(false);
        }
    }
        
    
    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonB) {
        	// Chapter 2.19 describes showInputDialog()
            //addPerson(JOptionPane.showInputDialog("Please enter a name:"));
        	
        	// if hungry check box is selected
        		addPerson(addPersonT.getText(), addPersonC.isSelected());
        
        }
        else if (e.getSource() == addPersonC) {}
        else {
        	// Isn't the second for loop more beautiful?
            /*for (int i = 0; i < list.size(); i++) {
                JButton temp = list.get(i);*/
        	for (JButton temp:list){
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
    public void addPerson(String name, Boolean hungryCheck) {
        if (name != null) {
        	addPersonC.setEnabled(false);
        	addPersonC.setSelected(false);
        	
            JButton button = new JButton(name);
            button.setBackground(Color.white);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 5));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            list.add(button);
            view.add(button);
            restPanel.addPerson(type, name);//puts customer on list
            restPanel.showInfo(type, name, hungryCheck);//puts hungry button on panel
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