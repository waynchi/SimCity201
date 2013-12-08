package restaurant_wc.gui;

import restaurant_wc.WcCookRole;
import restaurant_wc.WcCustomerRole;
import restaurant_wc.WcHostAgent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javax.swing.ImageIcon;

public class CookGui implements Gui{

	private WcCookRole cook;
	private int xPos, yPos;
	private int xDestination, yDestination;
	private int xHome, yHome;
	private RestaurantGui gui;
	private ImageIcon Blank = new ImageIcon("none");
	private ImageIcon steak = new ImageIcon(this.getClass().getResource("steak.jpg"));
	private ImageIcon chicken = new ImageIcon(this.getClass().getResource("chicken.jpg"));
	private ImageIcon salad = new ImageIcon(this.getClass().getResource("salad.jpg"));
	private ImageIcon pizza = new ImageIcon(this.getClass().getResource("pizza.jpg"));
	private List<MyImageIcon> FoodList = Collections.synchronizedList(new ArrayList<MyImageIcon>());
	
	public CookGui(RestaurantGui gui)
	{
		this.gui = gui;
		xPos = 450;
		yPos = 400;
		xHome = 450;
		yHome = 400;
		xDestination = 450;
		yDestination = 400;
		FoodList.add(new MyImageIcon(Blank, "none"));
		FoodList.add(new MyImageIcon(Blank, "none"));
		FoodList.add(new MyImageIcon(Blank, "none"));
	}
	public void updatePosition() {
		// TODO Auto-generated method stub
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;
		
		for(int i = 0; i < FoodList.size(); i++)
		{
			if(xPos == xDestination && yPos == yDestination && FoodList.get(i).Status.equals("inFridge"))
			{
				FoodList.get(i).Status = "moving";
				xDestination = gui.animationPanel.GrillX - 20;
				yDestination = gui.animationPanel.GrillY + i*35;
			}		
			if(xPos == xDestination && yPos == yDestination && FoodList.get(i).Status.equals("moving"))
			{
				FoodList.get(i).Status = "cooking";
				cook.msgDestinationArrival();
				xDestination = xHome;
				yDestination = yHome;
			}
			if(xPos == xDestination && yPos == yDestination && FoodList.get(i).Status.equals("readyToPlate"))
			{
				FoodList.get(i).Status = "plating";
				xDestination = gui.animationPanel.KitchenX + 20;
				yDestination = gui.animationPanel.GrillY + i*35;
			}
			if(xPos == xDestination && yPos == yDestination && FoodList.get(i).Status.equals("plating"))
			{
				FoodList.get(i).Status = "cooked";
				cook.msgDonePlating();
				xDestination = xHome;
				yDestination = yHome;
			}
		}
		
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		try{
		g.setColor(Color.RED);
		g.fillRect(xPos, yPos, 20, 20);	
		for(int i = 0; i < FoodList.size(); i++)
		{
			if(FoodList.get(i).Status.equals("moving") )
			{
				g.drawImage(FoodList.get(i).imageIcon.getImage(), xPos + 20, yPos+20, null);
			}
			if(FoodList.get(i).Status.equals("plating") )
			{
				g.drawImage(FoodList.get(i).imageIcon.getImage(), xPos + 20, yPos+20, null);
			}
			if(FoodList.get(i).Status.equals("cooking") || FoodList.get(i).Status.equals("readyToPlate"))
			{
				g.drawImage(FoodList.get(i).imageIcon.getImage(), gui.animationPanel.GrillX, gui.animationPanel.GrillY + i*35, null);
			}
			else if(FoodList.get(i).Status.equals("cooked"))
			{
				g.drawImage(FoodList.get(i).imageIcon.getImage(), gui.animationPanel.KitchenX, gui.animationPanel.GrillY + i*35, null);
			}
			}
		}
		catch(ConcurrentModificationException e)
		{
			return;
		}
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}

	public void setCook(WcCookRole cook) {
		// TODO Auto-generated method stub
		this.cook = cook;		
	}

	public void msgAddToGrill(String choice) {
		// TODO Auto-generated method stub
		if(choice == "Steak") {
			for(int i = 0; i < FoodList.size(); i++)
			{
				if(FoodList.get(i).imageIcon.equals(Blank))
				{
					FoodList.remove(FoodList.get(i));
					FoodList.add(i, new MyImageIcon(new ImageIcon(steak.getImage(), "Steak"), "inFridge"));	
					break;
				}
			}
		}
		if(choice == "Chicken") {
			for(int i = 0; i < FoodList.size(); i++)
			{
				if(FoodList.get(i).imageIcon.equals(Blank))
				{
					FoodList.remove(FoodList.get(i));
					FoodList.add(i, new MyImageIcon(new ImageIcon(chicken.getImage(), "Chicken"), "inFridge"));		
					break;
				}
			}
		}
		if(choice == "Pizza") {
			for(int i = 0; i < FoodList.size(); i++)
			{
				if(FoodList.get(i).imageIcon.equals(Blank))
				{
					FoodList.remove(FoodList.get(i));
					FoodList.add(i, new MyImageIcon(new ImageIcon(pizza.getImage(), "Pizza"), "inFridge"));		
					break;
				}
			}
		}
		if(choice == "Salad") {
			for(int i = 0; i < FoodList.size(); i++)
			{
				if(FoodList.get(i).imageIcon.equals(Blank))
				{
					FoodList.remove(FoodList.get(i));
					FoodList.add(i, new MyImageIcon(new ImageIcon(salad.getImage(), "Salad"), "inFridge"));		
					break;
				}
			}
		}
		xDestination = gui.animationPanel.FridgeX - 20;
		yDestination = gui.animationPanel.FridgeY - 20;
		}
	
	public void msgAddToPlating(String choice) {
		int j = 0;
		synchronized(FoodList){
			for(MyImageIcon i: FoodList){
				if(i.imageIcon.getDescription().equals(choice) && i.Status.equals("cooking"))
				{
					xDestination = gui.animationPanel.GrillX - 20;
					yDestination = gui.animationPanel.GrillY + j*35;
					i.Status = "readyToPlate";
					return;
				}
				j++;
			}
		}
		
	}
	public void msgTakingPlate(String choice) {
		synchronized(FoodList){
			for(int i = 0 ; i < FoodList.size(); i++) {
				if(FoodList.get(i).imageIcon.getDescription().equals(choice) && FoodList.get(i).Status.equals("cooked"))
				{
					
					FoodList.remove(FoodList.get(i));
					FoodList.add(i, new MyImageIcon(Blank, "none"));
					return;
				}
			}
		}
	}
	
	public class MyImageIcon
	{
		public ImageIcon imageIcon;
		public String Status;
		public MyImageIcon(ImageIcon imageIcon, String status)
		{
			this.imageIcon = imageIcon;
			this.Status = status;
		}
	}

	
}

