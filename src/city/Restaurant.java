package city;

import java.awt.Dimension;

import restaurant.HostRole;

public class Restaurant {
	public HostRole h;
	public Dimension l;
	public String n;
	public Integer bankAccountID;
	public Restaurant(HostRole host, Dimension loc, String name) {
		this.h = host;
		this.l = loc;
		this.n = name;
		
	}
	
}