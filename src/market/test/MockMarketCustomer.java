package market.test;

import java.util.Map;

import people.People;
import restaurant.test.mock.LoggedEvent;
import market.interfaces.MarketCashier;
import market.interfaces.MarketCustomer;

public class MockMarketCustomer extends Mock implements MarketCustomer {

	public MockMarketCustomer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHereIsYourOrder(Map<String, Integer> items) {
		log.add(new LoggedEvent("received msgHereIsYourOrder"));				
	}

	@Override
	public void msgHereIsWhatIsDue(double totalDue, MarketCashier marketCashier) {
		log.add(new LoggedEvent("received msgHereIsWhatIsDue from market cashier"));		
	}

	@Override
	public void msgHereIsChange(double change) {
		log.add(new LoggedEvent("received msgHereIsChange from market cashier"));				
	}

	@Override
	public People getPerson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgBuy(Map<String, Integer> items) {
		// TODO Auto-generated method stub
		
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
	public void msgAtCounter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtExit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtRegister() {
		// TODO Auto-generated method stub
		
	}

}
