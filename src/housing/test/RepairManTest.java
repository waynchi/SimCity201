package housing.test;

import static org.junit.Assert.*;
import java.util.List;
import housing.Apartments;
import housing.House;
import housing.HouseType;
import housing.HousingRepairManRole;
import housing.HousingRepairManRole.HouseState;
import housing.HousingRepairManRole.Location;
import housing.Item;
import housing.interfaces.Resident;
import org.junit.Test;
import people.PeopleAgent;
import people.PeopleAgent.AgentEvent;
import people.Role;

public class RepairManTest {
	Resident r1, r2, r3, r4, r5;
	HousingRepairManRole m;
	PeopleAgent p;
	House hv1, hv2, ha1, ha2, ha3;
	Apartments a1, a2;

	@Test
	public void test1() {
		setUp();
		
		((Role)m).isActive = false;
		((Role)m).msgIsActive();
		
		assertTrue(((Role)m).isActive);
		assertEquals(Location.Nowhere, m.location);
		
		m.pickAndExecuteAnAction();
		
		assertTrue(((Role)m).isActive);
		assertEquals(Location.Shop, m.location);
	}
	
	@Test
	public void test2() {
		setUp();
		
		m.location = Location.Shop;
		m.needHelp(ha1, 20);
		
		assertTrue(m.houseNeedsRepair(ha1));
		assertNull(m.getCurrentHouse());
		assertNull(m.getCurrentLocationHouse());
		
		m.pickAndExecuteAnAction();
		
		assertEquals(Location.OutsideFixing, m.location);
		assertNotNull(m.getCurrentHouse());
		assertNull(m.getCurrentLocationHouse());
		assertEquals(HouseType.Apartment, m.getCurrentHouse().h.type);
		
		p.event = AgentEvent.RepairManArrived;
		
		m.pickAndExecuteAnAction();
		
		assertEquals(Location.Resident, m.location);
		assertEquals(HouseState.Reached, m.getCurrentHouse().s);
		assertEquals(1, ((MockResident)r3).log.size());
		
		m.pickAndExecuteAnAction();
		
		assertEquals(2, ((MockResident)r3).log.size());
		assertEquals(0, m.getCurrentLocationHouse().h.getBrokenItems().size());
		assertNull(m.getCurrentHouse());
		assertNotNull(m.getCurrentLocationHouse());
		assertEquals(HouseState.None, m.getCurrentLocationHouse().s);
		assertEquals(Location.Resident, m.location);
	}
	
	@Test
	public void test3() {
		setUp();
		
		m.location = Location.Shop;
		m.needHelp(ha1, 20);
		m.needHelp(ha2, 20);
		m.pickAndExecuteAnAction();
		p.event = AgentEvent.RepairManArrived;
		m.pickAndExecuteAnAction();
		m.pickAndExecuteAnAction();
		
		assertEquals(2, ((MockResident)r3).log.size());
		assertEquals(0, m.getCurrentLocationHouse().h.getBrokenItems().size());
		assertNull(m.getCurrentHouse());
		assertNotNull(m.getCurrentLocationHouse());
		assertEquals(HouseState.None, m.getCurrentLocationHouse().s);
		assertEquals(Location.Resident, m.location);
		assertEquals(HouseType.Apartment, m.getCurrentLocationHouse().h.type);
		assertEquals(1, m.getCurrentLocationHouse().h.number);
		assertNull(m.getCurrentHouse());
		
		m.pickAndExecuteAnAction();
		
		assertNotNull(m.getCurrentHouse());
		assertNotNull(m.getCurrentLocationHouse());
		assertEquals(HouseState.Reached, m.getCurrentHouse().s);
		assertEquals(1, ((MockResident)r4).log.size());
		assertEquals("RepairMan is here.", ((MockResident)r4).log.getLastLoggedEvent().getMessage());
		
		m.pickAndExecuteAnAction();
		
		assertEquals(2, ((MockResident)r4).log.size());
		assertEquals(0, m.getCurrentLocationHouse().h.getBrokenItems().size());
		assertNull(m.getCurrentHouse());
		assertNotNull(m.getCurrentLocationHouse());
		assertEquals(HouseState.None, m.getCurrentLocationHouse().s);
		assertEquals(Location.Resident, m.location);
		assertEquals(HouseType.Apartment, m.getCurrentLocationHouse().h.type);
		assertEquals(4, m.getCurrentLocationHouse().h.number);
		assertNull(m.getCurrentHouse());
	}
	
	@Test
	public void test4() {
		setUp();
		
		m.location = Location.Shop;
		m.needHelp(ha2, 20);
		m.needHelp(ha3, 20);
		m.pickAndExecuteAnAction();
		p.event = AgentEvent.RepairManArrived;
		m.pickAndExecuteAnAction();
		m.pickAndExecuteAnAction();
		
		assertEquals(2, ((MockResident)r4).log.size());
		assertEquals(0, m.getCurrentLocationHouse().h.getBrokenItems().size());
		assertNull(m.getCurrentHouse());
		assertNotNull(m.getCurrentLocationHouse());
		assertEquals(HouseState.None, m.getCurrentLocationHouse().s);
		assertEquals(Location.Resident, m.location);
		assertEquals(HouseType.Apartment, m.getCurrentLocationHouse().h.type);
		assertEquals(4, m.getCurrentLocationHouse().h.number);
		assertNull(m.getCurrentHouse());
		
		m.pickAndExecuteAnAction();
		
		assertNotNull(m.getCurrentHouse());
		assertNotNull(m.getCurrentLocationHouse());
		assertEquals(Location.OutsideFixing, m.location);
		// Test my person for message in log.
		
		p.event = AgentEvent.RepairManArrived;
		m.pickAndExecuteAnAction();
		
		assertEquals(Location.Resident, m.location);
		assertEquals(HouseState.Reached, m.getCurrentHouse().s);
		assertEquals(1, ((MockResident)r5).log.size());
		assertEquals("RepairMan is here.", ((MockResident)r5).log.getLastLoggedEvent().getMessage());
		
		m.pickAndExecuteAnAction();
		
		assertEquals(2, ((MockResident)r5).log.size());
		assertEquals(0, m.getCurrentLocationHouse().h.getBrokenItems().size());
		assertNull(m.getCurrentHouse());
		assertNotNull(m.getCurrentLocationHouse());
		assertEquals(HouseState.None, m.getCurrentLocationHouse().s);
		assertEquals(Location.Resident, m.location);
		assertEquals(HouseType.Apartment, m.getCurrentLocationHouse().h.type);
		assertEquals(1, m.getCurrentLocationHouse().h.number);
		assertNull(m.getCurrentHouse());
	}
	
	@Test
	public void test5() {
		setUp();
		
		m.location = Location.Shop;
		m.needHelp(ha2, 20);
		m.needHelp(ha3, 20);
		m.pickAndExecuteAnAction();
		p.event = AgentEvent.RepairManArrived;
		m.pickAndExecuteAnAction();
		m.pickAndExecuteAnAction();
		
		assertEquals(2, ((MockResident)r4).log.size());
		assertEquals(0, m.getCurrentLocationHouse().h.getBrokenItems().size());
		assertNull(m.getCurrentHouse());
		assertNotNull(m.getCurrentLocationHouse());
		assertEquals(HouseState.None, m.getCurrentLocationHouse().s);
		assertEquals(Location.Resident, m.location);
		assertEquals(HouseType.Apartment, m.getCurrentLocationHouse().h.type);
		assertEquals(4, m.getCurrentLocationHouse().h.number);
		assertNull(m.getCurrentHouse());
	}
	
	public void setUp() {
		r1 = null;
		r2 = null;
		r3 = null;
		r4 = null;
		r5 = null;
		p = null;
		m = null;
		hv1 = null;
		hv2 = null;
		ha1 = null;
		ha2 = null;
		ha3 = null;
		a1 = null;
		a2 = null;
		
		r1 = new MockResident();
		r2 = new MockResident();
		r3 = new MockResident();
		r4 = new MockResident();
		r5 = new MockResident();
		p = new MockPeopleHousing("Stupid Dick");
		m = new HousingRepairManRole();
		hv1 = new House("R1", 1, HouseType.Villa);
		hv2 = new House("R2", 2, HouseType.Villa);
		a1 = new Apartments("A1");
		a2 = new Apartments("A2");
		
		ha1 = a1.houses.get(0);
		ha2 = a1.houses.get(3);
		ha3 = a2.houses.get(0);
		
		r1.setHouse(hv1);
		r2.setHouse(hv2);
		r3.setHouse(ha1);
		r4.setHouse(ha2);
		r5.setHouse(ha3);
		r1.setRepairMan(m);
		r2.setRepairMan(m);
		r3.setRepairMan(m);
		r4.setRepairMan(m);
		r5.setRepairMan(m);
		
		hv1.setOccupant(r1);
		hv2.setOccupant(r2);
		ha1.setOccupant(r3);
		ha2.setOccupant(r4);
		ha3.setOccupant(r5);
		
		m.addHouse(hv1, r1);
		m.addHouse(hv2, r2);
		m.addHouse(ha1, r3);
		m.addHouse(ha2, r4);
		m.addHouse(ha3, r5);
		m.testModeOn();
		m.setPerson(p);
	}
}
