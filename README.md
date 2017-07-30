# Shape Wars
Shape Wars is a 2-player desktop battle game written in Java inspired by Super Smash Bros. Brawl and Rocket League.

## Instructions
If the instructions in the game are not clear enough, here is an explication of this game's operations:

### General
1. When Player A's vertex collides with Player B's side, Player A inflicts damage upon Player B.
2. After two players collide, there is a two second grace period where neither player can inflict
   damage upon the other. This is indicated by a faded color and a timer in the middle of the screen.
   
#### Help Screen
* Press the '?' button in the top right corner of the menu screen for help

#### Side Mania
* To play Side Mania, click on the right side of the menu's image.
* Click on the shape each player will spawn as. I recommend beginners start with no more than 6 sides.
* When Player A's vertex collides with one of Player B's sides, Player B loses a side.
* First one to get hit while his shape has 3 sides loses.
* If a player collides with a Green circle, he gains a side.

#### Damage Battalion
* To play Damage Battalion, click on the left side of the menu's image.
* Click on the shape each player will spawn as. I recommend beginners start with 3 or 4.
* Each player's damage is indicated by the damage bars at the top of the window.
* When Player A's vertex collides with one of Player B's sides, Player B's damage is increased
  by Player A's velocity.
* First player who reaches or exceeds 100 damage points loses.
* If a player collides with a Red circle, he receives -5 damage points.

### Prerequisites
Java

## Authors 
* **Ward Bradt** - *Initial work* - [wardbradt](https://github.com/wardbradt)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgements/ Citations
* Stephen Russell, my computer science teacher, wrote most of the Game4 class.
* Loosely inspired by Super Smash Bros. Brawl and Rocket League
* https://stackoverflow.com/questions/223918/iterating-through-a-collection-avoiding-concurrentmodificationexception-when-re
* https://stackoverflow.com/questions/258486/calculate-the-display-width-of-a-string-in-java
* https://stackoverflow.com/questions/15690846/java-collision-detection-between-two-shape-objects
* https://gist.github.com/PurpleBooth/109311bb0361f32d87a2
