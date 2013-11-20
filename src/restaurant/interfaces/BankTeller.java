package restaurant.interfaces;

public interface BankTeller {
	public abstract void msgCreateAccount(String name, double money);
	public abstract void msgDeposit(int id, double money);
	public abstract void msgWithdraw(int id, double money);
	public String getName();
}
