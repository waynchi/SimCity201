package market.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import restaurant.CookRole;
import restaurant.interfaces.Cook;
import transportation.interfaces.Car;
import city.gui.CityPanel;
import city.gui.Lane;
import city.gui.VehicleGui;
import market.MarketTruckAgent;
import market.interfaces.MarketTruck;


public class MarketTruckGui extends VehicleGui{

	private MarketTruckAgent truckAgent;
	String destination;
	int restaurantIndex;
	Map<Integer, String> restaurantMap = new HashMap<Integer, String>();

	public MarketTruckGui(int i, int j, int k, int l, ArrayList<Lane> road2, Lane lane, ArrayList<ArrayList<Lane>> allRoads, CityPanel cityPanel){
		super(5, 5, 10, 10, road2, road2.get(0), allRoads, cityPanel,"Truck");	
		restaurantMap.put(1, "Restaurant 1");
		restaurantMap.put(2, "Restaurant 2");

	}

	public void doDeliver(MarketTruckAgent mt, Cook cook,int orderNumber) {
		truckAgent = mt;
		this.restaurantIndex = ((CookRole)cook).getRestaurantIndex();//
		destination = restaurantMap.get(restaurantIndex+1);
		super.driveHere(destination);
	}

	public void doGoBackToMarket() {
		destination = "Market";
	}
	
	@Override
	public void reachedDestination(){
		truckAgent.msgAnimationFinishedArrivedAtDestination(destination);
	}
	
}
