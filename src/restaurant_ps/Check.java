package restaurant_ps;

import restaurant_ps.interfaces.Customer;

public class Check {

	private Customer customer;
	public Food foodChoice;
	private double moneyOwed;
	
	public Check(Customer c, Food f)
	{
		customer = c;
		foodChoice = f;
		moneyOwed = f.price;
	}
	
	public Customer getCustomer() 
	{
		return customer;
	}
	
	public double getMoneyOwed(){
		return moneyOwed;
	}
	
	public void setMoneyOwed(double d)
	{
		moneyOwed = d;
	}
	

}
