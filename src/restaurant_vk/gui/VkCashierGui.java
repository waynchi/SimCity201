package restaurant_vk.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import restaurant_vk.VkCashierRole;

public class VkCashierGui implements VkGui {
	private int jobPosX = 620;
	private int jobPosY = 130;
	private int entranceX = 430;
	private int entranceY = -20;
	private int xPos = entranceX;
	private int yPos = entranceY;
	private int xDestination = entranceX;
	private int yDestination = entranceY;
	private State state = State.None;
	public VkCashierRole cashier;
	public RestaurantVkAnimationPanel ap;
	public Image sprite1 = new BufferedImage(20, 20, BufferedImage.TYPE_INT_BGR);
	public Image sprite2 = new BufferedImage(20, 20, BufferedImage.TYPE_INT_BGR);
	public Image sprite;
	
	enum State {None, Entering, OnDuty, Exiting};
	
	public VkCashierGui(VkCashierRole c) {
		this.cashier = c;
		
		try {
			sprite1 = ImageIO.read(new File("res/restaurant_vk/cashierFront.gif"));
		} catch (IOException e) {
			System.out.println("Image not found.");
		}
		try {
			sprite2 = ImageIO.read(new File("res/restaurant_vk/cashierBack.gif"));
		} catch (IOException e) {
			System.out.println("Image not found.");
		}
		sprite = sprite1;
	}

	@Override
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
				cashier.activityDone();
			}
			else if (state == State.Exiting) {
				state = State.None;
				cashier.activityDone();
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(sprite, xPos, yPos, null);
	}

	@Override
	public boolean isPresent() {
		if (state != State.None)
			return true;
		return false;
	}
	
	public void DoEnterRestaurant() {
		xDestination = jobPosX;
		yDestination = jobPosY;
		state = State.Entering;
		sprite = sprite1;
	}
	
	public void DoLeaveRestaurant() {
		xDestination = entranceX;
		yDestination = entranceY;
		state = State.Exiting;
		sprite = sprite2;
	}
	
	public void setAnimationPanel(RestaurantVkAnimationPanel p) {
		this.ap = p;
		ap.addGui(this);
	}
}