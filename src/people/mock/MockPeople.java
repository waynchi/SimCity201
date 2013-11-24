package people.mock;

import java.util.List;

import city.gui.CityGui;
import people.People;
import people.Role;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.Mock;

public class MockPeople extends Mock implements People{
	
	public EventLog log = new EventLog();

	public MockPeople(String name, double Money, boolean hasCar)
	{
		super(name);
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
	public double getMoney() {
		// TODO Auto-generated method stub
		return 0;
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
	public Role getHost() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getMaitreDName() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void addCityGui(CityGui gui) {
		// TODO Auto-generated method stub
		
	}

}
