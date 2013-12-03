package restaurant_vk.interfaces;

import java.util.List;
import restaurant_vk.CustomerRestaurantCheck;
import restaurant_vk.Menu;
import restaurant_vk.gui.CustomerGui;

public interface Customer {
	public void gotHungry();
	
	public void followMeToTable(Menu m);
	
	public void whatWouldYouLike();
	
	public void hereIsYourFood();
	
	public void msgDecideChoice(String choice);
	
	public void msgWantToLeave();
	
	public void outOfChoice(Menu m, String choice);
	
	public void hereIsCheck(CustomerRestaurantCheck c);
	
	public void hereIsChangeAndApprovedPayments(double change, List<CustomerRestaurantCheck> approvedPayments);
	
	public void tablesAreFull();
	
	public boolean isReadyToSit();
	
	public boolean canLeaveWhileWaiting();
	
	public void setWaiter(Waiter w);
	
	public CustomerGui getGui();
}
