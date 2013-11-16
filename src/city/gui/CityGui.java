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

/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class CityGui extends JFrame implements ActionListener {
	
	//Global Definitions
	static final int WINDOWX = 550;
	static final int WINDOWY = 650;
	static final int GRIDROWS = 1;
	static final int GRIDCOLS = 2;
	static final int GRIDHGAP = 30;
	static final int GRIDVGAP = 0;
	static final int BOUNDSX = 50;
	static final int BOUNDSY = 50;





	
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	JPanel animationFrame = new JPanel();
	AnimationPanel animationPanel = new AnimationPanel();
    private JPanel infoPanel2;
    private JLabel infoLabel2; //part of infoPanel
    private JButton stateCB2;//part of infoLabel

    private Object currentPerson;
    private JTextField textField;
    private JLabel lblMainCity;
   /* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public CityGui() {
    	CarPassengerRole carPassengerRole = new CarPassengerRole(); //Create Role
    	
    	Car car = new Car();
    	CarGui carGui = new CarGui();
    	car.startThread(); //Start Car Thread
    	car.setGui(carGui);
    	
    	PeopleAgent person = new PeopleAgent();
    	person.startThread();
    	person.hasCar = true;
    	carPassengerRole.setCar(car);
    	carPassengerRole.setPerson(person);
    	person.addJob("Car Passenger",0,0);
    	person.addRole(carPassengerRole,"carPassenger");
    	person.msgTimeIs(0);
    	
    	
        animationFrame.setBounds(100+WINDOWX, 50 , WINDOWX+100, WINDOWY+100);
        animationFrame.setVisible(true);
        getContentPane().setLayout(null);
        
        lblMainCity = new JLabel("Main City");
        lblMainCity.setBounds(0, 0, 59, 16);
        getContentPane().add(lblMainCity);
        animationPanel.setBounds(6, 0, 750, 628);
        animationPanel.setAlignmentX(1.0f);

        getContentPane().add(animationPanel);
        FlowLayout fl_animationPanel = new FlowLayout(FlowLayout.CENTER, 5, 5);
        animationPanel.setLayout(fl_animationPanel);
    	
    	setBounds(BOUNDSX, BOUNDSY, 1200, 900);
       
        // Now, setup the info panel
        Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * .15));
        
        infoPanel2 = new JPanel();
        infoPanel2.setBounds(803, 64, 321, 97);
        infoPanel2.setPreferredSize(infoDim);
        infoPanel2.setMinimumSize(infoDim);
        infoPanel2.setMaximumSize(infoDim);
        infoPanel2.setBorder(BorderFactory.createTitledBorder("People"));
        
        infoLabel2 = new JLabel(); 
	    infoLabel2.setText("<html><pre><i>None</i></pre></html>");
	    
	    
	    infoPanel2.setAlignmentX(Component.LEFT_ALIGNMENT);
	    infoLabel2.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        //Setup the my information panel
        Dimension myDim = new Dimension(WINDOWX, (int) (WINDOWY * .25));
        infoPanel2.setLayout(new GridLayout(GRIDROWS, GRIDCOLS, GRIDHGAP, GRIDVGAP));
        getContentPane().add(infoPanel2);
        
        
     
        infoPanel2.add(infoLabel2);
        
        textField = new JTextField();
        textField.setBounds(803, 24, 134, 28);
        getContentPane().add(textField);
        textField.setColumns(10);
        
        JButton btnAddPerson = new JButton("Add Person");
        btnAddPerson.setBounds(938, 23, 117, 29);
        getContentPane().add(btnAddPerson);
        
        JLabel lblControlPanel = new JLabel("Control Panel");
        lblControlPanel.setBounds(924, 6, 107, 16);
        getContentPane().add(lblControlPanel);
	



        
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person, int status) {        
        
    }
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {

    }
  
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        CityGui gui = new CityGui();
        gui.setTitle("csci201 Sim City");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
