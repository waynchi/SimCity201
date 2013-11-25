package city.gui;
import javax.swing.*;
import javax.swing.Timer;

import bank.TellerRole;
import bank.gui.BankGui;
import market.MarketEmployeeRole;
import market.gui.MarketGui;
import city.Bank;
import city.Market;
import city.Restaurant;
import people.PeopleAgent;
import restaurant.*;
import restaurant.gui.RestaurantGui;
import restaurant.gui.RestaurantPanel.CookWaiterMonitor;
import restaurant.gui.RestaurantPanel;
import restaurant.gui.WaiterGui;

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
	ArrayList<PeopleAgent> people = new ArrayList<PeopleAgent>();
	HostRole RestaurantHostRole = new HostRole();
	MarketEmployeeRole MarketEmployeeRole = new MarketEmployeeRole(marketGui);
	Restaurant restaurant = new Restaurant(RestaurantHostRole, new Dimension(100, 100), "Restaurant 1");
	Market market = new Market(MarketEmployeeRole, new Dimension(100,100),"Market 1"); 
	TellerRole BankTellerRole = new TellerRole(bankGui);
	Bank bank = new Bank(BankTellerRole, new Dimension(100, 100), "Bank 1");
	
	


	public int time;

	public CityGui() {

		cityPanel = new CityPanel(this);
		cityPanel.setPreferredSize(new Dimension(500, 500));
		cityPanel.setMaximumSize(new Dimension(500, 500));
		cityPanel.setMinimumSize(new Dimension(500, 500));
		
		
		Timer timer = new Timer(10, this);
		RestaurantPanel restPanel = new RestaurantPanel(restaurantGui,
				RestaurantHostRole);
		restPanel.setHost(RestaurantHostRole);
		CookWaiterMonitor RestaurantCookWaiterMonitor = restPanel.theMonitor;
		MarketEmployeeRole RestaurantMarketRole = new MarketEmployeeRole(marketGui);

		FileReader input;
		try {
			input = new FileReader("config.txt");
			BufferedReader bufRead = new BufferedReader(input);
			String line = null;
			while ((line = bufRead.readLine()) != null) {
				configParams.addAll(Arrays.asList(line.split("\\s*,\\s*")));
			}
			Iterator<String> configIteration = configParams.iterator();
			while (configIteration.hasNext()) {
				String amount = configIteration.next();
				String role = configIteration.next();
				String name = configIteration.next();
				if (isInteger(amount)) {
					PeopleAgent person = new PeopleAgent(name, 1000.0, false);
					person.setCityGui(this);
					PersonGui personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalks.get(29),cityPanel.sidewalks,cityPanel,person,cityPanel.buildings.get(0));
					person.setPersonGui(personGui);
					cityPanel.people.add(personGui);
					person.startThread();
					if (role.equals("RestaurantNormalWaiter")) {
						NormalWaiterRole RestaurantNormalWaiterRole = new NormalWaiterRole(restaurantGui);
						WaiterGui g = new WaiterGui(RestaurantNormalWaiterRole);
						RestaurantNormalWaiterRole.setGui(g);
						person.addJob("RestaurantNormalWaiter", 1000, 2000);
						person.addRole(RestaurantNormalWaiterRole,"RestaurantNormalWaiter");
						RestaurantNormalWaiterRole.setPerson(person);
					}
					if (role.equals("RestaurantCook")) {
						CookRole RestaurantCookRole = new CookRole(RestaurantCookWaiterMonitor, restaurantGui);
						person.addJob("RestaurantCook", 1000, 2000);
						person.addRole(RestaurantCookRole, "RestaurantCook");
						RestaurantCookRole.setPerson(person);
					}
					if (role.equals("RestaurantHost")) {
						person.addJob("RestaurantHost", 900, 2000);
						person.addRole(RestaurantHostRole, "RestaurantHost");
						RestaurantHostRole.setPerson(person);
					}
					if (role.equals("RestaurantCustomer")) {
						RestaurantCustomerRole RestaurantCustomerRole = new RestaurantCustomerRole(restaurantGui);
						person.addJob("RestaurantCustomer", 1000, 2000);
						person.addRole(RestaurantCustomerRole,"RestaurantCustomer");
						RestaurantCustomerRole.setPerson(person);
					}
					if (role.equals("RestaurantCashier")) {
						CashierRole RestaurantCashierRole = new CashierRole();
						person.addJob("RestaurantCashier", 1000, 2000);
						person.addRole(RestaurantCashierRole,"RestaurantCashier");
						RestaurantCashierRole.setPerson(person);

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
		for (PeopleAgent p : people) {
			p.Restaurants.add(restaurant);
			p.Markets.add(market);
			p.Banks.add(bank);
		}
		setVisible(true);
		setSize(1024, 1000);

		getContentPane().setLayout(new BorderLayout());

		
		cardLayout = new CardLayout();

		buildingPanels = new JPanel();
		buildingPanels.setLayout(cardLayout);
		buildingPanels.setMinimumSize(new Dimension(500, 300));
		buildingPanels.setMaximumSize(new Dimension(500, 300));
		buildingPanels.setPreferredSize(new Dimension(500, 300));
		buildingPanels.setBackground(Color.yellow);

		cityControls = new CityControls();

		// Create the BuildingPanel for each Building object
		ArrayList<Building> buildings = cityPanel.getBuildings();
		for (int i = 0; i < buildings.size(); i++) {
            Building b = buildings.get(i);
            BuildingPanel bp = new BuildingPanel(b, i, this);
            b.setBuildingPanel(bp);
		}
		
		JPanel jPanel = new JPanel();
		jPanel.setPreferredSize(new Dimension(500,250));
		jPanel.setDoubleBuffered(true);
		jPanel.add(bankGui);
		
		JScrollPane restaurantContainer = new JScrollPane(restaurantGui);
		JScrollPane bankContainer = new JScrollPane(bankGui);

        buildingPanels.add(restaurantContainer, "" + 0);
        buildingPanels.add(bankContainer, "" + 1);

		//getContentPane().add(BorderLayout.WEST, cityControls);
		getContentPane().add(BorderLayout.NORTH, cityPanel);
		getContentPane().add(BorderLayout.SOUTH, buildingPanels);
		
		restPanel.setBounds(768, 0, 666, 215);
		restPanel.setMinimumSize(new Dimension(500, 250));
		restPanel.setMaximumSize(new Dimension(500, 250));
		restPanel.setPreferredSize(new Dimension(500, 250));
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
		time++;
		if(time % 5 == 0)
		{
			for (PeopleAgent p : people) {
				p.msgTimeIs(time/5);
			}
		}
		if(time == 12000) {
			time=0;
		}
		repaint();

	}
}
