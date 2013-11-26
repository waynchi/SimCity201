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
	Resident r1;
	Resident r2;
	Resident r3;
	Resident r4;
	Resident r5;
	HousingRepairManRole m;
	PeopleAgent p;
	House h1;
	House h2;
	Apartments a1;
	Apartments a2;

	@Test
	public void test() {
	}
	
	public void setUp() {
		r1 = null;
		r2 = null;
		r3 = null;
		r4 = null;
		r5 = null;
		p = null;
		m = null;
		h1 = null;
		h2 = null;
		a1 = null;
		a2 = null;
		
		r1 = new MockResident();
		r2 = new MockResident();
		r3 = new MockResident();
		r4 = new MockResident();
		r5 = new MockResident();
		p = new MockPeopleHousing("Stupid Dick");
		m = new HousingRepairManRole();
		h1 = new House("R1", 1, HouseType.Villa);
		h2 = new House("R2", 2, HouseType.Villa);
		a1 = new Apartments("A1");
		a2 = new Apartments("A2");
	}
}
