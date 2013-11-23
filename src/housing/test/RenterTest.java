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
		
		r.setMoney(400.0);
		
		assertEquals(1, ((MockOwner)o).log.size());
		assertEquals("Added Residence", ((MockOwner)o).log.getLastLoggedEvent().getMessage());
		
		assertFalse(r.isRentDue());
		assertEquals(0, r.getTimesRentDue());
		
		r.rentReminder();
		
		assertTrue(r.isRentDue());
		assertEquals(1, r.getTimesRentDue());
		
		r.pickAndExecuteAnAction();
		
		assertEquals("Received rent of $200.0 from Residence", ((MockOwner)o).log.getLastLoggedEvent().getMessage());
		
		assertFalse(r.isRentDue());
		assertEquals(0, r.getTimesRentDue());
		
		r.rentReminder();
		
		assertTrue(r.isRentDue());
		assertEquals(1, r.getTimesRentDue());
		
		r.pickAndExecuteAnAction();
		
		assertEquals("Received rent of $200.0 from Residence", ((MockOwner)o).log.getLastLoggedEvent().getMessage());
	}
	
	public void testNonNormative1() {
		setUp();
		
		r.setMoney(200);
		
		r.rentReminder();
		r.rentReminder();
		
		assertTrue(r.isRentDue());
		assertEquals(2, r.getTimesRentDue());
		
		r.pickAndExecuteAnAction();
		
		assertEquals("Received rent of $200.0 from Residence", ((MockOwner)o).log.getLastLoggedEvent().getMessage());
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
		
		r.setOwner(o);
		((Resident)r).setHouse(h);
		
		o.addHouse(h, r);
	}
}
