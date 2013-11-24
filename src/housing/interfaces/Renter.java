package housing.interfaces;

import java.util.List;

import people.People;

public interface Renter extends Resident{
	// Messages

	public void payPenalty(double penalty);
	public void rentReminder();
	
	//-----------------------------------------------------------//
	
	// Scheduler
	
	public boolean pickAndExecuteAnAction();
	
	//-----------------------------------------------------------//

	// Utilities

	public People getAgent();		
	public void startRentTimer();
	public void setMoney(double m);	
	public double getMoney();
	public int getTimesRentDue();
	public boolean isRentDue();	
	public List<Double> getPenalties();
	public void setOwner(Owner o);
}
