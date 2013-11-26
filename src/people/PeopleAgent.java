package people;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;
import city.Bank;
import city.Market;
import city.Restaurant;
import city.gui.CityGui;
import city.gui.PersonGui;
import agent.Agent;

public class PeopleAgent extends Agent implements People{

	public List<MyRole> roles = Collections.synchronizedList(new ArrayList<MyRole>());
	public List<Restaurant> Restaurants = new ArrayList<Restaurant>();
	public List<Market> Markets = new ArrayList<Market>();
	public List<Bank> Banks = new ArrayList<Bank>();
	public List<Job> jobs = new ArrayList<Job>();
	public Double Money;
	public Double Balance;
	private int Hunger = 1200;
	public Boolean hasCar;
	public String name;
	public enum HungerState
	{NotHungry, Hungry, Eating};
	Random rand = new Random();
	PersonGui personGui;
	CityGui cityGui;
	private boolean testmode = false;
	
	private Semaphore moving = new Semaphore(0,true);
	
	public EventLog log = new EventLog();

	public enum BuyState{GoingToBuy, NotBuying, NextDay}
	public enum AgentState 
	{Waiting, Sleeping, Working, EatingAtRestaurant, EatingAtHome, Idle, RestingAtHome, BuyingCar, atHome, GoingToBank, IdleAtHome}
	public enum AgentEvent 
	{Waiting, GoingToSleep, WakingUp, GoingToRestaurant, GoingToWork, LeavingWork, GoingToRetrieveMoney, 
		GoingToDepositMoney, GoingToBuyCar, Idle, GoingHome, RepairManMovingShop, RepairManArrivedShop, RepairManMoving, RepairManArrived}
	public enum AgentLocation
	{Home, Bank, Market, Restaurant, Road}
	public HungerState hunger = HungerState.NotHungry;
	public AgentState state = AgentState.Sleeping;
	public AgentEvent event = AgentEvent.GoingToSleep;
	public AgentLocation location = AgentLocation.Home;
	public BuyState buy = BuyState.NextDay;
	
	
	public void setTest()
	{
		testmode = true;
	}
	public void Arrived()
	{
		moving.release();
	}
	
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
	
	public Role getHost(int i)
	{
		return Restaurants.get(i).h;
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
	
	public void setPersonGui(PersonGui gui)
	{
		personGui = gui;
	}
	
	public void setCityGui(CityGui gui)
	{
		cityGui = gui;
	}
	
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
		print("Recieved msgDone from "+ role);
		if(role != "ResidentRole")
		{
		log.add(new LoggedEvent("Recieved msgDone"));
		state = AgentState.Idle;
		event = AgentEvent.Idle;
		if(role.equals("RestaurantCustomerRole"))
		{
			hunger = HungerState.NotHungry;
			Hunger = 2400;
		}
		if(role.equals("BankCustomerRole"))
		{
			
		}
		if(role.equals("MarketCustomerRole"))
		{
			hasCar = true;
		}
		if(role.equals("RepairManFixing"))
		{
			state = AgentState.Working;
			event = AgentEvent.RepairManMoving;
			stateChanged();
		}
		if(role.equals("RepairManFixed"))
		{
			state = AgentState.Working;
			event = AgentEvent.RepairManMovingShop;
		}
		if(role.equals("DoneEating"))
		{
			hunger = HungerState.NotHungry;
			Hunger = 2400;
		}
		}
		else
		{
			//unfreeze the semaphore in the gui
		}
	}
	
	public PeopleAgent(String name, double Money, boolean hasCar)
	{
		super();
		this.name = name;
		this.Money = Money;
		this.hasCar = hasCar;
		this.Balance = this.Money;
	}
	
	
	/* (non-Javadoc)
	 * @see people.People#msgTimeIs(int)
	 */
	public int msgWhatIsTime()
	{
		return cityGui.time;
	}
	
	public void msgTimeIs(int Time)
	{
		/*if(Time == 0)
		{
			event = AgentEvent.GoingToRestaurant;
			stateChanged();
			print("GoingToCar");
			return;
		}*/
		Hunger--;
		if(Hunger == 0)
		{
			hunger = HungerState.Hungry;
		}
		if(Time == 800 && state == AgentState.Sleeping)
		{
			event = AgentEvent.WakingUp;
			log.add(new LoggedEvent("Waking Up In Message"));
			stateChanged();
			return;
		}
		if(state == AgentState.Idle)
		{
			if(!jobs.isEmpty())
			{
				for(int i = 0; i < jobs.size(); i++)
				{
				if(jobs.get(i).start - Time >= 200 && Time <=2100 && buy == BuyState.NextDay)
				{
					if(!hasCar)
					{
						if(rand.nextInt(100) <= 0)
						{
							buy = BuyState.GoingToBuy;
						}
						else
						{
							buy = BuyState.NotBuying;
						}
						if(buy == BuyState.GoingToBuy)
						{
							if(Money >= 30000)
							{
								event = AgentEvent.GoingToBuyCar;
								log.add(new LoggedEvent("Going To Buy Car. Event is now: " + event.toString()));
								buy = BuyState.NotBuying;
								return;
							}
							else
							{
								event = AgentEvent.GoingToRetrieveMoney;
								log.add(new LoggedEvent("Retrieving Money. Event is now: " + event.toString()));
								stateChanged();
								buy = BuyState.NotBuying;
								return;
							}
							
						}
					}
					else
					{
						//System.out.println(Money);
						if(Money >= 1000000 && Time >= 1130)
						{
							event = AgentEvent.GoingToDepositMoney;
							log.add(new LoggedEvent("Depositing Money. Event is now: " + event.toString()));
							stateChanged();
							return;
						}
					}
				}
				}
			}
			if(location != AgentLocation.Home)
			{
				event = AgentEvent.GoingHome;
				stateChanged();
				return;
			}
		}
		int lastTime = 100000;
		for(Job job: jobs)
		{
		if(Time == job.start)
		{
			event = AgentEvent.GoingToWork;
			print("Going To Work");
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
		//state != AgentState.Sleeping && state != AgentState.Working && state != AgentState.Waiting
		if(state == AgentState.Idle)
		{
			if(hunger == HungerState.Hungry)
			{
				if(Time <= 1900)
				{
					if(rand.nextInt(1) < 1)
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
		if(state == AgentState.IdleAtHome)
		{
			if(hunger == HungerState.Hungry)
			{
				if(Time <= 1900)
				{
					if(rand.nextInt(1) < 1)
					{
						event = AgentEvent.GoingToRestaurant;
						print("Going To Restaurant To Eat");
						stateChanged();
						return;
					}
					else
					{
						event = AgentEvent.GoingHome;
						print("Going Home To Eat");
						stateChanged();
						return;
					}
				}
				else
				{
					event = AgentEvent.GoingHome;
					print("Going Home To Eat");
					stateChanged();
					return;
				}
			}
		}
		if(Time >= lastTime && state == AgentState.Idle && Time <= 2100 && buy == BuyState.NextDay)
		{
			if(!hasCar)
			{
				if(rand.nextInt(100) <= 0)
				{
					buy = BuyState.GoingToBuy;
				}
				else
				{
					buy = BuyState.NotBuying;
				}
				if(buy == BuyState.GoingToBuy)
				{
					if(!(Time >= 2100) && Money >= 30000)
					{
						event = AgentEvent.GoingToBuyCar;
						stateChanged();
						return;
					}
					else if(!(Time>= 2100 && Money <= 30000))
					{
						event = AgentEvent.GoingToRetrieveMoney;
						log.add(new LoggedEvent("Retrieving Money. Event is now: " + event.toString()));
						stateChanged();
						return;
					}
					buy = BuyState.NotBuying;
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
			//event = AgentEvent.GoingHome;
		}
		if(Time == 2330)
		{
			event = AgentEvent.GoingToSleep;
			buy = BuyState.NextDay;
			log.add(new LoggedEvent("Sleeping In Message"));
			stateChanged();
			return;
		}
		if(Time >= 2330 && state != AgentState.Sleeping)
		{
			state = AgentState.IdleAtHome;
			event = AgentEvent.GoingToSleep;
			buy = BuyState.NextDay;
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
		synchronized(roles){
		for(MyRole m : roles)
		{
			if(m.role.isActive)
			{
				Roles = m.role.pickAndExecuteAnAction();
			}
		}
		}
		if(state == AgentState.Working && event == AgentEvent.RepairManMoving)
		{
			GoRepair();
			//event = AgentEvent.RepairManArrived;
			Person = true;		
		}
		if(state == AgentState.Working && event == AgentEvent.RepairManMovingShop)
		{
			GoRepairShop();
			Person = true;
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
			event = AgentEvent.Waiting;
			state = AgentState.Waiting;
			log.add(new LoggedEvent("Leaving Work. New State is " + state.toString()));
			LeaveWork();
			Person = true;
		}
		if((state == AgentState.Idle || state == AgentState.IdleAtHome) && event == AgentEvent.GoingToRestaurant)
		{
			state = AgentState.EatingAtRestaurant;
			GoToRestaurant();
			Person = true;
		}
		if((state == AgentState.Idle && event == AgentEvent.GoingHome))
		{
			if(hunger == HungerState.Hungry)
			{
			state = AgentState.EatingAtHome;
			hunger = HungerState.Eating;
			}
			else
			{
			state = AgentState.IdleAtHome;
			hunger = HungerState.NotHungry;
			}
			if(location != AgentLocation.Home)
			{
			GoToHouse();
			}
			Person = true;
		}
		if(state == AgentState.IdleAtHome && event == AgentEvent.GoingHome)
		{
			hunger = HungerState.Eating;
			state = AgentState.EatingAtHome;
			Person = true;
		}
		if(state == AgentState.Idle && event == AgentEvent.GoingToBuyCar)
		{
			state = AgentState.BuyingCar;
			log.add(new LoggedEvent("Going To Buy Car. New State is " + state.toString()));
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
			log.add(new LoggedEvent("Going To Bank. New State is " + state.toString()));
			GoToBank();
			Person = true;
		}
		if((state == AgentState.Idle || state == AgentState.IdleAtHome) && event == AgentEvent.GoingToSleep)
		{
			state = AgentState.Sleeping;
			log.add(new LoggedEvent("Sleeping In Scheduler. New State is " + state.toString()));
			Person = true;
		}

		
		return (Roles || Person);
	}

	





//Actions

	private void GoRepairShop() {
		// TODO Auto-generated method stub
		//gui message to ask for destination
		//Semaphore
		event = AgentEvent.RepairManArrivedShop;
		
	}

	private void GoRepair() {
		// TODO Auto-generated method stub
		//gui message to ask for destination
		//Semaphore
		event = AgentEvent.RepairManArrived;
		
	}

	public void GoToRestaurant()
	{
		location = AgentLocation.Road;
		
		//roles.RestaurantCustomerAgent.msg(this);
//		if(hasCar)
//		{
//			hunger = HungerState.Eating;
//		for(MyRole r: roles)
//		{
//			if(r.description.equals("carPassenger"))
//			{
//				r.role.msgIsActive();
//			}
//			//Guifor Car Animation
//			
//			/*if(r.description.equals("CustomerAgent"))
//			{			
//				r.role.msgIsActive();
//			}*/
//		}
//		}
//		else
		{
			//TODO
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
				if(!testmode)
				{
				personGui.GoToRestaurantOne();
				try {
					moving.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				location = AgentLocation.Restaurant;
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
		print("Going Back Home");
		location = AgentLocation.Road;
		if(!testmode)
		{
		personGui.GoToHouse();
		try {
			moving.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}
		location = AgentLocation.Home;
		for(MyRole r: roles)
		{
			if(r.description.equals("Resident"))
			{	
				print("I am now a Resident");
				r.role.msgIsActive();
			}
		}
		if(state != AgentState.EatingAtHome && state != AgentState.IdleAtHome)
		{
		state = AgentState.Idle;
		}
	}

	/* (non-Javadoc)
	 * @see people.People#GoBuyCar()
	 */
	@Override
	public void GoBuyCar()
	{
		location = AgentLocation.Road;
		if(!testmode)
		{
		personGui.GoToMarket();
		try {
			moving.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}
		location = AgentLocation.Market;
		for(MyRole r: roles)
		{
			if(r.description.equals("Resident"))
			{	
				if(r.role.isActive == true)
				{
				r.role.msgIsInActive();
				}
				//Stop
			}
			if(r.description.equals("MarketCustomer"))
			{	
				r.role.msgIsActive();
			}
		}
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
			if(r.description.equals("Resident"))
			{	
				if(r.role.isActive == false)
				{
				r.role.msgIsActive();
				}
				//Stop
			}
			if(r.description.equals(jobs.get(0).job))
			{
				System.out.println("I am leaving work");
				r.role.msgIsInActive();
				
			}
		}
	}

	/* (non-Javadoc)
	 * @see people.People#GoToBank()
	 */
	@Override
	public void GoToBank()
	{
		location = AgentLocation.Road;
		if(!testmode)
		{
		personGui.goToBank();
		try {
			moving.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}
		location = AgentLocation.Bank;
		
		for(MyRole r: roles)
		{
			if(r.description.equals("Resident"))
			{	
				if(r.role.isActive == true)
				{
					print("Resident Role turned off");
					r.role.msgIsInActive();
				}
				//Stop
			}
			if(r.description == "BankCustomer")
			{
				r.role.msgIsActive();
			}
		}
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
		for(int i = 0; i <jobs.size(); i++)
		{
		print("I am going to work now!");
		for(MyRole r: roles)
		{
			if(r.description.equals("Resident"))
			{	
				if(r.role.isActive == true)
				{
					print("Resident Role turned off");
					r.role.msgIsInActive();
				}
				//Stop
			}
		}
		//Pause the Gui
		//Release the Gui from msgDone
		if(jobs.get(i).job.equals("RestaurantNormalWaiter"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantNormalWaiter"))
				{	
					location = AgentLocation.Road;
					if(!testmode)
					{
					personGui.GoToRestaurantOne();
					try {
						moving.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					}
					location = AgentLocation.Restaurant;
					print("I am now a RestaurantNormalWaiter");
					r.role.msgIsActive();
				}
			}
			//roles.WaiterRole.msgIsActive();
		}
		if(jobs.get(i).job.equals("RestaurantHost"))
		{
			for(MyRole r: roles)
			{
				
				if(r.description.equals("RestaurantHost"))
				{	
					location = AgentLocation.Road;
					if(!testmode)
					{
					personGui.GoToRestaurantOne();
					try {
						moving.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					}
					location = AgentLocation.Restaurant;
					print("I am now a RestaurantHost");
					r.role.msgIsActive();
				}
			}
			//roles.HostRole.msgIsActive();
		}
		if(jobs.get(i).job.equals("RestaurantCook"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantCook"))
				{			
					location = AgentLocation.Road;
					if(!testmode)
					{
					personGui.GoToRestaurantOne();
					try {
						moving.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					}
					location = AgentLocation.Restaurant;
					print("I am now a RestaurantCook");
					r.role.msgIsActive();
				}
			}
			//roles.CookRole.msgIsActive();
		}
		if(jobs.get(i).job.equals("RestaurantCashier"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantCashier"))
				{	
					location = AgentLocation.Road;
					if(!testmode)
					{
					personGui.GoToRestaurantOne();
					try {
						moving.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					}
					location = AgentLocation.Restaurant;
					print("I am now a RestaurantCashier");
					r.role.msgIsActive();
					
				}
			}
			//roles.RepairRole.msgIsActive();
		}
		if(jobs.get(i).job.equals("Vendor"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("Vendor"))
				{			
					print("I am now a " + r.description);
					r.role.msgIsActive();
				}
			}
			//roles.VendorRole.msgIsActive();
		}
		if(jobs.get(i).job.equals("Teller"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("Teller"))
				{			
					print("I am now a " + r.description);
					r.role.msgIsActive();
				}
			}
			//roles.TellerRole.msgIsActive();
		}
		if(jobs.get(i).job.equals("MarketCashier"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("MarketCashier"))
				{			
					print("I am now a " + r.description);
					r.role.msgIsActive();
				}
			}
			//roles.TellerRole.msgIsActive();
		}
		if(jobs.get(i).job.equals("MarketEmployee"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("MarketEmployee"))
				{			
					print("I am now a " + r.description);
					r.role.msgIsActive();
				}
			}
			//roles.TellerRole.msgIsActive();
		}
		if(jobs.get(i).job.equals("RepairMan"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RepairMan"))
				{			
					print("I am now a " + r.description);
					r.role.msgIsActive();
				}
			}
			//roles.RepairRole.msgIsActive();
		}
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

		