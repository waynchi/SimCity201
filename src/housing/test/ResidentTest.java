package housing.test;

import static org.junit.Assert.*;

import java.util.List;
import housing.HousingResidentRole;
import housing.interfaces.RepairMan;
import housing.interfaces.Resident;
import people.PeopleAgent.AgentState;
import people.PeopleAgent.AgentEvent;
import people.PeopleAgent.HungerState;
import housing.House;
import housing.HouseType;
import housing.HousingResidentRole.State;
import housing.HousingResidentRole.RepairStage;
import housing.Item;
import org.junit.Test;
import people.PeopleAgent;
import people.Role;

public class ResidentTest {
	Resident r;
	PeopleAgent p;
	RepairMan m;
	House h;
	List<Item> items;

	@Test
	public void testForInitialStates() {
		setUp();
		
		assertEquals(AgentState.Sleeping, p.state);
		assertEquals(State.Sleeping, ((HousingResidentRole)r).myState);
		assertEquals(RepairStage.None, ((HousingResidentRole)r).repairStage);
	}
	
	@Test
	public void test1() {
		setUp();
		
		p.event = AgentEvent.WakingUp;
		r.pickAndExecuteAnAction();
		
		assertEquals(State.Idle, ((HousingResidentRole)r).myState);
		assertEquals(0, p.log.size());
		
		r.pickAndExecuteAnAction();
		
		assertEquals(State.Idle, ((HousingResidentRole)r).myState);
		assertEquals(0, p.log.size());
		assertTrue(r.leisure());
	}
	
	@Test
	public void test2() {
		setUp();
		
		p.event = AgentEvent.WakingUp;
		r.pickAndExecuteAnAction();
		
		assertEquals(State.Idle, ((HousingResidentRole)r).myState);
		
		assertFalse(((HousingResidentRole)r).needToLeave());
		((HousingResidentRole)r).msgIsInActive();
		assertTrue(((HousingResidentRole)r).needToLeave());
		
		r.pickAndExecuteAnAction();
		
		assertFalse(((HousingResidentRole)r).needToLeave());
		assertFalse(((HousingResidentRole)r).isActive);
		assertEquals(State.Idle, ((HousingResidentRole)r).myState);
//		assertEquals(1, p.log.size());
//		assertEquals("ResidentRole", p.log.getLastLoggedEvent().getMessage());
	}
	
	@Test
	public void test3() {
		setUp();
		
		p.event = AgentEvent.WakingUp;
		r.pickAndExecuteAnAction();
		
		assertEquals(State.Idle, ((HousingResidentRole)r).myState);
		assertEquals(0, p.log.size());
		
		r.pickAndExecuteAnAction();
		
		assertEquals(State.Idle, ((HousingResidentRole)r).myState);
		assertEquals(0, p.log.size());
		assertTrue(r.leisure());
		
		r.pickAndExecuteAnAction();
		
		assertEquals(State.Idle, ((HousingResidentRole)r).myState);
		assertEquals(0, p.log.size());
		assertTrue(r.leisure());
		
		p.event = AgentEvent.GoingToSleep;
		r.pickAndExecuteAnAction();
		
		assertEquals(State.Sleeping, ((HousingResidentRole)r).myState);
		assertEquals(0, p.log.size());
		assertFalse(r.leisure());
	}
	
	@Test
	public void test4() {
		setUp();
		
		p.event = AgentEvent.WakingUp;
		r.pickAndExecuteAnAction();
		
		assertEquals(State.Idle, ((HousingResidentRole)r).myState);
		assertEquals(0, p.log.size());
		
		r.pickAndExecuteAnAction();
		
		assertEquals(State.Idle, ((HousingResidentRole)r).myState);
		assertEquals(0, p.log.size());
		assertTrue(r.leisure());
		
		p.state = AgentState.EatingAtHome;
		p.hunger = HungerState.Eating;
		r.pickAndExecuteAnAction();
		
		assertEquals(State.FoodCooked, ((HousingResidentRole)r).myState);
		
		r.pickAndExecuteAnAction();
		
		assertEquals(State.Idle, ((HousingResidentRole)r).myState);
//		assertEquals(1, p.log.size());
//		assertEquals("DoneEating", p.log.getLastLoggedEvent().getMessage());
	}
	
	@Test
	public void test5() {
		setUp();
		
		p.event = AgentEvent.WakingUp;
		r.pickAndExecuteAnAction();
		
		assertEquals(State.Idle, ((HousingResidentRole)r).myState);
		assertEquals(0, p.log.size());
		
		r.pickAndExecuteAnAction();
		
		assertEquals(State.Idle, ((HousingResidentRole)r).myState);
		assertEquals(0, p.log.size());
		assertTrue(r.leisure());
		
		p.state = AgentState.EatingAtHome;
		p.hunger = HungerState.Eating;
		r.pickAndExecuteAnAction();
		
		assertEquals(State.FoodCooked, ((HousingResidentRole)r).myState);
		
		assertFalse(((HousingResidentRole)r).needToLeave());
		((HousingResidentRole)r).msgIsInActive();
		assertTrue(((HousingResidentRole)r).needToLeave());
		
		r.pickAndExecuteAnAction();
		
		assertFalse(((HousingResidentRole)r).needToLeave());
		assertFalse(((HousingResidentRole)r).isActive);
		assertEquals(State.Idle, ((HousingResidentRole)r).myState);
	}
	
	@Test
	public void test6() {
		setUp();
		
		items.get(1).breakIt();
		assertEquals(RepairStage.NeedsRepair, ((HousingResidentRole)r).repairStage);
		
		p.event = AgentEvent.WakingUp;
		r.pickAndExecuteAnAction();
		
		assertEquals(State.Idle, ((HousingResidentRole)r).myState);
		assertEquals(0, p.log.size());
		
		r.pickAndExecuteAnAction();
		
		assertEquals(1, ((MockRepairMan)m).log.size());
		assertEquals("House called for help. Money given: $20.0.", ((MockRepairMan)m).log.getLastLoggedEvent().getMessage());
		assertEquals(RepairStage.HelpRequested, ((HousingResidentRole)r).repairStage);
		assertEquals(State.Idle, ((HousingResidentRole)r).myState);
		
		r.ImHere();
		
		assertEquals(RepairStage.RepairManIsHere, ((HousingResidentRole)r).repairStage);
		
		r.repairDone();
		assertEquals(RepairStage.None, ((HousingResidentRole)r).repairStage);
		

		items.get(1).breakIt();
		h.breakIt();
		
		assertTrue(h.isBroken());
		assertEquals(RepairStage.NeedsRepair, ((HousingResidentRole)r).repairStage);
		
		assertEquals(State.Idle, ((HousingResidentRole)r).myState);
		assertEquals(0, p.log.size());
		
		r.pickAndExecuteAnAction();
		
		assertEquals(2, ((MockRepairMan)m).log.size());
		assertEquals("House called for help. Money given: $20.0.", ((MockRepairMan)m).log.getLastLoggedEvent().getMessage());
		assertEquals(RepairStage.HelpRequested, ((HousingResidentRole)r).repairStage);
		assertEquals(State.Idle, ((HousingResidentRole)r).myState);
		
		r.ImHere();
		
		assertEquals(RepairStage.RepairManIsHere, ((HousingResidentRole)r).repairStage);
		
		r.repairDone();
		
		assertEquals(RepairStage.None, ((HousingResidentRole)r).repairStage);
	}
	
	@Test
	public void test7() {
		setUp();
		m.setPerson(p);
		
		items.get(1).breakIt();
		assertEquals(RepairStage.NeedsRepair, ((HousingResidentRole)r).repairStage);
		
		p.event = AgentEvent.WakingUp;
		r.pickAndExecuteAnAction();
		
		assertEquals(State.Idle, ((HousingResidentRole)r).myState);
		assertEquals(0, p.log.size());
		
		r.pickAndExecuteAnAction();
		
		assertEquals(0, ((MockRepairMan)m).log.size());
		assertEquals(RepairStage.None, ((HousingResidentRole)r).repairStage);
		assertEquals(State.Idle, ((HousingResidentRole)r).myState);
	}
	
	@Test
	public void test8() {
		setUp();
		m.setPerson(p);
		
		((HousingResidentRole)r).myState = State.Idle;
		((HousingResidentRole)r).isActive = false;
		((Role)r).msgIsActive();
		
		assertTrue(((Role)r).isActive);
		assertEquals(State.Entering, ((HousingResidentRole)r).myState);
		
		r.pickAndExecuteAnAction();
		
		assertEquals(State.Idle, ((HousingResidentRole)r).myState);
	}
	
	@Test
	public void test9() {
		setUp();
		
		p.event = AgentEvent.WakingUp;
		r.pickAndExecuteAnAction();
		
		items.get(1).breakIt();
		
		assertEquals(RepairStage.NeedsRepair, ((HousingResidentRole)r).repairStage);
		
		((Role)r).msgIsInActive();
		
		assertTrue(((HousingResidentRole)r).needToLeave());
		
		r.pickAndExecuteAnAction();
		
		assertFalse(((HousingResidentRole)r).needToLeave());
		assertFalse(((HousingResidentRole)r).leisure());
		assertFalse(((Role)r).isActive);
		assertEquals(State.Idle, ((HousingResidentRole)r).myState);
		
		((Role)r).msgIsActive();
		
		assertTrue(((Role)r).isActive);
		assertEquals(State.Entering, ((HousingResidentRole)r).myState);
		
		r.pickAndExecuteAnAction();
		
		assertEquals(State.Idle, ((HousingResidentRole)r).myState);
		
		p.state = AgentState.EatingAtHome;
		p.hunger = HungerState.Eating;
		r.pickAndExecuteAnAction();
		
		assertEquals(State.FoodCooked, ((HousingResidentRole)r).myState);
		
		r.pickAndExecuteAnAction();
		
		assertEquals(State.Idle, ((HousingResidentRole)r).myState);
		
		assertEquals(1, h.getBrokenItems().size());
		
		p.hunger = HungerState.NotHungry;
		p.state = AgentState.Idle;
		
		r.pickAndExecuteAnAction();
		
		assertEquals(1, ((MockRepairMan)m).log.size());
		assertEquals("House called for help. Money given: $20.0.", ((MockRepairMan)m).log.getLastLoggedEvent().getMessage());
		assertEquals(RepairStage.HelpRequested, ((HousingResidentRole)r).repairStage);
		assertEquals(State.Idle, ((HousingResidentRole)r).myState);
		
		r.ImHere();
		
		assertEquals(RepairStage.RepairManIsHere, ((HousingResidentRole)r).repairStage);
		
		r.repairDone();
		assertEquals(RepairStage.None, ((HousingResidentRole)r).repairStage);
	}
	
	@Test
	public void test10() {
		setUp();
		m.setPerson(p);
		
		p.event = AgentEvent.WakingUp;
		r.pickAndExecuteAnAction();
		
		items.get(1).breakIt();
		
		assertEquals(RepairStage.NeedsRepair, ((HousingResidentRole)r).repairStage);
		
		((Role)r).msgIsInActive();
		
		assertTrue(((HousingResidentRole)r).needToLeave());
		
		r.pickAndExecuteAnAction();
		
		assertFalse(((HousingResidentRole)r).needToLeave());
		assertFalse(((HousingResidentRole)r).leisure());
		assertFalse(((Role)r).isActive);
		assertEquals(State.Idle, ((HousingResidentRole)r).myState);
		
		((Role)r).msgIsActive();
		
		assertTrue(((Role)r).isActive);
		assertEquals(State.Entering, ((HousingResidentRole)r).myState);
		
		r.pickAndExecuteAnAction();
		
		assertEquals(State.Idle, ((HousingResidentRole)r).myState);
		
		p.state = AgentState.EatingAtHome;
		p.hunger = HungerState.Eating;
		r.pickAndExecuteAnAction();
		
		assertEquals(State.FoodCooked, ((HousingResidentRole)r).myState);
		
		r.pickAndExecuteAnAction();
		
		assertEquals(State.Idle, ((HousingResidentRole)r).myState);
		
		assertEquals(1, h.getBrokenItems().size());
		
		p.hunger = HungerState.NotHungry;
		p.state = AgentState.Idle;
		
		r.pickAndExecuteAnAction();
		
		assertEquals(0, ((MockRepairMan)m).log.size());
		assertEquals(RepairStage.None, ((HousingResidentRole)r).repairStage);
		assertEquals(State.Idle, ((HousingResidentRole)r).myState);
	}
	
	public void setUp() {
		r = null;
		p = null;
		m = null;
		h = null;
		items = null;
		
		r = new HousingResidentRole();
		p = new MockPeopleHousing("Stupid Harry");
		m = new MockRepairMan();
		h = new House("House", 1, HouseType.Villa);
		
		h.testModeOn();
		h.setItemsWithoutGui();
		h.setOccupant(r);
		items = h.items;
		
		((HousingResidentRole)r).testModeOn();
		((Role)r).setPerson(p);
		r.setHouse(h);
		r.setRepairMan(m);
	}
}