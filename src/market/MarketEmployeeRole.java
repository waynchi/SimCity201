package market;

import java.util.List;

public class MarketEmployeeRole {
	// data
	enum orderState {New,OrderFulfilled,ReadyToPay,CheckDelivered,PaymentReceived,OrderPaid,DeliveringChange};
	List<MarketTruckRole> trucks;
	List<Order> orders;
	//List<Checks> checks;

	class Order {
		orderState state;
		List<Item> items;
		double totalDue;
		double totalPaid;
		double totalChange;
		MarketCustomerRole customer;
		boolean customerAtRestaurant;
	}

	class Item {
		String name;
		double price;
	}
	
	private Boolean isActive;
	private MarketCashierRole cashier;


	// messages
	public void msgHereIsAnOrder(List<Integer>chosenItems, boolean isRestaurant) {
		/*newOrder = new Order(order);
		newOrder.state = orderState.New;
		for(item in chosenItems) {
			newOrder.items.add(item);
		}
		newOrder.customerAtRestaurant = isAtRestaurant;
		orders.add(newOrder);*/
	}


	public void msgHereIsPayment(double amount, order) {
		order.totalPaid = order.totalPaid;
		order.state = orderState.PaymentReceived;		
	}

	public void msgHereIsChange(double change, order) {
		order.totalChange = change;
		order.state = orderState.DeliveringChange;
	}

	// scheduler
	public boolean pickAndExecuteAnAction() {
		for (Order o : orders) {
			if (order.state = orderState.New) {
				getOrder(order);
			}
			if (order.state = orderState.OrderFulfilled) {
				giveOrderToCustomer(order);
			}
			if (order.state = orderState.ReadyToPay) {
				giveCheckToCustomer(order);
			}
			if (order.state = orderState.PaymentReceived) {
				getChangeFromCashier(order);
			}
			if(order.state = orderState.DeliveringChange) {
				deliverChangeToCustomer(order);
			}
		}
	}


	// action
	private void getOrder(Order order) { //gui
		for item in order.items {
			DoGetItem(order.posX,order.posY);
		}
		order.state = OrderFulfilled;

	}

	
	// if customer is at the Market, give it the items; otherwise send a truck to its place
	private void giveOrderToCustomer(Order order) {
		//DoGoToCustomer(); //gui
		if(order.customerAtRestaurant) {
			order.customer.msgHereIsYourOrder(order.items);
		} 
		else {
			for truck in trucks where truck is available {
				truck.msgHereIsAnOrder(order.items,order.customer.location);
			}
		}
		order.state = ReadyToPay;
	}


	private void giveBillToCashier(Order order) {
		order.totalDue = totalDue;
		//DoGoToCashier();
		cashier.msgHereIsACheck(order.customer, order.items);
		
		orders.remove(order);
	}

	//utilities
	public Boolean isActive() {
		return isActive;
	}

	public void setCashier(MarketCashierRole c) {
		cashier = c;
	}
	
}





