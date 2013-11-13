package people;

public class Role {

	public Role() {
		// TODO Auto-generated constructor stub
	}
	PeopleAgent myPerson; 

	public void setPerson(PeopleAgent a) {myPerson=a;} 

	public PeopleAgent getPersonAgent() { 

	return myPerson;} //so other agents or role players can send you Person messages. 

	private void stateChanged(){ myPerson.stateChanged()}; 
		 
}
