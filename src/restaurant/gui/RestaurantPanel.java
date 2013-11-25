package restaurant.gui;

import restaurant.BaseWaiterRole;
import restaurant.CookRole;
import restaurant.HostRole;
import restaurant.NormalWaiterRole;
import restaurant.RestaurantCustomerRole;
import restaurant.SpecialWaiterRole;
import restaurant.CookRole.MyOrder;

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
public class RestaurantPanel extends JPanel{

    //Host, cook, waiters and customers
	public CookWaiterMonitor theMonitor = new CookWaiterMonitor();

    private HostRole host;
    //private HostGui hostGui = new HostGui(host);
	private CookRole cook;
	//private CookGui cookGui = new CookGui(cook);
	
	//private MarketRole market1 = new MarketRole("Market 1");
	//private MarketRole market2 = new MarketRole("Market 2");
	//private MarketRole market3 = new MarketRole("Market 3");
	
	private Double min_working_capital = 10.00;
	private Double working_capital = 100.00;
	private Double bank_balance = 0.0;
	private int bank_account = 0;
	

    private Vector<RestaurantCustomerRole> customers = new Vector<RestaurantCustomerRole>();
    private Vector<BaseWaiterRole> waiters = new Vector<BaseWaiterRole>();

	//private CashierRole cashier = new CashierRole("Cashier", this);

    
    private JPanel restLabel = new JPanel();

    private RestaurantGui gui; //reference to main gui
    
    private JButton pauseButton = new JButton ("Pause/Resume");
	private JPanel imagePanel = new JPanel();
	Boolean agentPaused = false;


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
        
        setLayout(new GridLayout(1, 2, 20, 20));
        

        initRestLabel();
        initImagePanel();
        add(restLabel);
        add(imagePanel);
        //add(customerPanel);
    }

    public RestaurantPanel() {
		// TODO Auto-generated constructor stub
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
        
        //addWaiterButton.addActionListener(this);
       	
	}

	/**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        restLabel.setLayout(new BorderLayout());
//        label.setText(
//                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("               "), BorderLayout.EAST);
        restLabel.add(new JLabel("               "), BorderLayout.WEST);
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
    
    public void setCook (CookRole c) {
    	cook = c;
    }
    
    public void setHost (HostRole h) {
    	host = h;
    }
    
    public CookRole getCook() {
    	return cook;
    }

    public HostRole getHost() {
    	return host;
    }


	public Double getMinWorkingCapital() {
		return min_working_capital;
	}



	public void setMinWorkingCapital(Double min_working_capital) {
		this.min_working_capital = min_working_capital;
	}



	public int getBankAccount() {
		// TODO Auto-generated method stub
		return bank_account;
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
