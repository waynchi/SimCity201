package restaurant_ps.gui;

import restaurant_ps.BaseWaiterRole;
import restaurant_ps.CookRolePS;
import restaurant_ps.HostRolePS;
import restaurant_ps.NormalWaiterRolePS;
import restaurant_ps.RestaurantCustomerRole;
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
	public CookWaiterMonitor theMonitor = new CookWaiterMonitor();

    private HostRolePS host;
    //private HostGui hostGui = new HostGui(host);
	private CookRolePS cook;
    private Vector<RestaurantCustomerRole> customers = new Vector<RestaurantCustomerRole>();
    private Vector<BaseWaiterRole> waiters = new Vector<BaseWaiterRole>();

	//private CashierRole cashier = new CashierRole("Cashier", this);

    private RestaurantGuiPS gui; //reference to main gui
    
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

    
    public RestaurantPanelPS(RestaurantGuiPS gui, HostRolePS h) {
    	host = h;
        this.gui = gui;
    }
    
    public RestaurantPanelPS(RestaurantGuiPS gui) {
        this.gui = gui;
    }



	
    public void addPerson(String type, String name) {

    	if (type.equals("Customers")) {
    		RestaurantCustomerRole c = new RestaurantCustomerRole(gui);	
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