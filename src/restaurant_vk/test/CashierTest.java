package restaurant_vk.test;

import static org.junit.Assert.*;
import java.util.List;
import org.junit.Test;
import restaurant_vk.VkCashierRole;
import restaurant_vk.VkCashierRole.Bill;
import restaurant_vk.CustomerRestaurantCheck;
import restaurant_vk.interfaces.Customer;
import restaurant_vk.interfaces.Market;
import restaurant_vk.interfaces.Waiter;
import restaurant_vk.test.mock.MockCustomer;
import restaurant_vk.test.mock.MockMarket;
import restaurant_vk.test.mock.MockWaiter;

public class CashierTest {
	VkCashierRole cashier;
	Market m1;
	Market m2;
	Customer c1;
	Customer c2;
	Waiter w1;
	Waiter w2;

	/*
	 * Simple scenario to test the computeBill message. Two customers
	 * and two waiters.
	 */
	@Test
	public void computeBillTest() {
		setUp();
		
		assertTrue(cashier.getChecks().isEmpty());
		assertEquals(0, cashier.getChecks().size());
		cashier.computeBill(c1, "Steak", w1);
		assertFalse(cashier.getChecks().isEmpty());
		assertEquals(1, cashier.getChecks().size());
		cashier.computeBill(c2, "Pizza", w2);
		assertEquals(2, cashier.getChecks().size());
	}
	
	/*
	 * Simple scenario to test delvierCheckToWaiter action of the cashier.
	 * Two customers and two waiters.
	 */
	@Test
	public void deliverCheckToWaiterTest1() {
		setUp();
		
		assertEquals(0, cashier.getChecks().size());
		cashier.computeBill(c1, "Steak", w1);
		assertEquals(1, cashier.getChecks().size());
		cashier.computeBill(c2, "Pizza", w2);
		assertEquals(2, cashier.getChecks().size());
		cashier.pickAndExecuteAnAction();
		cashier.pickAndExecuteAnAction();
		assertEquals(2, cashier.getChecks().size());
		assertEquals("New check $15.99 Steak Customer1 received", ((MockWaiter) w1).log.getLastLoggedEvent().getMessage());
		assertEquals("New check $8.99 Pizza Customer2 received", ((MockWaiter) w2).log.getLastLoggedEvent().getMessage());
	}
	
	/*
	 * Another test. Mixed test, to evaluate working of the above message and
	 * action together. One bill for c1. One for c2. And another for c1.
	 */
	@Test
	public void deliverCheckToWaiterTest2() {
		setUp();
		
		assertEquals(0, cashier.getChecks().size());
		cashier.computeBill(c1, "Steak", w1);
		assertEquals(1, cashier.getChecks().size());
		cashier.computeBill(c2, "Pizza", w2);
		assertEquals(2, cashier.getChecks().size());
		cashier.computeBill(c2, "Pizza", w1);
		assertEquals(3, cashier.getChecks().size());
		cashier.pickAndExecuteAnAction();
		assertEquals("New check $15.99 Steak Customer1 received", ((MockWaiter) w1).log.getLastLoggedEvent().getMessage());
		cashier.pickAndExecuteAnAction();
		cashier.pickAndExecuteAnAction();
		assertEquals(3, cashier.getChecks().size());
		assertEquals("New check $8.99 Pizza Customer2 received", ((MockWaiter) w2).log.getLastLoggedEvent().getMessage());
		assertEquals("New check $8.99 Pizza Customer2 received", ((MockWaiter) w1).log.getLastLoggedEvent().getMessage());
		assertEquals(2, ((MockWaiter)w1).log.size());
		assertEquals(1, ((MockWaiter)w2).log.size());
	}
	
	/*
	 * A test to check functioning of hereIsPayment message. Two customers
	 * receive their checks. One of them goes to pay and does the payment.
	 * Then the other goes to pay and hands over the payment. Both the customers
	 * have enough money to pay the cashier.
	 */
	@Test
	public void hereIsPaymentTest() {
		setUp();
		
		cashier.computeBill(c1, "Steak", w1);
		cashier.pickAndExecuteAnAction();
		cashier.computeBill(c2, "Steak", w2);
		cashier.pickAndExecuteAnAction();
		cashier.pickAndExecuteAnAction();
		assertEquals("New check $15.99 Steak Customer1 received", ((MockWaiter) w1).log.getLastLoggedEvent().getMessage());
		assertEquals("New check $15.99 Steak Customer2 received", ((MockWaiter) w2).log.getLastLoggedEvent().getMessage());
		CustomerRestaurantCheck ch1 = ((MockWaiter) w1).getCheck(c1);
		((MockCustomer) c1).checks.add(ch1);
		cashier.hereIsPayment(ch1, 20, ((MockCustomer) c1).checks);
		assertTrue(cashier.isCheckBeingPaid(c1));
		CustomerRestaurantCheck ch2 = ((MockWaiter) w2).getCheck(c2);
		((MockCustomer) c2).checks.add(ch2);
		cashier.hereIsPayment(ch2, 20, ((MockCustomer) c2).checks);
		assertTrue(cashier.isCheckBeingPaid(c2));
	}
	
	/*
	 * Normative scenario. Customer has enough money o pay and the payment gets
	 * completed n the first go.
	 */
	@Test
	public void decideResponseForCheckTest1() {
		setUp();
		
		cashier.computeBill(c1, "Steak", w1);
		cashier.computeBill(c2, "Steak", w2);
		cashier.pickAndExecuteAnAction();
		cashier.pickAndExecuteAnAction();
		assertEquals("New check $15.99 Steak Customer1 received", ((MockWaiter) w1).log.getLastLoggedEvent().getMessage());
		assertEquals("New check $15.99 Steak Customer2 received", ((MockWaiter) w2).log.getLastLoggedEvent().getMessage());
		
		CustomerRestaurantCheck ch1 = ((MockWaiter) w1).getCheck(c1);
		((MockCustomer) c1).checks.add(ch1);
		cashier.hereIsPayment(ch1, 20, ((MockCustomer) c1).checks);
		assertTrue(cashier.isCheckBeingPaid(c1));
		
		CustomerRestaurantCheck ch2 = ((MockWaiter) w2).getCheck(c2);
		((MockCustomer) c2).checks.add(ch2);
		cashier.hereIsPayment(ch2, 20, ((MockCustomer) c2).checks);
		assertTrue(cashier.isCheckBeingPaid(c2));
		
		Customer c3 = new MockCustomer("Customer3");
		Customer c4 = new MockCustomer("Customer4");
		cashier.computeBill(c3, "Steak", w1);
		cashier.computeBill(c4, "Steak", w2);
		
		cashier.pickAndExecuteAnAction();
		assertEquals(1, cashier.getPaidChecksNumber(c1));
		assertEquals(0, cashier.getPaidChecksNumber(c2));
		cashier.pickAndExecuteAnAction();
		assertEquals(1, cashier.getPaidChecksNumber(c1));
		assertEquals(1, cashier.getPaidChecksNumber(c2));
		assertEquals("New check $15.99 Steak Customer1 received", ((MockWaiter) w1).log.getLastLoggedEvent().getMessage());
		assertEquals("New check $15.99 Steak Customer2 received", ((MockWaiter) w2).log.getLastLoggedEvent().getMessage());
		
		cashier.pickAndExecuteAnAction();
		cashier.pickAndExecuteAnAction();
		assertEquals("New check $15.99 Steak Customer3 received", ((MockWaiter) w1).log.getLastLoggedEvent().getMessage());
		assertEquals("New check $15.99 Steak Customer4 received", ((MockWaiter) w2).log.getLastLoggedEvent().getMessage());
	}
	
	/*
	 * Non-norm: Customer has less money. Pays next time.
	 */
	@Test
	public void decideResponseForCheckTest2() {
		setUp();
		
		// First time, not paid.
		cashier.computeBill(c1, "Steak", w1);
		cashier.pickAndExecuteAnAction();
		assertEquals("New check $15.99 Steak Customer1 received", ((MockWaiter) w1).log.getLastLoggedEvent().getMessage());
		CustomerRestaurantCheck ch1 = ((MockWaiter) w1).getCheck(c1);
		((MockCustomer) c1).checks.add(ch1);
		cashier.hereIsPayment(ch1, 10, ((MockCustomer) c1).checks);
		assertTrue(cashier.isCheckBeingPaid(c1));
		cashier.pickAndExecuteAnAction();
		assertEquals(0, cashier.getPaidChecksNumber(c1));
		assertEquals(1, cashier.getLessPaidChecksNumber(c1));
		assertEquals(10, ((MockCustomer) c1).cashReceived, 0.0000001);
		
		// Second time, paid.
		cashier.computeBill(c1, "Steak", w1);
		cashier.pickAndExecuteAnAction();
		assertEquals("New check $15.99 Steak Customer1 received", ((MockWaiter) w1).log.getLastLoggedEvent().getMessage());
		ch1 = ((MockWaiter) w1).getCheck(c1);
		((MockCustomer) c1).checks.add(ch1);
		cashier.hereIsPayment(ch1, 32, ((MockCustomer) c1).checks);
		assertTrue(cashier.isCheckBeingPaid(c1));
		cashier.pickAndExecuteAnAction();
		assertEquals(2, cashier.getPaidChecksNumber(c1));
		assertEquals(0, cashier.getLessPaidChecksNumber(c1));
		assertEquals(0.02, ((MockCustomer) c1).cashReceived, 0.0000001);
		
		// Third time, not paid.
		cashier.computeBill(c1, "Steak", w1);
		cashier.pickAndExecuteAnAction();
		assertEquals("New check $15.99 Steak Customer1 received", ((MockWaiter) w1).log.getLastLoggedEvent().getMessage());
		ch1 = ((MockWaiter) w1).getCheck(c1);
		((MockCustomer) c1).checks.add(ch1);
		cashier.hereIsPayment(ch1, 10, ((MockCustomer) c1).checks);
		assertTrue(cashier.isCheckBeingPaid(c1));
		cashier.pickAndExecuteAnAction();
		assertEquals(2, cashier.getPaidChecksNumber(c1));
		assertEquals(1, cashier.getLessPaidChecksNumber(c1));
		assertEquals(10, ((MockCustomer) c1).cashReceived, 0.0000001);
		
		// Fourth time, paid.
		cashier.computeBill(c1, "Steak", w1);
		cashier.pickAndExecuteAnAction();
		assertEquals("New check $15.99 Steak Customer1 received", ((MockWaiter) w1).log.getLastLoggedEvent().getMessage());
		ch1 = ((MockWaiter) w1).getCheck(c1);
		((MockCustomer) c1).checks.add(ch1);
		cashier.hereIsPayment(ch1, 32, ((MockCustomer) c1).checks);
		assertTrue(cashier.isCheckBeingPaid(c1));
		cashier.pickAndExecuteAnAction();
		assertEquals(4, cashier.getPaidChecksNumber(c1));
		assertEquals(0, cashier.getLessPaidChecksNumber(c1));
		assertEquals(0.02, ((MockCustomer) c1).cashReceived, 0.0000001);
	}
	
	/*
	 * Simple testing of hereIsBillForMaterials message of the cashier.
	 */
	@Test
	public void hereIsBillForMaterialsTest() {
		setUp();
		
		assertEquals(0, cashier.getBills().size());
		cashier.hereIsBillForMaterials(m1, 15);
		assertEquals(1, cashier.getBills().size());
		assertEquals(1, cashier.getBills().size());
		cashier.hereIsBillForMaterials(m1, 10);
		assertEquals(2, cashier.getBills().size());
		assertEquals(2, cashier.getBills().size());
		cashier.hereIsBillForMaterials(m2, 10);
		assertEquals(3, cashier.getBills().size());
	}
	
	/*
	 * Here, the cashier has enough money to pay the markets. Messages sent
	 * sequentially.
	 */
	@Test
	public void payBillTest1() {
		setUp();
		
		assertEquals(0, cashier.getBills().size());
		cashier.hereIsBillForMaterials(m1, 15);
		assertEquals(1, cashier.getBills().size());
		assertEquals(1, cashier.getBills().size());
		cashier.hereIsBillForMaterials(m2, 5);
		assertEquals(2, cashier.getBills().size());
		cashier.pickAndExecuteAnAction();
		cashier.pickAndExecuteAnAction();
		assertEquals(1, ((MockMarket) m1).log.size());
		assertEquals(1, ((MockMarket) m2).log.size());
		assertEquals("Received $15.0 from cashier", ((MockMarket) m1).log.getLastLoggedEvent().getMessage());
		assertEquals("Received $5.0 from cashier", ((MockMarket) m2).log.getLastLoggedEvent().getMessage());
	}
	
	/*
	 * Here too, the cashier has enough money to pay the markets. But it is
	 * a type of test for One-Order-Two-Bills-Scenario.
	 */
	@Test
	public void payBillTest2() {
		setUp();
		
		assertEquals(0, cashier.getBills().size());
		cashier.hereIsBillForMaterials(m1, 1);
		assertEquals(1, cashier.getBills().size());
		assertEquals(1, cashier.getBills().size());
		cashier.hereIsBillForMaterials(m1, 2);
		assertEquals(2, cashier.getBills().size());
		assertEquals(2, cashier.getBills().size());
		cashier.hereIsBillForMaterials(m2, 2);
		assertEquals(3, cashier.getBills().size());
		assertEquals(3, cashier.getBills().size());
		cashier.hereIsBillForMaterials(m1, 3);
		assertEquals(4, cashier.getBills().size());
		cashier.pickAndExecuteAnAction();
		cashier.pickAndExecuteAnAction();
		assertEquals(2, ((MockMarket) m1).log.size());
		cashier.pickAndExecuteAnAction();
		assertEquals(1, ((MockMarket) m2).log.size());
		cashier.pickAndExecuteAnAction();
		assertEquals(3, ((MockMarket) m1).log.size());
		assertEquals("Received $3.0 from cashier", ((MockMarket) m1).log.getLastLoggedEvent().getMessage());
		assertEquals("Received $2.0 from cashier", ((MockMarket) m2).log.getLastLoggedEvent().getMessage());
	}
	
	/*
	 * Non-norm: Insufficient cash to pay a bill. The cashier initially has
	 * only $20 in his account.
	 */
	@Test
	public void payBillTest3() {
		setUp();
		
		assertEquals(0, cashier.getBills().size());
		cashier.hereIsBillForMaterials(m1, 30);
		assertEquals(1, cashier.getBills().size());
		cashier.pickAndExecuteAnAction();
		assertEquals(0, ((MockMarket) m1).log.size());
		assertEquals(1, cashier.getBills().size());
		cashier.hereIsBillForMaterials(m1, 10);
		assertEquals(2, cashier.getBills().size());
		assertEquals(2, cashier.getBills().size());
		cashier.hereIsBillForMaterials(m2, 30);
		assertEquals(3, cashier.getBills().size());
		assertEquals(3, cashier.getBills().size());
		cashier.hereIsBillForMaterials(m2, 10);
		assertEquals(4, cashier.getBills().size());
		assertEquals(4, cashier.getBills().size());
		cashier.hereIsBillForMaterials(m2, 10);
		assertEquals(5, cashier.getBills().size());
		cashier.pickAndExecuteAnAction();
		cashier.pickAndExecuteAnAction();
		assertEquals(1, ((MockMarket) m1).log.size());
		cashier.pickAndExecuteAnAction();
		assertEquals(1, ((MockMarket) m2).log.size());
		cashier.pickAndExecuteAnAction();
		assertEquals(1, ((MockMarket) m1).log.size());
		assertEquals("Received $10.0 from cashier", ((MockMarket) m1).log.getLastLoggedEvent().getMessage());
		assertEquals("Received $10.0 from cashier", ((MockMarket) m2).log.getLastLoggedEvent().getMessage());
		
		cashier.addCash(30.0);
		cashier.pickAndExecuteAnAction();
		assertEquals(2, ((MockMarket) m1).log.size());
		assertEquals("Received $30.0 from cashier", ((MockMarket) m1).log.getLastLoggedEvent().getMessage());
		
		cashier.pickAndExecuteAnAction();
		assertEquals(2, ((MockMarket) m1).log.size());
		assertEquals(1, ((MockMarket) m2).log.size());
		
		cashier.addCash(50);
		cashier.pickAndExecuteAnAction();
		assertEquals(2, ((MockMarket) m1).log.size());
		assertEquals(2, ((MockMarket) m2).log.size());
		assertEquals("Received $30.0 from cashier", ((MockMarket) m2).log.getLastLoggedEvent().getMessage());
		cashier.pickAndExecuteAnAction();
		assertEquals(2, ((MockMarket) m1).log.size());
		assertEquals(3, ((MockMarket) m2).log.size());
		assertEquals("Received $10.0 from cashier", ((MockMarket) m2).log.getLastLoggedEvent().getMessage());
		assertEquals(10.0, cashier.getCash(), 0.0000001);
	}
	
	/*
	 * The customer bill and market bill are paid. Mixed Scenario.
	 */
	@Test
	public void mixedScenarioTest() {
		setUp();
		
		cashier.computeBill(c1, "Steak", w1);
		cashier.hereIsBillForMaterials(m1, 10);
		cashier.pickAndExecuteAnAction();
		assertEquals("New check $15.99 Steak Customer1 received", ((MockWaiter) w1).log.getLastLoggedEvent().getMessage());
		assertEquals(0, ((MockMarket) m1).log.size());
		cashier.pickAndExecuteAnAction();
		assertEquals(1, ((MockMarket) m1).log.size());
		assertEquals("Received $10.0 from cashier", ((MockMarket) m1).log.getLastLoggedEvent().getMessage());
		
		cashier.hereIsBillForMaterials(m2, 10);
		CustomerRestaurantCheck ch1 = ((MockWaiter) w1).getCheck(c1);
		((MockCustomer) c1).checks.add(ch1);
		cashier.hereIsPayment(ch1, 20, ((MockCustomer) c1).checks);
		assertTrue(cashier.isCheckBeingPaid(c1));
		cashier.pickAndExecuteAnAction();
		assertEquals(0, ((MockMarket) m2).log.size());
		assertEquals(1, cashier.getPaidChecksNumber(c1));
		assertEquals(1, ((MockCustomer) c1).log.size());
	}
	
	public void setUp() {
		cashier = null;
		cashier = new VkCashierRole();
		
		m1 = null;
		m1 = new MockMarket("Market1");
		m2 = null;
		m2 = new MockMarket("Market2");
		
		c1 = null;
		c1 = new MockCustomer("Customer1");
		c2 = null;
		c2 = new MockCustomer("Customer2");
		
		w1 = null;
		w1 = new MockWaiter("Waiter1");
		w2 = null;
		w2 = new MockWaiter("Waiter2");
	}
}
