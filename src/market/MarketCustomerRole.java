package market;

import java.util.List;

import people.Role;

public class MarketCustomerRole extends Role{
	// data
	enum marketCustomerState {IN_MARKET, MADE_ORDER, PAYING, DONE};
	enum marketCustomerEvent {NONE, RECEIVED_ORDER, RECEIVED_CHANGE};
	
	MarketEmployeeRole employee;
	marketCustomerState state;
	marketCustomerEvent event;

	Order order;
	//double wallet;
	class Order {
		List<Integer> itemsNeeded; //each "item" is a unique ID that corresponds with a certain item.
		//Waiter w;
		double totalDue;
		//double totalReceived;
		
		public Order (List<Integer> items) {
			itemsNeeded = items;
			totalDue = 0.0;
		}
		
	}

	Boolean isActive;

	// messages
	public void msgBuy(List<Integer> itemsNeeded){ //From PeopleAgent 
		isActive = true;

		order = new Order(itemsNeeded);
		state = marketCustomerState.IN_MARKET;
		getPersonAgent().CallstateChanged();
	}

	public void msgHereIsWhatIsDue(totalDue) {
		order.totalDue = totalDue;
		customerState = Paying;
	}

	public void msgHereIsYourOrder(List<Integer>itemsReceived) {
		if(itemsNeeded.size() != itemsReceived.size() {
			//Didn't receive the right number of items (how to handle?)
		} else {
			customerState = OrderCompleted;
		}
	}

	public void msgHereIsChange(double totalChange) {
		customerState = OrderCompleted;
		order.totalReceived = totalChange;
	}

	// scheduler
	public void pickAndExecuteAnAction{
		if(state = MarketCustomer) {
			beginScenarioCustomer();
		}
		if(customerState = GotHungryRestarant) {
			beginScenarioRestaurant();
		}

		if(customerState = Paying) {
			payBill();
		}
		if(customerState = OrderCompleted) {
			returnHome();
		}
	}

	//action
	private void beginScenarioCustomer() {
		DoGoToRestaurant();
		customerState = AtRestaurant;
		employee.msgHereIsAnOrder(order.itemsNeeded,true);

	}

	private void beginScenarioRestaurant() {
		DoGoToRestaurant();
		employee.msgHereIsAnOrder(order.itemsNeeded,false);
	}

	private void payBill() {
		if(this.location == "market") {
			employee.msgHereIsPayment(order.totalDue);
		} else {
			employee.msgHereIsPayment(wallet);
		}
		customerState = Paid;	
	}

	private void returnHome() {
		wallet = wallet + order.totalReceived;
		DoGoHome();
		CustomerState = LeftRestaurant;
	}


	// unitilies
	public Boolean isActive() {
		return isActive;
	}

}
