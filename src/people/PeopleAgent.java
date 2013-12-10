package people;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.Semaphore;

import bank.interfaces.Teller;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;
import transportation.CarPassengerRole;
import restaurant.HostRole;
import restaurant_zt.HostRoleZt;
import restaurant_wc.HostRoleWc;
import restaurant_es.HostRoleEs;
import restaurant_vk.VkHostRole;
import restaurant_ps.HostRolePS;
import market.MarketEmployeeRole;
import city.Bank;
import city.Market;
import city.Restaurant;
import city.gui.CityGui;
import city.gui.PersonGui;
import agent.Agent;

public class PeopleAgent extends Agent implements People{

	public List<MyRole> roles = Collections.synchronizedList(new ArrayList<MyRole>());
	public List<Restaurant> Restaurants = Collections.synchronizedList(new ArrayList<Restaurant>());
	public List<Market> Markets = Collections.synchronizedList(new ArrayList<Market>());
	public List<Bank> Banks = Collections.synchronizedList(new ArrayList<Bank>());
	public List<Job> jobs = Collections.synchronizedList(new ArrayList<Job>());
	private Yelp yelp;
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
	public int HomeNum = 0;
	private String type = "default";
	
	private Semaphore moving = new Semaphore(0,true);
	
	public EventLog log = new EventLog();

	public enum BuyState{GoingToBuy, NotBuying, NextDay}
	public enum AgentState 
	{Waiting, Sleeping, Working, EatingAtRestaurant, EatingAtHome, Idle, RestingAtHome, BuyingCar, atHome, GoingToBank, IdleAtHome, GoingHome}
	public enum AgentEvent 
	{Waiting, GoingToSleep, WakingUp, GoingToRestaurant, GoingToWork, LeavingWork, GoingToRetrieveMoney, 
		GoingToDepositMoney, GoingToBuyCar, Idle, GoingHome, RepairManMovingShop, RepairManArrivedShop, RepairManMoving, RepairManArrived, EatingAtHome}
	public enum AgentLocation
	{Home, Bank, Market, Restaurant, Road}
	public enum DepositState
	{Deposited,NextDay}
	
	public HungerState hunger = HungerState.NotHungry;
	public AgentState state = AgentState.Sleeping;
	public AgentEvent event = AgentEvent.GoingToSleep;
	public AgentLocation location = AgentLocation.Home;
	public BuyState buy = BuyState.NextDay;
	public DepositState deposit = DepositState.NextDay;
	
	
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
	
	public void setType(String t)
	{
		this.type = t;
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
	
	public Role getTeller(int i)
	{
		return Banks.get(i).t;
	}
	
	public Bank getBank(int i)
	{
		return Banks.get(i);
	}
	
	public Market getMarket(int i)
	{
		return Markets.get(i);
	}
	
	public Restaurant getRestaurant(int i)
	{
		return Restaurants.get(i);
	}
	
	public Role getMarketEmployee(int i)
	{
		return Markets.get(i).mer;
	}
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	public PersonGui getPersonGui()
	{
		return personGui;
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
	
	public void setHasCar(boolean t)
	{
		hasCar = t;
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
		print("job added for " + job);
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
		if(role == "CarPassenger")
		{
			moving.release();
		}
		else if(role == "PersonGui")
		{
			moving.release();
		}
		else if(role != "ResidentRole")
		{
			log.add(new LoggedEvent("Recieved msgDone"));
			state = AgentState.Idle;
			event = AgentEvent.Idle;
			if(role.equals("RestaurantCustomerRole"))
			{
				hunger = HungerState.NotHungry;
				Hunger = 2400;
			}
//			if(role.equals("BankCustomerRole"))
//			{
//				
//			}
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
			if(role.equals("RestaurantCashierRole") || role.equals("RestaurantHostRole") || role.equals("RestaurantWaiterRole") || role.equals("RestaurantCookRole"))
			{
				//Gui Stuff TODO
				for(MyRole r: roles)
				{
					if(r.description.equals("Resident"))
					{	
						if(r.role.isActive == false)
						{
						r.role.msgIsActive();
						}
					}
				}
			}
			if(role.equals("MarketEmployeeRole") || role.equals("TellerRole") || role.equals("MarketCashierRole"))
			{
				for(MyRole r : roles)
				{
					//Gui STuff TODO
					if(r.description.equals("Resident"))
					{	
						if(r.role.isActive == false)
						{
						r.role.msgIsActive();
						}
						//Stop
					}
				}
			}
		}
		else
		{
			if(event == AgentEvent.GoingToWork)
			{
				state = AgentState.Idle;
				stateChanged();
			}
			if(event == AgentEvent.GoingToBuyCar)
			{
				state = AgentState.Idle;
				stateChanged();
			}
			if(event == AgentEvent.GoingToRestaurant)
			{
				state = AgentState.Idle;
				stateChanged();
			}
			if(event == AgentEvent.GoingToDepositMoney || event == AgentEvent.GoingToRetrieveMoney)
			{
				state = AgentState.Idle;
				stateChanged();
			}
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
		if(type.equals("NormativeB3"))
		{
			Hunger--;
			//print("Hunger is " + Hunger);
			if(Hunger == 0)
			{
				print("I have become hungry");
				hunger = HungerState.Hungry;
			}
			if(Time == 600 && state == AgentState.Sleeping)
			{
				print("I am waking up");
				event = AgentEvent.WakingUp;
				location = AgentLocation.Home;
				log.add(new LoggedEvent("Waking Up In Message"));
				stateChanged();
				return;
			}
			if(Time == 2330)
			{
				location = AgentLocation.Home;
				event = AgentEvent.GoingToSleep;
				buy = BuyState.NextDay;
				deposit = DepositState.NextDay;
				log.add(new LoggedEvent("Sleeping In Message"));
				Hunger = 1230;
				stateChanged();
				return;
			}
			if((state == AgentState.Idle || state == AgentState.IdleAtHome))
			{
				if(buy == BuyState.NextDay)
				{
					if(!Markets.get(0).isClosed)
					{
						if(rand.nextInt(100) <= 100)
						{
							buy = BuyState.GoingToBuy;
						}
						else
						{
							buy = BuyState.NotBuying;
						}
						if(buy == BuyState.GoingToBuy)
						{
							if(Money >= 20000)
							{
								event = AgentEvent.GoingToBuyCar;
								print("I am going to buy a car");
								log.add(new LoggedEvent("Going To Buy Car. Event is now: " + event.toString()));
								buy = BuyState.NotBuying;
								stateChanged();
								return;
							}
	//						else
	//						{
	//							event = AgentEvent.GoingToRetrieveMoney;
	//							log.add(new LoggedEvent("Retrieving Money. Event is now: " + event.toString()));
	//							stateChanged();
	//							buy = BuyState.NotBuying;
	//							return;
	//						}	
						}
					}
				}
			}
			if(state == AgentState.Idle)
			{
				if(!Banks.get(0).isClosed)
				{
					if(Money >= 10000)
					{
						if(deposit == DepositState.NextDay)
						{
							event = AgentEvent.GoingToDepositMoney;
							log.add(new LoggedEvent("Depositing Money. Event is now: " + event.toString()));
							deposit = DepositState.Deposited;
							stateChanged();
							return;
						}						
					}
				}
			}
			if(state == AgentState.Idle || state == AgentState.IdleAtHome)
			{
				if(hunger == HungerState.Hungry)
				{
					//change this for restaurant
					if(!Restaurants.get(1).isClosed)
					{
						if(rand.nextInt(2) <2)
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
						print("Eating at Home because no restaurants are open.");
						stateChanged();				
						return;
					}	
				}
			}
			if(state == AgentState.Idle)
			{
				if(location != AgentLocation.Home)
				{
					event = AgentEvent.GoingHome;
					stateChanged();
					return;
				}
			}
		}
		if(type.equals("NormativeB2"))
		{
			Hunger--;
			//print("Hunger is " + Hunger);
			if(Hunger == 0)
			{
				print("I have become hungry");
				hunger = HungerState.Hungry;
			}
			if(Time == 600 && state == AgentState.Sleeping)
			{
				print("I am waking up");
				event = AgentEvent.WakingUp;
				location = AgentLocation.Home;
				log.add(new LoggedEvent("Waking Up In Message"));
				stateChanged();
				return;
			}
			if(Time == 2330)
			{
				location = AgentLocation.Home;
				event = AgentEvent.GoingToSleep;
				buy = BuyState.NextDay;
				deposit = DepositState.NextDay;
				log.add(new LoggedEvent("Sleeping In Message"));
				Hunger = 1230;
				stateChanged();
				return;
			}
			if((state == AgentState.Idle || state == AgentState.IdleAtHome))
			{
				if(buy == BuyState.NextDay)
				{
					if(!Markets.get(0).isClosed)
					{
						if(rand.nextInt(100) <= 100)
						{
							buy = BuyState.GoingToBuy;
						}
						else
						{
							buy = BuyState.NotBuying;
						}
						if(buy == BuyState.GoingToBuy)
						{
							if(Money >= 20000)
							{
								event = AgentEvent.GoingToBuyCar;
								print("I am going to buy a car");
								log.add(new LoggedEvent("Going To Buy Car. Event is now: " + event.toString()));
								buy = BuyState.NotBuying;
								stateChanged();
								return;
							}
	//						else
	//						{
	//							event = AgentEvent.GoingToRetrieveMoney;
	//							log.add(new LoggedEvent("Retrieving Money. Event is now: " + event.toString()));
	//							stateChanged();
	//							buy = BuyState.NotBuying;
	//							return;
	//						}	
						}
					}
				}
			}
			if(state == AgentState.Idle &&  Time >= 1300)
			{
				if(!Banks.get(0).isClosed)
				{
					if(Money >= 10000)
					{
						if(deposit == DepositState.NextDay)
						{
							event = AgentEvent.GoingToDepositMoney;
							log.add(new LoggedEvent("Depositing Money. Event is now: " + event.toString()));
							deposit = DepositState.Deposited;
							stateChanged();
							return;
						}						
					}
				}
			}
			if(state == AgentState.Idle || state == AgentState.IdleAtHome)
			{
				if(hunger == HungerState.Hungry)
				{
					//change this for restaurant
					if(!Restaurants.get(1).isClosed)
					{
						if(rand.nextInt(2) <2)
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
						print("Eating at Home because no restaurants are open.");
						stateChanged();				
						return;
					}	
				}
			}
			if(state == AgentState.Idle)
			{
				if(location != AgentLocation.Home)
				{
					event = AgentEvent.GoingHome;
					stateChanged();
					return;
				}
			}
		}
		if(type.equals("NormativeB1"))
		{
			Hunger--;
			//print("Hunger is " + Hunger);
			if(Hunger == 0)
			{
				print("I have become hungry");
				hunger = HungerState.Hungry;
			}
			if(Time == 600 && state == AgentState.Sleeping)
			{
				print("I am waking up");
				event = AgentEvent.WakingUp;
				location = AgentLocation.Home;
				log.add(new LoggedEvent("Waking Up In Message"));
				stateChanged();
				return;
			}
			if(Time == 2330)
			{
				location = AgentLocation.Home;
				event = AgentEvent.GoingToSleep;
				deposit = DepositState.NextDay;
				buy = BuyState.NextDay;
				log.add(new LoggedEvent("Sleeping In Message"));	
				Hunger = 1230;
				stateChanged();
				return;
			}
			if(state == AgentState.Idle)
			{
				if(!Banks.get(0).isClosed)
				{
					if(Money >= 1000000)
					{
						if(deposit == DepositState.NextDay)
						{
							event = AgentEvent.GoingToDepositMoney;
							log.add(new LoggedEvent("Depositing Money. Event is now: " + event.toString()));
							deposit = DepositState.Deposited;
							stateChanged();
							return;
						}
					}
				}
			}
			if((state == AgentState.Idle || state == AgentState.IdleAtHome) &&  Time >= 1300)
			{
				if(buy == BuyState.NextDay)
				{
					if(!Markets.get(0).isClosed)
					{
						if(!hasCar)
						{
							if(rand.nextInt(100) <= 100)
							{
								buy = BuyState.GoingToBuy;
							}
							else
							{
								buy = BuyState.NotBuying;
							}
							if(buy == BuyState.GoingToBuy)
							{
								if(Money >= 20000)
								{
									event = AgentEvent.GoingToBuyCar;
									print("I am going to buy a car");
									log.add(new LoggedEvent("Going To Buy Car. Event is now: " + event.toString()));
									buy = BuyState.NotBuying;
									stateChanged();
									return;
								}
		//						else
		//						{
		//							event = AgentEvent.GoingToRetrieveMoney;
		//							log.add(new LoggedEvent("Retrieving Money. Event is now: " + event.toString()));
		//							stateChanged();
		//							buy = BuyState.NotBuying;
		//							return;
		//						}	
							}
						}
					}
				}
			
			}
			if(state == AgentState.Idle || state == AgentState.IdleAtHome)
			{
				if(hunger == HungerState.Hungry)
				{
					//change this for restaurant
					if(!Restaurants.get(1).isClosed)
					{
						if(rand.nextInt(2) <2)
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
						print("Eating at Home because no restaurants are open.");
						stateChanged();				
						return;
					}	
				}
			}
			if(state == AgentState.Idle)
			{
				if(location != AgentLocation.Home)
				{
					event = AgentEvent.GoingHome;
					stateChanged();
					return;
				}
			}
		}
		if(type.equals("default"))
		{
		/*if(Time == 0)
		{
			event = AgentEvent.GoingToRestaurant;
			stateChanged();
			print("GoingToCar");
			return;
		}*/
		Hunger--;
		//print("Hunger is " + Hunger);
		if(Hunger == 0)
		{
			print("I have become hungry");
			hunger = HungerState.Hungry;
		}
		if(Time == 600 && state == AgentState.Sleeping)
		{
			print("I am waking up");
			event = AgentEvent.WakingUp;
			location = AgentLocation.Home;
			log.add(new LoggedEvent("Waking Up In Message"));
			stateChanged();
			return;
		}
		if(Time == 2330)
		{
			location = AgentLocation.Home;
			event = AgentEvent.GoingToSleep;
			buy = BuyState.NextDay;
			deposit = DepositState.NextDay;
			log.add(new LoggedEvent("Sleeping In Message"));
			Hunger = 1230;
			stateChanged();
			return;
		}
		/*if(Time >= 2330 && state != AgentState.Sleeping)
		{
			state = AgentState.IdleAtHome;
			event = AgentEvent.GoingToSleep;
			buy = BuyState.NextDay;
			//print("Sleeping because of a bug");
			log.add(new LoggedEvent("Sleeping In Message"));
			Hunger = 1215;
			stateChanged();
			return;
		}*/
		int lastTime = 100000;
		for(Job job: jobs)
		{
		if(Time == job.start)
		{
			event = AgentEvent.GoingToWork;
			print("Going To Work from time is");
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
		if(job.job.contains("RestaurantHost"))
		{
			if(Time == 1830)
			{
				for(MyRole r: roles)
				{	
					if(r.description.equals("RestaurantHost"))
					{	
						((HostRole) r.role).msgSetClose();
					}
					if(r.description.equals("RestaurantHostVk"))
					{	
						((VkHostRole) r.role).msgSetClose();
					}
					if(r.description.equals("RestaurantHostVk"))
					{
						((VkHostRole) r.role).msgSetClose();
					}
					if(r.description.equals("RestaurantHostZt"))
					{	
						((HostRoleZt) r.role).msgSetClose();
					}
					if(r.description.equals("RestaurantHostWc"))
					{	
						((HostRoleWc) r.role).msgSetClose();
					}
					if(r.description.equals("RestaurantHostPs"))
					{	
						((HostRolePS) r.role).msgSetClose();
					}
					if(r.description.equals("RestaurantHostEs"))
					{	
						((HostRoleEs) r.role).msgSetClose();
					}
					if(r.description.equals("MarketEmployee"))
					{
						((MarketEmployeeRole) r.role).msgSetClose();
					}
				}
			}
		}
		lastTime = job.end;
		}
		if(state == AgentState.Idle)
		{
			if(!jobs.isEmpty())
			{
				if(buy == BuyState.NextDay && Time <=2100)
				{
					if(jobs.get(0).start - Time >= 200)
					{
						if(!(Markets.get(0).isClosed))
						{
							if(!hasCar)
							{
								if(rand.nextInt(100) <= 10)
								{
									buy = BuyState.GoingToBuy;
								}
								else
								{
									buy = BuyState.NotBuying;
								}
								if(buy == BuyState.GoingToBuy)
								{
									if(Money >= 20000)
									{
										event = AgentEvent.GoingToBuyCar;
										print("I am going to buy a car");
										log.add(new LoggedEvent("Going To Buy Car. Event is now: " + event.toString()));
										buy = BuyState.NotBuying;
										stateChanged();
										return;
									}
//									else
//									{
//										event = AgentEvent.GoingToRetrieveMoney;
//										log.add(new LoggedEvent("Retrieving Money. Event is now: " + event.toString()));
//										stateChanged();
//										buy = BuyState.NotBuying;
//										return;
//									}	
								}
							}			
						}
					}
				}
			}
			else if(!Banks.get(0).isClosed)
			{
				//System.out.println(Money);
				if(Money >= 1000000)
				{
					if(deposit == DepositState.NextDay)
					{
						event = AgentEvent.GoingToDepositMoney;
						log.add(new LoggedEvent("Depositing Money. Event is now: " + event.toString()));
						deposit = DepositState.Deposited;
						stateChanged();
						return;
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
		//state != AgentState.Sleeping && state != AgentState.Working && state != AgentState.Waiting
		if(state == AgentState.Idle)
		{
			if(hunger == HungerState.Hungry)
			{
				//change this for restaurant
				if(!Restaurants.get(1).isClosed)
				{
					if(rand.nextInt(2) <2)
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
					print("Eating at Home because no restaurants are open.");
					stateChanged();				
					return;
				}	
			}
		}
		if(state == AgentState.IdleAtHome)
		{
			if(hunger == HungerState.Hungry)
			{
				//change this for restaurant
				if(!Restaurants.get(1).isClosed)
				{
					if(rand.nextInt(2) < 2)
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
					event = AgentEvent.EatingAtHome;
					print("Eating at Home because no restaurants are open.");
					stateChanged();
					return;
				}
			}
		}
		if(Time >= lastTime)
		{
			if(state == AgentState.Idle)
			{
				if(!Markets.get(0).isClosed)
				{
					if(buy == BuyState.NextDay)
					{
						if(!hasCar)
						{
							if(rand.nextInt(100) <= 10)
							{
								buy = BuyState.GoingToBuy;
							}
							else
							{
								buy = BuyState.NotBuying;
							}
							if(buy == BuyState.GoingToBuy)
							{
								if(Money >= 20000)
								{
									event = AgentEvent.GoingToBuyCar;
									buy = BuyState.NotBuying;
									stateChanged();
									return;
								}
			//					else if(!(Time>= 2100 && Money <= 30000))
			//					{
			//						event = AgentEvent.GoingToRetrieveMoney;
			//						log.add(new LoggedEvent("Retrieving Money. Event is now: " + event.toString()));
			//						stateChanged();
			//						return;
			//					}
								buy = BuyState.NotBuying;
							}
						}				
						else if(!Banks.get(0).isClosed)
						{
							if(deposit == DepositState.NextDay)
							{
								event = AgentEvent.GoingToDepositMoney;
								deposit = DepositState.Deposited;
								stateChanged();
								return;
							}
						}
						//event = AgentEvent.GoingHome;
					}
				}
				if(location != AgentLocation.Home)
				{
					event = AgentEvent.GoingHome;
					stateChanged();
					return;
				}
			}
		}
		}
		
	}

	//scheduler
	@Override
	public boolean pickAndExecuteAnAction() {
//		print("My Current State is: " + state.toString());
//		print("My Current Event is: " + event.toString());
//		print("My Current Hunger is : " + hunger.toString());
//		print("My Type is: " + type);
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
//		if(state == AgentState.Working && event == AgentEvent.RepairManMoving)
//		{
//			GoRepair();
//			//event = AgentEvent.RepairManArrived;
//			Person = true;		
//		}
//		if(state == AgentState.Working && event == AgentEvent.RepairManMovingShop)
//		{
//			GoRepairShop();
//			Person = true;
//		}
		if(state == AgentState.Sleeping && event == AgentEvent.WakingUp)
		{
			state = AgentState.Idle;
			location = AgentLocation.Home;
			print("Waking up in scheduler");
			log.add(new LoggedEvent("Waking Up In Scheduler. New State is " + state.toString()));
			Person = true;
		}
		if(state == AgentState.Idle && event == AgentEvent.GoingToWork)
		{
			state = AgentState.Working;
			print("Going to work from scheduler");
			print("Location is" + location.toString());
			log.add(new LoggedEvent("Going To Work. New State is " + state.toString()));
			if(location == AgentLocation.Home)
			{
				LeaveHouse();
			}
			else
			{
				GoToWork();
			}
			Person = true;
		}
		if(state == AgentState.Working && event == AgentEvent.LeavingWork)
		{
			state = AgentState.Waiting;
			event = AgentEvent.Waiting;
			log.add(new LoggedEvent("Leaving Work. New State is " + state.toString()));
			LeaveWork();
			Person = true;
		}
		if((state == AgentState.Idle || state == AgentState.IdleAtHome) && event == AgentEvent.GoingToRestaurant)
		{
			state = AgentState.EatingAtRestaurant;
			if(location == AgentLocation.Home)
			{
				LeaveHouse();
			}
			else
			{
				print("Going To Restaurant From Scheduler");
				GoToRestaurant();
			}
			Person = true;
		}
		if((state == AgentState.Idle && event == AgentEvent.GoingHome))
		{
			if(hunger == HungerState.Hungry)
			{
			print("Eating at Home from Schedule");
			state = AgentState.EatingAtHome;
			hunger = HungerState.Eating;
			}
			else
			{
			state = AgentState.GoingHome;
			hunger = HungerState.NotHungry;
			}
			if(location != AgentLocation.Home)
			{
			GoToHouse();
			}
			Person = true;
		}
		if(state == AgentState.IdleAtHome && event == AgentEvent.EatingAtHome)
		{
			hunger = HungerState.Eating;
			state = AgentState.EatingAtHome;
			Person = true;
		}
		if(state == AgentState.Idle && event == AgentEvent.GoingToBuyCar)
		{
			state = AgentState.BuyingCar;
			print("Buying the car");
			log.add(new LoggedEvent("Going To Buy Car. New State is " + state.toString()));
			if(location == AgentLocation.Home)
			{
				LeaveHouse();
			}
			else
			{
				GoBuyCar();
			}
			Person = true;
		}
		if(state == AgentState.Idle && event == AgentEvent.GoingToRetrieveMoney)
		{
			state = AgentState.GoingToBank;
			log.add(new LoggedEvent("Going To Bank. New State is " + state.toString()));
			if(location == AgentLocation.Home)
			{
				LeaveHouse();
			}
			else
			{
				GoToBank();
			}
			Person = true;
		}
		if(state == AgentState.Idle && event == AgentEvent.GoingToDepositMoney)
		{
			state = AgentState.GoingToBank;
			log.add(new LoggedEvent("Going To Bank. New State is " + state.toString()));
			if(location == AgentLocation.Home)
			{
				LeaveHouse();
			}
			else
			{
				GoToBank();
			}
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
		int totaltemp = 0;
		int totalscore = 0;
		String restaurant = null;
		for (Entry<Restaurant, Integer> e : yelp.Ratings.entrySet()) {
			if(!e.getKey().isClosed)
			{
				totalscore += e.getValue();
			}
		}
		int tempNum = rand.nextInt(totalscore);
		for (Entry<Restaurant, Integer> e : yelp.Ratings.entrySet()) {
			if(!e.getKey().isClosed)
			{
				if(tempNum < (totaltemp + e.getValue()))
				{
					restaurant = e.getKey().n;
					break;
				}
				totaltemp += e.getValue();
			}
		}
		print(restaurant);
		if(restaurant.equals("Restaurant 1"))
		{
			if(!testmode)
			{
				if(hasCar)
				{
					for(MyRole r: roles)
					{
						if(r.description == "CarPassenger")
						{
							//change this for restaurant
							((CarPassengerRole)r.role).setDestination("Restaurant 1");
							r.role.msgIsActive();
						}
					}
				}
				else
				{
					//change this for restaurant
					personGui.setDestination("Restaurant 1");
					print("Do Not Have Car");
				}
			//personGui.GoToRestaurantOne();
			try {
				moving.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			location = AgentLocation.Restaurant;
			print("Going to Restaurant");
			hunger = HungerState.Eating;
			for(MyRole r: roles)
			{
				//change this for restaurant
				if(r.description.equals("RestaurantCustomer"))
				{
					r.role.msgIsActive();
				}
			}
		}
		if(restaurant.equals("Restaurant 2"))
		{
			if(!testmode)
			{
				if(hasCar)
				{
					for(MyRole r: roles)
					{						
						if(r.description == "CarPassenger")
						{
							//change this for restaurant
							((CarPassengerRole)r.role).setDestination("Restaurant 2");
							r.role.msgIsActive();
						}
					}
				}
				else
				{
					//change this for restaurant
					personGui.setDestination("Restaurant 2");
					print("Do Not Have Car");
				}
			//personGui.GoToRestaurantOne();
			try {
				moving.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			location = AgentLocation.Restaurant;
			print("Going to Restaurant");
			hunger = HungerState.Eating;
			for(MyRole r: roles)
			{
				//change this for restaurant
				personGui.setDestination("Restaurant 2");
				print("Do Not Have Car");
				if(r.description.equals("RestaurantCustomerVk"))
				{
					r.role.msgIsActive();
				}
			}
		}
		if(restaurant.equals("Restaurant 3"))
		{
			if(!testmode)
			{
				if(hasCar)
				{
					for(MyRole r: roles)
					{
						if(r.description == "CarPassenger")
						{
							//change this for restaurant
							((CarPassengerRole)r.role).setDestination("Restaurant 3");
							r.role.msgIsActive();
						}
					}
				}
				else
				{
					//change this for restaurant
					personGui.setDestination("Restaurant 3");
					print("Do Not Have Car");
				}
			//personGui.GoToRestaurantOne();
			try {
				moving.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			location = AgentLocation.Restaurant;
			print("Going to Restaurant");
			hunger = HungerState.Eating;
			for(MyRole r: roles)
			{
				//change this for restaurant
				if(r.description.equals("RestaurantCustomerZt"))
				{
					r.role.msgIsActive();
				}
			}
		}
		if(restaurant.equals("Restaurant 4"))
		{
			if(!testmode)
			{
				if(hasCar)
				{
					for(MyRole r: roles)
					{
						if(r.description == "CarPassenger")
						{
							//change this for restaurant
							((CarPassengerRole)r.role).setDestination("Restaurant 4");
							r.role.msgIsActive();
						}
					}
				}
				else
				{
					//change this for restaurant
					personGui.setDestination("Restaurant 4");
					print("Do Not Have Car");
				}
			//personGui.GoToRestaurantOne();
			try {
				moving.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			location = AgentLocation.Restaurant;
			print("Going to Restaurant");
			hunger = HungerState.Eating;
			for(MyRole r: roles)
			{
				//change this for restaurant
				if(r.description.equals("RestaurantCustomerWc"))
				{
					r.role.msgIsActive();
				}
			}
		}
		if(restaurant.equals("Restaurant 5"))
		{
			if(!testmode)
			{
				if(hasCar)
				{
					for(MyRole r: roles)
					{
						if(r.description == "CarPassenger")
						{
							//change this for restaurant
							((CarPassengerRole)r.role).setDestination("Restaurant 5");
							r.role.msgIsActive();
						}
					}
				}
				else
				{
					//change this for restaurant
					personGui.setDestination("Restaurant 5");
					print("Do Not Have Car");
				}
			//personGui.GoToRestaurantOne();
			try {
				moving.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			location = AgentLocation.Restaurant;
			print("Going to Restaurant");
			hunger = HungerState.Eating;
			for(MyRole r: roles)
			{
				//change this for restaurant
				if(r.description.equals("RestaurantCustomerEs"))
				{
					r.role.msgIsActive();
				}
			}
		}
		if(restaurant.equals("Restaurant 6"))
		{
			if(!testmode)
			{
				if(hasCar)
				{
					for(MyRole r: roles)
					{
						if(r.description == "CarPassenger")
						{
							//change this for restaurant
							((CarPassengerRole)r.role).setDestination("Restaurant 6");
							r.role.msgIsActive();
						}
					}
				}
				else
				{
					//change this for restaurant
					personGui.setDestination("Restaurant 6");
					print("Do Not Have Car");
				}
			//personGui.GoToRestaurantOne();
			try {
				moving.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			location = AgentLocation.Restaurant;
			print("Walking to Restaurant");
			hunger = HungerState.Eating;
			for(MyRole r: roles)
			{
				//change this for restaurant
				if(r.description.equals("RestaurantCustomerPs"))
				{
					r.role.msgIsActive();
				}
			}
		}
//		if(!testmode)
//		{
//			if(hasCar)
//			{
//				for(MyRole r: roles)
//				{
//					if(r.description == "CarPassenger")
//					{
//						//change this for restaurant
//						((CarPassengerRole)r.role).setDestination("Restaurant 3");
//						r.role.msgIsActive();
//					}
//				}
//			}
//			else
//			{
//				//change this for restaurant
//				personGui.setDestination("Restaurant 3");
//				print("Do Not Have Car");
//			}
//		//personGui.GoToRestaurantOne();
//		try {
//			moving.acquire();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
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
		//personGui.GoToHouse(); TODO
			if(hasCar)
			{
				for(MyRole r: roles)
				{
					if(r.description == "CarPassenger")
					{
						if(HomeNum <= 12)
						{
							((CarPassengerRole)r.role).setDestination("Home " + HomeNum);
						}
						else if(HomeNum <= 37)
						{
							((CarPassengerRole)r.role).setDestination("Apartment 1");
						}
						else if(HomeNum <= 62)
						{
							((CarPassengerRole)r.role).setDestination("Apartment 2");
						}
						r.role.msgIsActive();
					}
				}
			}
			else
			{
				if(HomeNum <= 12)
				{
					personGui.setDestination("Home "+ HomeNum);
				}
				else if(HomeNum <= 37)
				{
					personGui.setDestination("Apartment 1");
				}
				else if(HomeNum <= 62)
				{
					personGui.setDestination("Apartment 2");
				}
				 //TODO this is guess
				print("Do Not Have Car");
			}
			
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
			event = AgentEvent.Idle;
			print("I am now " + state.toString());
			location = AgentLocation.Home;
		}
	}

	/* (non-Javadoc)
	 * @see people.People#GoBuyCar()
	 */
	@Override
	public void GoBuyCar()
	{
//		boolean temp = true;
//		for(MyRole r: roles)
//		{
//			if(r.description.equals("Resident"))
//			{	
//				if(r.role.isActive == true)
//				{
//				r.role.msgIsInActive();
////				temp = false;
//				}
//				//Stop
//			}
//		}
////		if(temp)
////		{
////			GoBuyCarTwo();
////		}
//	}
//	
//	public void GoBuyCarTwo()
//	{
		location = AgentLocation.Road;
		if(!testmode)
		{
			if(hasCar)
			{
				for(MyRole r: roles)
				{
					if(r.description == "CarPassenger")
					{
						((CarPassengerRole)r.role).setDestination("Market");
						r.role.msgIsActive();
					}
				}
			}
			else
			{
				personGui.setDestination("Market");
				print("Do Not Have Car");
			}
		//personGui.GoToMarket(); TODO
		try {
			moving.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}
		location = AgentLocation.Market;
		for(MyRole r: roles)
		{
			if(r.description.equals("MarketCustomer"))
			{	
				r.role.msgIsActive();
			}
		}
	}

	/* (non-Javadoc)
	 * @see people.People#LeaveWork()
	 */
	@Override
	public void LeaveWork()
	{
		for(MyRole r: roles)
		{
			if(r.description.equals(jobs.get(0).job))
			{
				print("I am leaving work");
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
//		boolean temp = true;
//		for(MyRole r: roles)
//		{
//			if(r.description.equals("Resident"))
//			{	
//				
//				if(r.role.isActive == true)
//				{
//					print("Resident Role turned off");
////					temp = false;
//					r.role.msgIsInActive();
//				}
//				//Stop
//			}
//		}
//		if(temp)
//		{
//			GoToBankTwo();
//		}
//		//gui.GoToBank;
//		//roles.BankCustomerRole.msgIsActive();
//	}
//	
//	public void GoToBankTwo()
//	{
		location = AgentLocation.Road;
		if(!testmode)
		{
		//TODO personGui.goToBank();
			if(hasCar)
			{
				for(MyRole r: roles)
				{
					if(r.description == "CarPassenger")
					{
						((CarPassengerRole)r.role).setDestination("Bank");
						r.role.msgIsActive();
					}
				}
			}
			else
			{
				personGui.setDestination("Bank");
				print("Do Not Have Car");
			}
		try {
			moving.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}
		location = AgentLocation.Bank;
		
		for(MyRole r: roles)
		{
			if(r.description == "BankCustomer")
			{
				r.role.msgIsActive();
			}
		}
	}
		
	public void LeaveHouse()
	{
		print("I am leaving the house now");
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
		location = AgentLocation.Road;
		//Pause the Gui
	}

	/* (non-Javadoc)
	 * @see people.People#GoToWork()
	 */
	@Override
	//Going to Work
	public void GoToWork()
	{
		
//		for(int i = 0; i <jobs.size(); i++)
//		{
//			print("I am going to work now!");
//			for(MyRole r: roles)
//			{
//				if(r.description.equals("Resident"))
//				{	
//					if(r.role.isActive == true)
//					{
//						print("Resident Role turned off");
//						r.role.msgIsInActive();
//					}
//					//Stop
//				}
//			}
//			//Pause the Gui
//		}
//	}
//		
//	public void GoToWorkTwo(){
		//Release the Gui from msgDone
	for(int i = 0; i <jobs.size(); i++)
	{
		print("My job is " + jobs.get(i).job);
		if(jobs.get(i).job.equals("RestaurantNormalWaiter"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantNormalWaiter"))
				{	
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 1");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 1");
							print("Do Not Have Car");
						}
						// TODO personGui.GoToRestaurantOne();
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
		if(jobs.get(i).job.equals("RestaurantSpecialWaiter"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantSpecialWaiter"))
				{	
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 1");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 1");
							print("Do Not Have Car");
						}
						// TODO personGui.GoToRestaurantOne();
					try {
						moving.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					}
					location = AgentLocation.Restaurant;
					print("I am now a RestaurantSpecialWaiter");
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
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 1");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 1");
							print("Do Not Have Car");
						}
					//TODO personGui.GoToRestaurantOne();
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
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 1");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 1");
							print("Do Not Have Car");
						}
					//TODO personGui.GoToRestaurantOne();
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
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 1");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 1");
							print("Do Not Have Car");
						}
					//TODO personGui.GoToRestaurantOne();
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
		if(jobs.get(i).job.equals("RestaurantNormalWaiterVk"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantNormalWaiterVk"))
				{	
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 2");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 2");
							print("Do Not Have Car");
						}
						// TODO personGui.GoToRestaurantOne();
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
		if(jobs.get(i).job.equals("RestaurantSpecialWaiterVk"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantSpecialWaiterVk"))
				{	
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 2");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 2");
							print("Do Not Have Car");
						}
						// TODO personGui.GoToRestaurantOne();
					try {
						moving.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					}
					location = AgentLocation.Restaurant;
					print("I am now a RestaurantSpecialWaiter");
					r.role.msgIsActive();
				}
			}
			//roles.WaiterRole.msgIsActive();
		}
		
		if(jobs.get(i).job.equals("RestaurantHostVk"))
		{
			for(MyRole r: roles)
			{
				
				if(r.description.equals("RestaurantHostVk"))
				{	
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 2");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 2");
							print("Do Not Have Car");
						}
					//TODO personGui.GoToRestaurantOne();
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
		if(jobs.get(i).job.equals("RestaurantCookVk"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantCookVk"))
				{			
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 2");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 2");
							print("Do Not Have Car");
						}
					//TODO personGui.GoToRestaurantOne();
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
		if(jobs.get(i).job.equals("RestaurantCashierVk"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantCashierVk"))
				{	
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 2");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 2");
							print("Do Not Have Car");
						}
					//TODO personGui.GoToRestaurantOne();
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
		if(jobs.get(i).job.equals("RestaurantNormalWaiterZt"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantNormalWaiterZt"))
				{	
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 3");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 3");
							print("Do Not Have Car");
						}
						// TODO personGui.GoToRestaurantOne();
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
		if(jobs.get(i).job.equals("RestaurantSpecialWaiterZt"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantSpecialWaiterZt"))
				{	
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 3");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 3");
							print("Do Not Have Car");
						}
						// TODO personGui.GoToRestaurantOne();
					try {
						moving.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					}
					location = AgentLocation.Restaurant;
					print("I am now a RestaurantSpecialWaiter");
					r.role.msgIsActive();
				}
			}
			//roles.WaiterRole.msgIsActive();
		}
		
		if(jobs.get(i).job.equals("RestaurantHostZt"))
		{
			for(MyRole r: roles)
			{
				
				if(r.description.equals("RestaurantHostZt"))
				{	
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 3");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 3");
							print("Do Not Have Car");
						}
					//TODO personGui.GoToRestaurantOne();
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
		if(jobs.get(i).job.equals("RestaurantCookZt"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantCookZt"))
				{			
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 3");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 3");
							print("Do Not Have Car");
						}
					//TODO personGui.GoToRestaurantOne();
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
		if(jobs.get(i).job.equals("RestaurantCashierZt"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantCashierZt"))
				{	
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 3");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 3");
							print("Do Not Have Car");
						}
					//TODO personGui.GoToRestaurantOne();
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
		if(jobs.get(i).job.equals("RestaurantNormalWaiterWc"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantNormalWaiterWc"))
				{	
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 4");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 4");
							print("Do Not Have Car");
						}
						// TODO personGui.GoToRestaurantOne();
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
		if(jobs.get(i).job.equals("RestaurantSpecialWaiterWc"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantSpecialWaiterWc"))
				{	
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 4");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 4");
							print("Do Not Have Car");
						}
						// TODO personGui.GoToRestaurantOne();
					try {
						moving.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					}
					location = AgentLocation.Restaurant;
					print("I am now a RestaurantSpecialWaiter");
					r.role.msgIsActive();
				}
			}
			//roles.WaiterRole.msgIsActive();
		}
		
		if(jobs.get(i).job.equals("RestaurantHostWc"))
		{
			for(MyRole r: roles)
			{
				
				if(r.description.equals("RestaurantHostWc"))
				{	
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 4");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 4");
							print("Do Not Have Car");
						}
					//TODO personGui.GoToRestaurantOne();
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
		if(jobs.get(i).job.equals("RestaurantCookWc"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantCookWc"))
				{			
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 4");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 4");
							print("Do Not Have Car");
						}
					//TODO personGui.GoToRestaurantOne();
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
		if(jobs.get(i).job.equals("RestaurantCashierWc"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantCashierWc"))
				{	
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 4");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 4");
							print("Do Not Have Car");
						}
					//TODO personGui.GoToRestaurantOne();
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
		if(jobs.get(i).job.equals("RestaurantNormalWaiterEs"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantNormalWaiterEs"))
				{	
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 5");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 5");
							print("Do Not Have Car");
						}
						// TODO personGui.GoToRestaurantOne();
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
		if(jobs.get(i).job.equals("RestaurantSpecialWaiterEs"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantSpecialWaiterEs"))
				{	
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 5");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 5");
							print("Do Not Have Car");
						}
						// TODO personGui.GoToRestaurantOne();
					try {
						moving.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					}
					location = AgentLocation.Restaurant;
					print("I am now a RestaurantSpecialWaiter");
					r.role.msgIsActive();
				}
			}
			//roles.WaiterRole.msgIsActive();
		}
		
		if(jobs.get(i).job.equals("RestaurantHostEs"))
		{
			for(MyRole r: roles)
			{
				
				if(r.description.equals("RestaurantHostEs"))
				{	
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 5");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 5");
							print("Do Not Have Car");
						}
					//TODO personGui.GoToRestaurantOne();
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
		if(jobs.get(i).job.equals("RestaurantCookEs"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantCookEs"))
				{			
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 5");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 5");
							print("Do Not Have Car");
						}
					//TODO personGui.GoToRestaurantOne();
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
		if(jobs.get(i).job.equals("RestaurantCashierEs"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantCashierEs"))
				{	
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 5");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 5");
							print("Do Not Have Car");
						}
					//TODO personGui.GoToRestaurantOne();
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
		if(jobs.get(i).job.equals("RestaurantNormalWaiterPs"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantNormalWaiterPs"))
				{	
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 6");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 6");
							print("Do Not Have Car");
						}
						// TODO personGui.GoToRestaurantOne();
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
		if(jobs.get(i).job.equals("RestaurantSpecialWaiterPs"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantSpecialWaiterPs"))
				{	
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 6");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 6");
							print("Do Not Have Car");
						}
						// TODO personGui.GoToRestaurantOne();
					try {
						moving.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					}
					location = AgentLocation.Restaurant;
					print("I am now a RestaurantSpecialWaiter");
					r.role.msgIsActive();
				}
			}
			//roles.WaiterRole.msgIsActive();
		}
		if(jobs.get(i).job.equals("RestaurantHostPs"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantHostPs"))
				{	
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 6");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 6");
							print("Do Not Have Car");
						}
						print("test");
					//TODO personGui.GoToRestaurantOne();
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
		if(jobs.get(i).job.equals("RestaurantCookPs"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantCookPs"))
				{			
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 6");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 6");
							print("Do Not Have Car");
						}
					//TODO personGui.GoToRestaurantOne();
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
		if(jobs.get(i).job.equals("RestaurantCashierPs"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantCashierPs"))
				{	
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant 6");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Restaurant 6");
							print("Do Not Have Car");
						}
					//TODO personGui.GoToRestaurantOne();
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
		if(jobs.get(i).job.equals("Teller"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("Teller"))
				{		
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Bank");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Bank");
							print("Do Not Have Car");
						}
					//TOOD personGui.goToBank();
					try {
						moving.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					}
					location = AgentLocation.Bank;
					print("I am now a " + r.description);
					r.role.msgIsActive();
				}
			}
			//personGui.setDestination(580, 322);
			//roles.TellerRole.msgIsActive();
		}
		if(jobs.get(i).job.equals("MarketCashier"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("MarketCashier"))
				{		
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Market");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Market");
							print("Do Not Have Car");
						}
					//TODO personGui.GoToMarket();
					try {
						moving.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					}
					location = AgentLocation.Market;
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
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Market");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination("Market");
							print("Do Not Have Car");
						}
					//TODO personGui.GoToMarket();
					try {
						moving.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					}
					location = AgentLocation.Market;
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

	@Override
	public Teller getTeller() {
		// TODO Auto-generated method stub
		return null;
	}
	public void addYelp(Yelp yelp) {
		this.yelp = yelp;
	}
}

		