 package people;

import java.util.List;

import city.Bank;
import city.Market;
import city.Restaurant;
import city.gui.CityGui;
import city.gui.PersonGui;

public interface People {
	
	public abstract double getMoney();
	
	public abstract void setMoney(double Money);
	
	public abstract List<Role> getRoles();
	
	public abstract String getAgentState();
	
	public abstract String getAgentEvent();
	
	public abstract String getHunger();
	
	public abstract Role getHost(int i);
	
	public abstract Role getTeller(int i);
	
	public abstract Role getMarketEmployee(int i);
	
	public abstract Restaurant getRestaurant(int i);
	
	public abstract Market getMarket(int i);
	
	public abstract Bank getBank(int i);
	
	public abstract String getMaitreDName();
	
	public abstract String getName();
	
	public abstract void setPersonGui(PersonGui gui);
	
	public abstract void setCityGui(CityGui gui);
	
	public abstract void Arrived();

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

	int msgWhatIsTime();

}