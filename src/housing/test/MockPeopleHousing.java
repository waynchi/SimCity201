package housing.test;

import java.util.List;
import city.gui.CityGui;
import city.gui.PersonGui;
import people.People;
import people.PeopleAgent;
import people.Role;
import people.PeopleAgent.AgentEvent;
import people.PeopleAgent.AgentState;
import people.PeopleAgent.HungerState;
import restaurant.test.mock.LoggedEvent;
import market.test.EventLog;
import market.test.Mock;

public class MockPeopleHousing extends PeopleAgent implements People {
	
	EventLog log = new EventLog();

	public MockPeopleHousing(String name) {
		super(name, 100000, true);
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
		return state.toString();
	}

	@Override
	public String getAgentEvent() {
		return event.toString();
	}

	@Override
	public String getHunger() {
		return hunger.toString();
	}

	@Override
	public Role getHost(int i) {
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
		log.add(new LoggedEvent(role));
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
	
	public void setAgentState(AgentState s) {
		state = s;
	}
	
	public void setAgentState(AgentEvent e) {
		event = e;
	}
	
	public void setHungerState(HungerState h) {
		hunger = h;
	}
}