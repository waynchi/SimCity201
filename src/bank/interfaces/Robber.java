package bank.interfaces;

/**
 * Bank customer.
 */
public interface Robber {
	
	public abstract void msgReadyToHelp(Teller t);
	
	public abstract void msgPleaseDontHurtMe(double Money);
}