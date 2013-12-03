package restaurant_vk.test.mock;

import java.util.ArrayList;
import java.util.List;
import restaurant_vk.CustomerRestaurantCheck;
import restaurant_vk.interfaces.Customer;
import restaurant_vk.interfaces.Waiter;

public class MockWaiter extends Mock implements Waiter{
	
	private String name = null;
	public EventLog log = new EventLog();
	List<CustomerRestaurantCheck> checks = new ArrayList<CustomerRestaurantCheck>();

	public MockWaiter(String name) {
		super(name);
		this.name = name;
	}

	@Override
	public void sitAtTable(Customer c, int table) {
	}

	@Override
	public void readyToOrder(Customer c) {
	}

	@Override
	public void hereIsMyChoice(String item, Customer c) {
	}

	@Override
	public void orderIsReady(String choice, int table) {
	}

	@Override
	public void doneEatingAndLeaving(Customer c) {
	}

	@Override
	public void wantBreak() {
	}

	@Override
	public void getBackToWork() {
	}

	@Override
	public void noBreak() {
	}

	@Override
	public void takeABreak() {
	}

	@Override
	public void outOf(String choice, int table) {
	}

	@Override
	public void hereIsCheck(CustomerRestaurantCheck ch, Customer c) {
		log.add(new LoggedEvent("New check " + ch.toString() + " received"));
		checks.add(ch);
	}

	@Override
	public void leavingWithoutEating(Customer c) {
	}
	
	public String toString() {
		return name;
	}
	
	public CustomerRestaurantCheck getCheck(Customer c) {
		for (CustomerRestaurantCheck ch : checks) {
			if (ch.getCustomer() == c) {
				checks.remove(ch);
				return ch;
			}
		}
		return null;
	}
	
}
