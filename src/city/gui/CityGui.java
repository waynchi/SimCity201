package city.gui;
import javax.swing.*;
import javax.swing.Timer;

import city.Restaurant;
import people.PeopleAgent;
import restaurant.*;
import transportation.*;
import housing.*;
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
	CityPanel cityPanel;
	JPanel buildingPanels;
	CardLayout cardLayout;
	CityControls cityControls;
	List<String> configParams = Collections
			.synchronizedList(new ArrayList<String>());
	RestaurantGui restaurantGui = new RestaurantGui();
	ArrayList<PeopleAgent> people = new ArrayList<PeopleAgent>();
	HostRole RestaurantHostRole = new HostRole("Host");
	MarketRole market = new MarketRole("Market 1");
	Restaurant restaurant = new Restaurant(RestaurantHostRole, new Dimension(
			100, 100), "Restaurant 1");
	int time;

	public CityGui() {
		Timer timer = new Timer(10, this);
		RestaurantPanel restPanel = new RestaurantPanel(restaurantGui,
				RestaurantHostRole);
		restPanel.setHost(RestaurantHostRole);
		CookWaiterMonitor RestaurantCookWaiterMonitor = restPanel.theMonitor;
		MarketRole RestaurantMarketRole = new MarketRole("Market");

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
				// for(String item : configParams) {
				String amount = configIteration.next();
				String role = configIteration.next();
				String name = configIteration.next();
				if (isInteger(amount)) {
					PeopleAgent person = new PeopleAgent(role, 1000.0, false);
					person.startThread();
					if (role.equals("RestaurantNormalWaiter")) {
						NormalWaiterRole RestaurantNormalWaiterRole = new NormalWaiterRole(
								name);
						WaiterGui g = new WaiterGui(RestaurantNormalWaiterRole);
						RestaurantNormalWaiterRole.setGui(g);
						person.addJob("RestaurantNormalWaiter", 800, 2400);
						person.addRole(RestaurantNormalWaiterRole,
								"RestaurantNormalWaiter");
						RestaurantNormalWaiterRole.setPerson(person);
					}
					if (role.equals("RestaurantCook")) {
						CookRole RestaurantCookRole = new CookRole(name,
								RestaurantCookWaiterMonitor);
						person.addJob("RestaurantCook", 800, 2400);
						person.addRole(RestaurantCookRole, "RestaurantCook");
						RestaurantCookRole.setPerson(person);
						RestaurantCookRole.addMarket(RestaurantMarketRole);
					}
					if (role.equals("RestaurantHost")) {
						person.addJob("RestaurantHost", 700, 2400);
						person.addRole(RestaurantHostRole, "RestaurantHost");
						RestaurantHostRole.setPerson(person);
					}
					if (role.equals("RestaurantCustomer")) {
						RestaurantCustomerRole RestaurantCustomerRole = new RestaurantCustomerRole(
								name);
						person.addJob("RestaurantCustomer", 800, 2400);
						person.addRole(RestaurantCustomerRole,
								"RestaurantCustomer");
						RestaurantCustomerRole.setPerson(person);
					}
					if (role.equals("RestaurantCashier")) {
						CashierRole RestaurantCashierRole = new CashierRole(
								name);
						person.addJob("RestaurantCashier", 800, 2400);
						person.addRole(RestaurantCashierRole,
								"RestaurantCashier");
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
		}
		setVisible(true);
		setSize(1024, 1000);

		getContentPane().setLayout(new BorderLayout());

		cityPanel = new CityPanel();
		cityPanel.setPreferredSize(new Dimension(400, 250));
		cityPanel.setMaximumSize(new Dimension(400, 250));
		cityPanel.setMinimumSize(new Dimension(400, 250));
		
		cardLayout = new CardLayout();

		buildingPanels = new JPanel();
		buildingPanels.setLayout(cardLayout);
		buildingPanels.setMinimumSize(new Dimension(500, 250));
		buildingPanels.setMaximumSize(new Dimension(500, 250));
		buildingPanels.setPreferredSize(new Dimension(500, 450));
		buildingPanels.setBackground(Color.yellow);

		cityControls = new CityControls();

		// Create the BuildingPanel for each Building object
		ArrayList<Building> buildings = cityPanel.getBuildings();
		for (int i = 0; i < buildings.size(); i++) {
			Building b = buildings.get(i);
			BuildingPanel bp = new BuildingPanel(b, i, this);
			b.setBuildingPanel(bp);
			buildingPanels.add(bp, "" + i);
		}
		getContentPane().add(BorderLayout.WEST, cityControls);
		getContentPane().add(BorderLayout.NORTH, cityPanel);
		getContentPane().add(BorderLayout.SOUTH, buildingPanels);
		
		buildingPanels.add(restPanel, "name_1385002139901999000");
		restPanel.setBounds(768, 0, 666, 215);
		restPanel.setMinimumSize(new Dimension(500, 250));
		restPanel.setMaximumSize(new Dimension(500, 250));
		restPanel.setPreferredSize(new Dimension(500, 250));
		timer.start();

	}

	public void displayBuildingPanel(BuildingPanel bp) {
		System.out.println(bp.getName());
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
		for (PeopleAgent p : people) {
			p.msgTimeIs(time);
		}
		repaint();

	}
}
