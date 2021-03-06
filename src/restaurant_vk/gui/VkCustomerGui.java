package restaurant_vk.gui;

import restaurant_vk.VkCustomerRole;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class VkCustomerGui implements VkGui{

	private VkCustomerRole agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
	private boolean leaveOption = false;
	private String[] menuArray;
	private String caption = "";
	private Map<String, String> symbols = new HashMap<String, String>();
	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToRestaurant, GoToSeat, LeaveRestaurant, GoToPay, GoAway};
	private Command command=Command.noCommand;
	public static final int xTable = 350;
	public static final int yTable = 350;
	private final int CUST_WIDTH = 20;
	private final int CUST_HEIGHT = 20;
	RestaurantVkAnimationPanel ap;
	public Image sprite = new BufferedImage(20, 20, BufferedImage.TYPE_INT_BGR);

	public VkCustomerGui(VkCustomerRole c) {
		agent = c;
		xPos = -20;
		yPos = 300;
		xDestination = -20;
		yDestination = 300;
		setSymbols();
		try {
			sprite = ImageIO.read(new File("res/custsprite_2.png"));
		} catch (IOException e) {
			System.out.println("Image not found.");
		}
	}

	public void updatePosition() {
		if (xPos < xDestination && Math.abs(xDestination - xPos) > 1)
			xPos += 2;
		else if (xPos > xDestination && Math.abs(xDestination - xPos) > 1)
			xPos -= 2;
		
		if (yPos < yDestination && Math.abs(yDestination - yPos) > 1)
			yPos += 2;
		else if (yPos > yDestination && Math.abs(yDestination - yPos) > 1)
			yPos -= 2;

		if (Math.abs(xPos - xDestination) < 2 && Math.abs(yPos - yDestination) < 2) {
			xPos = xDestination;
			yPos = yDestination;
			
			if (command == Command.GoToRestaurant) {
				agent.msgAnimationFinishedGoToRestaurant();
			}
			else if (command==Command.GoToSeat) {
				agent.msgAnimationFinishedGoToSeat();
			}
			else if (command==Command.LeaveRestaurant) {
				xPos = -20;
				yPos = 300;
				xDestination = -20;
				yDestination = 300;
				agent.msgAnimationFinishedLeaveRestaurant();
				System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				isPresent = false;
			}
			else if (command == Command.GoToPay) {
				agent.msgAnimationFinishedGoToPay();
			}
			else if (command == Command.GoAway) {
				isHungry = false;
				xPos = -20;
				yPos = 300;
				xDestination = -20;
				yDestination = 300;
				isPresent = false;
				agent.msgAnimationFinishedLeaveRestaurant();
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.drawImage(sprite, xPos, yPos, null);
		
		// If some caption has to be displayed, then do this.
		g.setColor(Color.BLACK);
		g.drawString(symbols.get(caption), xPos, yPos + 30);
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

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToSeat() {
		command = Command.GoToSeat;
	}
	
	/*
	 * This message is called by the WaiterGui to show the way to
	 * the customer.
	 */
	public void goTo(Dimension d) {
		xDestination = d.width;
		yDestination = d.height;
	}
	
	public void DoGoToPay(Dimension d) {
		xDestination = d.width;
		yDestination = d.height;
		command = Command.GoToPay;
	}

	public void DoExitRestaurant() {
		xDestination = 430;
		yDestination = -20;
		command = Command.LeaveRestaurant;
		caption = "";
	}
	
	public void DoGoAway() {
		if (xPos == -40 && yPos == -40) {
			xPos = 0;
			yPos = 200;
		}
		command = Command.GoAway;
		xDestination = 500;
		yDestination = 460;
		caption = "";
	}
	
	public void DoGoToRestaurant() {
		command = Command.GoToRestaurant;
		xDestination = 20;
		yDestination = 150;
		isPresent = true;
	}
	
	/*
	 * Sets the caption key.
	 */
	public void setCaption(String c) {
		caption = c;
	}
	
	public void setLeaveOption(boolean b) {
		leaveOption = b;
	}
	
	public boolean getLeaveOption() {
		return leaveOption;
	}
	
	public String[] getMenuCopy() {
		setMenuCopy();
		return menuArray;
	}
	
	public void setMenuCopy() {
		restaurant_vk.Menu m = agent.getMenu();
		ArrayList<String> l = (ArrayList<String>) m.getAllFoodNames();
		int size = l.size();
		menuArray = null;
		menuArray = new String[size];
		int i = 0;
		for (String s : l) {
			menuArray[i] = s;
			i++;
		}
	}
	
	public void setAnimationPanel(RestaurantVkAnimationPanel p) {
		this.ap = p;
		ap.addGui(this);
	}
	
	/*
	 * Sets up symbols inside the map.
	 */
	private void setSymbols() {
		symbols.put("Steak", "ST");
		symbols.put("Chicken", "CH");
		symbols.put("Salad", "SA");
		symbols.put("Pizza", "PI");
		symbols.put("Steak?", "ST?");
		symbols.put("Chicken?", "CH?");
		symbols.put("Salad?", "SA?");
		symbols.put("Pizza?", "PI?");
		symbols.put("", "");
	}
}
