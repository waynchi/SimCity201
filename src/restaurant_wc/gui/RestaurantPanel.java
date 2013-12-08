package restaurant_wc.gui;

import restaurant_wc.WcCashierRole;
import restaurant_wc.WcCookRole;
import restaurant_wc.WcCustomerRole;
import restaurant_wc.WcHostAgent;
import restaurant_wc.MarketAgent;
import restaurant_wc.WaiterAgent;
import restaurant_wc.interfaces.Cashier;
import restaurant_wc.interfaces.Waiter;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.List;

/**
 * Panel in frame that contains all the restaurant_wc information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

    //Host, cook, waiters and customers
	private List<WaiterAgent> waiters = new ArrayList<WaiterAgent>();
    private WcHostAgent host = new WcHostAgent("Wayne");
    //private WaiterAgent waiter = new WaiterAgent("Wayne");
    //private WaiterGui waiterGui = new WaiterGui(waiter);
    public WcCookRole cook = new WcCookRole("Gordon Ramsay");
    public CookGui cookGui;
    public WcCashierRole cashier = new WcCashierRole("Cashier Man");
    private int waiterNum = 0;

    private Vector<WcCustomerRole> customers = new Vector<WcCustomerRole>();

    private JPanel restLabel = new JPanel();
    public ListPanel customerPanel = new ListPanel(this, "Customers");
    public ListPanel waiterPanel = new ListPanel(this, "Waiters");
    private JPanel group = new JPanel();

    private RestaurantGui gui; //reference to main gui

    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
       // waiter.setGui(waiterGui);
      //  host.addWaiter(waiter);
       // waiter.setHost(host);
       // waiter.setCook(cook);
        
      //  waiters.add(waiter);
        

        cashier.startThread();
        /*cook.addMarket(new MarketAgent("First", 0));
        cook.Markets.get(0).startThread();
        cook.Markets.get(0).setCook(cook);
        cook.addMarket(new MarketAgent("Second", 1));
        cook.Markets.get(1).startThread();
        cook.Markets.get(1).setCook(cook);
        cook.addMarket(new MarketAgent("Third", 2));
        cook.Markets.get(2).startThread();
        cook.Markets.get(2).setCook(cook);*/
        cook.startThread();
        cookGui = new CookGui(gui);
        gui.animationPanel.addGui(cookGui);
        cook.setGui(cookGui);
        cookGui.setCook(cook);

        
       // waiter.startThread();

      //  gui.animationPanel.addGui(waiterGui);
        host.startThread();

        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(1, 2, 10, 10));

        group.add(customerPanel);
        group.add(waiterPanel);

        initRestLabel();
        add(restLabel);
        add(group);
    }

    /**
     * Sets up the restaurant_wc label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>Host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

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
                WcCustomerRole temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
        
        if(type.equals("Waiters")) {
        	for (int j = 0; j < waiters.size(); j++) {
        		Waiter tempw = waiters.get(j);
        		if(tempw.getName() == name)
        			gui.updateInfoPanel(tempw);
        		
        	}
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
    		WcCustomerRole c = new WcCustomerRole(name);	
    		CustomerGui g = new CustomerGui(c, gui);

    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setCashier(cashier);
    		c.setGui(g);
    		customers.add(c);
    		c.startThread();
    	}
    	if (type.equals("Waiters")) {
    		WaiterAgent w = new WaiterAgent(name);
    		WaiterGui h = new WaiterGui(w, gui, waiterNum);
    		h.setCookGui(cookGui);
    		waiterNum++;
    		gui.animationPanel.addGui(h);
    		w.setCook(cook);
    		w.setCashier(cashier);
    		w.setGui(h);
    		w.setHost(host);
    		h.mainAnimation = gui.animationPanel;
    		host.addWaiter(w);
    		waiters.add(w);
    		w.startThread();
    	}
    }
    
    public void pauseAll(){
    	host.pause();
    	for(WaiterAgent w: waiters) {
    		w.pause();
    	}
    	//waiter.paused = true;
    	cook.pause();
    	cashier.pause();
    	for(WcCustomerRole c: customers){
    		c.pause();
    	}
    }

	public void restartAll() {
		host.restart();
	 	for(WaiterAgent w: waiters) {
    		w.restart();
    	}
    	//waiter.paused = false;
    	cook.restart();
    	cashier.restart();
    	for(WcCustomerRole c: customers){
    		c.restart();
    	}
//		host.pausedSem.release();
//	 	for(WaiterAgent w1: waiters) {
//    		w1.pausedSem.release();
//    	}
//		//waiter.pausedSem.release();
//		cook.pausedSem.release();
//		cashier.pausedSem.release();
//		for(WcCustomerRole c2: customers){
//			c2.pausedSem.release();
//		}
	}

}
