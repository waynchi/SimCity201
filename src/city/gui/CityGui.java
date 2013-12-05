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
import restaurant.*;
import restaurant.gui.RestaurantGui;
import restaurant.gui.RestaurantPanel.CookWaiterMonitor;
import restaurant.gui.RestaurantPanel;
import restaurant.gui.WaiterGui;
import transportation.CarAgent;
import transportation.CarGui;
import transportation.CarPassengerRole;

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
	BankGui bankGui;
	CityPanel cityPanel;
	public JPanel buildingPanels;
	CardLayout cardLayout;
	CityControls cityControls;
	List<String> configParams = Collections
			.synchronizedList(new ArrayList<String>());
	RestaurantGui restaurantGui1 = new RestaurantGui();



	MarketGui marketGui = new MarketGui();
	List<HouseAnimationPanel> houseAnimationPanels = new ArrayList<HouseAnimationPanel>();
	List<HouseAnimationPanel> apartment1HouseAnimationPanels = new ArrayList<HouseAnimationPanel>();
	List<HouseAnimationPanel> apartment2HouseAnimationPanels = new ArrayList<HouseAnimationPanel>();

	Apartments apartment1 = new Apartments("a1");
	Apartments apartment2 = new Apartments("a2");
	

		
	
	ArrayList<PeopleAgent> people = new ArrayList<PeopleAgent>();
	HostRole RestaurantHostRole1 = new HostRole();




	MarketEmployeeRole MarketEmployeeRole = new MarketEmployeeRole(marketGui);
	Restaurant restaurant = new Restaurant(RestaurantHostRole1, new Dimension(100, 100), "Restaurant 1");




	
	Market market = new Market(MarketEmployeeRole, new Dimension(100,100),"Market 1"); 
	TellerRole BankTellerRole = new TellerRole(bankGui); 
	Bank bank = new Bank(BankTellerRole, new Dimension(100, 100), "Bank 1");
	HousingRepairManRole repairManRole = new HousingRepairManRole();
	Random rand = new Random();
	
	public Timer timer;

	private int count = 0;

	public int time;

	public CityGui() {
		apartment1.gui.ap.setCityGui(this);
		apartment2.gui.ap.setCityGui(this);

		cityPanel = new CityPanel(this);
		cityPanel.setPreferredSize(new Dimension(1024, 500));		
		cityPanel.setMaximumSize(new Dimension(1024, 500));
		cityPanel.setMinimumSize(new Dimension(1024, 500));
		
		timer = new Timer(5, this);
		
		bankGui = new BankGui(timer);

		
		//Set trace tags
		RestaurantHostRole1.setTag(AlertTag.RESTAURANT1);



		repairManRole.setTag(AlertTag.HOME);
		BankTellerRole.setTag(AlertTag.BANK);
		MarketEmployeeRole.setTag(AlertTag.MARKET);
		
		BankTellerRole.addAccount(market);
		BankTellerRole.addAccount(restaurant);
		RestaurantPanel restPanel1 = new RestaurantPanel(restaurantGui1);
		restPanel1.setHost(RestaurantHostRole1);
		CookWaiterMonitor RestaurantCookWaiterMonitor = restPanel1.theMonitor;

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
					PersonGui personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip1,cityPanel.sidewalkStrip1.get(0),cityPanel.allSidewalks, cityPanel, person);					
					person.setPersonGui(personGui);
					person.Restaurants.add(restaurant);
					person.Banks.add(bank);
					person.Markets.add(market);
					RestaurantCustomerRole RestaurantCustomerRole = new RestaurantCustomerRole(restaurantGui1);
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
					BankCustomerRole bankCustomerRole = new BankCustomerRole(bankGui);
					
					
					
					bankCustomerRole.setTag(AlertTag.RESTAURANT1);
					cityPanel.people.add(personGui);
					person.addRole(bankCustomerRole,"BankCustomer");
					bankCustomerRole.setPerson(person);
					House house = new House("House", 1, HouseType.Villa);
					HouseGui houseGui = new HouseGui(house);
					house.setGui(houseGui);
					house.setItems();
					houseAnimationPanels.add(house.gui.hp);
					HousingResidentRole residentRole = new HousingResidentRole();
					
					house.setOccupant(residentRole);
					residentRole.setTag(AlertTag.HOME);
					//residentRole.testModeOn();
					residentRole.setPerson(person);
					residentRole.isActive = true;
					residentRole.setRepairMan(repairManRole);
					residentRole.setHouse(house);
					person.addRole(residentRole, "Resident");
					
					person.HomeNum = count;
					count++;
					person.startThread();
					if (job.equals("RestaurantNormalWaiter")) {
						NormalWaiterRole RestaurantNormalWaiterRole = new NormalWaiterRole(restaurantGui1);
						
						RestaurantNormalWaiterRole.setTag(AlertTag.RESTAURANT1);
						
						person.addJob("RestaurantNormalWaiter", start, end);
						person.addRole(RestaurantNormalWaiterRole,"RestaurantNormalWaiter");
						RestaurantNormalWaiterRole.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantCook")) {
						CookRole RestaurantCookRole = new CookRole(RestaurantCookWaiterMonitor, restaurantGui1);
						
						RestaurantCookRole.setTag(AlertTag.RESTAURANT1);
						
						person.addJob("RestaurantCook", start, end);
						person.addRole(RestaurantCookRole, "RestaurantCook");
						RestaurantCookRole.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantHost")) {
						person.addJob("RestaurantHost", start, end);
						person.addRole(RestaurantHostRole1, "RestaurantHost");
						RestaurantHostRole1.setPerson(person);
						person.hasCar = false;
					}
					if (job.equals("RestaurantCashier")) {
						CashierRole RestaurantCashierRole = new CashierRole(restaurantGui1);
						
						RestaurantCashierRole.setTag(AlertTag.RESTAURANT1);
						
						person.addJob("RestaurantCashier", start, end);
						person.addRole(RestaurantCashierRole,"RestaurantCashier");
						RestaurantCashierRole.setPerson(person);
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
						person.hasCar = true;
					}
					if(job.equals("Nobody")) {
						person.addJob("MarketEmployee", start, end);
						person.addRole(MarketEmployeeRole,"MarketEmployee");
						MarketEmployeeRole.setPerson(person);
						person.setMoney(1000000);
						person.hasCar = true;
					}
					if(job.equals("RepairMan"))
					{
						person.addJob("RepairMan", start, end);
						person.addRole(repairManRole,"RepairMan");
						repairManRole.setPerson(person);
						person.setMoney(1000000);
						person.hasCar = true;
					}
					if(job.equals("MarketEmployee"))
					{
						person.addJob("MarketEmployee", start,end);
						person.addRole(MarketEmployeeRole, "MarketEmployee");
						MarketEmployeeRole.setPerson(person);
						person.setMoney(10000);
						person.hasCar = true;
					}
					if(job.equals("MarketCashier"))
					{
						MarketCashierRole marketCashierRole = new MarketCashierRole(marketGui);
						person.addJob("MarketCashier", start, end);
						person.addRole(marketCashierRole, "MarketCashier");
						marketCashierRole.setPerson(person);
						person.setMoney(1000);
						person.hasCar = true;
					}
					if(job.equals("MarketCustomer"))
					{
						MarketCashierRole marketCashierRole = new MarketCashierRole(marketGui);
						person.addJob("MarketCashier", start, end);
						person.addRole(marketCashierRole, "MarketCashier");
						marketCashierRole.setPerson(person);
						person.setMoney(40000);
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
		JScrollPane restaurantContainer = new JScrollPane(restaurantGui1);
		restaurantContainer.setOpaque(true);
		JScrollPane bankContainer = new JScrollPane(bankGui);
		bankContainer.setOpaque(true);
		
		
		buildingPanels.add(apartment1Container,"" + 13);
		buildingPanels.add(apartment2Container,"" + 12);

        buildingPanels.add(restaurantContainer, "" + 16);
        buildingPanels.add(bankContainer, "" + 15);
        
        buildingPanels.add(marketContainer,"" + 14);
        
         
        for(int j = 0; j < houseAnimationPanels.size(); j++)
        {
        	if(j != 12) {
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

	public void displayBuildingPanel(BuildingPanel bp) {
		cardLayout.show(buildingPanels, bp.getName());
		System.out.println(bp.getName());
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
		restaurantGui1.updatePosition();
		for(int i = 0; i < houseAnimationPanels.size(); i++)
		{
			houseAnimationPanels.get(i).updatePosition();
		}
		
		int x = 10;
		time++;
		if(time % x == 0)
		{
			if(time%(x*10) == 0)
			{
				System.out.println(time/(x));
			}
			for (PeopleAgent p : people) {
				p.msgTimeIs(time/x);
			}
		}
		if(time % 100 == 60) {
			time += 40;
		}
		if(time == 2400*x) {
			time=0;
		}
		repaint();

	}
}

