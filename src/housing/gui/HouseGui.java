package housing.gui;

import housing.House;
import housing.HouseType;
import housing.Item;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class HouseGui implements HGui{
	public House h;
	List<ItemGui> items = new ArrayList<ItemGui>();
	public List<HGui> guis = new ArrayList<HGui>();
	public Dimension entranceCoordinatesInternal = new Dimension(435, 345);
	public Dimension entranceCoordinatesExternal = new Dimension();
	public Image villaImage = new BufferedImage(500, 500, BufferedImage.TYPE_INT_BGR);
	public Image apartmentImage = new BufferedImage(500, 500, BufferedImage.TYPE_INT_BGR);
	public HouseAnimationPanel hp = new HouseAnimationPanel();

	public HouseGui(House h) {
		this.h = h;
		setItems();
		try {
			villaImage = ImageIO.read(new File("res/housingItemImages/houseGui.png"));
		} catch (IOException e) {
			System.out.println("Image not found.");
		}
		try {
			apartmentImage = ImageIO.read(new File("res/housingItemImages/apartment.png"));
		} catch (IOException e) {
			System.out.println("Image not found.");
		}
		hp.addHouseGui(this);
	}

	@Override
	public void updatePosition() {
		for (HGui gui : guis) {
			gui.updatePosition();
		}
	}

	@Override
	public void draw(Graphics2D g) {
		if (this.h.type == HouseType.Villa)
			g.drawImage(villaImage, 0, 0, null);
		else {
			g.drawImage(apartmentImage, 0, 0, null);
			g.setColor(Color.BLACK);
			g.drawString(h.number + "", 250, 250);
			g.setColor(Color.BLUE);
			g.fillRect(470, 15, 30, 60);
			g.setColor(Color.BLACK);
			g.drawString("B", 480, 30);
			g.drawString("A", 480, 42);
			g.drawString("C", 480, 54);
			g.drawString("K", 480, 66);
		}
		
		for (HGui gui : guis) {
			if (gui.isPresent()) {
				gui.draw(g);
			}
		}
	}

	@Override
	public boolean isPresent() {
		return true;
	}
	
	public void setItems() {
		List<Item> list = h.items;
		for (Item i : list) {
			items.add(i.getGui());
		}
	}
	
	public Dimension getPosition(String s) {
		Dimension d = null;
		for (ItemGui g : items) {
			if (g.i.name.equals(s)) {
				d = new Dimension(g.x, g.y);
				return d;
			}
		}
		return null;
	}
	
	public void add(HGui g) {
		guis.add(g);
	}
	
	public void remove(HGui g) {
		guis.remove(g);
	}
	
	public void setExternalCoordinates(Dimension d) {
		entranceCoordinatesExternal.width = d.width;
		entranceCoordinatesExternal.height = d.height;
	}
}