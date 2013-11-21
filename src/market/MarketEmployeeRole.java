package market;

import java.util.List;

public class MarketEmployeeRole {
	// data
	enum orderState = {New,OrderFulfilled,ReadyToPay,CheckDelivered,PaymentReceived,OrderPaid,DeliveringChange};
	List<MarketTruckRole> trucks;
	List<Order> orders;
	List<Checks> checks;
	
	class Order {
		enum state;
		List<Item> items;
		double totalDue;
		double totalPaid;
		double totalChange;
		Customer customer;
		boolean customerAtRestaurant;
	};
	
	class Item {
		String name;
		double price;
	};
	
	
	// messages
	public void msgHereIsAnOrder (List<Integer>chosenItems, boolean isAtRestaurant) {
		newOrder = new Order(order);
		newOrder.state = orderState.New;
		for(item in chosenItems) {
			newOrder.items.add(item);
		}
		newOrder.customerAtRestaurant = isAtRestaurant;
		orders.add(newOrder);
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
	for order in orders {
		if order.state = orderState.New {
			getOrder(order);
		}
		if order.state = orderState.OrderFulfilled {
			giveOrderToCustomer(order);
		}
		if order.state = orderState.ReadyToPay {
			giveCheckToCustomer(order);
		}
		if order.state = orderState.PaymentReceived {
			getChangeFromCashier(order);
		}
		if(order.state = orderState.DeliveringChange) {
			deliverChangeToCustomer(order);
		}
	}
	
	
	// action
	getOrder(Order order) {
		for item in order.items {
			DoGetItem(order.posX,order.posY);
		}
		order.state = OrderFulfilled;

	}

	giveOrderToCustomer(order) {
		DoGoToCustomer();
		if(order.customerAtRestaurant == true) {
			order.customer.msgHereIsYourOrder(order.items);
			order.state = ReadyToPay;

		} 
		else {
			for truck in trucks where truck is available {
				truck.msgHereIsAnOrder(order.items,this.location);

			}
			order.state = ReadyToPay;
		}
	}


	giveCheckToCustomer(Order order) {
		for item in order.items {
			double totalDue = totalDue + item.price;
		}
		order.totalDue = totalDue;
		DoGoToCashier();
		order.customer.msgHereIsWhatIsDue(order.customer,totalDue);
		order.state = CheckDelivered;
	}
	getChangeFromCashier(order) {
		DoGoToCashier();
		cashier.msgHereIsPayment(order.totalPaid, order.totalDue, this);
		order.state = OrderPaid;
	}
	deliverChangeToCustomer(order) {
		DoGoToCustomer();
		order.customer.msgHereIsChange(order.totalChange);
		orders.remove(order);
	}
	
	
	
}
