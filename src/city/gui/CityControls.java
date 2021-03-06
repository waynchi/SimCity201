
package city.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import people.People;
import people.PeopleAgent;
import transportation.CarGui;
import city.gui.trace.AlertLevel;
import city.gui.trace.AlertLog;
import city.gui.trace.AlertTag;
import city.gui.trace.TracePanel;


public class CityControls extends JPanel implements ActionListener, ChangeListener {
	private JTextField textField;
	private JTextField name;
	TracePanel tracePanel;
	ControlPanel controlPanel;
	CityPanel cityPanel;
	CityGui cityGui;
	JComboBox rolesList;
	JTextField money;
	JCheckBox hasCar;
	JButton btnScenario8; //pedes crash
	JButton btnScenario7; //vehicle crash
	
	
	static final int FPS_MIN = 1;
	static final int FPS_MAX = 30;
	static final int FPS_INIT = 5; 
	
	public CityControls(CityPanel cityPanel, CityGui cityGui) {
		this.cityPanel = cityPanel;
		this.cityGui = cityGui;
		
		this.setVisible(true);

		// set grid layout for the frame

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		tabbedPane.addTab("Controls", makePanel("Controls"));
		tabbedPane.addTab("TraceLog", makePanel("TraceLog"));
		tabbedPane.addTab("Scenarios",makePanel("Scenarios"));

		tabbedPane.setPreferredSize(new Dimension(500, 268));
		
		this.add(tabbedPane);
		this.setSize(new Dimension(500, 268));
		
	}
	
	private JScrollPane makePanel(String text) {
		if(text.equals("Scenarios")) {
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout());
			JButton btnScenarioOne = new JButton("Normal Scenario 1");
			btnScenarioOne.addActionListener(this);
			panel.add(btnScenarioOne);
			
			JButton btnScenario1 = new JButton("Non-Norm Scenario G");
			btnScenario1.addActionListener(this);
			panel.add(btnScenario1);
			
			JButton btnScenario2 = new JButton("Normal Scenario 3");
			btnScenario2.addActionListener(this);
			panel.add(btnScenario2);
			
			JButton btnScenario3 = new JButton("Bus Stop Scenario");
			btnScenario3.addActionListener(this);
			panel.add(btnScenario3);
			
			JButton btnScenario4 = new JButton("Close Banks");
			btnScenario4.addActionListener(this);
			panel.add(btnScenario4);
			
			JButton btnScenario10 = new JButton("Close Markets");
			btnScenario10.addActionListener(this);
			panel.add(btnScenario10);
			
			JButton btnScenario11 = new JButton("Close Restaurants");
			btnScenario11.addActionListener(this);
			panel.add(btnScenario11);
			
			JButton btnScenario5 = new JButton("Non-Norm Scenario 2");
			btnScenario5.addActionListener(this);
			panel.add(btnScenario5);
			
			JButton btnScenario6 = new JButton("Bank Robbery");
			btnScenario6.addActionListener(this);
			panel.add(btnScenario6);
			
			btnScenario7 = new JButton("Vehicle Crash");
			btnScenario7.addActionListener(this);
			panel.add(btnScenario7);
			
			JButton btnScenario20 = new JButton("Set To Friday");
			btnScenario20.addActionListener(this);
			panel.add(btnScenario20);

			JButton btnScenario21 = new JButton("People Gui Labels");
			btnScenario21.addActionListener(this);
			panel.add(btnScenario21);
			
			btnScenario8 = new JButton("Pedestrian Collision");
			btnScenario8 = new JButton("Trigger Pedestrian Getting Hit");

			btnScenario8.addActionListener(this);
			panel.add(btnScenario8);
			
			JButton btnScenario9 = new JButton("Weekend");
			btnScenario9.addActionListener(this);
			panel.add(btnScenario9);
			
			JButton btnScenarioB= new JButton("Normative Scenario B");
			btnScenarioB.addActionListener(this);
			panel.add(btnScenarioB);
			
			JButton btnScenarioA= new JButton("Normative Scenario A");
			btnScenarioA.addActionListener(this);
			panel.add(btnScenarioA);
			
			JButton btnClear = new JButton("Clear World");
			btnClear.addActionListener(this);
			panel.add(btnClear);
			
			JButton btnNewWorld = new JButton("Create World From Config File");
			btnNewWorld.addActionListener(this);
			panel.add(btnNewWorld);
			panel.setPreferredSize( new Dimension(400, 268) );
			JScrollPane pane = new JScrollPane(panel);
			return pane;
		}
		if(text.equals("Controls")) {
			JPanel panel = new JPanel();
			
			JLabel nameLabel = new JLabel("Name:");
			panel.add(nameLabel);
			
			name = new JTextField();
			panel.add(name);
			name.setColumns(10);
			
			JLabel roleLabel = new JLabel("Role:");
			panel.add(roleLabel);
			String[] options = { "Unemployed","RestaurantSpecialWaiter", "RestaurantNormalWaiter", "RestaurantSpecialWaiterZt","RestaurantNormalWaiterZt","RestaurantSpecialWaiterEs","RestaurantNormalWaiterEs","RestaurantSpecialWaiterVk","RestaurantNormalWaiterVk","RestaurantSpecialWaiterWc","RestaurantNormalWaiterWc","RestaurantSpecialWaiterPs","RestaurantNormalWaiterPs" };
			rolesList = new JComboBox(options);
			rolesList.addActionListener(this);
			panel.add(rolesList);
			
			JLabel moneyLabel = new JLabel("Money:");
			panel.add(moneyLabel);
			money = new JTextField();
			panel.add(money);
			money.setColumns(10);
			
			JLabel carLabel = new JLabel("Has Car:");
			panel.add(carLabel);
			hasCar = new JCheckBox();
			panel.add(hasCar);
			
			JButton btnAddPerson = new JButton("Add Person");
			btnAddPerson.addActionListener(this);
			panel.add(btnAddPerson);
			
			
			
			JButton btnDemonstrateCollisionVehicle = new JButton("Add Test Vehicle");
			btnDemonstrateCollisionVehicle.addActionListener(this);
			panel.add(btnDemonstrateCollisionVehicle);
			
			JButton btnDemonstrateCollisionPerson = new JButton("Add Test Person");
			btnDemonstrateCollisionPerson.addActionListener(this);
			panel.add(btnDemonstrateCollisionPerson);
			

			JSlider framesPerSecond = new JSlider(JSlider.HORIZONTAL,
			                                      FPS_MIN, FPS_MAX, FPS_INIT);
			framesPerSecond.addChangeListener(this);

			//Turn on labels at major tick marks.
			framesPerSecond.setMajorTickSpacing(10);
			framesPerSecond.setMinorTickSpacing(1);
			framesPerSecond.setPaintTicks(true);
			framesPerSecond.setPaintLabels(true);
			
			panel.add(framesPerSecond);
			
			
			panel.setBackground( Color.ORANGE );
			panel.setPreferredSize( new Dimension(400, 268) );
			JScrollPane pane = new JScrollPane(panel);
			return pane;

		} 
		if (text.equals("TraceLog")) {
			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());
			tracePanel = new TracePanel();
			
			tracePanel.showAlertsWithLevel(AlertLevel.ERROR);		//THESE PRINT RED, WARNINGS PRINT YELLOW on a black background... :/
			tracePanel.showAlertsWithLevel(AlertLevel.INFO);		//THESE PRINT BLUE
			tracePanel.showAlertsWithLevel(AlertLevel.MESSAGE);		//THESE SHOULD BE THE MOST COMMON AND PRINT BLACK
			
			tracePanel.hideAlertsWithLevel(AlertLevel.DEBUG);
			
			tracePanel.showAlertsWithTag(AlertTag.BANK);
			tracePanel.showAlertsWithTag(AlertTag.HOME);
			tracePanel.showAlertsWithTag(AlertTag.RESTAURANT1);
			tracePanel.showAlertsWithTag(AlertTag.RESTAURANT2);
			tracePanel.showAlertsWithTag(AlertTag.RESTAURANT3);
			tracePanel.showAlertsWithTag(AlertTag.RESTAURANT4);
			tracePanel.showAlertsWithTag(AlertTag.RESTAURANT5);
			tracePanel.showAlertsWithTag(AlertTag.RESTAURANT6);
			tracePanel.showAlertsWithTag(AlertTag.MARKET);
			

			AlertLog.getInstance().addAlertListener(tracePanel);
			this.controlPanel = new ControlPanel(tracePanel);
			
			panel.add(tracePanel, BorderLayout.CENTER);
			panel.add(controlPanel, BorderLayout.EAST);
			panel.setPreferredSize(new Dimension(800, 400));
			JScrollPane pane = new JScrollPane(panel);
			pane.setPreferredSize(new Dimension(500, 268));
			JPanel panel2 = new JPanel();
			panel2.setLayout(new BorderLayout());
			panel2.add(pane, BorderLayout.CENTER);
			return pane;
		}
		else return null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Normal Scenario 1")) {
			
		}
		else if(e.getActionCommand().equals("Add Person")) {
			try {
				double moneyOut = Double.parseDouble(money.getText());
				System.out.println("Creating a person");
				cityGui.CreatePerson(name.getText(), rolesList.getSelectedItem().toString(), moneyOut, hasCar.isSelected());
			} catch(NumberFormatException nfe) {
				money.setText("Please enter a valid number");
			}


		}
		else if(e.getActionCommand().equals("Create World From Config File")) {
			System.out.println("Creating World From Config");
			cityGui.CreateWorld(this.cityGui.RestaurantCookWaiterMonitor, this.cityGui.RestaurantCookWaiterMonitorZT, this.cityGui.RestaurantCookWaiterMonitorWc, this.cityGui.RestaurantCookWaiterMonitorEs, this.cityGui.RestaurantCookWaiterMonitorPS, this.cityGui.revolvingStand);
		}
		else if(e.getActionCommand().equals("Clear World")) {
			System.out.println("Clearing World");
			cityGui.ClearWorld();
		}
		else if(e.getActionCommand().equals("Non-Norm Scenario G")) {
			System.out.println("Creating non normative scenario G. Market fails to deliver because restaurant is closed.");
			cityGui.createNonNormG();
		}
		else if(e.getActionCommand().equals("Normal Scenario 3")) {
			System.out.println("Starting normative scenario: all restaurants order from markets");
			cityGui.startNormalScenario3();

		}
		else if(e.getActionCommand().equals("Bus Stop Scenario")) {
			System.out.println("Starting bus stop scenario");
			cityGui.startBusStopScenario();
			
		}	
		else if(e.getActionCommand().equals("Normative Scenario B")) {
			System.out.println("Normative B initiated.");
			cityGui.createNormativeB();
		}
		else if(e.getActionCommand().equals("Normative Scenario A")) {
			System.out.println("Normative A initiated.");
			cityGui.createNormativeA();
		}
		
		else if(e.getActionCommand().equals("People Gui Labels")) {
			for (People peep : cityGui.people) {
				if (!peep.getPersonGui().labelIt) {
					peep.getPersonGui().labelIt = true;
				}
				else if (peep.getPersonGui().labelIt) {
					peep.getPersonGui().labelIt = false;
				}
			}
		}
		else if(e.getActionCommand().equals("Close Banks")) {
			System.out.println("Closing all banks");
			cityGui.closeBanks();
		}
		else if(e.getActionCommand().equals("Close Markets")) {
			System.out.println("Closing all markets");
			cityGui.closeMarkets();
		}
		else if(e.getActionCommand().equals("Close Restaurants")) {
			System.out.println("Closing all restaurants");
			cityGui.closeRestaurants();
		}
		else if(e.getActionCommand().equals("Non-Norm Scenario 2")) {
			
		}
		else if(e.getActionCommand().equals("Bank Robbery")) {
			System.out.println("Bank will be robbed soon");
			cityGui.robber.msgRobBank();
		}
		else if(e.getActionCommand().equals("Set To Friday")) {
			System.out.println("Day has been changed to friday");
			cityGui.dayOfWeek = 4; //4 is the integer equivalent of friday
		}
		else if(e.getActionCommand().equals("Vehicle Crash")) {
			if(cityGui.numberOfCarsDriving() < 2)
			{
				System.out.println("Must be more than 2 cars present to trigger a vehicle crash, currently only: " + (cityGui.numberOfCarsDriving()));
				return;
			}
			System.out.println(cityGui.cityPanel.vehicles.size());
			System.out.println("Triggering vehicle crash");
			this.btnScenario7.setEnabled(false);
			cityGui.triggerVehicleCrash();
		}
		else if(e.getActionCommand().equals("Trigger Pedestrian Getting Hit")) {
			if(cityGui.isPedestrianCrossingStreet())
			{
				btnScenario8.setEnabled(false);
				System.out.println("Starting pedestrian crash scenario");
				cityGui.stopPedestriansCrossingStreetAndTellVehiclesSimulationStarted();
			}
			else
			{
				System.out.println("Pedestrian must be crossing the street!");
			}
		}
		else if(e.getActionCommand().equals("Trigger Weekend")) {
			
		}
		
		else if(e.getActionCommand().equals("Add Test Vehicle")) {
			People person = new PeopleAgent("TEST PERSON", 1000.0, false);
			PersonGui personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip1,cityPanel.sidewalkStrip1.get(0),cityPanel.allSidewalks, cityPanel, person);
			person.setPersonGui(personGui);
			CarGui vehicle = new CarGui(5, 5, 10, 10, cityPanel.road2, cityPanel.road2.get(0), cityPanel.allRoads, cityPanel);
			vehicle.setPersonAgent(person);
			vehicle.setCarDestination("Restaurant 6");
			cityPanel.vehicles.add(vehicle);
		}
		else if(e.getActionCommand().equals("Add Test Person")) {
			PeopleAgent person = new PeopleAgent("TEST PERSON", 1000.0, false);
			PersonGui personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip1,cityPanel.sidewalkStrip1.get(0),cityPanel.allSidewalks, cityPanel, person);			
			person.setPersonGui(personGui);
			personGui.setDestination("Restaurant 6");
			cityPanel.people.add(personGui); 
			
			
		}
		
		// TODO Auto-generated method stub
		
	}
	
	public void stateChanged(ChangeEvent e) {
	    JSlider source = (JSlider)e.getSource();
	    if (!source.getValueIsAdjusting()) {
	    	int fps = (int)source.getValue();
	    	cityGui.timer.setDelay(fps);
	    }
	}
	
	private class ControlPanel extends JPanel {
		TracePanel tp;	//Hack so I can easily call showAlertsWithLevel for this demo.
		
		JToggleButton messagesButton;
		JToggleButton errorButton;
		JToggleButton bankButton;		
		JToggleButton restarauntButton;
		JToggleButton homeButton;
		JToggleButton marketButton;
		
		public ControlPanel(final TracePanel tracePanel) {
			this.tp = tracePanel;
			messagesButton = new JToggleButton("Hide Level: MESSAGE");
			errorButton = new JToggleButton("Hide Level: ERROR");
			bankButton = new JToggleButton("Hide Tag: BANK");
			restarauntButton = new JToggleButton("Hide Tag: RESTAURANT");
			homeButton = new JToggleButton("Hide Tag: HOME");
			marketButton = new JToggleButton("Hide Tag: MARKET");
			
			messagesButton.setSelected(true);
			errorButton.setSelected(true);
			bankButton.setSelected(true);
			restarauntButton.setSelected(true);
			homeButton.setSelected(true);
			marketButton.setSelected(true);
			
			
			messagesButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JToggleButton tBtn = (JToggleButton)e.getSource();
					if (tBtn.isSelected()) {
						tBtn.setText("Hide Level: MESSAGE");
						tracePanel.showAlertsWithLevel(AlertLevel.MESSAGE);
			        }
					else {
						tBtn.setText("Show Level: MESSAGE");
						tracePanel.hideAlertsWithLevel(AlertLevel.MESSAGE);
			        }
				}
			});
			errorButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JToggleButton tBtn = (JToggleButton)e.getSource();
					if (tBtn.isSelected()) {
						tBtn.setText("Hide Level: ERROR");
						tracePanel.showAlertsWithLevel(AlertLevel.ERROR);
			        }
					else {
						tBtn.setText("Show Level: ERROR");
						tracePanel.hideAlertsWithLevel(AlertLevel.ERROR);
			        }
				}
			});
			bankButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JToggleButton tBtn = (JToggleButton)e.getSource();
					if (tBtn.isSelected()) {
						tBtn.setText("Hide Tag: BANK");
						tracePanel.showAlertsWithTag(AlertTag.BANK);
			        }
					else {
						tBtn.setText("Show Tag: BANK");
						tracePanel.hideAlertsWithTag(AlertTag.BANK);
			        }
				}
			});
			marketButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JToggleButton tBtn = (JToggleButton)e.getSource();
					if (tBtn.isSelected()) {
						tBtn.setText("Hide Tag: MARKET");
						tracePanel.showAlertsWithTag(AlertTag.MARKET);
			        }
					else {
						tBtn.setText("Show Tag: MARKET");
						tracePanel.hideAlertsWithTag(AlertTag.MARKET);
			        }
				}
			});
			homeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JToggleButton tBtn = (JToggleButton)e.getSource();
					if (tBtn.isSelected()) {
						tBtn.setText("Hide Tag: HOME");
						tracePanel.showAlertsWithTag(AlertTag.HOME);
			        }
					else {
						tBtn.setText("Show Tag: HOME");
						tracePanel.hideAlertsWithTag(AlertTag.HOME);
			        }
				}
			});
			restarauntButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JToggleButton tBtn = (JToggleButton)e.getSource();
					if (tBtn.isSelected()) {
						tBtn.setText("Hide Tag: RESTAURANT");
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANT1);
			        }
					else {
						tBtn.setText("Show Tag: RESTAURANT");
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANT1);
			        }
				}
			});
			
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.add(messagesButton);
			this.add(errorButton);
			this.add(bankButton);
			this.add(homeButton);
			this.add(marketButton);
			this.add(restarauntButton);
			//this.setMinimumSize(new Dimension(50, 600));
		}
	}
	
}