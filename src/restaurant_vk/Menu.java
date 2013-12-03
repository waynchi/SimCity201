package restaurant_vk;

import java.util.ArrayList;
import java.util.List;

/*
 * The menu, which contains al the food items, along with their prices and
 * cooking times.
 */
public class Menu {
	private class Food {
		private String name;
		private double price;
		
		public Food(String name, double price) {
			this.name = name;
			this.price = price;
		}
	}
	
	private List<Food> list;
	
	public Menu() {
		list = new ArrayList<Food>();
		
		list.add(new Food("Steak", 15.99));
		list.add(new Food("Chicken", 10.99));
		list.add(new Food("Salad", 5.99));
		list.add(new Food("Pizza", 8.99));
	}
	
	public double getPrice(String item) {
		double price = 0.0;
		for (Food f : list) {
			if (f.name.equals(item)) {
				price = f.price;
				break;
			}
		}
		return price;
	}
	
	public Menu removeAndReturn(String choice) {
		Food ref = null;;
		for (Food f : list) {
			if (f.name.equals(choice)) {
				ref = f;
				break;
			}
		}
		list.remove(ref);
		return this;
	}
	
	public List<String> getAllFoodNames() {
		List<String> l = new ArrayList<String>();
		for (Food f : list) {
			l.add(f.name);
		}
		return l;
	}
	
	/*
	 * Method to deep copy this menu.
	 */
	public Menu deepCopy() {
		Menu m = new Menu();
		List<String> l = m.getAllFoodNames();
		List<String> k = this.getAllFoodNames();
		for (String s : l) {
			boolean found = false;
			for (String t : k) {
				if (s.equals(t)) {
					found = true;
					break;
				}
			}
			if (found == false) {
				m = m.removeAndReturn(s);
			}
		}
		return m;
	}
}
