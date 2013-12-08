package restaurant_wc.gui;

import restaurant_wc.WcCustomerRole;
import restaurant_wc.WcHostAgent;

import java.awt.*;
import java.util.concurrent.Semaphore;

import javax.swing.ImageIcon;

public class CustomerGui implements Gui{

	private WcCustomerRole agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	//private WcHostAgent host;
	RestaurantGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;

	//private Semaphore WaiterCalled = new Semaphore(0,true);
	public boolean WaiterCalled = false;
	public boolean waiting = false;
	public int customerNum;
    private int tableNumber = -1;
    private final int xIncrement = 100;
    private final int yIncrement = -50;
	public static final int xTable = 200;
	public static final int yTable = 250;
	private boolean OrderAppears = false;
	private boolean OrderGiven = false;
	//private ImageIcon steak = new ImageIcon("C:/Users/Wayne/restaurant_chiwayne/steak.jpg");
	private ImageIcon question = new ImageIcon(this.getClass().getResource("question.jpg"));
	private ImageIcon Food = new ImageIcon();
	private ImageIcon steak = new ImageIcon(this.getClass().getResource("steak.jpg"));
	private ImageIcon chicken = new ImageIcon(this.getClass().getResource("chicken.jpg"));
	private ImageIcon salad = new ImageIcon(this.getClass().getResource("salad.jpg"));
	private ImageIcon pizza = new ImageIcon(this.getClass().getResource("pizza.jpg"));
	

	public CustomerGui(WcCustomerRole c, RestaurantGui gui){ //WcHostAgent m) {
		agent = c;
		xPos = -20;
		yPos = -20;
		xDestination = -20;
		yDestination = -20;
		//maitreD = m;
		this.gui = gui;
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

		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				//System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, 20, 20);
		if(OrderGiven) {
			g.drawImage(question.getImage(),xPos+20,yPos,null);
		}
		if(OrderAppears){
			g.drawImage(Food.getImage(), xPos+20, yPos, null);
		}
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}
	
	public int getPosX() {
		return xPos;
	}
	
	public int getPosY() {
		return yPos;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public WcCustomerRole getCustomer() {
		return agent;
	}

	public void DoGoToSeat(int seatnumber) {//later you will map seatnumber to table coordinates.
		/*if(tableNumber == 0)
		{
			agent.getWaiter().ReguideCustomer(agent);
		}*/
		
		xDestination = xTable + ((tableNumber-1)*xIncrement);
		yDestination = yTable + ((tableNumber-1)*yIncrement);
		System.out.println("I am going to Table Number: " + tableNumber);	
		command = Command.GoToSeat;
	}

	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
		OrderAppears = false;
		OrderGiven = false;
	}

	public void setTableNumber(int tableNumber2) {
		tableNumber = tableNumber2;
		
	}

	public void OrderDelivered(String myChoice) {
		OrderGiven = false;
		if(myChoice == "Steak") {
			Food.setImage(steak.getImage());
			
		}
		if(myChoice == "Chicken") {
			Food.setImage(chicken.getImage());
			
		}
		if(myChoice == "Pizza") {
			Food.setImage(pizza.getImage());
			
		}
		if(myChoice == "Salad") {
			Food.setImage(salad.getImage());
			
		}
		OrderAppears = true;

	}
	
	public void OrderWaiting() {
		OrderGiven = true;
	}

	public void msgHeadToWaitingArea(int customerNum) {
		xDestination = 40 + customerNum*30;
		yDestination = 40;
		this.customerNum = customerNum;
		waiting = true;
		
	}
}

