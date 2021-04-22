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
	private int rungCapacity[];
	private Semaphore[] semArr;
	private Semaphore eastBlock; // East apes are blocked from using ladder
	private Semaphore westBlock; // West apes are blocked from using ladder

	public Ladder(int _nRungs) {
		rungCapacity = new int[_nRungs];
		semArr = new Semaphore[_nRungs];
		// capacity 1 available on each rung
		for (int i=0; i<_nRungs; i++) {
            rungCapacity[i] = 1;
			Semaphore sem = new Semaphore(1, true); 
			semArr[i] = sem;
        }
		eastBlock = new Semaphore(1, true);
		westBlock = new Semaphore(1, true);	
	}

	public Semaphore getEastBlock() {
		return eastBlock;
	}

	public Semaphore getWestBlock() {
		return westBlock;
	}

	public boolean tryAcquireEastBlock(){
		return eastBlock.tryAcquire();
	}

	public boolean tryAcquireWestBlock(){
		return westBlock.tryAcquire();
	}

	public void releaseEastBlock(){
		eastBlock.release();
	}

	public void releaseWestBlock(){
		westBlock.release();
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
