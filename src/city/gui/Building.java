package city.gui;
import java.awt.geom.*;


public class Building extends Rectangle2D.Double {
	BuildingPanel myBuildingPanel;
	public int xLocation;
	public int yLocation;
	public String name;

	public Building( int x, int y, int width, int height, int xLoc, int yLoc, String name ) {
		super( x, y, width, height );
		this.xLocation = xLoc;
		this.yLocation = yLoc;
		this.name = name;
	}
	
	public void displayBuilding() {
		myBuildingPanel.displayBuildingPanel();
	}
	
	public void setBuildingPanel( BuildingPanel bp ) {
		myBuildingPanel = bp;
	}
}
