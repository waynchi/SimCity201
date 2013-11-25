package housing.gui;

import housing.Item;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ItemGui implements HGui{
	public Item i;
	int x, y, width, height, d, wArc, hArc;
	Color c;
	boolean isBroken = false;
	Shape shape;
	private String imagePath = "";
	
	enum Shape {Circle, Rectangle, RoundRectangle};

	public ItemGui(Item i, int x, int y, int width, int height, Color c) {
		this.i = i;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.c = c;
		shape = Shape.Rectangle;
	}
	
	public ItemGui(Item i, int x, int y, int d, Color c) {
		this.i = i;
		this.x = x;
		this.y = y;
		this.d = d;
		this.c = c;
		shape = Shape.Circle;
	}
	
	public ItemGui(Item i, int x, int y, int width, int height, int wArc, int hArc, Color c) {
		this.i = i;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.wArc = wArc;
		this.hArc = hArc;
		this.c = c;
		shape = Shape.RoundRectangle;
	}
	
	public ItemGui(Item i, int x, int y, int width, int height, String imagePath) {
		this.i = i;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.imagePath = imagePath;
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(c);
		if (!this.imagePath.equals("")) {
			Image i = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
			try {
				i = ImageIO.read(new File(imagePath));
			} catch (IOException e) {
				System.out.println("Nope.");
			}
			g.drawImage(i, x, y, null);
		}
		else if (shape == Shape.Rectangle)
			g.fill3DRect(x, y, width, height, true);
		else if (shape == Shape.Circle)
			g.fillOval(x, y, d, d);
		else if (shape == Shape.RoundRectangle)
			g.fillRoundRect(x, y, width, height, wArc, hArc);
		if (isBroken()) {
			g.setColor(Color.WHITE);
			if (shape == Shape.Rectangle || shape == Shape.RoundRectangle) {
				g.drawLine(x, y, x + width, y + height);
				g.drawLine(x + width, y, x, y + height);
			}
			else if (shape == Shape.Circle) {
				g.drawLine(x, y, x + d, y + d);
				g.drawLine(x + d, y, x, y + d);
			}
		}
		
		if (this.i.name.equals("FussballTable")) {
			g.setColor(Color.BLACK);
			g.drawLine(x + 10, y, x + 10, y + 40);
			g.drawLine(x + 20, y, x + 20, y + 40);
			g.drawLine(x + 30, y, x + 30, y + 40);
			g.drawLine(x + 40, y, x + 40, y + 40);
			g.drawLine(x + 50, y, x + 50, y + 40);
			g.fillRect(x, y + 15, 5, 10);
			g.fillRect(x + 55, y + 15, 5, 10);
		}
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public void breakIt() {
		isBroken = true;
	}
	
	public void repair() {
		isBroken = false;
	}
	
	public boolean isBroken() {
		return isBroken;
	}
}