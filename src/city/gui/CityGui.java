package city.gui;
import housing.Apartments;
import housing.House;
import housing.HouseType;
import housing.HousingRepairManRole;
import housing.HousingResidentRole;
import housing.gui.HouseAnimationPanel;
import housing.gui.HouseGui;

import javax.swing.*;
import javax.swing.Timer;

import bank.BankCustomerRole;
import bank.TellerRole;
import bank.gui.BankGui;
import market.MarketCashierRole;
import market.MarketCustomerRole;
import market.MarketEmployeeRole;
import market.gui.MarketGui;
import city.Bank;
import city.Market;
import city.Restaurant;
import city.gui.trace.AlertTag;
import people.PeopleAgent;
import people.Yelp;
import restaurant.*;
import restaurant.gui.RestaurantGui;
import restaurant.gui.RestaurantPanel.CookWaiterMonitor;
import restaurant.gui.RestaurantPanel;
import restaurant.gui.WaiterGui;
import restaurant_es.CashierRoleEs;
import restaurant_es.CookRoleEs;
import restaurant_es.HostRoleEs;
import restaurant_es.NormalWaiterRoleEs;
import restaurant_es.RestaurantCustomerRoleEs;
import restaurant_es.SpecialWaiterRoleEs;
import restaurant_es.gui.RestaurantGuiEs;
import restaurant_es.gui.RestaurantPanelEs;
import restaurant_es.gui.RestaurantPanelEs.CookWaiterMonitorEs;
import restaurant_ps.HostRolePS;
import restaurant_ps.RestaurantCustomerRolePS;
import restaurant_ps.CashierRolePS;
import restaurant_ps.CookRolePS;
import restaurant_ps.NormalWaiterRolePS;
import restaurant_ps.SpecialWaiterRolePS;
import restaurant_ps.gui.RestaurantGuiPS;
import restaurant_ps.gui.RestaurantPanelPS;
import restaurant_ps.gui.RestaurantPanelPS.CookWaiterMonitorPS;
//vk
import restaurant_vk.gui.*;
import restaurant_vk.*;
import restaurant_wc.CashierRoleWc;
import restaurant_wc.CookRoleWc;
import restaurant_wc.HostRoleWc;
import restaurant_wc.NormalWaiterRoleWc;
import restaurant_wc.RestaurantCustomerRoleWc;
import restaurant_wc.SpecialWaiterRoleWc;
import restaurant_wc.gui.RestaurantGuiWc;
import restaurant_wc.gui.RestaurantPanelWc;
import restaurant_wc.gui.RestaurantPanelWc.CookWaiterMonitorWc;
import restaurant_zt.CashierRoleZt;
import restaurant_zt.CookRoleZt;
import restaurant_zt.HostRoleZt;
import restaurant_zt.NormalWaiterRoleZt;
import restaurant_zt.RestaurantCustomerRoleZt;
import restaurant_zt.SpecialWaiterRoleZt;
import restaurant_zt.gui.RestaurantGuiZt;
import restaurant_zt.gui.RestaurantPanelZt;
import restaurant_zt.gui.RestaurantPanelZt.CookWaiterMonitorZt;
import transportation.BusAgent;
import transportation.BusStop;
import transportation.CarAgent;
import transportation.CarGui;
import transportation.CarPassengerRole;
import transportation.gui.BusGui;
import transportation.gui.BusStopGui;
import transportation.gui.InsideBusGui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class CityGui extends JFrame implements ActionListener {
	public int dayOfWeek = 0;
	public Timer timer;
	BankGui bankGui;
	CityPanel cityPanel;
	public JPanel buildingPanels;
	CardLayout cardLayout;
	CityControls cityControls;
	List<String> configParams = Collections
			.synchronizedList(new ArrayList<String>());
	RestaurantGui restaurantGuiYc;
	RestaurantGuiZt restaurantGuiZt;
	RestaurantGuiWc restaurantGuiWc;
	RestaurantGuiEs restaurantGuiEs;
	RestaurantGuiPS restaurantGuiPS;

	RestaurantVkAnimationPanel vkAnimationPanel;
	Yelp yelp = new Yelp();
	
	
	


	MarketGui marketGui;
	List<House> houses = new ArrayList<House>();
	List<HouseAnimationPanel> houseAnimationPanels = new ArrayList<HouseAnimationPanel>();
	List<HouseAnimationPanel> apartment1HouseAnimationPanels = new ArrayList<HouseAnimationPanel>();
	List<HouseAnimationPanel> apartment2HouseAnimationPanels = new ArrayList<HouseAnimationPanel>();

	Apartments apartment1 = new Apartments("a1");
	Apartments apartment2 = new Apartments("a2");
	

		
	
	ArrayList<PeopleAgent> people = new ArrayList<PeopleAgent>();
	ArrayList<BusAgent> buses = new ArrayList<BusAgent>();
	ArrayList<House> availableApartments = new ArrayList<House>();
	HostRole RestaurantHostRoleYc = new HostRole();
	HostRoleZt RestaurantHostRoleZt = new HostRoleZt();
	HostRoleWc RestaurantHostRoleWc = new HostRoleWc();
	HostRoleEs RestaurantHostRoleEs = new HostRoleEs();
	HostRolePS RestaurantHostRolePS = new HostRolePS();
	VkHostRole RestaurantHostRoleVk;





	MarketEmployeeRole MarketEmployeeRole;
	Restaurant restaurant = new Restaurant(RestaurantHostRoleYc, new Dimension(100, 100), "Restaurant 1",1);
	Restaurant restaurant2 = new Restaurant(RestaurantHostRoleVk, new Dimension(100,100), "Restaurant 2",2);
	Restaurant restaurant3 = new Restaurant(RestaurantHostRoleZt, new Dimension(100,100), "Restaurant 3",3);
	Restaurant restaurant4 = new Restaurant(RestaurantHostRoleWc, new Dimension(100,100), "Restaurant 4",4);
	Restaurant restaurant5 = new Restaurant(RestaurantHostRoleEs, new Dimension(100,100), "Restaurant 5",5);
	Restaurant restaurant6 = new Restaurant(RestaurantHostRolePS, new Dimension(100,100), "Restaurant 6",6);
	

	List<House> myHouses = new ArrayList<House>();
	
	//List of all workplaces so that they can be shutdown
	List<Bank> banks = new ArrayList<Bank>();
	List<Market> markets = new ArrayList<Market>();
	List<Restaurant> restaurants = new ArrayList<Restaurant>();

	Market market;
	TellerRole BankTellerRole;
	Bank bank = new Bank(BankTellerRole, new Dimension(100, 100), "Bank 1");
	HousingRepairManRole repairManRole = new HousingRepairManRole();
	Random rand = new Random();
	
	private int count = 0;

	public int time = 0000;
	CookWaiterMonitor RestaurantCookWaiterMonitor;
	CookWaiterMonitorZt RestaurantCookWaiterMonitorZT;
	CookWaiterMonitorWc RestaurantCookWaiterMonitorWc;
	CookWaiterMonitorEs RestaurantCookWaiterMonitorEs;
	CookWaiterMonitorPS RestaurantCookWaiterMonitorPS;
	RevolvingStand revolvingStand;

	public CityGui() {
		RestaurantHostRoleYc.setTag(AlertTag.RESTAURANT1);
		RestaurantHostRoleZt.setTag(AlertTag.RESTAURANT3);
		RestaurantHostRoleWc.setTag(AlertTag.RESTAURANT4);
		RestaurantHostRoleEs.setTag(AlertTag.RESTAURANT5); 
		RestaurantHostRolePS.setTag(AlertTag.RESTAURANT6);
		
		repairManRole.setTag(AlertTag.HOME);
		
		yelp.addRestaurant(restaurant, 5);
		yelp.addRestaurant(restaurant2, 3);
		yelp.addRestaurant(restaurant3, 4);
		yelp.addRestaurant(restaurant4, 5);
		yelp.addRestaurant(restaurant5, 3);
		yelp.addRestaurant(restaurant6, 4);
		
		
		apartment1.gui.ap.setCityGui(this);
		apartment2.gui.ap.setCityGui(this);

		cityPanel = new CityPanel(this);
		cityPanel.setPreferredSize(new Dimension(1024, 500));		
		cityPanel.setMaximumSize(new Dimension(1024, 500));
		cityPanel.setMinimumSize(new Dimension(1024, 500));
		
		timer = new Timer(5, this);
		
		bankGui = new BankGui(timer);
		BankTellerRole = new TellerRole(bankGui); 
		BankTellerRole.setTag(AlertTag.BANK);
		bank.t = BankTellerRole;
		restaurantGuiYc = new RestaurantGui(timer);
		restaurantGuiZt = new RestaurantGuiZt(timer);
		restaurantGuiWc = new RestaurantGuiWc(timer);
		restaurantGuiEs = new RestaurantGuiEs(timer);
		restaurantGuiPS = new RestaurantGuiPS(timer);
		vkAnimationPanel = new RestaurantVkAnimationPanel(timer);
		RestaurantHostRoleVk = new VkHostRole(vkAnimationPanel);
		RestaurantHostRoleVk.setTag(AlertTag.RESTAURANT2);
		restaurant2.h = RestaurantHostRoleVk;


		
		marketGui = new MarketGui(timer);
		MarketEmployeeRole = new MarketEmployeeRole(marketGui);
		MarketEmployeeRole.setTag(AlertTag.MARKET);
		market = new Market(MarketEmployeeRole, new Dimension(100,100),"Market 1"); 
		
		
		markets.add(market);
		banks.add(bank);
		restaurants.add(restaurant);
		restaurants.add(restaurant2);
		restaurants.add(restaurant3);
		restaurants.add(restaurant4);
		restaurants.add(restaurant5);
		restaurants.add(restaurant6);
		
		BankTellerRole.addAccount(market);
		BankTellerRole.addAccount(restaurant);
		BankTellerRole.addAccount(restaurant2);
		BankTellerRole.addAccount(restaurant3);
		BankTellerRole.addAccount(restaurant4);
		BankTellerRole.addAccount(restaurant5);
		BankTellerRole.addAccount(restaurant6);
		
		
		RestaurantPanel restPanel1 = new RestaurantPanel(restaurantGuiYc);
		RestaurantPanelZt restPanel2 = new RestaurantPanelZt(restaurantGuiZt);
		RestaurantPanelWc restPanel3 = new RestaurantPanelWc(restaurantGuiWc);
		RestaurantPanelEs restPanel4 = new RestaurantPanelEs(restaurantGuiEs);
		RestaurantPanelPS restPanel6 = new RestaurantPanelPS(restaurantGuiPS);
		
		//vk revolving stand
		revolvingStand = new RevolvingStand();
		
		
		
		restPanel1.setHost(RestaurantHostRoleYc);
		restPanel2.setHost(RestaurantHostRoleZt);
		restPanel3.setHost(RestaurantHostRoleWc);
		restPanel4.setHost(RestaurantHostRoleEs);
		restPanel6.setHost(RestaurantHostRolePS);
		RestaurantCookWaiterMonitor = restPanel1.theMonitor;
		RestaurantCookWaiterMonitorZT = restPanel2.theMonitor;
		RestaurantCookWaiterMonitorWc = restPanel3.theMonitor;
		RestaurantCookWaiterMonitorEs = restPanel4.theMonitor;
		RestaurantCookWaiterMonitorPS = restPanel6.theMonitor;

		for (int i=0; i<=11;i++) {
			House house = new House("House", 1, HouseType.Villa);
			myHouses.add(house);
			HouseGui houseGui = new HouseGui(house);
			house.setGui(houseGui);
			house.setItems();
			houseAnimationPanels.add(house.gui.hp);
		}
		
		
		CreateWorld(RestaurantCookWaiterMonitor,RestaurantCookWaiterMonitorZT,RestaurantCookWaiterMonitorWc,RestaurantCookWaiterMonitorEs, RestaurantCookWaiterMonitorPS, revolvingStand);

		setVisible(true);
		setSize(1024, 768);

		getContentPane().setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		cityControls = new CityControls(cityPanel, this);
		cityControls.setPreferredSize(new Dimension(500, 268));
		cityControls.setMaximumSize(new Dimension(500, 268));
		cityControls.setMinimumSize(new Dimension(500, 268));
		
		cardLayout = new CardLayout();

		buildingPanels = new JPanel();
		buildingPanels.setLayout(cardLayout);
		buildingPanels.setPreferredSize(new Dimension(500, 268));
		buildingPanels.setMaximumSize(new Dimension(500, 268));
		buildingPanels.setMinimumSize(new Dimension(500, 268));
		buildingPanels.setBackground(Color.yellow);


		// Create the BuildingPanel for each Building object
		ArrayList<Building> buildings = cityPanel.getBuildings();

		for (int i = 0; i < buildings.size(); i++) {
            Building b = buildings.get(i);
            BuildingPanel bp = new BuildingPanel(b, i, this);
            b.setBuildingPanel(bp);
		}
		for(int i = 0; i < cityPanel.busStops.size(); i++) {
			BusStop bs = cityPanel.busStops.get(i);
			bs.setBuildingNumber(i+buildings.size());
			BuildingPanel bp = new BuildingPanel(bs,i+buildings.size(),this);
			bs.setBuildingPanel(bp);
		}
			
		for(House h : apartment1.houses) {
			apartment1HouseAnimationPanels.add(h.gui.hp);
		}
		for(House h : apartment2.houses) {
			apartment2HouseAnimationPanels.add(h.gui.hp);

		}
			
		JScrollPane apartment1Container = new JScrollPane(apartment1.gui.ap);
		apartment1Container.setOpaque(true);
		JScrollPane apartment2Container = new JScrollPane(apartment2.gui.ap);
		apartment2Container.setOpaque(true);
		JScrollPane marketContainer = new JScrollPane(marketGui);
		marketContainer.setOpaque(true);
		JScrollPane restaurantContainer = new JScrollPane(restaurantGuiYc);
		restaurantContainer.setOpaque(true);
		JScrollPane restaurantContainerZT = new JScrollPane(restaurantGuiZt);
		restaurantContainerZT.setOpaque(true);
		JScrollPane restaurantContainerWc = new JScrollPane(restaurantGuiWc);
		restaurantContainerWc.setOpaque(true);
		JScrollPane restaurantContainerEs = new JScrollPane(restaurantGuiEs);
		restaurantContainerEs.setOpaque(true);
		JScrollPane restaurantContainerPS = new JScrollPane(restaurantGuiPS);
		restaurantContainerPS.setOpaque(true);
		JScrollPane restaurantContainerVK = new JScrollPane(vkAnimationPanel);
		restaurantContainerVK.setOpaque(true);
		JScrollPane bankContainer = new JScrollPane(bankGui);
		bankContainer.setOpaque(true);
		JScrollPane busStop1Container = new JScrollPane(this.cityPanel.busStops.get(0).getGui());
		busStop1Container.setOpaque(true);
		JScrollPane busStop2Container = new JScrollPane(this.cityPanel.busStops.get(1).getGui());
		busStop1Container.setOpaque(true);
		JScrollPane busStop3Container = new JScrollPane(this.cityPanel.busStops.get(2).getGui());
		busStop1Container.setOpaque(true);
		JScrollPane busStop4Container = new JScrollPane(this.cityPanel.busStops.get(3).getGui());
		busStop1Container.setOpaque(true);
		JScrollPane bus1Container = new JScrollPane(this.cityPanel.buses.get(0).getGui());
		bus1Container.setOpaque(true);
		
		
		buildingPanels.add(apartment1Container,"" + 13);
		buildingPanels.add(apartment2Container,"" + 12);

        buildingPanels.add(restaurantContainer, "" + 16);
        buildingPanels.add(restaurantContainerVK, "" + 17);
        buildingPanels.add(restaurantContainerZT, "" + 18);
        buildingPanels.add(restaurantContainerWc, "" + 19);
        buildingPanels.add(restaurantContainerEs, "" + 20);
        buildingPanels.add(restaurantContainerPS, "" + 21);


        buildingPanels.add(bankContainer, "" + 15);
        
        buildingPanels.add(marketContainer,"" + 14);
        
        buildingPanels.add(busStop1Container,"" + cityPanel.busStops.get(0).getBuildingNumber());
        buildingPanels.add(busStop2Container,"" + cityPanel.busStops.get(1).getBuildingNumber());
        buildingPanels.add(busStop3Container,"" + cityPanel.busStops.get(2).getBuildingNumber());
        buildingPanels.add(busStop4Container,"" + cityPanel.busStops.get(3).getBuildingNumber());
        
        

        
        for(int j = 0; j < houseAnimationPanels.size(); j++)
        {
        	if(j != 12) { //This handles outside houses ONLY
	        	JScrollPane houseContainer = new JScrollPane(houseAnimationPanels.get(j));
	    		houseContainer.setOpaque(true);
	    	
	    		buildingPanels.add(houseContainer, "" + j);
        	}
        }
        for(int j = 0; j < apartment1HouseAnimationPanels.size(); j++) {
        	JScrollPane apartmentHouseContainer = new JScrollPane(apartment1HouseAnimationPanels.get(j));
        	buildingPanels.add(apartmentHouseContainer, "a1" + j);

        }
        for(int j = 0; j < apartment2HouseAnimationPanels.size(); j++) {
        	JScrollPane apartmentHouseContainer = new JScrollPane(apartment2HouseAnimationPanels.get(j));
        	buildingPanels.add(apartmentHouseContainer, "a2" + j);

        }

        
        buildingPanels.setOpaque(true);
        cityControls.setOpaque(true);
        cityPanel.setOpaque(true);
		
	    c.gridx = 0; c.gridy = 0;
	    c.gridwidth = 2; c.gridheight = 1;
	    this.add(cityPanel, c);

	    c.gridx = 0; c.gridy = 1;
	    c.gridwidth = 1; c.gridheight = 1;
	    this.add(buildingPanels, c);

	    c.gridx = 1; c.gridy = 1;
	    c.gridwidth = 1; c.gridheight = 1;
	    this.add(cityControls, c);
	    
	    this.pack();
		
		timer.start();
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	}
	
	public void ClearWorld() {
		for(PeopleAgent person : people) {
			person.roles.clear();
			person.Arrived();
			person.stopThread();
			
		}
		people.clear();
		for(House house : houses) {
			house.gui.guis.clear();
			house.removeOccupant();
		}
		apartment1.gui.guis.clear();
		apartment2.gui.guis.clear();
		for(BusAgent bus : buses) {
			bus.stopThread();
		}
		buses.clear();
		houses.clear();
		cityPanel.vehicles.clear();
		cityPanel.people.clear();
		cityPanel.buses.clear();
		for(Lane lane : cityPanel.lanes) {
			lane.hasCar = false;
		}
		for(Sidewalk sidewalk : cityPanel.sidewalks) {
			sidewalk.hasPerson = false;
		}
		time = 0;
		timer.stop();
		count = 0;
		configParams.clear();
	}
	public void CreateWorld(CookWaiterMonitor RestaurantCookWaiterMonitor, CookWaiterMonitorZt RestaurantCookWaiterMonitorZT, CookWaiterMonitorWc RestaurantCookWaiterMonitorWc, CookWaiterMonitorEs RestaurantCookWaiterMonitorEs, CookWaiterMonitorPS RestaurantCookWaiterMonitorPS, RevolvingStand revolvingStand) {
		FileReader input = null;
		try {
			if(System.getProperty("file.separator").equals("/"))
			{
				input = new FileReader( "src//config.txt");
			}
			else if(System.getProperty("file.separator").equals("\\"))
			{
				input = new FileReader( "src\\config.txt");
			}
			BufferedReader bufRead = new BufferedReader(input);
			String line = null;
			while ((line = bufRead.readLine()) != null) {
				configParams.addAll(Arrays.asList(line.split("\\s*,\\s*")));
			}
			Iterator<String> configIteration = configParams.iterator();
			while (configIteration.hasNext()) {
				System.out.println(count);
				String amount = configIteration.next();
				String job = configIteration.next();
				String name = configIteration.next();
				int start = Integer.parseInt(configIteration.next());
				int end = Integer.parseInt(configIteration.next());
				if (isInteger(amount)) {
					PeopleAgent person;
//					if(rand.nextInt(5) < 2)
//					{
						 person = new PeopleAgent(name, 1000.0, false); //TODO
//					}
//					else
//					{
//						 person = new PeopleAgent(name, 1000.0, true );
//					}//TODO
					person.setCityGui(this);
					person.addYelp(yelp);
					PersonGui personGui;
					if(count == 0) {
						personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip29,cityPanel.sidewalkStrip29.get(1),cityPanel.allSidewalks, cityPanel, person);					
					}
					else if(count == 1) {
						personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip29,cityPanel.sidewalkStrip29.get(5),cityPanel.allSidewalks, cityPanel, person);					

					}
					else if(count == 2) {
						personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip29,cityPanel.sidewalkStrip29.get(10),cityPanel.allSidewalks, cityPanel, person);					

					}
					else if(count == 3) {
						personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip27,cityPanel.sidewalkStrip27.get(1),cityPanel.allSidewalks, cityPanel, person);					

					}
					else if(count == 4) {
						personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip27,cityPanel.sidewalkStrip27.get(5),cityPanel.allSidewalks, cityPanel, person);					

					}
					else if(count == 5) {
						personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip27,cityPanel.sidewalkStrip27.get(10),cityPanel.allSidewalks, cityPanel, person);					

					}
					else if(count == 6) {
						personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip30,cityPanel.sidewalkStrip30.get(8),cityPanel.allSidewalks, cityPanel, person);					

					}
					else if(count == 7) {
						personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip30,cityPanel.sidewalkStrip30.get(12),cityPanel.allSidewalks, cityPanel, person);					

					}
					else if(count == 8) {
						personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip30,cityPanel.sidewalkStrip30.get(16),cityPanel.allSidewalks, cityPanel, person);					

					}
					else if(count == 9) {
						personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip28,cityPanel.sidewalkStrip28.get(2),cityPanel.allSidewalks, cityPanel, person);					

					}
					else if(count == 10) {
						personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip28,cityPanel.sidewalkStrip28.get(6),cityPanel.allSidewalks, cityPanel, person);					

					}
					else if(count == 11) {
						personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip28,cityPanel.sidewalkStrip28.get(10),cityPanel.allSidewalks, cityPanel, person);					

					}
					else if(count == 12) {
						personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip28,cityPanel.sidewalkStrip28.get(18),cityPanel.allSidewalks, cityPanel, person);					

					}
					else {
						personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip30,cityPanel.sidewalkStrip30.get(24),cityPanel.allSidewalks, cityPanel, person);					

					}
					person.setPersonGui(personGui);
					person.Restaurants.add(restaurant);
					person.Restaurants.add(restaurant2);
					person.Restaurants.add(restaurant3);
					person.Restaurants.add(restaurant4);
					person.Restaurants.add(restaurant5);
					person.Restaurants.add(restaurant6);
					person.Banks.add(bank);
					person.Markets.add(market);
					RestaurantCustomerRole RestaurantCustomerRole = new RestaurantCustomerRole(restaurantGuiYc);
					RestaurantCustomerRoleZt RestaurantCustomerRoleZt = new RestaurantCustomerRoleZt(restaurantGuiZt);
					RestaurantCustomerRoleWc RestaurantCustomerRoleWc = new RestaurantCustomerRoleWc(restaurantGuiWc);
					RestaurantCustomerRoleEs RestaurantCustomerRoleEs = new RestaurantCustomerRoleEs(restaurantGuiEs);
					RestaurantCustomerRolePS RestaurantCustomerRolePs = new RestaurantCustomerRolePS(restaurantGuiPS);

					VkCustomerRole RestaurantCustomerRoleVk = new VkCustomerRole(vkAnimationPanel);
					MarketCustomerRole marketCustomerRole = new MarketCustomerRole(marketGui);
					person.addRole(marketCustomerRole, "MarketCustomer");
					marketCustomerRole.setPerson(person);
					
					CarAgent carAgent = new CarAgent();
					carAgent.startThread();
					CarPassengerRole carPassengerRole = new CarPassengerRole();
					person.addRole(carPassengerRole, "CarPassenger");
					carPassengerRole.setPerson(person);
					CarGui carGui = new CarGui(5,5,10,10, cityPanel.road2, cityPanel.road2.get(0), cityPanel.allRoads, cityPanel);
					cityPanel.vehicles.add(carGui);
					carAgent.setGui(carGui);
					carPassengerRole.setCar(carAgent);
					
					RestaurantCustomerRole.setTag(AlertTag.RESTAURANT1);
					person.addRole(RestaurantCustomerRole,"RestaurantCustomer");
					RestaurantCustomerRole.setPerson(person);
					
					RestaurantCustomerRoleVk.setTag(AlertTag.RESTAURANT2);
					person.addRole(RestaurantCustomerRoleVk, "RestaurantCustomerVk");
					RestaurantCustomerRoleVk.setPerson(person);
					
					RestaurantCustomerRoleWc.setTag(AlertTag.RESTAURANT3);
					person.addRole(RestaurantCustomerRoleZt, "RestaurantCustomerZt");
					RestaurantCustomerRoleZt.setPerson(person);
				
					RestaurantCustomerRoleWc.setTag(AlertTag.RESTAURANT4);
					person.addRole(RestaurantCustomerRoleWc, "RestaurantCustomerWc");
					RestaurantCustomerRoleWc.setPerson(person);
					
					RestaurantCustomerRoleEs.setTag(AlertTag.RESTAURANT5);
					person.addRole(RestaurantCustomerRoleEs, "RestaurantCustomerEs");
					RestaurantCustomerRoleWc.setPerson(person);
					
					RestaurantCustomerRolePs.setTag(AlertTag.RESTAURANT6);
					person.addRole(RestaurantCustomerRolePs, "RestaurantCustomerPs");
					RestaurantCustomerRoleWc.setPerson(person);
										
					
					BankCustomerRole bankCustomerRole = new BankCustomerRole(bankGui);
					
					
					
					bankCustomerRole.setTag(AlertTag.BANK);
					cityPanel.people.add(personGui);
					person.addRole(bankCustomerRole,"BankCustomer");
					bankCustomerRole.setPerson(person);
					
//					int extraPeople = 0;
					HousingResidentRole residentRole = new HousingResidentRole();
					if(count <= 11) {
						House house = myHouses.get(count);
						houses.add(house);
						house.setOccupant(residentRole);
						residentRole.setHouse(house);
						System.out.println("Person added to villa");
					}
					else if(count <= 36){
//						extraPeople++;
						House apartmentHouse = apartment1.getAvailableApartment();
						houses.add(apartmentHouse);
						apartmentHouse.setOccupant(residentRole);
						residentRole.setHouse(apartmentHouse);
					}
					else
					{
						House apartmentHouse2 = apartment2.getAvailableApartment();
						houses.add(apartmentHouse2);
						apartmentHouse2.setOccupant(residentRole);
						residentRole.setHouse(apartmentHouse2);
					}
					
					
					residentRole.setTag(AlertTag.HOME);
					//residentRole.testModeOn();
					residentRole.setPerson(person);
					residentRole.isActive = true;
					residentRole.setRepairMan(repairManRole);
					person.addRole(residentRole, "Resident");
					
					person.HomeNum = count;
//					if(!(job.equals("BankRestaurantMarket"))){
//						person.setTest();
//					}
					count++;
					person.startThread();		
					
					
					if (job.equals("RestaurantNormalWaiter")) {
						NormalWaiterRole RestaurantNormalWaiterRole = new NormalWaiterRole(restaurantGuiYc);
						RestaurantNormalWaiterRole.setTag(AlertTag.RESTAURANT1);
						person.addJob("RestaurantNormalWaiter", start, end);
						person.addRole(RestaurantNormalWaiterRole,"RestaurantNormalWaiter");
						RestaurantNormalWaiterRole.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantNormalWaiterVk")) {
						VkWaiterNormalRole RestaurantNormalWaiterRoleVK = new VkWaiterNormalRole(RestaurantHostRoleVk);
						RestaurantHostRoleVk.addWaiter(RestaurantNormalWaiterRoleVK);						
						person.addJob("RestaurantNormalWaiterVk", start, end);
						person.addRole(RestaurantNormalWaiterRoleVK,"RestaurantNormalWaiterVk");
						RestaurantNormalWaiterRoleVK.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantNormalWaiterZt")) {
						NormalWaiterRoleZt RestaurantNormalWaiterRoleZT = new NormalWaiterRoleZt(restaurantGuiZt);						
						person.addJob("RestaurantNormalWaiterZt", start, end);
						person.addRole(RestaurantNormalWaiterRoleZT,"RestaurantNormalWaiterZt");
						RestaurantNormalWaiterRoleZT.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantNormalWaiterWc")) {
						NormalWaiterRoleWc RestaurantNormalWaiterRoleWc = new NormalWaiterRoleWc(restaurantGuiWc);
						RestaurantNormalWaiterRoleWc.setTag(AlertTag.RESTAURANT4);
						person.addJob("RestaurantNormalWaiterWc", start, end);
						person.addRole(RestaurantNormalWaiterRoleWc,"RestaurantNormalWaiterWc");
						RestaurantNormalWaiterRoleWc.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantNormalWaiterEs")) {
						NormalWaiterRoleEs RestaurantNormalWaiterRoleEs = new NormalWaiterRoleEs(restaurantGuiEs);						
						person.addJob("RestaurantNormalWaiterEs", start, end);
						person.addRole(RestaurantNormalWaiterRoleEs,"RestaurantNormalWaiterEs");
						RestaurantNormalWaiterRoleEs.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantNormalWaiterPs")) {
						NormalWaiterRolePS RestaurantNormalWaiterRolePS = new NormalWaiterRolePS(restaurantGuiPS);						
						person.addJob("RestaurantNormalWaiterPs", start, end);
						person.addRole(RestaurantNormalWaiterRolePS,"RestaurantNormalWaiterPs");
						RestaurantNormalWaiterRolePS.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantSpecialWaiter")) {
						SpecialWaiterRole RestaurantSpecialWaiterRole = new SpecialWaiterRole(RestaurantCookWaiterMonitor   ,restaurantGuiYc);
						RestaurantSpecialWaiterRole.setTag(AlertTag.RESTAURANT1);
						person.addJob("RestaurantSpecialWaiter", start, end);
						person.addRole(RestaurantSpecialWaiterRole,"RestaurantSpecialWaiter");
						RestaurantSpecialWaiterRole.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantSpecialWaiterVk")) {
						VkWaiterSpecialRole RestaurantSpecialWaiterRoleVk = new VkWaiterSpecialRole(RestaurantHostRoleVk,revolvingStand);
						RestaurantHostRoleVk.addWaiter(RestaurantSpecialWaiterRoleVk);						
						person.addJob("RestaurantSpecialWaiterVk", start, end);
						person.addRole(RestaurantSpecialWaiterRoleVk,"RestaurantSpecialWaiterVk");
						RestaurantSpecialWaiterRoleVk.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantSpecialWaiterZt")) {
						SpecialWaiterRoleZt RestaurantSpecialWaiterRoleZt = new SpecialWaiterRoleZt(RestaurantCookWaiterMonitorZT,restaurantGuiZt);
						RestaurantSpecialWaiterRoleZt.setTag(AlertTag.RESTAURANT3);
						person.addJob("RestaurantSpecialWaiterZt", start, end);
						person.addRole(RestaurantSpecialWaiterRoleZt,"RestaurantSpecialWaiterZt");
						RestaurantSpecialWaiterRoleZt.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantSpecialWaiterWc")) {
						SpecialWaiterRoleWc RestaurantSpecialWaiterRoleWc = new SpecialWaiterRoleWc(RestaurantCookWaiterMonitorWc,restaurantGuiWc);
						RestaurantSpecialWaiterRoleWc.setTag(AlertTag.RESTAURANT4);
						person.addJob("RestaurantSpecialWaiterWc", start, end);
						person.addRole(RestaurantSpecialWaiterRoleWc,"RestaurantSpecialWaiterWc");
						RestaurantSpecialWaiterRoleWc.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantSpecialWaiterEs")) {
						SpecialWaiterRoleEs RestaurantSpecialWaiterRoleEs = new SpecialWaiterRoleEs(RestaurantCookWaiterMonitorEs,restaurantGuiEs);
						RestaurantSpecialWaiterRoleEs.setTag(AlertTag.RESTAURANT5);
						person.addJob("RestaurantSpecialWaiterEs", start, end);
						person.addRole(RestaurantSpecialWaiterRoleEs,"RestaurantSpecialWaiterEs");
						RestaurantSpecialWaiterRoleEs.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantSpecialWaiterPs")) {
						SpecialWaiterRolePS RestaurantSpecialWaiterRolePs = new SpecialWaiterRolePS(RestaurantCookWaiterMonitorPS,restaurantGuiPS);
						RestaurantSpecialWaiterRolePs.setTag(AlertTag.RESTAURANT6);
						person.addJob("RestaurantSpecialWaiterPs", start, end);
						person.addRole(RestaurantSpecialWaiterRolePs,"RestaurantSpecialWaiterPs");
						RestaurantSpecialWaiterRolePs.setPerson(person);
						person.hasCar = false;
					}					
					if (job.equals("RestaurantCook")) {
						CookRole RestaurantCookRole = new CookRole(RestaurantCookWaiterMonitor, restaurantGuiYc);
						
						RestaurantCookRole.setTag(AlertTag.RESTAURANT1);
						
						person.addJob("RestaurantCook", start, end);
						person.addRole(RestaurantCookRole, "RestaurantCook");
						RestaurantCookRole.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantCookZt")) {
						CookRoleZt RestaurantCookRoleZT = new CookRoleZt(RestaurantCookWaiterMonitorZT, restaurantGuiZt);
						
						RestaurantCookRoleZT.setTag(AlertTag.RESTAURANT1);
						
						person.addJob("RestaurantCookZt", start, end);
						person.addRole(RestaurantCookRoleZT, "RestaurantCookZt");
						RestaurantCookRoleZT.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantCookWc")) {
						CookRoleWc RestaurantCookRoleWc = new CookRoleWc(RestaurantCookWaiterMonitorWc, restaurantGuiWc);
						RestaurantCookRoleWc.setTag(AlertTag.RESTAURANT1);
						person.addJob("RestaurantCookWc", start, end);
						person.addRole(RestaurantCookRoleWc, "RestaurantCookWc");
						RestaurantCookRoleWc.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantCookEs")) {
						CookRoleEs RestaurantCookRoleEs = new CookRoleEs(RestaurantCookWaiterMonitorEs, restaurantGuiEs);
						
						RestaurantCookRoleEs.setTag(AlertTag.RESTAURANT1);
						
						person.addJob("RestaurantCookEs", start, end);
						person.addRole(RestaurantCookRoleEs, "RestaurantCookEs");
						RestaurantCookRoleEs.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantCookPs")) {
						CookRolePS RestaurantCookRolePS = new CookRolePS(RestaurantCookWaiterMonitorPS, restaurantGuiPS);					
						RestaurantCookRolePS.setTag(AlertTag.RESTAURANT1);
						person.addJob("RestaurantCookPs", start, end);
						person.addRole(RestaurantCookRolePS, "RestaurantCookPs");
						RestaurantCookRolePS.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantCookVk")) {
						VkCookRole RestaurantCookRoleVK = new VkCookRole(revolvingStand, vkAnimationPanel);
						RestaurantCookRoleVK.setTag(AlertTag.RESTAURANT1);
						RestaurantCookRoleVK.setHost(RestaurantHostRoleVk);
						RestaurantHostRoleVk.setCook(RestaurantCookRoleVK);
						person.addJob("RestaurantCookVk", start, end);
						person.addRole(RestaurantCookRoleVK, "RestaurantCookVk");
						RestaurantCookRoleVK.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantHost")) {
						person.addJob("RestaurantHost", start, end);
						person.addRole(RestaurantHostRoleYc, "RestaurantHost");
						RestaurantHostRoleYc.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantHostZt")) {
						person.addJob("RestaurantHostZt", start, end);
						person.addRole(RestaurantHostRoleZt, "RestaurantHostZt");
						RestaurantHostRoleZt.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantHostEs")) {
						person.addJob("RestaurantHostEs", start, end);
						person.addRole(RestaurantHostRoleEs, "RestaurantHostEs");
						RestaurantHostRoleEs.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantHostPs")) {
						person.addJob("RestaurantHostPs", start, end);
						person.addRole(RestaurantHostRolePS, "RestaurantHostPs");
						RestaurantHostRolePS.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantHostWc")) {
						person.addJob("RestaurantHostWc", start, end);
						person.addRole(RestaurantHostRoleWc, "RestaurantHostWc");
						RestaurantHostRoleWc.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantHostVk")) {
						person.addJob("RestaurantHostVk", start, end);
						person.addRole(RestaurantHostRoleVk, "RestaurantHostVk");
						RestaurantHostRoleVk.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantCashier")) {
						CashierRole RestaurantCashierRole = new CashierRole(restaurantGuiYc);
						RestaurantCashierRole.setTag(AlertTag.RESTAURANT1);
						person.addJob("RestaurantCashier", start, end);
						person.addRole(RestaurantCashierRole,"RestaurantCashier");
						RestaurantCashierRole.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantCashierZt")) {
						CashierRoleZt RestaurantCashierRoleZT = new CashierRoleZt(restaurantGuiZt);
											
						person.addJob("RestaurantCashierZt", start, end);
						person.addRole(RestaurantCashierRoleZT,"RestaurantCashierZt");
						RestaurantCashierRoleZT.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantCashierWc")) {
						CashierRoleWc RestaurantCashierRoleWc = new CashierRoleWc(restaurantGuiWc);
						RestaurantCashierRoleWc.setTag(AlertTag.RESTAURANT1);
						person.addJob("RestaurantCashierWc", start, end);
						person.addRole(RestaurantCashierRoleWc,"RestaurantCashierWc");
						RestaurantCashierRoleWc.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantCashierEs")) {
						CashierRoleEs RestaurantCashierRoleEs = new CashierRoleEs(restaurantGuiEs);
						person.addJob("RestaurantCashierEs", start, end);
						person.addRole(RestaurantCashierRoleEs,"RestaurantCashierEs");
						RestaurantCashierRoleEs.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantCashierPs")) {
						CashierRolePS RestaurantCashierRolePS = new CashierRolePS(restaurantGuiPS);
						person.addJob("RestaurantCashierPs", start, end);
						person.addRole(RestaurantCashierRolePS,"RestaurantCashierPs");
						RestaurantCashierRolePS.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantCashierVk")) {
						VkCashierRole RestaurantCashierRoleVK = new VkCashierRole(vkAnimationPanel);
						RestaurantCashierRoleVK.setHost(RestaurantHostRoleVk);
						RestaurantCashierRoleVK.setTag(AlertTag.RESTAURANT1);
						RestaurantHostRoleVk.setCashier(RestaurantCashierRoleVK);	
						person.addJob("RestaurantCashierVk", start, end);
						person.addRole(RestaurantCashierRoleVK,"RestaurantCashierVk");
						RestaurantCashierRoleVK.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantCustomer"))
					{
						person.hasCar = false;
					}
					if (job.equals("Teller")) {
						person.addJob("Teller", start, end);
						person.addRole(BankTellerRole, "Teller");
						BankTellerRole.setPerson(person);	
//						person.hasCar = true;
					}
					if(job.equals("Nobody")) {
						person.addJob("MarketEmployee", start, end);
						person.addRole(MarketEmployeeRole,"MarketEmployee");
						MarketEmployeeRole.setPerson(person);
						person.setMoney(1000000);
//						person.hasCar = true;
					}
					if(job.equals("RepairMan"))
					{
						person.addJob("RepairMan", start, end);
						person.addRole(repairManRole,"RepairMan");
						repairManRole.setPerson(person);
						person.setMoney(1000000);
//						person.hasCar = true;
					}
					if(job.equals("MarketEmployee"))
					{
						person.addJob("MarketEmployee", start,end);
						person.addRole(MarketEmployeeRole, "MarketEmployee");
						MarketEmployeeRole.setPerson(person);
						person.setMoney(10000);
//						person.hasCar = true;
					}
					if(job.equals("MarketCashier"))
					{
						MarketCashierRole marketCashierRole = new MarketCashierRole(marketGui);
						person.addJob("MarketCashier", start, end);
						person.addRole(marketCashierRole, "MarketCashier");
						marketCashierRole.setPerson(person);
						person.setMoney(1000);
//						person.hasCar = true;
						person.hasCar = false;
					}
					if(job.equals("MarketCustomer"))
					{
						MarketCashierRole marketCashierRole = new MarketCashierRole(marketGui);
						person.addJob("MarketCashier", start, end);
						person.addRole(marketCashierRole, "MarketCashier");
						marketCashierRole.setPerson(person);
						person.setMoney(1000);
						person.hasCar = false;
					}
					if(job.equals("BankRestaurantMarket"))
					{
						MarketCashierRole marketCashierRole = new MarketCashierRole(marketGui);
						person.addJob("MarketCashier", start, end);
						person.addRole(marketCashierRole, "MarketCashier");
						person.setType("NormativeB1");
						person.setMoney(1000000);
						person.hasCar = false;
					}
					if(job.equals("MarketRestaurantBank"))
					{
						MarketCashierRole marketCashierRole = new MarketCashierRole(marketGui);
						person.addJob("MarketCashier", start, end);
						person.addRole(marketCashierRole, "MarketCashier");
						person.setType("NormativeB2");
						person.setMoney(50000);
						person.hasCar = true;
					}
					if(job.equals("BankMarketRestaurant"))
					{
						MarketCashierRole marketCashierRole = new MarketCashierRole(marketGui);
						person.addJob("MarketCashier", start, end);
						person.addRole(marketCashierRole, "MarketCashier");
						person.setType("NormativeB3");
						person.setMoney(50000);
						person.hasCar = false;
					}
					
				
				
					people.add(person);
					
				}
			}
			bufRead.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BusAgent busAgent = new BusAgent();
		buses.add(busAgent);
		BusStopGui busStopGui = new BusStopGui();
		cityPanel.busStops.add(new BusStop(busStopGui,220,180,30,30,220,152,new ArrayList<String>(Arrays.asList("Home 1","Home 2",
				"Home 3","Home 4","Home 5","Home 6","Home 7","Home 8","Home 9","Home 10","Home 11","Home 12","Apartment 1","Apartment 2")), "BusStop 1"));
		busStopGui = new BusStopGui();
		cityPanel.busStops.add(new BusStop(busStopGui,680,350,30,30,680,322,new ArrayList<String>(Arrays.asList("Market 1","Market 2")), "BusStop 2"));
		busStopGui = new BusStopGui();
		cityPanel.busStops.add(new BusStop(busStopGui,880,90,30,30,870,132,new ArrayList<String>(Arrays.asList("Restaurant 6","Restaurant 1", "Restaurant 3", "Restaurant 4")), "BusStop 3"));
		busStopGui = new BusStopGui();
		cityPanel.busStops.add(new BusStop(busStopGui,650,90,30,30,660,132,new ArrayList<String>(Arrays.asList("Bank","Restaurant 2", "Restaurant 5")), "BusStop 4"));
		busStopGui = new BusStopGui();
		
		InsideBusGui igb = new InsideBusGui();
		BusGui bg = new BusGui(igb,5, 5, 10, 10, cityPanel.road2, cityPanel.road2.get(0), cityPanel.allRoads, cityPanel);
		cityPanel.buses.add(bg);
		busAgent.setGui(bg);
		busAgent.startThread();
		bg.msgGoToNextStop(busAgent, cityPanel.busStops.get(cityPanel.busStops.size()-1));
		cityPanel.vehicles.add(bg);
		timer.start();
	}
	
	public void createNormativeB(CookWaiterMonitor RestaurantCookWaiterMonitor, CookWaiterMonitorZt RestaurantCookWaiterMonitorZT, CookWaiterMonitorWc RestaurantCookWaiterMonitorWc, CookWaiterMonitorEs RestaurantCookWaiterMonitorEs, CookWaiterMonitorPS RestaurantCookWaiterMonitorPS, RevolvingStand revolvingStand) {
		FileReader input = null;
		try {
			if(System.getProperty("file.separator").equals("/"))
			{
				input = new FileReader( "src//normativeBaseLineB.txt");
			}
			else if(System.getProperty("file.separator").equals("\\"))
			{
				input = new FileReader( "src\\normativeBaseLineB.txt");
			}
			BufferedReader bufRead = new BufferedReader(input);
			String line = null;
			while ((line = bufRead.readLine()) != null) {
				configParams.addAll(Arrays.asList(line.split("\\s*,\\s*")));
			}
			Iterator<String> configIteration = configParams.iterator();
			while (configIteration.hasNext()) {
				String amount = configIteration.next();
				String job = configIteration.next();
				String name = configIteration.next();
				int start = Integer.parseInt(configIteration.next());
				int end = Integer.parseInt(configIteration.next());
				if (isInteger(amount)) {
					PeopleAgent person;
//					if(rand.nextInt(5) < 2)
//					{
						 person = new PeopleAgent(name, 1000.0, false); //TODO
//					}
//					else
//					{
//						 person = new PeopleAgent(name, 1000.0, true );
//					}//TODO
					person.setCityGui(this);
					person.addYelp(yelp);
					PersonGui personGui;
					if(count == 0) {
						personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip29,cityPanel.sidewalkStrip29.get(1),cityPanel.allSidewalks, cityPanel, person);					
					}
					else if(count == 1) {
						personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip29,cityPanel.sidewalkStrip29.get(5),cityPanel.allSidewalks, cityPanel, person);					

					}
					else if(count == 2) {
						personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip29,cityPanel.sidewalkStrip29.get(10),cityPanel.allSidewalks, cityPanel, person);					

					}
					else if(count == 3) {
						personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip27,cityPanel.sidewalkStrip27.get(1),cityPanel.allSidewalks, cityPanel, person);					

					}
					else if(count == 4) {
						personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip27,cityPanel.sidewalkStrip27.get(5),cityPanel.allSidewalks, cityPanel, person);					

					}
					else if(count == 5) {
						personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip27,cityPanel.sidewalkStrip27.get(10),cityPanel.allSidewalks, cityPanel, person);					

					}
					else if(count == 6) {
						personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip30,cityPanel.sidewalkStrip30.get(8),cityPanel.allSidewalks, cityPanel, person);					

					}
					else if(count == 7) {
						personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip30,cityPanel.sidewalkStrip30.get(12),cityPanel.allSidewalks, cityPanel, person);					

					}
					else if(count == 8) {
						personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip30,cityPanel.sidewalkStrip30.get(16),cityPanel.allSidewalks, cityPanel, person);					

					}
					else if(count == 9) {
						personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip28,cityPanel.sidewalkStrip28.get(2),cityPanel.allSidewalks, cityPanel, person);					

					}
					else if(count == 10) {
						personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip28,cityPanel.sidewalkStrip28.get(6),cityPanel.allSidewalks, cityPanel, person);					

					}
					else if(count == 11) {
						personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip28,cityPanel.sidewalkStrip28.get(10),cityPanel.allSidewalks, cityPanel, person);					

					}
					else if(count == 12) {
						personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip28,cityPanel.sidewalkStrip28.get(18),cityPanel.allSidewalks, cityPanel, person);					

					}
					else {
						personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip30,cityPanel.sidewalkStrip30.get(24),cityPanel.allSidewalks, cityPanel, person);					

					}
					personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip1,cityPanel.sidewalkStrip1.get(0),cityPanel.allSidewalks, cityPanel, person);					
					person.setPersonGui(personGui);
					person.Restaurants.add(restaurant);
					person.Restaurants.add(restaurant2);
					person.Restaurants.add(restaurant3);
					person.Restaurants.add(restaurant4);
					person.Restaurants.add(restaurant5);
					person.Restaurants.add(restaurant6);
					person.Banks.add(bank);
					person.Markets.add(market);
					RestaurantCustomerRole RestaurantCustomerRole = new RestaurantCustomerRole(restaurantGuiYc);
					RestaurantCustomerRoleZt RestaurantCustomerRoleZt = new RestaurantCustomerRoleZt(restaurantGuiZt);
					RestaurantCustomerRoleWc RestaurantCustomerRoleWc = new RestaurantCustomerRoleWc(restaurantGuiWc);
					RestaurantCustomerRoleEs RestaurantCustomerRoleEs = new RestaurantCustomerRoleEs(restaurantGuiEs);
					RestaurantCustomerRolePS RestaurantCustomerRolePs = new RestaurantCustomerRolePS(restaurantGuiPS);

					VkCustomerRole RestaurantCustomerRoleVk = new VkCustomerRole(vkAnimationPanel);
					MarketCustomerRole marketCustomerRole = new MarketCustomerRole(marketGui);
					person.addRole(marketCustomerRole, "MarketCustomer");
					marketCustomerRole.setPerson(person);
					
					CarAgent carAgent = new CarAgent();
					carAgent.startThread();
					CarPassengerRole carPassengerRole = new CarPassengerRole();
					person.addRole(carPassengerRole, "CarPassenger");
					carPassengerRole.setPerson(person);
					CarGui carGui = new CarGui(5,5,10,10, cityPanel.road2, cityPanel.road2.get(0), cityPanel.allRoads, cityPanel);
					cityPanel.vehicles.add(carGui);
					carAgent.setGui(carGui);
					carPassengerRole.setCar(carAgent);
					
					RestaurantCustomerRole.setTag(AlertTag.RESTAURANT1);
					person.addRole(RestaurantCustomerRole,"RestaurantCustomer");
					RestaurantCustomerRole.setPerson(person);
					
					RestaurantCustomerRoleVk.setTag(AlertTag.RESTAURANT2);
					person.addRole(RestaurantCustomerRoleVk, "RestaurantCustomerVk");
					RestaurantCustomerRoleVk.setPerson(person);
					
					person.addRole(RestaurantCustomerRoleZt, "RestaurantCustomerZt");
					RestaurantCustomerRoleZt.setPerson(person);
					
					RestaurantCustomerRoleWc.setTag(AlertTag.RESTAURANT4);
					person.addRole(RestaurantCustomerRoleWc, "RestaurantCustomerWc");
					RestaurantCustomerRoleWc.setPerson(person);
					
					RestaurantCustomerRoleEs.setTag(AlertTag.RESTAURANT5);
					person.addRole(RestaurantCustomerRoleEs, "RestaurantCustomerEs");
					RestaurantCustomerRoleWc.setPerson(person);
					
					RestaurantCustomerRolePs.setTag(AlertTag.RESTAURANT5);
					person.addRole(RestaurantCustomerRolePs, "RestaurantCustomerPs");
					RestaurantCustomerRoleWc.setPerson(person);
										
					
					BankCustomerRole bankCustomerRole = new BankCustomerRole(bankGui);
					
					
					
					bankCustomerRole.setTag(AlertTag.RESTAURANT1);
					cityPanel.people.add(personGui);
					person.addRole(bankCustomerRole,"BankCustomer");
					bankCustomerRole.setPerson(person);
					
//					int extraPeople = 0;
					HousingResidentRole residentRole = new HousingResidentRole();
					if(count <= 11) {
						House house = myHouses.get(count);
						houses.add(house);
						house.setOccupant(residentRole);
						residentRole.setHouse(house);
						System.out.println("Person added to villa");
					}
					else if(count <= 36){
//						extraPeople++;
						House apartmentHouse = apartment1.getAvailableApartment();
						houses.add(apartmentHouse);
						apartmentHouse.setOccupant(residentRole);
						residentRole.setHouse(apartmentHouse);
					}
					else
					{
						House apartmentHouse2 = apartment2.getAvailableApartment();
						houses.add(apartmentHouse2);
						apartmentHouse2.setOccupant(residentRole);
						residentRole.setHouse(apartmentHouse2);
					}
					
					
					residentRole.setTag(AlertTag.HOME);
					//residentRole.testModeOn();
					residentRole.setPerson(person);
					residentRole.isActive = true;
					residentRole.setRepairMan(repairManRole);
					person.addRole(residentRole, "Resident");
					
					person.HomeNum = count;
					count++;
					person.startThread();
					
					
					
					if (job.equals("RestaurantNormalWaiter")) {
						NormalWaiterRole RestaurantNormalWaiterRole = new NormalWaiterRole(restaurantGuiYc);
						RestaurantNormalWaiterRole.setTag(AlertTag.RESTAURANT1);
						person.addJob("RestaurantNormalWaiter", start, end);
						person.addRole(RestaurantNormalWaiterRole,"RestaurantNormalWaiter");
						RestaurantNormalWaiterRole.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantNormalWaiterVk")) {
						VkWaiterNormalRole RestaurantNormalWaiterRoleVK = new VkWaiterNormalRole(RestaurantHostRoleVk);
						RestaurantHostRoleVk.addWaiter(RestaurantNormalWaiterRoleVK);						
						person.addJob("RestaurantNormalWaiterVk", start, end);
						person.addRole(RestaurantNormalWaiterRoleVK,"RestaurantNormalWaiterVk");
						RestaurantNormalWaiterRoleVK.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantNormalWaiterZt")) {
						NormalWaiterRoleZt RestaurantNormalWaiterRoleZT = new NormalWaiterRoleZt(restaurantGuiZt);						
						person.addJob("RestaurantNormalWaiterZt", start, end);
						person.addRole(RestaurantNormalWaiterRoleZT,"RestaurantNormalWaiterZt");
						RestaurantNormalWaiterRoleZT.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantNormalWaiterWc")) {
						NormalWaiterRoleWc RestaurantNormalWaiterRoleWc = new NormalWaiterRoleWc(restaurantGuiWc);
						RestaurantNormalWaiterRoleWc.setTag(AlertTag.RESTAURANT4);
						person.addJob("RestaurantNormalWaiterWc", start, end);
						person.addRole(RestaurantNormalWaiterRoleWc,"RestaurantNormalWaiterWc");
						RestaurantNormalWaiterRoleWc.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantNormalWaiterEs")) {
						NormalWaiterRoleEs RestaurantNormalWaiterRoleEs = new NormalWaiterRoleEs(restaurantGuiEs);						
						person.addJob("RestaurantNormalWaiterEs", start, end);
						person.addRole(RestaurantNormalWaiterRoleEs,"RestaurantNormalWaiterEs");
						RestaurantNormalWaiterRoleEs.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantNormalWaiterPs")) {
						NormalWaiterRolePS RestaurantNormalWaiterRolePS = new NormalWaiterRolePS(restaurantGuiPS);						
						person.addJob("RestaurantNormalWaiterPs", start, end);
						person.addRole(RestaurantNormalWaiterRolePS,"RestaurantNormalWaiterPs");
						RestaurantNormalWaiterRolePS.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantCook")) {
						CookRole RestaurantCookRole = new CookRole(RestaurantCookWaiterMonitor, restaurantGuiYc);
						
						RestaurantCookRole.setTag(AlertTag.RESTAURANT1);
						
						person.addJob("RestaurantCook", start, end);
						person.addRole(RestaurantCookRole, "RestaurantCook");
						RestaurantCookRole.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantCookZt")) {
						CookRoleZt RestaurantCookRoleZT = new CookRoleZt(RestaurantCookWaiterMonitorZT, restaurantGuiZt);
						
						RestaurantCookRoleZT.setTag(AlertTag.RESTAURANT1);
						
						person.addJob("RestaurantCookZt", start, end);
						person.addRole(RestaurantCookRoleZT, "RestaurantCookZt");
						RestaurantCookRoleZT.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantCookWc")) {
						CookRoleWc RestaurantCookRoleWc = new CookRoleWc(RestaurantCookWaiterMonitorWc, restaurantGuiWc);
						RestaurantCookRoleWc.setTag(AlertTag.RESTAURANT1);
						person.addJob("RestaurantCookWc", start, end);
						person.addRole(RestaurantCookRoleWc, "RestaurantCookWc");
						RestaurantCookRoleWc.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantCookEs")) {
						CookRoleEs RestaurantCookRoleEs = new CookRoleEs(RestaurantCookWaiterMonitorEs, restaurantGuiEs);
						
						RestaurantCookRoleEs.setTag(AlertTag.RESTAURANT1);
						
						person.addJob("RestaurantCookEs", start, end);
						person.addRole(RestaurantCookRoleEs, "RestaurantCookEs");
						RestaurantCookRoleEs.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantCookPs")) {
						CookRolePS RestaurantCookRolePS = new CookRolePS(RestaurantCookWaiterMonitorPS, restaurantGuiPS);					
						RestaurantCookRolePS.setTag(AlertTag.RESTAURANT1);
						person.addJob("RestaurantCookPs", start, end);
						person.addRole(RestaurantCookRolePS, "RestaurantCookPs");
						RestaurantCookRolePS.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantCookVk")) {
						VkCookRole RestaurantCookRoleVK = new VkCookRole(revolvingStand, vkAnimationPanel);
						RestaurantCookRoleVK.setTag(AlertTag.RESTAURANT1);
						RestaurantCookRoleVK.setHost(RestaurantHostRoleVk);
						RestaurantHostRoleVk.setCook(RestaurantCookRoleVK);
						person.addJob("RestaurantCookVk", start, end);
						person.addRole(RestaurantCookRoleVK, "RestaurantCookVk");
						RestaurantCookRoleVK.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantHost")) {
						person.addJob("RestaurantHost", start, end);
						person.addRole(RestaurantHostRoleYc, "RestaurantHost");
						RestaurantHostRoleYc.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantHostZt")) {
						person.addJob("RestaurantHostZt", start, end);
						person.addRole(RestaurantHostRoleZt, "RestaurantHostZt");
						RestaurantHostRoleZt.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantHostEs")) {
						person.addJob("RestaurantHostEs", start, end);
						person.addRole(RestaurantHostRoleEs, "RestaurantHostEs");
						RestaurantHostRoleEs.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantHostPs")) {
						person.addJob("RestaurantHostPs", start, end);
						person.addRole(RestaurantHostRolePS, "RestaurantHostPs");
						RestaurantHostRolePS.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantHostWc")) {
						person.addJob("RestaurantHostWc", start, end);
						person.addRole(RestaurantHostRoleWc, "RestaurantHostWc");
						RestaurantHostRoleWc.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantHostVk")) {
						person.addJob("RestaurantHostVk", start, end);
						person.addRole(RestaurantHostRoleVk, "RestaurantHostVk");
						RestaurantHostRoleVk.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantCashier")) {
						CashierRole RestaurantCashierRole = new CashierRole(restaurantGuiYc);
						RestaurantCashierRole.setTag(AlertTag.RESTAURANT1);
						person.addJob("RestaurantCashier", start, end);
						person.addRole(RestaurantCashierRole,"RestaurantCashier");
						RestaurantCashierRole.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantCashierZt")) {
						CashierRoleZt RestaurantCashierRoleZT = new CashierRoleZt(restaurantGuiZt);
											
						person.addJob("RestaurantCashierZt", start, end);
						person.addRole(RestaurantCashierRoleZT,"RestaurantCashierZt");
						RestaurantCashierRoleZT.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantCashierWc")) {
						CashierRoleWc RestaurantCashierRoleWc = new CashierRoleWc(restaurantGuiWc);
						RestaurantCashierRoleWc.setTag(AlertTag.RESTAURANT1);
						person.addJob("RestaurantCashierWc", start, end);
						person.addRole(RestaurantCashierRoleWc,"RestaurantCashierWc");
						RestaurantCashierRoleWc.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantCashierEs")) {
						CashierRoleEs RestaurantCashierRoleEs = new CashierRoleEs(restaurantGuiEs);
						person.addJob("RestaurantCashierEs", start, end);
						person.addRole(RestaurantCashierRoleEs,"RestaurantCashierEs");
						RestaurantCashierRoleEs.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantCashierPs")) {
						CashierRolePS RestaurantCashierRolePS = new CashierRolePS(restaurantGuiPS);
						person.addJob("RestaurantCashierPs", start, end);
						person.addRole(RestaurantCashierRolePS,"RestaurantCashierPs");
						RestaurantCashierRolePS.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantCashierVk")) {
						VkCashierRole RestaurantCashierRoleVK = new VkCashierRole(vkAnimationPanel);
						RestaurantCashierRoleVK.setHost(RestaurantHostRoleVk);
						RestaurantCashierRoleVK.setTag(AlertTag.RESTAURANT1);
						RestaurantHostRoleVk.setCashier(RestaurantCashierRoleVK);	
						person.addJob("RestaurantCashierVk", start, end);
						person.addRole(RestaurantCashierRoleVK,"RestaurantCashierVk");
						RestaurantCashierRoleVK.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantCustomer"))
					{
						person.hasCar = false;
					}
					if (job.equals("Teller")) {
						person.addJob("Teller", start, end);
						person.addRole(BankTellerRole, "Teller");
						BankTellerRole.setPerson(person);	
//						person.hasCar = true;
					}
					if(job.equals("Nobody")) {
						person.addJob("MarketEmployee", start, end);
						person.addRole(MarketEmployeeRole,"MarketEmployee");
						MarketEmployeeRole.setPerson(person);
						person.setMoney(1000000);
//						person.hasCar = true;
					}
					if(job.equals("RepairMan"))
					{
						person.addJob("RepairMan", start, end);
						person.addRole(repairManRole,"RepairMan");
						repairManRole.setPerson(person);
						person.setMoney(1000000);
//						person.hasCar = true;
					}
					if(job.equals("MarketEmployee"))
					{
						person.addJob("MarketEmployee", start,end);
						person.addRole(MarketEmployeeRole, "MarketEmployee");
						MarketEmployeeRole.setPerson(person);
						person.setMoney(10000);
//						person.hasCar = true;
					}
					if(job.equals("MarketCashier"))
					{
						MarketCashierRole marketCashierRole = new MarketCashierRole(marketGui);
						person.addJob("MarketCashier", start, end);
						person.addRole(marketCashierRole, "MarketCashier");
						marketCashierRole.setPerson(person);
						person.setMoney(1000);
//						person.hasCar = true;
						person.hasCar = false;
					}
					if(job.equals("MarketCustomer"))
					{
						MarketCashierRole marketCashierRole = new MarketCashierRole(marketGui);
						person.addJob("MarketCashier", start, end);
						person.addRole(marketCashierRole, "MarketCashier");
						marketCashierRole.setPerson(person);
						person.setMoney(1000);
						person.hasCar = false;
					}
					if(job.equals("NormativeB1"))
					{
						person.addJob("MarketEmployee", start, end);
						person.setMoney(100000);
						((PeopleAgent)person).setType(job);
						person.hasCar = false;
					}
					if(job.equals("NormativeB2"))
					{
						person.addJob("MarketCashier", start, end);
						person.setMoney(100000);
						((PeopleAgent)person).setType(job);
						person.hasCar = false;
					}
					if(job.equals("NormativeB3"))
					{
						person.addJob("MarketCashier", start, end);
						person.addRole(repairManRole, "RepairMan");
						repairManRole.setPerson(person);
						person.setMoney(100000);
						((PeopleAgent)person).setType(job);
//						person.hasCar = true;
					}
				
					people.add(person);
					
				}
			}
			bufRead.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BusAgent busAgent = new BusAgent();
		buses.add(busAgent);
		BusStopGui busStopGui = new BusStopGui();
		cityPanel.busStops.add(new BusStop(busStopGui,220,180,30,30,220,152,new ArrayList<String>(Arrays.asList("Home 1","Home 2",
				"Home 3","Home 4","Home 5","Home 6","Home 7","Home 8","Home 9","Home 10","Home 11","Home 12","Apartment 1","Apartment 2")), "BusStop 1"));
		busStopGui = new BusStopGui();
		cityPanel.busStops.add(new BusStop(busStopGui,680,350,30,30,680,322,new ArrayList<String>(Arrays.asList("Market 1","Market 2")), "BusStop 2"));
		busStopGui = new BusStopGui();
		cityPanel.busStops.add(new BusStop(busStopGui,880,90,30,30,870,132,new ArrayList<String>(Arrays.asList("Restaurant 6","Restaurant 1", "Restaurant 3", "Restaurant 4")), "BusStop 3"));
		busStopGui = new BusStopGui();
		cityPanel.busStops.add(new BusStop(busStopGui,650,90,30,30,660,132,new ArrayList<String>(Arrays.asList("Bank","Restaurant 2", "Restaurant 5")), "BusStop 4"));
		busStopGui = new BusStopGui();
		
		InsideBusGui igb = new InsideBusGui();
		BusGui bg = new BusGui(igb,5, 5, 10, 10, cityPanel.road2, cityPanel.road2.get(0), cityPanel.allRoads, cityPanel);
		cityPanel.buses.add(bg);
		busAgent.setGui(bg);
		busAgent.startThread();
		bg.msgGoToNextStop(busAgent, cityPanel.busStops.get(cityPanel.busStops.size()-1));
		cityPanel.vehicles.add(bg);
		timer.start();
	}
	
	public void closeBanks() {
		for (Bank bank : banks) {
			bank.isClosed = true;
		}
	}
	public void createNonNormG() {
		CookRole c = (CookRole) RestaurantHostRoleYc.getCook();
		c.setLow();
	}

	public void closeMarkets() {
		for (Market market : markets) {
			market.isClosed = true;
		}
	}
	
	public void closeRestaurants() {
		for (Restaurant rest : restaurants) {
			rest.isClosed = true;
		}
	}
	
	public void displayBuildingPanel(BuildingPanel bp) {
		cardLayout.show(buildingPanels, bp.getName());
		System.out.println(bp.getName());
	}
	public void displayBuildingPanel(String name) {
		cardLayout.show(buildingPanels, name);
		System.out.println(name);
	}

	public static void main(String[] args) {
		CityGui sc = new CityGui();

	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		bankGui.updatePosition();
		marketGui.updatePosition();
		restaurantGuiYc.updatePosition();
		restaurantGuiZt.updatePosition();
		restaurantGuiWc.updatePosition();
		restaurantGuiEs.updatePosition();
		restaurantGuiPS.updatePosition();
		vkAnimationPanel.updatePosition();
		for(int i = 0; i < houseAnimationPanels.size(); i++)
		{
			houseAnimationPanels.get(i).updatePosition();
		}
		for(int j = 0; j < apartment1HouseAnimationPanels.size(); j++)
		{
			apartment1HouseAnimationPanels.get(j).gui.updatePosition();
		}
		for(int k = 0; k < apartment1HouseAnimationPanels.size(); k++)
		{
			apartment2HouseAnimationPanels.get(k).gui.updatePosition();
		}
		int x = 20;
		time++;
		if(time%x == 0)
		{
			if(time%(x) == 0)
			{
				cityPanel.setTime(time/x);
				System.out.println(time/x);
			}
			for (PeopleAgent p : people) {
				p.msgTimeIs(time/x);
			}
		}
//		if((time % (100*x)+x) == 60*x) {
//			time += 40*x;
//		}
		if(dayOfWeek == 7) {
			dayOfWeek = 0;
		}
		if(time == 2400*x) {
			time=0;
			dayOfWeek++;
		}
		if(dayOfWeek == 5 && time == 0) {
			for(Bank bank : banks) {
				bank.isClosed = true;
			}
		}
		if(dayOfWeek == 6 && time == 0) {
			for(Bank bank :banks) {
				bank.isClosed = true;
			}
		}
		repaint();

	}

	public void startNormalScenario3() {
		RestaurantHostRoleVk.cook.setLow();
		RestaurantHostRoleYc.getCook().setLow();
		RestaurantHostRoleZt.getCook().setLow();
		RestaurantHostRoleWc.getCook().setLow();
		RestaurantHostRoleEs.getCook().setLow();
		RestaurantHostRolePS.getCook().setLow();
		
	}

	public void triggerVehicleCrash() {
		// TODO Auto-generated method stub
		List<CarGui> currentCars = new ArrayList<CarGui>();
		for(VehicleGui v : cityPanel.vehicles)
		{
			if(v.typeOfVehicle.equals("Car"))
				currentCars.add((CarGui)v);
		}
		int maxVehicleListIndex = currentCars.size() - 1;
		Random random = new Random();
		int randomVehicleIndex = random.nextInt(maxVehicleListIndex);
		CarGui carToStop = currentCars.get(randomVehicleIndex);
		carToStop.stopNow();
		
	}

	public boolean isPedestrianCrossingStreet() {
		// TODO Auto-generated method stub
		if(cityPanel.sidewalkStrip23.get(0).hasPerson || cityPanel.sidewalkStrip23.get(1).hasPerson)
			return true;
		
		
		return false;
	}

	public void stopPedestriansCrossingStreet() {
		// TODO Auto-generated method stub

		if(cityPanel.sidewalkStrip23.get(0).hasPerson)
			cityPanel.sidewalkStrip23.get(0).getPerson().getPersonGui().stopNow();
		if(cityPanel.sidewalkStrip23.get(1).hasPerson)
			cityPanel.sidewalkStrip23.get(1).getPerson().getPersonGui().stopNow();

	}
	
	
}

