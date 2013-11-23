package people;

public class Role {

	public Role() {
		// TODO Auto-generated constructor stub
	}

	protected People myPerson;
	protected boolean isActive = false;

	public void setPerson(People a) {
		myPerson = a;
	}

	public People getPersonAgent() {

		return myPerson;
	} // so other agents or role players can send you Person messages.

	protected void stateChanged() {
		if (myPerson != null) {
			myPerson.CallstateChanged();
		}
	}

	public void msgIsInActive(){ };
	
	public void msgIsActive(){ };

	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	};
	
	protected void print(String text) {
		myPerson.CallPrint(text);
	}
	
	protected void Do(String text){
		myPerson.CallDo(text);
	}
		 

}

