package restaurant_wc.gui;


import restaurant_wc.HostAgent;
import restaurant_wc.Table;
import restaurant_wc.WaiterAgent;
import restaurant_wc.interfaces.Customer;

import java.awt.*;
import java.util.concurrent.Semaphore;

import javax.swing.ImageIcon;

public class WaiterGui implements Gui {

    private WaiterAgent agent = null;
    
    RestaurantGui gui;

    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = 50, yDestination = 100;//default start position
    
    private int tableNum;
    private boolean RecievingOrder = false;
    private Semaphore nearCustomer = new Semaphore(0,true);
    private final int xIncrement = 100;
    private final int yIncrement = -50;
    public static final int xTable = 200;
    public static final int yTable = 250;
    private final int xCook = 380;
    private final int yCook = 330;
    private int waiterNum = 0;
    public CookGui cookGui;
    public AnimationPanel mainAnimation;
    private boolean OrderAppears = false;
    private boolean bringCustomer = false;
    private ImageIcon Food = new ImageIcon();
	private ImageIcon steak = new ImageIcon(this.getClass().getResource("steak.jpg"));
	private ImageIcon chicken = new ImageIcon(this.getClass().getResource("chicken.jpg"));
	private ImageIcon salad = new ImageIcon(this.getClass().getResource("salad.jpg"));
	private ImageIcon pizza = new ImageIcon(this.getClass().getResource("pizza.jpg"));

    public WaiterGui(WaiterAgent waiter, RestaurantGui gui, int waiterNum) {
        this.agent = waiter;
        this.gui = gui;
        this.waiterNum = waiterNum;
        xDestination = 50;
        yDestination = 100 + 30* waiterNum;
    }

	public RestaurantGui getGui(){
		return gui;
	}
	
	public void setCookGui(CookGui cookGui){
		this.cookGui = cookGui;
	}
	
    public void updatePosition() {
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTable + xIncrement*(tableNum-1)+ 20) & (yDestination == yTable + yIncrement*(tableNum-1) - 20)) {
        	if(RecievingOrder)
        	{
        		agent.msgReadyAtTable();
        		RecievingOrder = false;
        	}
        	else
        	{
        		agent.msgAtTable();	
        	}
           
        }
        if(xPos == 50 && yPos <= (100 + waiterNum*30))
        {
        	if(agent.fetching)
        	{
        		agent.msgAtDoor();
        		agent.fetching = false;
        	}
        }
        if(xPos == xDestination && yPos == yDestination && bringCustomer)
        {
        	bringCustomer = false;
        	nearCustomer.release();
        }
        //System.out.println((xPos) + ", " + yPos);
        if(xPos == xDestination && yPos == yDestination & (xDestination == xCook) && (yDestination == yCook)) {
        	//mainAnimation.changeImage();
        	agent.msgAtCook();
        	xDestination = 50;
        	yDestination = 100+30*waiterNum;
        	
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
		if(OrderAppears){
			g.drawImage(Food.getImage(), xPos+20, yPos, null);
		}
    }

    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable(Customer customer, int tableNumber) {
    	//TODO
    	//not sure if this is legal
    	if(customer.getGui().waiting)
    	{
    		xDestination = customer.getGui().customerNum*30 + 60;
        	yDestination = 60;
    	}
    	else{
    	xDestination = customer.getGui().getPosX()+20;
    	yDestination = customer.getGui().getPosY()+20;
    	}
    	bringCustomer = true;
    	try {
			nearCustomer.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        xDestination = xTable + xIncrement*(tableNumber-1)  + 20;
        yDestination = yTable + yIncrement*(tableNumber-1) - 20;
        tableNum = tableNumber;
        customer.getGui().setTableNumber(tableNumber);
        System.out.println("Setting table number");
        customer.getFollowing().release();
        agent.getTableSet().release();
    }
    
    public void DoBringFoodToTable(Customer customer, int tableNumber) {
    	xDestination = xTable + xIncrement*(tableNumber-1)  + 20;
        yDestination = yTable + yIncrement*(tableNumber-1) - 20;
        tableNum = tableNumber;
    }

    public void DoLeaveCustomer() {
        xDestination = 50;
        yDestination = 100 + 30*waiterNum;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

	public void GoToTable(Customer customer, Table table) {
		xDestination = xTable + xIncrement*(table.tableNumber-1)  + 20;
        yDestination = yTable + yIncrement*(table.tableNumber-1) - 20;
        tableNum = table.tableNumber;
        RecievingOrder = true;
		
	}

	public void giveCookOrder() {
		yDestination = yCook;
		xDestination = xCook;
		
	}
	
	public void OrderAppearing(boolean appearing, String choice) {
		if(appearing){
		cookGui.msgTakingPlate(choice);
		}
		if(choice == "Steak") {
			Food.setImage(steak.getImage());
			
		}
		if(choice == "Chicken") {
			Food.setImage(chicken.getImage());
			
		}
		if(choice == "Pizza") {
			Food.setImage(pizza.getImage());
			
		}
		if(choice == "Salad") {
			Food.setImage(salad.getImage());
			
		}
		OrderAppears = appearing;
	}
}
