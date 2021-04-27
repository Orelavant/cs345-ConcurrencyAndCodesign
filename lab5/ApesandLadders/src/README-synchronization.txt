To prevent a rung being grabbed by multiple apes:
An array of binary semaphores is initialized to the length of the # of ladder rungs. Apes must 
acquire a rung permit for their desired rung before grabbing it.

To prevent deadlock when an east and west ape collide:
Initial Idea:
A binary semaphore for the entire ladder is used to signal when an east ape or west ape is crossing.
That way, multiple apes can only cross with their fellow apes. To prevent starvation, a limit is set
to the number of apes that cross from one side at a time, so that the other side gets a chance to
cross too.

What was implemented after many iterations:
A boolean whose access methods are synchronized is used to signal when the ladder is completly free 
(i.e. not apes are waiting in any queues for ladder availability). If the ladder is free, then the 
first ape to request its use will get it. If the ladder is not available, apes will enter queues for
the sides they are on (i.e. a queue for the eastbound apes and a queue for the westbound apes). 
When the last ape on the ladder exits the ladder at any time, the queue opposite of the last ape's 
direction will be checked for apes and sent if any are there. If there are none, the other queue is 
checked for apes and the same steps are followed. Finally, if there areno apes in either queue, the 
ladder is made available again to the first ape who wants it. This approach is concurrently safe as 
access to locks are synchronized to their relevant objects (either the ladder or queues). It avoids
deadlock as it should not be possible for east and west apes to be on the ladder at the same time.
Finally it has optimal output as all of the apes that are ready to go from one side can go when the
ladder is free of apes from the opposite side. Of course it would be nice to always be outputting 
apes from the side with the greatest number of apes, but that could cause starvation. 