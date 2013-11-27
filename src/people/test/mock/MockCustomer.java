package people.test.mock;

import java.util.List;

import people.Role;
import restaurant.BaseWaiterRole.FoodOnMenu;
import restaurant.gui.CustomerGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

public class MockCustomer extends Role implements Customer{

	private String choice;
	private CustomerGui gui;
	
	public MockCustomer(String name) {
		super();
		// TODO Auto-generated constructor stub
	}

	public void gotHungry(){
		
	}

	public void msgRestaurantIsFull(){
		
	}
	
	// handles waiter follow me message and eventually sits down at the correct table
	public void msgFollowMeToTable(Waiter waiter, int tableNumber, List<FoodOnMenu> m){
		
	}

	// from animation, when customer has arrived at the table
	public void msgAtTable(){
		
	}
	
	public void msgAtCashier(){
		
	}
	
	//from animation, when customer has made the choice on pop up list
	public void msgAnimationChoiceMade(){
		
	}
	
	//from waiter agent
	public void msgWhatWouldYouLike(){
		
	}
	
	//from waiter agent
	public void msgReorder(List<FoodOnMenu> newMenu){
		
	}
	
	//from waiter agent
	public void msgHereIsYourFood(){
		
	}
	
	public void msgHereIsCheck (Double d, Cashier cashier){
		
	}
	
	public void msgHereIsYourChange (Double change){
	//	log.add(new LoggedEvent("Received msgHereIsYourChange from cashier and the amount is " + change));
	}

	//from animation
	public void msgAnimationFinishedLeaveRestaurant(){
		
	}

	public String getChoice(){
		return choice;
		
	}

	public CustomerGui getGui(){
		return gui;
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgAtExit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getState() {
		// TODO Auto-generated method stub
		return null;
	}

}
