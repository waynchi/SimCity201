
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

		tabbedPane.addTab("Tab1", makePanel("People"));
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
		
		JButton enableMessagesButton;		//You could (and probably should) substitute a JToggleButton to replace both
		JButton disableMessagesButton;		//of these, but I split it into enable and disable for clarity in the demo.
		JToggleButton messagesButton;
		JButton enableErrorButton;		
		JButton disableErrorButton;		
		JButton enableBankCustTagButton;		//You could (and probably should) substitute a JToggleButton to replace both
		JButton disableBankCustTagButton;		//of these, but I split it into enable and disable for clarity in the demo.
		
		public ControlPanel(final TracePanel tracePanel) {
			this.tp = tracePanel;
			messagesButton = new JToggleButton("Toggle Level: MESSAGE");
			enableErrorButton = new JButton("Show Level: ERROR");
			disableErrorButton = new JButton("Hide Level: ERROR");
			enableBankCustTagButton = new JButton("Show Tag: BANK_CUSTOMER");
			disableBankCustTagButton = new JButton("Hide Tag: BANK_CUSTOMER");
			
			
			messagesButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JToggleButton tBtn = (JToggleButton)e.getSource();
					if (tBtn.isSelected()) {
						tracePanel.showAlertsWithLevel(AlertLevel.MESSAGE);
			        }
					else {
						tracePanel.hideAlertsWithLevel(AlertLevel.MESSAGE);
			        }
				}
			});
			enableErrorButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//============================ TUTORIAL ==========================================
					//This is how you make messages with a level of ERROR show up in the trace panel.
					tracePanel.showAlertsWithLevel(AlertLevel.ERROR);
					//================================================================================
				}
			});
			disableErrorButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//============================ TUTORIAL ==========================================
					//This is how you make messages with a level of ERROR not show up in the trace panel.
					tracePanel.hideAlertsWithLevel(AlertLevel.ERROR);
					//================================================================================
				}
			});
			enableBankCustTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//============================ TUTORIAL ==========================================
					//This works the same way as AlertLevels, only you're using tags instead.
					//In this demo, I generate message with tag BANK_CUSTOMER when you click in the 
					//AnimationPanel somewhere.
					tracePanel.showAlertsWithTag(AlertTag.BANK_CUSTOMER);
					//================================================================================
				}
			});
			disableBankCustTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//============================ TUTORIAL ==========================================
					//This works the same way as AlertLevels, only you're using tags instead.
					tracePanel.hideAlertsWithTag(AlertTag.BANK_CUSTOMER);
					//================================================================================
				}
			});
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.add(messagesButton);
			this.add(enableErrorButton);
			this.add(disableErrorButton);
			this.add(enableBankCustTagButton);
			this.add(disableBankCustTagButton);
			//this.setMinimumSize(new Dimension(50, 600));
		}
	}
	
}