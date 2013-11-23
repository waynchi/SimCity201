package housing.test;

import static org.junit.Assert.*;
import org.junit.Test;
import housing.House;
import housing.HousingOwnerRole;
import housing.HousingOwnerRole.MyHouse;
import housing.HousingRenterRole;
import housing.HousingRepairManRole;
import housing.interfaces.Owner;
import housing.interfaces.Renter;
import housing.interfaces.RepairMan;
import housing.interfaces.Resident;

public class RenterTest {
	House h;
	Renter r;
	Owner o;
	
	@Test
	public void testNormative1() {
		setUp();
	}
	
	public void setUp() {
		h = null;
		r = null;
		o = null;
		
		h = new House("Residence", 1);
		r = new HousingRenterRole(200.0);
		o = new MockOwner();
		
		h.setOccupant((Resident)r);
		h.setItemsWithoutGui();
		
		o.addHouse(h, r);
		
		r.setOwner(o);
		((Resident)r).setHouse(h);
	}
}
