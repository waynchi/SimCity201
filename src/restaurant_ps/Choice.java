package restaurant_ps;


public class Choice {

	/**
	 * @param args
	 */
	public Food food;
	public int cookingTime;
	public boolean isCooked;
	
	public Choice(Food food, int cookingTime){
		this.food = food;
		this.cookingTime = cookingTime;
		this.isCooked = false;
		
		
	}
	
	public void setIsCooked(boolean value)
	{
		this.isCooked = value;
	}
	

}
