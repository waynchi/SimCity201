package restaurant_vk.gui;

import restaurant_vk.CashierAgent;
import restaurant_vk.CookAgent;
import restaurant_vk.CustomerAgent;
import restaurant_vk.HostAgent;
import restaurant_vk.MarketAgent;
import restaurant_vk.WaiterNormalAgent;
import restaurant_vk.CookAgent.Order;
import restaurant_vk.interfaces.Cashier;
import restaurant_vk.interfaces.Cook;
import restaurant_vk.interfaces.Host;
import restaurant_vk.interfaces.Market;
import restaurant_vk.interfaces.Waiter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

    //Host, cook, and customers
    private Host host = new HostAgent("Sarah");
    private HostGui hostGui = new HostGui((HostAgent)host);    
    private Cook cook = new CookAgent();
    private CookGui cookGui = new CookGui((CookAgent) cook);
    private Cashier cashier = new CashierAgent();
    private CashierGui cashierGui = new CashierGui();
    private Market market0 = new MarketAgent(0);
    private Market market1 = new MarketAgent(1);
    private Market market2 = new MarketAgent(2);
    private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
    private RevolvingStand stand = new RevolvingStand();

    private Dimension waiterHomePos = new Dimension(110, 130);
    
    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private JPanel group = new JPanel();
    
    // A boolean to aid implementation of the pausing-concept.
    private boolean isPaused = false;
    // JButton to pause or resume all the agents inside the restaurant.
    private JButton pauseResumeButton = new JButton("Pause");

    // Reference to main gui.
    private RestaurantGui gui;

    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
        ((HostAgent)host).setGui(hostGui);
        ((CashierAgent)cashier).setGui(cashierGui);
        ((CookAgent)cook).setGui(cookGui);

        gui.animationPanel.addGui(hostGui);
        gui.animationPanel.addGui(cashierGui);
        gui.animationPanel.addGui(cookGui);
        
        ((MarketAgent)market0).setInitialFoodQuantities(0, 1, 5, 0); // 0 1 5 0
        ((MarketAgent)market1).setInitialFoodQuantities(5, 5, 5, 0); // 5 5 5 0
        ((MarketAgent)market2).setInitialFoodQuantities(5, 1, 5, 0); // 5 1 5 0
        
        ((HostAgent)host).startThread();
        ((CookAgent)cook).startThread();
        ((CashierAgent)cashier).startThread();
        ((MarketAgent)market0).startThread();
        ((MarketAgent)market1).startThread();
        ((MarketAgent)market2).startThread();
        
        ((CookAgent)cook).addMarket(market0);
        ((CookAgent)cook).addMarket(market1);
        ((CookAgent)cook).addMarket(market2);
        ((CookAgent)cook).setStand(stand);
        ((MarketAgent)market0).setCook(cook);
        ((MarketAgent)market1).setCook(cook);
        ((MarketAgent)market2).setCook(cook);
        ((MarketAgent)market0).setCashier(cashier);
        ((MarketAgent)market1).setCashier(cashier);
        ((MarketAgent)market2).setCashier(cashier);

        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(1, 2, 10, 10));

        group.add(customerPanel);

        initRestLabel();
        add(restLabel);
        add(group);
        
        pauseResumeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (isPaused == false) {
					// Pause all the agents.
					pauseResumeButton.setText("Resume");
					isPaused = true;
					((CookAgent)cook).pause();
					ArrayList<Waiter> list = (ArrayList<Waiter>) host.getWaiters();
					for (Waiter w : list) {
						((WaiterNormalAgent)w).pause();
					}
					for (CustomerAgent c : customers) {
						c.pause();
					}
					((HostAgent)host).pause();
					((CashierAgent)cashier).pause();
					((MarketAgent)market0).pause();
					((MarketAgent)market1).pause();
					((MarketAgent)market2).pause();
				}
				else {
					// Restart all the agents.
					pauseResumeButton.setText("Pause");
					isPaused = false;
					((CookAgent)cook).restart();
					ArrayList<Waiter> list = (ArrayList<Waiter>) host.getWaiters();
					for (Waiter w : list) {
						((WaiterNormalAgent)w).restart();
					}
					for (CustomerAgent c : customers) {
						c.restart();
					}
					((HostAgent)host).restart();
					((CashierAgent)cashier).restart();
					((MarketAgent)market0).restart();
					((MarketAgent)market1).restart();
					((MarketAgent)market2).restart();
				}
				
			}});
        JPanel temp = new JPanel();
        temp.setLayout(new BorderLayout());
        temp.add(pauseResumeButton, BorderLayout.NORTH);
        group.add(temp);
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("        "), BorderLayout.EAST);
        restLabel.add(new JLabel("        "), BorderLayout.WEST);
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
                CustomerAgent temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
        else if (type.equals("Waiter")) {
        	ArrayList<Waiter> list = (ArrayList<Waiter>) host.getWaiters();
        	for (Waiter w : list) {
        		if (((WaiterNormalAgent)w).getName() == name) {
        			gui.updateInfoPanel(w);
        		}
        	}
        }
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name, double cash) {

    	// If the person to be added is a customer.
    	if (type.equals("Customers")) {
    		CustomerAgent c = new CustomerAgent(name, cash);	
    		CustomerGui g = new CustomerGui(c, gui);

    		gui.animationPanel.addGui(g);// dw
    		((CustomerAgent)c).setHost(host);
    		((CustomerAgent)c).setCashier(cashier);
    		if (customerPanel.isNewCustHungry()) {
    			g.setHungry();
    		}
    		c.setGui(g);
    		customers.add(c);
    		c.startThread();
    	}
    	
    	// If the person to be added is a waiter.
    	if (type.equals("Waiter")) {
    		Waiter w = new WaiterNormalAgent(host, name);
    		WaiterGui g = new WaiterGui((WaiterNormalAgent)w, gui, waiterHomePos);
    		waiterHomePos.width += 30;
    		
    		gui.animationPanel.addGui(g);
    		((WaiterNormalAgent)w).setGui(g);
    		((WaiterNormalAgent)w).setCook(cook);
    		((WaiterNormalAgent)w).setCashier(cashier);
    		((HostAgent)host).addWaiter(w);
    		((WaiterNormalAgent)w).startThread();
    		System.out.println("New " + w + " has been added.");
    		System.out.println(((HostAgent)host).getWaiters());
    	}
    }
    
    public class RevolvingStand {
    	private List<Order> orders = new ArrayList<Order>();
        synchronized public void addOrder(Waiter waiter, String food, int table) {
                orders.add(((CookAgent)cook).new Order(waiter, food, table));
        }
        synchronized public Order removeOrder() {
                Order temp;
                temp = orders.get(0);
                orders.remove(0);
                return temp;
        }
        public int size() {
        	return orders.size();
        }
    }
}