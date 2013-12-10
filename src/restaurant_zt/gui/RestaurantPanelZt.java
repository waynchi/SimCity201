package restaurant_zt.gui;

import restaurant_zt.BaseWaiterRole;
import restaurant_zt.CookRoleZt;
import restaurant_zt.HostRoleZt;
import restaurant_zt.NormalWaiterRoleZt;
import restaurant_zt.RestaurantCustomerRoleZt;
import restaurant_zt.SpecialWaiterRoleZt;
import restaurant_zt.CookRoleZt.MyOrder;

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
public class RestaurantPanelZt extends JPanel implements ActionListener{

    //Host, cook, waiters and customers
	public CookWaiterMonitorZt theMonitor = new CookWaiterMonitorZt();

    private HostRoleZt host;
    //private HostGui hostGui = new HostGui(host);
	private CookRoleZt cook;
    private Vector<RestaurantCustomerRoleZt> customers = new Vector<RestaurantCustomerRoleZt>();
    private Vector<BaseWaiterRole> waiters = new Vector<BaseWaiterRole>();

	//private CashierRole cashier = new CashierRole("Cashier", this);

    private RestaurantGuiZt gui; //reference to main gui
    
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

    public class CookWaiterMonitorZt extends Object {
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

    
    public RestaurantPanelZt(RestaurantGuiZt gui, HostRoleZt h) {
    	host = h;
        this.gui = gui;
    }
    
    public RestaurantPanelZt(RestaurantGuiZt gui) {
        this.gui = gui;
    }



	
    public void addPerson(String type, String name) {

    	if (type.equals("Customers")) {
    		RestaurantCustomerRoleZt c = new RestaurantCustomerRoleZt(gui);	
    		CustomerGuiZt g = new CustomerGuiZt(c);

    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setGui(g);
    		customers.add(c);
    		//c.startThread();
    	}
    	
    	if (type.equals("Waiters")) {
    		//if (name.equalsIgnoreCase("special")){
    			BaseWaiterRole w = new NormalWaiterRoleZt(gui);
    			if (name.equalsIgnoreCase("special")) {
        			w = new SpecialWaiterRoleZt(theMonitor,gui);
    			}
    			WaiterGuiZt g = new WaiterGuiZt(w);
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
    
    public void setCook (CookRoleZt c) {
    	cook = c;
    }
    
    public void setHost (HostRoleZt h) {
    	host = h;
    }
    
    public CookRoleZt getCook() {
    	return cook;
    }

    public HostRoleZt getHost() {
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