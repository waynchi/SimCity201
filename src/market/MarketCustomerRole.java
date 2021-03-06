package market;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import market.gui.AnimationPanel;
import market.gui.MarketCustomerGui;
import market.gui.MarketGui;
import market.interfaces.MarketCashier;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketEmployee;
import people.People;
import people.Role;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;

public class MarketCustomerRole extends Role implements MarketCustomer{
	// data
	public EventLog log = new EventLog();
	public boolean inTest = false;

	enum marketCustomerState {IN_MARKET, MADE_ORDER, WAITING_FOR_CHECK, WAITING_FOR_ORDER, PAYING, PAID, DONE};
	enum marketCustomerEvent {NONE, RECEIVED_ORDER, RECEIVED_CHECK, RECEIVED_CHANGE};

	MarketGui marketGui = null;
	MarketCustomerGui customerGui = null;

	private MarketEmployee employee = null;//should know it from PeopleAgent
	MarketCashier cashier;
	marketCustomerState state;
	marketCustomerEvent event;
	Dimension location; // should get from PeopleAgent

	Semaphore atCounter = new Semaphore(0,true);
	Semaphore atRegister = new Semaphore(0,true);
	Semaphore atExit = new Semaphore(0,true);

	// one shopping list at a time
	private	Map<String, Integer> itemsNeeded = new HashMap<String, Integer>();
	private	Map<String, Integer> itemsReceived = new HashMap<String, Integer>();
	private	double totalDue;

	//constructor
	public MarketCustomerRole(MarketGui gui){
		this.marketGui = gui;
		customerGui = new MarketCustomerGui(this);
		marketGui.getAnimationPanel().addGui(customerGui);
		customerGui.setPresent(false);
	}

	// messages
	public void msgIsActive () {
		print("received msgIsActive");
		log.add(new LoggedEvent("received msgIsActive"));
		isActive = true;
		customerGui.setPresent(true);
		itemsNeeded.put("Car", 1);
		state = marketCustomerState.IN_MARKET;
		if (!inTest){
			employee = (MarketEmployeeRole) (getPersonAgent().getMarketEmployee(0));
		}
		getPersonAgent().CallstateChanged();
	}

	public void msgAtCounter () {
		atCounter.release();
		getPersonAgent().CallstateChanged();
	}

	public void msgAtRegister() {
		atRegister.release();
		getPersonAgent().CallstateChanged();
	}

	public void msgAtExit() {
		atExit.release();
		getPersonAgent().CallstateChanged();
	}

	public void msgBuy(Map<String,Integer> items){ //From PeopleAgent 
		isActive = true;
		itemsNeeded = items;
		state = marketCustomerState.IN_MARKET;
		getPersonAgent().CallstateChanged();
	}


	public void msgHereIsYourOrder(Map<String, Integer> _itemsReceived) { //from MarketEmployee
		print("received my item");
		log.add(new LoggedEvent("received my item "));
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
		print("got check for my order");
		log.add(new LoggedEvent("got check for my order and total amount is " + _totalDue));
		totalDue = _totalDue;
		cashier = c;
		event = marketCustomerEvent.RECEIVED_CHECK;
		getPersonAgent().CallstateChanged();

	}

	public void msgHereIsChange(double totalChange) {
		print("got change for my purchase");
		log.add(new LoggedEvent("received change from cashier and amount is " + totalChange));
		double money = getPersonAgent().getMoney();
		money += totalChange;
		getPersonAgent().setMoney(money);
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

		if (state == marketCustomerState.WAITING_FOR_CHECK && event == marketCustomerEvent.RECEIVED_CHECK) {
			state = marketCustomerState.PAYING;
			payBill();
			return true;
		}

		if (state == marketCustomerState.WAITING_FOR_ORDER && event == marketCustomerEvent.RECEIVED_ORDER) {
			state = marketCustomerState.PAYING;
			payBill();
			return true;
		}

		if (state == marketCustomerState.PAID && event == marketCustomerEvent.RECEIVED_CHANGE) {
			done();
			return true;
		}

		return false;
	}

	//action
	private void orderItem() {
		if (!inTest){
			//customerGui.DoLineUp();
			customerGui.DoGoToMarketEmployee();
			try {
				atCounter.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		print("ordering my itmes");
		log.add(new LoggedEvent("ordering my items"));
		employee.msgHereIsAnOrder(this, itemsNeeded);
		state = marketCustomerState.MADE_ORDER;
	}


	private void payBill() {
		if (!inTest){
			customerGui.DoGoToRegister();
			try {
				atRegister.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		print("making payment to market cashier");
		log.add(new LoggedEvent("making payment to market cashier"));
		cashier.msgHereIsPayment(this, getPersonAgent().getMoney());
		getPersonAgent().setMoney(0.0);
		state = marketCustomerState.PAID;	
	}

	private void done() {
		print("done and leaving market");
		log.add(new LoggedEvent("done and leaving market"));
		if (!inTest){
			customerGui.DoGoToExit();
			try {
				atExit.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		state = marketCustomerState.DONE;
		getPersonAgent().msgDone("MarketCustomerRole");
		isActive = false;
		customerGui.setPresent(false);
	}


	// unitilies
	public Boolean isActive() {
		return isActive;
	}

	@Override
	public People getPerson() {
		return getPersonAgent();
	}

	public String getName() {
		return getPersonAgent().getName();
	}

	public void setEmployee(MarketEmployee e) {
		employee = e;
	}

	public String getState() {
		return state.toString();
	}

	public String getEvent() {
		return event.toString();
	}

	public AnimationPanel getAnimationPanel () {
		return marketGui.getAnimationPanel();
	}
}
