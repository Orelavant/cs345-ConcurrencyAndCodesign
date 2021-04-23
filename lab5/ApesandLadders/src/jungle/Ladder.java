import java.util.concurrent.*;
import java.util.ArrayList;

/**
 * @author davew
 *
 * The Ladder class is NOT a kind of thread, since it doesn't actually do anything except get used 
 * by Apes. The ladder just keeps track of how many apes are on each rung.
 */
public class Ladder {
	// @TODO Remove rungcapacity and replace with sem arr if by end that's okay.
	// ALSO REMOVE ANY METHODS THAT DON"T NEED TO BE SYNCHRONIZED.
	// USE LINKEDLISTS INSTEAD OF ARRAYS FOR Qs;
	// MAKE ADDING TO LADDER Q METHOD, SYNCHRONIZE IT SO IT DOESN'T CONFLICT WITH SENDING APES.
	// IF FAIRNESS MATTERS, WE CAN ADD A FIRST APE ATTRIBUTE SO THAT ONLY THE FIRST APE CHECKS IF LADDER IS AVAILABLE.
	private int rungCapacity[];
	private Semaphore[] semArr;
	private ArrayList<Ape> eastLadderq;
	private ArrayList<Ape> westLadderq;
	private static final int CAP = 5; // # of apes who can go next on the ladder
	private boolean ladderAvail;
	private boolean eastPriority; // Which side is ready to use the ladder.

	public Ladder(int _nRungs) {
		rungCapacity = new int[_nRungs];
		semArr = new Semaphore[_nRungs];
		// capacity 1 available on each rung
		for (int i=0; i<_nRungs; i++) {
            rungCapacity[i] = 1;
			Semaphore sem = new Semaphore(1, true); 
			semArr[i] = sem;
        }
		eastLadderq = new ArrayList<>();
		westLadderq = new ArrayList<>();
		ladderAvail = true;
	}

	public synchronized void sendApes(boolean eastApes) {
		// Set which q
		ArrayList<Ape> q;
		if (eastApes) {
			q = eastLadderq;
		} else {
			q = westLadderq;
		}

		// Send apes up to CAP if there are more than CAP.
		int i = 0;
		int size = q.size();
		int count;
		Ape currApe;
		if (size >= CAP) {
			count = CAP-1;
		} else {
			count = size - 1;
		}
		while(i != count) {
			currApe = q.get(0);
			currApe.setCanCross(true);
			q.remove(0); // SLOW, WILL CHANGE TO LINKED LIST.
			i++;
		}

		currApe = q.get(0);
		currApe.setCanCross(true);
		q.remove(0); // SLOW, WILL CHANGE TO LINKED LIST.
		currApe.lastApe = true; // THIS IS BAD SINCE ITS PUBLIC, CHANGE LATER.
	}

	public synchronized boolean getEastPriority() {
		return eastPriority;
	}

	public synchronized void setEastPriority(boolean bool){
		eastPriority = bool;
	}

	// Synchornized so that only 1 monkey can get and set ladderAvail.
	public synchronized boolean getAndSetLadderAvail() {
		if (ladderAvail) {
			ladderAvail = false;
			return true;
		}
		return ladderAvail;
	}

	// Synchronized so that only 1 monkey changes this.
	public synchronized void setLadderAvail(boolean bool){
		ladderAvail = bool;
	}

	public ArrayList<Ape> getEastLadderq() {
		return eastLadderq;
	}

	public ArrayList<Ape> getWestLadderq() {
		return westLadderq;
	}
	
    public int nRungs() {
		return rungCapacity.length;
	}

	// return True if rung is available, false if not.
	public boolean tryGrabRung(int which) {
		return semArr[which].tryAcquire();
	}

	public void releaseRung(int which) {
		semArr[which].release();
	}
}
