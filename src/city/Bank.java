package city;

import java.awt.Dimension;

import bank.TellerRole;
import restaurant.HostRole;

public class Bank {
	public TellerRole t;
	public Dimension l;
	public String n;
	public Bank(TellerRole host, Dimension loc, String name) {
		this.t = host;
		this.l = loc;
		this.n = name;
		
	}
	
}