/*
Jacob Valero
Reflection: As written below, every iteration of the loop will stop for a random amount of seconds. 
This is why we have variable print results as both threads run and then pause at variable intervals.
When we replace random with a constant, we don't get variable stopping times so the print results
do not vary as much. From experimentation, with a constant the loop iterations will only vary to the
degree that one thread may print twice before the other thread prints again, but never more than 
that. This is the same for a function that returns a constant and seems to be the case with more
than two threads as well. However, when I used the runnable interface without sleeping for a 
constant amount of time, some threads would finish printing much earlier than others, which was a
suprise.
*/

/*
 * Thread Example from
 * http://journals.ecs.soton.ac.uk/java/tutorial/java/threads/simple.html 
 */

class SimpleThread extends Thread {

    public SimpleThread(String str) {
        super(str);
    }

    public void run() {
        // Every iteration, the loop stops for a random amount of time. 
        // So, variable print results as both threads run and then pause at variable intervals.
        for (int i = 0; i < 10; i++) {
            System.out.println(i + " " + getName());
            try {
                sleep((int)(Math.random() * 1000));
            } catch (InterruptedException e) {}
        }
        System.out.println("DONE! " + getName());
    }

    // A function that returns a constant of 4 to the wait time.
    public int addTo4() {
        return 2+2;
    }
}

class Please {
    public static void main (String[] args) {
        new SimpleThread("Jamaica").start();
        new SimpleThread("Fiji").start();
        new SimpleThread("Cuba").start();
        
        // Source: https://www.javatpoint.com/runnable-interface-in-java
        // Runnable r1 = new RunnableThread("Jamaica");
        // Runnable r2 = new RunnableThread("Fiji");
        // Runnable r3 = new RunnableThread("Cuba"); 
        // Thread t1 = new Thread(r1);
        // Thread t2 = new Thread(r2);
        // Thread t3 = new Thread(r3);
        // t1.start();
        // t2.start();
        // t3.start();
    }
}

// Similar to SimpleThread but implements the Runnable interface instead of Thread.
class RunnableThread implements Runnable { 
    private String name;

    public RunnableThread(String str) {
        name = str;
    }
  
    public void run() { 
        for (int i = 0; i < 10; i++) {
            System.out.println(i + " " + name);
        }
        System.out.println("DONE! " + name); 
    } 
}


