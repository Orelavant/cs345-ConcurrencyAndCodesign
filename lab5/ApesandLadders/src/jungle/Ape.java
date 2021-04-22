import java.util.concurrent.*;

/**
 * @author davew
 *
 * The Ape class is a kind of thread, since all Apes can go about their activities concurrently.
 * Note that each Ape has his or her own name and direction, but in this system, many Apes will 
 * share one Ladder.
 */
public class Ape extends Thread {
	static private final double rungDelayMin = 0.8;
	static private final double rungDelayVar = 1.0;
	private String _name;
	private Ladder _ladderToCross;
	private boolean _goingEast; // if false, going west
	boolean printed;
	
	public Ape(String name, Ladder toCross, boolean goingEast) {
		_name = name;
		_ladderToCross = toCross;
		_goingEast = goingEast;
		printed = true;
	}
	
	@Override
	public void run() {
		int startRung; 
        int move; 
        int endRung;
		System.out.println("Ape " + _name + " starting to go " + (_goingEast?"East.":"West."));

		// Start, move, and end for west and eastbound apes.
		if (_goingEast) {
			startRung = 0;
			endRung = _ladderToCross.nRungs()-1;
			move = 1;
		} else {
			startRung = _ladderToCross.nRungs()-1;
			endRung = 0;
			move = -1;
		}
		
		// Signaling to want start rung and checking ladder use if opposite apes are crossing.
		System.out.println("Ape " + _name + " wants rung " + startRung);
		block(_goingEast);

		// If ladder available, attempt at grabbing start rung.
		while (!_ladderToCross.tryGrabRung(startRung)) {
			if (printed) {
				System.out.println("Ape " + _name + " did not get  rung " + startRung + 
				" will wait...");
			}
			printed = false;
		}
		System.out.println("Ape " + _name + "  got  rung " + startRung);

		// Move across rest of rungs when possible.
		for (int i = startRung+move; i!=endRung+move; i+=move) {
			Jungle.tryToSleep(rungDelayMin, rungDelayVar);
			System.out.println("Ape " + _name + " wants rung " + i);
			printed = true;			
			while (!_ladderToCross.tryGrabRung(i)) {
				if (printed) {
					System.out.println("Ape " + _name + "  did not get  " + i + " will wait...");
				}
				printed = false;
			}
			_ladderToCross.releaseRung(i-move);
			System.out.println("Ape " + _name + "  got  " + i + " releasing " + (i-move));
		}

		// Releasing end rung and signaling ladder use is done.
		_ladderToCross.releaseRung(endRung);
		if(_goingEast) {
			_ladderToCross.releaseWestBlock();
		} else {
			_ladderToCross.releaseEastBlock();
		}
		System.out.println("Ape " + _name + " releasing " + endRung);
		System.out.println("Ape " + _name + " finished going " + (_goingEast?"East.":"West."));
	}

	// Checking ladder use if opposite apes are crossing.
	private void block(boolean goingEast) {
		// Seeing if ladder is being used by an opposite crossing ape.
		if (goingEast) {
			// Wait until west apes cross.
			printed = true;
			// THIS DOES NOT WORK, GO BACK TO PAPER.
			while(_ladderToCross.getEastBlock().availablePermits() == 0) {
				if (printed) {
					System.out.println("West apes are crossing, ape " + _name + " will wait...");
				}
				printed = false;
			}

			// Block west apes. Can continue if unsuccesful since an east ape acquired it.
			_ladderToCross.tryAcquireWestBlock();
		} else {
			// Wait until east apes cross.
			printed = true;
			while(_ladderToCross.getWestBlock().availablePermits() == 0) {
				if (printed) {
					System.out.println("East apes are crossing, ape " + _name + " will wait...");
				}
				printed = false;
			}

			// Block east apes. Can continue if unsuccesful since an west ape acquired it.
			_ladderToCross.tryAcquireEastBlock();
		}
	}
}
