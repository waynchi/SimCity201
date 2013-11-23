package restaurant.interfaces;

import java.util.Map;

import people.People;

public interface Cook {

	abstract People getPerson();

	abstract void msgHereIsYourOrder(Map<String, Integer> items);

}
