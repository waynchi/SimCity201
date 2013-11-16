package restaurant.gui;

import restaurant.CashierRole;
import restaurant.CookRole;
import restaurant.CookRole.MyOrder;
import restaurant.RestaurantCustomerRole;
import restaurant.HostRole;
import restaurant.MarketRole;
import restaurant.BaseWaiterRole;
import restaurant.BaseWaiterRole.MyCustomer;
import restaurant.NormalWaiterRole;
import restaurant.SpecialWaiterRole;

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
public class RestaurantPanel extends JPanel implements ActionListener{

    //Host, cook, waiters and customers
	public CookWaiterMonitor theMonitor = new CookWaiterMonitor();

    private HostRole host = new HostRole("Sarah");
    private HostGui hostGui = new HostGui(host);
	private CookRole cook = new CookRole("Cook", theMonitor);
	private CookGui cookGui = new CookGui(cook);
	private CashierRole cashier = new CashierRole("Cashier");
	
	private MarketRole market1 = new MarketRole("Market 1");
	private MarketRole market2 = new MarketRole("Market 2");
	private MarketRole market3 = new MarketRole("Market 3");
	
	private Double min_working_capital = 500.00;
	private Double working_capital;
	private Double bank_balance;
	

    private Vector<RestaurantCustomerRole> customers = new Vector<RestaurantCustomerRole>();
    private Vector<BaseWaiterRole> waiters = new Vector<BaseWaiterRole>();

    private JPanel restLabel = new JPanel();

    private RestaurantGui gui; //reference to main gui
    
    private JButton pauseButton = new JButton ("Pause/Resume");
	private JPanel imagePanel = new JPanel();
	Boolean agentPaused = false;

	/*public class Order {
        int table;
        String food;
        WaiterAgent waiter;
        Order (int t, String f,WaiterAgent w) {
                table  = t;
                food = f;
                waiter = w;
        }
    }*/

    public class CookWaiterMonitor extends Object {
        private List<MyOrder> orders = new ArrayList<MyOrder>();
        synchronized public void addOrder (int table, String food, BaseWaiterRole waiter) {
                orders.add (cook.new MyOrder (food, waiter,table));
        }
        synchronized public MyOrder removeOrder () {
                MyOrder temp;
                temp = orders.get(0);
                orders.remove(0);
                return temp;
       }
        public int getOrderSize() {
    		return orders.size();
    	}
    }

    
    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
		gui.animationPanel.addGui(cookGui);
        host.setGui(hostGui);
        cook.setGui(cookGui);
        
        cook.addMarket(market1);
        cook.addMarket(market2);
        cook.addMarket(market3);
        cook.setCashier(cashier);
        
        //market1.setCook(cook);
        //market2.setCook(cook);
        //market3.setCook(cook);
                
        host.startThread();
        cook.startThread();
        market1.startThread();
        market2.startThread();
        market3.startThread();
        cashier.startThread();
        
        setLayout(new GridLayout(1, 2, 20, 20));
        

        initRestLabel();
        initImagePanel();
        add(restLabel);
        add(imagePanel);
        //add(customerPanel);
    }
    
    

    private void initImagePanel() {
    	 
    	ImageIcon image = new ImageIcon(getClass().getResource("saber.jpg"));
        JLabel imageLabel = new JLabel(image);
        imageLabel.setText("Welcome to Yinyi's!");
        imageLabel.setHorizontalTextPosition(JLabel.CENTER);
        imageLabel.setVerticalTextPosition(JLabel.TOP);
        imageLabel.setFont(new Font("Courier New", Font.BOLD, 20));

        imagePanel.add(imageLabel);
        imagePanel.add(pauseButton);
       // imagePanel.add(addWaiterButton);
        
        pauseButton.addActionListener(this);
        //addWaiterButton.addActionListener(this);
       	
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
        restLabel.add(new JLabel("               "), BorderLayout.EAST);
        restLabel.add(new JLabel("               "), BorderLayout.WEST);
    }

    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
    public void showInfo(String type, String name, Boolean hungryCheck) {

        if (type.equals("Customers")) {

            for (int i = 0; i < customers.size(); i++) {
                RestaurantCustomerRole temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp, hungryCheck);
            }
        }
        
        if (type.equals("Waiters")) {
        	for (BaseWaiterRole temp: waiters) {
        		if (temp.getName() == name)
        			gui.updateInfoPanel(temp, false);
        	}
        }
    }

	public void actionPerformed(ActionEvent e) {

	    if (e.getSource() == pauseButton){
	    	System.out.println("pause button pressed");
	    	if (agentPaused == false) {
	    		pauseAgents();
	    		agentPaused = true;
	    	}
	    	else {
	    		restartAgents();
	    		agentPaused = false;
	    	}
	    }
	  
	}
    
    // pause all the agents when pause button is clicked
    public void pauseAgents(){
    	host.pause();
    	cook.pause();
    	cashier.pause();
    	market1.pause();
    	market2.pause();
    	market3.pause();
    	
    	// since all waiters and customers are created in the restaurant panel, we have a list to keep track
    	// of them
    	// should be able to pause all agents now.
    	for (BaseWaiterRole waiter : waiters) {
    		waiter.pause();
    	}
    	for (RestaurantCustomerRole cust : customers){
    		//cust.pause();
    	}
    }
    
    public void restartAgents() {
		host.restart();
    	cook.restart();
    	cashier.restart();
    	market1.restart();
    	market2.restart();
    	market3.restart();
    	for (BaseWaiterRole waiter : waiters) {
    		waiter.restart();
    	}
    	for (RestaurantCustomerRole cust : customers){
    		//cust.restart();
    	}
	}
    
    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name) {

    	if (type.equals("Customers")) {
    		RestaurantCustomerRole c = new RestaurantCustomerRole(name);	
    		CustomerGui g = new CustomerGui(c, gui);

    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setGui(g);
    		customers.add(c);
    		//c.startThread();
    	}
    	
    	if (type.equals("Waiters")) {
    		//if (name.equalsIgnoreCase("special")){
    			BaseWaiterRole w = new NormalWaiterRole(name);
    			if (name.equalsIgnoreCase("special")) {
        			w = new SpecialWaiterRole(name, theMonitor);
    			}
    			WaiterGui g = new WaiterGui(w);
        		g.setHomePosition(waiters.size());
        		
                gui.animationPanel.addGui(g);
        		host.addWaiter(w);
        		waiters.add(w);
        		w.setHost(host);
        		w.setGui(g);
        		w.setCook(cook);
        		w.setCashier(cashier);
        		
                w.startThread();
    		//}
    	
    	}
    }
    
    public void setWorkingCapital (double amount) {
    	working_capital = amount;
    }
    
    public double getWorkingCapital () {
    	return working_capital;
    }
    
    public void setBankBalance (double amount) {
    	bank_balance = amount;
    }
    
    public double getBankBalance () {
    	return bank_balance;
    }

    public Vector<BaseWaiterRole> getWaiters () {
    	return waiters;
    }
    
    public CookRole getCook() {
    	return cook;
    }

    

/*	public void addWaiter(String name) {
		WaiterAgent newWaiter = new WaiterAgent(name);
		WaiterGui newWaiterGui = new WaiterGui(newWaiter);
		newWaiter.setGui(newWaiterGui);
		host.addWaiter(newWaiter);
		newWaiter.setHost(host);
		newWaiter.setCook(cook);
        gui.animationPanel.addGui(newWaiterGui);
        newWaiter.startThread();
	}
*/
}
