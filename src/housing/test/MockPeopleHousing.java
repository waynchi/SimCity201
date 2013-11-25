package housing.test;

import java.util.List;
import city.gui.CityGui;
import city.gui.PersonGui;
import people.People;
import people.Role;
import people.PeopleAgent.AgentEvent;
import people.PeopleAgent.AgentState;
import people.PeopleAgent.HungerState;
import market.test.EventLog;
import market.test.Mock;

public class MockPeopleHousing extends Mock implements People {
	
	EventLog log = new EventLog();
	public HungerState hunger = HungerState.NotHungry;
	public AgentState state = AgentState.Sleeping;
	public AgentEvent event = AgentEvent.GoingToSleep;
	
	public enum AgentState 
	{Sleeping, Working, EatingAtRestaurant, EatingAtHome, Idle, RestingAtHome, BuyingCar, atHome, GoingToBank}
	public enum AgentEvent 
	{GoingToSleep, WakingUp, GoingToRestaurant, GoingToWork, LeavingWork, GoingToRetrieveMoney, GoingToDepositMoney,
		GoingToBuyCar, Idle, GoingHome, RepairManMovingShop, RepairManArrivedShop, RepairManMoving, RepairManArrived}
	public enum HungerState {NotHungry, Hungry, Eating};

	public MockPeopleHousing(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double getMoney() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMoney(double Money) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Role> getRoles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAgentState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAgentEvent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHunger() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Role getHost(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Role getTeller() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Role getMarketEmployee() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMaitreDName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPersonGui(PersonGui gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCityGui(CityGui gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Arrived() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRole(Role r, String description) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void CallPrint(String text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void CallDo(String text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addJob(String job, int start, int end) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDone(String role) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTimeIs(int Time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void GoToRestaurant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void GoToHouse() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void GoBuyCar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void LeaveWork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void GoToBank() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void GoToWork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void CallstateChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int msgWhatIsTime() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
