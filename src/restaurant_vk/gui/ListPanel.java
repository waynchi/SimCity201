package restaurant_vk.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class ListPanel extends JPanel implements ActionListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    
    public JScrollPane waiterListPane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private List<JButton> waiterList = new ArrayList<JButton>();
    private JPanel waiterView = new JPanel();
    
    // This button appears when the user wants to add a new customer.
    private JButton addPersonB = new JButton("Add Customer");
    // This button appears when the user wants to add a new waiter.
    private JButton addWaiterButton = new JButton("Add Waiter");
    // This panel would contain the JTExtFiel and JCheckBox needed to added
    // a new customer.
    private JPanel inputPanel = new JPanel();
    // This sets the initial hunger state of the newly added customer.
    private JCheckBox inputCheck = new JCheckBox();
    // This takes in the name of the new customer.
    private JTextField inputField = new JTextField();
    // This takes in the cash for a customer.
    private JTextField cashField = new JTextField();
    // This panel hold the JTextField to needed to add a new waiter.
    private JPanel waiterPanel = new JPanel();
    // This takes in the name of the new waiter.
    private JTextField waiterNameField = new JTextField();

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
        add(new JLabel("<html><pre><u>" + type + "</u><br></pre></html>"));

        addPersonB.addActionListener(this);
        add(addPersonB);
        
        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        pane.setMinimumSize(new Dimension(0, 150));
        pane.setMaximumSize(new Dimension(300, 150));
        add(pane);
        
        ActionListener l = new TextFieldListener();
        inputField.addActionListener(l);
        cashField.addActionListener(l);
        waiterNameField.addActionListener(new TextFieldListener());
        
        inputPanel.setLayout(new BorderLayout());
        JPanel group1 = new JPanel();
        group1.setLayout(new GridLayout(3, 2));
        group1.add(new JLabel("Name"));
        group1.add(inputField);
        group1.add(new JLabel("Cash"));
        group1.add(cashField);
        group1.add(new JLabel("Hungry?"));
        group1.add(inputCheck);
        inputPanel.add(group1, BorderLayout.NORTH);
        
        waiterPanel.setLayout(new BorderLayout());
        JPanel group2 = new JPanel();
        group2.setLayout(new GridLayout(2, 1));
        group2.add(new JLabel("Waiter's Name"));
        group2.add(waiterNameField);
        waiterPanel.add(group2, BorderLayout.NORTH);
        
        waiterView.setLayout(new BoxLayout((Container) waiterView, BoxLayout.Y_AXIS));
        waiterListPane.setViewportView(waiterView);
        waiterListPane.setMinimumSize(new Dimension(0, 150));
        waiterListPane.setMaximumSize(new Dimension(300, 150));
        addWaiterButton.addActionListener(this);
        add(new JLabel("<html><pre><u>" + "Waiters" + "</u><br></pre></html>"));
        add(addWaiterButton);
        add(waiterListPane);
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonB) {
            restPanel.add(inputPanel);
            inputField.requestFocusInWindow();
        	revalidate();
        }
        else if (e.getSource() == addWaiterButton) {
        	restPanel.add(waiterPanel);
        	waiterNameField.requestFocusInWindow();
        	revalidate();
        }
        else {
        	for (JButton temp:list) {
                if (e.getSource() == temp) {
                    restPanel.showInfo(type, temp.getText());
                    return;
                }
            }
        	for (JButton temp:waiterList) {
                if (e.getSource() == temp) {
                	restPanel.showInfo("Waiter", temp.getText());
                    return;
                }
            }
        }
    }
    
    /*
     * ActionListener for the two JTextFields.
     */
    private class TextFieldListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getSource() == inputField || arg0.getSource() == cashField) {
				String input = inputField.getText();
				String cashString = cashField.getText();
				if (input.equals("") || cashString.equals(""))
					return;
				double cash = 0.0;
				
				try{
					cash = Double.parseDouble(cashString);
				} catch (Exception e) {
					System.out.println("Write a DECIMAL value in cash field only!!");
					cashField.setText("");
					return;
				}
				
				createCustomer(input, cash);
				inputField.setText("");
				cashField.setText("");
				restPanel.remove(inputPanel);
			}
			if (arg0.getSource() == waiterNameField) {
				String name = waiterNameField.getText();
				if (name.equals(""))
					return;
				createWaiter(name);
				waiterNameField.setText("");
				restPanel.remove(waiterPanel);
			}
			restPanel.revalidate();
		}
    	
    }
    
    public boolean isNewCustHungry() {
    	return inputCheck.isSelected();
    }

    private void createWaiter(String name) {
    	if (name.equals(""))
    		return;
    	JButton b = new JButton(name);
    	b.setBackground(Color.WHITE);
    	Dimension paneSize = waiterListPane.getSize();
        Dimension buttonSize = new Dimension(paneSize.width - 20,
                (int) (paneSize.height / 7));
        b.setPreferredSize(buttonSize);
        b.setMinimumSize(buttonSize);
        b.setMaximumSize(buttonSize);
    	b.addActionListener(this);
    	waiterList.add(b);
    	waiterView.add(b);
    	restPanel.addPerson("Waiter", name, 0.0);
    	restPanel.showInfo("Waiter", name);
    	validate();
    }
    
    /**
     * If the add button is pressed, this function creates
     * a spot for it in the scroll pane, and tells the restaurant panel
     * to add a new person.
     *
     * @param name - name of new person, cash - cash of the new customer
     */
    public void createCustomer(String name, double cash) {
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
            restPanel.addPerson(type, name, cash);//puts customer on list
            restPanel.showInfo(type, name);//puts hungry button on panel
            validate();
        }
    }
}
