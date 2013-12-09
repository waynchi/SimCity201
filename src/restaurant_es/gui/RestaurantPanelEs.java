package restaurant_es.gui;

import restaurant_es.BaseWaiterRoleEs;
import restaurant_es.CookRoleEs;
import restaurant_es.HostRoleEs;
import restaurant_es.NormalWaiterRoleEs;
import restaurant_es.RestaurantCustomerRoleEs;
import restaurant_es.SpecialWaiterRoleEs;
import restaurant_es.CookRoleEs.MyOrder;

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
public class RestaurantPanelEs extends JPanel implements ActionListener{

    //Host, cook, waiters and customers
	public CookWaiterMonitorEs theMonitor = new CookWaiterMonitorEs();

    private HostRoleEs host;
    //private HostGui hostGui = new HostGui(host);
	private CookRoleEs cook;
    private Vector<RestaurantCustomerRoleEs> customers = new Vector<RestaurantCustomerRoleEs>();
    private Vector<BaseWaiterRoleEs> waiters = new Vector<BaseWaiterRoleEs>();

	//private CashierRole cashier = new CashierRole("Cashier", this);

    private RestaurantGuiEs gui; //reference to main gui
    
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

    public class CookWaiterMonitorEs extends Object {
        private List<MyOrder> orders = new ArrayList<MyOrder>();
        synchronized public void addOrder (int table, String food, BaseWaiterRoleEs waiter) {
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

    
    public RestaurantPanelEs(RestaurantGuiEs gui, HostRoleEs h) {
    	host = h;
        this.gui = gui;
    }
    
    public RestaurantPanelEs(RestaurantGuiEs gui) {
        this.gui = gui;
    }



	
    public void addPerson(String type, String name) {

    	if (type.equals("Customers")) {
    		RestaurantCustomerRoleEs c = new RestaurantCustomerRoleEs(gui);	
    		CustomerGui g = new CustomerGui(c);

    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setGui(g);
    		customers.add(c);
    		//c.startThread();
    	}
    	
    	if (type.equals("Waiters")) {
    		//if (name.equalsIgnoreCase("special")){
    			BaseWaiterRoleEs w = new NormalWaiterRoleEs(gui);
    			if (name.equalsIgnoreCase("special")) {
        			w = new SpecialWaiterRoleEs(theMonitor,gui);
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
    
   

    public Vector<BaseWaiterRoleEs> getWaiters () {
    	return waiters;
    }
    
    public void setCook (CookRoleEs c) {
    	cook = c;
    }
    
    public void setHost (HostRoleEs h) {
    	host = h;
    }
    
    public CookRoleEs getCook() {
    	return cook;
    }

    public HostRoleEs getHost() {
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