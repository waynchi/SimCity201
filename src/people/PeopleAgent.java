package people;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;
import city.Restaurant;
import city.gui.CityGui;
import agent.Agent;

public class PeopleAgent extends Agent implements People{

	public List<MyRole> roles = new ArrayList<MyRole>();
	public List<Restaurant> Restaurants = new ArrayList<Restaurant>();
	public List<Job> jobs = new ArrayList<Job>();
	public Double Money;
	private int Hunger = 10;
	public Boolean hasCar;
	public String name;
	public enum HungerState
	{NotHungry, Hungry, Eating};
	Random rand = new Random();
	CityGui cityGui;
	public EventLog log = new EventLog();

	
	public enum AgentState 
	{Sleeping, Working, EatingAtRestaurant, EatingAtHome, Idle, RestingAtHome, BuyingCar, atHome, GoingToBank}
	public enum AgentEvent 
	{GoingToSleep, WakingUp, GoingToRestaurant, GoingToWork, LeavingWork, GoingToRetrieveMoney, GoingToDepositMoney, GoingToBuyCar, Idle, GoingHome}
	public enum AgentLocation
	{Home, Bank, Market, Restaurant, Road}
	public HungerState hunger = HungerState.NotHungry;
	public AgentState state = AgentState.Sleeping;
	public AgentEvent event = AgentEvent.GoingToSleep;
	
	public double getMoney()
	{
		return Money;
	}
	
	public void setMoney(double Money)
	{
		this.Money = Money;
	}
	
	
	public List<Role> getRoles()
	{
		List<Role> temp = new ArrayList<Role>();
		for(MyRole a: roles)
		{
			temp.add(a.role);
		}
		return temp;
	}
	
	public String getAgentState()
	{
		return state.toString();
	} 
	
	public String getAgentEvent()
	{
		return event.toString();
	}
	
	public String getHunger()
	{
		return hunger.toString();
	}
	
	public Role getHost()
	{
		return Restaurants.get(0).h;
	}
	
	public Role getTeller()
	{
		return null;
	}
	
	public Role getMarketEmployee()
	{
		return null;
	}
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	public void addCityGui(CityGui gui)
	{
		cityGui = gui;
	}
	
	//public void addPeopleGui()
	//TODO
	/* (non-Javadoc)
	 * @see people.People#addRole(people.Role, java.lang.String)
	 */
	@Override
	public void addRole(Role r, String description)
	{
		roles.add(new MyRole(r, description));
		log.add(new LoggedEvent("Role added: " + description));
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
		log.add(new LoggedEvent("Job added: " + job));
	}
	//messages

	/* (non-Javadoc)
	 * @see people.People#msgDone(people.Role)
	 */
	@Override
	public void msgDone(String role)
	{
		log.add(new LoggedEvent("Recieved msgDone"));
		state = AgentState.Idle;
		event = AgentEvent.Idle;
		if(role.equals("RestaurantCustomerRole"))
		{
			hunger = HungerState.NotHungry;
		}
		if(role.equals("BankCustomerRole"))
		{
			
		}
	}
	
	public PeopleAgent(String name, double Money, boolean hasCar)
	{
		super();
		this.name = name;
		this.Money = Money;
		this.hasCar = hasCar;
	}
	
	
	/* (non-Javadoc)
	 * @see people.People#msgTimeIs(int)
	 */
	@Override
	
	/*public int msgWhatIsTime()
	{
		//TODO
		return cityGui.Time;
	}*/
	
	public void msgTimeIs(int Time)
	{
		/*if(Time == 0)
		{
			event = AgentEvent.GoingToRestaurant;
			stateChanged();
			print("GoingToCar");
			return;
		}*/
		if(Time == 800 && state == AgentState.Sleeping)
		{
			event = AgentEvent.WakingUp;
			log.add(new LoggedEvent("Waking Up In Message"));
			stateChanged();
			return;
		}
		if(state == AgentState.Idle)
		{
		if(jobs.get(0).start - Time >= 200 && Time <=2100)
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
					log.add(new LoggedEvent("Retrieving Money. Event is now: " + event.toString()));
					stateChanged();
					return;
				}
			}
			else
			{
				if(Money >= 50000)
				{
					event = AgentEvent.GoingToDepositMoney;
					stateChanged();
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
			log.add(new LoggedEvent("Going To Work"));
			stateChanged();
			return;
		}
		if(Time == job.end)
		{
			event = AgentEvent.LeavingWork;
			log.add(new LoggedEvent("Leaving Work"));
			stateChanged();
			return;
		}
		lastTime = job.end;
		}
		if(state != AgentState.Sleeping && state != AgentState.Working)
		{
			if(hunger == HungerState.Hungry)
			{
				if(rand.nextInt() < 1)
				{
					event = AgentEvent.GoingToRestaurant;
					print("Going To Restaurant To Eat");
					stateChanged();
				}
				else
				{
					event = AgentEvent.GoingHome;
					print("Going Home To Eat");
					stateChanged();
				}
				
				return;
			}
		}
		if(Time >= lastTime && state == AgentState.Idle && Time <= 2100)
		{
			if(!hasCar)
			{
				if(!(Time >= 1700) && Money >= 30000)
				{
					event = AgentEvent.GoingToBuyCar;
					stateChanged();
					return;
				}
				else if(!(Time>= 1700 && Money <= 30000))
				{
						event = AgentEvent.GoingToRetrieveMoney;
						log.add(new LoggedEvent("Retrieving Money. Event is now: " + event.toString()));
						stateChanged();
						return;
				}
			}				
			else
			{
				if(Money >= 1000000)
				{
					event = AgentEvent.GoingToDepositMoney;
					stateChanged();
					return;
				}
			}
			event = AgentEvent.GoingHome;
		}
		if(Time == 2330)
		{
			event = AgentEvent.GoingToSleep;
			log.add(new LoggedEvent("Sleeping In Message"));
			stateChanged();
			return;
		}
		
	}

	//scheduler
	@Override
	public boolean pickAndExecuteAnAction() {
		print("My Current State is: " + state.toString());
		print("My Current Event is: " + event.toString());
		print("My Current Hunger is : " + hunger.toString());
		boolean Roles = false, Person = false;
		for(MyRole m : roles)
		{
			if(m.role.isActive)
			{
				Roles = m.role.pickAndExecuteAnAction();
			}
		}
		if(state == AgentState.Sleeping && event == AgentEvent.WakingUp)
		{
			state = AgentState.Idle;
			log.add(new LoggedEvent("Waking Up In Scheduler. New State is " + state.toString()));
			Person = true;
		}
		if(state == AgentState.Idle && event == AgentEvent.GoingToWork)
		{
			state = AgentState.Working;
			log.add(new LoggedEvent("Going To Work. New State is " + state.toString()));
			GoToWork();
			Person = true;
		}
		if(state == AgentState.Working && event == AgentEvent.LeavingWork)
		{
			event = AgentEvent.Idle;
			state = AgentState.Idle;
			log.add(new LoggedEvent("Leaving Work. New State is " + state.toString()));
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
			state = AgentState.Idle;
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
			log.add(new LoggedEvent("Going To Bank. New State is " + state.toString()));
			GoToBank();
			Person = true;
		}
		if(state == AgentState.Idle && event == AgentEvent.GoingToDepositMoney)
		{
			state = AgentState.GoingToBank;
			GoToBank();
			Person = true;
		}
		if(state == AgentState.Idle && event == AgentEvent.GoingToSleep)
		{
			state = AgentState.Sleeping;
			log.add(new LoggedEvent("Sleeping In Scheduler. New State is " + state.toString()));
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
			//Gui for car animation
			hunger = HungerState.Eating;
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
		else
		{
			/*if(rand.nextInt(1) == 1)
			{
				for(MyRole r: roles)
				{
					if(r.description.equals("busPassenger"))
					{
						r.role.msgIsActive();
					}
				}
			}
			else*/
				//GUI WALK
				print("Walking to Restaurant");
				hunger = HungerState.Eating;
				//Semaphore
				for(MyRole r: roles)
				{
					if(r.description.equals("RestaurantCustomer"))
					{
						r.role.msgIsActive();
					}
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
		//msgTimeIs( jobs.get(0).end); //TODO I probably need to get the actual time here
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
	//Going to Work
	public void GoToWork()
	{
		print("I am going to work now!");
		for(MyRole r: roles)
		{
			if(r.description.equals("Resident"))
			{			
				r.role.msgIsInActive();
			}
		}
		//gui
		if(jobs.get(0).job.equals("RestaurantNormalWaiter"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantNormalWaiter"))
				{			
					print("I am now a RestaurantNormalWaiter");
					r.role.msgIsActive();
				}
			}
			//roles.WaiterRole.msgIsActive();
		}
		if(jobs.get(0).job.equals("RestaurantHost"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantHost"))
				{			
					print("I am now a RestaurantHost");
					r.role.msgIsActive();
				}
			}
			//roles.HostRole.msgIsActive();
		}
		if(jobs.get(0).job.equals("RestaurantCook"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantCook"))
				{			
					print("I am now a RestaurantCook");
					//r.role.msgIsActive();
				}
			}
			//roles.CookRole.msgIsActive();
		}
		if(jobs.get(0).job.equals("RestaurantCashier"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantCashier"))
				{		
					print("I am now a RestaurantCashier");
					r.role.msgIsActive();
					
				}
			}
			//roles.RepairRole.msgIsActive();
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

		