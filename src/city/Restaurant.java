package city;

import java.awt.Dimension;

import restaurant.HostRole;

public class Restaurant {
	HostRole h;
	Dimension l;
	String n;
	public Restaurant(HostRole host, Dimension loc, String name) {
		this.h = host;
		this.l = loc;
		this.n = name;
		
	}
	
}