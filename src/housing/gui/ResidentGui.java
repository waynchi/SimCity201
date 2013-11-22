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
	public Resident r;
	public HouseGui hGui;
	
	private enum State {Idle, Pooping, Peeing, Bathing, FetchingFromShelves, Preping, Cooking, Eating, Reading, WatchingTV, RelaxingOnSofa, Sleeping};
	
	// Use timers to implement cooking, and then call
	// r.activityDone().
	// Also, use timers to implement eating, then call
	// r.activityDone().
	
	public ResidentGui(Resident r) {
		this.r = r;
	}

	@Override
	public void updatePosition() {
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
				readingBook = true;
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillOval(xPos, yPos, 10, 10);
		if (readingBook == true) {
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
		readingBook = false;
		state = State.Pooping;
		Dimension d = hGui.getPosition("Toilet");
		goToLocation(d);
	}
	
	public void DoBathe() {
		readingBook = false;
		state = State.Bathing;
		Dimension d = hGui.getPosition("BathTub");
		goToLocation(d);
	}

	public void DoPee() {
		readingBook = false;
		state = State.Peeing;
		Dimension d = hGui.getPosition("Toilet");
		goToLocation(d);
	}
	
	public void DoWatchTV() {
		readingBook = false;
		state = State.WatchingTV;
		Random generator = new Random();
		int num = generator.nextInt(3);
		num++;
		String sofa = "Sofa" + num;
		Dimension d = hGui.getPosition(sofa);
		goToLocation(d);
	}
	
	public void DoCook() {
		readingBook = false;
		state = State.FetchingFromShelves;
		Dimension d = hGui.getPosition("Shelves");
		goToLocation(d);
	}
	
	public void DoEat() {
		readingBook = false;
		state = State.Eating;
		Random generator = new Random();
		int num = generator.nextInt(4);
		num++;
		Dimension d = hGui.getPosition("Chair" + num);
		goToLocation(d);
	}
	
	public void DoSleep() {
		readingBook = false;
		state = State.Sleeping;
		Dimension d = hGui.getPosition("Bed");
		goToLocation(d);
	}
	
	public void DoRelaxOnSofa() {
		readingBook = false;
		state = State.RelaxingOnSofa;
		Dimension d = hGui.getPosition("Sofa1");
		goToLocation(d);
	}
	
	public void DoRead() {
		readingBook = false;
		state = State.Reading;
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