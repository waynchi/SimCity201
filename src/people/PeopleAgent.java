package people;
import java.util.List;
import java.util.Random;

import agent.Agent;

public class PeopleAgent extends Agent{

	public PeopleAgent() {
		// TODO Auto-generated constructor stub
	}

	List<MyRole> roles;
	List<Job> jobs;
	Double Money;
	Boolean hasCar;
	public enum HungerState
	{NotHungry, Hungry, Eating};
	Random rand = new Random();
	//CityGui gui;
	
	enum AgentState 
	{Sleeping, Working, EatingAtRestaurant, EatingAtHome, Idle, RestingAtHome, BuyingCar, atHome}
	enum AgentEvent 
	{GoingToSleep, WakingUp, GoingToRestaurant, GoingToWork, LeavingWork, GoingToRetrieveMoney, GoingToDepositMoney, GoingToBuyCar, Idle, GoingHome}
	public HungerState hunger = HungerState.NotHungry;
	public AgentState state = AgentState.Idle;
	public AgentEvent event = AgentEvent.Idle;
	
	//messages

	public void msgDone(Role r)
	{
		state = AgentState.Idle;
		r.msgIsInActive();
	}
	
	public void msgTimeIs(int Time)
	{
		if(Time == 800 && state == AgentState.Sleeping)
			
		{
			event = AgentEvent.WakingUp;
			stateChanged();
			return;
		}
		if(state == AgentState.Idle)
		{
		if(jobs.get(0).start - Time >= 200)
		{
			if(!hasCar)
			{
				if(Money >= 30000)
				{
				event = AgentEvent.GoingToBuyCar;
				return;
				}
				else
				{
					event = AgentEvent.GoingToRetrieveMoney;
					return;
				}
			}
			else
			{
				if(Money >= 50000)
				{
					event = AgentEvent.GoingToDepositMoney;
					return;
				}
			}
		}
		}
		int lastTime = 100000;
		for(Job job: jobs)
		{
		if(Time == job.start)
		{
			event = AgentEvent.GoingToWork;
			stateChanged();
			return;
		}
		if(Time == job.end)
		{
			event = AgentEvent.LeavingWork;
			stateChanged();
			return;
		}
		lastTime = job.end;
		}
		if(state != AgentState.Sleeping || state != AgentState.Working)
		{
			if(hunger == HungerState.Hungry)
			{
				if(rand.nextInt() < 1)
				{
					event = AgentEvent.GoingToRestaurant;
				}
				else
				{
					event = AgentEvent.GoingHome;
				}
				return;
			}
		}
		if(Time >= lastTime && state == AgentState.Idle)
		{
			if(!hasCar)
			{
				if(!(Time >= 1700) && Money >= 30000)
				{
					event = AgentEvent.GoingToBuyCar;
					return;
				}
				if(!(Time>= 1700 && Money <= 30000))
				{
					/*rand(){
						1. do nothing();
						2.{ event == GoingToRetrieveMoney();
					return}
					}*/
				}
			}				
			else
			{
				if(Money >= 1000000)
				{
					event = AgentEvent.GoingToDepositMoney;
					return;
				}
			}
			event = AgentEvent.GoingHome;
		}
		if(Time == 2330)
		{
			event = AgentEvent.GoingToSleep;
			return;
		}
		
	}

	//scheduler
	@Override
	protected boolean pickAndExecuteAnAction() {
		boolean Roles = false, Person = false;
		
		for(MyRole m : roles)
		{
			if(true /*m.isActive*/)
			{
				Roles = m.pickAndExecuteAnAction();
			}
		}
		if(state == AgentState.Sleeping && event == AgentEvent.WakingUp)
		{
			state = AgentState.Idle;
			return true;
		}
		if(state == AgentState.Idle && event == AgentEvent.GoingToWork)
		{
			state = AgentState.Working;
			GoToWork();
			return true;
		}
		if(state == Working && event == LeavingWork)
		{
			state == Idle;
			LeaveWork();
			return true;
		}
		if(state == Idle && event == GoingToRestaurant)
		{
			state == EatingAtRestaurant;
			GoingToRestaurant();
			return true;
		}
		if(state == Idle && event == GoingHome)
		{
			if(hunger == Hungry)
			{
			state == EatingAtHome;
			}
			else
			{
			state == RestingAtHome;
			}
			GoToHouse();
			return true;
		}
		if(state == Idle && event == GoingToBuyCar)
		{
			state == BuyingCar;
			GoBuyCar();
			return true;
		}
		if(state == Idle && event == GoingToRetrieveMoney)
		{
			state == GoingToBank;
			GoToBank(true);
			return true;
		}
		if(state == Idle && event == GoingToDepositMoney)
		{
			state == GoingToBank;
			GoToBank(false);
			return true;
		}

		
		return false;
	}

	





//Actions

	public void GoToRestaurant()
	{
		//gui.GoToRestaurant();
		//roles.RestaurantCustomerAgent.msg(this);
		
	}

	public void GoToHouse()
	{
		//gui.GoToHouse();
		//roles.HouseAgent.msg(this);	
	}

	public void GoBuyCar()
	{
		//gui.GoToMarket();
		//roles.MarketCustomerRole.msgBuyCar()
	}

	public void LeaveWork()
	{
		for(MyRole r: roles)
		{
			if(r.job == jobs.get(0).job)
			{
				r.role.msgIsInActive();
			}
		}
		msgTimeIs( jobs.get(0).end);
	}

	public void GoToBank()
	{
		//gui.GoToBank;
		//roles.BankCustomerRole.msgIsActive();
	}
		

	public void GoToWork()
	{
		for(MyRole r: roles)
		{
			if(r.job.equals("Resident"))
			{			
				r.role.msgIsInActive();
			}
		}
		//gui
		if(jobs.get(0).job.equals("Waiter"))
		{
			for(MyRole r: roles)
			{
				if(r.job.equals("Waiter"))
				{			
					r.role.msgIsActive();
				}
			}
			//roles.WaiterRole.msgIsActive();
		}
		if(jobs.get(0).job.equals("Host"))
		{
			for(MyRole r: roles)
			{
				if(r.job.equals("Host"))
				{			
					r.role.msgIsActive();
				}
			}
			//roles.HostRole.msgIsActive();
		}
		if(jobs.get(0).job.equals("Cook"))
		{
			for(MyRole r: roles)
			{
				if(r.job.equals("Cook"))
				{			
					r.role.msgIsActive();
				}
			}
			//roles.CookRole.msgIsActive();
		}
		if(jobs.get(0).job.equals("Vendor"))
		{
			for(MyRole r: roles)
			{
				if(r.job.equals("Vendor"))
				{			
					r.role.msgIsActive();
				}
			}
			//roles.VendorRole.msgIsActive();
		}
		if(jobs.get(0).job.equals("Teller"))
		{
			for(MyRole r: roles)
			{
				if(r.job.equals("Teller"))
				{			
					r.role.msgIsActive();
				}
			}
			//roles.TellerRole.msgIsActive();
		}
		if(jobs.get(0).job.equals("RepairMan"))
		{
			for(MyRole r: roles)
			{
				if(r.job.equals("RepairMan"))
				{			
					r.role.msgIsActive();
				}
			}
			//roles.RepairRole.msgIsActive();
		}
		if(jobs.get(0).job.equals("Cashier"))
		{
			for(MyRole r: roles)
			{
				if(r.job.equals("Cashier"))
				{			
					r.role.msgIsActive();
				}
			}
			//roles.RepairRole.msgIsActive();
		}

		//roles.ResidentRole.msgIsInActive();
	}

	
	public void CallstateChanged(){
		stateChanged();
	}
	
	class Job
	{
		String job;
		int start;
		int end;
		
		Job(String job, int start, int end)
		{
			this.job = job;
			this.start = start;
			this.end = end;
		}
	}

	class MyRole
	{
		String job;
		Role role;
		public void pickAndExecuteAnAction() {
			// TODO Auto-generated method stub
			
		}
	}


}

		