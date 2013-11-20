package housing.test;

import static org.junit.Assert.*;

import java.util.List;

import housing.House;
import housing.RepairManRole;
import housing.ResidentRole;
import housing.Item;

import org.junit.Test;

import people.People;
import people.PeopleAgent;

public class ResidentRepairManTest {

	Resident r1;
	Resident r2;
	RepairMan r;
	People p = new PeopleAgent();
	ResidentRole r1;
	ResidentRole r2;
	RepairManRole r;
	PeopleAgent p = new PeopleAgent();
	House h1;
	House h2;
	List<Item> h1Items;
	List<Item> h2Items;
	
	@Test
	public void test1() {
		setUp();
		
		h1Items.get(0).breakIt();
		assertFalse(r.doesItNeedRepair(h1));
		assertEquals(1, r1.getBrokenItems().size());
		assertFalse(r.doesItNeedRepair(h1));
		
		r1.pickAndExecuteAnAction();
		
		assertTrue(r.doesItNeedRepair(h1));
		assertFalse(r.anyCurrentHouse());
		
		r.pickAndExecuteAnAction();
		
		assertTrue(r.anyCurrentHouse());
		assertFalse(r.doesItNeedRepair(h1));
		
		r1.pickAndExecuteAnAction();
	}
	
	@Test
	public void test2() {
		setUp();
	}
	
	@Test
	public void test3() {
		setUp();
	}
	
	public void setUp() {
		h1 = null;
		h2 = null;
		r1 = null;
		r2 = null;
		r = null;
		h1Items = null;
		h2Items = null;
		
		h1 = new House("R1Residence", 1);
		r1 = new ResidentRole();
		h2 = new House("R2Residence", 2);
		r2 = new ResidentRole();
		r = new RepairManRole();
		r.setPerson(p);
		
		h1.setOccupant(r1);
		h1.setItemsWithoutGui();
		h2.setOccupant(r2);
		h2.setItemsWithoutGui();
		
		r1.setRepairMan(r);
		r1.setHouse(h1);
		r2.setRepairMan(r);
		r2.setHouse(h2);
		
		r.addHouse(h1, r1);
		r.addHouse(h2, r2);
		
		h1Items = h1.items;
		h2Items = h2.items;
	}
}
