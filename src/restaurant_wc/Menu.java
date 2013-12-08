package restaurant_wc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu {
	public List<String> Choices = Collections.synchronizedList(new ArrayList<String>());
	//public Map<String, Food> FoodTypes = new HashMap<String, Food>();
	public Map<String, Double> FoodCosts = new HashMap<String, Double>();
	
	Menu(List<String> choices, ArrayList<Double> cost) {
		this.Choices = choices;
		
		for(int i = 0; i < choices.size(); i++)
		{
			FoodCosts.put(choices.get(i), cost.get(i));
		}
	}
	
	Menu(List<String> choices, Map<String, Double> Costs)
	{
		synchronized(Choices){
		for(int i = 0; i < choices.size(); i++)
		{
			Choices.add(choices.get(i));
			FoodCosts.put(choices.get(i), Costs.get(choices.get(i)));
		}
		}
	}
}
