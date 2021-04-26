import java.util.concurrent.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author davew
 *
 * The Ladder class is NOT a kind of thread, since it doesn't actually do anything except get used 
 * by Apes. The ladder just keeps track of how many apes are on each rung.
 *
 * Sources for concurrent objects: 
 * https://stackoverflow.com/questions/3047564/java-synchronized-method-lock-on-object-or-method
 * https://stackoverflow.com/questions/38356636/concurrent-linkedlist-vs-concurrentlinkedqueue
 */ 
public class Ladder {
	/*
	* rungCapacity: Int array which is initialized to the # of rungs.
	* semArr: An array of semaphores initialized to the # of rungs.
	* eastLadderq: A synchronized queue of eastbound apes who are waiting for ladder usage.
	* westLadderq: A synchronized queue of westbound apes who are waiting for ladder usage.
	* ladderAvail: Bool which says when ladder available (when no apes are on ladder or in queue).
	* apeCount: An synchornized integer which states # of apes currently on the ladder.
	*/ 
	private int[] rungCapacity;
	private Semaphore[] semArr;
	private List<Ape> eastLadderq;
	private List<Ape> westLadderq;
	private boolean ladderAvail;
	private AtomicInteger apeCount;

	public Ladder(int _nRungs) {
		rungCapacity = new int[_nRungs];
		semArr = new Semaphore[_nRungs];

		// capacity 1 available on each rung
		for (int i=0; i<_nRungs; i++) {
            rungCapacity[i] = 1;
			Semaphore sem = new Semaphore(1, true); 
			semArr[i] = sem;
        }

		eastLadderq = Collections.synchronizedList(new LinkedList<>());
		westLadderq = Collections.synchronizedList(new LinkedList<>());
		ladderAvail = true;
		apeCount = new AtomicInteger();
	}

	public int getApeCount() {
		return apeCount.get();
	}

	// Adds n to the current ape count.
	public void addApeCount(int n) {
		apeCount.addAndGet(n);
	}

	// Send apes across ladder who are currently waiting in q.
	public synchronized void sendApes(boolean eastApes) {
		// Set which q
		List<Ape> q;
		if (eastApes) {
			q = eastLadderq;
		} else {
			q = westLadderq;
		}

		// Send apes currently in q.
		int count = q.size();
		Ape currApe;
		for(int i = 0; i < count; i++) {
			currApe = q.get(0);
			currApe.setCanCross(true);
			q.remove(0);
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

	public void setLadderAvail(boolean bool){
		ladderAvail = bool;
	}

	public List<Ape> getEastLadderq() {
		synchronized (eastLadderq) {
			return eastLadderq;
		}
	}

	public List<Ape> getWestLadderq() {
		synchronized (westLadderq) {
			return westLadderq;
		}
	}

	// Adds an ape to the east ladder queue.
	public void addEastLadderq(Ape ape) {
		synchronized (eastLadderq) {
			eastLadderq.add(ape);
		}
	}
	
	// Adds an ape to the west ladder queue.
	public void addWestLadderq(Ape ape) {
		synchronized (westLadderq) {
			westLadderq.add(ape);
		}
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
