package city;

import java.awt.Dimension;

import market.MarketEmployeeRole;

public class Market {
	public MarketEmployeeRole mer;
	public Dimension l;
	public String n;
	public int bankAccountID;
	public boolean isClosed;
	public Market(MarketEmployeeRole m, Dimension loc, String name) {
		this.mer = m;
		this.l = loc;
		this.n = name;
		this.isClosed = true;
		
	}
}
