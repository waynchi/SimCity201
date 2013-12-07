package restaurant_ps;

import restaurant_ps.interfaces.Customer;


	 public class Table {
		public Customer occupiedBy;
		public int tableNumber;

		public Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		public void setOccupant(Customer cust) {
			occupiedBy = cust;
		}

		public void setUnoccupied() {
			occupiedBy = null;
		}

		public Customer getOccupant() {
			return occupiedBy;
		}

		public boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}
