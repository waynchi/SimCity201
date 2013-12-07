package restaurant_vk.test;

import static org.junit.Assert.*;
import java.util.List;
import org.junit.Test;
import restaurant_vk.VkHostRole;
import restaurant_vk.interfaces.Customer;
import restaurant_vk.interfaces.Host;
import restaurant_vk.interfaces.Waiter;
import restaurant_vk.test.mock.MockCustomer;
import restaurant_vk.test.mock.MockWaiter;

public class HostTest {

	@Test
	public void sampleTest() {
		VkHostRole host = new VkHostRole("Vader");
		Customer c = new MockCustomer("Customer 1");
		List<Customer> list = host.getCustomers();
		assertTrue(list.isEmpty());
		assertFalse(((VkHostRole) host).isATableOccupied());
		host.IWantToEat(c);
		assertFalse(list.isEmpty());
		Waiter w = new MockWaiter("Waiter 1");
		host.addWaiter(w);
		host.pickAndExecuteAnAction();
		assertTrue(((VkHostRole) host).isATableOccupied());
	}

}
