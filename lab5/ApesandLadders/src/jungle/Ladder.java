package jungle;
import java.util.concurrent.*;
import java.util.ArrayList;

/**
 * @author davew
 *
 * The Ladder class is NOT a kind of thread, since it doesn't actually do anything except get used 
 * by Apes. The ladder just keeps track of how many apes are on each rung.
 */
public class Ladder {
	private int rungCapacity[];
	private Semaphore[] semArr;
	
	public Ladder(int _nRungs) {
		rungCapacity = new int[_nRungs];
		semArr = new Semaphore[_nRungs];
		// capacity 1 available on each rung
		for (int i=0; i<_nRungs; i++) {
            rungCapacity[i] = 1;
			Semaphore sem = new Semaphore(1, true); 
			semArr[i] = sem;
        }	
	}
	
    public int nRungs() {
		return rungCapacity.length;
	}

	// return True if you succeed in grabbing the rung
	public boolean grabRung(int which) {
		return semArr[which].tryAcquire();
	}

	public void releaseRung(int which) {
		semArr[which].release();
	}
}
