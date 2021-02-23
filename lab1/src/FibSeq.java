// Jacob Valero

// Sequential implementation of finding fibonacci numbers using recursion.
public class FibSeq {

    // Recursive fibonacci algorithm.
    private static int fibSeq(int n) {
        if (n <= 2) {
            return 1;
        } else {
            return fibSeq(n-1) + fibSeq(n-2);
        }
    }

    // Main: Calling the fib function, measuring output and resopnse time, and printing results.
    public static void main(String[] args) {
        //int input = Integer.parseInt(args[0]);
        int input = 20;

        long startTime = System.nanoTime();
        int answer = fibSeq(input);
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;

        // Printouts
        System.out.println("Paradigm: Sequential");
        System.out.println("Fibonacci Number: " + input);
		System.out.println("Answer: " + answer);
        System.out.println("Execution time in milliseconds: " +
                                timeElapsed / 1000000);
    }
}
