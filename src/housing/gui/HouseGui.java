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
	List<HGui> guis = new ArrayList<HGui>();
	public Dimension entranceCoordinatesInternal = new Dimension(455, 345);
	public Dimension entranceCoordinatesExternal = new Dimension();
	public Image i1 = new BufferedImage(500, 500, BufferedImage.TYPE_INT_BGR);

	public HouseGui(House h) {
		this.h = h;
		setItems();
		try {
			i1 = ImageIO.read(new File("res/housingItemImages/houseGui.png"));
		} catch (IOException e) {
			System.out.println("Image not found.");
		}
	}

	@Override
	public void updatePosition() {
		for (HGui gui : guis) {
			gui.updatePosition();
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(i1, 0, 0, null);
		
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
	
	private void setItems() {
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