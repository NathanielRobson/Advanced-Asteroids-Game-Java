# Advanced-Asteroids-Game-Java

My take on a well known classic 'Asteroids', produced by myself at the University of Essex.  

![Asteroid Action](/Images/Asteroidt.jpg)


## How to run my game
Navigate to the java file named ‘Game’ which can be found here:  ‘dAsteroids/src/AsteroidsFinal/Game/Game.java’  
The main method is in this class so simply run this (Right click, Run. Or green play button at the top of your IDE).  
Ensure that the project structure output file has been specified as the ‘out’ file and that the project SDK has been selected. 

## Key Bindings
W or UP: Thrust Ship  
A or Left: Rotate Ship Left  
D or Right: Rotate Ship Right  
T: Teleport Ship  
Spacebar: Fire Weapon  
ESC: Exit Game  
R: Restart Game  
Enter: Begin Game  
P: Toggle Pause Game  
H: Toggle Help/Controls In-Game Overlay 

### Game Story  
Advanced Asteroids is a spaced-theme multi-directional shooter computer game. The player controls a modern looking 2D spaceship in an asteroid field, not far from earth. The player will have to defend themselves from waves of asteroids, unfortunately for the player it gets harder as they progress. The player will go up against multiple alien enemies at once and must finish the game by reaching level 4. This is achieved by clearing the asteroids and finally destroying the mothership. Play for fun or play competitively to try and tackle your friends high scores. Pickup specialised powerups for an added twist to the classic game Asteroids. Watch out for the death field, marked by a light blue ring.

### Design Choices  
I decided to improve on the original Asteroids game on areas where it was lacking such as visuals, fluidity of gameplay, replayability and overall fun. Intentionally I used bright colors to portray each in game object such as the player ship to allow the game to feel more inviting to all ages. I also included a looped soundtrack which makes the game more immersive.  My game has powerups which when enemies or asteroids are destroyed, a random interval generates the chance of a new powerup spawning. This makes the game more exciting because you never know what you are going to get next. It could either be ‘CHAINFIRE’, ‘SHIELD’ or ‘EXTRA LIFE’. Each of these are custom created PNG images, which I designed in Photoshop. I also took the time to customize thoroughly the spaceships used in my game such as when the player thrusts, a thruster sprite is added to the game which improves gameplay. I decided to be very thorough when programming the game, 
many, many hours were spent on ensuring that the game ran smoothly, and no exceptions are thrown while playing. Each level has had the luxury of extensive testing and error checking.  I also implemented custom sounds which even some of them were edited to suit the game further. For example, the bullet firing sound was generic and loud, so I used another, but upon adding it to my game I realized it was too loud. To fix this I had to customize this in an external audio editor to ensure that it sounded great. Each powerup and action in game has a custom sound added. 
