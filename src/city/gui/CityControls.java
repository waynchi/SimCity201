
package city.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import people.PeopleAgent;
import people.Role;
import transportation.Car;
import transportation.CarGui;
import transportation.CarPassengerRole;

public class CityControls extends JPanel implements ActionListener {
	private JTextField textField;
	private JTextField textField_1;
	public CityControls() {
		
		textField_1 = new JTextField();
		add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnAddPerson = new JButton("Add Person");
		add(btnAddPerson);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}