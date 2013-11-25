package housing.test;

import static org.junit.Assert.*;
import org.junit.Test;
import housing.House;
import housing.HouseType;
import housing.HousingRenterRole;
import housing.interfaces.Owner;
import housing.interfaces.Renter;
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
		assertFalse(r.isRentDue());
		assertEquals(0, r.getTimesRentDue());
	}
	
	@Test
	public void testNonNormative1() {
		setUp();
		
		((HousingRenterRole)r).testModeOn();
		r.setMoney(200);
		
		r.rentReminder();
		
		assertTrue(r.isRentDue());
		assertEquals(1, r.getTimesRentDue());
		
		r.pickAndExecuteAnAction();
		
		assertEquals("Received rent of $200.0 from Residence", ((MockOwner)o).log.getLastLoggedEvent().getMessage());
		assertEquals(2, ((MockOwner)o).log.size());
		assertFalse(r.isRentDue());
		assertEquals(0, r.getTimesRentDue());
		
		r.rentReminder();
		assertTrue(r.isRentDue());
		assertEquals(1, r.getTimesRentDue());
		
		r.pickAndExecuteAnAction();
		assertEquals(2, ((MockOwner)o).log.size());
		assertTrue(r.isRentDue());
		assertEquals(1, r.getTimesRentDue());
	}
	
	@Test
	public void testNonNormative2() {
		setUp();
		
		((HousingRenterRole)r).testModeOn();
		r.setMoney(0);
		
		r.rentReminder();
		assertTrue(r.isRentDue());
		assertEquals(1, r.getTimesRentDue());
		
		r.pickAndExecuteAnAction();
		assertTrue(r.isRentDue());
		assertEquals(1, r.getTimesRentDue());
		assertEquals(1, ((MockOwner)o).log.size());
		
		r.rentReminder();
		assertTrue(r.isRentDue());
		assertEquals(2, r.getTimesRentDue());
		r.payPenalty(50.0);
		assertEquals(1, r.getPenalties().size());
		
		r.pickAndExecuteAnAction();
		assertEquals(1, ((MockOwner)o).log.size());
		assertEquals(2, r.getTimesRentDue());
		assertEquals(1, r.getPenalties().size());
	}
	
	@Test
	public void testNonNormative3() {
		setUp();
		
		((HousingRenterRole)r).testModeOn();
		r.setMoney(0);
		
		r.rentReminder();
		assertTrue(r.isRentDue());
		assertEquals(1, r.getTimesRentDue());
		
		r.pickAndExecuteAnAction();
		assertTrue(r.isRentDue());
		assertEquals(1, r.getTimesRentDue());
		assertEquals(1, ((MockOwner)o).log.size());
		
		r.rentReminder();
		assertTrue(r.isRentDue());
		assertEquals(2, r.getTimesRentDue());
		r.payPenalty(50.0);
		assertEquals(1, r.getPenalties().size());
		
		r.setMoney(200);
		r.pickAndExecuteAnAction();
		assertEquals("Received rent of $200.0 from Residence", ((MockOwner)o).log.getLastLoggedEvent().getMessage());
		assertEquals(2, ((MockOwner)o).log.size());
		assertTrue(r.isRentDue());
		assertEquals(1, r.getTimesRentDue());
		
		r.pickAndExecuteAnAction();
		assertEquals("Received rent of $200.0 from Residence", ((MockOwner)o).log.getLastLoggedEvent().getMessage());
		assertEquals(2, ((MockOwner)o).log.size());
	}
	
	@Test
	public void testNonNormative4() {
		setUp();
		
		((HousingRenterRole)r).testModeOn();
		r.setMoney(0);
		
		r.rentReminder();
		assertTrue(r.isRentDue());
		assertEquals(1, r.getTimesRentDue());
		
		r.pickAndExecuteAnAction();
		assertTrue(r.isRentDue());
		assertEquals(1, r.getTimesRentDue());
		assertEquals(1, ((MockOwner)o).log.size());
		
		r.rentReminder();
		assertTrue(r.isRentDue());
		assertEquals(2, r.getTimesRentDue());
		r.payPenalty(50.0);
		assertEquals(1, r.getPenalties().size());
		
		r.setMoney(250);
		r.pickAndExecuteAnAction();
		assertEquals("Received rent of $200.0 from Residence", ((MockOwner)o).log.getLastLoggedEvent().getMessage());
		assertEquals(2, ((MockOwner)o).log.size());
		assertTrue(r.isRentDue());
		assertEquals(1, r.getTimesRentDue());
		
		r.pickAndExecuteAnAction();
		assertEquals("Received penalty of $50.0 from Residence", ((MockOwner)o).log.getLastLoggedEvent().getMessage());
		assertEquals(3, ((MockOwner)o).log.size());
	}
	
	@Test
	public void testNonNormative5() {
		setUp();
		
		((HousingRenterRole)r).testModeOn();
		r.setMoney(0);
		
		r.rentReminder();
		assertTrue(r.isRentDue());
		assertEquals(1, r.getTimesRentDue());
		
		r.pickAndExecuteAnAction();
		assertTrue(r.isRentDue());
		assertEquals(1, r.getTimesRentDue());
		assertEquals(1, ((MockOwner)o).log.size());
		
		r.rentReminder();
		assertTrue(r.isRentDue());
		assertEquals(2, r.getTimesRentDue());
		r.payPenalty(50.0);
		assertEquals(1, r.getPenalties().size());
		
		r.setMoney(400);
		r.pickAndExecuteAnAction();
		assertEquals("Received rent of $200.0 from Residence", ((MockOwner)o).log.getLastLoggedEvent().getMessage());
		assertEquals(2, ((MockOwner)o).log.size());
		assertTrue(r.isRentDue());
		assertEquals(1, r.getTimesRentDue());
		
		r.pickAndExecuteAnAction();
		assertEquals("Received rent of $200.0 from Residence", ((MockOwner)o).log.getLastLoggedEvent().getMessage());
		assertEquals(3, ((MockOwner)o).log.size());
		assertFalse(r.isRentDue());
		assertEquals(0, r.getTimesRentDue());
		
		r.pickAndExecuteAnAction();
		assertEquals(3, ((MockOwner)o).log.size());
	}
	
	@Test
	public void testNonNormative6() {
		setUp();
		
		((HousingRenterRole)r).testModeOn();
		r.setMoney(0);
		
		r.rentReminder();
		assertTrue(r.isRentDue());
		assertEquals(1, r.getTimesRentDue());
		
		r.pickAndExecuteAnAction();
		assertTrue(r.isRentDue());
		assertEquals(1, r.getTimesRentDue());
		assertEquals(1, ((MockOwner)o).log.size());
		
		r.rentReminder();
		assertTrue(r.isRentDue());
		assertEquals(2, r.getTimesRentDue());
		r.payPenalty(50.0);
		assertEquals(1, r.getPenalties().size());
		
		r.setMoney(450);
		r.pickAndExecuteAnAction();
		assertEquals("Received rent of $200.0 from Residence", ((MockOwner)o).log.getLastLoggedEvent().getMessage());
		assertEquals(2, ((MockOwner)o).log.size());
		assertTrue(r.isRentDue());
		assertEquals(1, r.getTimesRentDue());
		
		r.pickAndExecuteAnAction();
		assertEquals("Received rent of $200.0 from Residence", ((MockOwner)o).log.getLastLoggedEvent().getMessage());
		assertEquals(3, ((MockOwner)o).log.size());
		assertFalse(r.isRentDue());
		assertEquals(0, r.getTimesRentDue());
		
		r.pickAndExecuteAnAction();
		assertEquals("Received penalty of $50.0 from Residence", ((MockOwner)o).log.getLastLoggedEvent().getMessage());
		assertEquals(4, ((MockOwner)o).log.size());
		assertFalse(r.isRentDue());
		assertEquals(0, r.getTimesRentDue());
		assertEquals(0, r.getPenalties().size());
	}
	
	public void setUp() {
		h = null;
		r = null;
		o = null;
		
		h = new House("Residence", 1, HouseType.Villa);
		r = new HousingRenterRole(200.0);
		o = new MockOwner();
		
		h.setOccupant((Resident)r);
		h.setItemsWithoutGui();
		
		r.setOwner(o);
		((Resident)r).setHouse(h);
		((HousingRenterRole)r).testModeOn();
		
		o.addHouse(h, r);
	}
}