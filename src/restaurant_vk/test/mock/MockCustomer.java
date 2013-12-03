package restaurant_vk.test.mock;

import java.util.ArrayList;
import java.util.List;
import restaurant_vk.CustomerRestaurantCheck;
import restaurant_vk.Menu;
import restaurant_vk.gui.CustomerGui;
import restaurant_vk.interfaces.Customer;
import restaurant_vk.interfaces.Waiter;

public class MockCustomer extends Mock implements Customer{
	
	private String name = null;
	public EventLog log = new EventLog();
	public List<CustomerRestaurantCheck> checks = new ArrayList<CustomerRestaurantCheck>();
	public List<CustomerRestaurantCheck> approvedChecks = new ArrayList<CustomerRestaurantCheck>();
	public double cashReceived = 0.0;

	public MockCustomer(String name) {
		super(name);
		this.name = name;
	}

	@Override
	public void gotHungry() {
	}

	@Override
	public void followMeToTable(Menu m) {
	}

	@Override
	public void whatWouldYouLike() {
	}

	@Override
	public void hereIsYourFood() {
	}

	@Override
	public void msgDecideChoice(String choice) {
	}

	@Override
	public void msgWantToLeave() {
	}

	@Override
	public void outOfChoice(Menu m, String choice) {
	}

	@Override
	public void hereIsCheck(CustomerRestaurantCheck c) {
	}

	@Override
	public void hereIsChangeAndApprovedPayments(double change, List<CustomerRestaurantCheck> approvedPayments) {
		log.add(new LoggedEvent("Received $" + change + " and " + approvedPayments.size() + " approved payments."));
		this.approvedChecks = approvedPayments;
		for (CustomerRestaurantCheck c : approvedPayments) {
			checks.remove(c);
		}
		cashReceived = change;
	}

	@Override
	public void tablesAreFull() {
	}

	@Override
	public boolean isReadyToSit() {
		return true;
	}

	@Override
	public boolean canLeaveWhileWaiting() {
		return false;
	}

	@Override
	public void setWaiter(Waiter w) {
	}

	@Override
	public CustomerGui getGui() {
		return null;
	}
	
	public String toString() {
		return name;
	}

}
