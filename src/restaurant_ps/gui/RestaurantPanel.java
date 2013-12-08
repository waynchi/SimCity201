package restaurant_ps.gui;

import restaurant_ps.gui.CashierGui;
import restaurant_ps.interfaces.Customer;
//import restaurant.interfaces.Market;
import market.*;
import restaurant_ps.interfaces.Waiter;
import restaurant_ps.CashierAgent;
import restaurant_ps.CookAgent;
import restaurant_ps.CustomerAgent;
import restaurant_ps.Food;
import restaurant_ps.HostAgent;
//import restaurant.MarketAgent;
import restaurant_ps.WaiterAgent;

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
@SuppressWarnings("serial")
public class RestaurantPanel extends JPanel implements ActionListener {

    //Host, cook, waiters and customers
	public CookAgent cook;
	
	boolean paused;
	
    public HostAgent host;
    List<WaiterAgent> restaurantWaiters = new ArrayList<WaiterAgent>();
    
    private HostGui hostGui;
    private CashierGui cashierGui;
    private CookGui cookGui;
    private CashierAgent cashier;
    public List<Food> inventory = new ArrayList<Food>();
    private int waiterHomeX = 370;
    private int waiterHomeY = 25;
    private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();

    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private ListPanel waiterPanel = new ListPanel(this,"Waiters");
    private JPanel group = new JPanel();
    private JPanel customPanel = new JPanel();
    private JButton pauseButton = new JButton("Pause");
    private JButton addWaiterButton = new JButton("Add Waiter");
    private RestaurantGui gui; //reference to main gui

	
    
    public RestaurantPanel(RestaurantGui gui) {
    	this.gui = gui;
        inventory.add(new Food("Pizza",30,3,5.00));
		inventory.add(new Food("Burger",15,3,8.00));
		inventory.add(new Food("Salad",5,3,3.00));
		host = new HostAgent("Sarah");
		hostGui = new HostGui(host);
		cook = new CookAgent("Chef Bob",inventory);
		cookGui = new CookGui(cook,gui);
		cookGui.setPresent(true);
		cook.setGui(cookGui);
		cashier = new CashierAgent("Cashier", 100); //initialize restaurant with $100
        cashierGui = new CashierGui(cashier,gui);
		//market = new MarketAgent("Market",inventory);
//		markets.add(new MarketAgent("Market 1",inventory));
//		markets.add(new MarketAgent("Market 2",inventory));
//		markets.add(new MarketAgent("Market 3", inventory));
//		for(MarketAgent m : markets)
//		{
//			cook.addMarket(m);
//			m.setCook(cook);
//			m.setCashier(cashier);
//		}
		host.setGui(hostGui);
       // restaurantWaiters.add(new WaiterAgent("Waiter Sean",cook,host));
        paused = false;
        
        
        
        
       // showWaiters();
        gui.getAnimationPanel().addHostGui(hostGui);
        gui.getAnimationPanel().addCashierGui(cashierGui);
        gui.getAnimationPanel().addCookGui(cookGui);
        
//        host.startThread();
//        cook.startThread();
//        cashier.startThread();
//        for(MarketAgent m : markets)
//		{
//			m.startThread();
//		}
        
//        setLayout(new GridLayout(1, 2, -15, 0));
//        group.setLayout(new GridLayout(1, 2, 0, 0));
        
        setLayout(new BorderLayout());
        group.setLayout(new BorderLayout());
        
        pauseButton.addActionListener(this);
        addWaiterButton.addActionListener(this);
        //customerPanel.setBounds(10, 10, 50, 50);
        group.add(customerPanel,BorderLayout.EAST);
        group.add(pauseButton,BorderLayout.SOUTH);
        //group.add(addWaiterButton,BorderLayout.SOUTH);
        group.add(waiterPanel,BorderLayout.WEST);
        initRestLabel();
        add(restLabel,BorderLayout.WEST);
        add(group);
        
       
        
        initCustomPanel();
        //add(customPanel,BorderLayout.PAGE_END);
    }

    public void showWaiters() {
		// TODO Auto-generated method stub
    	for(WaiterAgent w : restaurantWaiters)
        {
    		if(w.getGui() == null)
    		{
        	host.addWaiter(w);
        	host.newWaiter();
        	WaiterGui wg = new WaiterGui(w, gui, waiterHomeX, waiterHomeY);
        	waiterHomeY += 40;
    		gui.getAnimationPanel().addWaiterGui(wg);// dw
    		w.setGui(wg);
    		w.setCashier(cashier);
    		wg.setPresent(true);
    		w.startThread();
    		}
        }
	}

//	@Override
//	public void actionPerformed(ActionEvent event) {
//		// TODO Auto-generated method stub
//    	if(event.getSource() == pauseButton)
//    	{
//    		if(!paused)
//    		{
//    			paused = true;
//    			pauseAgents();
//			
//    		}
//    		else if(paused)
//    		{
//    			paused = false;
//    			restartAgents();
//			
//    		}
//    	}
//    	else if(event.getSource() == addWaiterButton)
//    	{
//    		String message = "Please enter the waiter's name:";  
//        	//Object[] options = {message, checkbox};
//        	addWaiter(JOptionPane.showInputDialog(message));
//    	}
	//}
    
//    public void addWaiter(String waiterName) {
//		// TODO Auto-generated method stub
//    	if(waiterName == null)
//   			return;
//    	
//    	
//    	restaurantWaiters.add(new WaiterAgent(waiterName,cook,host,inventory));
//    	showWaiters();
//	}

//	protected void restartAgents() {
//		// TODO Auto-generated method stub
//    	pauseButton.setText("Pause");
//		//System.out.println("Restart Agent");
//		host.restartAgent();
//		host.restartWaiters();
//		cook.restartAgent();
//		for(CustomerAgent c : customers)
//		{
//			c.restartAgent();
//		}
//		for(MarketAgent m : markets)
//		{
//			m.restartAgent();
//		}
//	}
    

//	protected void pauseAgents() {
//		// TODO Auto-generated method stub
//		pauseButton.setText("Restart");
//		//System.out.println("pause agent");
//		host.pauseWaitersAndTheirCustomers();
//		host.pauseAgent();
//		cook.pauseAgent();
//		for(CustomerAgent c : customers)
//		{
//			c.pauseAgent();
//		}
//		for(MarketAgent m : markets)
//		{
//			m.pauseAgent();
//		}
//	}

	/**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initCustomPanel() {
    	JLabel label = new JLabel();
    	label.setText("Name: Peppfy Sisay\nMajor: Computer Science\nRestaurant Money: " + cashier.getRestaurantMoney());
    	customPanel.add(label);
    	
    }
    
    public double getRestaurantMoney(){
    	return cashier.getRestaurantMoney();
    }
    
    private void initRestLabel() {
        JLabel label = new JLabel();
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>Host:</td><td>" + host.getName()  + "</tr></td></tr></table><h3><u> Menu</u></h3><table><tr><td>Burger</td><td>$8.00</td></tr><tr><td>Salad</td><td>$3.00</td></tr><tr><td>Pizza</td><td>$5.00</td></tr></table><br></html>");

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
    public void showInfo(String type, String name) {

        if (type.equals("Customers")) {

            for (int i = 0; i < customers.size(); i++) {
                Customer temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
        if(type.equals("Waiters")) {
        	for (int i = 0 ; i < restaurantWaiters.size(); i++) {
        		Waiter temp = restaurantWaiters.get(i);
        		if(temp.getName() == name)
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
    public static boolean isNumeric(String str)  
    {  
      try  
      {  
        @SuppressWarnings("unused")
		double d = Double.parseDouble(str);  
      }  
      catch(NumberFormatException nfe)  
      {  
        return false;  
      }  
      return true;  
    }
    public void addPerson(String type, String name, boolean isHungry) {

    	if (type.equals("Customers")) {
    		CustomerAgent c;
    		if(name.matches(".*\\d.*")){
    			String m = name.replaceAll("\\D+","");
    			c = new CustomerAgent(name,Integer.parseInt(m));
    		} 
    		else
    		{
    			c = new CustomerAgent(name,50);	//50 is default starting money value
    		}
    		CustomerGui g = new CustomerGui(c, gui);
    		
    		gui.getAnimationPanel().addCustomerGui(g);// dw
    		c.setHost(host);
    		c.setGui(g);
    		c.setCashier(cashier);
    		customers.add(c);
    		//c.startThread();
    		if(isHungry)
    		{
    			g.setHungry();
    		}
    	}
    }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	

}
