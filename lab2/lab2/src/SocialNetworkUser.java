// Jacob Valero

public class SocialNetworkUser implements Runnable{
    
    /* Fields:
    * @count: # of posts from this user.
    * @name: ID of this user.
    * @limit: The number of times a user will post and view posts.
    */
    long count;
    String name;
    SocialNetwork network;
    int limit;

    // Constructor
    public SocialNetworkUser(String name, SocialNetwork network) {
        count = 0;
        limit = 10;
        this.name = name;
        this.network = network;
    }

    // Run
    @Override
    public void run() {
        System.out.println(name + " has joined and " + "can't wait to get their feet wet!");
        
        // Adding and viewing posts with random second intervals inbetween.
        while(count < limit){
            network.addPost(this);
            nap();
            System.out.println(network.viewPosts(this));
            nap();
        }

        System.out.println(name + " has finished for the day. See you again soon!");
    }

    public long getCount() {
        return count;
    }

    public String getID() {
        return name;
    }

    // Contained method of java.sleep
    private static void nap() {
        try {
            Thread.sleep((int)(Math.random() * 1000));
        } catch (InterruptedException e) {}
    }

    // Checks if count has hit max value. If not, increment count.
    public void countInc() {
        if (count == Long.MAX_VALUE) {
            throw new IllegalStateException("counter overflow");
        }
        count++;
    }
}
