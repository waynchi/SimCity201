package city.gui;
import javax.swing.*;

import people.PeopleAgent;
import restaurant.NormalWaiterRole;
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
	NormalWaiterRole normalWaiterRole = new NormalWaiterRole("Waiter");
	WaiterGui g = new WaiterGui(normalWaiterRole);
	
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
					int currIndex = configParams.indexOf(item);
					PeopleAgent person = new PeopleAgent(1000,0);
					person.startThread();
					normalWaiterRole.setGui(g);
					person.addJob("Waiter", 0, 1200);
					person.addRole(normalWaiterRole,"Waiter");
					person.msgTimeIs(0);
					
					
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
