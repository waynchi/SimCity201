package market;

public class MarketCustomerRole {
	// data
	enum customerState = {GotHungry,AtRestaurant,Paying,Paid,OrderCompleted,LeftRestaurant};
	Order order;
	double wallet;
	class Order {
		List<Integer> itemsNeeded; //each "item" is a unique ID that corresponds with a certain item.
		Waiter w;
		double totalDue;
		double totalReceived;
	}
	MarketEmployeeRole employee;

	// messages
	public void msgGotHungry(List<Integer> itemsNeeded){ //From Gui 
	order = new Order(itemsNeeded);
	if(this.location == "market") {
		customerState = GotHungryCustomer;
	} else {
		customerState = GotHungryRestaurant;
	}
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
	if(customerState = GotHungryCustomer) {
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

}
