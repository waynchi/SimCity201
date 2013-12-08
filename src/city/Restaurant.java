package city;

import java.awt.Dimension;

import people.Role;

public class Restaurant {
	public Role h;
	public Dimension l;
	public String n;
	public Integer bankAccountID;
	public boolean isClosed;
	public int restaurantIndex;
	public Restaurant(Role host, Dimension loc, String name, int Index) {
		this.h = host;
		this.l = loc;
		this.n = name;
		this.isClosed = false;
		this.restaurantIndex = Index;
	}
	
	
}