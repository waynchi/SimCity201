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
	House h1;
	House h2;
	Renter r1;
	Renter r2;
	Owner o;
	RepairMan r;
	
	@Test
	public void testNormative1() {
		setUp();
		
		assertFalse(r1.isRentDue());
		r1.rentReminder();
		assertTrue(r1.isRentDue());
		assertEquals(1, r1.getTimesRentDue());
		
		assertEquals(0, o.getTimesRentDue(h1));
		o.generate(h1);
		assertEquals(1, o.getTimesRentDue(h1));
		
		r1.setMoney(200.0);
		r1.pickAnExecuteAnAction();
		
		assertFalse(r1.isRentDue());
		assertEquals(0, r1.getTimesRentDue());
		assertEquals(0, o.getTimesRentDue(h1));
	}
	
	@Test
	public void testNormative2() {
		setUp();
		
		assertFalse(r1.isRentDue());
		r1.rentReminder();
		assertTrue(r1.isRentDue());
		assertEquals(1, r1.getTimesRentDue());
		assertFalse(r2.isRentDue());
		r2.rentReminder();
		assertTrue(r2.isRentDue());
		assertEquals(1, r2.getTimesRentDue());
		
		assertEquals(0, o.getTimesRentDue(h1));
		o.generate(h1);
		assertEquals(1, o.getTimesRentDue(h1));
		assertEquals(0, o.getTimesRentDue(h2));
		o.generate(h2);
		assertEquals(1, o.getTimesRentDue(h2));
		
		r1.setMoney(200.0);
		r2.setMoney(200.0);
		r1.pickAnExecuteAnAction();
		r2.pickAnExecuteAnAction();
		
		assertFalse(r1.isRentDue());
		assertFalse(r2.isRentDue());
		assertEquals(0, r1.getTimesRentDue());
		assertEquals(0, o.getTimesRentDue(h1));
		assertEquals(0, r2.getTimesRentDue());
		assertEquals(0, o.getTimesRentDue(h2));
	}
	
	@Test
	public void testNormative3() {
		setUp();
		
		assertFalse(r1.isRentDue());
		r1.rentReminder();
		assertTrue(r1.isRentDue());
		assertEquals(1, r1.getTimesRentDue());
		
		assertEquals(0, o.getTimesRentDue(h1));
		o.generate(h1);
		assertEquals(1, o.getTimesRentDue(h1));
		
		r1.setMoney(600.0);
		r1.pickAnExecuteAnAction();
		
		assertFalse(r1.isRentDue());
		assertEquals(0, r1.getTimesRentDue());
		assertEquals(0, o.getTimesRentDue(h1));
		
		o.generate(h1);
		assertEquals(1, o.getTimesRentDue(h1));
		
		r1.rentReminder();
		assertTrue(r1.isRentDue());
		assertEquals(1, r1.getTimesRentDue());
		
		r1.pickAnExecuteAnAction();
		
		assertFalse(r1.isRentDue());
		assertEquals(0, r1.getTimesRentDue());
		assertEquals(0, o.getTimesRentDue(h1));
	}
	
	@Test
	public void testNormative4() {
		setUp();
		
		assertFalse(r1.isRentDue());
		r1.rentReminder();
		assertTrue(r1.isRentDue());
		assertEquals(1, r1.getTimesRentDue());
		assertFalse(r2.isRentDue());
		r2.rentReminder();
		assertTrue(r2.isRentDue());
		assertEquals(1, r2.getTimesRentDue());
		
		assertEquals(0, o.getTimesRentDue(h1));
		o.generate(h1);
		assertEquals(1, o.getTimesRentDue(h1));
		assertEquals(0, o.getTimesRentDue(h2));
		o.generate(h2);
		assertEquals(1, o.getTimesRentDue(h2));
		
		r1.setMoney(400.0);
		r2.setMoney(400.0);
		r1.pickAnExecuteAnAction();
		
		assertFalse(r1.isRentDue());
		assertEquals(0, r1.getTimesRentDue());
		assertEquals(0, o.getTimesRentDue(h1));
		
		r1.rentReminder();
		o.generate(h1);
		
		assertEquals(1, r1.getTimesRentDue());
		assertEquals(1, o.getTimesRentDue(h1));
		assertTrue(r1.isRentDue());
		
		r2.pickAnExecuteAnAction();
		
		assertFalse(r2.isRentDue());
		assertEquals(0, r2.getTimesRentDue());
		assertEquals(0, o.getTimesRentDue(h2));
		
		r1.pickAnExecuteAnAction();
		assertEquals(0, r1.getTimesRentDue());
		assertEquals(0, o.getTimesRentDue(h1));
		assertFalse(r1.isRentDue());
	}
	
	@Test
	public void testNoNormative1() {
		setUp();
	}
	
	public void setUp() {
		h1 = null;
		h2 = null;
		r1 = null;
		r2 = null;
		o = null;
		r = null;
		
		h1 = new House("R1Residence", 1);
		r1 = new HousingRenterRole(200.0);
		h2 = new House("R2Residence", 2);
		r2 = new HousingRenterRole(200.0);
		o = new HousingOwnerRole();
		r = new HousingRepairManRole();
		
		h1.setOccupant((Resident)r1);
		h1.setItemsWithoutGui();
		h2.setOccupant((Resident)r2);
		h2.setItemsWithoutGui();
		
		o.addHouse(h1, (Renter)r1);
		o.addHouse(h2, (Renter)r2);
		
		r.addHouse(h1, (Resident)r1);
		r.addHouse(h2, (Resident)r2);
		
		r1.setOwner(o);
		((Resident)r1).setHouse(h1);
		((Resident)r1).setRepairMan(r);
		r2.setOwner(o);
		((Resident)r2).setHouse(h2);
		((Resident)r2).setRepairMan(r);
	}
}
