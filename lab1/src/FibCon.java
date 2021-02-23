// Jacob Valero

public class FibCon extends Thread {
    
    int n;
    int result;

    public FibCon(int n) {
        this.n = n;
        result = 0;
    }

    @Override
    public void run() {
        result = fibCon(n);
	}

    private int fibCon(int n) {
        if (n <= 2) {
            return 1;
        } else {
            return fibCon(n-1) + fibCon(n-2);
        }
    }

	public int getResult() {
		return result;
	}

    // Execution of Fib recursive algorithm through two threads.
    // Time measuring code: https://www.techiedelight.com/measure-elapsed-time-execution-time-java/
	public static void main(String[] args) {
        //int input = Integer.parseInt(args[0]);
        int input = 20;
        int n1 = input - 1;
        int n2 = input - 2;
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