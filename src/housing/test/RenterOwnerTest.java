package housing.test;

import housing.House;
import housing.Owner;
import housing.Renter;

public class RenterOwnerTest {
	House h1;
	House h2;
	Renter r1;
	Renter r2;
	Owner o;
	
	public void setUp() {
		h1 = new House(1);
		r1 = new Renter(200.0);
		h2 = new House(2);
		r2 = new Renter(200.0);
		o = new Owner();
		h1.setOccupant(r1);
		o.addHouse(h1, r1);
	}
}
