package market.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import people.People;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import market.interfaces.MarketCashier;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketEmployee;

public class MockMarketEmployee extends Mock implements MarketEmployee{

	MarketCashier cashier = null;
	public List<People> workers = new ArrayList<People>();
	
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
	public void msgHereIsAnOrder(Map<String, Integer> order, Cook cook, Cashier cashier) {
		log.add(new LoggedEvent("received msgOrder from restaurant cook"));				
	}

	@Override
	public void setCashier(MarketCashier marketCashier) {
		cashier =  marketCashier;
		if (marketCashier.getPersonAgent()!=null ) {
			if (!workers.contains(marketCashier.getPersonAgent())){
				workers.add(cashier.getPersonAgent());
			}
		}
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

	@Override
	public MarketCashier getCashier() {
		return cashier;
		// TODO Auto-generated method stub
	}

	@Override
	public void msgOrderDelivered(int orderNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderNotDelivered(int orderNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<People> getWorkers() {
		// TODO Auto-generated method stub
		return workers;
	}

}
