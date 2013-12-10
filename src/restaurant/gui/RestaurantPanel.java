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
public class RestaurantPanel extends JPanel implements ActionListener{

    //Host, cook, waiters and customers
	public CookWaiterMonitor theMonitor = new CookWaiterMonitor();

    private HostRole host;
    //private HostGui hostGui = new HostGui(host);
	private CookRole cook;
    private Vector<RestaurantCustomerRole> customers = new Vector<RestaurantCustomerRole>();
    private Vector<BaseWaiterRole> waiters = new Vector<BaseWaiterRole>();

	//private CashierRole cashier = new CashierRole("Cashier", this);

    private RestaurantGui gui; //reference to main gui
    
    private JButton pauseButton = new JButton ("Pause/Resume");
	private JPanel imagePanel = new JPanel();
	Boolean agentPaused = false;
	

	public class Order {
       public int table;
       public String food;
       public  BaseWaiterRole waiter;
        Order (String f,BaseWaiterRole w, int t) {
                table  = t;
                food = f;
                waiter = w;
        }
    }

    public class CookWaiterMonitor extends Object {
        private List<Order> orders = new ArrayList<Order>();
        synchronized public void addOrder (int table, String food, BaseWaiterRole waiter) {
                orders.add (new Order (food, waiter,table));
        }
        synchronized public Order removeOrder () {
                Order temp;
                temp = orders.get(0);
                orders.remove(0);
                return temp;
       }
        public int getOrderSize() {
    		return orders.size();
    	}
    }

    
    public RestaurantPanel(RestaurantGui gui, HostRole h) {
    	host = h;
        this.gui = gui;
    }
    
    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
    }



	
    public void addPerson(String type, String name) {

    	if (type.equals("Customers")) {
    		RestaurantCustomerRole c = new RestaurantCustomerRole(gui);	
    		CustomerGui g = new CustomerGui(c);

    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setGui(g);
    		customers.add(c);
    		//c.startThread();
    	}
    	
    	if (type.equals("Waiters")) {
    		//if (name.equalsIgnoreCase("special")){
    			BaseWaiterRole w = new NormalWaiterRole(gui);
    			if (name.equalsIgnoreCase("special")) {
        			w = new SpecialWaiterRole(theMonitor,gui);
    			}
    			WaiterGui g = new WaiterGui(w);
        		g.setHomePosition(waiters.size());
        		
                gui.animationPanel.addGui(g);
        		host.addWaiter(w);
        		waiters.add(w);
        		w.setHost(host);
        		w.setGui(g);
        		w.setCook(cook);
        		//w.setCashier(cashier);
        		
                //w.startThread();
    		//}
    	
    	}
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



	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
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