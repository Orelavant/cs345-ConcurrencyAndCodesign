To prevent a rung being grabbed by multiple apes:
An array of binary semaphores is initialized to the length of the # of ladder rungs. Apes must 
acquire a rung permit for their desired rung before grabbing it.

To prevent deadlock when an east and west ape collide:
Initial Idea:
A binary semaphore for the entire ladder is used to signal when an east ape or west ape is crossing.
That way, multiple apes can only cross with their fellow apes. To prevent starvation, a limit is set
to the number of apes that cross from one side at a time, so that the other side gets a chance to
cross too.

What was implemented after many tries:
Assuming that we cannot know how many apes exist (i.e. by grabbing the number from jungle).