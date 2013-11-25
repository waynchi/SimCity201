package housing;

import housing.gui.RepairShopGui;
import housing.interfaces.RepairMan;

public class RepairShop {
	public RepairShopGui gui;
	public RepairMan r;
	
	public RepairShop() {
		gui = new RepairShopGui(this);
	}
	
	public RepairShop(RepairMan r) {
		gui = new RepairShopGui(this);
		this.r = r;
	}
	
	public void setRepairMan(RepairMan r) {
		this.r = r;
	}
}
