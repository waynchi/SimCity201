package city.gui;
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
	BankGui bankGui = new BankGui();
	CityPanel cityPanel;
	JPanel buildingPanels;
	CardLayout cardLayout;
	CityControls cityControls;
	List<String> configParams = Collections
			.synchronizedList(new ArrayList<String>());
	RestaurantGui restaurantGui = new RestaurantGui();
	MarketGui marketGui = new MarketGui();
	//HouseAnimationPanel houseAnimationPanel = new HouseAnimationPanel();
	List<HouseAnimationPanel> houseAnimationPanels = new ArrayList<HouseAnimationPanel>();
	
	ArrayList<PeopleAgent> people = new ArrayList<PeopleAgent>();
	HostRole RestaurantHostRole = new HostRole();
	MarketEmployeeRole MarketEmployeeRole = new MarketEmployeeRole(marketGui);
	Restaurant restaurant = new Restaurant(RestaurantHostRole, new Dimension(100, 100), "Restaurant 1");
	Market market = new Market(MarketEmployeeRole, new Dimension(100,100),"Market 1"); 
	TellerRole BankTellerRole = new TellerRole(bankGui); 
	Bank bank = new Bank(BankTellerRole, new Dimension(100, 100), "Bank 1");
	HousingRepairManRole repairManRole = new HousingRepairManRole();


	public int time;

	public CityGui() {
		//this.setResizable(false);
		cityPanel = new CityPanel(this);
		cityPanel.setPreferredSize(new Dimension(1024, 500));		
		cityPanel.setMaximumSize(new Dimension(1024, 500));
		cityPanel.setMinimumSize(new Dimension(1024, 500));
		
		Timer timer = new Timer(1, this);
		
		//Set trace tags
		RestaurantHostRole.setTag(AlertTag.RESTAURANT);
		repairManRole.setTag(AlertTag.HOME);
		BankTellerRole.setTag(AlertTag.BANK);
		MarketEmployeeRole.setTag(AlertTag.MARKET);
		
		BankTellerRole.addAccount(market);
		BankTellerRole.addAccount(restaurant);
		RestaurantPanel restPanel = new RestaurantPanel(restaurantGui);
		restPanel.setHost(RestaurantHostRole);
		CookWaiterMonitor RestaurantCookWaiterMonitor = restPanel.theMonitor;

		FileReader input = null;
		try {
			System.out.println(System.getProperty("file.separator"));
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
					PeopleAgent person = new PeopleAgent(name, 1000.0, true); //TODO
					person.setCityGui(this);
					PersonGui personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip1,cityPanel.sidewalkStrip1.get(0),cityPanel.allSidewalks, cityPanel);					
					//personGui.setDestination(130, 200);
					person.setPersonGui(personGui);
					person.Restaurants.add(restaurant);
					person.Banks.add(bank);
					person.Markets.add(market);
					cityPanel.people.add(personGui);
					RestaurantCustomerRole RestaurantCustomerRole = new RestaurantCustomerRole(restaurantGui);
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
					
					RestaurantCustomerRole.setTag(AlertTag.RESTAURANT);
					
					person.addRole(RestaurantCustomerRole,"RestaurantCustomer");
					RestaurantCustomerRole.setPerson(person);
					BankCustomerRole bankCustomerRole = new BankCustomerRole(bankGui);
					
					
					
					bankCustomerRole.setTag(AlertTag.RESTAURANT);
					
					person.addRole(bankCustomerRole,"BankCustomer");
					bankCustomerRole.setPerson(person);
					HouseAnimationPanel houseAnimationPanel = new HouseAnimationPanel();
					houseAnimationPanels.add(houseAnimationPanel);
					House house = new House("House", 1, HouseType.Villa);
					HousingResidentRole residentRole = new HousingResidentRole();
					house.setItems();
					HouseGui hGui = new HouseGui(house);
					house.setGui(hGui);
					house.setOccupant(residentRole);
					residentRole.setTag(AlertTag.HOME);
					houseAnimationPanel.addGui(house.gui);
					residentRole.testModeOn();
					residentRole.setPerson(person);
					residentRole.isActive = true;
					residentRole.setRepairMan(repairManRole);
					residentRole.setHouse(house);
					person.addRole(residentRole, "Resident");
					person.startThread();
					//setTest();
					person.setTest();
					
					if (job.equals("RestaurantNormalWaiter")) {
						NormalWaiterRole RestaurantNormalWaiterRole = new NormalWaiterRole(restaurantGui);
						
						RestaurantNormalWaiterRole.setTag(AlertTag.RESTAURANT);
						
						//WaiterGui g = new WaiterGui(RestaurantNormalWaiterRole);
						//RestaurantNormalWaiterRole.setGui(g);
						person.addJob("RestaurantNormalWaiter", start, end);
						person.addRole(RestaurantNormalWaiterRole,"RestaurantNormalWaiter");
						RestaurantNormalWaiterRole.setPerson(person);
					}
					if (job.equals("RestaurantCook")) {
						CookRole RestaurantCookRole = new CookRole(RestaurantCookWaiterMonitor, restaurantGui);
						
						RestaurantCookRole.setTag(AlertTag.RESTAURANT);
						
						person.addJob("RestaurantCook", start, end);
						person.addRole(RestaurantCookRole, "RestaurantCook");
						RestaurantCookRole.setPerson(person);
					}
					if (job.equals("RestaurantHost")) {
						person.addJob("RestaurantHost", start, end);
						person.addRole(RestaurantHostRole, "RestaurantHost");
						RestaurantHostRole.setPerson(person);
					}
					if (job.equals("RestaurantCashier")) {
						CashierRole RestaurantCashierRole = new CashierRole(restaurantGui);
						
						RestaurantCashierRole.setTag(AlertTag.RESTAURANT);
						
						person.addJob("RestaurantCashier", start, end);
						person.addRole(RestaurantCashierRole,"RestaurantCashier");
						RestaurantCashierRole.setPerson(person);
					}
					if (job.equals("RestaurantCustomer"))
					{
						
					}
					if (job.equals("Teller")) {
						person.addJob("Teller", start, end);
						person.addRole(BankTellerRole, "Teller");
						BankTellerRole.setPerson(person);			
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
					}
					if(job.equals("MarketCashier"))
					{
						MarketCashierRole marketCashierRole = new MarketCashierRole();
						person.addJob("MarketCashier", start, end);
						person.addRole(marketCashierRole, "MarketCashier");
						marketCashierRole.setPerson(person);
						person.setMoney(1000);
					}
					if(job.equals("MarketCustomer"))
					{
						MarketCashierRole marketCashierRole = new MarketCashierRole();
						person.addJob("MarketCashier", start, end);
						person.addRole(marketCashierRole, "MarketCashier");
						marketCashierRole.setPerson(person);
						person.setMoney(40000);
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
//		for (PeopleAgent p : people) {
//			p.Restaurants.add(restaurant);
//			p.Markets.add(market);
//			p.Banks.add(bank);
//		}
		setVisible(true);
		setSize(1024, 768);

		getContentPane().setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		cityControls = new CityControls();
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
		
		JScrollPane marketContainer = new JScrollPane(marketGui);
		marketContainer.setOpaque(true);
		JScrollPane restaurantContainer = new JScrollPane(restaurantGui);
		restaurantContainer.setOpaque(true);
		JScrollPane bankContainer = new JScrollPane(bankGui);
		bankContainer.setOpaque(true);
		
        buildingPanels.add(restaurantContainer, "" + 15);
        buildingPanels.add(bankContainer, "" + 14);
        
        buildingPanels.add(marketContainer,"" + 13);
        for(int j = 0; j < houseAnimationPanels.size(); j++)
        {
        	JScrollPane houseContainer = new JScrollPane(houseAnimationPanels.get(j));
    		houseContainer.setOpaque(true);
    		buildingPanels.add(houseContainer, "" + j);
        }


		//getContentPane().add(BorderLayout.WEST, cityControls);
		//getContentPane().add(BorderLayout.NORTH, cityPanel);
		//getContentPane().add(BorderLayout.SOUTH, buildingPanels);
        
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
		restaurantGui.updatePosition();
		for(int i = 0; i < houseAnimationPanels.size(); i++)
		{
			houseAnimationPanels.get(i).updatePosition();
		}
		
		int x = 10;
		time++;
		if(time % x == 0)
		{
			if(time%60 == 0)
			{
				System.out.println(time/x);
			}
			for (PeopleAgent p : people) {
				p.msgTimeIs(time/x);
			}
		}

		if(time == 2400*x) {
			time=0;
		}
		repaint();

	}
}

