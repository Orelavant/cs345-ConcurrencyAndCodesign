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
	// INSTEAD OF SYNCHRONIZED, USE A MUTEX FOR METHODS WHICH HAVE TO BE SYNCHRONIZED WITH ITSELF BUT NOT OTHER METHODS.
	// Ask how to make last ape go last, can you make it sleep?
	// Instead of doing last ape stuff, keep track of apes on ladder. Once apes are finished, you can release.
	private int[] rungCapacity;
	private Semaphore[] semArr;
	private ArrayList<Ape> eastLadderq;
	private ArrayList<Ape> westLadderq;
	private static final int CAP = 5; // # of apes who can go next on the ladder
	private boolean ladderAvail;
	private int apeCount; // # of apes on ladder.

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
		apeCount = 0;
	}

	public synchronized int getApeCount() {
		return apeCount;
	}

	public synchronized void addApeCount(int n) {
		apeCount += n;
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
		int count;
		Ape currApe;
		if (q.size() >= CAP) {
			count = CAP;
		} else {
			count = q.size();
		}
		for(int i = 0; i < count; i++) {
			currApe = q.get(0);
			currApe.setCanCross(true);
			q.remove(0); // SLOW, WILL CHANGE TO LINKED LIST.
		}
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

	public synchronized void addEastLadderq(Ape ape) {
		eastLadderq.add(ape);
	}
	
	public synchronized void addWestLadderq(Ape ape) {
		westLadderq.add(ape);
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
