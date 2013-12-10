
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
	private JTextField textField_1;
	TracePanel tracePanel;
	ControlPanel controlPanel;
	CityPanel cityPanel;
	CityGui cityGui;
	
	
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
	
	private JPanel makePanel(String text) {
		if(text.equals("Scenarios")) {
			JPanel panel = new JPanel();
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
			
			JButton btnScenario6 = new JButton("Trigger Bank Robbery");
			btnScenario6.addActionListener(this);
			panel.add(btnScenario6);
			
			JButton btnScenario7 = new JButton("Trigger Vehicle Crash");
			btnScenario7.addActionListener(this);
			panel.add(btnScenario7);
			
			JButton btnScenario8 = new JButton("Trigger Predestrian Getting Hit");
			btnScenario8.addActionListener(this);
			panel.add(btnScenario8);
			
			JButton btnScenario9 = new JButton("Trigger Weekend");
			btnScenario9.addActionListener(this);
			panel.add(btnScenario9);
			
			JButton btnClear = new JButton("Clear World");
			btnClear.addActionListener(this);
			panel.add(btnClear);
			
			JButton btnNewWorld = new JButton("Create World From Config File");
			btnNewWorld.addActionListener(this);
			panel.add(btnNewWorld);
			
			return panel;
		}
		if(text.equals("Controls")) {
			JPanel panel = new JPanel();
			textField_1 = new JTextField();
			panel.add(textField_1);
			textField_1.setColumns(10);
			
			JButton btnAddPerson = new JButton("Add Person");
			panel.add(btnAddPerson);
			
			JButton btnDemonstrateCollisionVehicle = new JButton("Add Vehicles to Demonstrate Collision");
			btnDemonstrateCollisionVehicle.addActionListener(this);
			panel.add(btnDemonstrateCollisionVehicle);
			
			JButton btnDemonstrateCollisionPerson = new JButton("Add Person to Demonstrate Collision");
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
			panel.setPreferredSize( new Dimension(500, 268) );
			return panel;

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
			return panel2;
		}
		else return null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Normal Scenario 1")) {
			
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
		else if(e.getActionCommand().equals("Trigger Bank Robbery")) {
			
		}
		else if(e.getActionCommand().equals("Trigger Vehicle Crash")) {
			if(cityGui.cityPanel.vehicles.size() < 3)
			{
				System.out.println("Must be more than 2 cars present to trigger a vehicle crash, currently only: " + (cityGui.cityPanel.vehicles.size()-1));
				return;
			}
			cityGui.triggerVehicleCrash();
		}
		else if(e.getActionCommand().equals("Trigger Pedestrian Getting Hit")) {
			
		}
		else if(e.getActionCommand().equals("Trigger Weekend")) {
			
		}
		
		else if(e.getActionCommand().equals("Add Vehicles to Demonstrate Collision")) {
			People person = new PeopleAgent("TEST PERSON", 1000.0, false);
			PersonGui personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip1,cityPanel.sidewalkStrip1.get(0),cityPanel.allSidewalks, cityPanel, person);
			person.setPersonGui(personGui);
			CarGui vehicle = new CarGui(5, 5, 10, 10, cityPanel.road2, cityPanel.road2.get(0), cityPanel.allRoads, cityPanel);
			vehicle.setPersonAgent(person);
			vehicle.setDestination(1,1);
			cityPanel.vehicles.add(vehicle);
		}
		else if(e.getActionCommand().equals("Add Person to Demonstrate Collision")) {
			PeopleAgent person = new PeopleAgent("TEST PERSON", 1000.0, false);
			PersonGui personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalkStrip1,cityPanel.sidewalkStrip1.get(0),cityPanel.allSidewalks, cityPanel, person);					
			personGui.setDestination("Bank");
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