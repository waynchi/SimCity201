package restaurant_vk;

import restaurant_vk.interfaces.Customer;

public class CustomerRestaurantCheck {
	private double amount;
	private String choice;
	private Customer c;
	
	public CustomerRestaurantCheck(Customer c, double amount, String choice) {
		this.amount = amount;
		this.choice = choice;
		this.c = c;
	}
	
	public double getCost() {
		return amount;
	}
	
	public String toString() {
		return "$" + amount + " " + choice + " " + c;
	}
	
	public Customer getCustomer() {
		return c;
	}
}
