package market;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import people.Role;
import restaurant.interfaces.Cashier;
import market.interfaces.MarketCashier;
import market.interfaces.MarketCustomer;

public class MarketCashierRole extends Role implements MarketCashier{

	// data
	private boolean isActive = false;
	private boolean turnActive = false;

	Map<String, Double> priceList = new HashMap<String, Double>();
	double marketMoney = 10000.0;

	private enum checkState {PENDING, SENT, PAID};
	List<Check> checks = new ArrayList<Check>();
	private class Check {
		MarketCustomer customer = null;
		Cashier restaurantCashier = null;
		Map<String, Integer> items = new HashMap<String, Integer>();
		double totalPaid;
		double totalDue;
		checkState state;

		// Check constructor for a regular MarketCustomer
		public Check (MarketCustomer cust, Map<String, Integer> _items) {
			customer = cust;
			items = _items;
			state = checkState.PENDING;
			totalPaid = totalDue = 0.0;
		}

		// Check constructor for a restaurant
		public Check(Cashier _restaurantCashier, Map<String, Integer> _items) {
			restaurantCashier = _restaurantCashier;
			items = _items;
			state = checkState.PENDING;
			totalPaid = totalDue = 0.0;
		}
	}
	
	MarketCashierRole() {
		priceList.put("Steak", 7.99);
		priceList.put("Chicken", 7.99);
		priceList.put("Salad", 2.99);
		priceList.put("Pizza", 3.99);
		priceList.put("Car", 100000.0);
	}



	// messages

	public void msgIsActive() {
		isActive = true;
		turnActive = true;
		getPersonAgent().CallstateChanged();
	}

	public void msgIsInActive() {
		isActive = false;
		getPersonAgent().CallstateChanged();

	}

	// from regular Market Customer
	public void msgHereIsACheck(MarketCustomer customer, Map<String, Integer> items){
		checks.add(new Check(customer, items));
		getPersonAgent().CallstateChanged();
	}

	// from restaurant Cashier
	public void msgHereIsACheck(Cashier restaurantCashier, Map<String, Integer> items) {
		checks.add(new Check(restaurantCashier, items));
		getPersonAgent().CallstateChanged();

	}

	// from regular market customer
	public void msgHereIsPayment(MarketCustomer customer, double totalPaid) {
		for (Check c : checks) {
			if (c.customer == customer) {
				c.state = checkState.PAID;
				c.totalPaid = totalPaid;
				marketMoney += c.totalPaid;
				break;
			}
		}
		getPersonAgent().CallstateChanged();
	}


	// from restaurant cashier
	public void msgHereIsPayment(Double amount, Cashier cashier) {
		for (Check c : checks) {
			if (c.restaurantCashier == cashier) {
				c.state = checkState.PAID;
				c.totalPaid = amount;
				marketMoney += c.totalPaid;
				break;
			}
		}
		getPersonAgent().CallstateChanged();

	}


	// scheduler
	public boolean pickAndExecuteAnAction(){
		if (turnActive) {
			clockIn();
			turnActive = false;
			return true;
		}
		
		for (Check c : checks) {
			if (c.state == checkState.PENDING) {
				computeAndSendCheck(c);
				return true;
			}
		}

		for (Check c : checks) {
			if (c.state == checkState.PAID) {
				giveChangeToCustomer(c);
				return true;
			}
		}

		return false;
	}




	// action
	private void clockIn() {
		getPersonAgent().getMarketEmployee().setCashier(this);
	}
	
	private void computeAndSendCheck(Check check) {
		//compute the total amount
		for (Map.Entry<String,Integer> entry : check.items.entrySet()) {
			check.totalDue += priceList.get(entry.getKey());
		}

		// if check is for restaurant
		if (check.restaurantCashier != null) {
			check.restaurantCashier.msgHereIsWhatIsDue(this, check.totalDue);
		}
		else { // check is for market customer
			check.customer.msgHereIsWhatIsDue(check.totalDue, this);
		}

		check.state = checkState.SENT;
	}


	private void giveChangeToCustomer(Check check) {
		double change = check.totalPaid - check.totalDue;
		marketMoney -= change;
		if (check.restaurantCashier != null) {
			check.restaurantCashier.msgHereIsChange (change);
		}
		
		else { // check is for regular customer
			check.customer.msgHereIsChange(change);
		}
		checks.remove(check);
	}

	//utilities
	public Boolean isActive() {
		return isActive;
	}


}
