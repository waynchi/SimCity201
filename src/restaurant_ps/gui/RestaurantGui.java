package restaurant_ps.gui;
import restaurant_ps.CustomerAgent;
import restaurant_ps.HostAgent;
import restaurant_ps.WaiterAgent;
import restaurant_ps.interfaces.Customer;
import restaurant_ps.interfaces.Waiter;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
@SuppressWarnings("serial")
public class RestaurantGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	static JFrame animationFrame = new JFrame("Restaurant Animation");
	//static AnimationPanel animationPanel = new AnimationPanel();
	private static AnimationPanel animationPanel = new AnimationPanel();
//  
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
  //  private RestaurantPanel restPanel = new RestaurantPanel(this);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel
    private JButton requestBreak;
    private JPanel customPanel = new JPanel();
    private JLabel text = new JLabel();
    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
        int WINDOWX = 450;
        int WINDOWY = 350;

        animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        animationFrame.setBounds(100+WINDOWX, 50 , WINDOWX+100, WINDOWY+100);
        //animationFrame.setVisible(true);
    	//animationFrame.add(animationPanel); 
    	setBounds(150, 150, WINDOWX, WINDOWY);

        setLayout(new BoxLayout((Container) getContentPane(), 
        		BoxLayout.Y_AXIS));

//        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .6));
//        restPanel.setPreferredSize(restDim);
//        restPanel.setMinimumSize(restDim);
//        restPanel.setMaximumSize(restDim);
        //add(restPanel);
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * .25));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);
        
        requestBreak = new JButton("Request for break");
        requestBreak.setVisible(false);
        requestBreak.addActionListener(this);

        infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        infoPanel.add(requestBreak);
       // add(infoPanel);
        
        
       // text.setText("<html>Name: Peppy Sisay<br/>Restaurant Money: " + this.restPanel.getRestaurantMoney() + "</html>");
        
        
        
        //ImageIcon icon = new ImageIcon("/Users/peppysisay/Desktop/RandomSprites/gameplayicon.png");
        
        //JLabel jl = new JLabel(icon);
        //customPanel.add(jl);
        customPanel.add(text);
        
        add(customPanel);
        
        //add(animationPanel);
        //add(animationFrame.getContentPane());
        HostAgent host = new HostAgent("test");
        HostGui hgui = new HostGui(host);
        host.msgIsActive();
        
        
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateMoneyOutput(double money)
    {
    	text.setText("<html>Name: Peppy Sisay<br/>Restaurant Money: $" + money + "</html>");
    }
    public void updateInfoPanel(Object person) {
        currentPerson = person;
        

        if (person instanceof CustomerAgent) {
        	stateCB.setVisible(true);
        	requestBreak.setVisible(false);
            Customer customer = (Customer) person;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre> Name: " + customer.getName() + "\n Money: $" + customer.getMoney() + "</pre></html>");
            
        }
        else if(person instanceof WaiterAgent){
        	requestBreak.setVisible(true);
        	stateCB.setVisible(false);
        	Waiter waiter = (Waiter) person;
        	if(waiter.isWantingToGoOnBreak())
        	{
        		requestBreak.setEnabled(false);
        		requestBreak.setText("Will go on break");
        	}
        	if(waiter.isOnBreak())
        	{
        		requestBreak.setEnabled(true);
        		requestBreak.setText("Go off break");
        	}
        	if(!waiter.isOnBreak() && !waiter.isWantingToGoOnBreak())
        	{
        		requestBreak.setEnabled(true);
        		requestBreak.setText("Request break");
        	}
        	//stateCB.setText("Wants to go on break?");
        	//stateCB.setSelected(waiter.isOnBreak());
        	//stateCB.setEnabled(!waiter.isOnBreak());
        	
        	infoLabel.setText(
                    "<html><pre>     Name: " +  waiter.getName() + " </pre></html>");
        }
        infoPanel.validate();
    }
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stateCB) {
            if (currentPerson instanceof CustomerAgent) {
                Customer c = (Customer) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
            
        }
        if(e.getSource() == requestBreak) {
        	if(currentPerson instanceof WaiterAgent) {
            	Waiter w = (Waiter) currentPerson;
            	if(!w.isOnBreak())
            		w.WantsToGoOnBreak();
            	else{
            		requestBreak.setEnabled(true);
            		requestBreak.setText("Request Break");
            		w.goOffBreak();
            		
            	}
        }
    }
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(Customer c) {
        if (currentPerson instanceof CustomerAgent) {
            Customer cust = (Customer) currentPerson;
            if (c.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        RestaurantGui gui = new RestaurantGui();
          gui.setTitle("csci201 Restaurant");
//        gui.setVisible(true);
//        gui.setResizable(false);
//        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFrame mainGui = new JFrame();
        mainGui.setLayout(new BorderLayout());
        mainGui.add(gui.getContentPane(),BorderLayout.WEST);
        mainGui.add(animationPanel,BorderLayout.CENTER);
        mainGui.setVisible(true);
        mainGui.setResizable(false);
        mainGui.setBounds(50,50,1050,670);
        mainGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       // mainGui.add(gui.restPanel,BorderLayout.SOUTH);
        
        
    }
	public AnimationPanel getAnimationPanel() {
		return animationPanel;
	}
	public static void setAnimationPanel(AnimationPanel animationPanel) {
		RestaurantGui.animationPanel = animationPanel;
	}
}
