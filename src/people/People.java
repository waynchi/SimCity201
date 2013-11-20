package people;

public interface People {

	public abstract void addRole(Role r, String description);

	public abstract void CallPrint(String text);

	public abstract void CallDo(String text);

	public abstract void addJob(String job, int start, int end);

	//messages

	public abstract void msgDone(Role r);

	public abstract void msgTimeIs(int Time);

	public abstract void GoToRestaurant();

	public abstract void GoToHouse();

	public abstract void GoBuyCar();

	public abstract void LeaveWork();

	public abstract void GoToBank();

	public abstract void GoToWork();

	public abstract void CallstateChanged();

}