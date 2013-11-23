package housing.gui;

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
	private State state = State.Idle;;
	private Timer timer;
	private final int POOPING_TIME = 5000;
	private final int PEEING_TIME = 2000;
	private final int BATHING_TIME = 7000;
	private final int PREPING_TIME = 1000;
	private final int COOKING_TIME = 2000;
	private boolean readingBook = false;
	private boolean videoGames = false;
	public Resident r;
	public HouseGui hGui;
	
	private enum State {Idle, Pooping, Peeing, Bathing, FetchingFromShelves, Preping, Cooking, Eating, Reading, WatchingTV, RelaxingOnSofa, Sleeping, PlayingVideoGames};
	
	// Use timers to implement cooking, and then call
	// r.activityDone().
	// Also, use timers to implement eating, then call
	// r.activityDone().
	
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
		if (yPos < yDestination)
			yPos++;
		if (xPos > xDestination)
			xPos--;
		if (yPos > yDestination)
			yPos--;
		
		if (xPos == xDestination && yPos == yDestination) {
			if (state == State.FetchingFromShelves) {
				state = State.Preping;
				Random generator = new Random();
				int num = generator.nextInt(2);
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
		}
	}

	@Override
	public void draw(Graphics2D g) {
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
	}

	@Override
	public boolean isPresent() {
		return false;
	}
	
	public void setHouseGui(HouseGui hGui) {
		this.hGui = hGui;
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
		int num = generator.nextInt(3);
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
		int num = generator.nextInt(4);
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
	
	public void goToLocation(Dimension d) {
		xDestination = d.width;
		yDestination = d.height;
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