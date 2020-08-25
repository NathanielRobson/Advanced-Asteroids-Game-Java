package AsteroidsFinal.Control;


import AsteroidsFinal.Game.Game;
import AsteroidsFinal.GameObjects.GameObject;

public interface EnemyController {
    Action action(Game game, GameObject object);

}