package people.test.mock;

import people.Role;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

public class MockWaiter extends Role implements Waiter{

	private Cashier cashier = null;

	public MockWaiter(String name) {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void setCashier(Cashier _cashier) {
		cashier = _cashier;
	}
		
	public void msgAtTable() {
		//log.add(new LoggedEvent("at table"));
	}
	
	public void msgAtCook() {
		//log.add(new LoggedEvent("at cook"));
	}
	
	public void msgAtCashier() {
		//log.add(new LoggedEvent("at cashier"));
	}
	
	public void msgAtWaitingCustomer() {
		//log.add(new LoggedEvent("at customer waiting area"));
	}
	
	public void msgAskForBreak(){
		//log.add(new LoggedEvent("go on break button in control panel is pressed"));
	}
	
	public void msgOffBreak(){
		//log.add(new LoggedEvent("go off break in control panel is pressed"));
	}
	
	public void msgBreakApproved(){
		//log.add(new LoggedEvent("Received message msgBreakApproved from host. Break request approved"));
	}
	
	public void msgBreakDenied(){
		//log.add(new LoggedEvent("Received message msgBreakDenied from host. Break request denied"));
	}
	
	public void SitAtTable(Customer customer, int table){
		//log.add(new LoggedEvent("Received message SitAtTable from host. Will take " + customer.getName() + 
		//		" to table " + table));
	}
	
	public void msgIAmReadyToOrder(Customer cust){
		//log.add(new LoggedEvent("Received message msgIAmReadyToOrder from customer. " + cust.getName() + 
		//		" is ready to order."));
	}

	public void msgHereIsMyOrder (Customer cust, String choice){
		//log.add(new LoggedEvent("Received message msgHereIsMyOrder from customer. " + cust.getChoice() + 
		//		" ordered " + choice));
	}
	
	public void msgOrderIsReady (String order, int t){
		//log.add(new LoggedEvent("Received message msgOrderIsReady from cook. " + order + " for table " +
		//		t + " is ready "));
	}
	
	public void msgOutOfFood (String order, int t){
		//log.add(new LoggedEvent("Received message msgOutOfFood from cook. We are running out of " +
		//		order + ". I need to tell customer at table " + t));
	}
	
	public void msgHereIsCheck (Customer cust, Double d){
		//log.add(new LoggedEvent("Received message msgHereIsCheck from cashier. Customer " + cust.getName() + 
		//		" needs to pay " + d));
	}
	
	public void msgDoneEatingAndLeaving (Customer cust){
	//	log.add(new LoggedEvent("Received message msgDoneEatingAndLeaving from customer. " + cust +
		//		" is leaving"));
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	
	

}
