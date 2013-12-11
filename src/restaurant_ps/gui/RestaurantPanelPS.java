package restaurant_ps.gui;

import restaurant_ps.BaseWaiterRole;
import restaurant_ps.CookRolePS;
import restaurant_ps.HostRolePS;
import restaurant_ps.NormalWaiterRolePS;
import restaurant_ps.RestaurantCustomerRolePS;
import restaurant_ps.SpecialWaiterRolePS;
import restaurant_ps.CookRolePS.MyOrder;

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
public class RestaurantPanelPS extends JPanel implements ActionListener{

    //Host, cook, waiters and customers
	public CookWaiterMonitorPS theMonitor = new CookWaiterMonitorPS();

    private HostRolePS host;
    //private HostGui hostGui = new HostGui(host);
	private CookRolePS cook;
    private Vector<RestaurantCustomerRolePS> customers = new Vector<RestaurantCustomerRolePS>();
    private Vector<BaseWaiterRole> waiters = new Vector<BaseWaiterRole>();

	//private CashierRole cashier = new CashierRole("Cashier", this);

    private RestaurantGuiPS gui; //reference to main gui
    
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

    public class CookWaiterMonitorPS extends Object {
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

    
    public RestaurantPanelPS(RestaurantGuiPS gui, HostRolePS h) {
    	host = h;
        this.gui = gui;
    }
    
    public RestaurantPanelPS(RestaurantGuiPS gui) {
        this.gui = gui;
    }



	
    public void addPerson(String type, String name) {

    	if (type.equals("Customers")) {
    		RestaurantCustomerRolePS c = new RestaurantCustomerRolePS(gui);	
    		CustomerGuiPS g = new CustomerGuiPS(c);

    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setGui(g);
    		customers.add(c);
    		//c.startThread();
    	}
    	
    	if (type.equals("Waiters")) {
    		//if (name.equalsIgnoreCase("special")){
    			BaseWaiterRole w = new NormalWaiterRolePS(gui);
    			if (name.equalsIgnoreCase("special")) {
        			w = new SpecialWaiterRolePS(theMonitor,gui);
    			}
    			WaiterGuiPS g = new WaiterGuiPS(w);
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
    
    public void setCook (CookRolePS c) {
    	cook = c;
    }
    
    public void setHost (HostRolePS h) {
    	host = h;
    }
    
    public CookRolePS getCook() {
    	return cook;
    }

    public HostRolePS getHost() {
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