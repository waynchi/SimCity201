package bank.gui;


import javax.swing.*;

import bank.BankCustomerAgent;
import bank.TellerAgent;
import agent.Agent;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class BankPanel extends JPanel {

    //Host, cook, waiters and customers

    private TellerAgent host = new TellerAgent("Teller");

    public Vector<Agent> agents = new Vector<Agent>();
    public Vector<BankCustomerAgent> customers = new Vector<BankCustomerAgent>();

    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private JPanel group = new JPanel();

    private BankGui gui; //reference to main gui

    public BankPanel(BankGui gui) {
    	agents.add(host);
    	
        this.gui = gui;
        host.startThread();

        setLayout(new BorderLayout());
        group.setLayout(new GridLayout(1, 2, 10, 10));

        group.add(customerPanel);

        initRestLabel();
        add(restLabel, BorderLayout.WEST);
        add(group, BorderLayout.CENTER);
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + "Sarah" + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.00</td></tr><tr><td>Chicken</td><td>$7.50</td></tr><tr><td>Salad</td><td>$5.50</td></tr><tr><td>Pizza</td><td>$9.00</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("         "), BorderLayout.EAST);
        restLabel.add(new JLabel("         "), BorderLayout.WEST);
    }

    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
    public void showInfo(String type, String name) {

        if (type.equals("Customers")) {

            for (int i = 0; i < customers.size(); i++) {
                BankCustomerAgent temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name, String status) {

    	if (type.equals("Customers")) {
    		if (status.equals("Hungry")) {
    			BankCustomerAgent c = new BankCustomerAgent(name);	
    			BankCustomerGui g = new BankCustomerGui(c, gui, customers.size());

    			gui.animationPanel.addGui(g);// dw
    			c.setTeller(host);
    			c.setGui(g);
    			agents.add(c);
    			c.startThread();
    			c.getGui().setHungry();
    		}
    		else {
    			BankCustomerAgent c = new BankCustomerAgent(name);	
    			BankCustomerGui g = new BankCustomerGui(c, gui, customers.size());

    			gui.animationPanel.addGui(g);// dw
    			c.setTeller(host);
    			c.setGui(g);
    			agents.add(c);
    			c.startThread();
    		}
    	}
    }
}
