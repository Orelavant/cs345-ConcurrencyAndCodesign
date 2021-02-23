// Jacob Valero

public class FibCon2 extends Thread {
    
    int n;
    int result;

    public FibCon2(int n) {
        this.n = n;
    }

    // Originally thread start contained in main which only forked once. By moving over forking to
    // the fib algorithm, multiple threads can be made.
    @Override
    public void run() {
        if (n <= 2) {
            result = 1;
        } else {
            FibCon2 f1 = new FibCon2(n-1);
            FibCon2 f2 = new FibCon2(n-2);
            f1.start();
            f2.start();
            try {
                f1.join();
                f2.join();
            } catch (InterruptedException e){};
            result = f1.getResult() + f2.getResult();
        }
	}

	public int getResult() {
		return result;
	}

    // Execution of Fib recursive algorithm through multiple threads.
    // Time measuring code: https://www.techiedelight.com/measure-elapsed-time-execution-time-java/
	public static void main(String[] args) {
        // Getting input either here or from args.
        // int input = Integer.parseInt(args[0]);
        int input = 20;

        // Starting and ending fib while measuring time.
		FibCon2 f1 = new FibCon2(input);
        long startTime = System.nanoTime();
        f1.start();
        try {
            f1.join();
        } catch (InterruptedException e){};
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;

        // Printouts
        System.out.println("Paradigm: Concurrent 2");
        System.out.println("Fibonacci Number: " + input);
		System.out.println("Answer: " + f1.getResult());
        System.out.println("Execution time in milliseconds: " +
                                timeElapsed / 1000000);
	}
}