package market.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.concurrent.Semaphore;


/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class MarketPanel extends JPanel {

	// decide how many tables to have
	private int nTables = 4;


	//Host, cook, waiters and customers

	private JPanel marketLabel = new JPanel();
	//private ListPanel customerPanel = new ListPanel(this, "Customers");
	private JPanel group = new JPanel();

	private MarketGui gui; //reference to main gui

	public MarketPanel(MarketGui gui) {

		this.gui = gui;

		//gui.animationPanel.addGui(hostGui);
		//city gui should have reference to each and every gui panels and pass the reference to the respective roles.
		// for example, pass a MarketGui reference to a MarketEmployeeRole
		
		setLayout(new GridLayout(1, 2, 20, 20));
		group.setLayout(new GridLayout(1, 2, 10, 10));

		//group.add(customerPanel);

		initMarketLabel();
		add(marketLabel);
		add(group);
	}

	/**
	 * Sets up the restaurant label that includes the menu,
	 * and host and cook information
	 */
	private void initMarketLabel() {
		JLabel label = new JLabel();
		//restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
		marketLabel.setLayout(new BorderLayout());
		//label.setText(
		//		"<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

		marketLabel.setBorder(BorderFactory.createRaisedBevelBorder());
		marketLabel.add(label, BorderLayout.CENTER);
		marketLabel.add(new JLabel("               "), BorderLayout.EAST);
		marketLabel.add(new JLabel("               "), BorderLayout.WEST);
	}

	/**
	 * When a customer or waiter is clicked, this function calls
	 * updatedInfoPanel() from the main gui so that person's information
	 * will be shown
	 *
	 * @param type indicates whether the person is a customer or waiter
	 * @param name name of person
	 */
	/*public void showInfo(String type, String name) {

		if (type.equals("Customers")) {

			for (int i = 0; i < customers.size(); i++) {
				CustomerAgent temp = customers.get(i);
				if (temp.getName() == name)
					gui.updateInfoPanel(temp);
			}
		}
	}*/


}
