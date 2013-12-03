package restaurant_vk.test.mock;

import restaurant_vk.interfaces.Market;

public class MockMarket extends Mock implements Market{
	
	private String name = null;
	public EventLog log = new EventLog();
	
	public MockMarket(String name) {
		super(name);
		this.name = name;
	}

	@Override
	public void runningLow(String food, int qty) {
	}

	@Override
	public void hereIsMoney(double cash) {
		log.add(new LoggedEvent("Received $" + cash + " from cashier"));
	}
	
	public String toString() {
		return name;
	}
}
