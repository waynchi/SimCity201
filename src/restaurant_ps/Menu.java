package restaurant_ps;
import java.util.ArrayList;
import java.util.List;


import restaurant_ps.Choice;
public class Menu {

		public ArrayList<Choice> menuItems = new ArrayList<Choice>();
		public List<Food> inventory;
		
		public Menu(List<Food> inventory){
			this.inventory = inventory;
			
			for(Food f : inventory)
			{
				if(f.amount > 0)
				menuItems.add(new Choice(f,f.cookingTime));
			}
			
		}
		
		public Choice chooseItem(int menuNumber)
		{
			return menuItems.get(menuNumber-1);
		}

}
