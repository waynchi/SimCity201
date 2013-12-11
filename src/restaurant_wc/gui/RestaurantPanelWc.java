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
    
	Boolean paused = false;

    public class CookWaiterMonitorWc extends Object {
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