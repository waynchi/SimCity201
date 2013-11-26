package people;

import city.gui.trace.AlertLog;
import city.gui.trace.AlertTag;

public class Role {

	public Role() {
		// TODO Auto-generated constructor stub
	}

	protected PeopleAgent myPerson;
	public boolean isActive = false;
	public AlertTag tag;

	public void setPerson(PeopleAgent a) {
		myPerson = a;
	}
	
	public void setTag(AlertTag t) {
		tag = t;
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
		AlertLog.getInstance().logMessage(this.tag, myPerson.name, text);
		myPerson.CallPrint(text);
	}
	
	protected void Do(String text){
		myPerson.CallDo(text);
	}
		 

}

