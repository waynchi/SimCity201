package restaurant.test.mock;

import java.util.List;

import city.gui.CityGui;
import people.People;
import people.Role;

public class MockPeople extends Mock implements People{

	public MockPeople(String name) {
		super(name);
		// TODO Auto-generated constructor stub
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
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public void setMoney(double Money) {
		// TODO Auto-generated method stub
		
	}

}
