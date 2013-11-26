package housing.test;

import static org.junit.Assert.*;
import java.util.List;
import housing.Apartments;
import housing.House;
import housing.HouseType;
import housing.HousingRepairManRole;
import housing.Item;
import housing.interfaces.Resident;
import org.junit.Test;
import people.PeopleAgent;

public class RepairManTest {
	Resident r1, r2, r3, r4, r5;
	HousingRepairManRole m;
	PeopleAgent p;
	House hv1, hv2, ha1, ha2, ha3;
	Apartments a1, a2;

	@Test
	public void test() {
		setUp();
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
		
		hv1.setOccupant(r1);
		hv2.setOccupant(r2);
		ha1.setOccupant(r3);
		ha2.setOccupant(r4);
		ha3.setOccupant(r5);
	}
}
