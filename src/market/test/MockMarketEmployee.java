package market.test;

import java.util.Map;

import people.Role;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import restaurant.test.mock.LoggedEvent;
import market.interfaces.MarketCashier;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketEmployee;

public class MockMarketEmployee extends Mock implements MarketEmployee{

	public MockMarketEmployee(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHereIsAnOrder(MarketCustomer marketCustomerRole,
			Map<String, Integer> itemsNeeded) {
		log.add(new LoggedEvent("received msgHereIsAnOrder from market customer"));				
	}

	@Override
	public void msgOrder(Map<String, Integer> order, Cook cook, Cashier cashier) {
		log.add(new LoggedEvent("received msgOrder from restaurant cook"));				
	}

	@Override
	public void setCashier(MarketCashier marketCashier) {
	}

	@Override
	public void msgIsActive() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIsInActive() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public void msgAtCabinet() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtCounter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtExit() {
		// TODO Auto-generated method stub
		
	}

}
