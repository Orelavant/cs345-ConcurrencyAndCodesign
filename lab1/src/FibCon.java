// Jacob Valero

public class FibCon extends Thread {
    
    /*
    n = The fibonacci number
    result = The result
    */
    int n;
    int result;

    // Constructor
    public FibCon(int n) {
        this.n = n;
        result = 0;
    }

    // Running Fib algorithm at start.
    @Override
    public void run() {
        result = fibCon(n);
	}

    // Recrusive fib algorithm.
    private int fibCon(int n) {
        if (n <= 2) {
            return 1;
        } else {
            return fibCon(n-1) + fibCon(n-2);
        }
    }

    // Public accessor for result.
	public int getResult() {
		return result;
	}

    // Execution of Fib recursive algorithm through two threads.
    // Time measuring code: https://www.techiedelight.com/measure-elapsed-time-execution-time-java/
	public static void main(String[] args) {
        // Getting input either here or from args.
        //int input = Integer.parseInt(args[0]);
        int input = 20;
        int n1 = input - 1;
        int n2 = input - 2;

        // Starting and ending fib while measuring time.
		FibCon f1 = new FibCon(n1);
        FibCon f2 = new FibCon(n2);
        long startTime = System.nanoTime();
		f1.start();
        f2.start();
		try {
			f1.join();
            f2.join();
		} catch (InterruptedException e){};
        int answer = f1.getResult() + f2.getResult();
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;

        // Printouts
        System.out.println("Paradigm: Concurrent 1");
        System.out.println("Fibonacci Number: " + input);
		System.out.println("Answer: " + answer);
        System.out.println("Execution time in milliseconds: " +
                                timeElapsed / 1000000);
	}
}