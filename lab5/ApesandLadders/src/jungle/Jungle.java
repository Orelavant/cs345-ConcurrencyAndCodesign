/**
 * @author davew
 *
 * This class just exists to create the objects and threads we need: One ladder and many apes.
 * You should not need to change anything here unless you want to use it to add other objects that 
 * aren't associated with some existing object (an ape or ladder).
 */
public class Jungle {	
	public static void main(String[] args) {
		int    eastBound = 0; // how many apes going East? use -1 for inifinity
		int    westBound = 3; // how many apes going West? use -1 for inifinity
		double apeMin = 1.0;  // how long to wait between consecutive apes going one way
		double apeVar = 1.0;  // 4 seconds is usually enough, but vary a bit to see what happens
		
		// create a Ladder
		Ladder l = new Ladder(4);
		
		// create some Eastbound and Westbound apes who want that ladder
		apesRun(eastBound, westBound, l, apeMin, apeVar);
	}

	// random number generator, for delays mostly
	private static java.util.Random dice = new java.util.Random(); 	
	public static void tryToSleep(double secMin, double secVar) {
        try {
            java.lang.Thread.sleep(Math.round(secMin*1000) + 
			Math.round(dice.nextDouble()*(secVar)*1000));
        } catch (InterruptedException e) {
            System.out.println("Not Handling interruptions yet ... " +
			"just going on with the program without as much sleep as needed ... how appropriate!");
        }
	}

	// Alternates creating eastBound and westBound apes
    public static void apesRun(int eastBound, int westBound, Ladder ladder, double apeMin, 
	double apeVar) {
        int nRemaining = eastBound + westBound;
		int eastApeCounter = 1;
		int westApeCounter = 1;
		boolean eastTurn;
		if (eastBound > 0) {
			eastTurn = true;
		} else {
			eastTurn = false;
		}
		while (nRemaining != 0) {
            if (eastBound > 0 && eastTurn){
                Ape a = new Ape("E-"+eastApeCounter, ladder,true);
                a.start();
				eastApeCounter++;

				if (westBound > 0){eastTurn = !eastTurn;}
            } else if (westBound > 0 && !eastTurn) {
                Ape a = new Ape("W-"+westApeCounter, ladder,false);
                a.start();
				westApeCounter++;

				if (eastBound > 0){eastTurn = !eastTurn;}
            }
            nRemaining--;
			//tryToSleep(apeMin, apeVar);
		}
    }
}
