package AsteroidsFinal.GameObjects.Ships;

import AsteroidsFinal.Control.Action;
import AsteroidsFinal.Game.Sprite;
import AsteroidsFinal.GameObjects.GameObject;
import utilities.Vector2D;

import java.awt.*;

//Helped pod
public class HelperPod extends Ship {

    private int SCALE = 3;

    public HelperPod(Vector2D position, Vector2D velocity, double radius, Action action) {
        super(position, velocity, radius, action);

    }

    @Override
    public void draw(Graphics2D g) {
        Sprite imgship = new Sprite(Sprite.PLAYER_HELPER, position, new Vector2D(direction
        ).rotate(1.57), radius * SCALE, radius * SCALE);
        imgship.draw(g);

    }

    @Override
    public boolean canHit(GameObject other) {
        return false;
    }
}
