package restaurant_wc.gui;

import restaurant_wc.CustomerAgent;
import restaurant_wc.MarketAgent;
import restaurant_wc.WaiterAgent;
import restaurant_wc.interfaces.Waiter;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	JFrame animationFrame = new JFrame("Restaurant Animation");
	public AnimationPanel animationPanel = new AnimationPanel();
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private RestaurantPanel restPanel = new RestaurantPanel(this);
    private boolean paused = false;
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel
    private JCheckBox stateCC;
    private JButton button;
    
    
    private JButton depleteCookButton;
    
    private JPanel overallLeft = new JPanel();
    private JPanel overallRight = new JPanel();
    
    private JButton pauseButton;
    private JButton restartButton;
    
    private JButton addWaiter;
    private JPanel LowerPanel;
   
    private JTextField textField;
    private JTextField textFieldw;
    
    private JButton addMarketButton;
    private JPanel MarketPanel;
    private JTextField textFieldm;
    
    private JButton depleteCashierButton;
    
    
    private JPanel LowerPanel2;
    private JPanel LowerPanel3;
    private JTextField xPos;
    private JTextField yPos;
    private JTextField Size;
    private JButton button2;

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    //private JPanel customerName;
    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
        int WINDOWX = 600;
        int WINDOWY = 600;

       /* animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        animationFrame.setBounds(100+WINDOWX, 50 , 550, 400);
        animationFrame.setVisible(true);
    	animationFrame.add(animationPanel); */
    	
    	setBounds(50, 50, (int) (WINDOWX * 2), (int) (WINDOWY));
    	

        setLayout(new BoxLayout((Container) getContentPane(), 
        		BoxLayout.X_AXIS));
    	//setLayout(new FlowLayout());
    	
    	
    	Dimension LeftDim = new Dimension((int) (WINDOWX), (int) (WINDOWY));
    	overallLeft.setPreferredSize(LeftDim);
    	overallLeft.setMinimumSize(LeftDim);
    	overallLeft.setMaximumSize(LeftDim);
    	overallLeft.setLayout(new BoxLayout((Container) overallLeft, 
        		BoxLayout.Y_AXIS));
    	add(overallLeft);
    	
    	Dimension RightDim = new Dimension((int) (WINDOWX), (int) (WINDOWY));
    	overallRight.setPreferredSize(RightDim);
    	overallRight.setMinimumSize(RightDim);
    	overallRight.setMaximumSize(RightDim);
    //	overallRight.setBounds(100+WINDOWX, 50, 550, 400);
    	//add(overallRight);
    	
    	
        //setLayout(new BoxLayout((Container) getContentPane(), 
        		//BoxLayout.Y_AXIS));

        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .45));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        overallLeft.add(restPanel);
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * .10));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));
       
        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);
        
        stateCC = new JCheckBox();
        stateCC.setVisible(true);
        stateCC.setText("Hungry?");
        stateCC.addActionListener(this);

        textField = new JTextField();
        textFieldw = new JTextField();
        textFieldm = new JTextField();
        
        xPos = new JTextField();
        yPos = new JTextField();
        Size = new JTextField();

        button = new JButton("ADD");
        button.addActionListener(this);
        
        pauseButton = new JButton ("Pause");
        pauseButton.addActionListener(this);
        restartButton = new JButton ("Restart");
        restartButton.addActionListener(this);
        
        button2 = new JButton ("ADD TABLE");
        button2.addActionListener(this);
        
        addWaiter = new JButton ("ADD WAITER");
        addWaiter.addActionListener(this);
        

        infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        overallLeft.add(infoPanel);
        
        LowerPanel = new JPanel();
        LowerPanel.setLayout(new GridLayout(1,2,10,0));
        Dimension infoDim2 = new Dimension(WINDOWX, (int) (WINDOWY * .1));
        LowerPanel.setPreferredSize(infoDim2);
        LowerPanel.setMinimumSize(infoDim2);
        LowerPanel.setMaximumSize(infoDim2);
        LowerPanel.setBorder(BorderFactory.createTitledBorder("LowerPanel"));
        LowerPanel.add(textField);
        LowerPanel.add(stateCC);
        LowerPanel.add(button);
        overallLeft.add(LowerPanel);
        
        LowerPanel3 = new JPanel();
        LowerPanel3.setLayout(new GridLayout(1,2,10,0));
        Dimension infoDim4 = new Dimension(WINDOWX, (int) (WINDOWY* .1));
        LowerPanel3.setPreferredSize(infoDim4);
        LowerPanel3.setMinimumSize(infoDim4);
        LowerPanel3.setMaximumSize(infoDim4);
        LowerPanel3.setBorder(BorderFactory.createTitledBorder("Add Waiters"));
        LowerPanel3.add(textFieldw);
        LowerPanel3.add(addWaiter);
        
        overallLeft.add(LowerPanel3);
        
        LowerPanel2 = new JPanel();
        LowerPanel2.setLayout(new GridLayout(1,2,10,0));
        Dimension infoDim3 = new Dimension(WINDOWX, (int) (WINDOWY * .1));
        LowerPanel2.setPreferredSize(infoDim3);
        LowerPanel2.setMinimumSize(infoDim3);
        LowerPanel2.setMaximumSize(infoDim3);
        LowerPanel2.setBorder(BorderFactory.createTitledBorder("LowerPanel2"));
       /* LowerPanel2.add(xPos);
        LowerPanel2.add(yPos);
        LowerPanel2.add(Size);
        LowerPanel2.add(button2);*/
        LowerPanel2.add(pauseButton);
        LowerPanel2.add(restartButton);
        overallLeft.add(LowerPanel2);
        
        addMarketButton = new JButton("Add Market");
        addMarketButton.addActionListener(this);
        depleteCookButton = new JButton("Deplete Cook");
        depleteCookButton.addActionListener(this);
        depleteCashierButton = new JButton("Deplete Cashier");
        depleteCashierButton.addActionListener(this);
        
        MarketPanel = new JPanel();
        MarketPanel.setLayout(new GridLayout(1,2,10,0));
        Dimension infoDim5 = new Dimension(WINDOWX, (int) (WINDOWY *.1));
        MarketPanel.setPreferredSize(infoDim5);
        MarketPanel.setMinimumSize(infoDim5);
        MarketPanel.setMaximumSize(infoDim5);
        MarketPanel.setBorder(BorderFactory.createTitledBorder("Add Markets"));
        MarketPanel.add(textFieldm);
        MarketPanel.add(addMarketButton);
        MarketPanel.add(depleteCookButton);
        MarketPanel.add(depleteCashierButton);
        overallLeft.add(MarketPanel);
        
        
  
        
      	add(animationPanel);
    	//System.out.println(animationPanel.getSize().height + "," + animationPanel.getSize().width);
        
        
        
     
        
        /*customerName = new JPanel();
        customerName.setPreferredSize(infoDim);
        customerName.setMinimumSize(infoDim);
        customerName.setMaximumSize(infoDim);
        customerName.setBorder(BorderFactory.createTitledBorder("Customer Name Input"));*/

    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) {
        stateCB.setVisible(true);
        currentPerson = person;

        if (person instanceof CustomerAgent) {
            CustomerAgent customer = (CustomerAgent) person;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());   
          //  stateCC.setSelected(customer.getGui().isHungry()); 
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
           // stateCC.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }
        if (person instanceof WaiterAgent) {
        	WaiterAgent waiter = (WaiterAgent) person;
        	stateCB.setText("Go On Break?");
        	stateCB.setSelected(waiter.OnBreak);
        	stateCB.setEnabled(!waiter.negotiating);
        	infoLabel.setText("<html><pre>     Name: " + waiter.getName() + " </pre></html>");
        }
        
        infoPanel.validate();
    }
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource() == depleteCookButton) {
    		if(!paused){
    			restPanel.cook.depleteInventory();
    		}
    	}
    	if(e.getSource() == depleteCashierButton) {
    		if(!paused){
    			restPanel.cashier.depleteCashRegister();
    		}
    	}
    	if (e.getSource() == addMarketButton){
    		if(!paused){
    			MarketAgent m = new MarketAgent(textFieldm.getText(), restPanel.cook.Markets.size());
    			restPanel.cook.addMarket(m);
    			m.setCook(restPanel.cook);
    			m.setCashier(restPanel.cashier);
    			m.startThread();
    			System.out.println("Added a Market");
    		}
    	}
    	if (e.getSource() == addWaiter) {
    		if(!paused){
    		//restPanel.addPerson("Waiter", textFieldw.getText());
    		restPanel.waiterPanel.addPerson(textFieldw.getText());
    		System.out.println("Added a Waiter");
    		}
    	}  
    	if (e.getSource() == button) {
    		if(!paused){
        	restPanel.customerPanel.addPerson(textField.getText());
        	if(stateCC.isSelected())
        	{
        	    CustomerAgent c = (CustomerAgent) currentPerson;
                c.getGui().setHungry();
               // stateCC.setEnabled(false); 
                stateCC.setSelected(false);
        	}
    		}
    	}
    	if (e.getSource() == pauseButton) {
    		paused = true;
    		restPanel.pauseAll();
    	}
    	if (e.getSource() == restartButton) {
    		paused = false;
    		restPanel.restartAll();
    	}
        //if (e.getSource() == button2) {
        //	System.out.println("Test");
        	//animationPanel.editValues(Integer.parseInt(xPos.getText()), Integer.parseInt(yPos.getText()), Integer.parseInt(Size.getText()));
        	//animationPanel.drawForMe(Integer.parseInt(xPos.getText()), Integer.parseInt(yPos.getText()), Integer.parseInt(Size.getText()));
       // }
        if (e.getSource() == stateCB) {
        	if(!paused){
            if (currentPerson instanceof CustomerAgent) {
                CustomerAgent c = (CustomerAgent) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
            if (currentPerson instanceof WaiterAgent) {
            	WaiterAgent w = (WaiterAgent) currentPerson;
            	if(!w.OnBreak)
            	{
            		stateCB.setEnabled(false);
            		w.msgDoIWantToGoOnBreak();
            	}
            	else
            	{
            		//stateCB.setEnabled(false);
            		w.msgIWantToGoWork();
            	}
            }
        	}
        }
       /* if(e.getSource() == stateCC) {
        	if (currentPerson instanceof CustomerAgent) {
        		CustomerAgent c = (CustomerAgent) currentPerson;
        		c.getGui().setHungry();
        		stateCC.setEnabled(false);
        	}
        }*/
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(CustomerAgent c) {
        if (currentPerson instanceof CustomerAgent) {
            CustomerAgent cust = (CustomerAgent) currentPerson;
            if (c.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
               // stateCC.setEnabled(true);
                //stateCC.setSelected(false);
            }
        }
    }
    
    public void setWaiterEnabled(WaiterAgent w) {
        if (currentPerson instanceof WaiterAgent) {
            Waiter waiter = (Waiter) currentPerson;
            if (w.equals(waiter)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(w.OnBreak);
               // stateCC.setEnabled(true);
                //stateCC.setSelected(false);
            }
        }
    }
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        RestaurantGui gui = new RestaurantGui();
        gui.setTitle("csci201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
