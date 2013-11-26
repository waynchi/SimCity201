
package city.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import city.gui.trace.AlertLevel;
import city.gui.trace.AlertLog;
import city.gui.trace.AlertTag;
import city.gui.trace.TracePanel;


public class CityControls extends JPanel implements ActionListener {
	private JTextField textField;
	private JTextField textField_1;
	TracePanel tracePanel;
	ControlPanel controlPanel;
	
	public CityControls() {
		
		this.setVisible(true);

		// set grid layout for the frame

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		tabbedPane.addTab("Controls", makePanel("People"));
		tabbedPane.addTab("TraceLog", makePanel("Other"));

		tabbedPane.setPreferredSize(new Dimension(500, 268));
		
		this.add(tabbedPane);
		this.setSize(new Dimension(500, 268));
		
	}
	
	private JPanel makePanel(String text) {
		if(text.equals("People")) {
			JPanel panel = new JPanel();
			textField_1 = new JTextField();
			panel.add(textField_1);
			textField_1.setColumns(10);
			
			JButton btnAddPerson = new JButton("Add Person");
			panel.add(btnAddPerson);
			
			panel.setBackground( Color.ORANGE );
			panel.setPreferredSize( new Dimension(500, 268) );
			return panel;

		} 
		if (text.equals("Other")) {
			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());
			tracePanel = new TracePanel();
			
			tracePanel.showAlertsWithLevel(AlertLevel.ERROR);		//THESE PRINT RED, WARNINGS PRINT YELLOW on a black background... :/
			tracePanel.showAlertsWithLevel(AlertLevel.INFO);		//THESE PRINT BLUE
			tracePanel.showAlertsWithLevel(AlertLevel.MESSAGE);		//THESE SHOULD BE THE MOST COMMON AND PRINT BLACK
			
			tracePanel.hideAlertsWithLevel(AlertLevel.DEBUG);
			
			tracePanel.showAlertsWithTag(AlertTag.PERSON);
			tracePanel.showAlertsWithTag(AlertTag.BANK_CUSTOMER);
			
			tracePanel.hideAlertsWithTag(AlertTag.BUS_STOP);

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
		// TODO Auto-generated method stub
		
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
			messagesButton = new JToggleButton("Show Level: MESSAGE");
			errorButton = new JToggleButton("Show Level: ERROR");
			bankButton = new JToggleButton("Show Tag: BANK");
			restarauntButton = new JToggleButton("Show Tag: RESTAURANT");
			homeButton = new JToggleButton("Show Tag: HOME");
			marketButton = new JToggleButton("Show Tag: MARKET");
			
			
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
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANT);
			        }
					else {
						tBtn.setText("Show Tag: RESTAURANT");
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANT);
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