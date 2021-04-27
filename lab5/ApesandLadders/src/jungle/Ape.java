/**
 * @author davew
 *
 * The Ape class is a kind of thread, since all Apes can go about their activities concurrently.
 * Note that each Ape has his or her own name and direction, but in this system, many Apes will 
 * share one Ladder.
 */
public class Ape extends Thread {
	/*
	* rungDelayMin: Minimum delay between grabbing rungs.
	* rungDelayVar: Variation in time between grabbing rungs.
	* _name: Name of ape.
	* _ladderToCross: Ladder being used to cross.
	* _goingEast: Whether or not the ape is heading east.
	* printed: Prevents multiple "waiting..." print messages from printing.
	* canCross: Whether an ape waiting in q has been given the signal to cross or not.
	*/ 
	private static final double rungDelayMin = 0.8;
	private static final double rungDelayVar = 1.0;
	private String _name;
	private Ladder _ladderToCross;
	private boolean _goingEast;
	private boolean printed;
	private boolean canCross;
	
	public Ape(String name, Ladder toCross, boolean goingEast) {
		_name = name;
		_ladderToCross = toCross;
		_goingEast = goingEast;
		printed = true;
		canCross = false;
	}

	public boolean getCanCross() {
		return canCross;
	}

	public void setCanCross(boolean bool) {
		canCross = bool;
	}
	
	@Override
	public void run() {
		int startRung; 
        int move; 
        int endRung;
		System.out.println("Ape " + _name + " wants to go " + (_goingEast?"East.":"West."));

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
		
		// If ladder available: go, bring fellow apes in queue, and block ladder. If not, wait in q.
		blockAndSend(_goingEast, _ladderToCross);

		// Attempt at grabbing start rung, wait if neccesary.
		_ladderToCross.addApeCount(1);
		System.out.println("Ape " + _name + " wants rung " + startRung);
		rungWait(startRung, _ladderToCross);
		System.out.println("Ape " + _name + "  got  rung " + startRung);

		// Move across rest of rungs when possible.
		for (int i = startRung+move; i!=endRung+move; i+=move) {
			// Delay, check rung availability and wait if neccesary, continue when possible.
			Jungle.tryToSleep(rungDelayMin, rungDelayVar);
			System.out.println("Ape " + _name + " wants rung " + i);
			rungWait(i, _ladderToCross);
			_ladderToCross.releaseRung(i-move);
			System.out.println("Ape " + _name + "  got  " + i + " releasing " + (i-move));
		}

		// Releasing end rung.
		_ladderToCross.releaseRung(endRung);
		_ladderToCross.addApeCount(-1);
		System.out.println("Ape " + _name + " releasing " + endRung);
		System.out.println("Ape " + _name + " finished going " + (_goingEast?"East.":"West."));

		// If last ape to cross, check qs for monkeys ready to go, with priority towards opposing q.
		qCheck();
	}

	// Checking ladder use and waiting if neccesary.
	private void blockAndSend(boolean goingEast, Ladder l) {
		if (goingEast) {
			l.addEastLadderq(this);
			qWait(l);

			// Send apes from q if you got the ladder first.
			if (!canCross){
				l.sendApes(goingEast); 
			}

		} else {
			l.addWestLadderq(this);
			qWait(l);

			// Send apes from q if you got the ladder first.
			if (!canCross){
				l.sendApes(!goingEast); 
			}
		}
	}

	// When last ape crosses, check opposing q for apes. If none there, check its q. 
	// If none at all available, ladder is now available.
	private void qCheck() {
		if (_ladderToCross.getApeCount() == 0) {
			if (_goingEast) {
				if (!_ladderToCross.getWestLadderq().isEmpty()) {
					_ladderToCross.sendApes(!_goingEast);
					System.out.println("Ape " + _name + " released west apes who were waiting.");
				} else if (!_ladderToCross.getEastLadderq().isEmpty()) {
					_ladderToCross.sendApes(_goingEast);
					System.out.println("Ape " + _name + " released east apes who were waiting.");
				} else {
					_ladderToCross.setLadderAvail(true);
					System.out.println("The ladder is available to the next ape who wants it");
				}
			} else {
				if (!_ladderToCross.getEastLadderq().isEmpty()) {
					_ladderToCross.sendApes(!_goingEast);
					System.out.println("Ape " + _name + " released east apes who were waiting.");
				} else if (!_ladderToCross.getWestLadderq().isEmpty()) {
					_ladderToCross.sendApes(_goingEast);
					System.out.println("Ape " + _name + " released west apes who were waiting.");
				} else {
					_ladderToCross.setLadderAvail(true);
					System.out.println("The ladder is available to the next ape who wants it");
				}
			}
		}
	}

	// When ladder is in use, wait in respective q.
	private void qWait(Ladder l) {
		// If ape can cross or if ladder is available, go.
		// Used a synchronized boolean instead of a sempahore because not all apes who
		// wait should wait for semaphore. Only first ape cares about ladder availability.
		// Using wait() and notify() would be more optimal but I had trouble implementing that.
		printed = true;
		while(!canCross && !l.getAndSetLadderAvail()) {
			if (printed) {
				System.out.println("Ape " + _name + " is waiting for permission to go...");
			}
			printed = false;
		}
	}

	// When next rung is in use, wait.
	private void rungWait(int rung, Ladder l) {
		// Using wait() and notify() would be more optimal but I had trouble implementing that.
		printed = true;
		while (!l.tryGrabRung(rung)) {
			if (printed) {
				System.out.println("Ape " + _name + " did not get rung " + rung + " will wait...");
			}
			printed = false;
		}
	}
}