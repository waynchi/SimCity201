package housing.test;

import static org.junit.Assert.*;
import org.junit.Test;
import housing.House;
import housing.Owner;
import housing.Renter;

public class RenterOwnerTest {
	House h1;
	House h2;
	Renter r1;
	Renter r2;
	Owner o;
	
	@Test
	public void test() {
		setUp();
		
		assertFalse(r1.isRentDue());
		assertFalse(r2.isRentDue());
		r1.rentReminder();
		assertTrue(r1.isRentDue());
	}
	
	public void setUp() {
		h1 = new House(1);
		r1 = new Renter(200.0);
		h2 = new House(2);
		r2 = new Renter(200.0);
		o = new Owner();
		
		h1.setOccupant(r1);
		h1.setItemsWithoutGui();
		h2.setOccupant(r2);
		h2.setItemsWithoutGui();
		
		o.addHouse(h1, r1);
		o.addHouse(h2, r2);
		
		r1.setOwner(o);
		r2.setOwner(o);
	}
}
