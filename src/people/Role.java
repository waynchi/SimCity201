package people;

public class Role {

	public Role() {
		// TODO Auto-generated constructor stub
	}

	protected PeopleAgent myPerson;

	public void setPerson(PeopleAgent a) {
		myPerson = a;
	}

	public PeopleAgent getPersonAgent() {

		return myPerson;
	} // so other agents or role players can send you Person messages.

	protected void stateChanged() {
		myPerson.CallstateChanged();
	}

	public void msgIsInActive(){ };
	
	public void msgIsActive() { }

	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	};
	
	protected void print() {
		myPerson.CallPrint();
	}
	
	protected void Do(){
		myPerson.CallDo();
	}
		 

}

