import java.util.*; 

public class SocialNetwork {
    
    /* Fields:
    * @posts: LinkedList containing the 6 most recent posts. 
    * @totalCount: Count of the # of posts so far.
    */
    LinkedList<String> posts;
    long totalCount;

    // Constructor
    public SocialNetwork() {
        posts = new LinkedList<>();
        totalCount = 0;
    }

    // Checks if totalCount has overflowed. If not, increment totalCount.
    private void increment() {
        if (totalCount == Long.MAX_VALUE) {
            throw new IllegalStateException("counter overflow");
        }
        totalCount++;
    }

    // Adds a post to the network.
    public synchronized void addPost(SocialNetworkUser u) {
        // 6 posts max.
        if (posts.size() >= 6) {
            posts.pop();
        }

        // Increment tallies.
        u.countInc();
        increment();

        // Add to list and printout
        posts.add(u.getID() + ":" + u.getCount());
        System.out.println("User " + u.getID() + " added a post! Post Count: " + u.getCount() + 
            " Total posts: " + totalCount);
    }

    // Displays a view of the six most recent posts.
    public synchronized String viewPosts(SocialNetworkUser u) {
        System.out.println("User " + u.getID() + " is requesting to view posts.");

        // Building string of six most recent posts.
        StringBuilder postView = new StringBuilder();
        for(String post : posts) {
            postView.append(post.toString() + "  ");
        }

        return postView.toString();
    }
}