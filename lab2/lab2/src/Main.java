/*  Questions:
        - Is thread ID just a string identifier of the thread (i.e. name?)
*/ 

public class Main {
    public static void main(String[] args) {
        // New social Network
        SocialNetwork feetbook = new SocialNetwork();

        // New Users
        SocialNetworkUser bill = new SocialNetworkUser("Bill", feetbook);
        SocialNetworkUser sue = new SocialNetworkUser("Sue", feetbook);
        SocialNetworkUser foo = new SocialNetworkUser("Foo", feetbook);
        SocialNetworkUser bar = new SocialNetworkUser("Bar", feetbook);

        // Starting threads
        Thread t1 = new Thread(bill);
        Thread t2 = new Thread(sue);
        Thread t3 = new Thread(foo);
        Thread t4 = new Thread(bar);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}
