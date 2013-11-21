package city.gui;
import javax.swing.*;

import people.PeopleAgent;
import restaurant.*;
import transportation.*;
import housing.*;
import restaurant.gui.RestaurantPanel.CookWaiterMonitor;
import restaurant.gui.RestaurantPanel;
import restaurant.gui.WaiterGui;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class CityGui extends JFrame {
	CityPanel cityPanel;
	JPanel buildingPanels;
	CardLayout cardLayout;
	CityControls cityControls;
	ArrayList<String> configParams = new ArrayList<String>();
	RestaurantPanel restPanel = new RestaurantPanel();

	
	
	
	public CityGui() {
		FileReader input;
		try {
			input = new FileReader("config.txt");
			BufferedReader bufRead = new BufferedReader(input);
			String line = null;
			while((line = bufRead.readLine()) != null) {
				configParams.addAll(Arrays.asList(line.split("\\s*,\\s*")));
			}
			for(String item : configParams) {
				if(isInteger(item)) {
					//Restaurant Role Setup
					CookWaiterMonitor RestaurantCookWaiterMonitor = restPanel.theMonitor;
					NormalWaiterRole RestaurantNormalWaiterRole = new NormalWaiterRole("Normal Waiter");
					WaiterGui g = new WaiterGui(RestaurantNormalWaiterRole);
					HostRole RestaurantHostRole = new HostRole("Host");
					SpecialWaiterRole RestaurantSpecialWaiterRole = new SpecialWaiterRole("Special Waiter",RestaurantCookWaiterMonitor);
					CookRole RestaurantCookRole = new CookRole("Cook",RestaurantCookWaiterMonitor);
					MarketRole RestaurantMarketRole = new MarketRole("Market");
					CashierRole RestaurantCashierRole = new CashierRole("Cashier");
					RestaurantCustomerRole RestaurantCustomerRole = new RestaurantCustomerRole("Customer");
					//Transportation Role Setup
//					BusPassengerRole BusPassengerRole = new BusPassengerRole();
//					CarPassengerRole CarPassengerRole = new CarPassengerRole();
					//Housing Role Setup
					OwnerRole HousingOwnerRole = new OwnerRole();
					RenterRole HousingRenterRole = new RenterRole(0); //wtf is the double?
					RepairManRole HousingRepairManRole = new RepairManRole();
					ResidentRole HousingResidentRole = new ResidentRole();
					//Bank Role Setup
					
					int currIndex = configParams.indexOf(item);
					System.out.println(item + " " + configParams.get(currIndex + 1) + " " + configParams.get(currIndex + 2));
					PeopleAgent person = new PeopleAgent(configParams.get(currIndex + 2),1000.0,false);
					person.startThread();
					if(configParams.get(currIndex + 1).equals("RestaurantNormalWaiter")) {
						RestaurantNormalWaiterRole.setGui(g);
						person.addJob("RestaurantNormalWaiter", 0, 1200);
						person.addRole(RestaurantNormalWaiterRole,"RestaurantNormalWaiter");
						person.msgTimeIs(0);
						System.out.println("Created" + RestaurantNormalWaiterRole);

					}
					if(configParams.get(currIndex + 1).equals("RestaurantCook")) {
						person.addJob("RestaurantCook", 0, 1200);
						person.addRole(RestaurantCookRole,"RestaurantNormalWaiter");
						person.msgTimeIs(0);
						System.out.println("Created" + RestaurantCookRole);


					}
					
					
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
		setVisible( true );
		setSize( 1024, 1000 );
		
		getContentPane().setLayout( new BorderLayout() );
		
		cityPanel = new CityPanel();
		cityPanel.setPreferredSize( new Dimension(500, 250) );
		cityPanel.setMaximumSize( new Dimension(500, 250) );
		cityPanel.setMinimumSize( new Dimension(500, 250) );
		
		cardLayout = new CardLayout();

		buildingPanels = new JPanel();
		buildingPanels.setLayout( cardLayout );
		buildingPanels.setMinimumSize( new Dimension( 500, 250 ) );
		buildingPanels.setMaximumSize( new Dimension( 500, 250 ) );
		buildingPanels.setPreferredSize( new Dimension(500, 450) );
		buildingPanels.setBackground(Color.yellow);
		
		cityControls = new CityControls();
		
		//Create the BuildingPanel for each Building object
		ArrayList<Building> buildings = cityPanel.getBuildings();
		for ( int i=0; i<buildings.size(); i++ ) {
			Building b = buildings.get(i);
			BuildingPanel bp = new BuildingPanel( b, i, this );
			b.setBuildingPanel( bp );
			buildingPanels.add( bp, "" + i );
		}
		getContentPane().add( BorderLayout.EAST, cityControls);
		getContentPane().add( BorderLayout.NORTH, cityPanel );
		getContentPane().add( BorderLayout.SOUTH, buildingPanels );
		buildingPanels.add(restPanel, "name_1385002139901999000");
		restPanel.setBounds(768, 0, 666, 215);
		restPanel.setMinimumSize( new Dimension( 500, 250 ) );
		restPanel.setMaximumSize( new Dimension( 500, 250 ) );
		restPanel.setPreferredSize( new Dimension( 500, 250 ) );
	}
	
	public void displayBuildingPanel( BuildingPanel bp ) {
		System.out.println("abc");
		System.out.println( bp.getName() );
		cardLayout.show( buildingPanels, bp.getName() );
	}
	
	public static void main( String[] args ) {
		CityGui sc = new CityGui();
		
	}
	
	public static boolean isInteger(String s) {
		 try { 
		        Integer.parseInt(s); 
		    } catch(NumberFormatException e) { 
		        return false; 
		    }
		    // only got here if we didn't return false
		return true;
	}
}
