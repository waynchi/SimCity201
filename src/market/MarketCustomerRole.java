package market;

import java.awt.Dimension;
import java.util.List;

import people.Role;

public class MarketCustomerRole extends Role{
	// data
	enum marketCustomerState {IN_MARKET, MADE_ORDER, WAIT_FOR_CHECK, WAIT_FOR_ORDER, PAYING, PAID, DONE};
	enum marketCustomerEvent {NONE, RECEIVED_ORDER, RECEIVED_CHECK, RECEIVED_CHANGE};
	
	MarketEmployeeRole employee = new MarketEmployeeRole();//should know it from PeopleAgent
	MarketCashierRole cashier;
	marketCustomerState state;
	marketCustomerEvent event;
	Dimension location; // should get from PeopleAgent

	// one shopping list at a time
	private	List<Integer> itemsNeeded; //each "item" is a unique ID that corresponds with a certain item.
	private List<Integer> itemsReceived;
	private	double totalDue;

	Boolean isActive;

	
	
	// messages
	public void msgBuy(List<Integer> items){ //From PeopleAgent 
		isActive = true;
		itemsNeeded = items;
		state = marketCustomerState.IN_MARKET;
		getPersonAgent().CallstateChanged();
	}


	public void msgHereIsYourOrder(List<Integer> _itemsReceived) { //from MarketEmployee
		itemsReceived = _itemsReceived;
		event = marketCustomerEvent.RECEIVED_ORDER;
		// need to tell People what we've got
		//if(itemsNeeded.size() != itemsReceived.size() {
			//Didn't receive the right number of items (how to handle?)
		//} else {
		//	customerState = OrderCompleted;
		//}
		getPersonAgent().CallstateChanged();
	}
	

	public void msgHereIsWhatIsDue(double _totalDue, MarketCashierRole c) {
		totalDue = _totalDue;
		cashier = c;
		event = marketCustomerEvent.RECEIVED_CHECK;
		getPersonAgent().CallstateChanged();

	}

	public void msgHereIsChange(double totalChange) {
		getPersonAgent().Money += totalChange;
		event = marketCustomerEvent.RECEIVED_CHANGE;
		//customerState = OrderCompleted;
		//order.totalReceived = totalChange;
		getPersonAgent().CallstateChanged();
	}

	// scheduler
	public boolean pickAndExecuteAnAction(){
		
		if (state == marketCustomerState.IN_MARKET) {
			orderItem();
			return true;
		}
		
		if (state == marketCustomerState.MADE_ORDER && event == marketCustomerEvent.RECEIVED_ORDER) {
			state = marketCustomerState.WAIT_FOR_CHECK;
			return true;
		}
		
		if (state == marketCustomerState.MADE_ORDER && event == marketCustomerEvent.RECEIVED_CHECK) {
			state = marketCustomerState.WAIT_FOR_ORDER;
			return true;
		}
		
		if (state == marketCustomerState.WAIT_FOR_CHECK && event == marketCustomerEvent.RECEIVED_CHECK) {
			state = marketCustomerState.PAYING;
			payBill();
			return true;
		}
		
		if (state == marketCustomerState.WAIT_FOR_ORDER && event == marketCustomerEvent.RECEIVED_ORDER) {
			state = marketCustomerState.PAYING;
			payBill();
			return true;
		}
		
		if (state == marketCustomerState.PAID && event == marketCustomerEvent.RECEIVED_CHANGE) {
			doneShopping();
			return true;
		}
		
		return false;
	}

	//action
	private void orderItem() {
		employee.msgHereIsAnOrder(itemsNeeded, false);
		state = marketCustomerState.MADE_ORDER;
	}
	
	
	/*private void beginScenarioCustomer() {
		DoGoToRestaurant();
		customerState = AtRestaurant;
		employee.msgHereIsAnOrder(order.itemsNeeded,true);

	}

	private void beginScenarioRestaurant() {
		DoGoToRestaurant();
		employee.msgHereIsAnOrder(order.itemsNeeded,false);
	}*/

	private void payBill() {
		//if(this.location == "market") {
		cashier.msgHereIsPayment(this, getPersonAgent().Money);
		//} else {
		//	cashier.msgHereIsPayment(wallet);
		//}
		state = marketCustomerState.PAID;	
	}

	private void doneShopping() {
		getPersonAgent().msgDone(this);
		state = marketCustomerState.DONE;
	}


	// unitilies
	public Boolean isActive() {
		return isActive;
	}

}
