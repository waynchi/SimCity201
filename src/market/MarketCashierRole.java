package market;

import java.util.List;

public class MarketCashierRole {

	// data
	enum checkState {PENDING, SENT, PAID, DONE};
	List<Check> checks;
	class Check {
		MarketCustomerRole customer;
		double totalPaid;
		double totalDue;
		
		public Check (MarketCustomerRole cust, double due) {
			customer = cust;
			totalDue = due;
		}
	}
	double change;
	private Boolean isActive;
	
	// messages
	public void msgHereIsACheck(MarketCustomerRole customer, List<Integer> items){
		double totalDue = 0;
		for (int item : items) {
			totalDue = totalDue + itemPrice.price; //itemPrice is a map
		}
		checks.add(new Check(customer, totalDue));
	}
	
	
	public void msgHereIsPayment(MarketCustomerRole cust, double totalPaid) {
        if there exists Check c in checks such that c.customer = cust
        		c.totalPaid = totalPaid;
        		c.state = paid;
}
	
	
	// scheduler
	public void pickAndExecuteAnAction(){
		if (there exists check c in checks such that c.state = pending) {
			askCustomerToPay(c);
		}
		
		if (there exists check c in checks such that c.state = paid) {
			giveChangeToCustomer(c);
		}
	}
	
	
	// action
	private void askCustomerToPay(Check check) {
		check.customer.msgHereIsWhatIsDue(c.totalDue, this);
		check.state = SENT;
	}
	
	
	private void giveChangeToCustomer(Check check) {
        change = check.totalPaid - check.totalDue;
        check.customer.msgHereIsChange(change);
        checks.remove(check);
}
	
	//utilities
	public Boolean isActive() {
		return isActive;
	}

	
}
