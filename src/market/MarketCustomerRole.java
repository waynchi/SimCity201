package market;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import market.interfaces.MarketCashier;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketEmployee;
import people.Role;

public class MarketCustomerRole extends Role implements MarketCustomer{
	// data
	enum marketCustomerState {IN_MARKET, MADE_ORDER, WAITING_FOR_CHECK, WAITING_FOR_ORDER, PAYING, PAID, DONE};
	enum marketCustomerEvent {NONE, RECEIVED_ORDER, RECEIVED_CHECK, RECEIVED_CHANGE};
	
	MarketEmployee employee;//should know it from PeopleAgent
	MarketCashier cashier;
	marketCustomerState state;
	marketCustomerEvent event;
	Dimension location; // should get from PeopleAgent

	// one shopping list at a time
	private	Map<String, Integer> itemsNeeded = new HashMap<String, Integer>();
	private	Map<String, Integer> itemsReceived = new HashMap<String, Integer>();
	private	double totalDue;

	Boolean isActive;

	
	
	// messages
	public void msgBuy(Map<String,Integer> items){ //From PeopleAgent 
		isActive = true;
		itemsNeeded = items;
		state = marketCustomerState.IN_MARKET;
		getPersonAgent().CallstateChanged();
	}


	public void msgHereIsYourOrder(Map<String, Integer> _itemsReceived) { //from MarketEmployee
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
	

	public void msgHereIsWhatIsDue(double _totalDue, MarketCashier c) {
		totalDue = _totalDue;
		cashier = c;
		event = marketCustomerEvent.RECEIVED_CHECK;
		getPersonAgent().CallstateChanged();

	}

	public void msgHereIsChange(double totalChange) {
		//getPersonAgent().Money += totalChange;
		event = marketCustomerEvent.RECEIVED_CHANGE;
		getPersonAgent().CallstateChanged();
	}

	// scheduler
	public boolean pickAndExecuteAnAction(){
		
		if (state == marketCustomerState.IN_MARKET) {
			orderItem();
			return true;
		}
		
		if (state == marketCustomerState.MADE_ORDER && event == marketCustomerEvent.RECEIVED_ORDER) {
			state = marketCustomerState.WAITING_FOR_CHECK;
			return true;
		}
		
		if (state == marketCustomerState.MADE_ORDER && event == marketCustomerEvent.RECEIVED_CHECK) {
			state = marketCustomerState.WAITING_FOR_ORDER;
			return true;
		}
		
		if (state == marketCustomerState.WAITING_FOR_CHECK && event == marketCustomerEvent.RECEIVED_ORDER) {
			state = marketCustomerState.PAYING;
			payBill();
			return true;
		}
		
		if (state == marketCustomerState.WAITING_FOR_ORDER && event == marketCustomerEvent.RECEIVED_CHECK) {
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
		employee.msgHereIsAnOrder(this, itemsNeeded);
		state = marketCustomerState.MADE_ORDER;
	}
	

	private void payBill() {
		cashier.msgHereIsPayment(this, getPersonAgent().getMoney());
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
