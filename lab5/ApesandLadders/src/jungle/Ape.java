import java.util.concurrent.*;

import jdk.internal.jshell.tool.resources.l10n_ja;

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
	private boolean printed;
	private boolean canCross; // Set to true if able to cross while a friendly ape is crossing.
	public boolean lastApe; // Last ape to cross in a bunch, will be the one to give ladder access back.
	
	public Ape(String name, Ladder toCross, boolean goingEast) {
		_name = name;
		_ladderToCross = toCross;
		_goingEast = goingEast;
		printed = true;
		canCross = false;
		lastApe = false;
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
		
		// Signaling to want start rung and checking ladder use if opposite apes are crossing.
		block(_goingEast, _ladderToCross);
		System.out.println("Ape " + _name + " wants rung " + startRung);

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

		// Releasing end rung. Check other q, if monkeys are ready to go, let them go.
		// If not, ladder is now available to next monkey.
		_ladderToCross.releaseRung(endRung);
		if (lastApe) {
			if (_goingEast) {
				if (!_ladderToCross.getWestLadderq().isEmpty()){
					_ladderToCross.sendApes(!_goingEast);
				} else {
					_ladderToCross.setLadderAvail(true);
				}
			} else {
				if (!_ladderToCross.getEastLadderq().isEmpty()){
					_ladderToCross.sendApes(_goingEast);
				} else {
					_ladderToCross.setLadderAvail(true);
				}
			}
		}
		System.out.println("Ape " + _name + " releasing " + endRung);
		System.out.println("Ape " + _name + " finished going " + (_goingEast?"East.":"West."));
	}

	public boolean getCanCross() {
		return canCross;
	}

	public void setCanCross(boolean bool) {
		canCross = bool;
	}

	// Checking ladder use if opposite apes are crossing.
	private void block(boolean goingEast, Ladder l) {
		// Check if ladder is available, if so, block ladder use from opposing apes.
		if (goingEast) {
			l.getEastLadderq().add(this);

			// CHANGE THIS TO A METHOD CALLED WAIT() AND PRINT CURRENT Q
			// If you can cross or if ladder is available, go.
			// In other words, if you can't cross and if ladder isn't available, stay.
			printed = true;
			while(!canCross && !l.getAndSetLadderAvail()) {
				if (printed) {
					System.out.println("Ape " + _name + " is waiting for permission to go...");
				}
				printed = false;
			}

			// Send apes from q if you got the ladder.
			if (!canCross){
				l.sendApes(goingEast); 
			}
		} else {
			l.getWestLadderq().add(this);

			// If you can cross or if ladder is available, go.
			// In other words, if you can't cross and if ladder isn't available, stay.
			printed = true;
			while(!canCross && !l.getAndSetLadderAvail()) {
				if (printed) {
					System.out.println("Ape " + _name + " is waiting for permission to go...");
				}
				printed = false;
			}

			// Send apes from q if you got the ladder.
			if (!canCross){
				l.sendApes(!goingEast); 
			}
		}
	}
}
