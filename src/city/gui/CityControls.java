
package city.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class CityControls extends JPanel implements ActionListener {
	private JTextField textField;
	private JTextField textField_1;
	public CityControls() {
		
		
		// Create and set up the window.
		final JFrame frame = new JFrame("Split Pane Example");

		// Display the window.
		this.setSize(500, 300);
		this.setVisible(true);

		// set grid layout for the frame

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		tabbedPane.addTab("Tab1", makePanel("People"));
		tabbedPane.addTab("Tab2", makePanel("Other"));

		this.add(tabbedPane);
		
		
		
		
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
			panel.setPreferredSize( new Dimension(200, 77) );
			return panel;

		} else {
			JPanel panel = new JPanel();
			return panel;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}