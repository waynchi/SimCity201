package people;

import java.util.ArrayList;
import java.util.List;

import people.PeopleAgent.Job;
import people.PeopleAgent.MyRole;
import restaurant.test.mock.LoggedEvent;
import city.gui.CityGui;
import city.gui.PersonGui;

public interface People {
	
	public double getMoney();
	
	public void setMoney(double Money);
	
	public List<Role> getRoles();
	
	public String getAgentState();
	
	public String getAgentEvent();
	
	public String getHunger();
	
	public Role getHost(int i);
	
	public Role getTeller();
	
	public Role getMarketEmployee();
	
	public String getMaitreDName();
	
	public String getName();
	
	public void setPersonGui(PersonGui gui);
	
	public void setCityGui(CityGui gui);

	public abstract void addRole(Role r, String description);

	public abstract void CallPrint(String text);

	public abstract void CallDo(String text);

	public abstract void addJob(String job, int start, int end);

	//messages

	public abstract void msgDone(String role);

	public abstract void msgTimeIs(int Time);

	public abstract void GoToRestaurant();

	public abstract void GoToHouse();

	public abstract void GoBuyCar();

	public abstract void LeaveWork();

	public abstract void GoToBank();

	public abstract void GoToWork();

	public abstract void CallstateChanged();

}