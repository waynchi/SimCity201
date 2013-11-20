package people;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import agent.Agent;

public class PeopleAgent extends Agent implements People{

	public PeopleAgent() {
		Money = 10.0;
		// TODO Auto-generated constructor stub
	}

	public List<MyRole> roles = new ArrayList<MyRole>();
	public List<Job> jobs = new ArrayList<Job>();
	public Double Money;
	public Boolean hasCar;
	public enum HungerState
	{NotHungry, Hungry, Eating};
	Random rand = new Random();
	//CityGui gui;
	
	public enum AgentState 
	{Sleeping, Working, EatingAtRestaurant, EatingAtHome, Idle, RestingAtHome, BuyingCar, atHome, GoingToBank}
	public enum AgentEvent 
	{GoingToSleep, WakingUp, GoingToRestaurant, GoingToWork, LeavingWork, GoingToRetrieveMoney, GoingToDepositMoney, GoingToBuyCar, Idle, GoingHome}
	public enum AgentLocation
	{Home, Bank, 	}
	public HungerState hunger = HungerState.NotHungry;
	public AgentState state = AgentState.Idle;
	public AgentEvent event = AgentEvent.Idle;
	
	/* (non-Javadoc)
	 * @see people.People#addRole(people.Role, java.lang.String)
	 */
	@Override
	public void addRole(Role r, String description)
	{
		roles.add(new MyRole(r, description));
	}
	
	/* (non-Javadoc)
	 * @see people.People#CallPrint(java.lang.String)
	 */
	@Override
	public void CallPrint(String text)
	{
		print(text);
	}
	
	/* (non-Javadoc)
	 * @see people.People#CallDo(java.lang.String)
	 */
	@Override
	public void CallDo(String text)
	{
		Do(text);
	}
	
	/* (non-Javadoc)
	 * @see people.People#addJob(java.lang.String, int, int)
	 */
	@Override
	public void addJob(String job, int start, int end)
	{
		jobs.add(new Job(job, start, end));
	}
	//messages

	/* (non-Javadoc)
	 * @see people.People#msgDone(people.Role)
	 */
	@Override
	public void msgDone(Role r)
	{
		state = AgentState.Idle;
		r.msgIsInActive();
	}
	
	public PeopleAgent(double Money, boolean hasCar)
	{
		super();
		this.Money = Money;
		this.hasCar = hasCar;
	}
	
	
	/* (non-Javadoc)
	 * @see people.People#msgTimeIs(int)
	 */
	@Override
	public void msgTimeIs(int Time)
	{
		if(Time == 0)
		{
			event = AgentEvent.GoingToRestaurant;
			stateChanged();
			print("GoingToCar");
			return;
		}
		if(Time == 800 && state == AgentState.Sleeping)
			
		{
			event = AgentEvent.WakingUp;
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
				Roles = m.role.pickAndExecuteAnAction();
			}
		}
		if(state == AgentState.Sleeping && event == AgentEvent.WakingUp)
		{
			state = AgentState.Idle;
			Person = true;
		}
		if(state == AgentState.Idle && event == AgentEvent.GoingToWork)
		{
			state = AgentState.Working;
			GoToWork();
			Person = true;
		}
		if(state == AgentState.Working && event == AgentEvent.LeavingWork)
		{
			state = AgentState.Idle;
			LeaveWork();
			Person = true;
		}
		if(state == AgentState.Idle && event == AgentEvent.GoingToRestaurant)
		{
			state = AgentState.EatingAtRestaurant;
			GoToRestaurant();
			Person = true;
		}
		if(state == AgentState.Idle && event == AgentEvent.GoingHome)
		{
			if(hunger == HungerState.Hungry)
			{
			state = AgentState.EatingAtHome;
			}
			else
			{
			state = AgentState.RestingAtHome;
			}
			GoToHouse();
			Person = true;
		}
		if(state == AgentState.Idle && event == AgentEvent.GoingToBuyCar)
		{
			state = AgentState.BuyingCar;
			GoBuyCar();
			Person = true;
		}
		if(state == AgentState.Idle && event == AgentEvent.GoingToRetrieveMoney)
		{
			state = AgentState.GoingToBank;
			GoToBank();
			Person = true;
		}
		if(state == AgentState.Idle && event == AgentEvent.GoingToDepositMoney)
		{
			state = AgentState.GoingToBank;
			GoToBank();
			Person = true;
		}

		
		return (Roles || Person);
	}

	





//Actions

	/* (non-Javadoc)
	 * @see people.People#GoToRestaurant()
	 */
	@Override
	public void GoToRestaurant()
	{
		//gui.GoToRestaurant();
		//roles.RestaurantCustomerAgent.msg(this);
		if(hasCar)
		{
		for(MyRole r: roles)
		{
			if(r.description.equals("carPassenger"))
			{
				r.role.msgIsActive();
			}
			/*if(r.description.equals("CustomerAgent"))
			{			
				r.role.msgIsActive();
			}*/
		}
		}
		
	}

	/* (non-Javadoc)
	 * @see people.People#GoToHouse()
	 */
	@Override
	public void GoToHouse()
	{
		//gui.GoToHouse();
		//roles.HouseAgent.msg(this);	
	}

	/* (non-Javadoc)
	 * @see people.People#GoBuyCar()
	 */
	@Override
	public void GoBuyCar()
	{
		//gui.GoToMarket();
		//roles.MarketCustomerRole.msgBuyCar()
	}

	/* (non-Javadoc)
	 * @see people.People#LeaveWork()
	 */
	@Override
	public void LeaveWork()
	{
		for(MyRole r: roles)
		{
			if(r.description == jobs.get(0).job)
			{
				r.role.msgIsInActive();
			}
		}
		msgTimeIs( jobs.get(0).end);
	}

	/* (non-Javadoc)
	 * @see people.People#GoToBank()
	 */
	@Override
	public void GoToBank()
	{
		//gui.GoToBank;
		//roles.BankCustomerRole.msgIsActive();
	}
		

	/* (non-Javadoc)
	 * @see people.People#GoToWork()
	 */
	@Override
	public void GoToWork()
	{
		for(MyRole r: roles)
		{
			if(r.description.equals("Resident"))
			{			
				r.role.msgIsInActive();
			}
		}
		//gui
		if(jobs.get(0).job.equals("Waiter"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("Waiter"))
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
				if(r.description.equals("Host"))
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
				if(r.description.equals("Cook"))
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
				if(r.description.equals("Vendor"))
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
				if(r.description.equals("Teller"))
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
				if(r.description.equals("RepairMan"))
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
				if(r.description.equals("Cashier"))
				{			
					r.role.msgIsActive();
				}
			}
			//roles.RepairRole.msgIsActive();
		}

		//roles.ResidentRole.msgIsInActive();
	}

	
	/* (non-Javadoc)
	 * @see people.People#CallstateChanged()
	 */
	@Override
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
		String description;
		Role role;
		MyRole(Role r, String d)
		{
			role = r;
			description = d;
		}
	}


}

		