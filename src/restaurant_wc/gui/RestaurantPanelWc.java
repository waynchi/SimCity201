package restaurant_wc.gui;

import restaurant_wc.BaseWaiterRole;
import restaurant_wc.CookRoleWc;
import restaurant_wc.HostRoleWc;
import restaurant_wc.NormalWaiterRoleWc;
import restaurant_wc.RestaurantCustomerRoleWc;
import restaurant_wc.SpecialWaiterRoleWc;
import restaurant_wc.CookRoleWc.MyOrder;

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
public class RestaurantPanelWc extends JPanel implements ActionListener{

    //Host, cook, waiters and customers
	public CookWaiterMonitorWc theMonitor = new CookWaiterMonitorWc();

    private HostRoleWc host;
    //private HostGui hostGui = new HostGui(host);
	private CookRoleWc cook;
    private Vector<RestaurantCustomerRoleWc> customers = new Vector<RestaurantCustomerRoleWc>();
    private Vector<BaseWaiterRole> waiters = new Vector<BaseWaiterRole>();

	//private CashierRoleWc cashier = new CashierRoleWc("Cashier", this);

    private RestaurantGuiWc gui; //reference to main gui
    
    private JButton pauseButton = new JButton ("Pause/Resume");
	private JPanel imagePanel = new JPanel();
	Boolean agentPaused = false;

	public class Order {
        public int table;
        public String food;
        public BaseWaiterRole waiter;
        Order (int t, String f,BaseWaiterRole w) {
                table  = t;
                food = f;
                waiter = w;
        }
    }

    public class CookWaiterMonitorWc extends Object {
        private List<Order> orders = new ArrayList<Order>();
        synchronized public void addOrder (int table, String food, BaseWaiterRole waiter) {
                orders.add (new Order (table, food, waiter));
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

    
    public RestaurantPanelWc(RestaurantGuiWc gui, HostRoleWc h) {
    	host = h;
        this.gui = gui;
    }
    
    public RestaurantPanelWc(RestaurantGuiWc gui) {
        this.gui = gui;
    }



	
    public void addPerson(String type, String name) {

    	if (type.equals("Customers")) {
    		RestaurantCustomerRoleWc c = new RestaurantCustomerRoleWc(gui);	
    		CustomerGui g = new CustomerGui(c);

    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setGui(g);
    		customers.add(c);
    		//c.startThread();
    	}
    	
    	if (type.equals("Waiters")) {
    		//if (name.equalsIgnoreCase("special")){
    			BaseWaiterRole w = new NormalWaiterRoleWc(gui);
    			if (name.equalsIgnoreCase("special")) {
        			w = new SpecialWaiterRoleWc(theMonitor,gui);
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
    
    public void setCook (CookRoleWc c) {
    	cook = c;
    }
    
    public void setHost (HostRoleWc h) {
    	host = h;
    }
    
    public CookRoleWc getCook() {
    	return cook;
    }

    public HostRoleWc getHost() {
    	return host;
    }



	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}


}