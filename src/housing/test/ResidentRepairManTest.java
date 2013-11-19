package housing.test;

import static org.junit.Assert.*;
import housing.House;
import housing.RepairMan;
import housing.Resident;
import housing.Item;

import org.junit.Test;

public class ResidentRepairManTest {

	Resident r1;
	Resident r2;
	RepairMan r;
	House h1;
	House h2;
	
	@Test
	public void test() {
		setUp();
	}
	
	public void setUp() {
		h1 = new House("R1Residence", 1);
		r1 = new Resident();
		h2 = new House("R2Residence", 2);
		r2 = new Resident();
		
		h1.setOccupant(r1);
		h1.setItemsWithoutGui();
		h1.setItemsWithoutGui();
		h2.setOccupant(r2);
		h2.setItemsWithoutGui();
		h2.setItemsWithoutGui();
		
		r1.setRepairMan(r);
		r1.setHouse(h1);
		r2.setRepairMan(r);
		r2.setHouse(h2);
		
		r.addHouse(h1, r1);
		r.addHouse(h2, r2);
	}
}
