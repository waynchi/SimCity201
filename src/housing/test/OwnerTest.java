package housing.test;

import static org.junit.Assert.*;
import housing.House;
import housing.interfaces.Owner;
import housing.interfaces.Renter;
import housing.interfaces.Resident;
import org.junit.Test;
import people.People;
import people.PeopleAgent;

public class OwnerTest {
	Owner o;
	Renter r1;
	Renter r2;
	Renter r3;
	House h1;
	House h2;
	House h3;
	
	@Test
	public void test1() {
		setUp();
	}
	
	public void setUp() {
		h1 = null;
		h2 = null;
		r1 = null;
		r2 = null;
		o = null;
		
		h1 = new House("R1Residence", 1);
		r1 = new MockRenter();
		h2 = new House("R2Residence", 2);
		r2 = new MockRenter();
		h3 = new House("R3Residence", 3);
		r3 = new MockRenter();
		
		h1.setOccupant((Resident)r1);
		h1.setItemsWithoutGui();
		h2.setOccupant((Resident)r2);
		h2.setItemsWithoutGui();
		h3.setOccupant((Resident)r3);
		h3.setItemsWithoutGui();
		
		o.addHouse(h1, r1);
		o.addHouse(h2, r2);
		o.addHouse(h3, r3);
		
		r1.setOwner(o);
		((Resident)r1).setHouse(h1);
		r1.setOwner(o);
		((Resident)r2).setHouse(h2);
		r1.setOwner(o);
		((Resident)r3).setHouse(h3);
	}
}
