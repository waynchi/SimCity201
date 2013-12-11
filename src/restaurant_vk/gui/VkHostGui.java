package restaurant_vk.gui;

import restaurant_vk.VkHostRole;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/*
 * Don't look at this thing. I just left it as it was in V1.
 */
public class VkHostGui implements VkGui {

    public VkHostRole host = null;
    private int jobPosX = 20;
    private int jobPosY = 115;
    private int entranceX = 20;
    private int entranceY = -20;
    private int xPos = entranceX, yPos = entranceY;
    private int xDestination = entranceX, yDestination = entranceY;
    public static final int xTable = 200;
    public static final int yTable = 250;
    private final int HOST_WIDTH = 20;
    private final int HOST_HEIGHT = 35;
	private State state = State.None;
	public RestaurantVkAnimationPanel ap;
	public Image sprite = new BufferedImage(HOST_WIDTH, HOST_HEIGHT, BufferedImage.TYPE_INT_BGR);
	
	enum State {None, Entering, Exiting, OnDuty};

    public VkHostGui(VkHostRole agent) {
        this.host = agent;
        
        try {
			sprite = ImageIO.read(new File("res/restaurant_vk/host.gif"));
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
			
        	if (state == State.Entering) {
				state = State.OnDuty;
				host.activityDone();
			}
			else if (state == State.Exiting) {
				state = State.None;
				host.activityDone();
			}
        }
    }

    public void draw(Graphics2D g) {
    	g.drawImage(sprite, xPos, yPos, null);
    }

    public boolean isPresent() {
    	if (state != State.None)
    		return true;
    	return false;
    }
    
    public void DoEnterRestaurant() {
    	xDestination = jobPosX;
		yDestination = jobPosY;
		state = State.Entering;
    }
    
    public void DoLeaveRestaurant() {
    	xDestination = entranceX;
		yDestination = entranceY;
		state = State.Exiting;
    }
    
    public void setAnimationPanel(RestaurantVkAnimationPanel p) {
    	this.ap = p;
    	ap.addGui(this);
    }
}
