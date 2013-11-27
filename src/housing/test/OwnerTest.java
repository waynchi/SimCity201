package housing.test;

import static org.junit.Assert.*;
import housing.House;
import housing.HouseType;
import housing.HousingOwnerRole;
import housing.HousingOwnerRole.RentOrderState;
import housing.interfaces.Owner;
import housing.interfaces.Renter;
import housing.interfaces.Resident;
import org.junit.Test;

import people.PeopleAgent;

public class OwnerTest {
	Owner o;
	Renter r1;
	Renter r2;
	Renter r3;
	House h1;
	House h2;
	House h3;
	PeopleAgent p;
	
	@Test
	public void testNormative1() {
		setUp();
		
		o.generate(h1);
		o.generate(h2);
		o.generate(h3);
		
		assertEquals(3, o.getTotalRents());
		
		o.hereIsRent(h1, 200);
		o.hereIsRent(h2, 200);
		o.hereIsRent(h3, 200);
		
		assertEquals(0, o.getTotalRents());
	}
	
	@Test
	public void testNormative2() {
		setUp();
		
		o.generate(h1);
		o.generate(h2);
		o.hereIsRent(h1, 200);
		o.generate(h3);
		o.hereIsRent(h2, 200);
		o.hereIsRent(h3, 200);
		
		assertEquals(0, o.getTotalRents());
	}
	
	@Test
	public void testNonNormative1() {
		setUp();
		
		assertEquals("Owner added", ((MockRenter)r1).log.getLastLoggedEvent().getMessage());
		
		o.generate(h1);
		o.generate(h1);
		o.generate(h2);
		assertEquals(3, o.getTotalRents());
		
		assertNotNull(((HousingOwnerRole)o).findRentOrderByState(RentOrderState.Due));
		
		o.pickAndExecuteAnAction();
		
		assertEquals("Penalty of $50.0 applied", ((MockRenter)r1).log.getLastLoggedEvent().getMessage());
		
		o.hereIsRent(h1, 200);
		
		assertEquals(2, o.getTotalRents());
		
		o.hereIsRent(h1, 200);
		o.hereIsRent(h2, 200);
		
		assertEquals(0, o.getTotalRents());
	}
	
	@Test
	public void testNonNormative2() {
		setUp();
		
		assertEquals("Owner added", ((MockRenter)r1).log.getLastLoggedEvent().getMessage());
		
		o.generate(h1);
		o.generate(h1);
		o.generate(h2);
		assertEquals(3, o.getTotalRents());
		assertEquals(2, o.getTimesRentDue(h1));
		
		o.hereIsRent(h1, 200);
		
		assertEquals("Owner added", ((MockRenter)r1).log.getLastLoggedEvent().getMessage());
		assertEquals(2, o.getTotalRents());
		assertEquals(1, o.getTimesRentDue(h1));
		
		o.hereIsRent(h1, 200);
		
		assertEquals("Owner added", ((MockRenter)r1).log.getLastLoggedEvent().getMessage());
		assertEquals(2, o.getTotalRents());
		assertEquals(1, o.getTimesRentDue(h1));
		
		o.pickAndExecuteAnAction();
		
		assertEquals("Penalty of $50.0 applied", ((MockRenter)r1).log.getLastLoggedEvent().getMessage());
		assertEquals(1, o.getTotalRents());
		assertEquals(0, o.getTimesRentDue(h1));
		
		o.hereIsRent(h1, 200);
		o.hereIsRent(h2, 200);
		
		assertEquals(0, o.getTotalRents());
	}
	
	public void setUp() {
		h1 = null;
		h2 = null;
		h3 = null;
		r1 = null;
		r2 = null;
		r3 = null;
		o = null;
		p = null;
		
		h1 = new House("R1Residence", 1, HouseType.Villa);
		r1 = new MockRenter();
		h2 = new House("R2Residence", 2, HouseType.Villa);
		r2 = new MockRenter();
		h3 = new House("R3Residence", 3, HouseType.Villa);
		r3 = new MockRenter();
		o = new HousingOwnerRole();
		p = new MockPeopleHousing("Name");
		
		((HousingOwnerRole)o).testModeOn();
		
		h1.testModeOn();
		h1.setOccupant((Resident)r1);
		h1.setItemsWithoutGui();
		h2.testModeOn();
		h2.setOccupant((Resident)r2);
		h2.setItemsWithoutGui();
		h3.testModeOn();
		h3.setOccupant((Resident)r3);
		h3.setItemsWithoutGui();
		
		((HousingOwnerRole)o).setPerson(p);
		o.addHouse(h1, r1);
		o.addHouse(h2, r2);
		o.addHouse(h3, r3);
		
		r1.setOwner(o);
		((Resident)r1).setHouse(h1);
		r2.setOwner(o);
		((Resident)r2).setHouse(h2);
		r3.setOwner(o);
		((Resident)r3).setHouse(h3);
	}
}