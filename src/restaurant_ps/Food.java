package restaurant_ps;

public class Food {

	/**
	 * @param args
	 */
	public String foodname;
	public int amount;
	public int capacity;
	public int cookingTime;
	public int low;
	public double price;
	
	public Food(String name, int cookingTime, int amount, double price) {
		this.cookingTime = cookingTime;
		foodname = name;
		this.amount = amount;
		capacity = 10;
		low = 5;
		this.price = price;
	}

}
