package market;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import people.Role;
import market.interfaces.MarketCashier;
import market.interfaces.MarketCustomer;

public class MarketCashierRole extends Role implements MarketCashier{

	// data
	private Boolean isActive = false;
	Map<String, Double> priceList = new HashMap<String, Double>();
	enum checkState {PENDING, SENT, PAID};
	List<Check> checks = new ArrayList<Check>();
	class Check {
		MarketCustomer customer;
		Map<String, Integer> items = new HashMap<String, Integer>();
		double totalPaid;
		double totalDue;
		checkState state;
		
		public Check (MarketCustomer cust, Map<String, Integer> _items) {
			customer = cust;
			items = _items;
			state = checkState.PENDING;
			totalPaid = totalDue = 0.0;
		}
	}
	
	// messages
	
	public void msgIsActive() {
		isActive = true;
		getPersonAgent().CallstateChanged();
	}
	
	public void msgHereIsACheck(MarketCustomer customer, Map<String, Integer> items){
		checks.add(new Check(customer, items));
		getPersonAgent().CallstateChanged();
	}
	
	
	public void msgHereIsPayment(MarketCustomer customer, double totalPaid) {
        for (Check c : checks) {
        	if (c.customer == customer) {
        		c.state = checkState.PAID;
        		c.totalPaid = totalPaid;
        		break;
        	}
        }
        getPersonAgent().CallstateChanged();
}
	
	
	// scheduler
	public boolean pickAndExecuteAnAction(){
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
	
	private void computeAndSendCheck(Check check) {
		//compute the total amount
		for (Map.Entry<String,Integer> entry : check.items.entrySet()) {
			check.totalDue += priceList.get(entry.getKey());
		}
		
		check.customer.msgHereIsWhatIsDue(check.totalDue, this);
		check.state = checkState.SENT;
	}
	
	
	private void giveChangeToCustomer(Check check) {
        double change = check.totalPaid - check.totalDue;
        check.customer.msgHereIsChange(change);
        checks.remove(check);
}
	
	//utilities
	public Boolean isActive() {
		return isActive;
	}

	
}
