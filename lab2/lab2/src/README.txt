Jacob Valero

Organization: The SocialNetwork class contains a LinkedList which will contain the six most recent 
posts. It also contains the methods used to add posts to this list (addPost()) and view them 
(viewPosts()). Each method requires a SocialNetworkUser object to be passed so that their 
information (name and number of posts) will be added to the LinkedList. The SocialNetworkUser class
simply contains getter methods for its fields, as well as a run() which will call addPost and 
viewPosts up to the number of times in the limit field with random pauses in between.

Reflection: I started this lab by considering what data structure would best represent this social
network. Because all the memory required in the social network was the six most recent posts, I
decided to use a LinkedList to contain the six most recent posts as it optimizes the runtime of 
adding and deleting posts from the front and end of the list respectively. We also don't need random
access to any elements in the list, which is usually the main factor in selecting an array or 
arrayList. All of the methods which modify and view this LinkedList, namely addPost and viewPosts, 
are synchronized as they may be used concurrently by N > 2 users. There are also catch statements 
in case the number of posts exceeds the limit for the long datatype, but at the moment each user
will only call addPost and viewPosts up to the number specified by the limit field in the 
SocialNetworkUser class.

