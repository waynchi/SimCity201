package people;

import java.util.HashMap;
import java.util.Map;

import city.Restaurant;

public class Yelp {
	
	public Map<Restaurant, Integer> Ratings = new HashMap<Restaurant, Integer>();

	public Yelp() {
		// TODO Auto-generated constructor stub
		System.out.println("Yelp Website Created!");
	}
	
	public void addRestaurant(Restaurant r , Integer rating)
	{
		Ratings.put(r, rating);
	}

}
