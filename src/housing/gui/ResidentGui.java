package housing.gui;

import housing.HouseType;
import housing.interfaces.Resident;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ResidentGui implements HGui{
	private int xDestination;
	private int yDestination;
	private int xPos;
	private int yPos;
	private State state = State.Idle;
	private Location location = Location.Home;
	private Timer timer = new Timer();
	private Timer cellPhoneTimer = new Timer();
	private final int CELLPHONE_TIME = 2000;
	private final int POOPING_TIME = 5000;
	private final int PEEING_TIME = 2000;
	private final int BATHING_TIME = 7000;
	private final int PREPING_TIME = 1000;
	private final int COOKING_TIME = 2000;
	private boolean readingBook = false;
	private boolean videoGames = false;
	private boolean cellPhone = false;
	public Resident r;
	public HouseGui hGui;
	public ApartmentsGui aGui;
	
	private enum State {Idle, Pooping, Peeing, Bathing, FetchingFromShelves, Preping, Cooking, Eating, Reading,
		WatchingTV, RelaxingOnSofa, Sleeping, PlayingVideoGames, PlayingFussball, Leaving, Entering};
	private enum Location {Home, Apartments, Outside};
	
	public ResidentGui(Resident r) {
		this.r = r;
	}

	@Override
	public void updatePosition() {
		if (state != State.Reading && state != State.Idle)
			readingBook = false;
		if (state != State.PlayingVideoGames && state != State.Idle)
			videoGames = false;
		
		if (xPos < xDestination)
			xPos++;
		else if (yPos < yDestination)
			yPos++;
		else if (xPos > xDestination)
			xPos--;
		else if (yPos > yDestination)
			yPos--;
		
		if (xPos == xDestination && yPos == yDestination) {
			if (state == State.FetchingFromShelves) {
				state = State.Preping;
				Random generator = new Random();
				int t;
				if (hGui.h.type == HouseType.Villa)
					t = 2;
				else
					t = 1;
				int num = generator.nextInt(t);
				num++;
				String s = "CookingSlab" + num;
				goToLocation(hGui.getPosition(s));
			}
			else if (state == State.Preping) {
				timer.schedule(getTimerTask(state), PREPING_TIME);
				state = State.Cooking;
			}
			else if (state == State.Cooking) {
				timer.schedule(getTimerTask(state), COOKING_TIME);
				state = State.Idle;
			}
			else if (state == State.Pooping) {
				timer.schedule(getTimerTask(state), POOPING_TIME);
				state = State.Idle;
			}
			else if (state == State.Peeing) {
				timer.schedule(getTimerTask(state), PEEING_TIME);
				state = State.Idle;
			}
			else if (state == State.Bathing) {
				timer.schedule(getTimerTask(state), BATHING_TIME);
				state = State.Idle;
			}
			else if (state == State.Reading) {
				state = State.Idle;
				readingBook = true;
			}
			else if (state == State.PlayingVideoGames) {
				state = State.Idle;
				videoGames = true;
			}
			else if (state == State.PlayingFussball) {
				state = State.Idle;
			}
			else if (state == State.Leaving) {
				videoGames = false;
				readingBook = false;
				if (hGui.h.type == HouseType.Apartment) {
					if (this.location == Location.Home) {
						location = Location.Apartments;
						xPos = hGui.entranceCoordinatesExternal.width;
						yPos = hGui.entranceCoordinatesExternal.height;
						goToLocation(aGui.entranceCoordinates);
					}
					else if (this.location == Location.Apartments) {
						state = State.Idle;
						location = Location.Outside;
						r.leftHouse();
					}
				}
				else {
					state = State.Idle;
					r.leftHouse();
				}
			}
			else if (state == State.Entering) {
				if (hGui.h.type == HouseType.Apartment) {
					if (this.location == Location.Apartments) {
						location = Location.Home;
						xPos = hGui.entranceCoordinatesInternal.width;
						yPos = hGui.entranceCoordinatesInternal.height;
						Random generator = new Random();
						int num = generator.nextInt(2);
						num++;
						Dimension d = hGui.getPosition("Chair" + num);
						goToLocation(d);
					}
					else {
						state = State.Idle;
					}
				}
				else {
					state = State.Idle;
				}
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		if (this.location != Location.Outside) {
			g.setColor(Color.BLUE);
			g.fillOval(xPos, yPos, 15, 15);
			if (readingBook == true) {
				g.setColor(Color.green);
				g.fill3DRect(xPos + 2, yPos - 14, 6, 4, true);
			}
			if (videoGames == true) {
				g.setColor(Color.darkGray);
				g.fill3DRect(xPos + 2, yPos - 14, 6, 4, true);
			}
			if (cellPhone == true) {
				g.setColor(Color.black);
				g.fillRoundRect(xPos - 3, yPos - 6, 3, 6, 1, 2);
			}
		}
	}

	@Override
	public boolean isPresent() {
		if (location == Location.Home)
			return true;
		return false;
	}
	
	public void setHouseGui(HouseGui hGui) {
		this.hGui = hGui;
	}
	
	public void setApartmentsGui(ApartmentsGui aGui) {
		this.aGui = aGui;
	}
	
	public void DoPoop() {
		state = State.Pooping;
		Dimension d = hGui.getPosition("Toilet");
		goToLocation(d);
	}
	
	public void DoBathe() {
		state = State.Bathing;
		Dimension d = hGui.getPosition("BathTub");
		goToLocation(d);
	}

	public void DoPee() {
		state = State.Peeing;
		Dimension d = hGui.getPosition("Toilet");
		goToLocation(d);
	}
	
	public void DoWatchTV() {
		state = State.WatchingTV;
		Random generator = new Random();
		int t;
		if (hGui.h.type == HouseType.Villa)
			t = 3;
		else
			t = 1;
		int num = generator.nextInt(t);
		num++;
		String sofa = "Sofa" + num;
		Dimension d = hGui.getPosition(sofa);
		goToLocation(d);
	}
	
	public void DoCook() {
		state = State.FetchingFromShelves;
		Dimension d = hGui.getPosition("Shelves");
		goToLocation(d);
	}
	
	public void DoEat() {
		state = State.Eating;
		Random generator = new Random();
		int t;
		if (hGui.h.type == HouseType.Villa)
			t = 4;
		else
			t = 2;
		int num = generator.nextInt(t);
		num++;
		Dimension d = hGui.getPosition("Chair" + num);
		goToLocation(d);
	}
	
	public void DoSleep() {
		state = State.Sleeping;
		Dimension d = hGui.getPosition("Bed");
		goToLocation(d);
	}
	
	public void DoRelaxOnSofa() {
		state = State.RelaxingOnSofa;
		Dimension d = hGui.getPosition("Sofa1");
		goToLocation(d);
	}
	
	public void DoRead() {
		state = State.Reading;
		Dimension d = hGui.getPosition("StudyChair");
		goToLocation(d);
	}
	
	public void DoPlayVideoGames() {
		state = State.PlayingVideoGames;
		Dimension d = hGui.getPosition("StudyChair");
		goToLocation(d);
	}
	
	public void DoPlayFussball() {
		state = State.PlayingFussball;
		Dimension d = hGui.getPosition("FussballTable");
		d.width += 25;
		d.height += 40;
		goToLocation(d);
	}
	
	public void DoEnterHome() {
		state = State.Entering;
		if (hGui.h.type == HouseType.Apartment) {
			location = Location.Apartments;
			xPos = aGui.entranceCoordinates.width;
			yPos = aGui.entranceCoordinates.height;
			goToLocation(hGui.entranceCoordinatesExternal);
		}
		else {
			location = Location.Home;
			xPos = hGui.entranceCoordinatesInternal.width;
			yPos = hGui.entranceCoordinatesInternal.height;
			Random generator = new Random();
			int t;
			if (hGui.h.type == HouseType.Villa)
				t = 4;
			else
				t = 2;
			int num = generator.nextInt(t);
			num++;
			Dimension d = hGui.getPosition("Chair" + num);
			goToLocation(d);
		}
	}
	
	public void DoLeaveHome() {
		state = State.Leaving;
		goToLocation(new Dimension(hGui.entranceCoordinatesInternal.width, hGui.entranceCoordinatesInternal.height));
	}
	
	public void DoUseCellPhone() {
		cellPhone = true;
		cellPhoneTimer.schedule(new TimerTask() {
			public void run() {
				cellPhone = false;
			}
		}, CELLPHONE_TIME);
	}
	
	public void goToLocation(Dimension d) {
		xDestination = d.width;
		yDestination = d.height;
	}
	
	public boolean isAtHome() {
		if (location == Location.Home)
			return true;
		return false;
	}
	
	private TimerTask getTimerTask(State s) {
		final Resident t = this.r;
		if (s == State.Preping) {
			return new TimerTask() {
				public void run() {
					goToLocation(hGui.getPosition("CookingGrill"));
					System.out.println("Preping done.");
				}
			};
		}
		else if (s == State.Cooking) {
			return new TimerTask() {
				public void run() {
					System.out.println("Cooking done.");
					t.foodCooked();
				}
			};
		}
		else {
			return new TimerTask() {
				public void run() {
					t.activityDone();
				}
			};
		}
	}
}