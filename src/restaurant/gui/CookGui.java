package restaurant.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import restaurant.CookRole;


public class CookGui implements Gui {

	private CookRole agent = null;
	Boolean isCooking;
	String foodBeingCooked = null;
	private List<String> foodPlated = new ArrayList<String>();
	
	public CookGui (CookRole cook) {
		isCooking = false;
		agent = cook;
		for (int i=0;i<3;i++) {
			foodPlated.add("");
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
        g.setColor(Color.blue);
		g.fillRect(70, 270, 20, 20);
        g.setColor(Color.BLACK);
        g.drawString("Cook", 65, 300);
        if (isCooking) {
        	g.drawString(foodBeingCooked, 50, 265);
        }
        else g.drawString("", 50, 265);
        for (int i=0; i<3;i++) {
        	g.drawString(foodPlated.get(i), 50+45*i, 225);
        }
	}

	@Override
	public boolean isPresent() {
		return true;
	}

	public void cookFood (String food) {
		isCooking = true;
		foodBeingCooked = food;
	}
	
	public void plateFood (String food, int i){
		foodPlated.set(i-1, food);	
	}
	
	public void foodPickedUp(int table) {
		foodPlated.set((table-1),"");
	}
	
	public void finishCooking () {
		isCooking = false;
	}
}
